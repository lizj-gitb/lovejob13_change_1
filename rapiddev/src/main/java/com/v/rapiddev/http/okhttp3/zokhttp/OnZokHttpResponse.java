package com.v.rapiddev.http.okhttp3.zokhttp;

import java.util.ArrayList;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Fast_Lib
 * Package_Name:com.fastlib.interfaces
 * Created on 2016-10-07 19:43
 */

public interface OnZokHttpResponse {
    void OnSucc(String data);

    void OnError(String errorMsg);
}
