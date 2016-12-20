package com.lovejob.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lovejob.BaseActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.v.rapiddev.utils.V;


/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:WechatPayTestDemo
 * Package_Name:com.lovejob.wechatpaytestdemo.wxapi
 * Created on 2016-10-28 21:11
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx28d1e39fb52b12bf");

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        V.d("onPayFinish, errCode = " + resp.errCode);
        Intent intent = new Intent();
        intent.setAction("com.lovejob.wechatpayinfos");
        intent.putExtra("code", resp.errCode);
        sendBroadcast(intent);
        V.d("微信支付状态广播发出成功");
        finish();
    }

}
