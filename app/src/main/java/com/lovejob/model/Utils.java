package com.lovejob.model;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.media.upload.UploadListener;
import com.alibaba.sdk.android.media.upload.UploadTask;
import com.alibaba.sdk.android.media.utils.FailReason;
import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.OnUpLoadImagesListener;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.v.rapiddev.dialogs.core.MaterialDialog;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.security.FFRSAUtils;
import com.v.rapiddev.utils.V;
import com.zwy.luban.Luban;
import com.zwy.luban.OnMultiCompressListener;

import net.bither.util.NativeUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static android.app.Activity.RESULT_OK;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob2
 * Package_Name:com.lovejob.modles.Utils
 * Created on 2016-11-21 13:13
 */

public class Utils {


    private static int uploadlenth = 0;//上传图片的数量  从0开始  当uploadlenth==传入的文件集合大小的时候为上传成功
    private static long l;

    /**
     * 获取新的图片名称  转换成类似：cxwl_6546578778578_lovejob_31037649902.jpg
     *
     * @param files                  需要处理的图片集合
     * @param context                上下文
     * @param isUpLoad               是否上传
     * @param onUpLoadImagesListener 回调监听   如果isUpLoad 为true时 该回调会在上传完成后执行   为false时 压缩完就执行
     */
    public static void ImageCo(final List<File> files, Context context, final boolean isUpLoad,
                               final OnUpLoadImagesListener onUpLoadImagesListener) {
        final String token = StaticParams.UpLoadImageToken;
        if (isUpLoad && TextUtils.isEmpty(token)) {
            throw new NullPointerException("当选择上传图片时token不能为空");
        }
        final String afterName = "cxwl";// 1
        final String and = "-";// 连接符//2  4
        final String lastName = "lovejob";// 5
        final String smallIndex = "SMALL";//小图在 6 后拼接该符号
        //生成大小图对应对象

        final List<ImageModle> imageModleList = new ArrayList<>();
//        int random = new Random().nextInt();//3
//        long timeThis = new Date().getTime();//6
//        for (int i = 0; i < files.size(); i++) {
//            String filePath = files.get(i).getAbsolutePath();
//            String lastPath = filePath.substring(filePath.lastIndexOf("."));//7
//            String bitImageName = afterName + and + random + and + lastName + timeThis + lastPath;//原图名称
//            //获取缩略图名称和文件
//            String smallImageName = afterName + and + random + and + lastName + timeThis + smallIndex + lastPath;//缩略图名称
//            File smallFile = null;//缩略图文件 压缩后的图片名称
//            imageModleList.add()
//        }


        Luban.get(context).load(files).putGear(Luban.THIRD_GEAR).launch(new OnMultiCompressListener() {
            @Override
            public void onStart() {
                l = new Date().getTime();
                V.d("开始压缩");
            }

            @Override
            public void onSuccess(List<File> fileList) {
                V.d("图片压缩完成,耗时：" + (new Date().getTime() - l) + " ms");
                for (int i = 0; i < fileList.size(); i++) {
                    String filePath = files.get(i).getAbsolutePath();
                    int random = new Random().nextInt();//3
                    long timeThis = new Date().getTime();//6
                    String lastPath = filePath.substring(filePath.lastIndexOf("."));//7
                    //获取原图名称和文件
                    String bitImageName = afterName + and + random + and + lastName + timeThis + lastPath;//原图名称
//                    File bigFile = files.get(i);//原图文件
                    File bigFile = fileList.get(i);//狸猫换太子


                    //获取缩略图名称和文件
                    String smallImageName = afterName + and + random + and + lastName + timeThis + smallIndex + lastPath;//缩略图名称
                    File smallFile = fileList.get(i);//缩略图文件 压缩后的图片名称
                    imageModleList.add(new ImageModle(bigFile, bitImageName, smallFile, smallImageName));
                }

                if (isUpLoad) {
                    V.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>开始上传大图文件>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    for (int i = 0; i < imageModleList.size(); i++) {
                        MyApplication.wantuService.upload(getBytes(imageModleList.get(i).getBigFile().getPath()), imageModleList.get(i).getBigFileName(), null, new UploadListener() {
                            @Override
                            public void onUploading(UploadTask uploadTask) {

                            }

                            @Override
                            public void onUploadFailed(UploadTask uploadTask, FailReason failReason) {
                                if (onUpLoadImagesListener!=null)onUpLoadImagesListener.onError();
                                return;
                            }

                            @Override
                            public void onUploadComplete(UploadTask uploadTask) {
                                //每上传一张 uploadlenth 自增一
                                uploadlenth++;
                                if (uploadlenth == imageModleList.size()) {
                                    uploadlenth = 0;//参数复位
                                    V.d("<<<<<<<<<<<<<<<<<<<<<<<<<<<<大图文件文件上传成功<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                                    V.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>开始上传缩略图文件>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                    for (int j = 0; j < imageModleList.size(); j++) {
                                        MyApplication.wantuService.upload(getBytes(imageModleList.get(j).getSmallFile().getPath()), imageModleList.get(j).getSmallFileName(),
                                                null, new UploadListener() {
                                                    @Override
                                                    public void onUploading(UploadTask uploadTask) {

                                                    }

                                                    @Override
                                                    public void onUploadFailed(UploadTask uploadTask, FailReason failReason) {
                                                        if (onUpLoadImagesListener!=null)  onUpLoadImagesListener.onError();
                                                        return;
                                                    }

                                                    @Override
                                                    public void onUploadComplete(UploadTask uploadTask) {
                                                        uploadlenth++;
                                                        if (uploadlenth == imageModleList.size()) {
                                                            uploadlenth = 0;
                                                            V.d("<<<<<<<<<<<<<<<<<<<<<<<<<<<<缩略图文件上传成功<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                                                            if (onUpLoadImagesListener!=null)  onUpLoadImagesListener.onSucc(imageModleList);
                                                        }
                                                    }

                                                    @Override
                                                    public void onUploadCancelled(UploadTask uploadTask) {

                                                    }
                                                }, token);
                                    }
                                }
                            }

                            @Override
                            public void onUploadCancelled(UploadTask uploadTask) {
                                if (onUpLoadImagesListener!=null)  onUpLoadImagesListener.onError();
                                return;
                            }
                        }, token);
                    }

                } else {
                    //不需上传时直接回调
                    if (onUpLoadImagesListener!=null)  onUpLoadImagesListener.onSucc(imageModleList);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (onUpLoadImagesListener!=null)  onUpLoadImagesListener.onError();
                V.e("压缩失败");
            }
        });
    }


    /**
     * 获得指定文件的byte数组
     */
    private static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static UserInputModel checkUserInputParams(View... view) {
        boolean isTrue = true;
        String[] params = new String[view.length];
        for (int i = 0; i < view.length; i++) {
            if (view[i] instanceof TextView) {
                isTrue = !TextUtils.isEmpty(((TextView) view[i]).getText());
                if (!TextUtils.isEmpty(((TextView) view[i]).getText())) {
                    params[i] = ((TextView) view[i]).getText().toString().trim();
                }
            }
            if (view[i] instanceof EditText) {
                isTrue = !TextUtils.isEmpty(((EditText) view[i]).getText());
                if (!TextUtils.isEmpty(((EditText) view[i]).getText())) {
                    params[i] = ((TextView) view[i]).getText().toString().trim();
                }
            }
            if (!isTrue) {
                break;
            }
        }
        return new UserInputModel(isTrue, params);
    }

    static Toast mToast;

    public static void showToast(final Activity context, final String msg) {
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(msg);
                    mToast.setDuration(Toast.LENGTH_SHORT);
                }
                mToast.show();
            }
        });
    }

    public static void showToast(final Context context, final String msg) {
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(msg);
                    mToast.setDuration(Toast.LENGTH_SHORT);
                }
                mToast.show();
            }
        });
    }

    public static void showToast(final Activity context, final int msg) {
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(msg);
                    mToast.setDuration(Toast.LENGTH_SHORT);
                }
                mToast.show();
            }
        });
    }

    /**
     * 判断密码强度
     *
     * @return Z = 字母 S = 数字 T = 特殊字符
     */
    public static String checkPassword(String passwordStr) {
        int GRADE_SCORE = -1;
        if (TextUtils.equals("", passwordStr)) {
            return "出现故障";
        }
        String regexZ = "\\d*";
        String regexS = "[a-zA-Z]+";
        String regexT = "\\W+$";
        String regexZT = "\\D*";
        String regexST = "[\\d\\W]*";
        String regexZS = "\\w*";
        String regexZST = "[\\w\\W]*";

        if (passwordStr.matches(regexZ)) {
            GRADE_SCORE = 20;
            return "弱";
        }
        if (passwordStr.matches(regexS)) {
            GRADE_SCORE = 20;
            return "弱";
        }
        if (passwordStr.matches(regexT)) {
            GRADE_SCORE = 20;
            return "弱";
        }
        if (passwordStr.matches(regexZT)) {
            GRADE_SCORE = 60;
            return "中";
        }
        if (passwordStr.matches(regexST)) {
            GRADE_SCORE = 60;
            return "中";
        }
        if (passwordStr.matches(regexZS)) {
            GRADE_SCORE = 60;
            return "中";
        }
        if (passwordStr.matches(regexZST)) {
            GRADE_SCORE = 90;
            return "强";
        }
        return passwordStr;
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

    public static String encrypt(Context context, String data) throws Exception {
        String data_de = null;
        AppPreferences appPreferences = new AppPreferences(context);
        String pk = appPreferences.getString(StaticParams.FileKey.__KEY_PUBLIC_SERVICE__, "");
        if (TextUtils.isEmpty(pk)) {
            V.e("the saved service public key is null,encrypt error.");
            return null;
        }
        data_de = new String((FFRSAUtils.encryptByPublicKey(data.getBytes("iso8859-1"), pk)), "iso8859-1");
        appPreferences = null;
        return data_de;
    }

    @Nullable
    public static String decrypt(Context context, String data) throws Exception {
        String data_de = null;
        AppPreferences appPreferences = new AppPreferences(context);
        String prik = appPreferences.getString(StaticParams.FileKey.__KEY_PRIVATE_CLIENT__, "");
        if (TextUtils.isEmpty(prik)) {
            V.e("the saved client private key is null,decrypt error.");
            return null;
        }
        data_de = new String((FFRSAUtils.decryptByPrivateKey(data.getBytes("iso8859-1"), prik)), "iso8859-1");
        appPreferences = null;
        V.d("decrypt success:" + data_de);
        return data_de;
    }

    private static int time = 0;
    public static Timer timer = null;

    public static void timeDown(final Activity context, final int i, final TextView tvRegisterGetmsgcode) {
        time = i;
        tvRegisterGetmsgcode.setTextColor(context.getResources().getColor(R.color.line));
        tvRegisterGetmsgcode.setTextSize(13);
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvRegisterGetmsgcode.setText(String.valueOf(time) + "后再试");
                        if (time == 0) {
                            tvRegisterGetmsgcode.setTextColor(context.getResources().getColor(R.color.white));
                            tvRegisterGetmsgcode.setEnabled(true);
                            tvRegisterGetmsgcode.setText("发送验证码");
                            timer.cancel();
                            time = 0;
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
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
     * @param requestCode
     * @param resultCode
     * @param data
     * @param photoAdapter   适配器
     * @param selectedPhotos 适配器填充的集合
     */
    public static void setSelectorImgOnResult(int requestCode, int resultCode, Intent data, PhotoAdapter photoAdapter, ArrayList<String> selectedPhotos) {
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE
                        || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                //                for (int i = 0; i < photos.size(); i++) {
                //                    bitmaps.add(compressImage(Uri.fromFile(new File(photos.get(i)))));
                //                    V.d("压缩后：" + FFUtils.getBitmapFromPath(bitmaps.get(i)).getByteCount());
                //                }

            }
            selectedPhotos.clear();

            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    public static String getImgName(String imgPath) {
        return String.valueOf("lovejob_" + new Random().nextInt() + System.currentTimeMillis() + new Random().nextInt() + imgPath.substring(imgPath.lastIndexOf(".")));
    }


//    /**
//     * @param context
//     * @param photoAdapter 适配器
//     * @param imgNames     上送的图片名车个集合，该名称集合长度必须与适配器填充的数据集合长度相等
//     * @param uploadToken
//     */
//    public static void upLoadImgToQINIUYUN(final Activity context, final PhotoAdapter photoAdapter, final List<String> imgNames, final String uploadToken, final int resultCode) {
//        final UploadManager uploadManager = new UploadManager();
//        final int[] uploadImgToQNY_size_add = {0};
//        yasuo(context, photoAdapter.getList(), new Handler() {
//            @Override
//            public void handleMessage(final Message msg) {
//                if (msg.arg1 == 9000) {
//
//                    uploadManager.put(msg.getData().getString("path"), imgNames.get(msg.getData().getInt("index")), uploadToken,
//                            new UpCompletionHandler() {
//                                @Override
//                                public void complete(String key, ResponseInfo info, JSONObject res) {
//                                    //res包含hash、key等信息，具体字段取决于上传策略的设置。
//                                    V.d("上传状态回调：" + key + ",\r\n " + info + ",\r\n " + res);
//                                    uploadImgToQNY_size_add[0]++;
//                                    V.d("index:" + uploadImgToQNY_size_add[0]);
//                                    V.d("size:" + ((photoAdapter.getList().size())));
//                                    if (uploadImgToQNY_size_add[0] == photoAdapter.getList().size()) {
//                                        Utils.showToast(context, "发布成功");
////                                        Utils.dissmissDiV(context);
//                                        Intent intent =new Intent();
//                                        intent.putExtra("isRefresh",true);
//                                        context.setResult(resultCode,intent);
//                                        context.finish();
//                                    }
//                                }
//                            }, null);
//                } else {
//                    V.e("压缩失败一次");
//                }
//            }
//        });
//    }
//
//    public static void yasuo(final Activity context, final ArrayList<String> list, final Handler handler) {
//
//
//        ThreadPoolUtils.getInstance().addTask(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < list.size(); i++) {
//                    Bundle bundle = new Bundle();
//                    Message message = handler.obtainMessage();
//                    try {
//                        bundle.putString("path", compressImage(context, Uri.fromFile(new File(list.get(i)))));
//                        bundle.putInt("index", i);
//                        message.arg1 = 9000;
//                    } catch (Exception e) {
//                        message.arg1 = -1;
//                    }
//                    message.setData(bundle);
//                    handler.sendMessage(message);
//                }
//            }
//        });
//
//
//    }
//
//    public static void dissmissDiV(Context context) {
//        if (context == null) {
//            V.e("消除进度条时传入context为空");
//            return;
//        }
//        if (FFSVProgressHUD.isShowing(context)) FFSVProgressHUD.dismiss(context);
//    }


//    public static void showInfoDiV(Context context, String msg) {
//        if (context == null) {
//            V.e("显示进度条时传入context为空");
//            return;
//        }
//        if (FFSVProgressHUD.isShowing(context)) FFSVProgressHUD.dismiss(context);
//        FFSVProgressHUD.showInfoWithStatus(context, msg);
//    }

//    public static void showSuccDiV(Context context, String msg) {
//        if (context == null) {
//            V.e("显示进度条时传入context为空");
//            return;
//        }
//        if (FFSVProgressHUD.isShowing(context)) FFSVProgressHUD.dismiss(context);
//        FFSVProgressHUD.showSuccessWithStatus(context, msg);
//    }

//    public static String compressImage(Activity activity, final Uri uri) {
//
//        V.d("************************华丽的分割线************************");
//        File saveFile = new File(activity.getExternalCacheDir(), "compress_" + System.currentTimeMillis() + ".jpg");
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
//            V.d(">>>>>>>>>>>>>>>>>压缩前路径：" + uri.getPath() + ",大小：" + bitmap.getByteCount() + "<<<<<<<<<<<<<<<<<<<");
//            V.d("=>=>=>=>=>=>=>=>" + "开始压缩" + "<=<=<=<=<=<=<=<=<=<=");
//            NativeUtil.compressBitmap(bitmap, saveFile.getAbsolutePath());
//            V.d("=>=>=>=>=>=>=>=>" + "压缩完成" + "<=<=<=<=<=<=<=<=<=<=");
//            V.d(">>>>>>>>>>>>>>>>>压缩后路径：" + saveFile.getAbsolutePath() + ",大小：" + Utils.getBitmapFromPath(saveFile.getAbsolutePath()).getByteCount() + "<<<<<<<<<<<<<<<<<<<");
//            bitmap = null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            V.e(e.toString());
//        }
//        V.d("************************华丽的分割线************************");
//        return saveFile.getAbsolutePath();
//    }

    /**
     * 保存方法
     */
    public static File saveBitmap(Activity activity, Bitmap bitmap) {
        File f = new File(activity.getExternalCacheDir(), "compress_" + System.currentTimeMillis() + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            V.e(e.toString());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            V.e(e.toString());
            return null;
        }
        return f;
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            // 利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);
            // 利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz
                    .getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
        gridView.setLayoutParams(params);
    }

    public static MaterialDialog showProgressDliago(Activity context, String msg) {
        return ZDialog.showZDlialog2(context, "提示", msg);
    }


    public static void inputstreamtofile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
}
