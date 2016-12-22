package com.lovejob.view._userinfo.mywalletinfos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.views.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/2.
 */
public class Aty_TokenDetails extends BaseActivity {
    @Bind(R.id.img_moneydetails_back)
    ImageView imgMoneydetailsBack;
    @Bind(R.id.lv_token)
    PullToRefreshListView lvToken;
    private Activity context;
    private FastAdapter<ThePerfectGirl.UserInfoDTO> adapter;
    String workTokenPid;
    private String workPid;
    private Call call_WorkTokenList;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_tokendetails);
        ButterKnife.bind(this);
        context = this;
        workTokenPid = context.getIntent().getStringExtra("worktokenpid");
        workPid = context.getIntent().getStringExtra("workPid");
        initAdapter();
        addData();

    }

    private void addData() {
        call_WorkTokenList = LoveJob.WorkTokenList(workTokenPid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getUserInfoDTOs() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getUserInfoDTOs().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getUserInfoDTOs().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.UserInfoDTO>(context, R.layout.item_lv_tokendetails) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_tokendetails, position);
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getPortraitId()).into((CircleImageView) viewHolder.getView(R.id.img_token_logo));
                ((TextView) viewHolder.getView(R.id.tv_token_name)).setText(getItem(position).getRealName());
                ((TextView) viewHolder.getView(R.id.tv_token_position)).setText(getItem(position).getPosition());
                ((TextView) viewHolder.getView(R.id.tv_token_company)).setText(getItem(position).getCompany());
                if (getItem(position).getState().equals("1")){
                    ((ImageView)viewHolder.getView(R.id.tv_token_faling)).setVisibility(View.GONE);
                    ((ImageView)viewHolder.getView(R.id.tv_token_yifa)).setVisibility(View.VISIBLE);
                }
                ((ImageView) viewHolder.getView(R.id.tv_token_faling)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog = Utils.showProgressDliago(context, "正在发令，请稍后");
                        LoveJob.soldWorkToken(getItem(position).getUserId(), workPid, new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                dialog.dismiss();
                                Utils.showToast(context, "发令成功");
                                AppManager.getAppManager().finishActivity();
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
        lvToken.setAdapter(adapter);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_WorkTokenList != null && call_WorkTokenList.isCanceled())
            call_WorkTokenList.cancel();
    }




    @OnClick(R.id.img_moneydetails_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
