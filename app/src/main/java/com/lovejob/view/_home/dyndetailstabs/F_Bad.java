package com.lovejob.view._home.dyndetailstabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob
 * Package_Name:com.lovejob.v.trunk.modular_home.dyndetailstabs
 * Created on 2016-11-11 15:33
 */

public class F_Bad extends BaseFragment {
    @Bind(R.id.tvnotbad)
    TextView tvnotbad;
    @Bind(R.id.lv_f_bad)
    MyListView lvFBad;
    private View view;
    private String dynPid;
    public FastAdapter<ThePerfectGirl.UserInfoDTO> adapter;
    private Call call_getDynGoodOrBadPersonList;
    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_bad, null);
        ButterKnife.bind(this, view);
        dynPid = getArguments().getString("dynPid");
        initAdapter();
        return view;
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.UserInfoDTO>(context,R.layout.item_dyndetails_personlist) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_dyndetails_personlist, position);
                Glide.with(context).load(StaticParams.ImageURL + getItem(position).getPortraitId()+"!logo")
                        .into(((CircleImageView) viewHolder.getView(R.id.item_dyndetails_img)));
                ((TextView) viewHolder.getView(R.id.item_dyndetails_name)).setText(getItem(position).getRealName());
                return viewHolder.getConvertView();
            }
        };
        lvFBad.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && adapter.getList().size() > 0) {
            adapter.removeAll();
        }
        getBadPersonData();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void loadData() {

    }

    private void getBadPersonData() {
        call_getDynGoodOrBadPersonList = LoveJob.getDynGoodOrBadPersonList(dynPid, 0, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getUserInfoDTOs() == null) {
                    tvnotbad.setVisibility(View.VISIBLE);
                    lvFBad.setVisibility(View.GONE);
                    return;
                }
                tvnotbad.setVisibility(View.GONE);
                lvFBad.setVisibility(View.VISIBLE);
                for (int i = 0; i < thePerfectGirl.getData().getUserInfoDTOs().size(); i++) {
                    adapter.addItem(thePerfectGirl.getData().getUserInfoDTOs().get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (call_getDynGoodOrBadPersonList != null && !call_getDynGoodOrBadPersonList.isCanceled())
            call_getDynGoodOrBadPersonList.cancel();
    }
//    @Bind(R.id.tvnotbad)
//    TextView tvnotbad;
//    @Bind(R.id.lv_f_bad)
//    MyListView lvFBad;
//    private View view;
//    private String dynPid;
//    private Activity context;
//    public FastAdapter<ThePerfectGirl.UserInfoDTO> adapter;
//    @Override
//    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.f_bad, null);
//        ButterKnife.bind(this, view);
//        context = getActivity();
//        dynPid = getArguments().getString("dynPid");
//        initAdapter();
//        return view;
//    }
//
//    private void initAdapter() {
//        adapter = new FastAdapter<ThePerfectGirl.UserInfoDTO>(context, R.layout.item_dyndetails_personlist) {
//            @Override
//            public View getViewHolder(int position, View convertView, ViewGroup parent) {
//                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_dyndetails_personlist, position);
//                MyApplication.imageloader.displayImage(StaticParam.QiNiuYunUrl + getItem(position).getPortraitId(), ((FFRoundImageView) viewHolder.getView(R.id.item_dyndetails_img)),
//                        ImageMode.DefaultImage, true, true);
//                ((TextView) viewHolder.getView(R.id.item_dyndetails_name)).setText(getItem(position).getRealName());
//                return viewHolder.getConvertView();
//            }
//        };
//        lvFBad.setAdapter(adapter);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (adapter != null && adapter.getList().size() > 0) {
//            adapter.removeAll();
//        }
//        adapter.notifyDataSetChanged();
//        getBadPersonData();
//
//    }
//
//    private void getBadPersonData() {
//        Map<String, String> map = new HashMap<>();
//        map.put("dynamicPid", dynPid);
//        map.put("state", "0");
//        map.put("requestType", "515");
//
//        HiteImpl.getImpl().getDynGoodOrBadPersonList(context, map, new OnAllParamsListener() {
//            @Override
//            public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                if (thePerfectGirl.getData().getUserInfoDTOs() == null) {
//                    tvnotbad.setVisibility(View.VISIBLE);
//                    lvFBad.setVisibility(View.GONE);
//                    return;
//                }
//                tvnotbad.setVisibility(View.GONE);
//                lvFBad.setVisibility(View.VISIBLE);
//                for (int i = 0; i < thePerfectGirl.getData().getUserInfoDTOs().size(); i++) {
//                    adapter.addItem(thePerfectGirl.getData().getUserInfoDTOs().get(i));
//                }
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//                Utils.showToast(context, errorMsg, false);
//            }
//        });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
}
