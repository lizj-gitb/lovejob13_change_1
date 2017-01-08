package com.lovejob.controllers;


import com.lovejob.model.ImageModle;

import java.util.List;

/**
 * 创建时间：2016-12-30 12:31
 * 创建人：赵文贇
 * 类描述：用户上传图片的回调
 * 包名：Cxwl_LoveJob
 */

public interface OnUpLoadImagesListener {
    void onSucc(List<ImageModle> imageModleList);

    void onError();
}
