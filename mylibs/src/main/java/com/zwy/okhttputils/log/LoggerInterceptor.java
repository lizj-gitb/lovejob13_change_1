package com.zwy.okhttputils.log;

import android.text.TextUtils;

import com.zwy.logger.Logger;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by zhy on 16/3/1.
 */
public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean showResponse;

    public LoggerInterceptor(boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public LoggerInterceptor(String tag) {
        this( false);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LoggerForRequest(request);
        Response response = chain.proceed(request);
        return LoggerForResponse(response);
    }

    private Response LoggerForResponse(Response response) {
        try {
            //===>response Logger
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Logger.d("url : " + clone.request().url() + "\ncode:" + clone.code()
                    + "\nprotocol:" + clone.protocol());
            if (!TextUtils.isEmpty(clone.message()))
                Logger.d("message : " + clone.message());

            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        Logger.d("responseBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = body.string();
                            Logger.d("responseBody's content : " + resp);

                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            Logger.d("responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }

        return response;
    }

    private void LoggerForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    Logger.d( "requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        Logger.d( "requestBody's content : " + bodyToString(request));
                    } else {
                        Logger.d( "requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            Logger.d( "method : " + request.method()+"\nurl : " + url
                    +"\nheaders:"+headers.toString()+"\ncontent:"+bodyToString(request));
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
