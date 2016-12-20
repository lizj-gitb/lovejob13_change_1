package com.lovejob.view._userinfo.mylist;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.views.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/29.
 */
public class Aty_MySendWork_ToBeSure extends BaseActivity {
    @Bind(R.id.img_mysendallworklist_date_back)
    ImageView imgMysendallworklistDateBack;
    @Bind(R.id.lv_tobesure)
    ListView lvTobesure;
    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter;
    private Activity context;
    int Page = 1;
    private Call call_getWorkList, call_CheckWork;


    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_published_work_tobesure);
        ButterKnife.bind(this);
        context = this;
        initAdapter();
        addData();


    }


    private void addData() {
        call_getWorkList = LoveJob.getWorkList("46", String.valueOf(Page), new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getWorkInfoDTOs() == null || thePerfectGirl.getData().getWorkInfoDTOs().size() == 0) {
                    Utils.showToast(context, "没有更多数据");
                    return;
                }
                for (int i = 0; i < thePerfectGirl.getData().getWorkInfoDTOs().size(); i++) {
                    adapter.addItem(thePerfectGirl.getData().getWorkInfoDTOs().get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(Aty_MySendWork_ToBeSure.this, msg);
            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.WorkInfoDTO>(context, R.layout.item_lv_tobesure) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_tobesure, position);
                TextView tvTobesureTime = (TextView) viewHolder.getView(R.id.tv_tobesure_time);
                TextView tvTobesureTitle = (TextView) viewHolder.getView(R.id.tv_tobesure_title);
                TextView tvTobesureName = (TextView) viewHolder.getView(R.id.tv_tobesure_name);
                TextView tvTobesurePosetion = (TextView) viewHolder.getView(R.id.tv_tobesure_posetion);
                TextView tvTobesureCommpl = (TextView) viewHolder.getView(R.id.tv_tobesure_commpl);
                CircleImageView imgTobesureLogo = (CircleImageView) viewHolder.getView(R.id.img_tobesure_logo);
                TextView tvTobesurePrice = (TextView) viewHolder.getView(R.id.tv_tobesure_price);
                TextView tvTobeshureYanshou = (TextView) viewHolder.getView(R.id.tv_tobeshure_yanshou);
                TextView tvTobeshureTuikuan = (TextView) viewHolder.getView(R.id.tv_tobeshure_tuikuan);
                TextView tvTobesureTuikuanzhong = (TextView) viewHolder.getView(R.id.tv_worktobesure_tuikuanzhong);
                if (getItem(position).getState() == 3) {
                    tvTobeshureTuikuan.setVisibility(View.GONE);
                    tvTobesureTuikuanzhong.setVisibility(View.VISIBLE);
                }
                tvTobesureTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(getItem(position).getReleaseDate())));
                tvTobesureTitle.setText(getItem(position).getTitle());
                tvTobesureName.setText(getItem(position).getReleaseInfo().getRealName());
                tvTobesurePosetion.setText(getItem(position).getReleaseInfo().getPosition() + "");
                tvTobesureCommpl.setText(getItem(position).getReleaseInfo().getCompany() + "");
                tvTobesurePrice.setText(getItem(position).getSalary() + "/" + getItem(position).getPaymentDec());
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getReleaseInfo().getPortraitId()).into(imgTobesureLogo);
                tvTobeshureYanshou.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        call_CheckWork = LoveJob.CheckWork(getItem(position).getPid(), getItem(position).getReleaseInfo().getUserId(), new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                Utils.showToast(context, "操作成功");
                                adapter.remove(position);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String msg) {
                                Utils.showToast(context, msg);
                            }
                        });

                    }
                });
                tvTobeshureTuikuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog = Utils.showProgressDliago(context, "正在退款");
                        LoveJob.WorkRefund(getItem(position).getReleaseInfo().getUserId(), getItem(position).getPid(), new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                dialog.dismiss();
                                Utils.showToast(context, "退款成功");
                            }

                            @Override
                            public void onError(String msg) {
                                dialog.dismiss();
                                Utils.showToast(context, msg);
                            }
                        });
                    }
                });
                return viewHolder.getConvertView();
            }
        };
        lvTobesure.setAdapter(adapter);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_getWorkList != null && call_getWorkList.isCanceled())
            call_getWorkList.cancel();
        if (call_CheckWork != null && call_CheckWork.isCanceled())
            call_CheckWork.cancel();
    }


    @OnClick(R.id.img_mysendallworklist_date_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
