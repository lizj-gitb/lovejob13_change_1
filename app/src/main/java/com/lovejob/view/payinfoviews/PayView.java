package com.lovejob.view.payinfoviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.model.PayTypeInfo;
import com.lovejob.model.StaticParams;
import com.lovejob.model.Utils;
import com.lovejob.model.bean.Token;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.V;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/26.
 */

public class PayView extends BaseActivity {
    @Bind(R.id.img_buytoken_back)
    ImageView imgBuytokenBack;
    @Bind(R.id.tv_buytoken_Price1)
    TextView tvBuytokenPrice1;
    @Bind(R.id.et_buytoken_count1)
    EditText etBuytokenCount1;
    @Bind(R.id.tv_buytoken_total1)
    TextView tvBuytokenTotal1;
    @Bind(R.id.tv_buytoken_Price2)
    TextView tvBuytokenPrice2;
    @Bind(R.id.et_buytoken_count2)
    EditText etBuytokenCount2;
    @Bind(R.id.tv_buytoken_total2)
    TextView tvBuytokenTotal2;
    @Bind(R.id.tv_buytoken_Price3)
    TextView tvBuytokenPrice3;
    @Bind(R.id.et_buytoken_count3)
    EditText etBuytokenCount3;
    @Bind(R.id.tv_buytoken_total3)
    TextView tvBuytokenTotal3;
    @Bind(R.id.tv_buytoken_Price4)
    TextView tvBuytokenPrice4;
    @Bind(R.id.et_buytoken_count4)
    EditText etBuytokenCount4;
    @Bind(R.id.tv_buytoken_total4)
    TextView tvBuytokenTotal4;
    @Bind(R.id.tv_buytoken_Price5)
    TextView tvBuytokenPrice5;
    @Bind(R.id.et_buytoken_count5)
    EditText etBuytokenCount5;
    @Bind(R.id.tv_buytoken_total5)
    TextView tvBuytokenTotal5;
    @Bind(R.id.et_buytoken_money)
    EditText etBuytokenMoney;
    @Bind(R.id.et_buytoken_count)
    EditText etBuytokenCount;
    @Bind(R.id.tv_buytoken_total)
    TextView tvBuytokenTotal;
    @Bind(R.id.tv_buytoken_money)
    TextView tvBuytokenMoney;
    @Bind(R.id.tv_buytoken_pay)
    TextView tvBuytokenPay;
    private ArrayList<TextView> tvs;
    private ArrayList<EditText> ets;
    private ArrayList<TextView> tvvs;
    private Activity context;
    private String workPid, level;
    int userLeave = -1;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.payview);
        ButterKnife.bind(this);
        context = this;
        initListener();
        workPid = getIntent().getStringExtra("workPid");
        level = new AppPreferences(context).getString(StaticParams.FileKey.__LEVEL__, "lv1");
        if (workPid == null || level == null) {
            Utils.showToast(context, "系统异常");
            return;
        }
        try {
            userLeave = Integer.valueOf(level);
        } catch (Throwable throwable) {
            V.e("用户等级转换错误,赋默认值1");
            userLeave = 1;
        }

        switch (userLeave) {
            case 1:
                selector(0);
                setEnlanble(0);
                break;

            case 2:
                selector(1);
                setEnlanble(1);
                break;

            case 3:
                selector(2);
                setEnlanble(2);
                break;
            case 4:
                selector(3);
                setEnlanble(3);
                break;

            case 5:
                selector(4);
                setEnlanble(4);
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initListener() {
        tvs = new ArrayList<TextView>();
        tvs.add(tvBuytokenPrice1);
        tvs.add(tvBuytokenPrice2);
        tvs.add(tvBuytokenPrice3);
        tvs.add(tvBuytokenPrice4);
        tvs.add(tvBuytokenPrice5);
        ets = new ArrayList<EditText>();
        ets.add(etBuytokenCount1);
        ets.add(etBuytokenCount2);
        ets.add(etBuytokenCount3);
        ets.add(etBuytokenCount4);
        ets.add(etBuytokenCount5);

        tvvs = new ArrayList<TextView>();
        tvvs.add(tvBuytokenTotal1);
        tvvs.add(tvBuytokenTotal2);
        tvvs.add(tvBuytokenTotal3);
        tvvs.add(tvBuytokenTotal4);
        tvvs.add(tvBuytokenTotal5);


        ets.get(0).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvvs.get(0).setText("0");
                } else {
                    tvvs.get(0).setText(String.valueOf((Integer.valueOf(s.toString()) * 1)));
                    addAmount();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ets.get(1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvvs.get(1).setText("0");
                } else {
                    tvvs.get(1).setText(String.valueOf((Integer.valueOf(s.toString()) * 10)));
                    addAmount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ets.get(2).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvvs.get(2).setText("0");
                } else {
                    tvvs.get(2).setText(String.valueOf((Integer.valueOf(s.toString()) * 100)));
                    addAmount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ets.get(3).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvvs.get(3).setText("0");
                } else {
                    tvvs.get(3).setText(String.valueOf((Integer.valueOf(s.toString()) * 1000)));
                    addAmount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ets.get(4).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvvs.get(4).setText("0");
                } else {
                    tvvs.get(4).setText(String.valueOf((Integer.valueOf(s.toString()) * 10000)));
                    addAmount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etBuytokenMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvBuytokenTotal.setText("0");
                } else {
                    if (TextUtils.isEmpty(etBuytokenCount.getText().toString())) {
                        tvBuytokenTotal.setText("0");
                    } else {
                        tvBuytokenTotal.setText(String.valueOf((Integer.valueOf(s.toString()) * Integer.valueOf(etBuytokenCount.getText().toString()))));
                        addAmount();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etBuytokenCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tvBuytokenTotal.setText("0");
                } else {
                    if (TextUtils.isEmpty(etBuytokenMoney.getText().toString())) {
                        tvBuytokenTotal.setText("0");
                    } else {
                        tvBuytokenTotal.setText(String.valueOf((Integer.valueOf(s.toString()) * Integer.valueOf(etBuytokenMoney.getText().toString()))));
                    }

                    addAmount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void addAmount() {
        tvBuytokenMoney.setText(String.valueOf(Integer.valueOf(tvBuytokenTotal1.getText().toString()) +
                Integer.valueOf(tvBuytokenTotal2.getText().toString()) +
                Integer.valueOf(tvBuytokenTotal3.getText().toString()) +
                Integer.valueOf(tvBuytokenTotal4.getText().toString()) +
                Integer.valueOf(tvBuytokenTotal5.getText().toString()) +
                Integer.valueOf(tvBuytokenTotal.getText().toString())));
    }

    @OnClick({R.id.img_buytoken_back, R.id.tv_buytoken_Price1, R.id.tv_buytoken_Price2, R.id.tv_buytoken_Price3, R.id.tv_buytoken_Price4, R.id.tv_buytoken_Price5, R.id.tv_buytoken_total, R.id.tv_buytoken_money, R.id.tv_buytoken_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_buytoken_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.tv_buytoken_Price1:
                selector(0);
                break;
            case R.id.tv_buytoken_Price2:
                selector(1);
                break;
            case R.id.tv_buytoken_Price3:
                selector(2);
                break;
            case R.id.tv_buytoken_Price4:
                selector(3);
                break;
            case R.id.tv_buytoken_Price5:
                selector(4);
                break;
//            case R.id.tv_buytoken_money:
//                break;
            case R.id.tv_buytoken_pay:
//                if (TextUtils.isEmpty(tvBuytokenMoney.getText().toString())) {
//                    Utils.showToast(context, "金额不可为空");
//                    return;
//                }
                int money = Integer.parseInt((tvBuytokenMoney.getText().toString()));
                if (money<=0){
                    Utils.showToast(context, "金额不可为空");
                    return;
                }
                Intent intent = new Intent(this, PayViewSelectPayment.class);

//                intent.putExtra("level1", etBuytokenCount1.getText().toString());
//                intent.putExtra("level2", etBuytokenCount1.getText().toString());
//                intent.putExtra("level3", etBuytokenCount3.getText().toString());
//                intent.putExtra("level4", etBuytokenCount4.getText().toString());
//                intent.putExtra("level5", etBuytokenCount5.getText().toString());

                intent.putExtra("workPid", workPid);
                intent.putExtra("token", new Token(etBuytokenCount1.getText().toString(),
                        etBuytokenCount2.getText().toString(), etBuytokenCount3.getText().toString()
                        , etBuytokenCount4.getText().toString(), etBuytokenCount5.getText().toString(), tvBuytokenTotal.getText().toString(), etBuytokenCount.getText().toString()));
                intent.putExtra("price", tvBuytokenMoney.getText().toString());
                intent.putExtra("PayTypeInfo", PayTypeInfo.SendJobWork);
                startActivity(intent);
                AppManager.getAppManager().finishActivity(this);
                break;
        }

    }


    private void setEnlanble(int index) {
        for (int i = 0; i < 5; i++) {
            if (index == i) {
                tvs.get(i).setEnabled(true);
            } else {
                tvs.get(i).setEnabled(false);
            }
        }
    }

    //0
    private void selector(int index) {

        for (int i = 0; i < 5; i++) {
            if (index == i) {
                tvs.get(i).setBackground(getResources().getDrawable(R.mipmap.danjia));
                tvs.get(i).setTextColor(getResources().getColor(R.color.white));
                ets.get(i).setEnabled(true);
                ets.get(i).setFocusable(true);
                ets.get(i).setFocusableInTouchMode(true);
                ets.get(i).requestFocus();//获取焦点 光标出现
            } else {
                tvs.get(i).setBackground(getResources().getDrawable(R.mipmap.keyigoumai));
                tvs.get(i).setTextColor(getResources().getColor(R.color.actionbar));
                ets.get(i).setEnabled(false);
            }
        }
    }


    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }
}
