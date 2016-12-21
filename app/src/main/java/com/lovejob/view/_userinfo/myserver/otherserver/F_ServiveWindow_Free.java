package com.lovejob.view._userinfo.myserver.otherserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view._userinfo.myserver.Aty_SerSkillDetails;
import com.lovejob.view.payinfoviews.PayViewSelectPayment;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.views.MyListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Administrator on 2016/11/29.
 */
public class F_ServiveWindow_Free extends BaseFragment {
    @Bind(R.id.lv_ser_skill)
    MyListView lvSerSkill;
//    @Bind(R.id.sv_ser_skill)
//    PullToRefreshScrollView svSerSkill;
    private View view;
    private Activity context;
    String userId = null;
    private FastAdapter<ThePerfectGirl.ServerDTO> adapter_lv;
    private Call call_getServiceList1;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_ser_skill, null);
        ButterKnife.bind(this, view);
        initAdapter();
        userId = getActivity().getIntent().getStringExtra("userId");
        context =getActivity();
        addData();
        return view;
    }

    private void addData() {
        call_getServiceList1 =LoveJob.getServiceList1("65", "2", userId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                if (svSerSkill!=null)
//                    svSerSkill.onRefreshComplete();
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getServerDTOs() != null){
                    for (int i = 0; i < thePerfectGirl.getData().getServerDTOs().size(); i++) {
                        adapter_lv.addItem(thePerfectGirl.getData().getServerDTOs().get(i));
                    }
                    adapter_lv.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
//                svSerSkill.onRefreshComplete();
            }
        });

    }

    private void initAdapter() {
        adapter_lv = new FastAdapter<ThePerfectGirl.ServerDTO>(context,R.layout.item_lv_ser_skill) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context,convertView,parent,R.layout.item_lv_ser_skill,position);
                ((TextView) viewHolder.getView(R.id.tv_ser_skill_title)).setText(getItem(position).getTitle());
                ((TextView) viewHolder.getView(R.id.tv_ser_skill_money)).setVisibility(View.GONE);
                ((TextView) viewHolder.getView(R.id.tv_ser_skill_payment)).setVisibility(View.GONE);
                ((TextView) viewHolder.getView(R.id.tv_ser_skill_haoping)).setText(getItem(position).getLevel());
                ((TextView) viewHolder.getView(R.id.tv_ser_skill_sold)).setText(getItem(position).getSoldCount());
                ((TextView) viewHolder.getView(R.id.tv_ser_skill_explain)).setText(getItem(position).getContent());
                ((ImageView)viewHolder.getView(R.id.bt_ser_skill_buy)).setImageResource(R.mipmap.button_buyservice_off);
                final RecyclerView recyclerView = (RecyclerView) viewHolder.getView(R.id.gv_ser_skill_content);
                if (!TextUtils.isEmpty(getItem(position).getPictrueId()) && getItem(position).getPictrueId() != null) {
                    final ArrayList<String> selectedPhotos = new ArrayList<>();
                    String[] l = getItem(position).getPictrueId().split("\\|");
                    for (int i = 0; i < l.length; i++) {
                        selectedPhotos.add(StaticParams.QiNiuYunUrl + l[i]);
                    }

                    final PhotoAdapter photoAdapter = new PhotoAdapter(context, selectedPhotos, true);

                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
                    recyclerView.setAdapter(photoAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            PhotoPreview.builder()
                                    .setPhotos(((PhotoAdapter) recyclerView.getAdapter()).getList())
                                    .setCurrentItem(position)
                                    .setShowDeleteButton(false)
                                    .start(context);
                        }
                    }));
                }
                setOnItemListener(viewHolder.getConvertView(),position);
                return viewHolder.getConvertView();
            }
        };
        lvSerSkill.setAdapter(adapter_lv);
        lvSerSkill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, Aty_SerSkillDetails.class);
//                intent.putExtra("isEdit",adapter.getItem(position).getShowApplyBtn()== 0 ? false : true);
                intent.putExtra("serPid", adapter_lv.getItem(position).getServerPid());
                startActivity(intent);
            }

        });
    }

    private void setOnItemListener(View convertView, final int position) {
        ImageView buyImg = (ImageView) convertView.findViewById(R.id.bt_ser_skill_buy);
        buyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = Utils.showProgressDliago(context,"正在购买，请稍后");
             callList.add(LoveJob.BuyFreeServer(adapter_lv.getItem(position).getServerPid(), new OnAllParameListener() {
                 @Override
                 public void onSuccess(ThePerfectGirl thePerfectGirl) {
                     Utils.showToast(context,"购买成功");
                     dialog.dismiss();
                     AppManager.getAppManager().finishActivity();
                 }

                 @Override
                 public void onError(String msg) {
                     dialog.dismiss();
                     Utils.showToast(context,msg);
                 }
             }));
            }
        });
    }

    @Override
    public void loadData() {

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (call_getServiceList1 != null && call_getServiceList1.isCanceled())
            call_getServiceList1.cancel();
    }
}
