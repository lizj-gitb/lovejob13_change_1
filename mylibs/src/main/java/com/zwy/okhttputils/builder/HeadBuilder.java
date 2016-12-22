package com.zwy.okhttputils.builder;


import com.zwy.okhttputils.OkHttpUtils;
import com.zwy.okhttputils.request.OtherRequest;
import com.zwy.okhttputils.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
