package com.v.rapiddev.dialogs.zdialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.v.rapiddev.R;
import com.v.rapiddev.dialogs.core.DialogAction;
import com.v.rapiddev.dialogs.core.MaterialDialog;
import com.v.rapiddev.utils.V;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:RapidDevSample
 * Package_Name:com.v.rapiddev.dialogs.zdialog
 * Created on 2016-11-24 21:27
 */

public final class ZDialog {

    private static Handler handler;

    public static void setHandler(Handler handler) {
        ZDialog.handler = handler;
    }

    /**
     * 对话框
     *
     * @param context
     * @param title           标题
     * @param content         内容
     * @param leftButtonText  按钮文字 左
     * @param rightButtonText 按钮文字 右
     * @param clickListener   监听
     * @return
     */
    public static MaterialDialog showZDlialog(final Activity context, final String title, final String content, final String leftButtonText, final String rightButtonText, final OnDialogItemClickListener clickListener) {
        return new MaterialDialog.Builder(context)
                .iconRes(R.drawable.ic_launcher)
                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                .title(title)
                .content(content)
                .autoDismiss(false)
                .positiveText(rightButtonText)
                .negativeText(leftButtonText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        clickListener.onRightButtonClickListener();
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        clickListener.onLeftButtonClickListener();
                    }
                })
                .show();
    }

    public static MaterialDialog showZDlialog(final Activity context, String title, String content, String leftButtonText, String rightButtonText, @DrawableRes int icon, final OnDialogItemClickListener clickListener) {
        return new MaterialDialog.Builder(context)
                .iconRes(icon)
                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                .title(title)
                .content(content)
                .autoDismiss(false)
                .positiveText(rightButtonText)
                .negativeText(leftButtonText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        clickListener.onRightButtonClickListener();
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        clickListener.onLeftButtonClickListener();
                    }
                })
                .show();
    }

    /**
     * 进度条 没有固定长度  不定长
     *
     * @param context
     * @param title
     * @param content
     * @return
     */
    public static MaterialDialog showZDlialog2(Activity context, String title, String content) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(true, 0)
                .widgetColor(Color.parseColor("#3F51B5"))
                .progressIndeterminateStyle(false)
                .show();
    }

    {

//
//        new MaterialDialog.Builder(this)
//                .title("标题")
//                .content("请等待")
//                .widgetColor(Color.parseColor("#3F51B5"))
//                .contentGravity(GravityEnum.CENTER)
//                .progress(false, 100, true)
//                .cancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
////                        if (mThread != null)
////                            mThread.interrupt();
//                        Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .showListener(new DialogInterface.OnShowListener() {
//                    @Override
//                    public void onShow(DialogInterface dialogInterface) {
//                        final MaterialDialog dialog = (MaterialDialog) dialogInterface;
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                while (dialog.getCurrentProgress() != dialog.getMaxProgress() &&
//                                        !Thread.currentThread().isInterrupted()) {
//                                    if (dialog.isCancelled())
//                                        break;
//                                    try {
//                                        Thread.sleep(50);
//                                    } catch (InterruptedException e) {
//                                        break;
//                                    }
//                                    dialog.incrementProgress(1);
//                                }
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
////                                        mThread = null;
//                                        dialog.setContent("完成");
//                                        dialog.dismiss();
//                                    }
//                                });
//                            }
//                        }).start();
//                    }
//                }).show();
    }
//    public static Progress showZProgressDlialog(final Activity context, String title, String content) {
//        return (Progress) new MaterialDialog.Builder(context)
//                .title(title)
//                .content(content)
//                .widgetColor(Color.parseColor("#3F51B5"))
//                .contentGravity(GravityEnum.CENTER)
//
//                .cancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
////                        if (mThread != null)
////                            mThread.interrupt();
////                        Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .show();
//    }

//    public class Progress extends MaterialDialog {
//        protected Progress(Builder builder) {
//            super(builder);
//        }
//        public void setProgresss(int max){
//           super.getBuilder() .progress(false, max, true);
//        }
//        public void updataProgress() {
//            super.getBuilder().showListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface dialogInterface) {
//                    final MaterialDialog dialog = (MaterialDialog) dialogInterface;
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            while (dialog.getCurrentProgress() != dialog.getMaxProgress() &&
//                                    !Thread.currentThread().isInterrupted()) {
//                                if (dialog.isCancelled())
//                                    break;
//                                try {
//                                    Thread.sleep(50);
//                                } catch (InterruptedException e) {
//                                    break;
//                                }
//                                dialog.incrementProgress(1);
//                            }
//                            context.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
////                                        mThread = null;
//                                    dialog.setContent("完成");
//                                    dialog.dismiss();
//                                }
//                            });
//                        }
//                    }).start();
//                }
//            })
//        }
//    }
}
