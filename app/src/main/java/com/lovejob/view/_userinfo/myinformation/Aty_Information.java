package com.lovejob.view._userinfo.myinformation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
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

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by Administrator on 2016/11/30.
 */
public class Aty_Information extends BaseActivity {
    @Bind(R.id.img_inf_back)
    ImageView imgInfBack;
    @Bind(R.id.tv_inf_submit)
    TextView tvInfSubmit;
    @Bind(R.id.img_user_head)
    ImageView imgUserHead;
    @Bind(R.id.img_camera)
    ImageView imgCamera;
    @Bind(R.id.tv_infor_name)
    TextView tvInforName;
    @Bind(R.id.img_inf_editname)
    ImageView imgInfEditname;
    @Bind(R.id.img_infor_sex_boy)
    ImageView imgInforSexBoy;
    @Bind(R.id.tv_infor_sex_boy)
    TextView tvInforSexBoy;
    @Bind(R.id.rl_infor_sex_boy)
    LinearLayout rlInforSexBoy;
    @Bind(R.id.img_infor_sex_girl)
    ImageView imgInforSexGirl;
    @Bind(R.id.tv_infor_sex_girl)
    TextView tvInforSexGirl;
    @Bind(R.id.rl_infor_sex_girl)
    LinearLayout rlInforSexGirl;
    @Bind(R.id.img_xx)
    TextView imgXx;
    @Bind(R.id.tv_infor_height)
    TextView tvInforHeight;
    @Bind(R.id.img_gg)
    ImageView imgGg;
    @Bind(R.id.rl_infor_height)
    RelativeLayout rlInforHeight;
    @Bind(R.id.img_xx1)
    TextView imgXx1;
    @Bind(R.id.img_gg1)
    ImageView imgGg1;
    @Bind(R.id.tv_infor_age)
    TextView tvInforAge;
    @Bind(R.id.rl_infor_age)
    RelativeLayout rlInforAge;
    @Bind(R.id.img_xx2)
    TextView imgXx2;
    @Bind(R.id.img_gg2)
    ImageView imgGg2;
    @Bind(R.id.tv_infor_address)
    TextView tvInforAddress;
    @Bind(R.id.rl_infor_address)
    RelativeLayout rlInforAddress;
    @Bind(R.id.img_gg3)
    ImageView imgGg3;
    @Bind(R.id.tv_infor_education)
    TextView tvInforEducation;
    @Bind(R.id.rl_infor_education)
    RelativeLayout rlInforEducation;
    @Bind(R.id.img_gg4)
    ImageView imgGg4;
    @Bind(R.id.tv_infor_major)
    TextView tvInforMajor;
    @Bind(R.id.rl_infor_major)
    RelativeLayout rlInforMajor;
    @Bind(R.id.img_gg5)
    ImageView imgGg5;
    @Bind(R.id.tv_infor_school)
    TextView tvInforSchool;
    @Bind(R.id.rl_infor_school)
    RelativeLayout rlInforSchool;
    @Bind(R.id.tv_gg6)
    TextView tvGg6;
    @Bind(R.id.et_infor_experience)
    EditText etInforExperience;
    @Bind(R.id.tv_gg8)
    TextView tvGg8;
    @Bind(R.id.et_infor_industry)
    EditText etInforIndustry;
    @Bind(R.id.tv_gg9)
    TextView tvGg9;
    @Bind(R.id.et_infor_worke)
    EditText etInforWorke;
    @Bind(R.id.tv_gg10)
    TextView tvGg10;
    @Bind(R.id.et_infor_skill)
    EditText etInforSkill;
    @Bind(R.id.et_infor_mine)
    EditText etInforMine;
    private String userPid;
    private String Sex = "1";
    private Call call_getResumeList, call_saveResume;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_basicinformation);
        ButterKnife.bind(this);
        context = this;
        AppPreferences appPreferences = new AppPreferences(context);
        userPid = appPreferences.getString(StaticParams.FileKey.__USERPID__, "");
        addData();
    }

    private void addData() {
        call_getResumeList = LoveJob.getResumeList(userPid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getResumeDTO() != null) {
                    tvInforName.setText(thePerfectGirl.getData().getResumeDTO().getName() + "".trim());

                    if (thePerfectGirl.getData().getResumeDTO().getSex() == 1) {
                        imgInforSexBoy.setImageResource(R.mipmap.cicle_choose_ok);
                        tvInforSexBoy.setTextColor(getResources().getColor(R.color.actionbar));
                        imgInforSexGirl.setImageResource(R.mipmap.cicle_choose);
                        tvInforSexGirl.setTextColor(getResources().getColor(R.color.hiteTextColor));
                        Sex = "1";
                    } else {
                        imgInforSexBoy.setImageResource(R.mipmap.cicle_choose);
                        tvInforSexBoy.setTextColor(getResources().getColor(R.color.hiteTextColor));
                        imgInforSexGirl.setImageResource(R.mipmap.cicle_choose_ok);
                        tvInforSexGirl.setTextColor(getResources().getColor(R.color.actionbar));
                        Sex = "0";
                    }
                    tvInforHeight.setText(thePerfectGirl.getData().getResumeDTO().getHeight() + "".trim());
                    tvInforAge.setText(thePerfectGirl.getData().getResumeDTO().getAge() + "".trim());
                    tvInforAddress.setText(thePerfectGirl.getData().getResumeDTO().getAddress() + "".trim());
                    tvInforEducation.setText(thePerfectGirl.getData().getResumeDTO().getEducation() + "".trim());
                    tvInforMajor.setText(thePerfectGirl.getData().getResumeDTO().getMajor() + "".trim());
                    tvInforSchool.setText(thePerfectGirl.getData().getResumeDTO().getSchool() + "".trim());
                    etInforExperience.setText(thePerfectGirl.getData().getResumeDTO().getEducationExperience() + "".trim());
                    etInforIndustry.setText(thePerfectGirl.getData().getResumeDTO().getIndustryDirection() + "".trim());
                    etInforWorke.setText(thePerfectGirl.getData().getResumeDTO().getExperience() + "".trim());
                    etInforSkill.setText(thePerfectGirl.getData().getResumeDTO().getSkill() + "".trim());
                    etInforMine.setText(thePerfectGirl.getData().getResumeDTO().getPersonalEvaluation());
                    Glide.with(context).load(StaticParams.QiNiuYunUrl+thePerfectGirl.getData().getResumeDTO().getPortraitId()).dontAnimate().placeholder(R.drawable.ic_launcher).into(imgUserHead);

                }
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
        if (call_saveResume != null && !call_saveResume.isCanceled())
            call_saveResume.cancel();

    }

    ArrayList<PriceBean> options1Items;
    OptionsPickerView pvOptions;
    private ArrayList<String> selectedPhotos = new ArrayList<>();

    @OnClick({R.id.img_inf_back, R.id.tv_inf_submit, R.id.img_user_head, R.id.img_inf_editname, R.id.rl_infor_sex_boy, R.id.rl_infor_sex_girl, R.id.rl_infor_height, R.id.rl_infor_age, R.id.rl_infor_address, R.id.rl_infor_major, R.id.rl_infor_school, R.id.rl_infor_education})
    public void onClick(View view) {
        final Intent in = new Intent(context, WriteView.class);
        int requestCode = -1;
        int maxLenth = 10;
        int writeType = -1;
        switch (view.getId()) {
            case R.id.img_inf_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tv_inf_submit:
                Submit();
                dialog = Utils.showProgressDliago(context, "正在保存，请稍后……");
                break;
            case R.id.img_user_head:
                //TODO 头像选择
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(this);
                break;
            case R.id.img_inf_editname:
                in.putExtra("title", "姓名");
                in.putExtra("content", tvInforName.getText() == null ? "" : tvInforName.getText().toString());
                requestCode = StaticParams.RequestCode.RequsetCode_Person_UserName;
                maxLenth = 4;
                break;
            case R.id.rl_infor_sex_boy:
                imgInforSexBoy.setImageResource(R.mipmap.cicle_choose_ok);
                tvInforSexBoy.setTextColor(getResources().getColor(R.color.actionbar));
                imgInforSexGirl.setImageResource(R.mipmap.cicle_choose);
                tvInforSexGirl.setTextColor(getResources().getColor(R.color.hiteTextColor));
                Sex = "1";
                break;
            case R.id.rl_infor_sex_girl:
                imgInforSexBoy.setImageResource(R.mipmap.cicle_choose);
                tvInforSexBoy.setTextColor(getResources().getColor(R.color.hiteTextColor));
                imgInforSexGirl.setImageResource(R.mipmap.cicle_choose_ok);
                tvInforSexGirl.setTextColor(getResources().getColor(R.color.actionbar));
                Sex = "0";
                break;
            case R.id.rl_infor_height:
                in.putExtra("title", "身高");
                in.putExtra("writeType", 1);
                in.putExtra("content", tvInforHeight.getText() == null ? "" : tvInforHeight.getText().toString());
                requestCode = StaticParams.RequestCode.RequsetCode_Person_High;
                maxLenth = 3;
                writeType = 1;
                break;
            case R.id.rl_infor_age:
                in.putExtra("title", "年龄");
                in.putExtra("writeType", 1);
                in.putExtra("content", tvInforName.getText() == null ? "" : tvInforName.getText().toString());
                requestCode = StaticParams.RequestCode.RequsetCode_Person_Age;
                maxLenth = 3;
                writeType = 1;
                break;
            case R.id.rl_infor_address:
                in.putExtra("title", "住址");
                in.putExtra("content", tvInforAddress.getText() == null ? "" : tvInforAddress.getText().toString());
                requestCode = StaticParams.RequestCode.RequsetCode_Person_Address;
                maxLenth = 18;
                break;
            case R.id.rl_infor_major:
                in.putExtra("title", "专业");
                in.putExtra("content", tvInforMajor.getText() == null ? "" : tvInforMajor.getText().toString());
                requestCode = StaticParams.RequestCode.RequsetCode_Person_Major;
                break;
            case R.id.rl_infor_school:
                in.putExtra("title", "学校");
                in.putExtra("content", tvInforSchool.getText() == null ? "" : tvInforSchool.getText().toString());
                requestCode = StaticParams.RequestCode.RequsetCode_Person_School;
                break;
            case R.id.rl_infor_education:
                options1Items = new ArrayList<>();
                options1Items.add(new PriceBean("不限"));
                options1Items.add(new PriceBean("大专"));
                options1Items.add(new PriceBean("本科"));
                options1Items.add(new PriceBean("硕士"));
                options1Items.add(new PriceBean("博士"));
                pvOptions = new OptionsPickerView(this);
                pvOptions.setPicker(options1Items);
                pvOptions.setTitle("学历要求");
                pvOptions.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(1, 1, 1);
                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        tvInforEducation.setText(options1Items.get(options1).getPickerViewText());
                    }
                });
                pvOptions.show();
                break;
        }
        if (requestCode > 0) {
            in.putExtra("requestCode", requestCode);
            in.putExtra("writeType", writeType);
            in.putExtra("maxLenth", maxLenth);
            startActivityForResult(in, requestCode);
            requestCode = -1;
        }
    }

    private void Submit() {
        if (TextUtils.isEmpty(tvInforName.getText())) {
            Utils.showToast(this, "用户姓名不可为空");
            return;
        }
        if (TextUtils.isEmpty(tvInforAge.getText())) {
            Utils.showToast(this, "用户年龄不可为空");
            return;
        }
        if (TextUtils.isEmpty(tvInforHeight.getText())) {
            Utils.showToast(this, "用户身高不可为空");
            return;
        }
        if (TextUtils.isEmpty(tvInforAddress.getText())) {
            Utils.showToast(this, "用户地址不可为空");
            return;
        }
        call_saveResume = LoveJob.saveResume(path, tvInforName.getText().toString(), Sex, tvInforHeight.getText().toString(),
                tvInforAge.getText().toString(), tvInforAddress.getText().toString(), tvInforEducation.getText().toString()
                , tvInforMajor.getText().toString(), tvInforSchool.getText().toString(), etInforExperience.getText().toString(), etInforIndustry.getText().toString(),
                etInforWorke.getText().toString(), etInforSkill.getText().toString(), etInforMine.getText().toString(), new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                            UploadManager uploadManager = new UploadManager();
                            uploadManager.put(path, path, thePerfectGirl.getData().getUploadToken(), new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    dialog.dismiss();
                                    Utils.showToast(context, "保存成功");
                                }
                            }, null);
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                });
    }

    String path = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        switch (requestCode) {
            case StaticParams.RequestCode.RequsetCode_Person_UserName:
                tvInforName.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequsetCode_Person_High:
                tvInforHeight.setText(data.getStringExtra("content") + "cm");
                break;
            case StaticParams.RequestCode.RequsetCode_Person_Age:
                tvInforAge.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequsetCode_Person_Address:
                tvInforAddress.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequsetCode_Person_Education:
                tvInforEducation.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequsetCode_Person_Major:
                tvInforMajor.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequsetCode_Person_School:
                tvInforSchool.setText(data.getStringExtra("content"));
                break;
            case PhotoPicker.REQUEST_CODE:
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (photos.size() > 0) {
                    imgUserHead.setImageBitmap(Utils.getBitmapFromPath(photos.get(0)));
                    Utils.yasuo(context, photos, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.arg1 == 9000) {
//                                    File saveFile = new File(getExternalCacheDir(), "compress_" + System.currentTimeMillis() + ".jpg");
//                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(msg.getData().getString("path")));
//                                    NativeUtil.compressBitmap(bitmap, saveFile.getAbsolutePath());
                                path = msg.getData().getString("path");
                            }
                        }
                    });
                }
                break;

        }
    }
}
