package com.lovejob.view._userinfo.myinformation;

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
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.views.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/1.
 */
public class Aty_HistoryComm extends BaseActivity {
    @Bind(R.id.img_history_back)
    ImageView imgHistoryBack;
    @Bind(R.id.lv_historycomm)
    PullToRefreshListView lvHistorycomm;
    private FastAdapter<ThePerfectGirl.WorkEvaluateView> adapter;
    private String userPid;
    Activity context;
    private Call call_getHistoryComm;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_historycomm);
        ButterKnife.bind(this);
        context = this;
        AppPreferences appPreferences = new AppPreferences(context);
        userPid = appPreferences.getString(StaticParams.FileKey.__USERPID__, "");
        initAdapter();
        addData();
    }

    private void addData() {
        LoveJob.getHistoryComm("130", userPid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getWorkEvaluateViews() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getWorkEvaluateViews().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getWorkEvaluateViews().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.WorkEvaluateView>(context, R.layout.item_lv_history_comm) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_history_comm, position);
                ((TextView) viewHolder.getView(R.id.tv_history_name)).setText(getItem(position).getCriticInfo().getRealName());
                ((TextView) viewHolder.getView(R.id.tv_history_title)).setText(getItem(position).getTitle());
                ((TextView) viewHolder.getView(R.id.tv_history_company)).setText(getItem(position).getCriticInfo().getCompany());
                ((TextView) viewHolder.getView(R.id.tv_history_content)).setText(getItem(position).getContent());
                ((TextView) viewHolder.getView(R.id.tv_history_othercontent)).setText(getItem(position).getContent2());
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getCriticInfo().getPortraitId()).into((CircleImageView) viewHolder.getView(R.id.img_history_logo));
                String s1 = String.format("%tF%n", getItem(position).getCreatDate());
                ((TextView) viewHolder.getView(R.id.tv_history_time)).setText(s1);
                //TODO星星
                return viewHolder.getConvertView();
            }
        };
        lvHistorycomm.setAdapter(adapter);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_getHistoryComm != null && !call_getHistoryComm.isCanceled())
            call_getHistoryComm.cancel();
    }




    @OnClick(R.id.img_history_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
