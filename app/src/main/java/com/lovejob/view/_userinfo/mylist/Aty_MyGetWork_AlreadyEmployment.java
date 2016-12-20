package com.lovejob.view._userinfo.mylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view._money.Aty_OriDetails;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/29.
 */
public class Aty_MyGetWork_AlreadyEmployment extends BaseActivity {
    @Bind(R.id.img_mygetwork_already_employment_back)
    ImageView imgMygetworkAlreadyEmploymentBack;
    @Bind(R.id.lv_mygetwork_already_employment_list)
    PullToRefreshListView lvMygetworkAlreadyEmploymentList;
    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter;
    private Activity context;
    private int page = 1;
    private Call call_getWorkList;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_mygetwork_alreadyemployment);
        ButterKnife.bind(this);
        context = this;
        initAdapter();
        addData();

    }

    private void addData() {
       call_getWorkList =  LoveJob.getWorkList1("411", String.valueOf(page), new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getWorkInfoDTOs() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getWorkInfoDTOs().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getWorkInfoDTOs().get(i));
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
        adapter = new FastAdapter<ThePerfectGirl.WorkInfoDTO>(context,R.layout.item_lv_alreademploment) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context,convertView,parent,R.layout.item_lv_alreademploment,position);
                //TODO 设置数据
                ((TextView) viewHolder.getView(R.id.tv_alreadyempl_title)).setText(getItem(position).getTitle());
                ((TextView) viewHolder.getView(R.id.tv_alreadyempl_price)).setText("￥" + getItem(position).getSalary() + "元/" + getItem(position).getPaymentDec());
                ((TextView) viewHolder.getView(R.id.tv_alreadyempl_date)).setText("时间：" + new SimpleDateFormat("yyyy-MM-dd").format(new Date(getItem(position).getReleaseDate())));
                ((TextView) viewHolder.getView(R.id.tv_alreadyempl_address)).setText("地址：" + getItem(position).getAddress());
                return viewHolder.getConvertView();
            }
        };
        lvMygetworkAlreadyEmploymentList.setAdapter(adapter);
        lvMygetworkAlreadyEmploymentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(context, Aty_OriDetails.class);
                intent.putExtra("workId", adapter.getItem(position - 1).getPid());
                intent.putExtra("isEdit", adapter.getItem(position - 1).getShowApplyBtn() == 0 ? false : true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_getWorkList != null && call_getWorkList.isCanceled())
            call_getWorkList.cancel();
    }

    @OnClick(R.id.img_mygetwork_already_employment_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
