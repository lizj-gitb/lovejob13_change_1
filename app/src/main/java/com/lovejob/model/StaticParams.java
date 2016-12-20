package com.lovejob.model;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.modles
 * Created on 2016-11-21 10:05
 */

public class StaticParams {
    //Splash页面默认睡眠时间
    public static final int SPLASH_TIME = 0;
    //日志标签
    public static final String TAG = "__lovejob";
    //是否开启日志打印、
    public static boolean isDebug = false;
    //
//    public static final String URL = "http://192.168.3.90/lovejob";
//    public static final String URL = "http://192.168.3.8:8084/lovejob";
//    public static final String URL = "http://192.168.3.37/lovejob";
//    public static final String URL = "http://192.168.3.23/lovejob";
//    public static final String URL = "http://www.congxinwl.com/lovejob";
    public static final String URL = "http://117.34.116.121:8084/lovejob";


    //友盟推送注册成功返回
    public static String DeviceToken = "";
    //QQ微信授权回调的结果里
    public static String openId = "";

    /*
       青牛云图片加载URL前缀（项目所有涉及图片的请求均需加入该前缀）
    */
    public static final String QiNiuYunUrl = "http://oejyij5hl.bkt.clouddn.com/";

    /*
        青牛云图片加载URL前缀（新闻）
         */
    public static final String QiNiuYunUrl_News = "http://oek6mvktl.bkt.clouddn.com/";
    /**
     * 默认获取列表的数量
     */
    public static String ROWS = "5";


    public static class FileKey {
        //是否为第一次启动
        public static final String __ISFIRSTSTARTAPP__ = "isFirstStartApp";
        //是否显示app中的引导页面
        public static final String __ISSHOWGUIDEVIEW__ = "isShowDuideView";

        //公钥
        public static final String __KEY_PUBLICK_CLIENT__ = "publickKey_client";
        //私钥
        public static final String __KEY_PRIVATE_CLIENT__ = "privateKey_client";

        //服务器公钥
        public static final String __KEY_PUBLIC_SERVICE__ = "publickKey_service";

        //登录返回的用户资料
        public static final String __USERPID__ = "userPid";
        public static final String __USERNAME__ = "userName";
        public static final String __USERTYPE__ = "userType";
        public static final String __REALNAME__ = "realName";
        public static final String __LOCALTOKEN__ = "localToken";
        public static final String __LEVEL__ = "level";
        public static final String __RONGTOKEN__ = "rongToken";
        public static final String __IDENTIFY__ = "false";

        //百度定位相关参数存储
        public static final String __City__ = "city";
        public static final String __LONGITUDE__ = "Longitude";
        public static final String __LATITUDE__ = "Latitude";
        //全局异常存储
        public static final String __CrashFile__ = "crashfile";
        public static final String __UserNumber__ = "usernumber";
        public static final String __CrashDeviceType__ = "crashdevicetype";

        public static final String __DynamicContent__ ="dynamcicontent";


    }

    public static class RequestCode {
        //登录页面跳转注册页面
        public static final int REQUESTCODE_LOGINACTIVITY_TO_REGISTERACTIVITY = 0x01;
        //登录页面跳转绑定页面
        public static final int RequestCode_Aty_Login_BoundQQOrWeChatAty = 0x02;

        //登录页面跳转重置密码页面
        public static final int RequestCode_Aty_Login_FORGOTPASSWORD = 0x03;

        //首页跳转城市选择页面
        public static final int RequestCode_F_Home_TO_CitySelector = 0x04;
        //首页跳转发布动态页面
        public static final int RequestCode_F_Home_TO_SendDyn = 0x05;
        //首页跳转新闻详情页面
        public static final int RequestCode_F_Home_TO_NewsDetails = 0x06;
        //首页跳转动态详情页面
        public static final int RequestCode_F_Home_TO_DynDetails = 0x07;
        //动态详情页面评论输入框
        public static final int RequsetCode_DynDetails_Input = 0x08;
        //job页面跳转城市选择页面
        public static final int RequestCode_F_Job_TO_CitySelector = 0x09;

        //job页跳转新闻详情页面
        public static final int RequestCode_F_Job_TO_NewsDetails = 0x10;
        //job页跳转发布长期工作页面
        public static final int RequestCode_F_Job_TO_SendJob = 0x11;


        /* 发布长期工作跳转输入页面 code*/
        public static final int RequestCode_F_SendJob_TO_WriteView_Name = 0x12;
        public static final int RequestCode_F_SendJob_TO_WriteView_Skill = 0x13;
        public static final int RequestCode_F_SendJob_TO_WriteView_Number = 0x14;
        public static final int RequestCode_F_SendJob_TO_WriteView_Price = 0x39;


        public static final int RequestCode_F_SendJob_TO_WriteView_Location = 0x15;
        public static final int RequestCode_F_SendJob_TO_WriteView_Content = 0x0a;
        public static final int RequestCode_F_SendJob_TO_WriteView_WorkType = 0x0b;
        /**
         * 选择职位类型
         */
        public static final int RequestCode_ToWheelSelector_jobtype = 0x0c;
        /**
         * job详情页面评论按钮跳转点评输入页面
         */
        public static final int RequestCode_JobDetails_WriteView = 0x0d;

        /**
         * job详情页面到回复点评的输入页面
         */
        public static final int RequestCode_JobDetailsReComm_WriteView = 0x0e;
        //发布服务
        public static final int RequsetCode_Aty_ReleaseService_To_Title = 9999;
        public static final int RequsetCode_Aty_ReleaseService_To_Price = 9998;
        //个人信息
        public static final int RequsetCode_Person_UserName = 0x2c;
        public static final int RequsetCode_Person_High = 0x2d;
        public static final int RequsetCode_Person_Age = 0x2e;
        public static final int RequsetCode_Person_Address = 0x2f;
        public static final int RequsetCode_Person_Education = 0x30;
        public static final int RequsetCode_Person_Major = 0x31;
        public static final int RequsetCode_Person_School = 0x32;

        //赚点现钱页面跳转新闻详情页面
        public static final int RequestCode_F_Money_TO_NewsDetails = 0x33;

        //赚点现钱页面跳转城市选择页面的返回
        public static final int RequestCode_F_Money_TO_CitySelector = 0x34;
        //从赚点现钱页面跳转到发布兼职工作和长期工作的返回
        public static final int RequestCode_F_Money_TO_SendMoneyWork = 0x40;

        //从创意工作发布页面跳转到输入页面的相关code
        public static final int RequestCode_SendOriWork_TO_WriteView_Title = 0x41;
        public static final int RequestCode_SendOriWork_TO_WriteView_Want = 0x42;
        public static final int RequestCode_SendOriWork_TO_WriteView_Location = 0x43;
        public static final int RequestCode_SendOriWork_TO_WriteView_Price = 0x44;
        public static final int RequestCode_SendOriWork_TO_WriteView_Person = 0x45;
        public static final int RequestCode_SendOriWork_TO_WriteView_PhoneNumber = 0x46;


        //从兼职工作页面跳转的code
        public static final int RequestCode_SendPakWork_TO_WriteView_Title = 0x47;
        public static final int RequestCode_SendPakWork_TO_JobType = 0x48;
        public static final int RequestCode_SendPakWork_TO_WriteView_Skill = 0x49;
        public static final int RequestCode_SendPakWork_TO_WriteView_Hight = 0x50;
        public static final int RequestCode_SendPakWork_TO_WriteView_Context = 0x51;


        public static final int RequestCode_SendPakWork_TO_Location = 0x52;
        public static final int RequestCode_SendPakWork_TO_WriteView_Wanttonum = 0x53;
        public static final int RequestCode_SendPakWork_TO_WriteView_Price = 0x54;
        public static final int RequestCode_SendPakWork_TO_WriteView_Phonenumber = 0x55;
        public static final int RequestCode_SendPakWork_TO_WriteView_Countdown = 0x56;


        //修改个人资料
        public static final int RequestCode_UpDataUserInfo_To_WriteView_UserName = 0x35;
        public static final int RequestCode_UpDataUserInfo_To_WriteView_Commpl = 0x36;
        public static final int RequestCode_UpDataUserInfo_To_WriteView_CommPosition = 0x37;
        public static final int RequestCode_UpDataUserInfo_To_WriteView_Location = 0x38;


        //从创意工作详情页面跳转到输入评论的页面
        public static final int RequestCode_OriWork_To_WriteView_WriteComm = 0x57;
        public static final int RequestCode_OriWork_To_WriteView_WriteReComm = 0x58;


        //跳转到地图选择器页面的code
        public static final int RequestCode_SendJobWork_LocationSelected = 0x59;
        public static final int RequestCode_SendOriWork_LocationSelected = 0x60;
        public static final int RequestCode_SendPakWork_LocationSelected = 0x61;

        public static final int Android6_PerM_Request = 0x99;
    }
}