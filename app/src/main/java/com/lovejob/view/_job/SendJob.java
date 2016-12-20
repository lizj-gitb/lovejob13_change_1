package com.lovejob.view._job;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.baidumap.BaiDuMapCitySelctor;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.UserInputModel;
import com.lovejob.model.Utils;
import com.lovejob.model.bean.PriceBean;
import com.lovejob.view.JobTypeAty;
import com.lovejob.view.payinfoviews.SendJobSuccess;
import com.lovejob.view.WriteView;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.pickerview.OptionsPickerView;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.utils.V;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendJobWork_LocationSelected;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.view._job
 * Created on 2016-11-28 16:45
 */

public class SendJob extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    @Bind(R.id.tv_permanert_job_name)
    TextView tvPermanertJobName;
    @Bind(R.id.img_permanert_job_name)
    ImageView imgPermanertJobName;
    @Bind(R.id.rel_permanert_job_name)
    RelativeLayout relPermanertJobName;
    @Bind(R.id.tv_permanert_job_position)
    TextView tvPermanertJobPosition;
    @Bind(R.id.img_permanert_job_position)
    ImageView imgPermanertJobPosition;
    @Bind(R.id.rel_permanert_job_position)
    RelativeLayout relPermanertJobPosition;
    @Bind(R.id.tv_permanert_job_wage)
    TextView tvPermanertJobWage;
    @Bind(R.id.img_permanert_job_wage)
    ImageView imgPermanertJobWage;
    @Bind(R.id.rel_permanert_job_wage)
    RelativeLayout relPermanertJobWage;
    @Bind(R.id.tv_permanert_job_sex)
    TextView tvPermanertJobSex;
    @Bind(R.id.img_permanert_job_sex)
    ImageView imgPermanertJobSex;
    @Bind(R.id.rel_permanert_job_sex)
    RelativeLayout relPermanertJobSex;
    @Bind(R.id.tv_permanert_job_experience)
    TextView tvPermanertJobExperience;
    @Bind(R.id.img_permanert_job_experience)
    ImageView imgPermanertJobExperience;
    @Bind(R.id.rel_permanert_job_experience)
    RelativeLayout relPermanertJobExperience;
    @Bind(R.id.tv_permanert_job_education_background)
    TextView tvPermanertJobEducationBackground;
    @Bind(R.id.img_permanert_job_education_background)
    ImageView imgPermanertJobEducationBackground;
    @Bind(R.id.rel_permanert_job_education_background)
    RelativeLayout relPermanertJobEducationBackground;
    @Bind(R.id.tv_permanert_job_age)
    TextView tvPermanertJobAge;
    @Bind(R.id.img_permanert_job_age)
    ImageView imgPermanertJobAge;
    @Bind(R.id.rel_permanert_job_age)
    RelativeLayout relPermanertJobAge;
    @Bind(R.id.tv_permanert_job_skill)
    TextView tvPermanertJobSkill;
    @Bind(R.id.img_permanert_job_skill)
    ImageView imgPermanertJobSkill;
    @Bind(R.id.rel_permanert_job_skill)
    RelativeLayout relPermanertJobSkill;
    @Bind(R.id.tv_permanert_job_recryit)
    TextView tvPermanertJobRecryit;
    @Bind(R.id.img_permanert_job_recryit)
    ImageView imgPermanertJobRecryit;
    @Bind(R.id.rel_permanert_job_recruit)
    RelativeLayout relPermanertJobRecruit;
    @Bind(R.id.tv_permanert_job_site)
    TextView tvPermanertJobSite;
    @Bind(R.id.img_permanert_job_site)
    ImageView imgPermanertJobSite;
    @Bind(R.id.rel_permanert_job_site)
    RelativeLayout relPermanertJobSite;
    @Bind(R.id.tv_permanert_job_content)
    TextView tvPermanertJobContent;
    @Bind(R.id.img_permanert_job_content)
    ImageView imgPermanertJobContent;
    @Bind(R.id.rel_permanert_job_content)
    RelativeLayout relPermanertJobContent;
    @Bind(R.id.tv_permanert_job_time)
    TextView tvPermanertJobTime;
    @Bind(R.id.img_permanert_job_time)
    ImageView imgPermanertJobTime;
    @Bind(R.id.rel_permanert_job_time)
    RelativeLayout relPermanertJobTime;
    @Bind(R.id.tv_permanert_job_phone)
    TextView tvPermanertJobPhone;
    @Bind(R.id.img_permanert_job_phone)
    ImageView imgPermanertJobPhone;
    @Bind(R.id.rel_permanert_job_phone)
    RelativeLayout relPermanertJobPhone;
    private String positionName, positionNumber;
    private String identify;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.sendjob);
        ButterKnife.bind(this);
        AppPreferences preferences = new AppPreferences(context);
        identify = preferences.getString(StaticParams.FileKey.__IDENTIFY__,"");
        if (identify.equals("false")){
            Utils.showToast(context,"请填写个人资料");
            AppManager.getAppManager().finishActivity();
            return ;
        }
        setDefaultParams();
    }

    private void setDefaultParams() {
        actionbarSave.setText("发布");
        actionbarTitle.setText("发布长期工作");
        actionbarTitle.setTextColor(Color.WHITE);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    ArrayList<PriceBean> options1Items;
    OptionsPickerView pvOptions;

    @OnClick({R.id.actionbar_back, R.id.actionbar_save, R.id.rel_permanert_job_name, R.id.rel_permanert_job_position, R.id.rel_permanert_job_wage, R.id.rel_permanert_job_sex, R.id.rel_permanert_job_experience, R.id.rel_permanert_job_education_background, R.id.rel_permanert_job_age, R.id.rel_permanert_job_skill, R.id.rel_permanert_job_recruit, R.id.rel_permanert_job_site, R.id.rel_permanert_job_content, R.id.rel_permanert_job_time, R.id.rel_permanert_job_phone})
    public void onClick(View view) {
        final Intent in = new Intent(context, WriteView.class);
        int requestCode = -1;
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.actionbar_save:
                dialog = Utils.showProgressDliago(context, "正在发布工作");
                UserInputModel inputModel = Utils.checkUserInputParams(tvPermanertJobName, tvPermanertJobPosition, tvPermanertJobWage,
                        tvPermanertJobSex, tvPermanertJobExperience, tvPermanertJobEducationBackground,
                        tvPermanertJobAge, tvPermanertJobSkill, tvPermanertJobRecryit, tvPermanertJobSite, tvPermanertJobContent, tvPermanertJobTime, tvPermanertJobPhone);
                V.d(inputModel.toString());
                if (inputModel.isNotEmpty()) {
                    V.d("发布工作");
                    LoveJob.sendJob(inputModel, positionNumber, new OnAllParameListener() {
                        @Override
                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                            dialog.dismiss();
//                            Utils.showToast(context, "发布成功");
                            Intent intent = new Intent(context, SendJobSuccess.class);
                            //TODO 模拟数据
                            if (thePerfectGirl.getData().getWorkPid() == null) {
                                Utils.showToast(context, "发布失败");
                                return;
                            }
                            intent.putExtra("workPid", thePerfectGirl.getData().getWorkPid());
                            startActivity(intent);
                            AppManager.getAppManager().finishActivity(context);
                        }

                        @Override
                        public void onError(String msg) {
                            dialog.dismiss();
                            Utils.showToast(context, msg);
                        }
                    });
                } else {
                    dialog.dismiss();
                    Utils.showToast(context, "不能有空值");
                }
                break;
            case R.id.rel_permanert_job_name:
                in.putExtra("title", "职位名称");
                in.putExtra("content", tvPermanertJobName.getText() == null ? "" : tvPermanertJobName.getText().toString());
                in.putExtra("maxLenth", 10);
                requestCode = StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Name;
                break;
            case R.id.rel_permanert_job_position:
                startActivityForResult(new Intent(this, JobTypeAty.class), StaticParams.RequestCode.RequestCode_ToWheelSelector_jobtype);
                break;
            case R.id.rel_permanert_job_wage:
                in.putExtra("title", "薪资待遇");
                in.putExtra("content", tvPermanertJobWage.getText() == null ? "" : tvPermanertJobWage.getText().toString());
                in.putExtra("maxLenth", 8);
                in.putExtra("writeType", 3);
                requestCode = StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Price;
//                options1Items = new ArrayList<>();
//                options1Items.add(new PriceBean("1000~2000"));
//                options1Items.add(new PriceBean("2000~3000"));
//                options1Items.add(new PriceBean("3000~5000"));
//                options1Items.add(new PriceBean("5000~8000"));
//                options1Items.add(new PriceBean("8000~10000"));
//                options1Items.add(new PriceBean("10000以上"));
//
//                ArrayList<ArrayList<String>> op = new ArrayList<>();
//                final ArrayList<String> l = new ArrayList<>();
////                l.add("次");
////                l.add("时");
////                l.add("日");
//                l.add("月");
////                l.add("年");
//
//                op.add(l);
//
//                pvOptions = new OptionsPickerView(this);
//                pvOptions.setPicker(options1Items, op, false);
//                pvOptions.setTitle("选择薪资待遇");
//                pvOptions.setCyclic(false, true, true);
//                //设置默认选中的三级项目
//                //监听确定选择按钮
//                pvOptions.setSelectOptions(1, 1, 1);
//                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
//
//                    @Override
//                    public void onOptionsSelect(int options1, int option2, int options3) {
//                        //返回的分别是三个级别的选中位置
//                        String tx = options1Items.get(options1).getPickerViewText();
//                        String tx2 = l.get(option2);
//                        tvPermanertJobWage.setText(tx + "/" + tx2);
//                        V.d(tx);
//                    }
//                });
//                pvOptions.show();
                break;
            case R.id.rel_permanert_job_sex:
                options1Items = new ArrayList<>();
                options1Items.add(new PriceBean("男"));
                options1Items.add(new PriceBean("女"));
                options1Items.add(new PriceBean("不限"));
                pvOptions = new OptionsPickerView(this);
                pvOptions.setPicker(options1Items);
                pvOptions.setTitle("选择性别");
                pvOptions.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(1, 1, 1);
                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        tvPermanertJobSex.setText(options1Items.get(options1).getPickerViewText());
                    }
                });
                pvOptions.show();
                break;
            case R.id.rel_permanert_job_experience:
                options1Items = new ArrayList<>();
                options1Items.add(new PriceBean("不限"));
                options1Items.add(new PriceBean("应届生"));
                options1Items.add(new PriceBean("一年以内"));
                options1Items.add(new PriceBean("1~3年"));
                options1Items.add(new PriceBean("3~5年"));
                options1Items.add(new PriceBean("5~10年"));
                options1Items.add(new PriceBean("10年以上"));

                pvOptions = new OptionsPickerView(this);
                pvOptions.setPicker(options1Items);
                pvOptions.setTitle("经验要求");
                pvOptions.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(1, 1, 1);
                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        tvPermanertJobExperience.setText(options1Items.get(options1).getPickerViewText());
                    }
                });
                pvOptions.show();
                break;
            case R.id.rel_permanert_job_education_background:
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
                        tvPermanertJobEducationBackground.setText(options1Items.get(options1).getPickerViewText());
                    }
                });
                pvOptions.show();
                break;
            case R.id.rel_permanert_job_age:
                options1Items = new ArrayList<>();

                options1Items.add(new PriceBean("不限"));
                options1Items.add(new PriceBean("18~22岁"));
                options1Items.add(new PriceBean("22~26岁"));
                options1Items.add(new PriceBean("27~35岁"));
                options1Items.add(new PriceBean("35岁以上"));
                pvOptions = new OptionsPickerView(this);
                pvOptions.setPicker(options1Items);
                pvOptions.setTitle("年龄要求");
                pvOptions.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(1, 1, 1);
                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        tvPermanertJobAge.setText(options1Items.get(options1).getPickerViewText());
                    }
                });
                pvOptions.show();
                break;
            case R.id.rel_permanert_job_skill:
                in.putExtra("title", "技能要求");
                in.putExtra("content", tvPermanertJobSkill.getText() == null ? "" : tvPermanertJobSkill.getText().toString());
                requestCode = StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Skill;
                break;
            case R.id.rel_permanert_job_recruit:
                in.putExtra("title", "招聘人数");
                in.putExtra("writeType", 1);
                in.putExtra("content", tvPermanertJobRecryit.getText() == null ? "" : tvPermanertJobRecryit.getText().toString());
                requestCode = StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Number;
                break;
            case R.id.rel_permanert_job_site:
//                in.putExtra("title", "工作地点");
//                in.putExtra("content", tvPermanertJobSite.getText() == null ? "" : tvPermanertJobSite.getText().toString());
//                requestCode = StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Location;
                //打开地图选择位置信息 TODO
                Intent intent = new Intent(context,BaiDuMapCitySelctor.class);
                intent.putExtra("requestCode", RequestCode_SendJobWork_LocationSelected);
                context.startActivityForResult(intent, RequestCode_SendJobWork_LocationSelected);
                break;
            case R.id.rel_permanert_job_content:
                in.putExtra("title", "工作内容");
                in.putExtra("content", tvPermanertJobContent.getText() == null ? "" : tvPermanertJobContent.getText().toString());
                requestCode = StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Content;
                break;
            case R.id.rel_permanert_job_time:
                options1Items = new ArrayList<>();
                options1Items.add(new PriceBean("00:00"));
                options1Items.add(new PriceBean("01:00"));
                options1Items.add(new PriceBean("02:00"));
                options1Items.add(new PriceBean("03:00"));
                options1Items.add(new PriceBean("04:00"));
                options1Items.add(new PriceBean("05:00"));
                options1Items.add(new PriceBean("06:00"));
                options1Items.add(new PriceBean("07:00"));
                options1Items.add(new PriceBean("08:00"));
                options1Items.add(new PriceBean("09:00"));
                options1Items.add(new PriceBean("10:00"));
                options1Items.add(new PriceBean("11:00"));
                options1Items.add(new PriceBean("12:00"));
                options1Items.add(new PriceBean("13:00"));
                options1Items.add(new PriceBean("14:00"));
                options1Items.add(new PriceBean("15:00"));
                options1Items.add(new PriceBean("16:00"));
                options1Items.add(new PriceBean("17:00"));
                options1Items.add(new PriceBean("18:00"));
                options1Items.add(new PriceBean("19:00"));
                options1Items.add(new PriceBean("20:00"));
                options1Items.add(new PriceBean("21:00"));
                options1Items.add(new PriceBean("22:00"));
                options1Items.add(new PriceBean("23:00"));


                final ArrayList<String> options2Items_01 = new ArrayList<>();
                options2Items_01.add("00:00");
                options2Items_01.add("01:00");
                options2Items_01.add("02:00");
                options2Items_01.add("03:00");
                options2Items_01.add("04:00");
                options2Items_01.add("05:00");
                options2Items_01.add("06:00");
                options2Items_01.add("07:00");
                options2Items_01.add("08:00");
                options2Items_01.add("09:00");
                options2Items_01.add("10:00");
                options2Items_01.add("11:00");
                options2Items_01.add("12:00");
                options2Items_01.add("13:00");
                options2Items_01.add("14:00");
                options2Items_01.add("15:00");
                options2Items_01.add("16:00");
                options2Items_01.add("17:00");
                options2Items_01.add("18:00");
                options2Items_01.add("19:00");
                options2Items_01.add("20:00");
                options2Items_01.add("21:00");
                options2Items_01.add("22:00");
                options2Items_01.add("23:00");
                pvOptions = new OptionsPickerView(this);

                final ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
                options2Items.add(options2Items_01);
                pvOptions.setPicker(options1Items, options2Items, false);
                pvOptions.setTitle("时间选择");
                pvOptions.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(1, 1, 1);
                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        V.d("----" + options1 + "," + option2 + "," + options3);
                        tvPermanertJobTime.setText(options1Items.get(options1).getPickerViewText() + "-" + options2Items_01.get(option2));
                    }
                });
                pvOptions.show();
                break;
            case R.id.rel_permanert_job_phone:
                in.putExtra("title", "联系电话");
                in.putExtra("writeType", 1);
                in.putExtra("maxLenth", 11);
                in.putExtra("content", tvPermanertJobPhone.getText() == null ? "" : tvPermanertJobPhone.getText().toString());
                requestCode = StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_WorkType;
                break;
        }
        if (requestCode > 0) {
            in.putExtra("requestCode", requestCode);
            startActivityForResult(in, requestCode);
            requestCode = -1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        V.d("code:" + resultCode);
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Name:
                tvPermanertJobName.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Skill:
                tvPermanertJobSkill.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Number:
                tvPermanertJobRecryit.setText(data.getStringExtra("content"));
                break;

//            case StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Location:
//                tvPermanertJobSite.setText(data.getStringExtra("content"));
//                break;
            case RequestCode_SendJobWork_LocationSelected:
                tvPermanertJobSite.setMaxLines(1);
                tvPermanertJobSite.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Content:
                tvPermanertJobContent.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_WorkType:
                tvPermanertJobPhone.setText(data.getStringExtra("content"));
                break;
            case StaticParams.RequestCode.RequestCode_ToWheelSelector_jobtype:
                positionName = data.getStringExtra("positionName");
                positionNumber = data.getStringExtra("positionNumber");
                tvPermanertJobPosition.setText(positionName);
                break;

            case StaticParams.RequestCode.RequestCode_F_SendJob_TO_WriteView_Price:
                tvPermanertJobWage.setText(data.getStringExtra("content") + "/月");
                break;


        }
    }


}
