package com.v.rapiddev.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.v.rapiddev.http.okhttp3.FormBody;
import com.v.rapiddev.security.FF3DESUtils;
import com.v.rapiddev.security.onGetMac;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:RapidDevSample
 * Package_Name:com.v.rapiddev.utils
 * Created on 2016-11-24 10:29
 */

public class Utils_RapidDev {
    public static String appendParameter(String url, Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(url).append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        return stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
    }


    public static FormBody.Builder addParam(Map<String, String> map) {
        FormBody.Builder param = new FormBody.Builder();
        Set keySet = map.keySet(); // key的set集合
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            Object k = it.next(); // key
            Object v = map.get(k);  //value
            param.add(String.valueOf(k), String.valueOf(v));
        }
        return param;
    }


    public static Bitmap getBitmapFromPath(String path) {

        if (!new File(path).exists()) {
            System.err.println("getBitmapFromPath: file not exists");
            return null;
        }
        // Bitmap bitmap = Bitmap.createBitmap(1366, 768, Config.ARGB_8888);
        // Canvas canvas = new Canvas(bitmap);
        // Movie movie = Movie.decodeFile(path);
        // movie.draw(canvas, 0, 0);
        //
        // return bitmap;

        byte[] buf = new byte[1024 * 1024];// 1M
        Bitmap bitmap = null;

        try {

            FileInputStream fis = new FileInputStream(path);
            int len = fis.read(buf, 0, buf.length);
            bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
            if (bitmap == null) {
                System.out.println("len= " + len);
                System.err
                        .println("path: " + path + "  could not be decode!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return bitmap;
    }

    /**
     * ASCII 转换工具
     *
     * @param s
     * @return
     */
    public static String toASCIIString(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 传入Double 获取12位交易金额，不足位数前补零
     *
     * @param mmmmm 传入金额
     * @return 返回12位doubble金额
     */
    public static String doubleToAmount(Double mmmmm) {
        StringBuffer sb = new StringBuffer();
        String amountStr = String.valueOf((int) (mmmmm.doubleValue() * 100));
        for (int i = 0; i < 12 - amountStr.length(); ++i) {
            sb.append("0");
        }
        sb.append(amountStr);
        return sb.toString();
    }


    /**
     * 集合排序，拼接Mac时用到
     *
     * @param map 传入Map
     * @return 排序后的Map字符串
     */
    public static String sort_Map(Map<String, String> map) {
        String[] keys = (String[]) map.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder raw = new StringBuilder();
        String[] var7 = keys;
        int var6 = keys.length;

        String con;
        for (int var5 = 0; var5 < var6; ++var5) {
            con = var7[var5];
            raw.append(con).append("=").append((String) map.get(con)).append("&");
        }

        con = raw.deleteCharAt(raw.length() - 1).toString();
        return con;
    }


    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getSizeToFilePath(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap getHaveStatusScreenCapture(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap getNoHaveStatusScreenCapture(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }


    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean internetIsConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断是否是wifi连接
     */
    public static boolean isInternetConnectTypeWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }


    /**
     * 打开网络设置界面
     */
    public static void openNetworkSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }


    /**
     * 判断是否符合邮箱格式
     */
    public static boolean checkEmailValid(String strEmail) {
        if (null == strEmail) {
            return false;
        }
        return strEmail.matches("[a-zA-Z0-9_]+@[a-z0-9]+(.[a-z]+){2}");
    }


    /**
     * 验证手机号方法
     *
     * @param strPhoneNum
     * @return
     */
    public static boolean checkMobileNumberValid(String strPhoneNum) {
        if (null == strPhoneNum) {
            return false;
        }
        /**
         * 匹配13、15、18开头手机号 排除154 开头手机号
         * 匹配170、176、177、178开头手机号
         * 匹配规则参考当前（2015-04-29）百度百科“手机号”罗列号码
         */
        String checkphone = "(^(((13|18)[0-9])|(15[^4,\\D])|170|176|177|178)\\d{8}$)";
        return strPhoneNum.toString().matches(checkphone);

    }


    //转换中文对应的时段
    public static String convertMorning(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hourString = sdf.format(date);
        int hour = Integer.parseInt(hourString);
        if (hour < 6) {
            return "凌晨";
        } else if (hour >= 6 && hour < 12) {
            return "早上";
        } else if (hour == 12) {
            return "中午";
        } else if (hour > 12 && hour <= 18) {
            return "下午";
        } else if (hour >= 19) {
            return "晚上";
        }
        return "";
    }


    /**
     * 传入秒数 换算出如下格式的时间
     * 剩余秒数：18天12时43分40秒
     *
     * @param time
     * @return
     */
    public static String convertDays(int time) {
        int day = time / 86400;
        int hour = (time - 86400 * day) / 3600;
        int min = (time - 86400 * day - 3600 * hour) / 60;
        int sec = (time - 86400 * day - 3600 * hour - 60 * min);
        StringBuilder sb = new StringBuilder();
        sb.append(day);
        sb.append("天");
        sb.append(hour);
        sb.append("时");
        sb.append(min);
        sb.append("分");
        sb.append(sec);
        sb.append("秒");
        return sb.toString();
    }

    /**
     * 获取网路连接类型
     *
     * @param context 上下文
     * @return 网络类型
     * 需要添加权限<uses-permission android:name="android.permission.INTERNET"/>
     * 需要添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     */
    public static String getNetworkType(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        String result = null;
        if (info != null && info.isAvailable()) {
            if (info.isConnected()) {
                int type = info.getType();
                String typeName = info.getTypeName();
                switch (type) {
                    case ConnectivityManager.TYPE_BLUETOOTH:
                        result = "蓝牙连接   :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_DUMMY:
                        result = "虚拟数据连接    :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_ETHERNET:
                        result = "以太网数据连接    :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        result = "移动数据连接   : " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE_DUN:
                        result = "网络桥接 :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE_HIPRI:
                        result = "高优先级的移动数据连接 :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE_MMS:
                        result = "运营商的多媒体消息服务  : " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE_SUPL:
                        result = "平面定位特定移动数据连接  :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        result = "Wifi数据连接   : " + typeName;
                        break;
                    case ConnectivityManager.TYPE_WIMAX:
                        result = "全球微波互联   : " + typeName;
                        break;
                    default:
                        break;
                }
            }
        }
        return result;
    }


    /**
     * 从资源文件中获取bitmap
     *
     * @param context
     * @param id
     * @return
     */
    public static Bitmap getBitmapFromResources(Context context, int id) {
        Bitmap bmp = null;
        Resources res = context.getResources();
        bmp = BitmapFactory.decodeResource(res, id);

        return bmp;
    }


    /**
     * bitmap 转换byte数组
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    /**
     * byte 转换bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


//    public static void main(String[] args) {
////        System.out.print("toASCIIString:" + toASCIIString("123456"));
////        System.out.print("toASCIIString:" + doubleToAmount(12.5));
//        System.out.print("toASCIIString:" + isSDCardEnable());
//    }


    /**
     * 获取IMEI号，IESI号，手机型号
     */
    public static List<String> getPhoneInfo(Context context) {
        List<String> list = new ArrayList<>();
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL; // 手机型号
        String mtyb = android.os.Build.BRAND;//手机品牌
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        V.d("手机IMEI号：" + imei + "手机IESI号：" + imsi + "手机型号：" + mtype + "手机品牌：" + mtyb + "手机号码" + numer);
        list.add(imei);
        list.add(imsi);
        list.add(mtype);
        list.add(mtyb);
        list.add(numer);

        return list;
    }

    /**
     * 获取手机MAC地址
     *
     * @param context
     * @return
     */
    private static String getMacAddress(Context context) {
        String result = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        V.d("手机macAdd:" + result);
        return result;
    }

    /**
     * 获取手机服务商信息
     */
    public static String getProvidersName(Context context) {
        String ProvidersName = "N/A";
        String IMSI;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            System.out.println(IMSI);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
        } catch (Exception e) {
            e.printStackTrace();
            ProvidersName = "未知";
        }
        return ProvidersName;
    }

    /**
     * 根据一个网络连接(String)获取bitmap图像
     * （图片数量巨大时可使用util包下的FFImageLoad框架）
     *
     * @param imageUri
     * @return
     */
    public static Bitmap getbitmapFromURL(String imageUri) {
        V.d("getbitmap:" + imageUri);
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            V.d("image download finished." + imageUri);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            V.d("getbitmap bmp fail---");
            bitmap = null;
        }
        return bitmap;
    }


    /**
     * 将16进制字符串转为byte【】
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexStringToByteArray(String hexStr) {
        if (hexStr == null) {
            return null;
        } else if (hexStr.length() % 2 != 0) {
            return null;
        } else {
            byte[] data = new byte[hexStr.length() / 2];

            for (int i = 0; i < hexStr.length() / 2; ++i) {
                char hc = hexStr.charAt(2 * i);
                char lc = hexStr.charAt(2 * i + 1);
                byte hb = hexChar2Byte(hc);
                byte lb = hexChar2Byte(lc);
                if (hb < 0 || lb < 0) {
                    return null;
                }

                int n = hb << 4;
                data[i] = (byte) (n + lb);
            }

            return data;
        }
    }

    public static byte hexChar2Byte(char c) {
        return c >= 48 && c <= 57 ? (byte) (c - 48) : (c >= 97 && c <= 102 ? (byte) (c - 97 + 10) : (c >= 65 && c <= 70 ? (byte) (c - 65 + 10) : -1));
    }

    /**
     * 将字节数组转换成十六进制字符串
     */
    public static String byteArratToHexString(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return toHexString(data, 0, data.length);
    }

    /**
     * 将字节数组中的指定字节转换成十六进制字符串
     */
    public static String toHexString(byte[] data, int offset, int length) {
        if (data == null || data.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(toHexString(data[offset + i]));
        }
        return sb.toString();
    }

    /**
     * 十六进制输出byte，每个byte两个字符，不足时前补0
     */
    public static final String toHexString(byte b) {
        String s = Integer.toHexString(b & 0xFF).toUpperCase();
        if (s.length() == 1) {
            s = "0" + s; // 补齐成两个字符
        }
        return s;
    }

    /**
     * @param TMK      终端主密钥明文 hexString 16bytes
     * @param MacKey_e MacKey密文 hexString 16bytes
     * @param macCv    MacKey校验值  hexString  8bytes
     * @param data     参与计算的数据  byte[]
     * @param onGetMac 计算结果回调
     */
    public static final void getMac(String TMK, String MacKey_e, String macCv, byte[] data, onGetMac onGetMac) {
        if (FF3DESUtils.setKeyInfo("DESede", 168, "DESede/ECB/NoPadding")) {
            try {
//                    byte[] key = FF3DESUtils.initKey();
                byte[] mackey_d = getMacKey(TMK, MacKey_e, macCv);
                if (mackey_d == null) {
                    onGetMac.onError("还原Mackey失败");
                    return;
                }

//                byte[] key = FFUtils.hexStringToByteArray("21831D0E98FC56F6838F7569B8B7587E");
                V.d("当前使用的秘钥为（mackey）：" + byteArratToHexString(mackey_d));
//                String da = "991a9a2edd89e241614a3e155f6eaef7";
                V.d("参与加密的数据：" + data);
                byte[] encryptdata = FF3DESUtils.encrypt(mackey_d, data);
                onGetMac.onSucc(byteArratToHexString(encryptdata));
//                V.d("加密后：" + FFUtils.byteArratToHexString(encryptdata));
//                byte[] decryptdata = FF3DESUtils.decrypt(key, encryptdata);
//                V.d("解密后：" + new String(decryptdata));

            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
                V.e("加解密异常，" + e.getMessage());
                onGetMac.onError("Mac计算失败，" + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                onGetMac.onError("Mac计算失败，" + e.getMessage());
                V.e("加解密异常，" + e.getMessage());
            } catch (InvalidKeyException e) {
                V.e("加解密异常，" + e.getMessage());
                onGetMac.onError("Mac计算失败，" + e.getMessage());
                e.printStackTrace();
            } catch (BadPaddingException e) {
                V.e("加解密异常，" + e.getMessage());
                onGetMac.onError("Mac计算失败，" + e.getMessage());
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
                onGetMac.onError("Mac计算失败，" + e.getMessage());
                V.e("加解密异常，" + e.getMessage());
            }
        } else {
            V.d("参数设置失败");
            onGetMac.onError("计算模式等数据设置失败");
        }
    }

    private static byte[] getMacKey(String tmk, String MacKey_e, String macKeyCV) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        boolean isCheckMackeyOk = false;
        FF3DESUtils.setKeyInfo("DESede", 168, "DESede/ECB/NoPadding");
        byte[] tmk_byte = hexStringToByteArray(tmk);
        byte[] mackey = FF3DESUtils.decrypt(tmk_byte, hexStringToByteArray(MacKey_e));
        V.d("MacKey解密成功，Mackey：" + byteArratToHexString(mackey));
//        byte[] i = new byte[]{0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30};
        byte[] i = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        byte[] macKeyCv = FF3DESUtils.encrypt(mackey, i);
        V.d("计算得mackey校验值：" + byteArratToHexString(macKeyCv).substring(0, 16).toLowerCase());
        V.d("传入mackey校验值：" + macKeyCV);
        if (macKeyCV.equals(byteArratToHexString(macKeyCv).substring(0, 8).toLowerCase())) {
            isCheckMackeyOk = true;
        }
        return isCheckMackeyOk ? mackey : null;
    }

    /**
     * 获取指定view的截图(bug'不清晰)
     *
     * @param v
     * @return
     */
    public static Bitmap getBitmapFromView(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            V.e("failed getViewBitmap(" + v + ")");
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    public static Bitmap getSmallBitmapFromFile(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;

    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }


    /**
     * 获取拼音的首字母（大写）
     *
     * @param pinyin
     * @return
     */
    public static String getFirstLetter(final String pinyin) {
        if (TextUtils.isEmpty(pinyin)) return "定位";
        String c = pinyin.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()) {
            return c.toUpperCase();
        } else if ("0".equals(c)) {
            return "定位";
        } else if ("1".equals(c)) {
            return "热门";
        }
        return "定位";
    }

    /**
     * 提取出城市或者县
     *
     * @param city
     * @param district
     * @return
     */
    public static String extractLocation(final String city, final String district) {
        return district.contains("县") ? district.substring(0, district.length() - 1) : city.substring(0, city.length() - 1);
    }

}
