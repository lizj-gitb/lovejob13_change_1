package com.lovejob.controllers.task;

import android.content.Context;

import com.lovejob.model.StaticParams;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.security.FFRSAUtils;
import com.v.rapiddev.utils.V;

import java.util.Map;

/**
 * ClassType:生成公私钥
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.controllers.publictask
 * Created on 2016-11-21 14:42
 */

public class CreateRSAKey {
    public String CreateRSAKey(Context context) throws Exception {
        String key = null;
        V.d("start create key...");
        //获取密钥对
        Map<String, Object> keyPair = FFRSAUtils.genKeyPair();
        AppPreferences appPreferences = new AppPreferences(context);
        //获取公钥并写入文件
        appPreferences.put(StaticParams.FileKey.__KEY_PUBLICK_CLIENT__, FFRSAUtils.getPublicKey(keyPair));
        //获取私钥并写入文件
        appPreferences.put(StaticParams.FileKey.__KEY_PRIVATE_CLIENT__, FFRSAUtils.getPrivateKey(keyPair));
        V.d("saved keys success");
        key = appPreferences.getString(StaticParams.FileKey.__KEY_PUBLICK_CLIENT__, "");
        keyPair.clear();
        return key;
    }
}
