package com.lovejob.view._userinfo.mywalletinfos.moneydetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/28.
 */
public class F_Record extends BaseFragment {
    @Bind(R.id.lv_record)
    PullToRefreshListView lvRecord;
    private View view;
    private FastAdapter<ThePerfectGirl.AccountDTO> adapter;
    Activity context;
    private Call call_getMoneyRecordList;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_record, null);
        ButterKnife.bind(this, view);
        context = getActivity();
        initAdapter();
        setRefreshListener();


        addData();
        return view;
    }

    private void setRefreshListener() {
        lvRecord.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                adapter.removeAll();
                addData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.AccountDTO>(context, R.layout.item_lv_record) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_record, position);
                ((TextView) viewHolder.getView(R.id.record_title)).setText(getItem(position).getDescription());
                ((TextView) viewHolder.getView(R.id.record_money)).setText(getItem(position).getAmount() + "");
                ((TextView) viewHolder.getView(R.id.record_symbol)).setText(getItem(position).getType().equals("0") ? "+" : "-");
                ((TextView) viewHolder.getView(R.id.record_time)).setText(getItem(position).getCreateDateDec());
                return viewHolder.getConvertView();
            }
        };
        lvRecord.setAdapter(adapter);
    }

    private void addData() {
        call_getMoneyRecordList = LoveJob.getMoneyRecordList(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                lvRecord.onRefreshComplete();
                if (thePerfectGirl.getData().getAccountDTOs() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getAccountDTOs().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getAccountDTOs().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
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
        if (call_getMoneyRecordList != null && call_getMoneyRecordList.isCanceled())
            call_getMoneyRecordList.cancel();
    }
}
