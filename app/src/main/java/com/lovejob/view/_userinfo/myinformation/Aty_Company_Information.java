package com.lovejob.view._userinfo.myinformation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.qiniuyun.http.ResponseInfo;
import com.lovejob.qiniuyun.storage.UpCompletionHandler;
import com.lovejob.qiniuyun.storage.UploadManager;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.preferences.AppPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Administrator on 2016/11/30.
 */
public class Aty_Company_Information extends BaseActivity {
    @Bind(R.id.img_inf_back)
    ImageView imgInfBack;
    @Bind(R.id.tv_inf_submit)
    TextView tvInfSubmit;
    @Bind(R.id.et_comm_info_name)
    EditText etCommInfoName;
    @Bind(R.id.et_comm_info_address)
    EditText etCommInfoAddress;
    @Bind(R.id.et_comm_info_scale)
    EditText etCommInfoScale;
    @Bind(R.id.et_comm_info_commcode)
    EditText etCommInfoCommcode;
    @Bind(R.id.et_comm_info_website)
    EditText etCommInfoWebsite;
    @Bind(R.id.et_comm_info_business)
    EditText etCommInfoBusiness;
    @Bind(R.id.gv_commpany_info_img)
    RecyclerView gvCommpanyInfoImg;
    //    @Bind(R.id.img_comm_info_addimg)
//    ImageView imgCommInfoAddimg;
    Activity context;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.bt2)
    Button bt2;
    @Bind(R.id.main)
    ImageView main;
    @Bind(R.id.layout_addImage)
    LinearLayout layoutAddImage;
    @Bind(R.id.img1)
    ImageView img1;
    @Bind(R.id.img2)
    ImageView img2;
    private String userPid;
    private HashMap<String, String> images = null;//要上传图片名称的集合
    private PhotoAdapter photoAdapter;

    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private Call call_getResumeList;
    private int maxImagesLenth = 0;
    private boolean isOpen = false;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_company_information);
        ButterKnife.bind(this);
        context = this;
        AppPreferences appPreferences = new AppPreferences(context);
        userPid = appPreferences.getString(StaticParams.FileKey.__USERPID__, "");
        addData();
        photoAdapter = new PhotoAdapter(this, selectedPhotos, false);
        gvCommpanyInfoImg.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        gvCommpanyInfoImg.setAdapter(photoAdapter);

        gvCommpanyInfoImg.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PhotoPreview.builder()
                        .setPhotos(selectedPhotos)
                        .setCurrentItem(position)
                        .setShowDeleteButton(true)
                        .start(context);
            }
        }));
    }

    private void addData() {
        LoveJob.getResumeList(userPid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                etCommInfoAddress.setText(thePerfectGirl.getData().getResumeDTO() != null ? thePerfectGirl.getData().getResumeDTO().getAddress() : "");
                etCommInfoName.setText(thePerfectGirl.getData().getResumeDTO() != null ? thePerfectGirl.getData().getResumeDTO().getName() : "");
                etCommInfoScale.setText(thePerfectGirl.getData().getResumeDTO() != null ? thePerfectGirl.getData().getResumeDTO().getScale() : "");
                etCommInfoCommcode.setText(thePerfectGirl.getData().getResumeDTO() != null ? thePerfectGirl.getData().getResumeDTO().getOrganizationCode() : "");
                etCommInfoWebsite.setText(thePerfectGirl.getData().getResumeDTO() != null ? thePerfectGirl.getData().getResumeDTO().getWebsite() : "");
                etCommInfoBusiness.setText(thePerfectGirl.getData().getResumeDTO() != null ? thePerfectGirl.getData().getResumeDTO().getMainBusiness() : "");
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_getResumeList != null && !call_getResumeList.isCanceled())
            call_getResumeList.cancel();
    }


    @OnClick({R.id.img_inf_back, R.id.tv_inf_submit, R.id.bt1, R.id.bt2, R.id.main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_inf_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tv_inf_submit:
                Submit();
                break;
            case R.id.main:
                PhotoPicker.builder()
                        .setPhotoCount(3)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(this);
//                main.startAnimation(getAnimation(isOpen ? 2 : 1));
                break;
//            case R.id.bt1:
//                main.startAnimation(getAnimation(isOpen ? 2 : 1));
//                PhotoPicker.builder()
//                        .setPhotoCount(9)
//                        .setShowCamera(false)
//                        .setSelected(selectedPhotos)
//                        .start(this);
//                break;
//            case R.id.bt2:
//                main.startAnimation(getAnimation(isOpen ? 2 : 1));
//                PhotoPicker.builder()
//                        .setPhotoCount(9)
//                        .setShowCamera(true)
//                        .setSelected(selectedPhotos)
//                        .start(this);
//                break;
        }
    }

    private void Submit() {

        if (TextUtils.isEmpty(etCommInfoName.getText().toString())
                || TextUtils.isEmpty(etCommInfoAddress.getText().toString())
                || TextUtils.isEmpty(etCommInfoScale.getText().toString())
                || TextUtils.isEmpty(etCommInfoWebsite.getText().toString())
                || TextUtils.isEmpty(etCommInfoBusiness.getText().toString())
                || TextUtils.isEmpty(etCommInfoAddress.getText().toString())) {
            Utils.showToast(context, "不能有空值");
            return;
        }

        final StringBuffer sb = new StringBuffer();
        images = new HashMap<>();
        //获取所有上传前图片路径
        //        List<String> data = adapter_img.getList();
//        String str = null;
//        for (int i = 0; i < photoAdapter.getList().size(); i++) {
//            V.d("图片信息：" + photoAdapter.getList().get(i).toString());
//            str = photoAdapter.getList().get(i);
//            images.add(i, Utils.getImgName(str));
//            sb.append(images.get(i)).append("|");
//            str = null;
//        }
        if (photoAdapter.getList().size() > 0) {
            //压缩图片
            Utils.yasuo(context, photoAdapter.getList(), new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.arg1 == 9000) {
                        String path = msg.getData().getString("path");
                        maxImagesLenth++;
                        images.put("lovejob_" + new Random().nextInt() + "cxwl" + new Date().getTime() + path.substring(path.lastIndexOf(".")), path);
                        if (maxImagesLenth == photoAdapter.getList().size()) {
                            //压缩完成
                            for (Iterator iter = images.entrySet().iterator(); iter.hasNext(); ) {
                                Map.Entry element = (Map.Entry) iter.next();
                                Object strKey = element.getKey();
                                Object strObj = element.getValue();
                                sb.append(strKey).append("|");
                            }
                            uploadImagesAndPushDate(sb.toString());
                        }
                    }
                }
            });
        } else {
            uploadImagesAndPushDate(null);
        }

    }

    private void uploadImagesAndPushDate(String allImageName) {
        maxImagesLenth = 0;
        LoveJob.saveResume1(allImageName, etCommInfoName.getText().toString(), etCommInfoAddress.getText().toString(), etCommInfoScale.getText().toString(),
                etCommInfoAddress.getText().toString(), etCommInfoWebsite.getText().toString(), etCommInfoBusiness.getText().toString(), new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                        Utils.showToast(context, "保存成功");
                        //TODO 上传图片
                        if (!TextUtils.isEmpty(thePerfectGirl.getData().getUploadToken())) {
                            UploadManager uploadManager = new UploadManager();
                            for (Iterator iter = images.entrySet().iterator(); iter.hasNext(); ) {
                                Map.Entry element = (Map.Entry) iter.next();
                                Object strKey = element.getKey();
                                Object strObj = element.getValue();
                                uploadManager.put(strObj.toString(), strKey.toString(), thePerfectGirl.getData().getUploadToken(), new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject response) {
                                        maxImagesLenth++;
                                        if (images.size() == maxImagesLenth) {
                                            Utils.showToast(context, "修改成功");
                                        }
                                    }
                                }, null);
                            }
                        } else {
                            Utils.showToast(context, "修改成功");
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.setSelectorImgOnResult(requestCode, resultCode, data, photoAdapter, selectedPhotos);
    }

    /**
     * @param isLeft 顺时针传1   逆时针传2
     * @return
     */
    private RotateAnimation getAnimation(int isLeft) {
        RotateAnimation animation = null;
        /** 设置旋转动画 */
        if (isLeft == 1) {
            animation = new RotateAnimation(0f, 135f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


            Animation animation1 = new ScaleAnimation(0f, 1.2f, 0f,
                    1.2f,// 整个屏幕就0.0到1.0的大小//缩放
                    Animation.INFINITE, 45f,
                    Animation.INFINITE, 20f);
            animation1.setDuration(300);
            animation1.setFillAfter(true);

            Animation translateAnimation1 = new TranslateAnimation(0, -120, 0, -190);// 移动
            translateAnimation1.setDuration(300);
            AnimationSet mAnimationSet = new AnimationSet(false);
            mAnimationSet.addAnimation(animation1);
            mAnimationSet.setFillAfter(true);
            mAnimationSet.addAnimation(translateAnimation1);
            img1.setVisibility(View.VISIBLE);
            img1.startAnimation(mAnimationSet);

            translateAnimation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    img1.setVisibility(View.GONE);
                    bt1.setEnabled(true);
                    isOpen = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            //TODO
            Animation animation2 = new ScaleAnimation(0f, 1.2f, 0f,
                    1.2f,// 整个屏幕就0.0到1.0的大小//缩放
                    Animation.INFINITE, 45f,
                    Animation.INFINITE, 20f);
            animation2.setDuration(300);
            animation2.setFillAfter(true);

            Animation translateAnimation2 = new TranslateAnimation(0, 120, 0, -190);// 移动
            translateAnimation2.setDuration(300);
            AnimationSet mAnimationSet2 = new AnimationSet(false);
            mAnimationSet2.addAnimation(animation2);
            mAnimationSet2.setFillAfter(true);
            mAnimationSet2.addAnimation(translateAnimation2);
            img2.setVisibility(View.VISIBLE);
            img2.startAnimation(mAnimationSet2);
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    img2.setVisibility(View.GONE);
                    bt2.setEnabled(true);
                    isOpen = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

//
//            Animation mScaleAnimation1 = new ScaleAnimation(1f, 1.2f, 1f,
//                    1.2f,// 整个屏幕就0.0到1.0的大小//缩放
//                    Animation.INFINITE, 0.5f,
//                    Animation.INFINITE, 0.5f);
//            mScaleAnimation1.setDuration(1000);
//            mScaleAnimation1.setFillAfter(true);
//
//            Animation mTranslateAnimation1 = new TranslateAnimation(0, 150, 0, -150);// 移动
//            mTranslateAnimation1.setDuration(1000);
//            mAnimationSet1 = new AnimationSet(false);
//            mAnimationSet1.addAnimation(mScaleAnimation1);
//            mAnimationSet1.setFillAfter(true);
//            mAnimationSet1.addAnimation(mTranslateAnimation1);

        } else {
            //主
            animation = new RotateAnimation(135f, 0f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            isOpen = false;

            Animation animation1 = new ScaleAnimation(1.2f, 0f, 1.2f,
                    0f,// 整个屏幕就0.0到1.0的大小//缩放
                    Animation.INFINITE, 45f,
                    Animation.INFINITE, 20f);
            animation1.setDuration(300);
            animation1.setFillAfter(true);

            Animation translateAnimation1 = new TranslateAnimation(-120, 0, -190, 0);// 移动
            translateAnimation1.setDuration(300);
            AnimationSet mAnimationSet = new AnimationSet(false);
            mAnimationSet.addAnimation(animation1);
            mAnimationSet.setFillAfter(true);
            mAnimationSet.addAnimation(translateAnimation1);
            img1.setVisibility(View.VISIBLE);
            img1.startAnimation(mAnimationSet);
            translateAnimation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bt1.setEnabled(false);
                    isOpen = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            //TODO
            Animation animation2 = new ScaleAnimation(1.2f, 0f, 1.2f,
                    0f,// 整个屏幕就0.0到1.0的大小//缩放
                    Animation.INFINITE, 45f,
                    Animation.INFINITE, 20f);
            animation2.setDuration(300);
            animation2.setFillAfter(true);

            Animation translateAnimation2 = new TranslateAnimation(120, 0, -190, 0);// 移动
            translateAnimation2.setDuration(300);
            AnimationSet mAnimationSet2 = new AnimationSet(false);
            mAnimationSet2.addAnimation(animation2);
            mAnimationSet2.setFillAfter(true);
            mAnimationSet2.addAnimation(translateAnimation2);
//            img2.setVisibility(View.VISIBLE);
            img2.startAnimation(mAnimationSet2);

            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bt2.setEnabled(false);
                    isOpen = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
        animation.setDuration(300);//设置动画持续时间
        /** 常用方法 */
        animation.setRepeatCount(0);//设置重复次数
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        return animation;
    }


}
