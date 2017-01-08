package com.lovejob.view._home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lovejob.view.WriteView;
import com.lovejob.view._home.dyndetailstabs.F_Bad;
import com.lovejob.view._home.dyndetailstabs.F_Good;
import com.lovejob.view._home.dyndetailstabs.f_comm.F_comm;
import com.lovejob.view._othersinfos.Others;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPreview;

import com.lovejob.view._home.dyndetailstabs.F_Bad;
import com.lovejob.view._home.dyndetailstabs.F_Good;
import com.lovejob.view._home.dyndetailstabs.f_comm.F_comm;
//import com.lovejob.view.publicview.Aty_View_Write;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.view._home
 * Created on 2016-11-22 06:02
 */

public class DynDetailsAty extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    @Bind(R.id.img_dyndetails_userlogo)
    CircleImageView imgDyndetailsUserlogo;
    @Bind(R.id.tv_dyndetails_username)
    TextView tvDyndetailsUsername;
    @Bind(R.id.tv_dyndetails_date)
    TextView tvDyndetailsDate;
    @Bind(R.id.tv_dyndetails_content)
    TextView tvDyndetailsContent;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.img_dyndetails_like)
    ImageView imgDyndetailsLike;
    @Bind(R.id.tv_dyndetails_like_number)
    TextView tvDyndetailsLikeNumber;
    @Bind(R.id.xx1)
    ImageView xx1;
    @Bind(R.id.lt_dyndetails_like)
    LinearLayout ltDyndetailsLike;
    @Bind(R.id.img_dyndetails_bad)
    ImageView imgDyndetailsBad;
    @Bind(R.id.tv_dyndetails_bad_number)
    TextView tvDyndetailsBadNumber;
    @Bind(R.id.xx2)
    ImageView xx2;
    @Bind(R.id.lt_dyndetails_bad)
    LinearLayout ltDyndetailsBad;
    @Bind(R.id.img_dyndetails_comm)
    ImageView imgDyndetailsComm;
    @Bind(R.id.tv_dyndetails_comm_number)
    TextView tvDyndetailsCommNumber;
    @Bind(R.id.xx3)
    ImageView xx3;
    @Bind(R.id.lt_dyndetails_comm)
    LinearLayout ltDyndetailsComm;
    @Bind(R.id.fl_dyndetails_view)
    FrameLayout flDyndetailsView;
    @Bind(R.id.prsv)
    PullToRefreshScrollView prsv;
    @Bind(R.id.lt1)
    ImageView lt1;
    @Bind(R.id.lt2)
    ImageView lt2;
    @Bind(R.id.lt3)
    ImageView lt3;
    private String dynPid;
    private ArrayList<Fragment> fragments;
    private boolean isComm = false;
    private ThePerfectGirl.UserInfoDTO girl;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.dyndetails);
        ButterKnife.bind(this);
        dynPid = getIntent().getStringExtra("dynPid");
        isComm = getIntent().getBooleanExtra("isComm", false);
        if (TextUtils.isEmpty(dynPid)) {
            return;
        }
        /**
         * 获取数据
         */
        getData();
        /**
         * 初始化三个tab页面
         */
        initPages();
        /**
         * 设置点击事件
         */
        lt1.setOnClickListener(new myOclick(1));
        lt2.setOnClickListener(new myOclick(2));
        lt3.setOnClickListener(new myOclick(3));

        ltDyndetailsLike.setOnClickListener(new myOclick(4));
        ltDyndetailsBad.setOnClickListener(new myOclick(5));
        ltDyndetailsComm.setOnClickListener(new myOclick(6));
        if (isComm) {
            repaleF(fragments.get(2));
            xx1.setVisibility(View.INVISIBLE);
            xx2.setVisibility(View.INVISIBLE);
            xx3.setVisibility(View.VISIBLE);
        } else {
            repaleF(fragments.get(0));
            xx1.setVisibility(View.VISIBLE);
            xx2.setVisibility(View.INVISIBLE);
            xx3.setVisibility(View.INVISIBLE);
        }
        setActionbar();
    }

    private void initPages() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new F_Good());
        fragments.add(new F_Bad());
        fragments.add(new F_comm());


        replaceFragment(fragments.get(0));
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_dyndetails_view, fragment).commit();
    }

    public String getDynPid() {
        return dynPid;
    }

    private void getData() {
        callList.add(LoveJob.getDynDetails(dynPid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.isSuccess()) {

                    girl = thePerfectGirl.getData().getDynamicDTO().getReleaseInfo();
                    ThePerfectGirl.DynamicDTO dynamicDTO = thePerfectGirl.getData().getDynamicDTO();
                    Glide.with(context).load(StaticParams.ImageURL + dynamicDTO.getReleaseInfo()
                            .getPortraitId()+"!logo").placeholder(R.drawable.ic_launcher).into(imgDyndetailsUserlogo);
                    tvDyndetailsUsername.setText(dynamicDTO.getReleaseInfo().getRealName());
                    tvDyndetailsDate.setText(dynamicDTO.getCreateTimeDec());
                    tvDyndetailsContent.setText(dynamicDTO.getContent());
                    tvDyndetailsLikeNumber.setText(dynamicDTO.getGoodCount()+"".trim());
                    tvDyndetailsBadNumber.setText(dynamicDTO.getBadCount()+"".trim());
                    tvDyndetailsCommNumber.setText(dynamicDTO.getCommentCount()+"".trim());
                    tvDyndetailsDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dynamicDTO.getCreateTime()));
                    //图片
                    final ArrayList<String> selectedPhotos = new ArrayList<String>();
                    if (dynamicDTO.getPictrueid()!=null){
                        String[] img = dynamicDTO.getPictrueid().split("\\|");
                        for (int i = 0; i < img.length; i++) {
                            if (!TextUtils.isEmpty(img[i])) {
                                selectedPhotos.add(StaticParams.ImageURL + img[i]+"!logo");
                            }
                        }
                    }
                    PhotoAdapter photoAdapter = new PhotoAdapter(context, selectedPhotos, true);
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
                    recyclerView.setAdapter(photoAdapter);

                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .setShowDeleteButton(false)
                                    .start(context);
                        }
                    }));
                } else {
                    V.e("动态详情数据返回失败");
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        }));
    }


    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
    }


    private void repaleF(Fragment f) {
        Bundle bundle = new Bundle();
        bundle.putString("dynPid", dynPid);
        f.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_dyndetails_view, f).commit();

    }

    private void setActionbar() {
        actionbarTitle.setText("动态详情");
        actionbarTitle.setTextColor(Color.WHITE);
        actionbarTitle.setTextSize(18);
        actionbarSave.setVisibility(View.INVISIBLE);

    }

    //
    private void setOnRefreshSv() {
        prsv.setMode(PullToRefreshBase.Mode.DISABLED);
    }


    public class myOclick implements View.OnClickListener {
        private int type = -1;

        public myOclick(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            switch (type) {
                case 1:
//                    赞
                    callList.add(LoveJob.toDynGoodOrBad(dynPid, 1, new OnAllParameListener() {
                        @Override
                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                            if (((F_Good) fragments.get(0)).isVisible()) {

                                ((F_Good) fragments.get(0)).onResume();
                            } else {
                                repaleF(fragments.get(0));
                            }
                            setImgResou(thePerfectGirl.getData().getPoints());
                            xx1.setVisibility(View.VISIBLE);
                            xx2.setVisibility(View.INVISIBLE);
                            xx3.setVisibility(View.INVISIBLE);
                            int p = thePerfectGirl.getData().getPoints();
                            int c = thePerfectGirl.getData().getCount();
                            tvDyndetailsLikeNumber.setText(String.valueOf(c));
                        }

                        @Override
                        public void onError(String msg) {
                            Utils.showToast(context, msg);
                        }
                    }));
                    break;
                case 2:
                    callList.add(LoveJob.toDynGoodOrBad(dynPid, 0, new OnAllParameListener() {
                        @Override
                        public void onSuccess(ThePerfectGirl thePerfectGirl) {
                            if (((F_Bad) fragments.get(1)).isVisible()) {

                                ((F_Bad) fragments.get(1)).onResume();
                            } else {
                                repaleF(fragments.get(1));
                            }
                            xx1.setVisibility(View.INVISIBLE);
                            xx2.setVisibility(View.VISIBLE);
                            xx3.setVisibility(View.INVISIBLE);
                            setImgResou(thePerfectGirl.getData().getPoints());
                            int c = thePerfectGirl.getData().getCount();
                            tvDyndetailsBadNumber.setText(String.valueOf(c));
                        }

                        @Override
                        public void onError(String msg) {
                            Utils.showToast(context, msg);
                        }
                    }));
                    break;
                case 3:
                    if (((F_comm) fragments.get(2)).isVisible()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("hite", "请输入评论内容");
                        bundle.putInt("textSize", 13);
                        bundle.putString("context", "");
                        bundle.putInt("requestCode", StaticParams.RequestCode.RequsetCode_DynDetails_Input);
                        Intent intent = new Intent(context, WriteView.class);
                        intent.putExtra("data", bundle);
//                        int inputType = bundle.getInt("inputType");
                        startActivityForResult(intent, StaticParams.RequestCode.RequsetCode_DynDetails_Input);
                    } else {
                        repaleF(fragments.get(2));
                    }
                    xx1.setVisibility(View.INVISIBLE);
                    xx2.setVisibility(View.INVISIBLE);
                    xx3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    //切换点赞tab
                    if (!fragments.get(0).isVisible()) {
                        repaleF(fragments.get(0));
                        xx1.setVisibility(View.VISIBLE);
                        xx2.setVisibility(View.INVISIBLE);
                        xx3.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 5:
                    //切换差评tab
                    if (!fragments.get(1).isVisible()) {
                        repaleF(fragments.get(1));
                        xx1.setVisibility(View.INVISIBLE);
                        xx2.setVisibility(View.VISIBLE);
                        xx3.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 6:
                    //切换评论tab
                    if (!fragments.get(2).isVisible()) {
                        repaleF(fragments.get(2));
                        xx1.setVisibility(View.INVISIBLE);
                        xx2.setVisibility(View.INVISIBLE);
                        xx3.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    //
    private void setImgResou(int isPointGood) {
        switch (isPointGood) {
            case 0:
                //已差评
                imgDyndetailsLike.setImageResource(R.mipmap.icon_good_common);
                imgDyndetailsBad.setImageResource(R.mipmap.icon_bad_on);//icon_bad_common
                lt2.setImageResource(R.mipmap.chapinglan);
                lt1.setImageResource(R.mipmap.zantahui);
                break;
            case 1:
                //已点赞
                imgDyndetailsLike.setImageResource(R.mipmap.icon_good_on);
                imgDyndetailsBad.setImageResource(R.mipmap.icon_bad_common);//
                lt1.setImageResource(R.mipmap.zantalan);
                lt2.setImageResource(R.mipmap.chapinghui);
                break;

            case 2:
                //什么都可以干
                imgDyndetailsLike.setImageResource(R.mipmap.icon_good_common);
                imgDyndetailsBad.setImageResource(R.mipmap.icon_bad_common);//
                lt1.setImageResource(R.mipmap.zantahui);
                lt2.setImageResource(R.mipmap.chapinghui);
                break;
        }
    }

    @OnClick({R.id.actionbar_back, R.id.img_dyndetails_userlogo, R.id.lt_dyndetails_like, R.id.lt_dyndetails_bad, R.id.lt_dyndetails_comm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.img_dyndetails_userlogo:
                Intent intent = new Intent(context, Others.class);
                intent.putExtra("userType",girl.getType());
                intent.putExtra("userId", girl.getUserId());
                startActivity(intent);
                break;
            case R.id.lt_dyndetails_like:
                if (!fragments.get(0).isVisible()) {

                    repaleF(fragments.get(0));
                    xx1.setVisibility(View.VISIBLE);
                    xx2.setVisibility(View.INVISIBLE);
                    xx3.setVisibility(View.INVISIBLE);
                }

                break;
            case R.id.lt_dyndetails_bad:
                if (!fragments.get(1).isVisible()) {

                    repaleF(fragments.get(1));
                    xx1.setVisibility(View.INVISIBLE);
                    xx2.setVisibility(View.VISIBLE);
                    xx3.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.lt_dyndetails_comm:
                if (!fragments.get(2).isVisible()) {

                    repaleF(fragments.get(2));
                    xx1.setVisibility(View.INVISIBLE);
                    xx2.setVisibility(View.INVISIBLE);
                    xx3.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticParams.RequestCode.RequsetCode_DynDetails_Input) {
            if (data != null) {
                String msg = data.getStringExtra("content");
                ((F_comm) fragments.get(2)).sendComm(msg);
            }
        }
    }
}
