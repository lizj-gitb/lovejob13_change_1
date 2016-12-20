package com.lovejob.view._userinfo.mywalletinfos.moneydetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.MyOnClickListener;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view.payinfoviews.PayViewSelectPayment;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.utils.V;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/28.
 */
public class F_State extends BaseFragment {
    @Bind(R.id.lv_state)
    PullToRefreshListView lvState;
    private View view;
    Activity context;
    private FastAdapter<ThePerfectGirl.orderDetail> adapter;
    private Call call_getBillDetailed;
    PayTypeInfo payTypeInfo;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_state, null);
        ButterKnife.bind(this, view);
        context = getActivity();
        initAdapter();
        addData();
        return view;
    }

    private void addData() {
        call_getBillDetailed = LoveJob.getBillDetailed(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getOrderDetails() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getOrderDetails().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getOrderDetails().get(i));
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
        adapter = new FastAdapter<ThePerfectGirl.orderDetail>(context, R.layout.item_lv_state) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_state, position);
                ((TextView) viewHolder.getView(R.id.tv_state_title)).setText(getItem(position).getTitle());
                ((TextView) viewHolder.getView(R.id.tv_state_money)).setText(getItem(position).getAmount() + "元");
                TextView tvzy = ((TextView) viewHolder.getView(R.id.tv_state_waitoralready));
                switch (getItem(position).getTradeStatus()) {
                    case 0:
                        tvzy.setText("待付款");
                        break;
                    case 1:
                        tvzy.setText("不能付款");
                        ((RelativeLayout) viewHolder.getView(R.id.rl_state_buy)).setEnabled(false);
                        break;
                    case 2:
                        tvzy.setText("已完成");
                        ((RelativeLayout) viewHolder.getView(R.id.rl_state_cancel)).setVisibility(View.GONE);
                        ((RelativeLayout) viewHolder.getView(R.id.rl_state_buy)).setVisibility(View.GONE);
                        break;
                    case 3:
                        tvzy.setText("已完成");
                        break;

                }
                String s1 = String.format("%tF%n", getItem(position).getCreateDate());
                String s2 = String.format("%tR%n", getItem(position).getCreateDate());
                ((TextView) viewHolder.getView(R.id.tv_state_date)).setText(s1);
                ((TextView) viewHolder.getView(R.id.tv_state_time)).setText(s2);
                setItemOnListener(viewHolder.getConvertView(), position);
                ((RelativeLayout) viewHolder.getView(R.id.rl_state_buy)).setOnClickListener(new MyOnClickListener() {
                    @Override
                    protected void onclickListener(View v) {
                        V.d("付款"+getItem(position).getOutTradeNo());
                        Intent intent =new Intent(context, PayViewSelectPayment.class);
                        intent.putExtra("PayTypeInfo",PayTypeInfo.RePay);
                        intent.putExtra("outTradeNo",getItem(position).getOutTradeNo());
                        startActivity(intent);
                        context.finish();
                    }
                });
                return viewHolder.getConvertView();
            }

            private void setItemOnListener(View convertView, final int position) {
                RelativeLayout tvcance = (RelativeLayout) convertView.findViewById(R.id.rl_state_cancel);
                tvcance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoveJob.CancelBill(adapter.getItem(position).getType(), adapter.getItem(position).getOutTradeNo(), new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                Utils.showToast(context, "取消成功");
                                adapter.removeAll();
                                addData();
                            }

                            @Override
                            public void onError(String msg) {
                                Utils.showToast(context, msg);
                            }
                        });
                    }
                });
                RelativeLayout tvbuy = (RelativeLayout) convertView.findViewById(R.id.rl_state_buy);
//                tvbuy.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });


//                tvbuy.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, PayViewSelectPayment.class);
//                        switch (adapter.getItem(position).getType()) {
//                            case "0":
//                                payTypeInfo = PayTypeInfo.SendJobWork;
//                                break;
//                            case "1":
//                                payTypeInfo = PayTypeInfo.SendMoneyWork_Ori;
//                                break;
//                            case "2":
//                                payTypeInfo = PayTypeInfo.BuyUserService;
//                                break;
//                        }
//                        intent.putExtra("PayTypeInfo", payTypeInfo);
//                        intent.putExtra("serPid", adapter.getItem(position).getWorkPid());
//                        intent.putExtra("price", adapter.getItem(position).getAmount() + "");
//                        startActivity(intent);
//                    }
//                });

            }
        };
        lvState.setAdapter(adapter);
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
        if (call_getBillDetailed != null && call_getBillDetailed.isCanceled())
            call_getBillDetailed.cancel();
    }
}
