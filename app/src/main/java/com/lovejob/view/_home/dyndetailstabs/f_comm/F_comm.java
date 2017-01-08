package com.lovejob.view._home.dyndetailstabs.f_comm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseFragment;
import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.utils.V;
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

public class F_comm extends BaseFragment {
    @Bind(R.id.tvnotcommm)
    TextView tvnotcommm;
    @Bind(R.id.lv_f_comm)
    MyListView lvFComm;
    private View view;
    private String dynPid;
    private int page = 1;
    private FastAdapter<ThePerfectGirl.DynamicCommentDTO> adapter;
    private Call call_toDynCommListGood,call_getDynCommList,call_toCommDyn;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_comm, null);
        ButterKnife.bind(this, view);
        dynPid = getArguments().getString("dynPid");
        initAdapter();
        setListViewItemClickListener();
        addDataToCommList();
        return view;
    }

    @Override
    public void loadData() {

    }

    private void setListViewItemClickListener() {
        lvFComm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(context, DynCommDetails.class);
                intent.putExtra("dynamicCommentPid", adapter.getItem(position).getDynamicCommentPid());
                intent.putExtra("name", adapter.getItem(position).getReleaseInfo().getRealName());
                intent.putExtra("uid", adapter.getItem(position).getUserPid());
                startActivity(intent);
            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.DynamicCommentDTO>(context,R.layout.item_dyndetails_commentlist) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_dyndetails_commentlist, position);
                CircleImageView ivDyncommlistUserlogo = (CircleImageView) viewHolder.getView(R.id.iv_dyncommlist_userlogo);
                TextView tvDyncommlistUsername = (TextView) viewHolder.getView(R.id.tv_dyncommlist_username);
                LinearLayout ltDyncommlistGood = (LinearLayout) viewHolder.getView(R.id.lt_dyncommlist_good);
                ImageView ivDyncommlistGood = (ImageView) viewHolder.getView(R.id.iv_dyncommlist_good);
                TextView tvDyncommlistGood = (TextView) viewHolder.getView(R.id.tv_dyncommlist_good);
                TextView tvDyncommlistContent = (TextView) viewHolder.getView(R.id.tv_dyncommlist_content);
                TextView tvDyncommlistTime = (TextView) viewHolder.getView(R.id.tv_dyncommlist_time);
                TextView tvDyncommlistResend = (TextView) viewHolder.getView(R.id.tv_dyncommlist_resend);
               int i = Integer.valueOf((getItem(position).getDetailCount()));
                if (i>0){
                    ((ImageView) viewHolder.getView(R.id.img_dyncommlist_more)).setVisibility(View.VISIBLE);
                }
                //评论人头像
                Glide.with(context).load(StaticParams.ImageURL + getItem(position).getReleaseInfo().getPortraitId()).into(ivDyncommlistUserlogo);

                //评论人姓名
                tvDyncommlistUsername.setText(getItem(position).getReleaseInfo().getRealName());

                //自己是否已经点赞
                if (getItem(position).isPointGood()) {
                    ivDyncommlistGood.setImageResource(R.mipmap.icon_good_on);
                } else {
                    ivDyncommlistGood.setImageResource(R.mipmap.icon_good_common);
                }
                //点赞数量
                tvDyncommlistGood.setText(String.valueOf(getItem(position).getCount()));

                //评论的内容
                tvDyncommlistContent.setText(getItem(position).getCommentContent());

                //评论的时间
                tvDyncommlistTime.setText(getItem(position).getCreateDateDec());

//                //回复按钮点击事件
//                tvDyncommlistResend.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Utils.showToast(context, "" + position, false);
//                    }
//                });
                //对评论点赞按钮事件
                ltDyncommlistGood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.showToast(context, "点赞");
                        call_toDynCommListGood = LoveJob.toDynCommListGood(getItem(position).getDynamicCommentPid(), new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                Utils.showToast(context, "点赞成功");
                                adapter.removeAll();
                                addDataToCommList();
                            }

                            @Override
                            public void onError(String msg) {
                                Utils.showToast(context, "点赞失败，" + msg);

                            }
                        });


                    }
                });

                return viewHolder.getConvertView();
            }
        };
        lvFComm.setAdapter(adapter);
    }

    private void addDataToCommList() {
        call_getDynCommList = LoveJob.getDynCommList(dynPid, page, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getList() == null) {
                    tvnotcommm.setVisibility(View.VISIBLE);
                    lvFComm.setVisibility(View.GONE);
                } else {
                    tvnotcommm.setVisibility(View.GONE);
                    lvFComm.setVisibility(View.VISIBLE);
                    for (int i = 0; i < thePerfectGirl.getData().getList().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getList().get(i));
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (call_toDynCommListGood != null && !call_toDynCommListGood.isCanceled())
            call_toDynCommListGood.cancel();
        if (call_getDynCommList != null && !call_getDynCommList.isCanceled())
            call_getDynCommList.cancel();
        if (call_toCommDyn != null && !call_toCommDyn.isCanceled())
            call_toCommDyn.cancel();
    }

    public void sendComm(String msg) {
        //发表评论
        V.d("发布的内容:" + msg);
        call_toCommDyn = LoveJob.toCommDyn(dynPid, msg, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                Utils.showToast(context, "评论成功");
                adapter.removeAll();
                addDataToCommList();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);

            }
        });

    }
}
