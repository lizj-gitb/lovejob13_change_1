package com.lovejob.view._money.fragments_sendmoney;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.baidumap.BaiDuMapCitySelctor;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.MyOnClickListener;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.UserInputModel;
import com.lovejob.model.Utils;
import com.lovejob.qiniuyun.http.ResponseInfo;
import com.lovejob.qiniuyun.storage.UpCompletionHandler;
import com.lovejob.qiniuyun.storage.UploadManager;
import com.lovejob.view.WriteView;
import com.lovejob.view.payinfoviews.PayInfoParams;
import com.lovejob.view.payinfoviews.PayViewSelectPayment;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.dialogs.core.MaterialDialog;
import com.v.rapiddev.dialogs.zdialog.OnDialogItemClickListener;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
import com.v.rapiddev.pickerview.TimePickerView;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.V;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendJobWork_LocationSelected;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendOriWork_LocationSelected;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendOriWork_TO_WriteView_Location;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendOriWork_TO_WriteView_Person;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendOriWork_TO_WriteView_PhoneNumber;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendOriWork_TO_WriteView_Price;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendOriWork_TO_WriteView_Want;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob
 * Package_Name:com.lovejob.view._money.fragments_sendmoney
 * Created on 2016-12-06 15:16
 */

public class SendOriWork extends BaseFragment {
    View view;
    @Bind(R.id.tv_send_work_ori_title)
    TextView tvSendWorkOriTitle;
    @Bind(R.id.rt_send_work_ori_title)
    RelativeLayout rtSendWorkOriTitle;
    @Bind(R.id.tv_send_work_ori_want)
    TextView tvSendWorkOriWant;
    @Bind(R.id.rt_send_work_ori_want)
    RelativeLayout rtSendWorkOriWant;
    @Bind(R.id.tv_send_work_ori_location)
    TextView tvSendWorkOriLocation;
    @Bind(R.id.rt_send_work_ori_location)
    RelativeLayout rtSendWorkOriLocation;
    @Bind(R.id.tv_send_work_ori_price)
    TextView tvSendWorkOriPrice;
    @Bind(R.id.rt_send_work_ori_price)
    RelativeLayout rtSendWorkOriPrice;
    @Bind(R.id.tv_send_work_ori_person)
    TextView tvSendWorkOriPerson;
    @Bind(R.id.rt_send_work_ori_person)
    RelativeLayout rtSendWorkOriPerson;
    @Bind(R.id.tv_send_work_ori_phonenumber)
    TextView tvSendWorkOriPhonenumber;
    @Bind(R.id.rt_send_work_ori_phonenumber)
    RelativeLayout rtSendWorkOriPhonenumber;
    @Bind(R.id.tv_send_work_ori_countdown)
    TextView tvSendWorkOriCountdown;
    @Bind(R.id.rt_send_work_ori_countdown)
    RelativeLayout rtSendWorkOriCountdown;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
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
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    //    StringBuffer stringBuffer_Path = new StringBuffer();//存储压缩后的图片路径的strbf
    ArrayList<String> arrayList_path = new ArrayList<>();//存储压缩后的图片路径的strbf
    StringBuffer stringBuffer_Name = new StringBuffer();//存储压缩后的图片名称的strbf
    private String identify;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sendoriwork, null);
        ButterKnife.bind(this, view);
        AppPreferences preferences = new AppPreferences(context);
        identify = preferences.getString(StaticParams.FileKey.__IDENTIFY__,"");
        if (identify.equals("false")){
            Utils.showToast(context,"请填写个人资料");
            AppManager.getAppManager().finishActivity();
            return view;
        }
        initTextView();
        initRecyclerView();
        setSaveListener();
        return view;
    }

    private void initTextView() {
        tvSendWorkOriTitle.setMaxLines(1);
//        tvSendWorkOriTitle.setText("标题五个字");

        tvSendWorkOriWant.setMaxLines(1);
//        tvSendWorkOriWant.setText("需求20个字可为空");

        tvSendWorkOriLocation.setMaxLines(1);
//        tvSendWorkOriLocation.setText("地址20个字");

        tvSendWorkOriPrice.setMaxLines(1);

        tvSendWorkOriPerson.setMaxLines(1);
//        tvSendWorkOriPerson.setText("张三");

        tvSendWorkOriPhonenumber.setMaxLines(1);
//        tvSendWorkOriPhonenumber.setText("18966714431");

        tvSendWorkOriCountdown.setMaxLines(1);

    }

    private void setSaveListener() {
        getActivity().findViewById(R.id.actionbar_save).setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onclickListener(View v) {
                V.d("开始发布创意工作");
                ZDialog.showZDlialog(context, "提示", "是否发布", "发布", "再写点", new OnDialogItemClickListener() {
                    @Override
                    public void onLeftButtonClickListener() {
                        V.d("发布");
                        sendOriWorkAndUpLoadImages();
                    }

                    @Override
                    public void onRightButtonClickListener() {
                        V.d("取消");
                    }
                }).show();
            }
        });
    }

    private void sendOriWorkAndUpLoadImages() {
//        dialog = Utils.showProgressDliago(context, "正在为您发布创意工作，请稍后……");
        final UserInputModel inputModel = Utils.checkUserInputParams(tvSendWorkOriTitle, tvSendWorkOriLocation,
                tvSendWorkOriPrice, tvSendWorkOriPhonenumber, tvSendWorkOriCountdown);
        if (!inputModel.isNotEmpty()) {
            Utils.showToast(context, "不能有空值");
            return;
        }
        String s = tvSendWorkOriPrice.getText().toString();
        String s1 =s.substring(0,s.length()-2);
        int money = Integer.parseInt(s1);
        if (money<=0){
            Utils.showToast(context, "支付金额不能小于0元");
            return;
        }
        String want = tvSendWorkOriWant.getText() == null ? "" : tvSendWorkOriWant.getText().toString().trim();
        String person = tvSendWorkOriPerson.getText() == null ? "" : tvSendWorkOriPerson.getText().toString().trim();
        final String[] strs = new String[8];
        for (int i = 0; i < inputModel.getParams().length; i++) {
            strs[i] = inputModel.getParams()[i];
        }
        strs[5] = want;
        strs[6] = person;
        //压缩
        //跳转支付页面  传入支付以及发布工作所需参数
        final Intent intent = new Intent(context, PayViewSelectPayment.class);
        if (selectedPhotos.size() > 0) {
            Utils.yasuo(context, selectedPhotos, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.arg1 == 9000) {
                        String path = msg.getData().getString("path");
                        final int index = msg.getData().getInt("index");
                        V.d("第" + (index + 1) + "张图片压缩成功,压缩后的地址：" + path);
//                        stringBuffer_Path.append(path).append("|");
                        arrayList_path.add(path);
                        stringBuffer_Name.append("lovejob" + new Date().getTime() + "cxwl" + new Random().nextInt() + path.substring(path.lastIndexOf("."))).append("|");
                        if ((index + 1) == selectedPhotos.size()) {
                            strs[7] = stringBuffer_Name.toString();
                            inputModel.setParams(strs);
                            V.d("全部图片已压缩完成,即将上送的图片路径：\n paths:" + arrayList_path.toString() + "\nnames:" + stringBuffer_Name.toString());
                            intent.putExtra("photosPaths", arrayList_path);/*压缩后图片路径的集合*/
                            intent.putExtra("inputModel", inputModel);/*该对象包括用户页面上输入的信息以及[7]为压缩完的图片名称sb*/
                            intent.putExtra("PayTypeInfo", PayTypeInfo.SendMoneyWork_Ori);/*支付的类型*/
                            AppManager.getAppManager().toNextPage(intent, true);
                        }
                    }
                }
            });
        } else {
            strs[7] = "";
            inputModel.setParams(strs);
            intent.putExtra("inputModel", inputModel);/*该对象包括用户页面上输入的信息以及[7]为压缩完的图片名称sb*/
            intent.putExtra("PayTypeInfo", PayTypeInfo.SendMoneyWork_Ori);/*支付的类型*/
            AppManager.getAppManager().toNextPage(intent, true);
        }

//        StringBuffer stringBuffer = new StringBuffer();
//        for (int i = 0; i < selectedPhotos.size(); i++) {
//            stringBuffer.append(selectedPhotos.get(i)).append("|");
//        }


//
//
//
//        if (selectedPhotos.size() > 0) {
//            intent.putExtra("photosPaths", selectedPhotos);
//        }
//        AppManager.getAppManager().toNextPage(intent, true);
//        //整理上传前的参数
//        callList.add(LoveJob.sendOriWork(inputModel, new OnAllParameListener() {
//            @Override
//            public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                dialog.dismiss();
//                V.d("发布成功");
//            }
//
//            @Override
//            public void onError(String msg) {
//                dialog.dismiss();
//                V.e("发布失败，" + msg);
//            }
//        }));
    }

    /**
     * 进入该方法时表示当前fragment是可见的，可以加载用户数据 注意：请做标示，当数据加载完一次后第二次不去加载
     */
    @Override
    public void loadData() {

    }

    private void initRecyclerView() {
        photoAdapter = new PhotoAdapter(context, selectedPhotos, false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rt_send_work_ori_title, R.id.rt_send_work_ori_want, R.id.rt_send_work_ori_location, R.id.rt_send_work_ori_price, R.id.rt_send_work_ori_person, R.id.rt_send_work_ori_phonenumber, R.id.rt_send_work_ori_countdown, R.id.bt1, R.id.bt2, R.id.main})
    public void onClick(View view) {
        Intent intent = new Intent(context, WriteView.class);
        String title = "";
        String content = "";
        int requestCode = -1;
        int writeType = -1;
        int maxLenth = -1;

        switch (view.getId()) {
            case R.id.rt_send_work_ori_title:
                title = "请输入工作名称";
                content = tvSendWorkOriTitle.getText().toString().trim();
                requestCode = StaticParams.RequestCode.RequestCode_SendOriWork_TO_WriteView_Title;
                maxLenth = 10;
                break;
            case R.id.rt_send_work_ori_want:
                title = "请输入工作需求";
                content = tvSendWorkOriWant.getText().toString().trim();
                requestCode = RequestCode_SendOriWork_TO_WriteView_Want;
                maxLenth = 50;
                break;
            case R.id.rt_send_work_ori_location:
//                title = "请输入工作地点";
//                content = tvSendWorkOriLocation.getText().toString().trim();
//                requestCode = RequestCode_SendOriWork_TO_WriteView_Location;
//                maxLenth = 20;
                Intent intent_1 = new Intent(context, BaiDuMapCitySelctor.class);
                intent_1.putExtra("requestCode", RequestCode_SendOriWork_LocationSelected);
                context.startActivityForResult(intent_1, RequestCode_SendOriWork_LocationSelected);
                break;
            case R.id.rt_send_work_ori_price:
                title = "请输入酬金";
                content = tvSendWorkOriPrice.getText().toString().trim();
                requestCode = RequestCode_SendOriWork_TO_WriteView_Price;
                writeType = 3;
                maxLenth = 8;

                break;
            case R.id.rt_send_work_ori_person:
                title = "请输入联系人";
                content = tvSendWorkOriPerson.getText().toString().trim();
                requestCode = RequestCode_SendOriWork_TO_WriteView_Person;
                maxLenth = 4;

                break;
            case R.id.rt_send_work_ori_phonenumber:
                title = "请输入联系电话";
                writeType = 1;
                content = tvSendWorkOriPhonenumber.getText().toString().trim();
                requestCode = RequestCode_SendOriWork_TO_WriteView_PhoneNumber;
                maxLenth = 11;

                break;
            case R.id.rt_send_work_ori_countdown:
                //倒计时
                TimePickerView timePickerView = new TimePickerView(context, TimePickerView.Type.ALL);
                timePickerView.show();
                timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        tvSendWorkOriCountdown.setText(new SimpleDateFormat("yyyy年MM月dd日HH时mm分").format(date));
                    }
                });
                break;
//            case R.id.bt1:
//                V.d("left");
//                main.startAnimation(getAnimation(isOpen ? 2 : 1));
//                PhotoPicker.builder()
//                        .setPhotoCount(3)
//                        .setShowCamera(false)
//                        .setSelected(selectedPhotos)
//                        .start(context);
//                break;
//            case R.id.bt2:
//                V.d("right");
//                main.startAnimation(getAnimation(isOpen ? 2 : 1));

            case R.id.main:
                PhotoPicker.builder()
                        .setPhotoCount(3)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(context);
                break;
        }
        if (requestCode != -1) {
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            intent.putExtra("requestCode", requestCode);
            intent.putExtra("writeType", writeType);
            intent.putExtra("maxLenth", maxLenth);
            context.startActivityForResult(intent, requestCode);
            requestCode = -1;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.setSelectorImgOnResult(requestCode, resultCode, data, photoAdapter, selectedPhotos);
//        V.d("图片选择完毕");
        String content = "";
        if (data == null) {
            return;
        }
        content = data.getStringExtra("content");
        if (TextUtils.isEmpty(content)) {
            return;
        }
        switch (requestCode) {
            case StaticParams.RequestCode.RequestCode_SendOriWork_TO_WriteView_Title:
                tvSendWorkOriTitle.setText(content);
                break;
            case RequestCode_SendOriWork_TO_WriteView_Want:
                tvSendWorkOriWant.setText(content);
                break;
            case RequestCode_SendOriWork_LocationSelected:
                tvSendWorkOriLocation.setText(content);
                break;
            case RequestCode_SendOriWork_TO_WriteView_Price:
                tvSendWorkOriPrice.setText(content + "元/次");
                break;
            case RequestCode_SendOriWork_TO_WriteView_Person:
                tvSendWorkOriPerson.setText(content);
                break;
            case RequestCode_SendOriWork_TO_WriteView_PhoneNumber:
                tvSendWorkOriPhonenumber.setText(content);
                break;
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
