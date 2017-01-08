package com.lovejob.view._userinfo.mylist;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.views.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Aty_MySendServer_ToBeSure extends BaseActivity {
    @Bind(R.id.img_mysendallserverlist_date_back)
    ImageView imgMysendallserverlistDateBack;
    @Bind(R.id.lv_tobesure)
    PullToRefreshListView lvTobesure;
    private FastAdapter<ThePerfectGirl.ServerDTO> Lvadapter;
    private Call call_getServiceList, call_checkServer;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_server_tobesure);
        ButterKnife.bind(this);
        initAdapter();
        setRefreshListener();
        addData();

    }

    private void setRefreshListener() {
        lvTobesure.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Lvadapter.removeAll();
                addData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                lvTobesure.onRefreshComplete();
            }
        });
    }

    private void addData() {
        call_getServiceList = LoveJob.getServiceList("419", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                lvTobesure.onRefreshComplete();
                if (thePerfectGirl.getData().getServerDTOs() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getServerDTOList().size(); i++) {
                        Lvadapter.addItem(thePerfectGirl.getData().getServerDTOList().get(i));
                    }
                    Lvadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    private void initAdapter() {
        Lvadapter = new FastAdapter<ThePerfectGirl.ServerDTO>(context, R.layout.item_lv_ser_tobesure) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_ser_tobesure, position);
                String serverPid = getItem(position).getServerRelationPid();
                ((TextView) viewHolder.getView(R.id.tv_ser_tobesure_title)).setText(getItem(position).getTitle());
//               String s1 = String.format("%tF%n", getItem(position).g);
                ((TextView) viewHolder.getView(R.id.tv_ser_tobesure_time)).setText(getItem(position).getTitle());
                ((TextView) viewHolder.getView(R.id.tv_ser_tobesure_name)).setText(getItem(position).getUserInfo().getRealName());
                ((TextView) viewHolder.getView(R.id.tv_ser_tobesure_posetion)).setText(getItem(position).getUserInfo().getPosition());
                ((TextView) viewHolder.getView(R.id.tv_ser_tobesure_commpl)).setText(getItem(position).getUserInfo().getCompany());
                ((TextView) viewHolder.getView(R.id.tv_ser_tobesure_price)).setText(getItem(position).getMoney() + "元/" + getItem(position).getPaymentDec());
                switch (getItem(position).getState()){
                    case "3":
                        viewHolder.getView(R.id.tv_ser_tobeshure_yanshou).setVisibility(View.GONE);
                        viewHolder.getView(R.id.tv_ser_tobeshure_tuikuan).setVisibility(View.GONE);
                        viewHolder.getView(R.id.tv_tobesure_tuikuanzhong).setVisibility(View.VISIBLE);
                        break;
                }
                Glide.with(context).load(StaticParams.ImageURL + getItem(position).getUserInfo().getPortraitId()).into((CircleImageView) viewHolder.getView(R.id.img_ser_tobesure_logo));
                setOnItemListener(viewHolder.getConvertView(), position, serverPid);
                return viewHolder.getConvertView();
            }
        };
        lvTobesure.setAdapter(Lvadapter);
    }

    /**
     * 验收退款监听
     */
    private void setOnItemListener(View convertView, final int position, final String serverPid) {
        TextView tvyanshou = (TextView) convertView.findViewById(R.id.tv_ser_tobeshure_yanshou);
        tvyanshou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_checkServer = LoveJob.checkServer("66", serverPid, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        Utils.showToast(context, "验收成功");
                        Lvadapter.remove(position);
                        Lvadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                });
            }
        });
        final TextView tvtuikuan = (TextView) convertView.findViewById(R.id.tv_ser_tobeshure_tuikuan);
        tvtuikuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = Utils.showProgressDliago(context, "正在退款，请稍后");
                LoveJob.ServerRefund(serverPid, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        dialog.dismiss();
                        Utils.showToast(context, "已申请退款");
                        Lvadapter.removeAll();
                        addData();
                        Lvadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, msg);
                    }
                });
            }
        });
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_checkServer != null && call_checkServer.isCanceled())
            call_checkServer.cancel();
        if (call_getServiceList != null && call_getServiceList.isCanceled())
            call_getServiceList.cancel();
    }



    @OnClick(R.id.img_mysendallserverlist_date_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
