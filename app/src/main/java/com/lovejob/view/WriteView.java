package com.lovejob.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.model.Utils;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.utils.V;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.view
 * Created on 2016-11-28 17:54
 */

public class WriteView extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    @Bind(R.id.et_aty_write)
    EditText etAtyWrite;
    private String title, content;
    private int requestCode;
    private int writeType;
    private String observerPid, questionPid;
    private int maxLenth = Integer.MAX_VALUE;
    private boolean isChanged = false;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.writeview);
        ButterKnife.bind(this);
        getParams();
        setDefaultParams();
    }

    private void setDefaultParams() {
        actionbarTitle.setText(title == null ? "" : title);
        etAtyWrite.setText(content == null ? "" : content);
//        etAtyWrite.setInputType(writeType == -1 ? EditorInfo.TYPE_CLASS_TEXT : EditorInfo.TYPE_CLASS_NUMBER);
        switch (writeType) {
            case -1:
                etAtyWrite.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            case 1:
                etAtyWrite.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                break;
            case 2:
                etAtyWrite.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL | EditorInfo.TYPE_CLASS_NUMBER);
                break;
            case 3:
                etAtyWrite.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL | EditorInfo.TYPE_CLASS_NUMBER);
                break;

        }
        etAtyWrite.setGravity(Gravity.TOP);
        etAtyWrite.setSingleLine(false);
        etAtyWrite.setHorizontallyScrolling(false);
        InputFilter[] filters = {new InputFilter.LengthFilter(maxLenth)};
        etAtyWrite.setFilters(filters);
        etAtyWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (writeType == 3) {


                    if (isChanged) {// ----->如果字符未改变则返回
                        return;
                    }
                    String str = s.toString();

                    isChanged = true;
                    String cuttedStr = str;
                /* 删除字符串中的dot */
                    for (int i = str.length() - 1; i >= 0; i--) {
                        char c = str.charAt(i);
                        if ('.' == c) {
                            cuttedStr = str.substring(0, i) + str.substring(i + 1);
                            break;
                        }
                    }
                /* 删除前面多余的0 */
                    int NUM = cuttedStr.length();
                    int zeroIndex = -1;
                    for (int i = 0; i < NUM - 2; i++) {
                        char c = cuttedStr.charAt(i);
                        if (c != '0') {
                            zeroIndex = i;
                            break;
                        } else if (i == NUM - 3) {
                            zeroIndex = i;
                            break;
                        }
                    }
                    if (zeroIndex != -1) {
                        cuttedStr = cuttedStr.substring(zeroIndex);
                    }
                /* 不足3位补0 */
                    if (cuttedStr.length() < 3) {
                        cuttedStr = "0" + cuttedStr;
                    }
                /* 加上dot，以显示小数点后两位 */
                    cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2)
                            + "." + cuttedStr.substring(cuttedStr.length() - 2);

                    etAtyWrite.setText(cuttedStr);

                    etAtyWrite.setSelection(etAtyWrite.length());
                    isChanged = false;
                }
            }
        });
    }

    private void getParams() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        requestCode = intent.getIntExtra("requestCode", -1);
        writeType = intent.getIntExtra("writeType", -1);
        maxLenth = intent.getIntExtra("maxLenth", Integer.MAX_VALUE);
        if (title != null && !TextUtils.isEmpty(title)) {
            if (title.equals("请输入真实姓名")) {
                Toast.makeText(context, "错误的姓名将导致您无法正常获得收益", Toast.LENGTH_LONG).show();
            }
        }
        try {
            observerPid = intent.getStringExtra("observerPid");
            questionPid = intent.getStringExtra("questionPid");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    @OnClick({R.id.actionbar_back, R.id.actionbar_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.actionbar_save:
                if (!TextUtils.isEmpty(etAtyWrite.getText().toString())) {
                    Intent i = new Intent();
                    i.putExtra("content", etAtyWrite.getText().toString().trim());
                    if (observerPid != null && questionPid != null) {
                        i.putExtra("observerPid", observerPid);
                        i.putExtra("questionPid", questionPid);
                    }
                    setResult(requestCode, i);
                    AppManager.getAppManager().finishActivity(this);
                    return;
                } else {
                    Utils.showToast(context, "输入不能为空");
                }
                break;
        }
    }
}
