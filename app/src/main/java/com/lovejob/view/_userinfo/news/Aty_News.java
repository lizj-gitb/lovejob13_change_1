package com.lovejob.view._userinfo.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.ms.MainActivityMs;
import com.lovejob.view.cityselector.cityselector.utils.ToastUtils;
import com.v.rapiddev.utils.V;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Aty_News extends BaseActivity {
    @Bind(R.id.img_news_back)
    ImageView imgNewsBack;
    @Bind(R.id.tvNewMsg)
    TextView tvNewMsg;
    @Bind(R.id.tvNewMsgNumber)
    TextView tvNewMsgNumber;
    @Bind(R.id.img_111)
    ImageView img111;
    @Bind(R.id.tv_news_xiaoxi_time)
    TextView tvNewsXiaoxiTime;
    @Bind(R.id.rl_news_xiaoxi)
    RelativeLayout rlNewsXiaoxi;
    @Bind(R.id.tv222)
    TextView tv222;
    @Bind(R.id.tv_news_tongzhi)
    TextView tvNewsTongzhi;
    @Bind(R.id.img_222)
    ImageView img222;
    @Bind(R.id.tv_news_tongzhi_time)
    TextView tvNewsTongzhiTime;
    @Bind(R.id.rl_news_tongzhi)
    RelativeLayout rlNewsTongzhi;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.img_333)
    ImageView img333;
    @Bind(R.id.tv_news_dongtai_time)
    TextView tvNewsDongtaiTime;
    @Bind(R.id.tv_news_dongtai)
    TextView tvNewsDongtai;
    @Bind(R.id.tv333)
    TextView tv333;
    @Bind(R.id.rl_news_dongtai)
    RelativeLayout rlNewsDongtai;
    int dynamiccount = 0;
    int goodcount = 0;
    int badcount = 0;
    private String userPid;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_news);
        ButterKnife.bind(this);

        //获取未读消息数
//        RongIM.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
//            @Override
//            public void onSuccess(Integer integer) {
//                V.d("获取未读消息数成功：" + integer);
//                if (integer > 0) {
//                    tvNewMsg.setVisibility(View.VISIBLE);
//                    tvNewMsgNumber.setVisibility(View.VISIBLE);
//                    tvNewMsgNumber.setText(String.valueOf(integer) + "条");
//                } else {
//                    tvNewMsg.setVisibility(View.GONE);
//                    tvNewMsgNumber.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//                V.e("获取未读消息数失败：" + errorCode);
//            }
//        });

        //获取最后一条消息的时间
//        List<Conversation> conversationlist = RongIMClient.getInstance().getConversationList();
//        if (conversationlist != null && conversationlist.size() > 0) {
//            tvNewsXiaoxiTime.setVisibility(View.VISIBLE);
//            V.d(new SimpleDateFormat("MM-dd HH:mm:ss").format(conversationlist.get(0).getSentTime()));
//            tvNewsXiaoxiTime.setText(new SimpleDateFormat("HH:mm").format(conversationlist.get(0).getSentTime()));
//        } else {
//            tvNewsXiaoxiTime.setVisibility(View.GONE);
//        }
    }

    private void addData() {
        LoveJob.getNewNum(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                dynamiccount = thePerfectGirl.getData().getUserInfoDTO().getCommCount();
                goodcount = thePerfectGirl.getData().getUserInfoDTO().getGoodCount();
                badcount = thePerfectGirl.getData().getUserInfoDTO().getBadCount();
                if (thePerfectGirl.getData().getUserInfoDTO().getInformCount() == 0) {
                    tv222.setVisibility(View.GONE);
                    tvNewsTongzhi.setVisibility(View.GONE);
                    tvNewsTongzhiTime.setVisibility(View.GONE);
                }
                userPid = thePerfectGirl.getData().getUserInfoDTO().getUserId();
                tvNewsTongzhi.setText(thePerfectGirl.getData().getUserInfoDTO().getInformCount() + "条");
                String s1 = String.format("%tR%n", thePerfectGirl.getData().getUserInfoDTO().getLastTime());
                tvNewsTongzhiTime.setText(s1);
                if (thePerfectGirl.getData().getUserInfoDTO().getDynamicCount() == 0) {
                    tv333.setVisibility(View.GONE);
                    tvNewsDongtai.setVisibility(View.GONE);
                    tvNewsDongtaiTime.setVisibility(View.GONE);
                }
                tvNewsDongtai.setText(thePerfectGirl.getData().getUserInfoDTO().getDynamicCount() + "条");
                String s2 = String.format("%tR%n", thePerfectGirl.getData().getUserInfoDTO().getDynamicNewTime());
                tvNewsDongtaiTime.setText(s2);
            }

            @Override
            public void onError(String msg) {

            }
        });


    }

    @Override
    public void onResume_() {
        addData();

        MainActivityMs.mIMKit.getConversationService().addTotalUnreadChangeListener(new IYWConversationUnreadChangeListener() {
            @Override
            public void onUnreadChange() {
                tvNewMsg.setVisibility(View.VISIBLE);
                tvNewMsgNumber.setText("1条");
            }
        });

    }

    @Override
    public void onDestroy_() {

    }


    @OnClick({R.id.img_news_back, R.id.rl_news_xiaoxi, R.id.rl_news_tongzhi, R.id.rl_news_dongtai})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_news_back:
                finish();
                break;
            case R.id.rl_news_xiaoxi:
                //打开所有会话列表
                if (!StaticParams.isConnectChetService) {
                    ToastUtils.showToast(context, "您未连接到聊天服务器，可能是网络异常，请退出重新登录");
                    return;
                }
                //最近聊天列表
                Intent intent1 = MainActivityMs.mIMKit.getConversationActivityIntent();
                startActivity(intent1);
                V.d("消息");
                break;
            case R.id.rl_news_tongzhi:
                startActivity(new Intent(context, Aty_TongZhi.class));
                V.d("通知");
                break;
            case R.id.rl_news_dongtai:
                Intent intent = new Intent(context, Aty_dongtai.class);
                intent.putExtra("dynamiccount", dynamiccount);
                intent.putExtra("goodcount", goodcount);
                intent.putExtra("badcount", badcount);
                startActivity(intent);
//                startActivity(new Intent(context, Aty_dongtai.class));
                V.d("新动态");
                break;
        }
    }
}
