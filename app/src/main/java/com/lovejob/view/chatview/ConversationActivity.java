package com.lovejob.view.chatview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.utils.V;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by Bob on 15/8/18.
 * 会话页面
 */
public class ConversationActivity extends FragmentActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.txt1)
    TextView mTitle;
    @Bind(R.id.img3)
    TextView img3;
    @Bind(R.id.rl)
    RelativeLayout rl;
    private String mTargetId, title;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                AppManager.getAppManager().finishActivity();
//                AppManager.getAppManager().finishActivity(ConversationActivity.class);
            }
        });
        setActionBar();

        getIntentDate(intent);

        isReconnect(intent);
        /**
         * 设置会话界面操作的监听器。
         */
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                V.d("点击用户头像");
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                V.d("长按用户头像");
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                V.d("点击消息");
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                V.d("连接被点击");
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                V.d("长按消息");
                return false;
            }
        });
        mHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        mTitle.setText(title + "正在输入...");
                        break;
                    case 2:
                        mTitle.setText(title + "正在讲话...");
                        break;

                    case -1:
                        mTitle.setText("与" + title + "聊天中");
                        break;
                }
            }
        };
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
        setInputState();
    }

    private void setInputState() {
        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    android.os.Message message = mHandler.obtainMessage();
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    int count = typingStatusSet.size();
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            //显示“对方正在输入”
                            message.what = 1;
                        } else if (objectName.equals(voiceTag.value())) {
                            //显示"对方正在讲话"
                            message.what = 2;
                        }
                    } else {
                        //当前会话没有用户正在输入，标题栏仍显示原来标题
                        message.what = -1;
                    }
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        title = intent.getData().getQueryParameter("title");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
        setActionBarTitle(title);
    }


    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }


    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {


        String token = null;

//        if (DemoContext.getInstance() != null) {
//
//            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
//        }

        //push或通知过来
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
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }

    /**
     * 设置 actionbar 事件
     */
    private void setActionBar() {
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }


    /**
     * 设置 actionbar title
     */
    private void setActionBarTitle(String targetid) {

        mTitle.setText("与"+targetid+"聊天中");
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

                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
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
}
