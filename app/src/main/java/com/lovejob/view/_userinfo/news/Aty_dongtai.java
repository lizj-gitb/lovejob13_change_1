package com.lovejob.view._userinfo.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view.DragPointView;
import com.lovejob.view._home.DynDetailsAty;
import com.lovejob.view._home.dyndetailstabs.f_comm.DynCommDetails;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.views.CircleImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/2.
 */
public class Aty_dongtai extends BaseActivity {


    @Bind(R.id.img_newsdongtai_back)
    ImageView imgNewsdongtaiBack;
    @Bind(R.id.tv_newsdongtai_pinglun)
    TextView tvNewsdongtaiPinglun;
    @Bind(R.id.tv_newsdongtai_good)
    TextView tvNewsdongtaiGood;
    @Bind(R.id.tv_newsdongtai_bad)
    TextView tvNewsdongtaiBad;
    @Bind(R.id.lv_newsdongtai_pinglun)
    PullToRefreshListView lvNewsdongtaiPinglun;
    @Bind(R.id.lv_newsdongtai_good)
    ListView lvNewsdongtaiGood;
    @Bind(R.id.lv_newsdongtai_bad)
    ListView lvNewsdongtaiBad;
    @Bind(R.id.line_1)
    View line1;
    @Bind(R.id.line_2)
    View line2;
    @Bind(R.id.line_3)
    View line3;
    @Bind(R.id.tv_drag_comm)
    DragPointView tvDragComm;
    @Bind(R.id.tv_drag_good)
    DragPointView tvDragGood;
    @Bind(R.id.tv_drag_bad)
    DragPointView tvDragBad;
    private Activity context;
    FastAdapter<ThePerfectGirl.ViewDynamic> pladapter;
    FastAdapter<ThePerfectGirl.DynamicPointPraiseInfo> goodadapter;
    FastAdapter<ThePerfectGirl.DynamicPointPraiseInfo> badadapter;
    private Call call_getNewsDynamic;
    int dynamiccount, goodcount, badcount;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_newsdongtai);
        ButterKnife.bind(this);
        context = this;
        dynamiccount = getIntent().getIntExtra("dynamiccount", 0);
        goodcount = getIntent().getIntExtra("goodcount", 0);
        badcount = getIntent().getIntExtra("badcount", 0);
        if (dynamiccount > 0) {
            tvDragComm.setVisibility(View.VISIBLE);
            tvDragComm.setText(String.valueOf(dynamiccount));
        }
        if (goodcount > 0) {
            tvDragGood.setVisibility(View.VISIBLE);
            tvDragGood.setText(String.valueOf(goodcount));
        }
        if (badcount > 0) {
            tvDragBad.setVisibility(View.VISIBLE);
            tvDragBad.setText(String.valueOf(badcount));
        }
        initplAdapter();
        addplData();
        line2.setVisibility(View.INVISIBLE);
        line3.setVisibility(View.INVISIBLE);


    }

    private void addplData() {
        call_getNewsDynamic = LoveJob.getNewsDynamic(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getViewDynamics() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getViewDynamics().size(); i++) {
                        pladapter.addItem(thePerfectGirl.getData().getViewDynamics().get(i));
                    }
                    pladapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    private void initplAdapter() {
        pladapter = new FastAdapter<ThePerfectGirl.ViewDynamic>(context, R.layout.item_lv_newspinglun) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_newspinglun, position);
                ((TextView) viewHolder.getView(R.id.tv_pinglu_name)).setText(getItem(position).getUserInfoDTO().getRealName());
                ((TextView) viewHolder.getView(R.id.tv_pinglun_time)).setText(getItem(position).getCreateDateDec());
                if (getItem(position).getfPicture() == null) {
                    viewHolder.getView(R.id.img_pinglun_mycontent).setVisibility(View.VISIBLE);
                    viewHolder.getView(R.id.img_pinglun_pic).setVisibility(View.GONE);
                    ((TextView) viewHolder.getView(R.id.img_pinglun_mycontent)).setText(getItem(position).getfContent());
                } else {
                    final ArrayList<String> selectedPhotos = new ArrayList<>();
                    String[] imgs = getItem(position).getfPicture().split("\\|");
                    if (imgs.length > 0 && !TextUtils.isEmpty(imgs[0])) {
                        ((RecyclerView) viewHolder.getView(R.id.img_pinglun_pic)).setVisibility(View.VISIBLE);
                        selectedPhotos.add(StaticParams.QiNiuYunUrl + imgs[0]);
                    } else {
                        ((RecyclerView) viewHolder.getView(R.id.img_pinglun_pic)).setVisibility(View.GONE);
                    }
                    final PhotoAdapter photoAdapter = new PhotoAdapter(context, selectedPhotos, true);
                    ((RecyclerView) viewHolder.getView(R.id.img_pinglun_pic)).setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
                    ((RecyclerView) viewHolder.getView(R.id.img_pinglun_pic)).setAdapter(photoAdapter);
                }
                ((TextView) viewHolder.getView(R.id.tv_pinglun_content)).setText(getItem(position).getContent());
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getUserInfoDTO().getPortraitId()).into((CircleImageView) viewHolder.getView(R.id.img_pinglu_logo));
//                ((TextView) viewHolder.getView(R.id.tv_pinglun_time)).setText(getItem(position).g);
                lvNewsdongtaiPinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (pladapter.getItem(position - 1).getType().equals("2")) {
                            Intent intent = new Intent(context, DynCommDetails.class);
                            intent.putExtra("dynamicCommentPid", pladapter.getItem(position - 1).getDynamicId());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, DynDetailsAty.class);
                            intent.putExtra("dynPid", pladapter.getItem(position - 1).getDynamicId());
                            startActivity(intent);
                        }
                    }
                });
                return viewHolder.getConvertView();
            }
        };
        lvNewsdongtaiPinglun.setAdapter(pladapter);
    }


    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_getNewsDynamic != null && call_getNewsDynamic.isCanceled())
            call_getNewsDynamic.cancel();
    }


    @OnClick({R.id.img_newsdongtai_back, R.id.tv_newsdongtai_pinglun, R.id.tv_newsdongtai_good, R.id.tv_newsdongtai_bad})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_newsdongtai_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tv_newsdongtai_pinglun:
                lvNewsdongtaiPinglun.setVisibility(View.VISIBLE);
                lvNewsdongtaiBad.setVisibility(View.GONE);
                lvNewsdongtaiGood.setVisibility(View.GONE);
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_newsdongtai_good:
                lvNewsdongtaiPinglun.setVisibility(View.GONE);
                lvNewsdongtaiBad.setVisibility(View.GONE);
                lvNewsdongtaiGood.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                line1.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                initGoodAdapter();
                addGoodData();
                break;
            case R.id.tv_newsdongtai_bad:
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.VISIBLE);
                lvNewsdongtaiPinglun.setVisibility(View.GONE);
                lvNewsdongtaiBad.setVisibility(View.VISIBLE);
                lvNewsdongtaiGood.setVisibility(View.GONE);
                initBadAdapter();
                addBadData();
                break;
        }
    }

    private void addBadData() {
        LoveJob.getNewsDynamicGood("137", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getDynamicGoodPointPraiseInfos() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getDynamicGoodPointPraiseInfos().size(); i++) {
                        badadapter.addItem(thePerfectGirl.getData().getDynamicGoodPointPraiseInfos().get(i));
                    }
                    badadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    private void initBadAdapter() {
        badadapter = new FastAdapter<ThePerfectGirl.DynamicPointPraiseInfo>(context, R.layout.item_lv_newsbad) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_newsbad, position);
                ((TextView) viewHolder.getView(R.id.tv_bad_name)).setText(getItem(position).getUserInfo().getRealName());
                ((TextView) viewHolder.getView(R.id.tv_bad_time)).setText(getItem(position).getCreateTimeDec());

                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getUserInfo().getPortraitId()).into((CircleImageView) viewHolder.getView(R.id.img_bad_logo));
                if (getItem(position).getfPicture() == null) {
                    viewHolder.getView(R.id.tv_bad_content).setVisibility(View.VISIBLE);
                    viewHolder.getView(R.id.img_bad_pic).setVisibility(View.GONE);
                    ((TextView) viewHolder.getView(R.id.tv_bad_content)).setText(getItem(position).getfContent());
                } else {
                    final ArrayList<String> selectedPhotos = new ArrayList<>();
                    String[] imgs = getItem(position).getfPicture().split("\\|");
                    if (imgs.length > 0 && !TextUtils.isEmpty(imgs[0])) {
                        ((RecyclerView) viewHolder.getView(R.id.img_bad_pic)).setVisibility(View.VISIBLE);
                        selectedPhotos.add(StaticParams.QiNiuYunUrl + imgs[0]);
                    } else {
                        ((RecyclerView) viewHolder.getView(R.id.img_bad_pic)).setVisibility(View.GONE);
                    }
                    final PhotoAdapter photoAdapter = new PhotoAdapter(context, selectedPhotos, true);
                    ((RecyclerView) viewHolder.getView(R.id.img_bad_pic)).setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
                    ((RecyclerView) viewHolder.getView(R.id.img_bad_pic)).setAdapter(photoAdapter);
                }
                lvNewsdongtaiBad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, DynDetailsAty.class);
                        intent.putExtra("dynPid", badadapter.getItem(position).getDynamicPid());
                        startActivity(intent);
                    }
                });
                return viewHolder.getConvertView();
            }
        };
        lvNewsdongtaiBad.setAdapter(badadapter);
    }

    private void initGoodAdapter() {
        goodadapter = new FastAdapter<ThePerfectGirl.DynamicPointPraiseInfo>(context, R.layout.item_lv_newsgood) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_newsgood, position);
                ((TextView) viewHolder.getView(R.id.tv_good_name)).setText(getItem(position).getUserInfo().getRealName());
                ((TextView) viewHolder.getView(R.id.tv_good_time)).setText(getItem(position).getCreateTimeDec());
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getUserInfo().getPortraitId()).into((CircleImageView) viewHolder.getView(R.id.img_good_logo));
                if (getItem(position).getfPicture() == null) {
                    viewHolder.getView(R.id.tv_good_content).setVisibility(View.VISIBLE);
                    viewHolder.getView(R.id.img_good_pic).setVisibility(View.GONE);
                    ((TextView) viewHolder.getView(R.id.tv_good_content)).setText(getItem(position).getfContent());
                } else {
                    final ArrayList<String> selectedPhotos = new ArrayList<>();
                    String[] imgs = getItem(position).getfPicture().split("\\|");
                    if (imgs.length > 0 && !TextUtils.isEmpty(imgs[0])) {
                        ((RecyclerView) viewHolder.getView(R.id.img_good_pic)).setVisibility(View.VISIBLE);
                        selectedPhotos.add(StaticParams.QiNiuYunUrl + imgs[0]);
                    } else {
                        ((RecyclerView) viewHolder.getView(R.id.img_good_pic)).setVisibility(View.GONE);
                    }
                    final PhotoAdapter photoAdapter = new PhotoAdapter(context, selectedPhotos, true);
                    ((RecyclerView) viewHolder.getView(R.id.img_good_pic)).setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
                    ((RecyclerView) viewHolder.getView(R.id.img_good_pic)).setAdapter(photoAdapter);
                }
                lvNewsdongtaiGood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, DynDetailsAty.class);
                        intent.putExtra("dynPid", goodadapter.getItem(position).getDynamicPid());
                        startActivity(intent);
                    }
                });
                return viewHolder.getConvertView();
            }
        };
        lvNewsdongtaiGood.setAdapter(goodadapter);
    }

    private void addGoodData() {
        LoveJob.getNewsDynamicGood("136", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getDynamicGoodPointPraiseInfos() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getDynamicGoodPointPraiseInfos().size(); i++) {
                        goodadapter.addItem(thePerfectGirl.getData().getDynamicGoodPointPraiseInfos().get(i));
                    }
                    goodadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }


}
