package com.lovejob;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.ThreadPoolUtils;
import com.lovejob.model.Utils;
import com.lovejob.view.login.AQQQ;
import com.lovejob.view.login.LoginAcitvity;
import com.lovejob.view.login.SplashActivity;
//import com.squareup.leakcanary.LeakCanary;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
import com.v.rapiddev.http.okhttp3.zokhttp.ZokHttp;
import com.v.rapiddev.logger.Logger;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.Utils_RapidDev;
import com.v.rapiddev.utils.V;
import com.zwy.activitymanage.AppManager;
import com.zwy.nineimageslook.NineGridView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static android.os.Build.VERSION.SDK;
import static com.lovejob.model.StaticParams.RequestCode.Android6_PerM_Request;


/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.base
 * Created on 2016-11-21 10:40
 */

public class MyApplication extends MultiDexApplication {
    public static LocationClient mLocationClient;
    private static Context applicationContext;
    private static Handler mHandler;
    private static AppPreferences appPreferences;
    private static ThePerfectGirl thePerfectGirl;

    public static Handler getHandler() {
        return mHandler;
    }

    private static MyApplication mMyApplication;
    public static PushAgent mPushAgent;
    @Override
    public void onCreate() {
        super.onCreate();
        uploadCrachFile();
        V.isDebug = true;
        V.d("into MyApplication...");
        mMyApplication = this;
        UMShareAPI.get(this);
        /**
         * 初始化UMeng相关数据
         */
        initUMengInfos();
        /**
         * 初始化百度定位相关参数
         */
        initBaiDuLocationInfos();
        /**
         * 初始化融云相关参数
         */
        RongIM.init(this);
//        /**
//         * 初始化facebook图片加载
//         */
//        Fresco.initialize(getApplicationContext());
        Logger.init(StaticParams.TAG);

        this.applicationContext = getApplicationContext();
        mHandler = new Handler();
        ZDialog.setHandler(mHandler);
        ZokHttp.setHandler(mHandler);

        appPreferences = new AppPreferences(this.applicationContext);
        V.d("开始读取资源文件／／／／／开始反序列化到对象");
        readDateFromResuseAndToObject();
        //注册App异常崩溃处理器
        AppException.getInstance().init(getApplicationContext());

//        //上线删除下面的代码
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
////             This process is dedicated to LeakCanary for heap analysis.
////             You should not init your app in this process.
//            LeakCanary.install(this);
//        }
        SDKInitializer.initialize(this);
        initNinePic();
    }

    private void initNinePic() {
        NineGridView.setImageLoader(new NineGridView.ImageLoader() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String url) {
                //使用Glide加载图片
                Glide.with(context).load(url).placeholder(R.mipmap.ic_launcher).into(imageView);
            }

            @Override
            public Bitmap getCacheImage(String url) {
                return null;
            }
        });
    }

    private void uploadCrachFile() {
//        if (!TextUtils.isEmpty(new AppPreferences(getApplicationContext()).getString(StaticParams.FileKey.__CrashFile__, ""))) {
//            V.d("开始上传异常信息到服务器");
//            LoveJob.uploadCrachFile();
//        }
    }

    public static MyApplication getApplication() {
        return mMyApplication;
    }

    public static ThePerfectGirl getJobType() {
        return thePerfectGirl;
    }

    private void readDateFromResuseAndToObject() {
        V.d("开始反序列化");
        ThreadPoolUtils.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = getResources().getAssets().open("lovejob_jobtypes.ser");
                    File file = new File(Utils_RapidDev.getSDCardPath() + "lovejob_jobtypes.ser");
                    if (file.exists()) file.delete();
                    if (!file.exists()) file.createNewFile();
                    Utils.inputstreamtofile(inputStream, file);
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                    thePerfectGirl = (ThePerfectGirl) in.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                    V.e("从资源文件反序列化失败,e:" + e.toString());
                }
            }
        });
    }

    public static String getCity() {
        return appPreferences.getString(StaticParams.FileKey.__City__, "");
    }

    public static AppPreferences getAppPreferences() {
        return appPreferences;
    }

//    public class MyConnectCallback extends RongIMClient.ConnectCallback {
//
//        /**
//         * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
//         */
//        @Override
//        public void onTokenIncorrect() {
//            Toast.makeText(MyApplication.this, R.string.canNotConnectService, Toast.LENGTH_SHORT).show();
//            V.e("connect rong service error");
//        }
//
//        /**
//         * 连接融云成功
//         *
//         * @param userid 当前 token
//         */
//        @Override
//        public void onSuccess(String userid) {
//            AppConfig.isConnectRongService = true;
//            V.d("connect to rong service success!");
//        }
//
//        /**
//         * 连接融云失败
//         *
//         * @param errorCode 错误码，可到官网 查看错误码对应的注释
//         */
//        @Override
//        public void onError(RongIMClient.ErrorCode errorCode) {
//            V.e("connect rong service error,errorCode:" + errorCode);
//        }
//    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static Context getContext() {
        return applicationContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /**
         * apk分包
         */
        MultiDex.install(this);
    }

    private void initBaiDuLocationInfos() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener(getApplicationContext()));    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void initUMengInfos() {
        V.d("start init umeng infos ...");
        PlatformConfig.setWeixin("wx28d1e39fb52b12bf", "69a672ed725aef9c15298986b32c376a");
        PlatformConfig.setQQZone("1105707606", "w7lxyDyD8RmZlMKq");
         mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                V.d("Umeng push init ok,DeviceToken：" + deviceToken);
                StaticParams.DeviceToken = deviceToken + "1";
            }

            @Override
            public void onFailure(String s, String s1) {
                V.e("init umeng infos error:" + s + ",s1:" + s1);
            }
        });

//        UmengMessageHandler messageHandler = new UmengMessageHandler() {
//            @Override
//            public Notification getNotification(Context context, UMessage msg) {
//                V.d("000000");
////                V.d("自定义通知相关数据：\n" + msg.custom + "\n"
////                        + "activity:" + msg.activity + "\nafter_open:" + msg.after_open
////                        + "\nalias:" + msg.alias + "\ndisplay_type:" + msg.display_type
////                        + "\nicon:" + msg.icon + "\nimg:" + msg.img + "\nlargeIcon:" + msg.largeIcon
////                        + "\nmessage_id:" + msg.message_id + "\nmsg_id:" + msg.msg_id + "\nsound:" + msg.sound
////                        + "\ntask_id:" + msg.task_id + "\ntext:" + msg.text + "\nticker:" + msg.ticker
////                        + "\ntitle:" + msg.title + "\nurl:" + msg.url + "\nclickOrDismiss:" + msg.clickOrDismiss
////                        + "\nextra:" + msg.extra + "\ngetRaw:" + msg.getRaw() + "\nbuilder_id:" + msg.builder_id);
//
////                switch (msg.builder_id) {
////                    case 1:
////                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//////                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//////                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//////                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//////                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//////                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
//////                        builder.setContent(myNotificationView);
//////                        Notification mNotification = builder.build();
//////                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
//////                        mNotification.contentView = myNotificationView;
////                        return null;
////                    default:
////                        //默认为0，若填写的builder_id并不存在，也使用默认。
////                        return super.getNotification(context, msg);
////                }
//                return null;
//            }
//        };
//        mPushAgent.setMessageHandler(messageHandler);

//        UmengMessageHandler messageHandler = new UmengMessageHandler() {
//            @Override
//            public void dealWithCustomMessage(Context context, UMessage uMessage) {
//                V.d("aaaa");
//                AQQQ aqqq = new Gson().fromJson(uMessage.custom, AQQQ.class);
//
//                if (aqqq != null && aqqq.getMessageType() != null
//                        && !TextUtils.isEmpty(aqqq.getMessageType())) {
//                    if (aqqq.getMessageType().equals("0")) {
//                        Utils.showToast(getApplicationContext(), "您的账号在异地登录，请重新登录");
//                        com.zwy.preferences.AppPreferences appPreferences = new com.zwy.preferences.AppPreferences(getApplicationContext());
//                        appPreferences.put(StaticParams.FileKey.__LOCALTOKEN__, "");
//                        AppManager.getAppManager().AppExit(context);
//                        startActivity(new Intent(context, LoginAcitvity.class));
//                    }
//                }
//
//            }
//
////            @Override
////            public Notification getNotification(Context context, UMessage uMessage) {
////
////                switch (uMessage.builder_id) {
////                    case 1:
////
////                        return null;
////                    default:
////                        return super.getNotification(context, uMessage);
////
////                }
////
////            }
//
//
//        };
//        mPushAgent.setMessageHandler(messageHandler);

    }


    public class MyLocationListener implements BDLocationListener {
        private Context context = null;

        public MyLocationListener(Context context) {
            this.context = context;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getCity() != null && location.getLongitude() != 0 && location.getLatitude() != 0) {
                Intent intent = new Intent();
                intent.setAction("com.lovejob.location");
                intent.putExtra("city", location.getCity());
                context.sendBroadcast(intent);
                AppPreferences preferences = new AppPreferences(context);
                preferences.put(StaticParams.FileKey.__City__, location.getCity());
                preferences.put(StaticParams.FileKey.__LONGITUDE__, String.valueOf(location.getLongitude()));
                preferences.put(StaticParams.FileKey.__LATITUDE__, String.valueOf(location.getLatitude()));
                MyApplication.mLocationClient.stop();
                V.d("baidu location was successed,the params was saved.and was stop locatiton");
            }
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            V.d(sb.toString());
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }
}
