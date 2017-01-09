package com.lovejob.view._userinfo;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.bumptech.glide.Glide;
import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.OnUpLoadImagesListener;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.ImageModle;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.ThreadPoolUtils;
import com.lovejob.model.Utils;
import com.lovejob.ms.MainActivityMs;
import com.lovejob.view._othersinfos.Others;
import com.lovejob.view._userinfo.myinformation.Aty_Resume;
import com.lovejob.view._userinfo.mylist.Aty_MyList;
import com.lovejob.view._userinfo.myserver.ServiceMyActivity;
import com.lovejob.view._userinfo.mywalletinfos.Aty_Money;
import com.lovejob.view._userinfo.news.Aty_News;
import com.lovejob.view._userinfo.setting.Aty_Setting;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static android.app.Activity.RESULT_OK;

public class F_UserInfo extends BaseFragment {
    @Bind(R.id.img_f_my_userlogo)
    CircleImageView imgFMyUserlogo;
    @Bind(R.id.img_f_my_sex)
    ImageView imgFMySex;
    @Bind(R.id.tv_f_my_username)
    TextView tvFMyUsername;
    @Bind(R.id.pb_f_my_progressbar)
    ProgressBar pbFMyProgressbar;
    @Bind(R.id.tv_f_my_userdatanum)
    TextView tvFMyUserdatanum;
    @Bind(R.id.img_f_my_touserdetails)
    ImageView imgFMyTouserdetails;
    @Bind(R.id.tv_f_my_Humour)
    TextView tvFMyHumour;
    @Bind(R.id.tv_f_my_Loveliness)
    TextView tvFMyLoveliness;
    @Bind(R.id.tv_f_my_Lively)
    TextView tvFMyLively;
    @Bind(R.id.tv_f_my_PostCount)
    TextView tvFMyPostCount;
    @Bind(R.id.tv_f_my_RepliesCount)
    TextView tvFMyRepliesCount;
    @Bind(R.id.InformationCount)
    TextView InformationCount;

    @Bind(R.id.ttv1)
    TextView ttv1;
    @Bind(R.id.ttv2)
    TextView ttv2;
    @Bind(R.id.ttv3)
    TextView ttv3;

    @Bind(R.id.lt_f_my_msg)
    LinearLayout ltFMyMsg;
    @Bind(R.id.DynamicCount)
    TextView DynamicCount;
    @Bind(R.id.lt_f_my_dyn)
    LinearLayout ltFMyDyn;
    @Bind(R.id.lt_f_my_money)
    LinearLayout ltFMyMoney;
    @Bind(R.id.lt_f_my_list)
    LinearLayout ltFMyList;
    @Bind(R.id.lt_f_my_myservice)
    LinearLayout ltFMyMyservice;
    @Bind(R.id.lt_f_my_redeme)
    LinearLayout ltFMyRedeme;
    @Bind(R.id.lt_f_my_setting)
    LinearLayout ltFMySetting;

    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.ll2)
    LinearLayout ll2;
    @Bind(R.id.ll3)
    LinearLayout ll3;

    @Bind(R.id.sv_f_my_refreshview)
    PullToRefreshScrollView svFMyRefreshview;
    @Bind(R.id.lt_userbg)
    LinearLayout lt_userbg;
    @Bind(R.id.rl_1)
    LinearLayout rl1;
    private View mainView;
    private List<Call> calls = new ArrayList<>();
    private boolean isUpLoadUserBg = false;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    private MyActvityResult broadcastReceiver;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.f_userinfo, null);
        ButterKnife.bind(this, mainView);
        setRefreshListener();
        //我的回帖，目前功能没实现
        rl1.setVisibility(View.GONE);
        broadcastReceiver = new MyActvityResult();
        IntentFilter intentFilter = new IntentFilter("com.lovejob.onactivityresult");
        context.registerReceiver(broadcastReceiver, intentFilter);
        return mainView;
    }

    private void setRefreshListener() {
        svFMyRefreshview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        svFMyRefreshview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                addUserInfoData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    /**
     * 进入该方法时表示当前fragment是可见的，可以加载用户数据 注意：请做标示，当数据加载完一次后第二次不去加载
     */
    @Override
    public void loadData() {
//        if (isAddData)
//            addUserInfoData();
//        isAddData = false;
    }

    @Override
    public void onPause() {
        super.onPause();
//        isUpLoadUserBg = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        addUserInfoData();
//        if (isUpLoadUserBg) {
//            Object[] o = ((MainActivityMs) getActivity()).getRequestCodeAndData();
//            if (((Intent) o[2]) != null)
//                updataUserBg(((Intent) o[2]).getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS));
//        }
    }

    private void addUserInfoData() {
        calls.add(
                LoveJob.getUserHomeInfos(null, new OnAllParameListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        try {
                            svFMyRefreshview.onRefreshComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        final ThePerfectGirl.UserInfoDTO userInfo = thePerfectGirl.getData().getUserInfoDTO();
                        Glide.with(context).load(StaticParams.ImageURL + userInfo.getPortraitId()+"!logo").dontAnimate().placeholder(R.drawable.ic_launcher).into(imgFMyUserlogo);

                        int userSex = userInfo.getUserSex() == 1 ? R.mipmap.icon_male : R.mipmap.icon_famale;

                        imgFMySex.setImageResource(userSex);
                        tvFMyUsername.setText(userInfo.getRealName());
                        //userImpression 用户标签 TODO
                        //TODO 资料完整度、消息数、
                        int i = (int) (thePerfectGirl.getData().getUserInfoDTO().getImproveDegree() * 100);
                        pbFMyProgressbar.setProgress(i);//模拟
                        tvFMyUserdatanum.setText((thePerfectGirl.getData().getUserInfoDTO().getImproveDegree() * 100) + "%".trim());//模拟
                        if (userInfo.getCount() > 0) {
                            InformationCount.setVisibility(View.VISIBLE);
                            InformationCount.setText(String.valueOf(userInfo.getCount()));
                        } else {
                            InformationCount.setVisibility(View.GONE);
                        }
                        if (thePerfectGirl.getData().getUserInfoDTO().getUserImpression() == null
                                || thePerfectGirl.getData().getUserInfoDTO().getUserImpression().size() == 0) {
                            ll1.setVisibility(View.GONE);
                            ll2.setVisibility(View.GONE);
                            ll3.setVisibility(View.GONE);

                        } else {
                            List<ThePerfectGirl.ImpressionDTO> ss = thePerfectGirl.getData().getUserInfoDTO().getUserImpression();
                            try {
                                ttv1.setText(ss.get(0).getImpression());
                                tvFMyHumour.setText("  :" + String.valueOf(ss.get(0).getCount()));
                            } catch (Exception e) {

                            }
                            try {
                                ttv2.setText(ss.get(1).getImpression());
                                tvFMyLoveliness.setText("  :" + String.valueOf(ss.get(1).getCount()));
                            } catch (Exception e) {

                            }
                            try {
                                ttv3.setText(ss.get(2).getImpression());
                                tvFMyLively.setText("  :" + String.valueOf(ss.get(2).getCount()));
                            } catch (Exception e) {

                            }

                        }
                        if (!TextUtils.isEmpty(userInfo.getBackground())) {

                            ThreadPoolUtils.getInstance().addTask(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Bitmap myBitmap = Glide.with(context)
                                                .load(StaticParams.ImageURL + userInfo.getBackground()+"!logo")
                                                .asBitmap() //必须
                                                .centerCrop()
                                                .into(500, 500)
                                                .get();
                                        final Drawable drawable = new BitmapDrawable(myBitmap);
                                        HandlerUtils.post(new Runnable() {
                                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                            @Override
                                            public void run() {
                                                lt_userbg.setBackground(drawable);
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
                            lt_userbg.setBackgroundResource(R.mipmap.beij);
                        }
                        //获取融云平台未读消息数
//                /**
//                 * 接收未读消息的监听器。
//                 *
//                 * @param listener          接收所有未读消息消息的监听器。
//                 */
//                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new RongIM.OnReceiveUnreadCountChangedListener() {
//                    @Override
//                    public void onMessageIncreased(int i) {
//                        V.d("所有未读消息数：" + i);
//                    }
//                });
//                        RongIM.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
//                            @Override
//                            public void onSuccess(Integer integer) {
//                                svFMyRefreshview.onRefreshComplete();
//                                V.d("获取未读消息数成功：" + integer);
//                                if (userInfo.getCount() + integer > 0) {
//                                    InformationCount.setVisibility(View.VISIBLE);
//                                    InformationCount.setText(String.valueOf(userInfo.getCount() + integer));
//                                } else {
//                                    InformationCount.setVisibility(View.GONE);
//                                }
//                            }
//
//                            @Override
//                            public void onError(RongIMClient.ErrorCode errorCode) {
//                                V.e("获取未读消息数失败：" + errorCode);
//                                svFMyRefreshview.onRefreshComplete();
//                            }
//                        });
//                RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
//                    @Override
//                    public void onCountChanged(int i) {
//                        V.d("接收到未读消息数：" + i);
//                    }
//                });
//                        MainActivityMs.mIMKit.getIMCore().
//                        if (!StaticParams.isConnectChetService) {
//                            ToastUtils.showToast(context, "您未连接到聊天服务器，可能是网络异常，请退出重新登录");
//                            return;
//                        }
//                        startActivity(MainActivityMs.mIMKit.getChattingActivityIntent(usePid));
                    }

                    @Override
                    public void onError(String msg) {
                        try {
                            svFMyRefreshview.onRefreshComplete();
                            Utils.showToast(context, msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (Call mCall : calls) {
            if (mCall != null && !mCall.isCanceled()) mCall.cancel();
        }
        calls = null;
        context.unregisterReceiver(broadcastReceiver);
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.img_f_my_touserdetails, R.id.lt_userbg, R.id.tv_f_my_PostCount, R.id.tv_f_my_RepliesCount, R.id.lt_f_my_msg, R.id.lt_f_my_dyn, R.id.lt_f_my_money, R.id.lt_f_my_list, R.id.lt_f_my_myservice, R.id.lt_f_my_redeme, R.id.lt_f_my_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lt_userbg:
                V.d("更换背景图片");
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(context);
                isUpLoadUserBg = true;
                break;

            case R.id.img_f_my_touserdetails:
                V.d("修改用户资料");
                startActivity(new Intent(context, UpDataUserInfos.class));
                context.overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
                break;
            case R.id.tv_f_my_PostCount:
                startActivity(new Intent(context, Others.class));
                break;
            case R.id.tv_f_my_RepliesCount:
                break;
            case R.id.lt_f_my_msg:
                startActivity(new Intent(getActivity(), Aty_News.class));
                //消息
                break;
            case R.id.lt_f_my_dyn:
                startActivity(new Intent(getActivity(), Aty_MyDynamic.class));
                //动态
                break;
            case R.id.lt_f_my_money:
                startActivity(new Intent(getActivity(), Aty_Money.class));
                //钱包

                break;
            case R.id.lt_f_my_list:
                startActivity(new Intent(getActivity(), Aty_MyList.class));
                //账单

                break;
            case R.id.lt_f_my_myservice:
                //服务
                startActivity(new Intent(getActivity(), ServiceMyActivity.class));
                break;
            case R.id.lt_f_my_redeme:
                startActivity(new Intent(getActivity(), Aty_Resume.class));
                //简历

                break;
            case R.id.lt_f_my_setting:
                //设置
                startActivity(new Intent(getActivity(), Aty_Setting.class));
                break;
        }
    }

    //
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        V.d("进入回调");
//        if (resultCode == RESULT_OK) {
//            ArrayList<String> photos = null;
//            if (data != null) {
//                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
//                updataUserBg(photos);
//            }
//        }
//    }
//
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void updataUserBg(final ArrayList<String> photos) {
        if (photos != null && photos.size() > 0) {

            List<File> files = new ArrayList<>();
            for (int i = 0; i < selectedPhotos.size(); i++) {
                files.add(new File(selectedPhotos.get(i)));
            }
            Utils.ImageCo(files, context, true, new OnUpLoadImagesListener() {
                @Override
                public void onSucc(List<ImageModle> imageModleList) {
                    final StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < imageModleList.size(); i++) {
                        stringBuffer.append(imageModleList.get(i).getSmallFileName()).append("|");
                    }
                    HandlerUtils.post(new Runnable() {
                        @Override
                        public void run() {

                            calls.add(LoveJob.uploadUserBg(stringBuffer.toString(), new OnAllParameListener() {
                                @Override
                                public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                    lt_userbg.setBackground(BitmapDrawable.createFromPath(photos.get(0)));
                                }

                                @Override
                                public void onError(String msg) {
                                    Utils.showToast(context, msg);
                                }
                            }));
                        }
                    });
                }

                @Override
                public void onError() {
                    Utils.showToast(context, "图片上传失败，请稍后再试");
                }
            });
//            Utils.yasuo(context, photos, new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    if (msg.arg1 == 9000) {
////                                    File saveFile = new File(getExternalCacheDir(), "compress_" + System.currentTimeMillis() + ".jpg");
////                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(msg.getData().getString("path")));
////                                    NativeUtil.compressBitmap(bitmap, saveFile.getAbsolutePath());
//                        final String path = msg.getData().getString("path");
//                        //开始上传图片  上传的图片路径为msg.getData().getString("path")
//                        HandlerUtils.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                calls.add(LoveJob.uploadUserBg(path, new OnAllParameListener() {
//                                    @Override
//                                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
//
//                                        UploadManager uploadManager = new UploadManager();
//                                        uploadManager.put(path, path,
//
//                                                thePerfectGirl.getData().getUploadToken(), new UpCompletionHandler() {
//                                                    @Override
//                                                    public void complete(String key, ResponseInfo info, JSONObject response) {
//                                                        V.d("上传成功");
//                                                        lt_userbg.setBackground(BitmapDrawable.createFromPath(photos.get(0)));
//                                                    }
//                                                }, null);
//                                    }
//
//                                    @Override
//                                    public void onError(String msg) {
//                                        Utils.showToast(context, msg);
//                                    }
//                                }));
//                            }
//                        });
////                                uploadManager.put(msg.getData().getString("path"), imgNames.get(msg.getData().getInt("index")), uploadToken,
////                                        new UpCompletionHandler() {
////                                            @Override
////                                            public void complete(String key, ResponseInfo info, JSONObject res) {
////                                                //res包含hash、key等信息，具体字段取决于上传策略的设置。
////                                                V.d("上传状态回调：" + key + ",\r\n " + info + ",\r\n " + res);
////                                                uploadImgToQNY_size_add[0]++;
////                                                V.d("index:" + uploadImgToQNY_size_add[0]);
////                                                V.d("size:" + ((photoAdapter.getList().size())));
////                                                if (uploadImgToQNY_size_add[0] == photoAdapter.getList().size()) {
////                                                    Utils.showToast(context, "发布成功");
//////                                        Utils.dissmissDiV(context);
////                                                    context.setResult(resultCode);
////                                                    context.finish();
////                                                }
////                                            }
////                                        }, null);
//                    } else {
//                        V.e("压缩失败一次");
//                    }
//                }
//            });
        }
        isUpLoadUserBg = false;
    }

    public class MyActvityResult extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.lovejob.onactivityresult")) {
                V.d("F_UserInfo接收到广播");
                int requestCode = intent.getIntExtra("requestCode", -1);
                int resultCode = intent.getIntExtra("resultCode", -1);
                if (F_UserInfo.this.isVisible()) {
                    updataUserBg(intent.getStringArrayListExtra("photos"));
                }
            }
        }
    }
}