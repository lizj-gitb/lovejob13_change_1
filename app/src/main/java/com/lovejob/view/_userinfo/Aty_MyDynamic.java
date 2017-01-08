package com.lovejob.view._userinfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.ThreadPoolUtils;
import com.lovejob.model.Utils;
import com.lovejob.view._home.DynDetailsAty;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.dialogs.zdialog.OnDialogItemClickListener;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPreview;

/**
 * 动态
 * Created by Administrator on 2016/11/28.
 */
public class Aty_MyDynamic extends BaseActivity {
    @Bind(R.id.img_history_back)
    ImageView imgHistoryBack;
    @Bind(R.id.img_dynamic_logo)
    CircleImageView imgDynamicLogo;
    @Bind(R.id.tv_dynamic_name)
    TextView tvDynamicName;
    @Bind(R.id.lv_mydynamic)
    PullToRefreshListView lvMydynamic;
    @Bind(R.id.dynamic_beij)
    RelativeLayout dynamicBeij;
    private FastAdapter<ThePerfectGirl.DynamicDTO> adapter;
    private Activity context;
    private String userPid;
    private Call call_getDynamic;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_mydynamci);
        ButterKnife.bind(this);
        context = this;
        AppPreferences apppreference = new AppPreferences(context);
        userPid = apppreference.getString(StaticParams.FileKey.__USERPID__, "");
        initAdapter();
        addData();
//        lvMydynamic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                ImageView clear = (ImageView)view.findViewById(R.id.img_clear_dynamic);
//                int num = clear.getVisibility();
//                if (num==8){
//                    clear.setVisibility(View.VISIBLE);
//                }else {
//                    clear.setVisibility(View.GONE);
//                }
//                return true;
//            }
//        });
        lvMydynamic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DynDetailsAty.class);
                intent.putExtra("dynPid", adapter.getItem(position-1).getPid());
                startActivity(intent);
            }
        });
    }

    private void addData() {
        call_getDynamic = LoveJob.getDynamic(userPid, new OnAllParameListener() {
            @Override
            public void onSuccess(final ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getDynamicInfos() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getDynamicInfos().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getDynamicInfos().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }

                tvDynamicName.setText(thePerfectGirl.getData().getUserInfoDTO().getRealName());
                if (!TextUtils.isEmpty(thePerfectGirl.getData().getUserInfoDTO().getBackground())) {

                    ThreadPoolUtils.getInstance().addTask(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Bitmap myBitmap = Glide.with(context)
                                        .load(StaticParams.ImageURL + thePerfectGirl.getData().getUserInfoDTO().getBackground())
                                        .asBitmap() //必须
                                        .centerCrop()
                                        .into(500, 500)
                                        .get();
                                final Drawable drawable = new BitmapDrawable(myBitmap);
                                HandlerUtils.post(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void run() {
                                        dynamicBeij.setBackground(drawable);
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    dynamicBeij.setBackgroundResource(R.mipmap.beij);
                }
                Glide.with(context).load(StaticParams.ImageURL + thePerfectGirl.getData().getUserInfoDTO().getPortraitId()).into(imgDynamicLogo);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.DynamicDTO>(context,R.layout.item_lv_dynamic) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_dynamic, position);
                ((TextView) viewHolder.getView(R.id.tv_dynamic_content)).setText(getItem(position).getContent());
                if (getItem(position).getTimeDec().equals("今天") || getItem(position).getTimeDec().equals("昨天")) {
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_time)).setText(getItem(position).getTimeDec());
                } else {
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_time)).setVisibility(View.GONE);
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_timemonth)).setVisibility(View.VISIBLE);
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_timeday)).setVisibility(View.VISIBLE);

                    String s1 = String.format("%tF%n", getItem(position).getCreateTime());
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_timemonth)).setText(s1.substring(5, 7) + "月");
                    ((TextView) viewHolder.getView(R.id.tv_dynamic_timeday)).setText(s1.substring(8, 10));
                }
                ((ImageView)viewHolder.getView(R.id.img_clear_dynamic)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ZDialog.showZDlialog(context, "提示", "是否删除", "删除", "取消", new OnDialogItemClickListener() {
                            @Override
                            public void onLeftButtonClickListener() {
//                                dialog = Utils.showProgressDliago(context, "正在删除");
                                callList.add(LoveJob.CancelDynamic(adapter.getItem(position).getPid(), new OnAllParameListener() {
                                    @Override
                                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                        adapter.removeAll();
                                        addData();
                                        adapter.notifyDataSetChanged();
//                                        dialog.dismiss();

                                    }

                                    @Override
                                    public void onError(String msg) {
//                                        dialog.dismiss();
                                        Utils.showToast(context, msg);
                                    }
                                }));
                            }

                            @Override
                            public void onRightButtonClickListener() {

                            }
                        }).show();
                    }
                });

                final RecyclerView recyclerView = (RecyclerView) viewHolder.getView(R.id.rv_dynamic_pic);
                recyclerView.setTag(position);
                final ArrayList<String> selectedPhotos = new ArrayList<>();
                String[] imgs = getItem(position).getPictrueid().split("\\|");
                ImageView imgView = (ImageView) viewHolder.getView(R.id.img_dynamic_pic);
                if (TextUtils.isEmpty(imgs[0])) {
                    recyclerView.setVisibility(View.GONE);
                    imgView.setVisibility(View.GONE);
                } else if (imgs.length == 1) {
                    imgView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Glide.with(context).load(StaticParams.ImageURL + imgs[0]).into(imgView);
                } else {
                    if (imgs.length > 0 && !TextUtils.isEmpty(imgs[0])) {
                        recyclerView.setVisibility(View.VISIBLE);
                        imgView.setVisibility(View.GONE);
                        for (int i = 0; i < imgs.length; i++) {
                            selectedPhotos.add(StaticParams.ImageURL + imgs[i]);
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
                                photos.add(StaticParams.ImageURL + imgs[i]);
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
        }

        ;
        lvMydynamic.setAdapter(adapter);
    }




    @Override
    public void onResume_() {
    }

    @Override
    public void onDestroy_() {
        if (call_getDynamic != null && call_getDynamic.isCanceled())
            call_getDynamic.cancel();
    }


    @OnClick(R.id.img_history_back)
    public void onClick(){
        AppManager.getAppManager().finishActivity();
    }


}
