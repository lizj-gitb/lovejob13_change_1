package com.lovejob.view.payinfoviews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.ThreadPoolUtils;
import com.lovejob.model.UserInputModel;
import com.lovejob.model.Utils;
import com.lovejob.model.bean.Token;
import com.lovejob.qiniuyun.http.ResponseInfo;
import com.lovejob.qiniuyun.storage.UpCompletionHandler;
import com.lovejob.qiniuyun.storage.UploadManager;
import com.lovejob.view._job.JobDetails;
import com.lovejob.view._money.Aty_OriDetails;
import com.lovejob.view._money.Aty_ParDetails;
import com.lovejob.view._userinfo.mylist.Aty_MyList;
import com.lovejob.view._userinfo.mywalletinfos.Aty_MoneyDetails;
import com.lovejob.view.payinfoviews.alipay.AlipayResponseData;
import com.lovejob.view.payinfoviews.alipay.PayResult;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.dialogs.zdialog.OnDialogItemClickListener;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
import com.v.rapiddev.utils.V;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/27.
 */

public class PayViewSelectPayment extends BaseActivity {
    @Bind(R.id.tv_money_price)
    TextView tvMoneyPrice;
    @Bind(R.id.img_payment_selector_wechat)
    ImageView imgPaymentSelectorWechat;
    @Bind(R.id.rl_payment_payforWechat)
    RelativeLayout rlPaymentPayforWechat;
    @Bind(R.id.img_payment_selector_alaipay)
    ImageView imgPaymentSelectorAlaipay;
    @Bind(R.id.rl_payment_payforAlipay)
    RelativeLayout rlPaymentPayforAlipay;
    @Bind(R.id.bt_payment_commit)
    Button btPaymentCommit;
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    private String workPid, price, outTradeNo;
    private PayType payType = PayType.WeiXin;
    private IWXAPI api;
    private Token token;
    private MyWeChatPayListener payListener;
    private PayTypeInfo payTypeInfo;
    private UserInputModel userInputModel;
    private ArrayList<String> photosPaths;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.selectpayment);
        ButterKnife.bind(this);
        /**
         * 设置支付的actionbar
         */
        setActionbar();
        /**
         * 获取上个页面传入的业务类型
         */
        payTypeInfo = (PayTypeInfo) getIntent().getSerializableExtra("PayTypeInfo");
        /**
         * 未接收到业务类型时不做任何处理
         */
        if (payTypeInfo == null) {
            Utils.showToast(context, "系统不安全，请稍后再试。");
            V.e("支付页面未接收到业务类型");
            return;
        }
        /**
         * 区分当前支付类型（是什么业务前的支付）
         */
        switch (payTypeInfo) {
            case SendJobWork:
                //从发布长期工作进入
                V.d("发布长期工作----->>>>购买令牌");
                workPid = getIntent().getStringExtra("workPid");
                price = getIntent().getStringExtra("price");
                token = (Token) getIntent().getSerializableExtra("token");
                if (TextUtils.isEmpty(workPid) || TextUtils.isEmpty(price) || token == null) {
                    Utils.showToast(context, "系统异常");
                    return;
                }
                break;

            case SendMoneyWork_Ori:
                //从发布创意工作进入
                V.d("发布创意工作----->>>>送用户的工作信息");
                userInputModel = (UserInputModel) getIntent().getSerializableExtra("inputModel");
                if (userInputModel == null) {
                    Utils.showToast(context, "系统异常");
                    return;
                }
//                workPid = getIntent().getStringExtra("workPid");
                price = userInputModel.getParams()[2].substring(0, userInputModel.getParams()[2].length() - 3);
                price = userInputModel.getParams()[2];
                photosPaths = getIntent().getStringArrayListExtra("photosPaths");
                break;
            case SnedMoneyWork_Pak:
                //从发布兼职工作进入
                V.d("发布兼职工作----->>>>送用户的工作信息");
                userInputModel = (UserInputModel) getIntent().getSerializableExtra("inputModel");
                if (userInputModel == null) {
                    Utils.showToast(context, "系统异常");
                    return;
                }
//                workPid = getIntent().getStringExtra("workPid");
//                price = userInputModel.getParams()[5].substring(0, userInputModel.getParams()[5].length() - 3);
                int personNumber = Integer.valueOf(userInputModel.getParams()[4]);
                Double price_ = Double.valueOf(userInputModel.getParams()[5]);
                price = String.valueOf(personNumber*price_);
                break;

            case BuyUserService:
                //从购买服务页面进入
                V.d("购买服务----->>>>送服务相关信息");
                workPid = getIntent().getStringExtra("serPid");
                price = getIntent().getStringExtra("price");
                break;

            case RePay:
                //重新付款接口
                outTradeNo = getIntent().getStringExtra("outTradeNo");
                break;

        }
        tvMoneyPrice.setText(price);
    }

    private void setActionbar() {
        actionbarTitle.setText("支付");
        actionbarTitle.setTextSize(16);
        actionbarTitle.setTextColor(Color.WHITE);
        actionbarShared.setVisibility(View.INVISIBLE);
        actionbarSave.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume_() {
    }

    @Override
    public void onDestroy_() {
        try {
            unregisterReceiver(payListener);
            V.d("微信支付广播注销成功");
        } catch (Exception e) {
            e.printStackTrace();
            V.e("微信支付的广播未注册，注销失败");
        }
    }

    @OnClick({R.id.actionbar_back, R.id.rl_payment_payforWechat, R.id.rl_payment_payforAlipay, R.id.bt_payment_commit})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(PayViewSelectPayment.this);
                break;
            case R.id.rl_payment_payforWechat:
                imgPaymentSelectorWechat.setImageResource(R.mipmap.xuanzhong);
                imgPaymentSelectorAlaipay.setImageResource(R.mipmap.weixuanzhong);
                V.d("选择了微信支付");
                payType = PayType.WeiXin;
                break;
            case R.id.rl_payment_payforAlipay:
                imgPaymentSelectorWechat.setImageResource(R.mipmap.weixuanzhong);
                imgPaymentSelectorAlaipay.setImageResource(R.mipmap.xuanzhong);
                payType = PayType.Aliapay;
                V.d("选择了支付宝支付");
                break;
            case R.id.bt_payment_commit:
                //获取订单号
                dialog = Utils.showProgressDliago(context, "请稍后……");
                switch (payTypeInfo) {
                    case SendJobWork:
                        //发布长期工作
                        sendJobWork();
                        break;

                    case SendMoneyWork_Ori:
                        //发布创意工作
                        senMoneyWork_Ori();
                        break;
                    case SnedMoneyWork_Pak:
                        //发布兼职工作
                        senMoneyWork_Park();
                        break;

                    case BuyUserService:
                        //购买服务页面
                        buyService();
                        break;
                    case RePay:
                        /**
                         * 重新付款
                         */
                        repay();
                        break;
                }
                break;
        }
    }

    private void repay() {
        callList.add(LoveJob.repay(outTradeNo, payType, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                switch (payType) {
                    case Aliapay:
                        openAlipayClient(thePerfectGirl.getData().getApliPayDTO().getAlipaySign(), new OnPayListener() {
                            @Override
                            public void onSuccess() {
                                dialog.dismiss();
                                Utils.showToast(context, "支付成功");
                                V.d("跳转我的账单");
                                AppManager.getAppManager().toNextPage(Aty_MyList.class, true);
                            }

                            @Override
                            public void onError(String errorMsg) {
                                dialog.dismiss();
                                Utils.showToast(context, errorMsg);
                                AppManager.getAppManager().finishActivity(context);
                            }
                        });
                        break;

                    case WeiXin:
                        registerBr(new OnPayListener() {
                            @Override
                            public void onSuccess() {
                                dialog.dismiss();
                                Utils.showToast(context, "支付成功");
                                V.d("跳转我的账单");
                                AppManager.getAppManager().toNextPage(Aty_MyList.class, true);
                            }

                            @Override
                            public void onError(String errorMsg) {
                                dialog.dismiss();
                                Utils.showToast(context, errorMsg);
                                AppManager.getAppManager().finishActivity(context);
                            }
                        });
                        openWeChatClient(thePerfectGirl.getData().getWeChatPayDTO());
                        break;
                }
            }

            @Override
            public void onError(String msg) {
                dialog.dismiss();
                Utils.showToast(context, "付款失败，请稍后再试。" + msg);
                V.e("重新付款失败，" + msg);
            }
        }));
    }

    private void buyService() {
        callList.add(LoveJob.getOrderSer("61", payType, workPid, "用户购买服务", "用户购买服务的金额", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                V.d("购买服务前生成支付订单成功");
                switch (payType) {
                    case Aliapay:
                        openAlipayClient(thePerfectGirl.getData().getApliPayDTO().getAlipaySign(), new OnPayListener() {
                            @Override
                            public void onSuccess() {
                                dialog.dismiss();
                                Utils.showToast(context, "支付成功");
                                V.d("跳转我的账单");
                                AppManager.getAppManager().toNextPage(Aty_MyList.class, true);
                            }

                            @Override
                            public void onError(String errorMsg) {
                                dialog.dismiss();
                                Utils.showToast(context, errorMsg);
                                AppManager.getAppManager().finishActivity(context);
                            }
                        });
                        break;

                    case WeiXin:
                        registerBr(new OnPayListener() {
                            @Override
                            public void onSuccess() {
                                dialog.dismiss();
                                Utils.showToast(context, "支付成功");
                                V.d("跳转我的账单");
                                AppManager.getAppManager().toNextPage(Aty_MyList.class, true);
                            }

                            @Override
                            public void onError(String errorMsg) {
                                dialog.dismiss();
                                Utils.showToast(context, errorMsg);
                                AppManager.getAppManager().finishActivity(context);
                            }
                        });
                        openWeChatClient(thePerfectGirl.getData().getWeChatPayDTO());
                        break;
                }
            }

            @Override
            public void onError(String msg) {
                dialog.dismiss();
                Utils.showToast(context, "服务购买失败，请稍后再试。" + msg);
                V.e("服务购买失败，" + msg);
            }
        }));
    }

    private void senMoneyWork_Park() {
        callList.add(LoveJob.sendMoneyWork_park(userInputModel, payType, price,
                "用户发布兼职工作预付金", "用户发布兼职工作的预付金", new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        workPid = thePerfectGirl.getData().getWorkPid();
                        V.d("兼职工作支付订单生成成功");
                        switch (payType) {
                            case Aliapay:
                                openAlipayClient(thePerfectGirl.getData().getApliPayDTO().getAlipaySign(), new OnPayListener() {
                                    @Override
                                    public void onSuccess() {
                                        dialog.dismiss();
                                        Utils.showToast(context, "支付成功");
                                        V.d("跳转兼职工作详情页面");
                                        Intent intentpar = new Intent(context, Aty_ParDetails.class);
                                        intentpar.putExtra("workId", workPid);
                                        intentpar.putExtra("isEdit", true);
                                        AppManager.getAppManager().toNextPage(intentpar, true);
                                    }

                                    @Override
                                    public void onError(final String errorMsg) {
                                        dialog.dismiss();
                                        HandlerUtils.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ZDialog.showZDlialog(PayViewSelectPayment.this, "提示", "支付失败，" + errorMsg + ",请至我的钱包-流水页面重新付款,当您付款成功后工作会展示相应页面",
                                                        "去付款", "取消", new OnDialogItemClickListener() {
                                                            @Override
                                                            public void onLeftButtonClickListener() {
                                                                V.d("去付款");
                                                                Intent intent = new Intent(context, Aty_MoneyDetails.class);
                                                                intent.putExtra("isSelectOrder", true);
                                                                AppManager.getAppManager().toNextPage(intent, true);
                                                            }

                                                            @Override
                                                            public void onRightButtonClickListener() {
                                                                V.e("用户点击取消按钮");
                                                                AppManager.getAppManager().finishActivity(context);
                                                            }
                                                        });
                                            }
                                        });

                                    }
                                });
                                break;

                            case WeiXin:
                                registerBr(new OnPayListener() {
                                    @Override
                                    public void onSuccess() {
                                        dialog.dismiss();
                                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                                        V.d("支付成功，跳转兼职工作详情");
                                        Intent intentpar = new Intent(context, Aty_ParDetails.class);
                                        intentpar.putExtra("workId", workPid);
                                        intentpar.putExtra("isEdit", true);
                                        AppManager.getAppManager().toNextPage(intentpar, true);
                                    }

                                    @Override
                                    public void onError(final String errorMsg) {
                                        dialog.dismiss();
                                        HandlerUtils.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ZDialog.showZDlialog(PayViewSelectPayment.this, "提示", "支付失败，" + errorMsg + ",请至我的钱包-流水页面重新付款,当您付款成功后工作会展示相应页面",
                                                        "去付款", "取消", new OnDialogItemClickListener() {
                                                            @Override
                                                            public void onLeftButtonClickListener() {
                                                                V.d("去付款");
                                                                Intent intent = new Intent(context, Aty_MoneyDetails.class);
                                                                intent.putExtra("isSelectOrder", true);
                                                                AppManager.getAppManager().toNextPage(intent, true);
                                                            }

                                                            @Override
                                                            public void onRightButtonClickListener() {
                                                                V.e("用户点击取消按钮");
                                                                AppManager.getAppManager().finishActivity(context);
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });
                                openWeChatClient(thePerfectGirl.getData().getWeChatPayDTO());
                                break;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, "兼职工作发布失败，请稍后再试。" + msg);
                        V.e("生成兼职工作发布的订单失败，" + msg);
                    }
                }));
    }

    private void senMoneyWork_Ori() {
        //生成发布创意工作的支付订单
//        //生成新的名字的集合
//        String[] stts = userInputModel.getParams()[7].split("\\|");
//        ArrayList<String> list = new ArrayList<>();
//        for (int i = 0; i < stts.length; i++) {
//            list.add(stts[i]);
//        }
//        Utils.yasuo(context, list, new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//
//            }
//        });
        callList.add(LoveJob.sendOriWork(userInputModel, payType, price, "用户发布创意工作", "用户发布创意工作的预付金",
                new OnAllParameListener() {
                    @Override
                    public void onSuccess(final ThePerfectGirl thePerfectGirl) {
                        workPid = thePerfectGirl.getData().getWorkPid();
                        //创意订单生成成功，开始上传图片 并发起支付
                        V.d("用户发布创意工作成功，已经返回上传图片的token和支付的订单号");
                        if (thePerfectGirl.getData().getUploadToken() != null) {
                            //上传图片
                            ThreadPoolUtils.getInstance().addTask(new Runnable() {
                                @Override
                                public void run() {
                                    String[] imgName = userInputModel.getParams()[7].split("\\|");
                                    for (int i = 0; i < imgName.length; i++) {
                                        UploadManager m = new UploadManager();
                                        m.put(photosPaths.get(i), imgName[i], thePerfectGirl.getData().getUploadToken(), new UpCompletionHandler() {
                                            @Override
                                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                                V.d("支付页面生成订单后的图片上传成功");
                                            }
                                        }, null);
                                    }
                                }
                            });
                        }

                        switch (payType) {
                            case Aliapay:
                                //打开支付宝客户端交易
                                openAlipayClient(thePerfectGirl.getData().getApliPayDTO().getAlipaySign(), new OnPayListener() {
                                    @Override
                                    public void onSuccess() {
                                        dialog.dismiss();
                                        Utils.showToast(context, "支付成功");
                                        V.d("跳转创意工作详情页面");
                                        Intent intent = new Intent(context, Aty_OriDetails.class);
                                        intent.putExtra("workId", workPid);
                                        AppManager.getAppManager().toNextPage(intent, true);
                                    }

                                    @Override
                                    public void onError(final String errorMsg) {
                                        dialog.dismiss();
                                        HandlerUtils.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ZDialog.showZDlialog(PayViewSelectPayment.this, "提示", "支付失败，" + errorMsg + ",请至我的钱包-流水页面重新付款,当您付款成功后工作会展示相应页面",
                                                        "去付款", "取消", new OnDialogItemClickListener() {
                                                            @Override
                                                            public void onLeftButtonClickListener() {
                                                                V.d("去付款");
                                                                Intent intent = new Intent(context, Aty_MoneyDetails.class);
                                                                intent.putExtra("isSelectOrder", true);
                                                                AppManager.getAppManager().toNextPage(intent, true);
                                                            }

                                                            @Override
                                                            public void onRightButtonClickListener() {
                                                                V.e("用户点击取消按钮");
                                                                AppManager.getAppManager().finishActivity(context);
                                                            }
                                                        });
                                            }
                                        });

                                    }
                                });
                                break;
                            case WeiXin:
                                //打开微信客户端交易
                                registerBr(new OnPayListener() {
                                    @Override
                                    public void onSuccess() {
                                        dialog.dismiss();
                                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                                        V.d("跳转创意工作详情页面");
                                        Intent intent = new Intent(context, Aty_OriDetails.class);
                                        intent.putExtra("workId", workPid);
                                        intent.putExtra("isEdit", true);
                                        AppManager.getAppManager().toNextPage(intent, true);
                                    }

                                    @Override
                                    public void onError(final String errorMsg) {
                                        dialog.dismiss();
                                        HandlerUtils.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ZDialog.showZDlialog(PayViewSelectPayment.this, "提示", "支付失败，" + errorMsg + ",请至我的钱包-流水页面重新付款,当您付款成功后工作会展示相应页面",
                                                        "去付款", "取消", new OnDialogItemClickListener() {
                                                            @Override
                                                            public void onLeftButtonClickListener() {
                                                                V.d("去付款");
                                                                Intent intent = new Intent(context, Aty_MoneyDetails.class);
                                                                intent.putExtra("isSelectOrder", true);
                                                                AppManager.getAppManager().toNextPage(intent, true);
                                                            }

                                                            @Override
                                                            public void onRightButtonClickListener() {
                                                                V.e("用户点击取消按钮");
                                                                AppManager.getAppManager().finishActivity(context);
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });
                                openWeChatClient(thePerfectGirl.getData().getWeChatPayDTO());
                                break;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, "创意工作发布失败，" + msg);
                        V.e("生成创意工作发布的订单失败，" + msg);
                    }
                }));
    }

    private void sendJobWork() {
        /**
         * 生成订单
         */
        callList.add(LoveJob.getOrder("312", payType, workPid, price,
                "购买令牌", "用户用于购买工作令牌", token, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        //根据用户选择的支付方式调起不同的客户端
                        switch (payType) {
                            case Aliapay:
                                dialog.dismiss();//关闭进度条 因为支付宝自带进度条
                                //调起支付宝客户端
                                openAlipayClient(thePerfectGirl.getData().getApliPayDTO().getAlipaySign(), new OnPayListener() {
                                    @Override
                                    public void onSuccess() {
                                        dialog.dismiss();
                                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, JobDetails.class);
                                        intent.putExtra("workId", workPid);
                                        AppManager.getAppManager().toNextPage(intent, true);
                                    }

                                    @Override
                                    public void onError(final String errorMsg) {
                                        dialog.dismiss();
                                        HandlerUtils.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ZDialog.showZDlialog(PayViewSelectPayment.this, "提示", "支付失败，" + errorMsg + ",请至我的钱包-流水页面重新付款,当您付款成功后相应的令牌会展示到首页",
                                                        "去付款", "取消", new OnDialogItemClickListener() {
                                                            @Override
                                                            public void onLeftButtonClickListener() {
                                                                V.d("去付款");
                                                                Intent intent = new Intent(context, Aty_MoneyDetails.class);
                                                                intent.putExtra("isSelectOrder", true);
                                                                AppManager.getAppManager().toNextPage(intent, true);
                                                            }

                                                            @Override
                                                            public void onRightButtonClickListener() {
                                                                V.e("用户点击取消按钮");
                                                                AppManager.getAppManager().finishActivity(context);
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });
                                break;
                            case WeiXin:
                                //调起微信客户端
                                //注册微信支付结果的通知
                                registerBr(new OnPayListener() {
                                    @Override
                                    public void onSuccess() {
                                        dialog.dismiss();
                                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, JobDetails.class);
                                        intent.putExtra("workId", workPid);
                                        AppManager.getAppManager().toNextPage(intent, true);
                                    }

                                    @Override
                                    public void onError(final String errorMsg) {
                                        //不管成功失败，均关掉该页面,防止发起第二次支付
                                        dialog.dismiss();
                                        HandlerUtils.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ZDialog.showZDlialog(PayViewSelectPayment.this, "提示", "支付失败，" + errorMsg + ",请至我的钱包-流水页面重新付款,当您付款成功后相应的令牌会展示到首页",
                                                        "去付款", "取消", new OnDialogItemClickListener() {
                                                            @Override
                                                            public void onLeftButtonClickListener() {
                                                                V.d("去付款");
                                                                Intent intent = new Intent(context, Aty_MoneyDetails.class);
                                                                intent.putExtra("isSelectOrder", true);
                                                                AppManager.getAppManager().toNextPage(intent, true);
                                                            }

                                                            @Override
                                                            public void onRightButtonClickListener() {
                                                                V.e("用户点击取消按钮");
                                                                AppManager.getAppManager().finishActivity(context);
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });
                                openWeChatClient(thePerfectGirl.getData().getWeChatPayDTO());
                                break;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialog.dismiss();
                        Utils.showToast(context, "令牌购买失败，" + msg);
                        V.e("生成购买令牌的订单失败，" + msg);
                    }
                }));
    }

    private void registerBr(OnPayListener onPayListener) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.lovejob.wechatpayinfos");
        payListener = new MyWeChatPayListener(onPayListener);
        registerReceiver(payListener, filter);
        V.d("微信支付状态通知广播注册成功");
    }

    private void openWeChatClient(ThePerfectGirl.WeChatPayDTO weChatPayDTO) {
        //TODO 打开微信客户端
        PayReq request = new PayReq();
        request.appId = weChatPayDTO.getAppid();
        request.partnerId = weChatPayDTO.getPartnerid();
        request.prepayId = weChatPayDTO.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = weChatPayDTO.getNoncestr();
        request.timeStamp = weChatPayDTO.getTimeStamp();

//       request.sign = MD5.getMessageDigest((FFUtils.sort_Map(map) + "&key=" + key).getBytes()).toUpperCase();
        request.sign = weChatPayDTO.getSign();
        api = WXAPIFactory.createWXAPI(context, weChatPayDTO.getAppid(), true);
        api.registerApp(weChatPayDTO.getAppid());
        boolean isok = false;
        if (api.isWXAppInstalled()) {
            V.d("检测到微信客户端已安装，调起微信客户端");
            isok = api.sendReq(request);
            if (!isok) {
                dialog.dismiss();
                Utils.showToast(context, "微信客户端检测异常");
                return;
            }
            //微信支付的订单，发起交易查询时用到
            PayInfoParams.prepayId = request.prepayId;
        } else {
            V.e("检测到微信客户端未安装");
            Utils.showToast(context, "请安装微信客户端");
            dialog.dismiss();
        }
        V.d("-------" + isok);
    }

    /**
     * 打开支付宝客户端  （将在用户支付状态有改变时（包括取消）时发起该笔订单的状态查询（到爱上工作服务器））
     *
     * @param orderId  支付订单
     * @param listener 状态监听
     */
    private void openAlipayClient(final String orderId, final OnPayListener listener) {
        //开启新线程调起支付宝客户端
        ThreadPoolUtils.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                /**
                 * orderId  订单号
                 * false    是否弹出进度条
                 */
                Map<String, String> result = alipay.payV2(orderId, false);
                //将支付的结果解析到对象
                PayResult payResult = new PayResult(result);
                //获取支付宝返回的支付状态（不可信）
                String state = payResult.getResultStatus();/*无论成功与否均向爱上工作服务器发起订单状态查询*/
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息(包括发起交易查询时的订单号)
                AlipayResponseData alipayResponseData = new Gson().fromJson(resultInfo, AlipayResponseData.class);
                if (state.equals("9000")) {
                    if (alipayResponseData.getAlipay_trade_app_pay_response().getCode().equals("10000")) {
                        //发起交易查询
                        callList.add(LoveJob.getPayStateFromService(payType, alipayResponseData.getAlipay_trade_app_pay_response().getOut_trade_no(),
                                new OnAllParameListener() {
                                    @Override
                                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                        V.d("支付宝支付状态查询成功");
                                        listener.onSuccess();
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        listener.onError(msg);
                                    }
                                }));
                    } else {
                        listener.onError(alipayResponseData.getAlipay_trade_app_pay_response().getMsg());
                    }
                } else {
                    listener.onError(payResult.getMemo());
                }
            }
        });
    }


    public class MyWeChatPayListener extends BroadcastReceiver {
        OnPayListener onPayListener;

        public MyWeChatPayListener(OnPayListener onPayListener) {
            this.onPayListener = onPayListener;
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("com.lovejob.wechatpayinfos")) {
                V.d("接收到微信的支付状态code：" + intent.getIntExtra("code", -1));

                int errCode = intent.getIntExtra("code", -1);
                switch (errCode) {
                    case -1:
                        onPayListener.onError("支付失败");
                        break;

                    case 0:
                        V.d("微信返回支付成功，开始发起订单查询,当前查询支付订单号：" + PayInfoParams.prepayId);
                        callList.add(LoveJob.getPayStateFromService(payType, PayInfoParams.prepayId, new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                onPayListener.onSuccess();
                            }

                            @Override
                            public void onError(String msg) {
                                onPayListener.onError(msg);
                            }
                        }));
                        break;

                    case -2:
                        onPayListener.onError("用户取消操作");
                        break;
                }
            }
        }
    }


}
