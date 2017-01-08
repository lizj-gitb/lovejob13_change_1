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
import com.lovejob.view._home.DynDetailsAty;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob
 * Package_Name:com.lovejob.v.trunk.modular_home.dyndetailstabs
 * Created on 2016-11-11 15:33
 */

public class F_Good extends BaseFragment {
    @Bind(R.id.tvnotgood)
    TextView tvnotgood;
    @Bind(R.id.lv_f_good)
    MyListView lvFGood;
    private View view;
    private String dynPid;
    private FastAdapter<ThePerfectGirl.UserInfoDTO> adapter;
    private Call call_getGoodPersonList;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_good, null);
        ButterKnife.bind(this, view);
        this.dynPid = ((DynDetailsAty) getActivity()).getDynPid();
        /**
         * 初始化评论列表适配器
         */
        initAdapter();
        /**
         * 添加点赞的人列表
         */
        return view;
    }

    private void addData() {
        call_getGoodPersonList = LoveJob.getDynGoodOrBadPersonList(dynPid, 1, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.isSuccess()) {
//                    adapter.addItem();
                    List<ThePerfectGirl.UserInfoDTO> userInfoDTOList = thePerfectGirl.getData().getUserInfoDTOs();
                    if (userInfoDTOList!=null&&userInfoDTOList.size() > 0) {
                        tvnotgood.setVisibility(View.GONE);
                        for (int i = 0; i < userInfoDTOList.size(); i++) {
                            adapter.addItem(userInfoDTOList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        tvnotgood.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvnotgood.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.UserInfoDTO>(context, R.layout.item_lv_good_list) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_good_list, position);
                Glide.with(context).load(StaticParams.ImageURL + getItem(position).getPortraitId()+"!logo").into((CircleImageView) viewHolder.getView(R.id.item_dyndetails_img));
                ((TextView) viewHolder.getView(R.id.item_dyndetails_name)).setText(getItem(position).getRealName());
                return viewHolder.getConvertView();
            }
        };
        lvFGood.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && adapter.getList().size() > 0) {
            adapter.removeAll();
        }
        addData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (call_getGoodPersonList != null && !call_getGoodPersonList.isCanceled())
            call_getGoodPersonList.cancel();
    }

//    @Bind(R.id.lv_f_good)
//    MyListView lvFGood;
//    @Bind(R.id.tvnotgood)
//    TextView tvnotgood;
//    private View view;
//    private String dynPid;
//    private Activity context;
//
//    public FastAdapter<ThePerfectGirl.UserInfoDTO> adapter;
//
//    public FastAdapter<ThePerfectGirl.UserInfoDTO> getAdapter() {
//        return adapter;
//    }
//
//    @Override
//    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.f_good, null);
//        ButterKnife.bind(this, view);
//        context = getActivity();
//        dynPid = getArguments().getString("dynPid");
//        initAdapter();
////        setHomeOclickListner();
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (adapter != null && adapter.getList().size() > 0) {
//            adapter.removeAll();
//        }
//        adapter.notifyDataSetChanged();
//        getGoodPersonData();
//    }
//
//    private void setHomeOclickListner() {
////        getActivity().findViewById(R.id.lt1).setOnClickListener(new myOclick(1));
////        getActivity().findViewById(R.id.lt2).setOnClickListener(new myOclick(2));
////        getActivity().findViewById(R.id.lt3).setOnClickListener(new myOclick(3));
//    }
//
//    private void getGoodPersonData() {
//        Map<String, String> map = new HashMap<>();
//        map.put("dynamicPid", dynPid);
//        map.put("state", "1");
//        map.put("requestType", "515");
//
//        HiteImpl.getImpl().getDynGoodOrBadPersonList(context, map, new OnAllParamsListener() {
//            @Override
//            public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                if (thePerfectGirl.getData().getUserInfoDTOs() == null) {
//                    tvnotgood.setVisibility(View.VISIBLE);
//                    lvFGood.setVisibility(View.GONE);
//                    return;
//                }
//                tvnotgood.setVisibility(View.GONE);
//                lvFGood.setVisibility(View.VISIBLE);
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
//        lvFGood.setAdapter(adapter);
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
//
//    public void setImgResou(int isPointGood) {
//        ImageView imgDyndetailsLike = (ImageView) getActivity().findViewById(R.id.img_dyndetails_like);
//        ImageView imgDyndetailsBad = (ImageView) getActivity().findViewById(R.id.img_dyndetails_bad);
//        switch (isPointGood) {
//            case 0:
//                //已差评
//                imgDyndetailsLike.setImageResource(R.mipmap.icon_good_common);
//                imgDyndetailsBad.setImageResource(R.mipmap.icon_bad_on);//icon_bad_common
//                break;
//            case 1:
//                //已点赞
//                imgDyndetailsLike.setImageResource(R.mipmap.icon_good_on);
//                imgDyndetailsBad.setImageResource(R.mipmap.icon_bad_common);//
//                break;
//
//            case 2:
//                //什么都可以干
//                imgDyndetailsLike.setImageResource(R.mipmap.icon_good_common);
//                imgDyndetailsBad.setImageResource(R.mipmap.icon_bad_common);//
//                break;
//        }
}

//
//    public class myOclick implements View.OnClickListener {
//        private int type = -1;
//
//        public myOclick(int type) {
//            this.type = type;
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (type) {
//                case 1:
//
//                    //赞
//                    Map<String, String> map = new HashMap<>();
//                    map.put("dynamicPid", dynPid);
//                    map.put("state", "1");
//                    HiteImpl.getImpl().toDynGoodOrBad(context, map, new OnAllParamsListener() {
//                        @Override
//                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                            ((TextView) getActivity().findViewById(R.id.tv_dyndetails_like_number)).setText(String.valueOf(thePerfectGirl.getData().getCount()));
//                            setImgResou(thePerfectGirl.getData().getPoints());
//                            getGoodPersonData();
//                        }
//
//                        @Override
//                        public void onError(String errorMsg) {
//                            Utils.showToast(context, errorMsg, false);
//                        }
//                    });
//                    break;
//
//                case 2:
//                    Map<String, String> map1 = new HashMap<>();
//                    map1.put("dynamicPid", dynPid);
//                    map1.put("state", "0");
//                    HiteImpl.getImpl().toDynGoodOrBad(context, map1, new OnAllParamsListener() {
//                        @Override
//                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                            ((TextView) getActivity().findViewById(R.id.tv_dyndetails_bad_number)).setText(String.valueOf(thePerfectGirl.getData().getCount()));
//                            setImgResou(thePerfectGirl.getData().getPoints());
//                        }
//
//                        @Override
//                        public void onError(String errorMsg) {
//                            Utils.showToast(context, errorMsg, false);
//                        }
//                    });
//                    break;
//
//                case 3:
//
//                    break;
//            }
//        }
//    }

