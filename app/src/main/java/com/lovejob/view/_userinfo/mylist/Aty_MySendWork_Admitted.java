package com.lovejob.view._userinfo.mylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Created by Administrator on 2016/11/29.
 */
public class Aty_MySendWork_Admitted extends BaseActivity {
    @Bind(R.id.img_mysendallworklist_date_back)
    ImageView imgMysendallworklistDateBack;
    @Bind(R.id.lv_refresh_mysendallwork)
    PullToRefreshListView lvRefreshMysendallwork;
    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter;
    private int page = 1;
    private Activity context;
    private Call call_getWorkList;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.mysendallwork);
        ButterKnife.bind(this);
        context = this;
        initAdapter();
        setToRefresh();
        addDataToAdapter();
    }

    private void setToRefresh() {
        lvRefreshMysendallwork.setMode(PullToRefreshBase.Mode.BOTH);
        lvRefreshMysendallwork.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                adapter.removeAll();
                addDataToAdapter();
            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                addDataToAdapter();
            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.WorkInfoDTO>(context, R.layout.aty_trade_worklist) {
            public FastAdapter<String> adapter_lv_gridview;

            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.aty_trade_worklist, position);
                TextView tvMysendallworklistTitle = (TextView) viewHolder.getView(R.id.tv_mysendallworklist_title);
                TextView tvMysendallworklistPrice = (TextView) viewHolder.getView(R.id.tv_mysendallworklist_price);
                TextView tvMysendallworklistTime = (TextView) viewHolder.getView(R.id.tv_mysendallworklist_time);
                TextView tvMysendallworklistAlreadySignInNumber = (TextView) viewHolder.getView(R.id.tv_mysendallworklist_alreadySignInNumber);
                TextView tvMysendallworklistLocation = (TextView) viewHolder.getView(R.id.tv_mysendallworklist_location);
                GridView MysendallworklistGv = (GridView) viewHolder.getView(R.id.tv_mysendallworklist_gv);
                tvMysendallworklistTitle.setText(getItem(position).getTitle());
                tvMysendallworklistPrice.setText(getItem(position).getSalary() + "元/" + getItem(position).getPaymentDec());
                long l = getItem(position).getReleaseDate();
                String s1 = String.format("%tF%n", l);
                tvMysendallworklistTime.setText("时间:" + s1);
                tvMysendallworklistAlreadySignInNumber.setText(String.valueOf(getItem(position).getApplyCount()));
                tvMysendallworklistLocation.setText(getItem(position).getAddress());
                initGridviewAdapater();


                MysendallworklistGv.setAdapter(adapter_lv_gridview);
                if (getItem(position).getEmployeeInfo() != null && getItem(position).getEmployeeInfo().size() > 0) {
                    setGridView(MysendallworklistGv, getItem(position).getEmployeeInfo().size());
                }
                if (getItem(position).getEmployeeInfo() != null && getItem(position).getEmployeeInfo().size() > 0) {
                    for (int i = 0; i < getItem(position).getEmployeeInfo().size(); i++) {
                        adapter_lv_gridview.addItem(getItem(position).getEmployeeInfo().get(i).getPortraitId() + "".trim());
                    }
                }

//                adapter_lv_gridview.notifyDataSetChanged();
                return viewHolder.getConvertView();
            }

            private void initGridviewAdapater() {
                adapter_lv_gridview = new FastAdapter<String>(context, R.layout.item_lv_f_money_gridview) {
                    @Override
                    public View getViewHolder(int position, View convertView, ViewGroup parent) {
                        FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_f_money_gridview, position);
                        CircleImageView img_item_lv_f_money_gridview = (CircleImageView) viewHolder.getView(R.id.img_item_lv_f_money_gridview);
                        Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position)).placeholder(R.mipmap.ic_launcher).dontAnimate().into(img_item_lv_f_money_gridview);
                        return viewHolder.getConvertView();
                    }
                };
            }

            private void setGridView(GridView tvMysendallworklistGv, int size) {
                int length = 35;
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                float density = dm.density;
                int gridviewWidth = (int) (size * (length + 4) * density);
                int itemWidth = (int) (length * density);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
                tvMysendallworklistGv.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
                tvMysendallworklistGv.setColumnWidth(itemWidth); // 设置列表项宽
                tvMysendallworklistGv.setHorizontalSpacing(5); // 设置列表项水平间距
                tvMysendallworklistGv.setStretchMode(GridView.NO_STRETCH);
                tvMysendallworklistGv.setNumColumns(size); // 设置列数量=列表集合数
                tvMysendallworklistGv.setAdapter(adapter_lv_gridview);
            }
        };
        lvRefreshMysendallwork.setAdapter(adapter);
        lvRefreshMysendallwork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(context, Aty_MySendWork_Admitted_State.class);
                intent.putExtra("workId", adapter.getItem(position - 1).getPid());
                startActivity(intent);
            }
        });
    }

    private void addDataToAdapter() {
        call_getWorkList = LoveJob.getWorkList("45", String.valueOf(page), new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getWorkInfoDTOs() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getWorkInfoDTOs().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getWorkInfoDTOs().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    lvRefreshMysendallwork.onRefreshComplete();
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
        if (call_getWorkList != null && call_getWorkList.isCanceled())
            call_getWorkList.cancel();
    }

    @OnClick(R.id.img_mysendallworklist_date_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
