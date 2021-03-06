package com.lovejob.view._othersinfos;


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

import com.bumptech.glide.Glide;
import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.view._home.DynDetailsAty;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.MyListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPreview;


/**
 * Created by Administrator on 2016/11/17.
 *  他的个人中心—动态—Fragment
 */

public class F_OtherDynamic extends BaseFragment {
    View view;
    @Bind(R.id.lv_mydynamic)
    MyListView lvMydynamic;
    String userPid;
    private Activity context;
    private FastAdapter<ThePerfectGirl.DynamicDTO> adapter;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_other_dynamic, null);
        ButterKnife.bind(this, view);
        context = getActivity();
        userPid = getActivity().getIntent().getStringExtra("userId");
        initAdapter();
        addData();
        lvMydynamic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DynDetailsAty.class);
                intent.putExtra("dynPid", adapter.getItem(position).getPid());
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    /**
     * 获取数据
     */
    private void addData() {
        LoveJob.getDynamic(userPid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getDynamicInfos() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getDynamicInfos().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getDynamicInfos().get(i));
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
     * 初始化适配器
     */
    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.DynamicDTO>(context, R.layout.item_lv_dynamic) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_dynamic, position);
                ((TextView) viewHolder.getView(R.id.tv_dynamic_content)).setText(getItem(position).getContent());               //内容说明
                ((ImageView)viewHolder.getView(R.id.img_clear_dynamic)).setVisibility(View.GONE);
                if (getItem(position).getTimeDec().equals("今天") || getItem(position).getTimeDec().equals("昨天")) {
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_time)).setText(getItem(position).getTimeDec());              //时间
                } else {
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_time)).setVisibility(View.GONE);
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_timemonth)).setVisibility(View.VISIBLE);
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_timeday)).setVisibility(View.VISIBLE);

                    String s1 = String.format("%tF%n", getItem(position).getCreateTime());
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_timemonth)).setText(s1.substring(5, 7) + "月");
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_timeday)).setText(s1.substring(8, 10));
                }


                //图片
                final RecyclerView recyclerView = (RecyclerView) viewHolder.getView(R.id.rv_dynamic_pic);
                recyclerView.setTag(position);
                final ArrayList<String> selectedPhotos = new ArrayList<>();
                if ( getItem(position).getPictrueid()!=null){
                    String[] imgs = getItem(position).getPictrueid().split("\\|");
                    ImageView imgView = (ImageView) viewHolder.getView(R.id.img_dynamic_pic);
                    if (TextUtils.isEmpty(imgs[0])) {
                        recyclerView.setVisibility(View.GONE);
                        imgView.setVisibility(View.GONE);
                    } else if (imgs.length == 1) {
                        imgView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        Glide.with(context).load(StaticParams.ImageURL + imgs[0]+"!logo").into(imgView);
                    }else {
                        if (imgs.length > 0 && !TextUtils.isEmpty(imgs[0])) {
                            recyclerView.setVisibility(View.VISIBLE);
                            imgView.setVisibility(View.GONE);
                            for (int i = 0; i < imgs.length; i++) {
                                selectedPhotos.add(StaticParams.ImageURL + imgs[i]+"!logo");
                            }
                        }
                }
                    final PhotoAdapter photoAdapter = new PhotoAdapter(context, selectedPhotos, true);
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
                    recyclerView.setAdapter(photoAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position1) {

                            final ArrayList<String> photos = new ArrayList<>();
                            String[] imgs = getItem((int) recyclerView.getTag()).getPictrueid().split("\\|");
                            for (int i = 0; i < imgs.length; i++) {
                                photos.add(StaticParams.ImageURL + imgs[i]+"!logo");
                            }
                            PhotoPreview.builder()
                                    //http://oejyij5hl.bkt.clouddn.com/lovejob_195107463214808463580161563989879.jpg
                                    .setPhotos(photos)
                                    .setCurrentItem(position1)
                                    .setShowDeleteButton(false)
                                    .start(context);

                        }
                    }));
                }
                return viewHolder.getConvertView();
            }
        };
        lvMydynamic.setAdapter(adapter);
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
