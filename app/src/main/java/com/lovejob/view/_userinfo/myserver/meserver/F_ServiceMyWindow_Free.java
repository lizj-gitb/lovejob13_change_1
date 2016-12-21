package com.lovejob.view._userinfo.myserver.meserver;

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
import android.widget.TextView;

import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view._userinfo.myserver.Aty_ReleaseService;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
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
public class F_ServiceMyWindow_Free extends BaseFragment {
    @Bind(R.id.lv_ser_my_skill)
    MyListView lvSerMySkill;
    @Bind(R.id.sv_ser_my_skill)
    PullToRefreshScrollView svSerMySkill;
    private View view;
    private Activity context;
    FastAdapter<ThePerfectGirl.ServerDTO> adapter_lv;
    private Call call_getServiceList1;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_ser_my_skill, null);
        ButterKnife.bind(this, view);
        context = getActivity();
        initAdapter();
        return view;
    }

    private void addData() {
       call_getServiceList1 =  LoveJob.getServiceList1("65", "2",null, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (svSerMySkill != null)
                    svSerMySkill.onRefreshComplete();
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getServerDTOs() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getServerDTOs().size(); i++) {
                        adapter_lv.addItem(thePerfectGirl.getData().getServerDTOs().get(i));
                    }
                    adapter_lv.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(getActivity(), msg);
                svSerMySkill.onRefreshComplete();
            }
        });
    }

    private void initAdapter() {
        adapter_lv = new FastAdapter<ThePerfectGirl.ServerDTO>(context,R.layout.item_lv_myser_skill) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolde = FFViewHolder.get(context,convertView,parent,R.layout.item_lv_myser_skill,position);
                ((TextView) viewHolde.getView(R.id.tv_ser_my_skill_title)).setText(getItem(position).getTitle());
                ((TextView) viewHolde.getView(R.id.tv_ser_my_skill_money)).setVisibility(View.GONE);
                ((TextView) viewHolde.getView(R.id.tv_ser_my_skill_payment)).setVisibility(View.GONE);
                ((TextView) viewHolde.getView(R.id.tv_my_skill_goodtalk)).setText(getItem(position).getLevel());
                ((TextView) viewHolde.getView(R.id.tv_my_skill_buycount)).setText(getItem(position).getSoldCount());
                ((TextView) viewHolde.getView(R.id.tv_my_skill_content)).setText(getItem(position).getContent());
                final RecyclerView recyclerView = (RecyclerView) viewHolde.getView(R.id.gv_my_skill_img);
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
                setItemOnListener(viewHolde.getConvertView(), position);
                return viewHolde.getConvertView();
            }
        };
        lvSerMySkill.setAdapter(adapter_lv);
    }

    private void setItemOnListener(View convertView, final int position) {
        TextView textView = (TextView) convertView.findViewById(R.id.tv_my_skill_edit);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Aty_ReleaseService.class);
//                intent.putExtra("serverPid", adapter_lv.getItem(position).getServerPid());
//                intent.putExtra("title",adapter_lv.getItem(position).getTitle());
//                intent.putExtra("money",adapter_lv.getItem(position).getMoney().toString());
//                intent.putExtra("content",adapter_lv.getItem(position).getContent());
//                intent.putExtra("state",adapter_lv.getItem(position).getState());
                intent.putExtra("serverDto", adapter_lv.getItem(position));
                intent.putExtra("have","0");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter_lv.removeAll();
        addData();
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
        if (call_getServiceList1 != null && call_getServiceList1.isCanceled())
            call_getServiceList1.cancel();

    }
}
