package com.lovejob.view._userinfo.myserver;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view.payinfoviews.PayViewSelectPayment;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/30.
 */
public class Aty_SerSkillDetails extends BaseActivity {
    @Bind(R.id.img_job_details_back)
    ImageView imgJobDetailsBack;
    @Bind(R.id.tv_ser_skill_title)
    TextView tvSerSkillTitle;
    @Bind(R.id.tv_ser_skill_money)
    TextView tvSerSkillMoney;
    @Bind(R.id.tv_ser_skill_payment)
    TextView tvSerSkillPayment;
    @Bind(R.id.gv_ser_details)
    RecyclerView gvSerDetails;
    @Bind(R.id.tv_ser_skill_explain)
    TextView tvSerSkillExplain;
    @Bind(R.id.tv_ser_skill_count)
    TextView tvSerSkillCount;
    @Bind(R.id.tv_ser_skill_star)
    TextView tvSerSkillStar;
    @Bind(R.id.lv_ser_details)
    MyListView lvSerDetails;
    @Bind(R.id.img_oridetails_chat)
    ImageView imgOridetailsChat;
    @Bind(R.id.img_oridetails_call)
    ImageView imgOridetailsCall;
    @Bind(R.id.img_oridetails_grad)
    ImageView imgOridetailsGrad;
    @Bind(R.id.hiteView)
    RelativeLayout hiteView;
    private FastAdapter<ThePerfectGirl.EvaluateInfoDTO> lvadapter;
    private String serverPid, price;
    private Call call_getServiceDetails;
    private String phoneNumber;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_ser_skilldetails);
        ButterKnife.bind(this);
        serverPid = getIntent().getStringExtra("serPid");
        price = getIntent().getStringExtra("price");
        addData();
        initAdapter();
    }

    private void initAdapter() {
        lvadapter = new FastAdapter<ThePerfectGirl.EvaluateInfoDTO>(context, R.layout.item_lv_ser_details) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder =FFViewHolder.get(context,convertView,parent,R.layout.item_lv_ser_details,position);

                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getPortraitId().toString().trim()).into((CircleImageView) viewHolder.getView(R.id.img_f_my_other_logo));
                ((TextView) viewHolder.getView(R.id.tv_serdetails_other_name)).setText(getItem(position).getRealName());
                ((TextView) viewHolder.getView(R.id.tv_serdetails_other_content)).setText(getItem(position).getContent());
                String s = String.format("%tR%n", getItem(position).getCreateDate());
                ((TextView) viewHolder.getView(R.id.tv_serdetails_time)).setText(s);
                ((RatingBar)viewHolder.getView(R.id.rating)).setRating(getItem(position).getLevel());
//                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getMyPortraitId().toString().trim()).into((CircleImageView) viewHolder.getView(R.id.img_f_my_userlogo));
//                ((TextView) viewHolder.getView(R.id.tv_serdetails_my_content)).setText(getItem(position).getMyContent());
                return viewHolder.getConvertView();
            }
        };
        lvSerDetails.setAdapter(lvadapter);
    }

    private void addData() {
        call_getServiceDetails = LoveJob.getServiceDetails(serverPid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getServerDTO() != null) {
                    tvSerSkillTitle.setText(thePerfectGirl.getData().getServerDTO().getTitle());
                    int serType =thePerfectGirl.getData().getServerDTO().getServiceType();
                    if (serType==2){
                        tvSerSkillMoney.setVisibility(View.GONE);
                        tvSerSkillPayment.setVisibility(View.GONE);
                    }
                    tvSerSkillPayment.setText("/"+thePerfectGirl.getData().getServerDTO().getPaymentDec());
                    tvSerSkillMoney.setText(thePerfectGirl.getData().getServerDTO().getMoney()+"");
                    tvSerSkillExplain.setText(thePerfectGirl.getData().getServerDTO().getContent());
                    tvSerSkillCount.setText(thePerfectGirl.getData().getServerDTO().getSoldCount());
                    tvSerSkillStar.setText(thePerfectGirl.getData().getServerDTO().getLevel());
                  phoneNumber=thePerfectGirl.getData().getServerDTO().getUserInfo().getPhoneNumber();
                    if (thePerfectGirl.getData().getServerDTO().getEvaluateInfoDTOList() != null) {
                        for (int i = 0; i < thePerfectGirl.getData().getServerDTO().getEvaluateInfoDTOList().size(); i++) {
                            lvadapter.addItem(thePerfectGirl.getData().getServerDTO().getEvaluateInfoDTOList().get(i));
                        }
                    }
                    lvadapter.notifyDataSetChanged();
                    if (!TextUtils.isEmpty(thePerfectGirl.getData().getServerDTO().getPictrueId()) && thePerfectGirl.getData().getServerDTO().getPictrueId() != null) {
                        gvSerDetails.setVisibility(View.VISIBLE);
                        final ArrayList<String> selectedPhotos = new ArrayList<>();
                        String[] l = thePerfectGirl.getData().getServerDTO().getPictrueId().split("\\|");
                        for (int i = 0; i < l.length; i++) {
                            selectedPhotos.add(StaticParams.QiNiuYunUrl + l[i]);
                        }
                        final PhotoAdapter photoAdapter = new PhotoAdapter(context, selectedPhotos, true);
                        gvSerDetails.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
                        gvSerDetails.setAdapter(photoAdapter);

                    }
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
        if (call_getServiceDetails != null && call_getServiceDetails.isCanceled())
            call_getServiceDetails.cancel();
    }

    @OnClick({R.id.img_job_details_back, R.id.img_oridetails_chat, R.id.img_oridetails_call, R.id.img_oridetails_grad})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_job_details_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.img_oridetails_chat:
            
                break;
            case R.id.img_oridetails_call:
                Utils.showToast(context,"sadasd");
                if (TextUtils.isEmpty(phoneNumber)) {
                    Utils.showToast(context, "请稍后再试");
                    return;
                }
                Intent intent_phone = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phoneNumber);
                intent_phone.setData(data);
                break;
            case R.id.img_oridetails_grad:
                Intent intent = new Intent(context, PayViewSelectPayment.class);
                intent.putExtra("PayTypeInfo", PayTypeInfo.BuyUserService);
                intent.putExtra("serPid", serverPid);
                intent.putExtra("price", price);
                startActivity(intent);
                break;
        }
    }
}
