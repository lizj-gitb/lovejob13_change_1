package com.lovejob.view._home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.utils.V;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.view._home
 * Created on 2016-11-25 20:10
 */

public class NewsDetails extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    @Bind(R.id.title_2_newsdetails)
    TextView title2Newsdetails;
    @Bind(R.id.eyes_newsdetails)
    TextView eyesNewsdetails;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.tv_newsdetails_content)
    TextView tvNewsdetailsContent;
    private String newsId;
    private Call call_getNewsDetails;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.newsdetails);
        ButterKnife.bind(this);
        /**
         * 获取上个页面传入的参数
         */
        getLastPageSendParams();
        /**
         * 设置actionbar相关
         */
        setActionbar();
        /**
         * 填充数据
         */
        addData();
    }

    private void addData() {
        call_getNewsDetails = LoveJob.getNewsDetails(newsId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                List<ThePerfectGirl.InformationInfo> informationInfos = thePerfectGirl.getData().getInformationInfos();
                if (informationInfos != null
                        && informationInfos.size() > 0) {
                    ThePerfectGirl.InformationInfo info = informationInfos.get(0);
                    /**
                     * 更新UI
                     */
                    upDataUI(info);
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    private void upDataUI(ThePerfectGirl.InformationInfo info) {
        actionbarTitle.setText(info.getTitle());
        title2Newsdetails.setText(info.getSubTitle());
        eyesNewsdetails.setText(String.valueOf(info.getCount()));
        Glide.with(context).load(StaticParams.QiNiuYunUrl_News + info.getPictrueid()).placeholder(R.drawable.ic_launcher).into(imageView);
        tvNewsdetailsContent.setText(info.getContent());
    }

    private void setActionbar() {
        actionbarSave.setVisibility(View.GONE);
        actionbarShared.setVisibility(View.VISIBLE);
    }

    private void getLastPageSendParams() {
        newsId = getIntent().getStringExtra("newsId");
        if (newsId == null) return;
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {
        if (call_getNewsDetails != null && !call_getNewsDetails.isCanceled())
            call_getNewsDetails.cancel();
    }

    @OnClick({R.id.actionbar_back, R.id.actionbar_shared})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.actionbar_shared:
                /**
                 * 分享
                 */
                toShard();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void toShard() {
//        new ShareAction(context).
        new ShareAction(context).withTargetUrl("http://1597k6h652.imwork.net:18527/lovejob-pn/test.jsp?toOtherActivity=0&otherId=" + newsId)
//        new ShareAction(context).withText("http://192.168.3.8:8081/test?toOtherActivity=0&otherId=" + newsId)

                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .setPlatform(SHARE_MEDIA.QQ)

                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        Utils.showToast(context, "分享成功");
                        V.d("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        V.d("分享错误");
                        Utils.showToast(context, "分享错误");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        V.d("分享被取消");
                        Utils.showToast(context, "分享被取消");
                    }
                }).open();
    }
}
