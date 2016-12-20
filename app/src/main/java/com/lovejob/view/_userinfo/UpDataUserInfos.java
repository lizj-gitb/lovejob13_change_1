package com.lovejob.view._userinfo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.UserInputModel;
import com.lovejob.model.Utils;
import com.lovejob.model.bean.PriceBean;
import com.lovejob.qiniuyun.http.ResponseInfo;
import com.lovejob.qiniuyun.storage.UpCompletionHandler;
import com.lovejob.qiniuyun.storage.UploadManager;
import com.lovejob.view.WriteView;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pickerview.OptionsPickerView;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

import static com.lovejob.model.StaticParams.RequestCode.RequestCode_UpDataUserInfo_To_WriteView_CommPosition;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_UpDataUserInfo_To_WriteView_Commpl;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_UpDataUserInfo_To_WriteView_Location;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_UpDataUserInfo_To_WriteView_UserName;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob
 * Package_Name:com.lovejob.view._userinfo
 * Created on 2016-12-05 17:03
 */

public class UpDataUserInfos extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    @Bind(R.id.img_setuserinfo_logo)
    CircleImageView imgSetuserinfoLogo;
    @Bind(R.id.lt_setuserinfo_setuserlogo)
    LinearLayout ltSetuserinfoSetuserlogo;
    @Bind(R.id.tv_setuserinfo_name)
    TextView tvSetuserinfoName;
    @Bind(R.id.lt_setuserinfo_name)
    LinearLayout ltSetuserinfoName;
    @Bind(R.id.tv_setuserinfo_sex)
    TextView tvSetuserinfoSex;
    @Bind(R.id.lt_setuserinfo_sex)
    LinearLayout ltSetuserinfoSex;
    @Bind(R.id.tv_setuserinfo_commpl)
    TextView tvSetuserinfoCommpl;
    @Bind(R.id.lt_setuserinfo_commpl)
    LinearLayout ltSetuserinfoCommpl;
    @Bind(R.id.tv_setuserinfo_posstion)
    TextView tvSetuserinfoPosstion;
    @Bind(R.id.lt_setuserinfo_posstion)
    LinearLayout ltSetuserinfoPosstion;
    @Bind(R.id.tv_setuserinfo_location)
    TextView tvSetuserinfoLocation;
    @Bind(R.id.lt_setuserinfo_location)
    LinearLayout ltSetuserinfoLocation;
    @Bind(R.id.bt_setuserinfo_commit)
    Button btSetuserinfoCommit;
    @Bind(R.id.tv_setuserinfo_jobstate)
    TextView tvSetuserinfoJobstate;
    @Bind(R.id.lt_setuserinfo_jobstate)
    LinearLayout ltSetuserinfoJobstate;
    private boolean isUploadImgSucc = false;//图片是否上传成功
    private List<Call> calls = new ArrayList<>();

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.updatauserinfos);
        ButterKnife.bind(this);
        setActionbar();
        getUserDetails();
    }

    private void getUserDetails() {
        calls.add(LoveJob.getUserDetails(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                tvSetuserinfoName.setText(thePerfectGirl.getData().getUserInfoDTO().getRealName());
                tvSetuserinfoSex.setText(thePerfectGirl.getData().getUserInfoDTO().getUserSex() == 0 ? "女" : "男");
                tvSetuserinfoCommpl.setText(thePerfectGirl.getData().getUserInfoDTO().getCompany());
                tvSetuserinfoPosstion.setText(thePerfectGirl.getData().getUserInfoDTO().getPosition());
                tvSetuserinfoLocation.setText(thePerfectGirl.getData().getUserInfoDTO().getAddress() == null ? "" : thePerfectGirl.getData().getUserInfoDTO().getAddress().toString());
//                String jobstate ="";
//                switch (thePerfectGirl.getData().getUserInfoDTO().getJobState()){
//                    case "0":
//                        jobstate="离职";
//                        break;
//                    case "1":
//                        jobstate="在职";
//                        break;
//                    case "2":
//                        jobstate="在职·寻找新机会";
//                        break;
//
//                }
                tvSetuserinfoJobstate.setText(TextUtils.isEmpty(thePerfectGirl.getData().getUserInfoDTO().getJobState()) ? "" : thePerfectGirl.getData().getUserInfoDTO().getJobState());
                Glide.with(context).load(StaticParams.QiNiuYunUrl + thePerfectGirl.getData().getUserInfoDTO().getPortraitId()).placeholder(R.mipmap.ic_launcher).dontAnimate().into(imgSetuserinfoLogo);
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        }));
    }

    private void setActionbar() {
        actionbarTitle.setText("编辑个人信息");
        actionbarTitle.setTextSize(16);
        actionbarTitle.setTextColor(Color.WHITE);
        actionbarSave.setVisibility(View.GONE);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        for (Call callList : calls) {
            if (callList != null && !callList.isCanceled()) callList.cancel();
        }
    }

    private ArrayList<String> selectedPhotos = new ArrayList<>();

    @OnClick({R.id.actionbar_back, R.id.lt_setuserinfo_setuserlogo, R.id.lt_setuserinfo_jobstate, R.id.lt_setuserinfo_name, R.id.lt_setuserinfo_sex, R.id.lt_setuserinfo_commpl, R.id.lt_setuserinfo_posstion, R.id.lt_setuserinfo_location, R.id.bt_setuserinfo_commit})
    public void onClick(View view) {
        Intent intent = new Intent(context, WriteView.class);
        int requestCode = -1;
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.lt_setuserinfo_setuserlogo:
                //用户头像
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(this);
                break;
            case R.id.lt_setuserinfo_name:
                //用户姓名
                intent.putExtra("title", "请输入真实姓名");
                intent.putExtra("content", tvSetuserinfoName.getText() != null ? tvSetuserinfoName.getText().toString().trim() : "");
                intent.putExtra("requestCode", RequestCode_UpDataUserInfo_To_WriteView_UserName);
                intent.putExtra("writeType", -1);
                intent.putExtra("maxLenth", 4);
                requestCode = RequestCode_UpDataUserInfo_To_WriteView_UserName;
                break;
            case R.id.lt_setuserinfo_sex:
                //用户性别
                final ArrayList<PriceBean> options1Items = new ArrayList<>();
                options1Items.add(new PriceBean("男"));
                options1Items.add(new PriceBean("女"));
                OptionsPickerView pvOptions = new OptionsPickerView(this);
                pvOptions.setPicker(options1Items);
                pvOptions.setTitle("选择性别");
                pvOptions.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(1, 1, 1);
                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        tvSetuserinfoSex.setText(options1Items.get(options1).getPickerViewText());
//                        options1Items.clear();
                    }
                });
                pvOptions.show();
                break;
            case R.id.lt_setuserinfo_commpl:
                //公司名称
                intent.putExtra("title", "请输入公司名称");
                intent.putExtra("content", tvSetuserinfoCommpl.getText() != null ? tvSetuserinfoCommpl.getText().toString().trim() : "");
                intent.putExtra("requestCode", RequestCode_UpDataUserInfo_To_WriteView_Commpl);
                intent.putExtra("writeType", -1);
                intent.putExtra("maxLenth", 20);
                requestCode = RequestCode_UpDataUserInfo_To_WriteView_Commpl;
                break;
            case R.id.lt_setuserinfo_posstion:
                //职位
                intent.putExtra("title", "请输入您的职位");
                intent.putExtra("content", tvSetuserinfoPosstion.getText() != null ? tvSetuserinfoPosstion.getText().toString().trim() : "");
                intent.putExtra("requestCode", RequestCode_UpDataUserInfo_To_WriteView_CommPosition);
                intent.putExtra("writeType", -1);
                intent.putExtra("maxLenth", 10);
                requestCode = RequestCode_UpDataUserInfo_To_WriteView_CommPosition;
                break;
            case R.id.lt_setuserinfo_location:
                //地区
                intent.putExtra("title", "请输入您的地区");
                intent.putExtra("content", tvSetuserinfoLocation.getText() != null ? tvSetuserinfoLocation.getText().toString().trim() : "");
                intent.putExtra("requestCode", RequestCode_UpDataUserInfo_To_WriteView_Location);
                intent.putExtra("writeType", -1);
                intent.putExtra("maxLenth", 20);
                requestCode = RequestCode_UpDataUserInfo_To_WriteView_Location;
                break;
            case R.id.lt_setuserinfo_jobstate:
                final ArrayList<PriceBean> jobstates = new ArrayList<>();
                jobstates.add(new PriceBean("在职"));
                jobstates.add(new PriceBean("离职"));
                jobstates.add(new PriceBean("在职·寻找新机会"));

                OptionsPickerView pvOptions1 = new OptionsPickerView(this);
                pvOptions1.setPicker(jobstates);
                pvOptions1.setTitle("在职状态");
                pvOptions1.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions1.setSelectOptions(1, 1, 1);
                pvOptions1.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        tvSetuserinfoJobstate.setText(jobstates.get(options1).getPickerViewText());
                    }
                });
                pvOptions1.show();
                break;
            case R.id.bt_setuserinfo_commit:
                //提交
                updataUserInfos();
                break;
        }
        if (requestCode > 0) {
            AppManager.getAppManager().toNextPage(intent, requestCode);
            requestCode = -1;
        }
    }

    private void updataUserInfos() {
        final UserInputModel inputModel = Utils.checkUserInputParams(tvSetuserinfoName, tvSetuserinfoSex, tvSetuserinfoCommpl, tvSetuserinfoPosstion, tvSetuserinfoLocation, tvSetuserinfoJobstate);
        if (!inputModel.isNotEmpty()) {
            Utils.showToast(context, "不能有空值");
            return;
        }
        dialog = Utils.showProgressDliago(context, "正在更新用户资料");
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {

                calls.add(LoveJob.updataUserInfo(inputModel.getParams()[0], inputModel.getParams()[1],
                        inputModel.getParams()[2], inputModel.getParams()[3], inputModel.getParams()[4],
                        inputModel.getParams()[5], new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                             AppPreferences appPreferences=   new AppPreferences(context);
                                appPreferences.put(StaticParams.FileKey.__IDENTIFY__, "true");
                                dialog.dismiss();
                                Utils.showToast(context, "更新成功");
                                appPreferences = null;
                                AppManager.getAppManager().finishActivity(context);
                            }

                            @Override
                            public void onError(String msg) {
                                dialog.dismiss();
                                Utils.showToast(context, msg);
                            }
                        }));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;

        switch (requestCode) {
            case RequestCode_UpDataUserInfo_To_WriteView_UserName:
                V.d("输入姓名返回");
                tvSetuserinfoName.setText(data.getStringExtra("content"));
                break;
            case RequestCode_UpDataUserInfo_To_WriteView_Commpl:
                V.d("输入公司名称返回");
                tvSetuserinfoCommpl.setMaxLines(1);
                tvSetuserinfoCommpl.setText(data.getStringExtra("content"));
                break;

            case RequestCode_UpDataUserInfo_To_WriteView_CommPosition:
                V.d("输入职位返回");
                tvSetuserinfoPosstion.setMaxLines(1);
                tvSetuserinfoPosstion.setText(data.getStringExtra("content"));
                break;

            case RequestCode_UpDataUserInfo_To_WriteView_Location:
                V.d("输入地区返回");
                tvSetuserinfoLocation.setMaxLines(1);
                tvSetuserinfoLocation.setText(data.getStringExtra("content"));
                break;
            case PhotoPicker.REQUEST_CODE:
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (photos.size() > 0) {
                    imgSetuserinfoLogo.setImageBitmap(Utils.getBitmapFromPath(photos.get(0)));
                    Utils.yasuo(context, photos, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.arg1 == 9000) {
//                                    File saveFile = new File(getExternalCacheDir(), "compress_" + System.currentTimeMillis() + ".jpg");
//                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(msg.getData().getString("path")));
//                                    NativeUtil.compressBitmap(bitmap, saveFile.getAbsolutePath());
                                final String path = msg.getData().getString("path");
                                //开始上传图片  上传的图片路径为msg.getData().getString("path")
                                HandlerUtils.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        calls.add(LoveJob.upDataUserLogo(path, new OnAllParameListener() {
                                            @Override
                                            public void onSuccess(ThePerfectGirl thePerfectGirl) {

                                                UploadManager uploadManager = new UploadManager();
                                                uploadManager.put(path, path,
                                                        thePerfectGirl.getData().getUploadToken(), new UpCompletionHandler() {
                                                            @Override
                                                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                                                V.d("上传成功");
                                                            }
                                                        }, null);
                                            }

                                            @Override
                                            public void onError(String msg) {
                                                Utils.showToast(context, msg);
                                            }
                                        }));
                                    }
                                });
//                                uploadManager.put(msg.getData().getString("path"), imgNames.get(msg.getData().getInt("index")), uploadToken,
//                                        new UpCompletionHandler() {
//                                            @Override
//                                            public void complete(String key, ResponseInfo info, JSONObject res) {
//                                                //res包含hash、key等信息，具体字段取决于上传策略的设置。
//                                                V.d("上传状态回调：" + key + ",\r\n " + info + ",\r\n " + res);
//                                                uploadImgToQNY_size_add[0]++;
//                                                V.d("index:" + uploadImgToQNY_size_add[0]);
//                                                V.d("size:" + ((photoAdapter.getList().size())));
//                                                if (uploadImgToQNY_size_add[0] == photoAdapter.getList().size()) {
//                                                    Utils.showToast(context, "发布成功");
////                                        Utils.dissmissDiV(context);
//                                                    context.setResult(resultCode);
//                                                    context.finish();
//                                                }
//                                            }
//                                        }, null);
                            } else {
                                V.e("压缩失败一次");
                            }
                        }
                    });
                }
                break;
        }
    }


}
