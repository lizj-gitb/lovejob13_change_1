package com.lovejob.view._userinfo.mylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view._money.Aty_OriDetails;
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
 * Created by Administrator on 2016/11/29.
 */
public class Aty_MyGetWork_AreadySignIn extends BaseActivity {
    @Bind(R.id.img_mygetwork_already_signin_back)
    ImageView imgMygetworkAlreadySigninBack;
    @Bind(R.id.lv_mygetwork_already_signin)
    PullToRefreshListView lvMygetworkAlreadySignin;
    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter;
    private Activity context;
    private int page = 1;
    private Call call_getWorkList;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_mygetwork_alreadysignin);
        ButterKnife.bind(this);
        context = this;
        initAdapter();
        addData();

    }

    private void addData() {
        call_getWorkList = LoveJob.getWorkList("410", String.valueOf(page), new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getWorkInfoDTOs() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getWorkInfoDTOs().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getWorkInfoDTOs().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    lvMygetworkAlreadySignin.onRefreshComplete();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
                lvMygetworkAlreadySignin.onRefreshComplete();
            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.WorkInfoDTO>(context, R.layout.item_lv_mygetwork_signin) {
            public FastAdapter<String> adapter_lv_gridview;

            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_mygetwork_signin, position);
                TextView tvMylistMygetworkAlreaySignTitle = (TextView) viewHolder.getView(R.id.tv_mylist_mygetwork_alreaySign_title);
                TextView tvMylistMygetworkAlreaySignPrice = (TextView) viewHolder.getView(R.id.tv_mylist_mygetwork_alreaySign_price);
                TextView tvMylistMygetworkAlreaySignTime = (TextView) viewHolder.getView(R.id.tv_mylist_mygetwork_alreaySign_time);
                TextView tvMylistMygetworkAlreaySignSignInNumber = (TextView) viewHolder.getView(R.id.tv_mylist_mygetwork_alreaySign_signIn_number);
                TextView tvMylistMygetworkAlreaySignLocation = (TextView) viewHolder.getView(R.id.tv_mylist_mygetwork_alreaySign_location);
                GridView gvMylistMygetworkAlreaySign = (GridView) viewHolder.getView(R.id.gv_mylist_mygetwork_alreaySign);
                TextView tvMylistMygetworkAlreaySignCancle = (TextView) viewHolder.getView(R.id.tv_mylist_mygetwork_alreaySign_cancle);

                tvMylistMygetworkAlreaySignTitle.setText(getItem(position).getTitle());
                tvMylistMygetworkAlreaySignPrice.setText(getItem(position).getSalary() + "元/" + getItem(position).getPaymentDec());
                long l = getItem(position).getReleaseDate();
                String s1 = String.format("%tF%n", l);
                tvMylistMygetworkAlreaySignTime.setText("时间:" + s1);
                tvMylistMygetworkAlreaySignSignInNumber.setText(String.valueOf(getItem(position).getApplyCount()));
                tvMylistMygetworkAlreaySignLocation.setText(getItem(position).getAddress());
                initGridviewAdapater();
                gvMylistMygetworkAlreaySign.setAdapter(adapter_lv_gridview);
                for (int i = 0; i < getItem(position).getEmployeeInfo().size(); i++) {
                    setGridView(gvMylistMygetworkAlreaySign, i + 1);
                    if (getItem(position).getEmployeeInfo().get(i) != null) {
                        adapter_lv_gridview.addItem(getItem(position).getEmployeeInfo().get(i).getPortraitId() + "".trim());
                    } else {
                        adapter_lv_gridview.addItem(null);
                    }

                }
                adapter_lv_gridview.notifyDataSetChanged();
                tvMylistMygetworkAlreaySignCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //取消报名
                        cancleSignIn(position);
                        Utils.showToast(context, "取消" + getItem(position).getTitle());
                    }
                });
                return viewHolder.getConvertView();
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

            private void initGridviewAdapater() {
                adapter_lv_gridview = new FastAdapter<String>(context, R.layout.item_lv_f_money_gridview) {
                    @Override
                    public View getViewHolder(int position, View convertView, ViewGroup parent) {
                        FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_f_money_gridview, position);
                        CircleImageView img_item_lv_f_money_gridview = (CircleImageView) viewHolder.getView(R.id.img_item_lv_f_money_gridview);
                        Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position)).into(img_item_lv_f_money_gridview);
                        return viewHolder.getConvertView();
                    }
                };
            }
        };
        lvMygetworkAlreadySignin.setAdapter(adapter);
        lvMygetworkAlreadySignin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(Aty_MyGetWork_AreadySignIn.this, Aty_OriDetails.class);
                intent.putExtra("workId", adapter.getItem(position - 1).getPid());
                intent.putExtra("isEdit", adapter.getItem(position - 1).getShowApplyBtn() == 0 ? false : true);
                startActivity(intent);
            }
        });
    }

    private void cancleSignIn(final int position) {
        LoveJob.getGrabForm(adapter.getItem(position).getPid(), new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                adapter.remove(position);
                adapter.notifyDataSetChanged();
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


    @OnClick(R.id.img_mygetwork_already_signin_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
