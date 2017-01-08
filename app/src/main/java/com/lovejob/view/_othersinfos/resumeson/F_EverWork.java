package com.lovejob.view._othersinfos.resumeson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyListView;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/11/17.
 */
public class F_EverWork extends BaseFragment {
    @Bind(R.id.lv_historycomm)
    MyListView lvHistorycomm;
    private FastAdapter<ThePerfectGirl.WorkEvaluateView> adapter;
    String userpid;
    View view;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_everwork, null);
        ButterKnife.bind(this, view);
        userpid = getActivity().getIntent().getStringExtra("userId");
        initAdapter();
        addData();
        return view;
    }

    private void addData() {
        LoveJob.getHistoryComm("130", userpid, new OnAllParameListener() {
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
                ((TextView) viewHolder.getView(R.id.tv_history_company)).setText(getItem(position).getCriticInfo().getCompany());
                ((TextView) viewHolder.getView(R.id.tv_history_content)).setText(getItem(position).getContent());
                ((TextView) viewHolder.getView(R.id.tv_history_othercontent)).setText(getItem(position).getContent2());
                Glide.with(context).load(StaticParams.ImageURL + getItem(position).getCriticInfo().getPortraitId()+"!logo").into((CircleImageView) viewHolder.getView(R.id.img_history_logo));
                String s1 = String.format("%tF%n", getItem(position).getCreatDate());
                ((TextView) viewHolder.getView(R.id.tv_history_time)).setText(s1);
                ((RatingBar)viewHolder.getView(R.id.rating)).setRating(getItem(position).getLevel());
                ((RatingBar)viewHolder.getView(R.id.rating_comm)).setRating(getItem(position).getLevel2());

                //TODO星星
                return viewHolder.getConvertView();
            }
        };
        lvHistorycomm.setAdapter(adapter);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
