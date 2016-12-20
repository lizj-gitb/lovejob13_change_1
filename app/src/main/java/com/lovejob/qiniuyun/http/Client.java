package com.lovejob.qiniuyun.http;

import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.Domain;
import com.lovejob.qiniuyun.common.Constants;
import com.lovejob.qiniuyun.storage.UpCancellationSignal;
import com.lovejob.qiniuyun.utils.AsyncRun;
import com.lovejob.qiniuyun.utils.StringMap;
import com.lovejob.qiniuyun.utils.StringUtils;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.http.okhttp3.Callback;
import com.v.rapiddev.http.okhttp3.Dns;
import com.v.rapiddev.http.okhttp3.HttpUrl;
import com.v.rapiddev.http.okhttp3.Interceptor;
import com.v.rapiddev.http.okhttp3.MediaType;
import com.v.rapiddev.http.okhttp3.MultipartBody;
import com.v.rapiddev.http.okhttp3.OkHttpClient;
import com.v.rapiddev.http.okhttp3.Request;
import com.v.rapiddev.http.okhttp3.RequestBody;
import com.v.rapiddev.http.okhttp3.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.lovejob.qiniuyun.http.ResponseInfo.NetworkError;

/**
 * Created by bailong on 15/11/12.
 */
public final class Client {
    public static final String ContentTypeHeader = "Content-Type";
    public static final String DefaultMime = "application/octet-stream";
    public static final String JsonMime = "application/json";
    public static final String FormMime = "application/x-www-form-urlencoded";
    private final UrlConverter converter;
    private OkHttpClient httpClient;

    public Client() {
        this(null, 10, 30, null, null);
    }

    public Client(ProxyConfiguration proxy, int connectTimeout, int responseTimeout, UrlConverter converter, final DnsManager dns) {
        this.converter = converter;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (proxy != null) {
            builder.proxy(proxy.proxy());
            if (proxy.user != null && proxy.password != null) {
                builder.proxyAuthenticator(proxy.authenticator());
            }
        }
        if (dns != null) {
            builder.dns(new Dns() {
                @Override
                public List<InetAddress> lookup(String hostname) throws UnknownHostException {
                    InetAddress[] ips;
                    try {
                        ips = dns.queryInetAdress(new Domain(hostname));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new UnknownHostException(e.getMessage());
                    }
                    if (ips == null) {
                        throw new UnknownHostException(hostname + " resolve failed");
                    }
                    List<InetAddress> l = new ArrayList<>();
                    Collections.addAll(l, ips);
                    return l;
                }
            });
        }
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                final long before = System.currentTimeMillis();
                Response response = chain.proceed(request);
                final long after = System.currentTimeMillis();

                ResponseTag tag = (ResponseTag) request.tag();
                String ip = "";
                try {
                    ip = chain.connection().socket().getRemoteSocketAddress().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tag.ip = ip;
                tag.duration = after - before;
                return response;
            }
        });

        builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        builder.readTimeout(responseTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(0, TimeUnit.SECONDS);
        httpClient = builder.build();

    }

    private static String via( Response response) {
        String via;
        if (!(via = response.header("X-Via", "")).equals("")) {
            return via;
        }

        if (!(via = response.header("X-Px", "")).equals("")) {
            return via;
        }

        if (!(via = response.header("Fw-Via", "")).equals("")) {
            return via;
        }
        return via;
    }

    private static String ctype( Response response) {
        MediaType mediaType = response.body().contentType();
        if (mediaType == null) {
            return "";
        }
        return mediaType.type() + "/" + mediaType.subtype();
    }

    private static JSONObject buildJsonResp(byte[] body) throws Exception {
        String str = new String(body, Constants.UTF_8);
        // 允许 空 字符串
        if (StringUtils.isNullOrEmpty(str)) {
            return new JSONObject();
        }
        return new JSONObject(str);
    }

    public void asyncSend(final Request.Builder requestBuilder, StringMap headers, final CompletionHandler complete) {
        if (headers != null) {
            headers.forEach(new StringMap.Consumer() {
                @Override
                public void accept(String key, Object value) {
                    requestBuilder.header(key, value.toString());
                }
            });
        }

        requestBuilder.header("User-Agent", UserAgent.instance().toString());
        ResponseTag tag = new ResponseTag();
        httpClient.newCall(requestBuilder.tag(tag).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                int statusCode = NetworkError;
                String msg = e.getMessage();
                if (e instanceof CancellationHandler.CancellationException) {
                    statusCode = ResponseInfo.Cancelled;
                } else if (e instanceof UnknownHostException) {
                    statusCode = ResponseInfo.UnknownHost;
                } else if (msg != null && msg.indexOf("Broken pipe") == 0) {
                    statusCode = ResponseInfo.NetworkConnectionLost;
                } else if (e instanceof SocketTimeoutException) {
                    statusCode = ResponseInfo.TimedOut;
                } else if (e instanceof java.net.ConnectException) {
                    statusCode = ResponseInfo.CannotConnectToHost;
                }

                HttpUrl u = call.request().url();
                ResponseInfo info = new ResponseInfo(null, statusCode, "", "", "", u.host(), u.encodedPath(), "", u.port(), 0, 0, e.getMessage());

                complete.complete(info, null);
            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {
                ResponseTag tag = (ResponseTag) response.request().tag();
                onRet(response, tag.ip, tag.duration, complete);
            }
        });
    }

    public void asyncPost(String url, byte[] body,
                          StringMap headers, ProgressHandler progressHandler,
                          CompletionHandler completionHandler, UpCancellationSignal c) {
        asyncPost(url, body, 0, body.length, headers, progressHandler, completionHandler, c);
    }

    public void asyncPost(String url, byte[] body, int offset, int size,
                          StringMap headers, ProgressHandler progressHandler,
                          CompletionHandler completionHandler, CancellationHandler c) {
        if (converter != null) {
            url = converter.convert(url);
        }

        RequestBody rbody;
        if (body != null && body.length > 0) {
            MediaType t = MediaType.parse(DefaultMime);
            rbody = RequestBody.create(t, body, offset, size);
        } else {
            rbody = RequestBody.create(null, new byte[0]);
        }
        if (progressHandler != null) {
            rbody = new CountingRequestBody(rbody, progressHandler, c);
        }

        Request.Builder requestBuilder = new Request.Builder().url(url).post(rbody);
        asyncSend(requestBuilder, headers, completionHandler);
    }

    public void asyncMultipartPost(String url,
                                   PostArgs args,
                                   ProgressHandler progressHandler,
                                   CompletionHandler completionHandler,
                                   CancellationHandler c) {
        RequestBody file;
        if (args.file != null) {
            file = RequestBody.create(MediaType.parse(args.mimeType), args.file);
        } else {
            file = RequestBody.create(MediaType.parse(args.mimeType), args.data);
        }
        asyncMultipartPost(url, args.params, progressHandler, args.fileName, file, completionHandler, c);
    }

    private void asyncMultipartPost(String url,
                                    StringMap fields,
                                    ProgressHandler progressHandler,
                                    String fileName,
                                    RequestBody file,
                                    CompletionHandler completionHandler,
                                    CancellationHandler cancellationHandler) {
        if (converter != null) {
            url = converter.convert(url);
        }
        final MultipartBody.Builder mb = new MultipartBody.Builder();
        mb.addFormDataPart("file", fileName, file);

        fields.forEach(new StringMap.Consumer() {
            @Override
            public void accept(String key, Object value) {
                mb.addFormDataPart(key, value.toString());
            }
        });
        mb.setType(MediaType.parse("multipart/form-data"));
        RequestBody body = mb.build();
        if (progressHandler != null) {
            body = new CountingRequestBody(body, progressHandler, cancellationHandler);
        }
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        asyncSend(requestBuilder, null, completionHandler);
    }

    private static ResponseInfo buildResponseInfo( Response response, String ip, long duration){
        int code = response.code();
        String reqId = response.header("X-Reqid");
        reqId = (reqId == null) ? null : reqId.trim();
        byte[] body = null;
        String error = null;
        try {
            body = response.body().bytes();
        } catch (IOException e) {
            error = e.getMessage();
        }
        JSONObject json = null;
        if (ctype(response).equals(Client.JsonMime) && body != null) {
            try {
                json = buildJsonResp(body);
                if (response.code() != 200) {
                    String err = new String(body, Constants.UTF_8);
                    error = json.optString("error", err);
                }
            } catch (Exception e) {
                if (response.code() < 300) {
                    error = e.getMessage();
                }
            }
        } else {
            error = body == null ? "null body" : new String(body);
        }

        HttpUrl u = response.request().url();
        return new ResponseInfo(json, code, reqId, response.header("X-Log"),
                via(response), u.host(), u.encodedPath(), ip, u.port(), duration, 0, error);
    }

    private static void onRet( Response response, String ip, long duration,
                              final CompletionHandler complete) {
        final ResponseInfo info = buildResponseInfo(response, ip, duration);

        AsyncRun.runInMain(new Runnable() {
            @Override
            public void run() {
                complete.complete(info, info.response);
            }
        });
    }

    public void asyncGet(String url, StringMap headers, CompletionHandler completionHandler){
        Request.Builder requestBuilder = new Request.Builder().get().url(url);
        asyncSend(requestBuilder, headers, completionHandler);
    }

//    public ResponseInfo syncGet(String url, StringMap headers){
//        Request.Builder requestBuilder = new Request.Builder().get().url(url);
//        return send(requestBuilder, headers);
//    }

    public ResponseInfo send(final Request.Builder requestBuilder, StringMap headers) {
        if (headers != null) {
            headers.forEach(new StringMap.Consumer() {
                @Override
                public void accept(String key, Object value) {
                    requestBuilder.header(key, value.toString());
                }
            });
        }

        requestBuilder.header("User-Agent", UserAgent.instance().toString());
        long start = System.currentTimeMillis();
         Response res = null;
        ResponseTag tag = new ResponseTag();
        Request req = requestBuilder.tag(tag).build();
        try {
            res = httpClient.newCall(req).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseInfo(null, NetworkError, "", "", "",
                    req.url().host(), req.url().encodedPath(), tag.ip, req.url().port(),
                    tag.duration, 0, e.getMessage());
        }

        return buildResponseInfo(res, tag.ip, tag.duration);
    }

    private static class ResponseTag {
        public String ip = "";
        public long duration = 0;
    }
}
