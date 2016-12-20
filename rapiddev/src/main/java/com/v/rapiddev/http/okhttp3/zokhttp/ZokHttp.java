package com.v.rapiddev.http.okhttp3.zokhttp;

import android.os.Handler;
import android.text.TextUtils;

import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.http.okhttp3.Callback;
import com.v.rapiddev.http.okhttp3.FormBody;
import com.v.rapiddev.http.okhttp3.HttpUrl;
import com.v.rapiddev.http.okhttp3.OkHttpClient;
import com.v.rapiddev.http.okhttp3.Request;
import com.v.rapiddev.http.okhttp3.RequestBody;
import com.v.rapiddev.http.okhttp3.Response;
import com.v.rapiddev.http.okhttp3.ResponseBody;
import com.v.rapiddev.utils.Utils_RapidDev;
import com.v.rapiddev.utils.V;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:RapidDevSample
 * Package_Name:com.v.rapiddev.http.okhttp3.zokhttp
 * Created on 2016-11-24 10:53
 */

public class ZokHttp {
    private static OkHttpClient okHttpClient = null;


    private static ZokHttp ZokHttp;
    private static Handler handler;

    public static void setHandler(Handler h) {
        handler = h;
    }

    public static ZokHttp getInstance() {
        if (okHttpClient == null) okHttpClient =
                new OkHttpClient().newBuilder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
        if (ZokHttp == null) ZokHttp = new ZokHttp();
        return ZokHttp;
    }

    public static ZokHttp getInstance(boolean isReTry) {
        if (okHttpClient == null) okHttpClient =
                new OkHttpClient().newBuilder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(isReTry)
                        .build();
        if (ZokHttp == null) ZokHttp = new ZokHttp();
        return ZokHttp;
    }

    /**
     * 超时时间
     *
     * @param ms 单位 秒
     * @return
     */
    public static ZokHttp getInstance(int ms) {
        if (okHttpClient == null) okHttpClient =
                new OkHttpClient().newBuilder()
                        .connectTimeout(ms, TimeUnit.SECONDS)
                        .readTimeout(ms, TimeUnit.SECONDS)
                        .build();
        if (ZokHttp == null) ZokHttp = new ZokHttp();
        return ZokHttp;
    }

    /**
     * 无参的Post请求
     *
     * @param URL
     * @param response
     * @param tag
     * @return
     */
    public Call doPost(String URL, OnZokHttpResponse response, String... tag) {
        if (HttpUrl.parse(URL) == null) {
            throw new UnknownError("未知的URL类型");
        }
        Request.Builder builder = new Request.Builder();
        builder.url(URL);
        if (ResponseHeadersInfo.SessionId != null)
            builder.addHeader("Cookie", ResponseHeadersInfo.SessionId);
        Request request = builder.post(new FormBody.Builder().build()).build();
        Call call = okHttpClient.newCall(request);
        V.d("发起了一个无参的Post请求,上送请求头：" + request.headers().toString());
        call.enqueue(new MyCallBack(response, tag));
        return call;
    }

    /**
     * 带参数的Post请求
     *
     * @param URL      请求URl
     * @param params   参数key=v
     * @param response callback回调
     * @param tag      日志输出标签
     * @return
     */
    public Call doPost(String URL, Map<String, String> params, OnZokHttpResponse response, String... tag) {
        if (HttpUrl.parse(URL) == null) {
            throw new UnknownError("未知的URL类型");
        }
        Request.Builder builder = new Request.Builder();
        builder.url(URL);
        if (ResponseHeadersInfo.SessionId != null)
            builder.addHeader("Cookie", ResponseHeadersInfo.SessionId);
        FormBody.Builder param = Utils_RapidDev.addParam(params);
        RequestBody requestBody = param.build();
        Request request = builder.post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        V.d("发起了一个Post请求,上送参数：" + params.toString() + "\n请求头：" + request.headers().toString());
        call.enqueue(new MyCallBack(response, tag));
        return call;
    }

//    /**
//     * POST同步提交数据
//     *
//     * @param url
//     * @return Response
//     * @throws IOException
//     */
//    public Response postSync(String url, RequestBody body) throws IOException {
//        url = String.format(API_URL, url);
//        Request request = new Request.Builder().url(url).post(body).build();
//        Response response = mOkHttpClient.newCall(request).execute();
//        if (response.isSuccessful()) {
//            return response;
//        } else {
//            throw new IOException("Unexpected code " + response);
//        }
//    }


    /**
     * 无参的Get请求
     *
     * @param URL
     * @param response
     * @param tag
     * @return
     */
    public Call doGet(String URL, OnZokHttpResponse response, String... tag) {
        if (HttpUrl.parse(URL) == null) {
            throw new UnknownError("未知的URL类型");
        }
        Request.Builder builder = new Request.Builder();
        builder.url(URL);
        if (ResponseHeadersInfo.SessionId != null && !TextUtils.isEmpty(ResponseHeadersInfo.SessionId))
            builder.addHeader("Cookie", ResponseHeadersInfo.SessionId);

        Request request = builder.get().build();
        Call call = okHttpClient.newCall(request);
        V.d("发起了一个无参的Get请求,上送请求头：" + request.headers().toString());
        call.enqueue(new MyCallBack(response, tag));
        return call;
    }


    /**
     * 带参数的Get请求
     *
     * @param URL      请求URl
     * @param params   参数key=v
     * @param response callback回调
     * @param tag      日志输出标签
     * @return
     */
    public Call doGet(String URL, Map<String, String> params, OnZokHttpResponse response, String... tag) {
        if (HttpUrl.parse(URL) == null) {
            throw new UnknownError("未知的URL类型");
        }
        Request.Builder builder = new Request.Builder();
        if (ResponseHeadersInfo.SessionId != null)
            builder.addHeader("Cookie", ResponseHeadersInfo.SessionId);
        builder.url(Utils_RapidDev.appendParameter(URL, params));
        Request request = builder.get().build();
        Call call = okHttpClient.newCall(request);
        V.d("发起了一个get请求,上送参数：url=" + request.url().toString() + "\n请求头：" + request.headers().toString());
        call.enqueue(new MyCallBack(response, tag));
        return call;
    }

    public class MyCallBack implements Callback {
        private OnZokHttpResponse mResponse;
        private String tag;

        public MyCallBack(OnZokHttpResponse response, String... tag) {
            this.mResponse = response;
            if (tag.length > 0)
                this.tag = tag[0];
        }

        /**
         * Called when the request could not be executed due to cancellation, a connectivity problem or
         * timeout. Because networks can fail during an exchange, it is possible that the remote server
         * accepted the request before the failure.
         *
         * @param call
         * @param e
         */
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mResponse.OnError("请求超时");
                }
            });
            call.cancel();
        }

        /**
         * Called when the HTTP response was successfully returned by the remote server. The callback may
         * proceed to read the response body with {@link Response#body}. The response is still live until
         * its response body is {@linkplain ResponseBody closed}. The recipient of the callback may
         * consume the response body on another thread.
         * <p>
         * <p>Note that transport-layer success (receiving a HTTP response code, headers and body) does
         * not necessarily indicate application-layer success: {@code response} may still indicate an
         * unhappy HTTP response code like 404 or 500.
         *
         * @param call
         * @param response
         */
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String data = response.body().string();
            V.d("请求" + tag + "的响应:" + data.trim().toString().trim());
            List<String> cookies = response.headers().values("Set-Cookie");
            for (String str : cookies) {
                ResponseHeadersInfo.SessionId = str.split(";")[0];
            }

            if (response.isSuccessful()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mResponse.OnSucc(data == null ? "网络异常" : data);
                    }
                });

            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mResponse.OnError("网络异常");
                    }
                });

            }
            call.cancel();
        }
    }

}
