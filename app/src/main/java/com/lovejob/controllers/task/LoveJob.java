package com.lovejob.controllers.task;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lovejob.AppConfig;
import com.lovejob.MyApplication;
import com.lovejob.model.CheckResponse;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.UserInputModel;
import com.lovejob.model.Utils;
import com.lovejob.model.WorkType;
import com.lovejob.model.bean.Token;
import com.lovejob.view.payinfoviews.PayType;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.http.okhttp3.zokhttp.OnZokHttpResponse;
import com.v.rapiddev.http.okhttp3.zokhttp.ZokHttp;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.security.FFMD5Util;
import com.v.rapiddev.utils.V;

import java.util.HashMap;
import java.util.Map;

import static com.lovejob.model.StaticParams.FileKey.AppException_ExceptionType;
import static com.lovejob.model.StaticParams.FileKey.AppException_FileContent;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.controllers.task
 * Created on 2016-11-25 11:46
 */

public class LoveJob {

    public static Context context = MyApplication.getContext();


    public static Call getSystemVersion(final OnAllParameListener onAllParameListener) {
        Map hashMap = new HashMap();
        hashMap.put("requestType", "703");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, hashMap, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                V.e("版本获取失败");
            }
        }, "获取系统最新版本");
    }

    public static Call upLoadExcption() {
        AppPreferences mAppPreferences = new AppPreferences(context);
        String mExceptionContent = mAppPreferences.getString(AppException_FileContent, "");
        if (TextUtils.isEmpty(mExceptionContent)) {
            mAppPreferences = null;
            V.d("无日志需要上送");
            return null;
        }
        Map hashMap = new HashMap();
        hashMap.put("requestType", "666");
        hashMap.put("type", "1");//1安卓  2ios
        hashMap.put("devicePid", mAppPreferences.getString(AppException_ExceptionType, ""));//设备型号
        hashMap.put("exceptionDec", mExceptionContent);//错误详细信息
        mAppPreferences = null;
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, hashMap, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    AppPreferences appPreferences = new AppPreferences(context);
                    appPreferences.put(AppException_FileContent, "");
                    appPreferences.put(AppException_ExceptionType, "");
                    V.d("日志上传成功，已清空日志");
                } else {
                    V.e("日志上送失败");
                }
            }

            @Override
            public void OnError(String errorMsg) {
                V.e("日志上送失败");
            }
        }, "日志上送");
    }

    //交换密钥的类型  登录或忘记密码传0 第三方登录传1  绑定或注册传2
    public static Call exChangeKey(String phoneNumbeOrOpenId, int exchangeType, final OnAllParameListener onAllParameListener) {
        try {
            String publickKey = new CreateRSAKey().CreateRSAKey(context);
            Map<String, String> map = new HashMap<>();
            map.put("changeType", String.valueOf(exchangeType));
            map.put("phoneNumber", phoneNumbeOrOpenId);
            map.put("requestType", "124");
            map.put("clientKey", publickKey);
            map.put("userIndex", "1");
            return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
                @Override
                public void OnSucc(String data) {
                    ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                    if (thePerfectGirl.isSuccess()) {
                        V.d("交换密钥成功，开始登录操作");
                        V.d("exchange key success...\npublickey_service:" + thePerfectGirl.getData().getPublicKey());
                        AppPreferences appPreferences = new AppPreferences(context);
                        appPreferences.put(StaticParams.FileKey.__KEY_PUBLIC_SERVICE__, thePerfectGirl.getData().getPublicKey());
                        V.d("save service public key success");
                        onAllParameListener.onSuccess(thePerfectGirl);
                    } else {
                        if (thePerfectGirl.getErrcode() == -666) {
                            onAllParameListener.onError("-666");
                        } else {
                            onAllParameListener.onError(thePerfectGirl.getErrmsg());
                        }
                    }
                }

                @Override
                public void OnError(String errorMsg) {
                    onAllParameListener.onError(errorMsg);
                }
            }, "交换密钥");
        } catch (Exception e) {
            e.printStackTrace();
            onAllParameListener.onError("操作系统不安全,请稍后再试");
            return null;
        }
    }

    public static Call startLogin(String userNumber, String password, OnAllParameListener onAllParameListener) {
        return login(userNumber, password, onAllParameListener, false, null);
    }

    public static Call startLogin(String openId, OnAllParameListener onAllParameListener) {
        return login(null, null, onAllParameListener, true, openId);
    }


    private static Call login(String userNumber, String password, final OnAllParameListener onAllParameListener, boolean isLoginOther, String openId) {
        if (isLoginOther && TextUtils.isEmpty(openId)) {
            onAllParameListener.onError("第三方登录时OpenId不可为空");
            return null;
        }

        try {
            Map<String, String> map = new HashMap<>();
            map.put("deviceToken", Utils.encrypt(context, StaticParams.DeviceToken));
            map.put("userIndex", "1");
            if (isLoginOther) {
                map.put("requestType", "127");
                map.put("openId", StaticParams.openId);
            } else {
                map.put("username", userNumber);
                map.put("password", Utils.encrypt(context, FFMD5Util.getMD5String(password)));
                map.put("requestType", "11");
            }
            return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
                @Override
                public void OnSucc(String data) {
                    ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                    if (thePerfectGirl.isSuccess()) {
                        AppPreferences preferences = new AppPreferences(context);
                        preferences.put(StaticParams.FileKey.__USERPID__, thePerfectGirl.getData().getPid());
                        preferences.put(StaticParams.FileKey.__USERNAME__, thePerfectGirl.getData().getUsername());
                        preferences.put(StaticParams.FileKey.__USERTYPE__, thePerfectGirl.getData().getUserType());
                        preferences.put(StaticParams.FileKey.__REALNAME__, thePerfectGirl.getData().getRealName());
                        preferences.put(StaticParams.FileKey.__IDENTIFY__, (!thePerfectGirl.getData().isIdentify()) ? "false" : "true");
                        preferences.put(StaticParams.FileKey.__LEVEL__, thePerfectGirl.getData().getLevel());
                        try {
                            preferences.put(StaticParams.FileKey.__LOCALTOKEN__, Utils.decrypt(context, thePerfectGirl.getData().getLocalToken()));
                            preferences.put(StaticParams.FileKey.__RONGTOKEN__, Utils.decrypt(context, thePerfectGirl.getData().getRongCloudToken()));
                            V.d("saved login some data success");

                            AppPreferences p = new AppPreferences(context);
                            V.e("登录后****本地公钥：" + p.getString(StaticParams.FileKey.__KEY_PUBLICK_CLIENT__, ""));
                            V.e("登录后****本地私钥：" + p.getString(StaticParams.FileKey.__KEY_PRIVATE_CLIENT__, ""));
                            V.e("登录后****服务器公钥：" + p.getString(StaticParams.FileKey.__KEY_PUBLIC_SERVICE__, ""));

                            onAllParameListener.onSuccess(thePerfectGirl);
                        } catch (Exception e) {
                            e.printStackTrace();
                            onAllParameListener.onError("V,请稍后再试");
                        }
                        preferences = null;
                    } else {
                        onAllParameListener.onError(thePerfectGirl.getErrmsg());
                    }
                }

                @Override
                public void OnError(String errorMsg) {
                    onAllParameListener.onError(errorMsg);
                }
            }, "用户登录");
        } catch (Exception e) {
            e.printStackTrace();
            onAllParameListener.onError("V,请稍后再试");
            return null;
        }
    }

    /**
     * 注册 =0  忘记密码=1
     *
     * @return
     */
    public static Call sendMsgCode(String phoneNumber, String msgCodeType, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "01");
        map.put("data", msgCodeType);
        map.put("phoneNumber", phoneNumber);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "发送验证码");
    }

    /**
     * 绑定
     *
     * @param phoneNumber
     * @param msgCode
     * @param psw1
     * @param onAllParameListener
     * @return
     */
    public static Call bound(String phoneNumber, String msgCode, String psw1, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("phoneNumber", phoneNumber);
        map.put("captcha", msgCode);
        map.put("openId", StaticParams.openId);
        map.put("userIndex", "1");
        map.put("requestType", "10");
        try {
            map.put("password", Utils.encrypt(context, FFMD5Util.getMD5String(psw1)));
            map.put("deviceToken", Utils.encrypt(context, StaticParams.DeviceToken));
        } catch (Exception e) {
            e.printStackTrace();
            onAllParameListener.onError("系统不安全");
            return null;
        }
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    AppPreferences preferences = new AppPreferences(context);
                    preferences.put(StaticParams.FileKey.__USERPID__, thePerfectGirl.getData().getPid());
                    preferences.put(StaticParams.FileKey.__USERNAME__, thePerfectGirl.getData().getUsername());
                    preferences.put(StaticParams.FileKey.__USERTYPE__, thePerfectGirl.getData().getUserType());
                    preferences.put(StaticParams.FileKey.__REALNAME__, thePerfectGirl.getData().getRealName());

                    preferences.put(StaticParams.FileKey.__IDENTIFY__, (!thePerfectGirl.getData().isIdentify()) ? "false" : "true");
                    preferences.put(StaticParams.FileKey.__LEVEL__, thePerfectGirl.getData().getLevel());
                    try {
                        preferences.put(StaticParams.FileKey.__LOCALTOKEN__, Utils.decrypt(context, thePerfectGirl.getData().getLocalToken()));
                        preferences.put(StaticParams.FileKey.__RONGTOKEN__, Utils.decrypt(context, thePerfectGirl.getData().getRongCloudToken()));
                        V.d("saved bound some data success");
                        onAllParameListener.onSuccess(thePerfectGirl);
                        AppPreferences p = new AppPreferences(context);
                        V.e("绑定后****本地公钥：" + p.getString(StaticParams.FileKey.__KEY_PUBLICK_CLIENT__, ""));
                        V.e("绑定后****本地私钥：" + p.getString(StaticParams.FileKey.__KEY_PRIVATE_CLIENT__, ""));
                        V.e("绑定后****服务器公钥：" + p.getString(StaticParams.FileKey.__KEY_PUBLIC_SERVICE__, ""));

                    } catch (Exception e) {
                        e.printStackTrace();
                        onAllParameListener.onError("系统不安全");
                    }
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "绑定");
    }

    /**
     * 注册
     *
     * @param userNumber
     * @param password
     * @param userType
     * @param captcha
     * @param context
     * @param onAllParameListener
     * @return
     */
    public static Call register(String userNumber, String password, String userType, String captcha, Activity context, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("password", Utils.encrypt(context, FFMD5Util.getMD5String(password)));
            map.put("userIndex", "1");
            map.put("requestType", "10");
            map.put("userType", userType);
            map.put("phoneNumber", userNumber);
            map.put("captcha", captcha);
            return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
                @Override
                public void OnSucc(String data) {
                    ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                    if (thePerfectGirl.isSuccess()) {
                        onAllParameListener.onSuccess(thePerfectGirl);
                    } else {
                        onAllParameListener.onError(thePerfectGirl.getErrmsg());
                    }
                }

                @Override
                public void OnError(String errorMsg) {
                    onAllParameListener.onError(errorMsg);
                }
            }, "注册");
        } catch (Exception e) {
            e.printStackTrace();
            onAllParameListener.onError("系统不安全");
            return null;
        }
    }

    /**
     * 重置密码
     *
     * @param phoneNumber
     * @param password
     * @param captcha
     * @param onAllParameListener
     * @return
     */
    public static Call forgotpsw(String phoneNumber, String password, String captcha, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("requestType", "13");
            map.put("phoneNumber", phoneNumber);
            map.put("password", Utils.encrypt(context, FFMD5Util.getMD5String(password)));
            map.put("captcha", captcha);
            map.put("userIndex", "1");
            return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
                @Override
                public void OnSucc(String data) {
                    ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                    if (thePerfectGirl.isSuccess()) {
                        onAllParameListener.onSuccess(thePerfectGirl);
                    } else {
                        onAllParameListener.onError(thePerfectGirl.getErrmsg());
                    }
                }

                @Override
                public void OnError(String errorMsg) {
                    onAllParameListener.onError(errorMsg);
                }
            }, "重置密码");
        } catch (Exception e) {
            e.printStackTrace();
            onAllParameListener.onError("系统不安全");
            return null;
        }
    }

    /**
     * 免登录
     *
     * @param localToken
     * @param onAllParameListener
     * @return
     */
    public static Call authLocalToken(String localToken, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("loginToken", localToken);
        map.put("requestType", "14");
        map.put("userIndex", "1");

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {

                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    try {
                        AppPreferences preferences = new AppPreferences(context);
//                        preferences.put(StaticParams.FileKey.__USERPID__, thePerfectGirl.getData().getPid());
//                        preferences.put(StaticParams.FileKey.__USERNAME__, thePerfectGirl.getData().getUsername());
                        preferences.put(StaticParams.FileKey.__USERTYPE__, thePerfectGirl.getData().getUserType());
//                        preferences.put(StaticParams.FileKey.__REALNAME__, thePerfectGirl.getData().getRealName());
                        preferences.put(StaticParams.FileKey.__LEVEL__, thePerfectGirl.getData().getLevel());
                        preferences.put(StaticParams.FileKey.__IDENTIFY__, thePerfectGirl.getData().isIdentify() ? "true" : "false");
//                        preferences.put(StaticParams.FileKey.__LOCALTOKEN__, Utils.decrypt(context, thePerfectGirl.getData().getLocalToken()));
                        preferences.put(StaticParams.FileKey.__RONGTOKEN__, Utils.decrypt(context, thePerfectGirl.getData().getRongCloudToken()));
                        V.d("saved bound some data success");
                        onAllParameListener.onSuccess(thePerfectGirl);
                    } catch (Exception e) {
                        e.printStackTrace();
                        onAllParameListener.onError("系统不安全");
                    }
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "免登录");
    }

    /**
     * 获取新闻列表
     *
     * @param newstype
     * @param onAllParameListener
     * @return
     */
    public static Call getNewsList(String newstype, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "20");
        map.put("type", newstype);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取新闻列表");
    }

    /**
     * 获取首页动态列表
     *
     * @param page
     * @param address
     * @param lng
     * @param lat
     * @param userName
     * @param onAllParameListener
     * @return
     */
    public static Call getDynList(String page, String address, String lng, String lat, String userName, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "512");
        map.put("rows", StaticParams.ROWS);
        map.put("page", page);
        map.put("address", address);
        map.put("lng", lng);
        map.put("lat", lat);
        if (!TextUtils.isEmpty(userName)) {
            map.put("username", userName);
        }

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取动态列表");
    }

    /**
     * 获取新闻详情页面
     *
     * @param newsId
     * @param onAllParameListener
     * @return
     */
    public static Call getNewsDetails(String newsId, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("informationPid", newsId);
        map.put("requestType", "21");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取新闻实体");
    }

    /**
     * 发布动态
     *
     * @param address
     * @param content
     * @param lat
     * @param lng
     * @param pictrueid
     * @param onAllParameListener
     * @return
     */
    public static Call sendDyn(String address, String content, String lat, String lng, String pictrueid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("pictrueid", pictrueid);
        map.put("content", content);
        map.put("address", address);
        map.put("lng", lng);
        map.put("lat", lat);
        map.put("requestType", "511");

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "发布动态");
    }

    /**
     * 获取动态详情
     *
     * @param dynamicPid
     * @param onAllParameListener
     * @return
     */
    public static Call getDynDetails(String dynamicPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "513");
        map.put("dynamicPid", dynamicPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取动态详情");
    }

    /**
     * @param dynPid
     * @param state               1 点赞
     * @param onAllParameListener
     * @return
     */
    public static Call getDynGoodOrBadPersonList(String dynPid, int state, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "515");
        map.put("dynamicPid", dynPid);
        map.put("state", String.valueOf(state));
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取动态详情页面点赞或差评的人列表");
    }

    /**
     * 对动态详情页面评论列表的点赞接口
     *
     * @param dynamicCommentPid
     * @param onAllParameListener
     * @return
     */
    public static Call toDynCommListGood(String dynamicCommentPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "520");
        map.put("dynamicCommentPid", dynamicCommentPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "对动态详情页面评论列表的点赞");
    }

    /**
     * 获取评论列表
     *
     * @param dynamicCommentPid
     * @param onAllParameListener
     * @return
     */
    public static Call getCommListItem_Resendlist(String dynamicCommentPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "519");
        map.put("dynamicCommentPid", dynamicCommentPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取动态详情页面的单条评论的回复列表");
    }

    /**
     * 发布评论
     *
     * @param dynamicCommentPid
     * @param content
     * @param userid
     * @param onAllParameListener
     * @return
     */
    public static Call sendReComm(String dynamicCommentPid, String content, String userid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "517");
        map.put("commentPid", dynamicCommentPid);
        map.put("detailContent", content);
        map.put("reUserPid", userid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "回复");
    }

    /**
     * 动态点赞差评接口
     *
     * @param dynamicPid
     * @param state
     * @param onAllParameListener
     * @return
     */
    public static Call toDynGoodOrBad(String dynamicPid, int state, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "514");
        map.put("dynamicPid", dynamicPid);
        map.put("state", String.valueOf(state));
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "评论点赞或者点差");
    }

    /**
     * 获取动态评论的列表
     *
     * @param onAllParameListener
     * @return
     */
    public static Call UUUUUUUUU(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "518");
//        map.put("page", String.valueOf(page));
//        map.put("rows", "5");
//        map.put("dynamicPid", dynPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取动态评论列表");
    }

    /**
     * 获取动态评论的列表
     *
     * @param dynPid
     * @param page
     * @param onAllParameListener
     * @return
     */
    public static Call getDynCommList(String dynPid, int page, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "518");
        map.put("page", String.valueOf(page));
        map.put("rows", "5");
        map.put("dynamicPid", dynPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取动态评论列表");
    }

    /**
     * 评论动态
     *
     * @param dynPid
     * @param msg
     * @param onAllParameListener
     * @return
     */
    public static Call toCommDyn(String dynPid, String msg, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("content", msg);
        map.put("dynamicPid", dynPid);
        map.put("requestType", "516");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "请求评论动态");
    }

    /**
     * 获取工作列表    job页面传 0   赚点闲钱页面传1
     *
     * @return
     */
    public static Call getWorkList(String type, int page, String address, String jobName, final OnAllParameListener onAllParameListener, String... str) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "31");
        map.put("type", type);
        map.put("page", String.valueOf(page));
        if (type.equals("0")) {
            map.put("sortType", "1");
        } else {
            if (str != null && str.length > 0 && !TextUtils.isEmpty(str[0])) {
                map.put("time", str[0]);
            }
        }
        map.put("rows", StaticParams.ROWS);
        map.put("address", address == null ? "西安市" : address);

        if (!TextUtils.isEmpty(jobName)) {
            map.put("title", jobName);
        }
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取type=" + type + "的工作列表");
    }

    public static Call getWorkList(String requestType, String page, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        map.put("page", page);
        map.put("rows", "5");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取工作列表");
    }

    /**
     * 获取动态详情
     *
     * @param workPid
     * @param onAllParameListener
     * @return
     */
    public static Call getWorkDetails(String workPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "32");
        map.put("workPid", workPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取工作详情");
    }

    /**
     * 获取职位类型
     *
     * @param onAllParameListener
     * @return
     */
    public static Call getPosstionTypeList(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "02");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取职位类型");
    }

    /**
     * 发布长期工作
     *
     * @param userInputModel
     * @param positionNumber
     * @param onAllParameListener
     * @return
     */
    public static Call sendJob(UserInputModel userInputModel, String positionNumber, final OnAllParameListener onAllParameListener) {

        Map<String, String> map = new HashMap<>();
        String[] params = userInputModel.getParams();
        map.put("requestType", "30");
        map.put("type", "0");
        map.put("title", params[0]);
        map.put("positionType", positionNumber);
        map.put("salary", params[2].split("/")[0]);
        int num = -1;
        switch (params[2].split("/")[1]) {
            case "次":
                num = 0;
                break;
            case "时":
                num = 1;
                break;
            case "日":
                num = 2;
                break;
            case "月":
                num = 3;
                break;
            case "年":
                num = 4;
                break;
        }
        map.put("workPayment", String.valueOf(num));
        String number_sex = null;
        switch (params[3]) {
            case "不限":
                number_sex = "3";
                break;
            case "男":
                number_sex = "1";
                break;
            case "女":
                number_sex = "0";
                break;
        }

        map.put("sex", number_sex);
        String number_ex = null;
        switch (params[4]) {
            case "不限":
                number_ex = "0";
                break;
            case "应届生":
                number_ex = "1";
                break;
            case "1年以内":
                number_ex = "2";
                break;
            case "1~3年":
                number_ex = "3";
                break;
            case "3~5年":
                number_ex = "4";
                break;
            case "5~10年":
                number_ex = "5";
                break;
            case "10年以上":
                number_ex = "6";
                break;
        }
        map.put("experience", number_ex);

        String number = null;
        switch (params[5]) {
            case "不限":
                number = "0";
                break;
            case "大专":
                number = "1";
                break;
            case "本科":
                number = "2";
                break;
            case "硕士":
                number = "3";
                break;
            case "博士":
                number = "4";
                break;
        }
        map.put("education", number);


//        String number_age = null;
//        switch (params[6]) {
//            case "不限":
//                number_age = "0";
//                break;
//            case "18～22岁":
//                number_age = "1";
//                break;
//            case "23～26岁":
//                number_age = "2";
//                break;
//            case "27～35岁":
//                number_age = "3";
//                break;
//            case "35岁以上":
//                number_age = "4";
//                break;
//        }
        map.put("age", params[6]);

        map.put("skill", params[7]);
        map.put("number", params[8]);
        map.put("address", params[9]);
        map.put("content", params[10]);
        map.put("workHours", params[11]);
        map.put("contactPhone", params[12]);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "发布长期工作");
    }

    /**
     * 生成长期工作支付订单
     *
     * @param requestType
     * @param payType             支付宝／微信
     * @param workPid             workpid
     * @param amount              金额  单位元
     * @param orderTitle          标题
     * @param orderBody           支付内容说明
     * @param token               传入的购买令牌前的参数
     * @param onAllParameListener
     * @return
     */
    public static Call getOrder(String requestType, PayType payType, String workPid, String amount, String orderTitle, String orderBody, Token token, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        map.put("payment", payType == PayType.WeiXin ? "2" : "1");
        map.put("workPid", workPid);
        map.put("amount", AppConfig.TestAmount == null ? amount : AppConfig.TestAmount);
        map.put("orderTitle", orderTitle);
        map.put("orderBody", orderBody);
        map.put("levelOne", TextUtils.isEmpty(token.getLv1()) ? "0" : token.getLv1());
        map.put("levelTwo", TextUtils.isEmpty(token.getLv2()) ? "0" : token.getLv2());
        map.put("levelThree", TextUtils.isEmpty(token.getLv3()) ? "0" : token.getLv3());
        map.put("levelFour", TextUtils.isEmpty(token.getLv4()) ? "0" : token.getLv4());
        map.put("levelFive", TextUtils.isEmpty(token.getLv5()) ? "0" : token.getLv5());
        map.put("levelSix", TextUtils.isEmpty(token.getLv6()) ? "0" : token.getLv6());
        map.put("count", TextUtils.isEmpty(token.getCount()) ? "0" : token.getCount());

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "请求生成工作订单");
    }

    public static Call getOrderSer(String requestType, PayType payType, String serPid, String orderTitle, String orderBody, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);

        map.put("payment", payType == PayType.WeiXin ? "2" : "1");
        map.put("serverPid", serPid);
        map.put("amount", "0.01");
        map.put("orderTitle", orderTitle);
        map.put("orderBody", orderBody);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "请求生成服务订单");
    }

    /**
     * 获取支付状态
     *
     * @return
     */
    public static Call getPayStateFromService(PayType payType, String outTradeNo, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        switch (payType) {
            case WeiXin:
                map.put("requestType", "06");
                map.put("tradeNo", outTradeNo);
                break;

            case Aliapay:
                map.put("requestType", "05");
                map.put("outTradeNo", outTradeNo);
                break;
        }

        return ZokHttp.getInstance(true).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        });
    }

    /**
     * 分享／接令
     *
     * @param type                0-接令   1分享
     * @param workPid
     * @param onAllParameListener
     * @return
     */
    public static Call getJobToken_1(String type, String workPid, final OnAllParameListener onAllParameListener) {
        //接 315   workPid                 分享  313 ·workPid
        String requestType = "";
        switch (type) {
            case "0":
                requestType = "315";
                break;

            case "1":
                requestType = "313";
                break;
        }

        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        map.put("workPid", workPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "分享／接令");

    }

    /**
     * 获取job详情页面
     *
     * @param workPid
     * @param onAllParameListener
     * @return
     */
    public static Call getJobDetails(String workPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("workPid", workPid);
        map.put("requestType", "32");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取工作详情");
    }

    /**
     * 对job详情页面的单个工作的评论接口
     *
     * @return
     */
    public static Call sendJobDetailsComm(String workPid, String content, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "36");
        map.put("workPid", workPid);
        map.put("content", content);
//        map.put("parentPid", parentPid);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "发起评论");
    }

    /**
     * 对job详情页面的单个工作的评论的回复
     *
     * @return
     */
    public static Call sendJobDetailsComm(String workPid, String observerPid, String questionPid, String content, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "36");
        map.put("workPid", workPid);
        map.put("content", content);
        map.put("parentPid", questionPid);
//        map.put("questionPid", questionPid);


        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "发起评论");
    }

    /**
     * 获取job详情页面评论列表
     *
     * @param workPid
     * @param onAllParameListener
     * @return
     */
    public static Call getWorkCommList(String workPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("workPid", workPid);
        map.put("requestType", "37");

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取点评列表");
    }

    public static Call comMent(String requestType, String serverPid, String argued, String level, String content, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        map.put("serverRelationPid", serverPid);
        map.put("argued", argued);

        map.put("level", level);
        map.put("content", content);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "评论");
    }

    public static Call ServerRefund(String pid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "67");
        map.put("serverRelationPid", pid);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "服务退款");
    }


    public static Call WorkRefund(String employeePid, String pid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "310");
        map.put("workPid", pid);
        map.put("employeePid", employeePid);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "工作退款");
    }


    public static Call onOrOffServer(String serpid, String state, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "62");
        map.put("state", state);
        map.put("serverPid", serpid);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "关闭开启服务");
    }


    //type:0是微信,1是支付,2是银行卡
    public static Call boundAlipay(String account, String name, String type, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "150");
        map.put("account", account);
        map.put("name", name);
        map.put("type", type);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "绑定支付宝");
    }

    //type:0是微信,1是支付,2是银行卡
    public static Call Withdrawals(String money, String account, String type, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "147");
        map.put("account", account);
        map.put("money", money);
        map.put("type", type);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "提现");
    }


    public static Call getNewNum(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "41");

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取消息数量");
    }


    public static Call getTongzhiList(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "42");

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取消息通知详情");
    }

    public static Call comMent(String requestType, String serverPid, String level, String content, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        map.put("serverRelationPid", serverPid);

        map.put("level", level);
        map.put("content", content);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "评论");
    }

    public static Call toComm(String workPid, String content, String level, String argued, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("workPid", workPid);
        map.put("content", content);
        map.put("level", level);
        map.put("argued", argued);
        map.put("requestType", "311");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "评价工作");
    }


    public static Call getServiceList(String requestType, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取List里面的服务列表");
    }

    public static Call getServiceDetails(String serverPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "63");
        map.put("serverPid", serverPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取服务详情");
    }

    public static Call getWorkList1(String requestType, String page, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("isApply", "true");
        map.put("page", String.valueOf(page));
        map.put("rows", "5");
        map.put("applaySate", "1");
        map.put("requestType", requestType);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取工作列表");
    }


    public static Call getGrabForm(String workPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("workPid", workPid);
        map.put("isApply", "flase");
        map.put("requestType", "34");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "申请工作");
    }


    public static Call getAllNumber(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "43");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取我的交易首页标签数字");
    }

    public static Call checkServer(String requestType, String serverPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        map.put("serverRelationPid", serverPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "验收服务");
    }


    public static Call toAdmitted(String employeePid, String workPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "38");
        map.put("employeePid", employeePid);
        map.put("workPid", workPid);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "录用");
    }

    public static Call CheckWork(String workPid, String employeePid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("workPid", workPid);
        map.put("employeePid", employeePid);
        map.put("requestType", "39");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "验收工作");
    }


    public static Call getServiceList1(String requestType, String serviceType, String userPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        if (userPid != null) {
            map.put("userPid", userPid);
        }
        map.put("serviceType", serviceType);
        map.put("requestType", requestType);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取服务列表");
    }

    public static Call sendService(String title, String content, String money, String pictrueId, String serverType, String serverPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("content", content);
        if (!money.equals("null")) {
            map.put("money", money);
        }
        map.put("payment", "0");
        if (pictrueId != null)
            map.put("pictrueId", pictrueId);
        if (serverType != null) {
            map.put("serviceType", serverType);
        }
        if (serverPid != null) {
            map.put("serverPid", serverPid);
        }
        map.put("requestType", "20");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "发布服务");
    }

    public static Call getBalance(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "316");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取钱包数据");
    }


    public static Call getWork(String workPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("workPid", workPid);
        map.put("requestType", "34");
        map.put("isApply", "true");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "申请工作");
    }

    public static Call getResumeList(String userPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("userPid", userPid);
        map.put("requestType", "423");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取简历");
    }

    public static Call saveResume(String pic, String name, String Sex, String height, String age, String address, String education, String major, String school,
                                  String educationExperience, String industryDirection, String experience, String skill, String mine, final OnAllParameListener onAllParameListener) {

        Map<String, String> map = new HashMap<>();
        map.put("requestType", "424");
        map.put("name", name);
        map.put("sex", Sex);
        map.put("height", height);
        map.put("age", age);
        map.put("address", address);
        if (pic != null) {
            map.put("portraitId", pic);

        }
        map.put("education", education);
        map.put("major", major);
        map.put("school", school);
        map.put("educationExperience", educationExperience);
        map.put("industryDirection", industryDirection);
        map.put("experience", experience);
        map.put("skill", skill);
        map.put("personalEvaluation", mine);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "保存个人简历");
    }


    public static Call saveResume1(String pic, String name, String address, String scale, String organizationCode, String website, String mainBusiness, final OnAllParameListener onAllParameListener) {

        Map<String, String> map = new HashMap<>();
        map.put("requestType", "424");
        if (!TextUtils.isEmpty(pic))
            map.put("businessLicense", pic);
        map.put("name", name);
        map.put("address", address);
        map.put("scale", scale);
        map.put("organizationCode", organizationCode);
        map.put("website", website);
        map.put("mainBusiness", mainBusiness);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "保存公司简历");
    }

    public static Call getUserHomeInfos(String userPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        if (userPid != null) {
            map.put("userPid", userPid);
        }
        map.put("requestType", "40");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "加载用户个人中心首页数据");
    }


    public static Call getUserNameAndUserLogo(String userid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "145");
        map.put("userPids", userid);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取用户头像和昵称");
    }


    public static Call getDynamic(String userid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "139");
        map.put("userPid", userid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取动态列表");
    }

    public static Call Cancellation(String userPid, String token, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "12");
        map.put("userPid", userPid);
        map.put("token", token);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "注销");
    }


    public static Call getNewsDynamic(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "135");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取消息动态列表");
    }

    public static Call getNewsDynamicGood(String request, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", request);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取消息动态点赞列表");
    }


    public static Call getMyToken(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "317");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取悬赏令数量");
    }

    public static Call WorkTokenBack(String worktokenPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "314");
        map.put("workTokenPid", worktokenPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "悬赏令退现");
    }


    public static Call WorkTokenList(String worktokenPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "318");
        map.put("workTokenPid", worktokenPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "悬赏令发令列表");
    }

    public static Call soldWorkToken(String userPid, String workPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "319");
        map.put("userPid", userPid);
        map.put("workPid", workPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "悬赏令发令");
    }

    public static Call getBillDetailed(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "129");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取账单状态");
    }

    public static Call getMoneyRecordList(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "704");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取账单流水");
    }

    public static Call getFriendTalk(String requestType, String type, String userPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        map.put("type", type);
        if (userPid != null) {
            map.put("userPid", userPid);
        }
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "请求朋友印象");
    }

    public static Call getHistoryComm(String requestType, String userPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        map.put("userPid", userPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取以往工作评价");
    }

    public static Call getWorkList2(String requestType, String Workpid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", requestType);
        map.put("workPid", Workpid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取工作列表");
    }

    public static Call AddFriendTalk(String content, String envaluationuser, String type, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "132");
        map.put("content", content);
        map.put("evaluationuser", envaluationuser);
        map.put("type", type);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "添加印象");
    }

    public static Call updataUserInfo(String realName, String sex, String commpany, String position, String address, String jobstate, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("realName", realName);

        map.put("sex", sex.equals("女") ? "0" : "1");

        map.put("company", commpany);

        map.put("position", position);
        map.put("address", address);
        String st = "0";
        switch (jobstate) {
            case "寻找新机会":
                st = "2";
                break;
            case "在职":
                st = "1";
                break;
            case "求职":
                st = "0";
                break;

        }
        map.put("jobState", st);

        map.put("requestType", "415");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "更新用户资料");
    }


    public static Call upDataUserLogo(String portraitId, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("portraitId", portraitId);
        map.put("requestType", "414");

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "上传用户头像");
    }

    public static Call getUserDetails(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "413");
        return ZokHttp.getInstance(AppConfig.OutTime).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取用户实体");
    }


    public static Call uploadUserBg(String portraitId, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("background", portraitId);
        map.put("requestType", "140");

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "上传用户背景");
    }

    /**
     * 发布创意工作 生成支付订单并发布工作   会返回uploadToken 和支付订单  在返回后开始上传图片并发起交易
     *
     * @param userInputModel      发布工作界面传入的用户输入的信息
     * @param payType             用户选择的支付方式
     * @param amount              支付的金额
     * @param orderTitle          支付的标题说明
     * @param orderBody           支付信息说明
     * @param onAllParameListener 生成订单的状态监听
     * @return
     */
    public static Call sendOriWork(UserInputModel userInputModel, PayType payType, String amount, String orderTitle, String orderBody, final OnAllParameListener onAllParameListener) {
//        for (int i = 0; i < userInputModel.getParams().length; i++) {
//            V.d(i + ":" + userInputModel.getParams()[i]);
//        }
        String[] strs = userInputModel.getParams();
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "30");
        map.put("type", "1");
//
        map.put("title", strs[0]);
        if (!TextUtils.isEmpty(strs[5])) {
            map.put("content", strs[5]);
        }

        map.put("address", strs[1]);
        map.put("salary", strs[2]);
        map.put("workPayment", "0");//创意工作限制为日  0 ci   3月  2日
        if (!TextUtils.isEmpty(strs[6])) {
            map.put("contact", strs[6]);
        }

        map.put("contactPhone", strs[3]);
        map.put("deadline", strs[4]);
//        String[] imgs = strs[7].split("\\|");
//        StringBuffer stringBuffer = new StringBuffer();
//        for (int i = 0; i < imgs.length; i++) {
//            stringBuffer.append(imgs[i].replace("/", "lovejob")).append("|");
//        }
        map.put("pictrueId", userInputModel.getParams()[7]);
//
        map.put("payment", payType == PayType.WeiXin ? "2" : "1");
//        map.put("workPid", workPid);
        map.put("amount", AppConfig.TestAmount == null ? amount : AppConfig.TestAmount);
        map.put("orderTitle", orderTitle);
        map.put("orderBody", orderBody);
//
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "发布创意工作");

    }

    public static Call sendMoneyWork_park(UserInputModel userInputModel
            , PayType payType, String amount, String orderTitle, String orderBody,
                                          final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        String[] params = userInputModel.getParams();
        int number = -1;
        switch (params[10]) {
            case "不限":
                number = 0;
                break;
            case "大专":
                number = 1;
                break;
            case "本科":
                number = 2;
                break;
            case "硕士":
                number = 3;
                break;
            case "博士":
                number = 4;
                break;

        }

        String number_sex = null;
        switch (params[2]) {
            case "不限":
                number_sex = "3";
                break;
            case "男":
                number_sex = "1";
                break;
            case "女":
                number_sex = "0";
                break;
        }

        String number_ex = null;
        switch (params[9]) {
            case "不限":
                number_ex = "0";
                break;
            case "应届生":
                number_ex = "1";
                break;
            case "1年以内":
                number_ex = "2";
                break;
            case "1-3年":
                number_ex = "3";
                break;
            case "3-5年":
                number_ex = "4";
                break;
            case "5-10年":
                number_ex = "5";
                break;
            case "10年以上":
                number_ex = "6";
                break;

        }
        //职位类型未传
        map.put("requestType", "30");
        map.put("type", "2");
        map.put("title", params[0]);
        map.put("positionType", params[13]);
        map.put("experience", number_ex);
        map.put("sex", number_sex);
        map.put("education", String.valueOf(number));
        map.put("skill", params[11]);
        map.put("height", params[12].substring(0, params[12].length() - 2));
        map.put("content", params[7]);
        map.put("address", params[3]);
        map.put("number", params[4]);
        map.put("salary", params[5]);
        map.put("deadline", params[8]);
        map.put("workPayment", "2");
        map.put("contactPhone", params[6]);

        map.put("payment", payType == PayType.WeiXin ? "2" : "1");
        map.put("amount", AppConfig.TestAmount == null ? amount : AppConfig.TestAmount);
        map.put("orderTitle", orderTitle);
        map.put("orderBody", orderBody);
        return ZokHttp.getInstance(AppConfig.OutTime).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "发布兼职工作");
    }


    public static Call getOriCommList(String workPid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "37");
        map.put("workPid", workPid);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取创意工作页面评论列表");
    }

    /**
     * 申请工作
     *
     * @param workPid
     * @param grab    报名传true  取消传fasle
     * @return
     */
    public static Call getGrabForm(String workPid, boolean grab, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "34");
        map.put("workPid", workPid);
        map.put("isApply", grab ? "true" : "false");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "申请／取消工作");
    }

    /**
     * 对创意工作的评论／回复接口
     *
     * @param workPid             要评论的workpid
     * @param content             评论的内容
     * @param reQuestPid          该字段不为空时将为回复
     * @param onAllParameListener
     * @return
     */
    public static Call sendOriWorkComm(String workPid, String reQuestPid, String content, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "36");

        map.put("workPid", workPid);
        if (reQuestPid != null) {
            map.put("parentPid", reQuestPid);
        }
        map.put("content", content);
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "评论／回复");
    }


    public static Call boundQQOrWeChat(String openId, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "133");
        map.put("openId", openId);
        return ZokHttp.getInstance(AppConfig.OutTime).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "绑定QQ微信（APP内）");
    }

    public static Call getSettingInfos(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "151");
        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取设置页面数据");
    }

    public static Call CancelBill(String type, String outTradeNo, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "148");
        map.put("type", type);
        map.put("outTradeNo", outTradeNo);
        return ZokHttp.getInstance(AppConfig.OutTime).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "取消账单）");
    }

    public static Call uploadCrachFile() {
        Map<String, String> map = new HashMap<>();
        final AppPreferences appPreferences = new AppPreferences(context);
        map.put("requestType", "666");
        map.put("exceptionDec", appPreferences.getString(StaticParams.FileKey.__CrashFile__, ""));
        map.put("type", "1");
        map.put("devicePid", appPreferences.getString(StaticParams.FileKey.__CrashDeviceType__, ""));

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    appPreferences.put(StaticParams.FileKey.__CrashFile__, "");
                    appPreferences.put(StaticParams.FileKey.__CrashDeviceType__, "");
                }
            }

            @Override
            public void OnError(String errorMsg) {

            }
        }, "上传用户奔溃日志");
    }

    public static Call KeepSuggest(String content, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "701");
        map.put("state", "1");
        map.put("content", content);
        map.put("phone", "18220781390");

        return ZokHttp.getInstance(AppConfig.OutTime).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "保存反馈意见");
    }

    public static Call getSuggest(final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "702");


        return ZokHttp.getInstance(AppConfig.OutTime).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "获取反馈意见");
    }


    public static Call BuyFreeServer(String pid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "61");
        map.put("serverPid", pid);


        return ZokHttp.getInstance(AppConfig.OutTime).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "购买免费服务");
    }


    public static Call repay(String outTradeNo, PayType payType, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "143");

        map.put("payment", payType == PayType.WeiXin ? "2" : "1");
        map.put("outTradeNo", outTradeNo);

        return ZokHttp.getInstance(20).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "请求重新付款");
    }

    public static Call CancelDynamic(String pid, final OnAllParameListener onAllParameListener) {
        Map<String, String> map = new HashMap<>();
        map.put("requestType", "521");
        map.put("dynamicPid", pid);


        return ZokHttp.getInstance(AppConfig.OutTime).doPost(StaticParams.URL, map, new OnZokHttpResponse() {
            @Override
            public void OnSucc(String data) {
                ThePerfectGirl thePerfectGirl = new Gson().fromJson(data, ThePerfectGirl.class);
                if (thePerfectGirl.isSuccess()) {
                    onAllParameListener.onSuccess(thePerfectGirl);
                } else {
                    onAllParameListener.onError(thePerfectGirl.getErrmsg());
                }
            }

            @Override
            public void OnError(String errorMsg) {
                onAllParameListener.onError(errorMsg);
            }
        }, "删除动态");
    }
//
//
//    public static Call checkY(String url, final CheckResponse checkResponse){
//      return   ZokHttp.getInstance().doGet(url+"?pulp", new OnZokHttpResponse() {
//            @Override
//            public void OnSucc(String data) {
//                checkResponse.onSucc();
//            }
//
//            @Override
//            public void OnError(String errorMsg) {
//                checkResponse.onError(errorMsg);
//            }
//        });
//    }

}
