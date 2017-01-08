package com.lovejob.view._home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Toast;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.OnUpLoadImagesListener;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.ImageModle;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.UserInputModel;
import com.lovejob.model.Utils;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.InputMethodLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static com.lovejob.model.StaticParams.RequestCode.RequestCode_F_Home_TO_SendDyn;


/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.view._home
 * Created on 2016-11-22 02:47
 */

public class SendDynamic extends BaseActivity {
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.bt2)
    Button bt2;
    @Bind(R.id.main)
    ImageView main;
    @Bind(R.id.img1)
    ImageView img1;
    @Bind(R.id.img2)
    ImageView img2;
    //    private TaskHelper<Object> taskHelper;
//    private SendDynamicTask sendDynamicTask;
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.et_send_dynamic)
    EditText etSendDynamic;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.inputstate)
    InputMethodLayout inputstate;
    @Bind(R.id.layout_addImage)
    LinearLayout layout_addImage;
    private boolean isOpen = false;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private List<String> images = null;
    private Call call_senDyn;
    private String content, identify;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_senddynamic);
        ButterKnife.bind(this);
        AppPreferences preferences = new AppPreferences(context);
        identify = preferences.getString(StaticParams.FileKey.__IDENTIFY__, "");
        if (identify.equals("false")) {
            Utils.showToast(context, "请填写个人资料");
            AppManager.getAppManager().finishActivity();
            return;
        }
        setActionbar();
//        taskHelper = new TaskHelper<>();
        photoAdapter = new PhotoAdapter(this, selectedPhotos, false);
        initRecyclerView();
        inputstate.setOnkeyboarddStateListener(new InputMethodLayout.onKeyboardsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    case InputMethodLayout.KEYBOARD_STATE_SHOW:
                        V.d("软键盘被打开");
                        layout_addImage.setVisibility(View.GONE);
                        break;
                    case InputMethodLayout.KEYBOARD_STATE_HIDE:
                        V.d("软键盘被关闭");
                        layout_addImage.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        content = preferences.getString(StaticParams.FileKey.__DynamicContent__, "");
        if (!TextUtils.isEmpty(content)) {
            etSendDynamic.setText(content);
        }
    }

    private void initRecyclerView() {
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

    private void setActionbar() {
        actionbarTitle.setText("发布新动态");
        actionbarSave.setText("发布");
        actionbarTitle.setTextColor(Color.WHITE);
        actionbarTitle.setTextSize(18);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_senDyn != null && !call_senDyn.isCanceled()) call_senDyn.cancel();
        saveContent();
    }

    private void saveContent() {
        AppPreferences preferences = new AppPreferences(context);
        if (!TextUtils.isEmpty(etSendDynamic.getText().toString())) {
            preferences.put(StaticParams.FileKey.__DynamicContent__, etSendDynamic.getText().toString());
        } else {
            preferences.put(StaticParams.FileKey.__DynamicContent__, "");

        }
    }


    @OnClick({R.id.actionbar_back, R.id.actionbar_save, R.id.bt1, R.id.bt2, R.id.main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(context);
                break;
            case R.id.actionbar_save:
                //发布动态
                AppPreferences appPreferences = new AppPreferences(context);
                final String address = appPreferences.getString(StaticParams.FileKey.__City__, "");
                final String lng = appPreferences.getString(StaticParams.FileKey.__LONGITUDE__, "");
                final String lat = appPreferences.getString(StaticParams.FileKey.__LATITUDE__, "");
                if (TextUtils.isEmpty(lng) || TextUtils.isEmpty(lat)) {
                    Utils.showToast(context, "请先定位后再试");
                    return;
                }
                final UserInputModel inputModel = Utils.checkUserInputParams(etSendDynamic);
                if (!inputModel.isNotEmpty()) {
                    Utils.showToast(context, R.string.inputNull);
                    return;
                }
                final String content = inputModel.getParams()[0];
//                Utils.showProgreceBar(context, "正在发布动态，请稍后");
                if (selectedPhotos.size() == 0) {
                    HandlerUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog = Utils.showProgressDliago(context, "正在发布动态，请稍后");
                        }
                    });
                    call_senDyn = LoveJob.sendDyn(address, content, lat, lng, "", new OnAllParameListener() {
                        @Override
                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                            //上传图片到七牛云
                            etSendDynamic.setText("");
                            Utils.showToast(context, "发布成功");
                            dialog.dismiss();
                            Utils.showToast(context, R.string.senddynamic);
                            Intent intent = new Intent();
                            intent.putExtra("isRefresh", true);
                            setResult(RequestCode_F_Home_TO_SendDyn, intent);
                            finish();
                        }

                        @Override
                        public void onError(String msg) {
                            try {
                                dialog.dismiss();
                                Utils.showToast(context, msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return;
                }
                List<File> files = new ArrayList<>();
                for (int i = 0; i < selectedPhotos.size(); i++) {
                    files.add(new File(selectedPhotos.get(i)));
                }
                HandlerUtils.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog = Utils.showProgressDliago(context, "图片压缩中……");
                    }
                });
                Utils.ImageCo(files, context, true, new OnUpLoadImagesListener() {
                    @Override
                    public void onSucc(List<ImageModle> imageModleList) {
                        dialog.setContent("发布中……");
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 0; i < imageModleList.size(); i++) {
                            stringBuffer.append(imageModleList.get(i).getSmallFileName()).append("|");
                        }
                        call_senDyn = LoveJob.sendDyn(address, content, lat, lng, stringBuffer.toString(), new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                //上传图片到七牛云
                                dialog.dismiss();
                                etSendDynamic.setText("");
                                Utils.showToast(context, "发布成功");
                                dialog.dismiss();
                                Utils.showToast(context, R.string.senddynamic);
                                Intent intent = new Intent();
                                intent.putExtra("isRefresh", true);
                                setResult(RequestCode_F_Home_TO_SendDyn, intent);
                                finish();
                            }

                            @Override
                            public void onError(String msg) {
                                try {
                                    dialog.dismiss();
                                    Utils.showToast(context, msg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        Utils.showToast(context, "图片上传失败，请稍后再试");
                    }
                });

//                sendDynamicTask = new SendDynamicTask(address, content, lat, lng, sb.toString());
//                taskHelper.execute(sendDynamicTask, SendDynamicTaskCallBack);
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
//
//                break;
            case R.id.main:
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
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
