package com.lovejob.view._home.dyndetailstabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.utils.V;
import com.zwy.logger.Logger;
import com.zwy.nineimageslook.ImageInfo;
import com.zwy.nineimageslook.NineGridView;
import com.zwy.nineimageslook.preview.NineGridViewClickAdapter;
import com.zwy.pulltorefresh.BaseQuickAdapter;
import com.zwy.pulltorefresh.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/20.
 */

public class NewsDetails extends BaseActivity {

    @Bind(R.id.actionbar_back)
    ImageView mActionbarBack;
    @Bind(R.id.actionbar_title)
    TextView mActionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView mActionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView mActionbarShared;
    @Bind(R.id.tv_newsdetails_subtitle)
    TextView mTvNewsdetailsSubtitle;
    @Bind(R.id.tv_newsdetails_date)
    TextView mTvNewsdetailsDate;
    @Bind(R.id.tv_newsdetails_time)
    TextView mTvNewsdetailsTime;
    @Bind(R.id.tv_newsdetails_number)
    TextView mTvNewsdetailsNumber;
    @Bind(R.id.rv_newsdetails)
    RecyclerView mRvNewsdetails;
    private String newsId;
    private MyAdapter adapter;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.newsdetails);
        ButterKnife.bind(this);
        /**
         * 获取请求的新闻ID
         */
        newsId = getIntent().getStringExtra("newsId");
        Logger.e("接收到的ID：" + newsId);
        if (TextUtils.isEmpty(newsId) || newsId == null) {
            AppManager.getAppManager().finishActivity();
        }
        /**
         * 获取新闻详情数据
         */
        getNewsDataFromSevice();

        setActionbar();
    }

    private void getNewsDataFromSevice() {
        callList.add(LoveJob.getNewsDetails(newsId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() != null &&
                        thePerfectGirl.getData().getInformationInfo() != null) {
                    ThePerfectGirl.InformationInfo newsDetails = thePerfectGirl.getData().getInformationInfo();
                    mActionbarTitle.setText(newsDetails.getTitle());
                    mTvNewsdetailsSubtitle.setText(newsDetails.getSubTitle());
                    mTvNewsdetailsDate.setText(new SimpleDateFormat("MM-dd").format(newsDetails.getReleaseTime()));
                    mTvNewsdetailsTime.setText(new SimpleDateFormat("HH:mm").format(newsDetails.getReleaseTime()));
                    mTvNewsdetailsNumber.setText(String.valueOf(newsDetails.getCount()));


                    mRvNewsdetails.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new MyAdapter(R.layout.item_news_details, newsDetails.getInformationInfoList());
                    mRvNewsdetails.setAdapter(adapter);
                    adapter.setNewData(newsDetails.getInformationInfoList());
                }
            }

            @Override
            public void onError(String msg) {

            }
        }));
    }

    private void setActionbar() {
        mActionbarSave.setVisibility(View.GONE);
        mActionbarTitle.setText("新闻详情");
        mActionbarShared.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.actionbar_back, R.id.actionbar_shared})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.actionbar_shared:
                Logger.e("分享出去 的ULRL：" + newsId);
                new ShareAction(context).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withText(((MyAdapter) mRvNewsdetails.getAdapter()).getData().get(0).getContent())
                        .withMedia(new UMImage(context, shardURL))
                        .withTitle(mTvNewsdetailsSubtitle.getText().toString())
                        .withTargetUrl(StaticParams.URL_Shared+"?otherId=" + newsId + "&toOtherActivity=0")
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .open();
                break;
        }
    }

    private String shardURL = "";

    private class MyAdapter extends BaseQuickAdapter<ThePerfectGirl.NewsContentDetails, BaseViewHolder> {
        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param layoutResId The layout resource id of each item.
         * @param data        A new list is created out of this one to avoid mutable list
         */
        public MyAdapter(int layoutResId, List<ThePerfectGirl.NewsContentDetails> data) {
            super(layoutResId, data);
        }

        /**
         * Implement this method and use the helper to adapt the view to the given item.
         *
         * @param helper A fully initialized helper.
         * @param item   The item that needs to be displayed.
         */
        @Override
        protected void convert(BaseViewHolder helper, ThePerfectGirl.NewsContentDetails item) {
            TextView textView = ((TextView) helper.getView(R.id.tv_item_newscontent));
            textView.setText("\u3000\u3000" + item.getContent());
            textView.setAnimation(AnimationUtils
                    .loadAnimation(mContext, R.anim.design_fab_in));
            NineGridView nineGridView = (NineGridView) helper.getView(R.id.ng_item_newscontentimages);
            List<ImageInfo> imageInfos = new ArrayList<>();
            if (item.getPictrueid() != null && !TextUtils.isEmpty(item.getPictrueid())) {
                String[] imgs = item.getPictrueid().split("\\|");
                for (int i = 0; i < imgs.length; i++) {
                    if (!TextUtils.isEmpty(imgs[i])) {
                        imageInfos.add(new ImageInfo(StaticParams.QiNiuYunUrl_News + imgs[i], StaticParams.QiNiuYunUrl_News + imgs[i]));
                        shardURL = StaticParams.QiNiuYunUrl_News + imgs[i];
                        Logger.e("++++++++" + shardURL);
                    }
                }
                nineGridView.setAdapter(new NineGridViewClickAdapter(mContext, imageInfos));
            }
        }
    }

}
