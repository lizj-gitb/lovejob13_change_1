package com.lovejob.view._userinfo.myserver;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
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
import com.lovejob.view.WriteView;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.utils.V;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Administrator on 2016/11/29.
 */
public class Aty_ReleaseService extends BaseActivity {
    @Bind(R.id.img_service_window_back)
    ImageView imgServiceWindowBack;
    @Bind(R.id.tv_ser_my_release)
    TextView tvSerMyRelease;
    @Bind(R.id.tv_send_ser_skill_title)
    TextView tvSendSerSkillTitle;
    @Bind(R.id.lt_send_ser_skill_title)
    RelativeLayout ltSendSerSkillTitle;
    @Bind(R.id.tv_send_ser_skill_price)
    TextView tvSendSerSkillPrice;
    @Bind(R.id.lt_send_ser_skill_price)
    RelativeLayout ltSendSerSkillPrice;
    @Bind(R.id.tv_send_ser_skill_payment)
    TextView tvSendSerSkillPayment;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.textView4)
    TextView textView4;
    @Bind(R.id.lt_send_ser_skill_payment)
    RelativeLayout ltSendSerSkillPayment;
    @Bind(R.id.tv_send_ser_skill_introduce)
    EditText etSendSerSkillIntroduce;
    @Bind(R.id.lt_send_ser_skill_introduce)
    LinearLayout ltSendSerSkillIntroduce;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.bt_send_skill_open)
    LinearLayout btSendSkillOpen;
    @Bind(R.id.tv_send_onoroff)
    TextView tvSendOnoroff;
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
    private boolean isOpen = false;
    String serverType, have;
    private List<String> images = null;//要上传图片名称的集合
    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    ArrayList<String> path = new ArrayList<>();
    ArrayList<String> path_local = new ArrayList<>();
    private int maxImagesLegth = 0;
    private ThePerfectGirl.ServerDTO serverDTO;
    private String state = "";
    private String photos = "";//用|分割开的所有图片url

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_releasesercice);
        ButterKnife.bind(this);
        serverType =getIntent().getStringExtra("server");
        have = context.getIntent().getStringExtra("have");
        if (serverType!=null){
            if (serverType.equals("2")){
                ltSendSerSkillPrice.setVisibility(View.GONE);
            }
        }
        serverDTO = (ThePerfectGirl.ServerDTO) getIntent().getSerializableExtra("serverDto");
        if (serverDTO != null) {
            state = String.valueOf(serverDTO.getState());
            if (serverDTO.getServiceType()==2) {
                ltSendSerSkillPrice.setVisibility(View.GONE);
            }
        }
        photos = getIntent().getStringExtra("bitmaps");
        if (!TextUtils.isEmpty(photos)) {
            initPhotoAdapter_network();

        } else {
            initPhotoAdapter_path();
        }
        if (have.equals("1")) {
            btSendSkillOpen.setVisibility(View.GONE);

        } else {
            btSendSkillOpen.setVisibility(View.VISIBLE);
            if (state.equals("1")) {
                tvSendOnoroff.setBackgroundColor(Color.parseColor("#f1f1f1"));
                tvSendOnoroff.setText("关闭该服务");
                tvSendOnoroff.setTextColor(getResources().getColor(R.color.hiteTextColor));
            }
        }
        if (serverDTO != null) {
            tvSendSerSkillPrice.setText(String.valueOf(serverDTO.getMoney()));
            tvSendSerSkillTitle.setText(serverDTO.getTitle());
            etSendSerSkillIntroduce.setText(serverDTO.getContent());
            selectedPhotos.clear();
            try {
                for (int i = 0; i < photos.split("\\|").length; i++) {
                    selectedPhotos.add(StaticParams.QiNiuYunUrl + photos.split("\\|")[i]);
                }
                photoAdapter.addItem(selectedPhotos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void initPhotoAdapter_path() {
        photoAdapter = new PhotoAdapter(this, selectedPhotos, false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
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

    public void initPhotoAdapter_network() {
        photoAdapter = new PhotoAdapter(this, selectedPhotos, true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                PhotoPreview.builder()
//                        .setPhotos(selectedPhotos)
//                        .setCurrentItem(position)
//                        .setShowDeleteButton(false)
//                        .start(context);
//            }
//        }));
    }


    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (data == null) {
            return;
        }
//        if ( data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)!=null || data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS).size()>0){
        initPhotoAdapter_path();
        Utils.setSelectorImgOnResult(requestCode, resultCode, data, photoAdapter, selectedPhotos);
//        }
        switch (requestCode) {
            case StaticParams.RequestCode.RequsetCode_Aty_ReleaseService_To_Title:
                tvSendSerSkillTitle.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequsetCode_Aty_ReleaseService_To_Price:
                tvSendSerSkillPrice.setText(data.getStringExtra("content"));
                break;
        }
    }

    @OnClick({R.id.bt1, R.id.bt2, R.id.main, R.id.img_service_window_back, R.id.tv_ser_my_release, R.id.lt_send_ser_skill_title, R.id.lt_send_ser_skill_price, R.id.lt_send_ser_skill_payment, R.id.bt_send_skill_open})
    public void onClick(View view) {
        final Intent in = new Intent(context, WriteView.class);
        int requestCode = -1;
        switch (view.getId()) {

            case R.id.img_service_window_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.tv_ser_my_release:
                sendService();
                dialog = Utils.showProgressDliago(context, "正在发布请稍后");
                break;
            case R.id.lt_send_ser_skill_title:
                in.putExtra("title", "服务名称");
                in.putExtra("content", tvSendSerSkillTitle.getText() == null ? "" : tvSendSerSkillTitle.getText().toString());
                requestCode = StaticParams.RequestCode.RequsetCode_Aty_ReleaseService_To_Title;
                break;
            case R.id.lt_send_ser_skill_price:
                in.putExtra("title", "服务价格");
                in.putExtra("writeType", 3);
                in.putExtra("maxLenth", 8);
                in.putExtra("content", tvSendSerSkillPrice.getText() == null ? "" : tvSendSerSkillPrice.getText().toString());
                requestCode = StaticParams.RequestCode.RequsetCode_Aty_ReleaseService_To_Price;
                break;
            case R.id.bt_send_skill_open:

                if (state.equals("1")) {
                    state = "0";
                }
                if (state.equals("0")) {
                    state = "1";
                }
                LoveJob.onOrOffServer(serverDTO.getServerPid(), state, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        if (state.equals("1")) {
                            Utils.showToast(context, "开启该服务成功");
                        }
                        if (state.equals("0")) {
                            Utils.showToast(context, "关闭该服务成功");

                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                });
                break;
//            case R.id.bt1:
//                main.startAnimation(getAnimation(isOpen ? 2 : 1));
//                PhotoPicker.builder()
//                        .setPhotoCount(3)
//                        .setShowCamera(false)
//                        .setSelected(selectedPhotos)
//                        .start(this);
//                break;
//            case R.id.bt2:
//                main.startAnimation(getAnimation(isOpen ? 2 : 1));
//
//                break;
            case R.id.main:
                PhotoPicker.builder()
                        .setPhotoCount(3)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(this);
                break;
        }
        if (requestCode > 0) {
            in.putExtra("requestCode", requestCode);
            startActivityForResult(in, requestCode);
            requestCode = -1;
        }
    }

    private void sendService() {
        final String titie, price, introduce, payment;
        titie = tvSendSerSkillTitle.getText().toString().trim();
        price = tvSendSerSkillPrice.getText().toString().trim();
        introduce = etSendSerSkillIntroduce.getText().toString().trim();
        payment = tvSendSerSkillPayment.getText().toString().trim();
        if (TextUtils.isEmpty(titie)
                || TextUtils.isEmpty(introduce)) {
            Utils.showToast(context, "不能有空值");
            if (dialog!=null) {
                dialog.dismiss();
            }
            return;
        }
//        if (selectedPhotos.size()>0) {
        if (selectedPhotos.size() > 0) {
            //压缩
            Utils.yasuo(context, selectedPhotos, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.arg1 == 9000) {
                        V.d("压缩完成的图片路径：" + msg.getData().getString("path"));
                        String str = msg.getData().getString("path");
                        path.add("lovejob_" + new Date().getTime() + "lobejob" + new Random().nextInt() + str.substring(str.lastIndexOf(".")));
                        path_local.add(str);
                        maxImagesLegth++;
                        if (maxImagesLegth == selectedPhotos.size()) {
                            V.d("所有图片压缩完成");
                            StringBuffer stringBuffer = new StringBuffer();
                            for (String p : path) {
                                stringBuffer.append(p).append("|");
                            }
                            //TODO ip
                            String pid = null;
                            if (serverDTO != null && serverDTO.getServerPid() != null) {
                                pid = serverDTO.getServerPid();
                            }
                            callList.add(LoveJob.sendService(titie, introduce,price, stringBuffer.toString(), serverType,
                                    pid, new OnAllParameListener() {
                                @Override
                                public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                    String token = thePerfectGirl.getData().getUploadToken();
                                    UploadManager uploadManager = new UploadManager();
                                    maxImagesLegth = 0;
                                    for (int i = 0; i < path_local.size(); i++) {
                                        uploadManager.put(path_local.get(i), path.get(i), token, new UpCompletionHandler() {
                                            @Override
                                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                                maxImagesLegth++;
                                                if (maxImagesLegth == selectedPhotos.size()) {
                                                    V.d("工作发布成功");
                                                    dialog.dismiss();
                                                    Utils.showToast(context, "发布成功");
                                                    AppManager.getAppManager().finishActivity();
                                                }
                                            }
                                        }, null);
                                    }
                                }

                                @Override
                                public void onError(String msg) {
                                    dialog.dismiss();
                                    Utils.showToast(context, msg);
                                }
                            }));

                        }
                    }
                }
            });
        } else {

            if (serverDTO != null) {
                callList.add(LoveJob.sendService(titie, introduce, price, null, serverType, serverDTO.getServerPid(), new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        V.d("工作发布成功");
                        dialog.dismiss();
                        Utils.showToast(context, "发布成功");
                        AppManager.getAppManager().finishActivity();
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, msg);
                    }
                }));
            } else {
                callList.add(LoveJob.sendService(titie, introduce, price, null, serverType, null, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        V.d("工作发布成功");
                        dialog.dismiss();
                        Utils.showToast(context, "发布成功");
                        AppManager.getAppManager().finishActivity();
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, msg);
                    }
                }));
            }
        }
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
