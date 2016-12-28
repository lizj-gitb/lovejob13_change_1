package com.lovejob.view._money.fragments_sendmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.baidumap.BaiDuMapCitySelctor;
import com.lovejob.model.MyOnClickListener;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.StaticParams;
import com.lovejob.model.UserInputModel;
import com.lovejob.model.Utils;
import com.lovejob.model.bean.PriceBean;
import com.lovejob.view.JobTypeAty;
import com.lovejob.view.WriteView;
import com.lovejob.view.payinfoviews.PayViewSelectPayment;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.dialogs.zdialog.OnDialogItemClickListener;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
import com.v.rapiddev.pickerview.OptionsPickerView;
import com.v.rapiddev.pickerview.TimePickerView;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.V;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendOriWork_LocationSelected;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_LocationSelected;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_JobType;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_Location;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_WriteView_Context;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_WriteView_Countdown;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_WriteView_Hight;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_WriteView_Phonenumber;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_WriteView_Price;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_WriteView_Skill;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_WriteView_Title;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_SendPakWork_TO_WriteView_Wanttonum;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob
 * Package_Name:com.lovejob.view._money.fragments_sendmoney
 * Created on 2016-12-06 15:16
 */

public class SendpakWork extends BaseFragment {
    View view;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv_send_work_par_title)
    TextView tvSendWorkParTitle;
    @Bind(R.id.rl_send_work_par_title)
    RelativeLayout rlSendWorkParTitle;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv_send_work_par_type)
    TextView tvSendWorkParType;
    @Bind(R.id.rl_send_work_par_type)
    RelativeLayout rlSendWorkParType;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.tv_send_work_par_experience)
    TextView tvSendWorkParExperience;
    @Bind(R.id.rl_send_work_par_experience)
    RelativeLayout rlSendWorkParExperience;
    @Bind(R.id.tv4)
    TextView tv4;
    @Bind(R.id.tv_send_work_par_sex)
    TextView tvSendWorkParSex;
    @Bind(R.id.rl_send_work_par_sex)
    RelativeLayout rlSendWorkParSex;
    @Bind(R.id.tv5)
    TextView tv5;
    @Bind(R.id.tv_send_work_par_school)
    TextView tvSendWorkParSchool;
    @Bind(R.id.rl_send_work_par_school)
    RelativeLayout rlSendWorkParSchool;
    @Bind(R.id.tv6)
    TextView tv6;
    @Bind(R.id.tv_send_work_par_skill)
    TextView tvSendWorkParSkill;
    @Bind(R.id.rl_send_work_par_skill)
    RelativeLayout rlSendWorkParSkill;
    @Bind(R.id.tv7)
    TextView tv7;
    @Bind(R.id.tv_send_work_par_hight)
    TextView tvSendWorkParHight;
    @Bind(R.id.rl_send_work_par_hight)
    RelativeLayout rlSendWorkParHight;
    @Bind(R.id.tv8)
    TextView tv8;
    @Bind(R.id.tv_send_work_par_context)
    TextView tvSendWorkParContext;
    @Bind(R.id.rl_send_work_par_context)
    RelativeLayout rlSendWorkParContext;
    @Bind(R.id.tv9)
    TextView tv9;
    @Bind(R.id.tv_send_work_par_location)
    TextView tvSendWorkParLocation;
    @Bind(R.id.rl_send_work_par_location)
    RelativeLayout rlSendWorkParLocation;
    @Bind(R.id.tv10)
    TextView tv10;
    @Bind(R.id.tv_send_work_par_wanttonum)
    TextView tvSendWorkParWanttonum;
    @Bind(R.id.rl_send_work_par_wanttonum)
    RelativeLayout rlSendWorkParWanttonum;
    @Bind(R.id.tv11)
    TextView tv11;
    @Bind(R.id.tv_send_work_par_price)
    TextView tvSendWorkParPrice;
    @Bind(R.id.rl_send_work_par_price)
    RelativeLayout rlSendWorkParPrice;
    @Bind(R.id.tv12)
    TextView tv12;
    @Bind(R.id.tv_send_work_par_phonenumber)
    TextView tvSendWorkParPhonenumber;
    @Bind(R.id.rl_send_work_par_phonenumber)
    RelativeLayout rlSendWorkParPhonenumber;
    @Bind(R.id.tv13)
    TextView tv13;
    @Bind(R.id.tv_send_work_par_countdown)
    TextView tvSendWorkParCountdown;
    @Bind(R.id.rl_send_work_par_countdown)
    RelativeLayout rlSendWorkParCountdown;
    private String positionName;
    private String positionNumber;
    private String identify;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sendpakwork, null);
        ButterKnife.bind(this, view);
        AppPreferences preferences = new AppPreferences(context);
        identify = preferences.getString(StaticParams.FileKey.__IDENTIFY__,"");
        if (identify.equals("false")){
            Utils.showToast(context,"请填写个人资料");
            AppManager.getAppManager().finishActivity();
            return view;
        }
        /**
         * 发布按钮点击事件
         */
        setSendWorkListener();
        /**
         * 设置textview的长度
         */
        setDefaultParams();
        return view;
    }

    private void setDefaultParams() {
        tvSendWorkParSkill.setMaxLines(1);
        tvSendWorkParContext.setMaxLines(1);
        tvSendWorkParLocation.setMaxLines(1);
//        tvSendWorkParHight.setText("CM");
//        tvSendWorkParWanttonum.setText("人");
//        tvSendWorkParPrice.setText("/天");
    }

    private void setSendWorkListener() {
        getActivity().findViewById(R.id.actionbar_save).setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onclickListener(View v) {
                V.d("开始发布兼职工作");

                ZDialog.showZDlialog(context, "提示", "是否发布", "发布", "再写点", new OnDialogItemClickListener() {
                    @Override
                    public void onLeftButtonClickListener() {
                        V.d("发布");
                        sendParkWork();
                    }

                    @Override
                    public void onRightButtonClickListener() {
                        V.d("取消");
                    }
                }).show();
//                3 5 7 8 6
            }
        });
    }

    private void sendParkWork() {
        UserInputModel inputModel = Utils.checkUserInputParams(tvSendWorkParTitle, tvSendWorkParType, tvSendWorkParSex
                , tvSendWorkParLocation, tvSendWorkParWanttonum, tvSendWorkParPrice, tvSendWorkParPhonenumber, tvSendWorkParContext,tvSendWorkParCountdown);

        if (!inputModel.isNotEmpty()) {
            Utils.showToast(context, "不能有空值");
            return;
        }
        String s = tvSendWorkParPrice.getText().toString();
        String s1 =s.substring(0,s.length()-3);
        Double money = Double.parseDouble(s1);
        if (money<=0){
            Utils.showToast(context, "支付金额不能小于0元");
            return;
        }
        String[] strs = new String[14];
        for (int i = 0; i < inputModel.getParams().length; i++) {
            strs[i] = inputModel.getParams()[i];
        }
        strs[9] = tvSendWorkParExperience.getText().toString();
        strs[10] = tvSendWorkParSchool.getText().toString();
        strs[11] = tvSendWorkParSkill.getText().toString();
        strs[12] = tvSendWorkParHight.getText().toString();
        strs[13] = positionNumber;
        inputModel.setParams(strs);
        Double i1 = Double.parseDouble(tvSendWorkParWanttonum.getText().toString());
        String amount=String.valueOf(i1*money);
        inputModel.getParams()[5] = amount;
        //跳转支付页面
        Intent intent = new Intent(context, PayViewSelectPayment.class);
        intent.putExtra("inputModel", inputModel);
        intent.putExtra("PayTypeInfo", PayTypeInfo.SnedMoneyWork_Pak);
        AppManager.getAppManager().toNextPage(intent, true);
    }

    /**
     * 进入该方法时表示当前fragment是可见的，可以加载用户数据 注意：请做标示，当数据加载完一次后第二次不去加载
     */
    @Override
    public void loadData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    ArrayList<PriceBean> options1Items;
    OptionsPickerView pvOptions;

    @OnClick({R.id.rl_send_work_par_title, R.id.rl_send_work_par_type, R.id.rl_send_work_par_experience, R.id.rl_send_work_par_sex, R.id.rl_send_work_par_school, R.id.rl_send_work_par_skill, R.id.rl_send_work_par_hight, R.id.rl_send_work_par_context, R.id.rl_send_work_par_location, R.id.rl_send_work_par_wanttonum, R.id.rl_send_work_par_price, R.id.rl_send_work_par_phonenumber, R.id.rl_send_work_par_countdown})
    public void onClick(View view) {

        Intent intent = new Intent(context, WriteView.class);
        String title = "";
        String content = "";
        int requestCode = -1;
        int writeType = -1;
        int maxLenth = -1;

        switch (view.getId()) {
            case R.id.rl_send_work_par_title:
                title = "请输入工作名称";
                content = tvSendWorkParTitle.getText().toString().trim();
                requestCode = RequestCode_SendPakWork_TO_WriteView_Title;
                maxLenth = 10;
                break;
            case R.id.rl_send_work_par_type:
                context.startActivityForResult(new Intent(context, JobTypeAty.class), StaticParams.RequestCode.RequestCode_SendPakWork_TO_JobType);
                break;
            case R.id.rl_send_work_par_experience:
                options1Items = new ArrayList<>();
                options1Items.add(new PriceBean("不限"));
                options1Items.add(new PriceBean("应届生"));
                options1Items.add(new PriceBean("一年以内"));
                options1Items.add(new PriceBean("1~3年"));
                options1Items.add(new PriceBean("3~5年"));
                options1Items.add(new PriceBean("5~10年"));
                options1Items.add(new PriceBean("10年以上"));

                pvOptions = new OptionsPickerView(context);
                pvOptions.setPicker(options1Items);
                pvOptions.setTitle("经验要求");
                pvOptions.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(1, 1, 1);
                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        tvSendWorkParExperience.setText(options1Items.get(options1).getPickerViewText());
                    }
                });
                pvOptions.show();
                break;
            case R.id.rl_send_work_par_sex:
                options1Items = new ArrayList<>();
                options1Items.add(new PriceBean("男"));
                options1Items.add(new PriceBean("女"));
                options1Items.add(new PriceBean("不限"));
                pvOptions = new OptionsPickerView(context);
                pvOptions.setPicker(options1Items);
                pvOptions.setTitle("选择性别");
                pvOptions.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(1, 1, 1);
                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        tvSendWorkParSex.setText(options1Items.get(options1).getPickerViewText());
                    }
                });
                pvOptions.show();
                break;
            case R.id.rl_send_work_par_school:
                options1Items = new ArrayList<>();
                options1Items.add(new PriceBean("不限"));
                options1Items.add(new PriceBean("大专"));
                options1Items.add(new PriceBean("本科"));
                options1Items.add(new PriceBean("硕士"));
                options1Items.add(new PriceBean("博士"));
                pvOptions = new OptionsPickerView(context);
                pvOptions.setPicker(options1Items);
                pvOptions.setTitle("学历要求");
                pvOptions.setCyclic(false, true, true);
                //设置默认选中的三级项目
                //监听确定选择按钮
                pvOptions.setSelectOptions(1, 1, 1);
                pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        tvSendWorkParSchool.setText(options1Items.get(options1).getPickerViewText());
                    }
                });
                pvOptions.show();
                break;
            case R.id.rl_send_work_par_skill:
                title = "请输入技能要求";
                content = tvSendWorkParSkill.getText().toString();
                maxLenth = 20;
                requestCode = RequestCode_SendPakWork_TO_WriteView_Skill;

                break;
            case R.id.rl_send_work_par_hight:
                title = "请输入身高要求";
                content = tvSendWorkParHight.getText().toString();
                maxLenth = 3;
                writeType = 1;
                requestCode = RequestCode_SendPakWork_TO_WriteView_Hight;

                break;
            case R.id.rl_send_work_par_context:
                title = "请输入工作内容";
                content = tvSendWorkParContext.getText().toString();
                maxLenth = 100;
                requestCode = RequestCode_SendPakWork_TO_WriteView_Context;

                break;
            case R.id.rl_send_work_par_location:
//                title = "请输入工作地点";
//                content = tvSendWorkParLocation.getText().toString();
//                maxLenth = 20;
//                requestCode = RequestCode_SendPakWork_TO_Location;

                Intent intent_1 = new Intent(context,BaiDuMapCitySelctor.class);
                intent_1.putExtra("requestCode", RequestCode_SendPakWork_LocationSelected);
                context.startActivityForResult(intent_1, RequestCode_SendPakWork_LocationSelected);
                break;
            case R.id.rl_send_work_par_wanttonum:
                title = "请输入招聘人数";
                content = tvSendWorkParWanttonum.getText().toString();
                maxLenth = 4;
                writeType = 1;
                requestCode = RequestCode_SendPakWork_TO_WriteView_Wanttonum;
                break;
            case R.id.rl_send_work_par_price:
                title = "请输入酬金，单位元";
                content = tvSendWorkParPrice.getText().toString();
                maxLenth = 8;
                writeType = 2;
                requestCode = RequestCode_SendPakWork_TO_WriteView_Price;

                break;
            case R.id.rl_send_work_par_phonenumber:
                title = "请输入联系电话";
                content = tvSendWorkParPhonenumber.getText().toString();
                maxLenth = 11;
                writeType = 1;
                requestCode = RequestCode_SendPakWork_TO_WriteView_Phonenumber;

                break;
            case R.id.rl_send_work_par_countdown:
                TimePickerView timePickerView = new TimePickerView(context, TimePickerView.Type.ALL);
                timePickerView.show();
                timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        tvSendWorkParCountdown.setText(new SimpleDateFormat("yyyy年MM月dd日HH时mm分").format(date));
                    }
                });
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
        if (data == null) {
            V.e("从其他页面返回的data为空");
            return;
        }
        String content = data.getStringExtra("content");
        switch (requestCode) {
            case RequestCode_SendPakWork_TO_WriteView_Title:
                tvSendWorkParTitle.setText(content);
                break;
            case RequestCode_SendPakWork_TO_JobType:
                positionName = data.getStringExtra("positionName");
                positionNumber = data.getStringExtra("positionNumber");
                tvSendWorkParType.setText(positionName);
                break;

            case RequestCode_SendPakWork_TO_WriteView_Skill:
                tvSendWorkParSkill.setText(content);
                break;

            case RequestCode_SendPakWork_TO_WriteView_Hight:
                tvSendWorkParHight.setText(content + "CM");
                break;
            case RequestCode_SendPakWork_TO_WriteView_Context:
                tvSendWorkParContext.setText(content);
                break;
            case RequestCode_SendPakWork_LocationSelected:
                tvSendWorkParLocation.setText(content);
                break;
            case RequestCode_SendPakWork_TO_WriteView_Wanttonum:
                tvSendWorkParWanttonum.setText(content );
                break;

            case RequestCode_SendPakWork_TO_WriteView_Price:
                tvSendWorkParPrice.setText(content + "元／天");
                break;

            case RequestCode_SendPakWork_TO_WriteView_Phonenumber:
                tvSendWorkParPhonenumber.setText(content);
                break;
        }
    }
}
