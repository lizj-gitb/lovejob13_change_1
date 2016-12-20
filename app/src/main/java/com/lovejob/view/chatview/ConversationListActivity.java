package com.lovejob.view.chatview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.v.rapiddev.utils.V;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * ClassType:会话列表
 * User:wenyunzhao
 * ProjectName:ChatTestDemo
 * Package_Name:com.lovejob.chattestdemo
 * Created on 2016-11-07 21:50
 */

public class ConversationListActivity extends FragmentActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.txt1)
    TextView title;
    @Bind(R.id.img3)
    TextView img3;
    @Bind(R.id.rl)
    RelativeLayout rl;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_chat);
        ButterKnife.bind(this);
        setActionBarTitle();
        isReconnect();
        context = this;
//        getUnReadMsgNumber();
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(final String userid) {
                V.d("~~~~~" + userid);
                HandlerUtils.post(new Runnable() {
                    @Override
                    public void run() {
                        getUserDate(userid);
                    }
                });
//               UserInfo userInfo =new UserInfo(userid,"张三", Uri.parse(StaticParam.TestUserVo));
                return null;
            }
        }, true);
    }

    private void getUserDate(String userid) {
        LoveJob.getUserNameAndUserLogo(userid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                List<ThePerfectGirl.UserInfoDTO> userInfoDTOList = thePerfectGirl.getData().getUserInfoDTOs();
                if (thePerfectGirl.getData().getUserInfoDTOs() == null || thePerfectGirl.getData().getUserInfoDTOs().size() == 0) {
                    return;
                }
                if (userInfoDTOList.get(0)!=null &&userInfoDTOList.get(0).getUserId()!=null &&  userInfoDTOList.get(0).getRealName()!=null && userInfoDTOList.get(0).getPortraitId()!=null){
                    UserInfo userInfo = new UserInfo(userInfoDTOList.get(0).getUserId(), userInfoDTOList.get(0).getRealName(), Uri.parse(StaticParams.QiNiuYunUrl+userInfoDTOList.get(0).getPortraitId()));
                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void getUnReadMsgNumber() {
        /**
         *  设置接收消息的监听器。
         */
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                V.d("-------" + message.getTargetId());
                return false;
            }
        });
        RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                switch (connectionStatus) {

                    case CONNECTED://连接成功。
                        V.d("-------" + "连接成功");
                        break;
                    case DISCONNECTED://断开连接。
                        V.d("-------" + "断开连接");
                        break;
                    case CONNECTING://连接中。
                        V.d("-------" + "连接中");
                        break;
                    case NETWORK_UNAVAILABLE://网络不可用。
                        V.d("-------" + "网络不可用");
                        break;
                    case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                        V.d("-------" + "用户账户在其他设备登录，本机会被踢掉线");
                        break;
                }
            }
        });

        /**
         * 设置会话列表界面操作的监听器。
         */
        RongIM.setConversationListBehaviorListener(new RongIM.ConversationListBehaviorListener() {
            @Override
            public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
                V.d("用户头像被点击");
                return false;
            }

            @Override
            public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
                V.d("用户头像被长按击");
                return false;
            }

            @Override
            public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
                V.d("item 长按");
                return false;
            }

            @Override
            public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
                V.d("item 点击");
                return false;
            }
        });

        /**
         * 接收未读消息的监听器。
         *
         * @param listener          接收所有未读消息消息的监听器。
         */
        RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new RongIM.OnReceiveUnreadCountChangedListener() {
            @Override
            public void onMessageIncreased(int i) {
                V.d("所有未读消息数：" + i);
            }
        });

        /**
         * 设置接收未读消息的监听器。
         *
         * @param listener          接收未读消息消息的监听器。
         * @param conversationTypes 接收指定会话类型的未读消息数。
         */
        RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new RongIM.OnReceiveUnreadCountChangedListener() {
            @Override
            public void onMessageIncreased(int i) {
                V.d("私聊未读消息数：" + i);
            }
        }, Conversation.ConversationType.PRIVATE);

    }

    /**
     * 加载 会话列表 ConversationListFragment
     */
    private void enterFragment() {

        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
    }

    /**
     * 设置 actionbar 事件
     */
    private void setActionBarTitle() {
        title.setText("会话列表");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect() {

        Intent intent = getIntent();
        String token = null;

//        if (DemoContext.getInstance() != null) {
//
//            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
//        }

        //push，通知或新消息过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    reconnect(token);
                } else {
                    enterFragment();
                }
            }
        }
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {

                    enterFragment();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }
}
