package com.lovejob.model;

/**
 * 创建时间：2016-12-22 18:28
 * 创建人：赵文贇
 * 类描述：
 * 包名：lovejob11
 */

public interface CheckResponse {
    void onSucc();

    void onError(String msg);
}
