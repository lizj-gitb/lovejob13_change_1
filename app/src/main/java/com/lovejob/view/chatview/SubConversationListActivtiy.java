package com.lovejob.view.chatview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.R;
import com.v.rapiddev.base.AppManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Bob on 15/8/18.
 * 聚合会话列表
 * 什么是聚合会话列表？
 */
public class SubConversationListActivtiy extends FragmentActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.txt1)
    TextView mTitle;
    @Bind(R.id.img3)
    TextView img3;
    @Bind(R.id.rl)
    RelativeLayout rl;
    /**
     * 聚合类型
     */
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subconversationlist);
        ButterKnife.bind(this);
        getActionBarTitle();
    }

    /**
     * 通过 intent 中的数据，得到当前的 targetId 和 type
     */
    private void getActionBarTitle() {

        Intent intent = getIntent();

        type = intent.getData().getQueryParameter("type");

        if (type.equals("group")) {
            mTitle.setText("聚合群组");
        } else if (type.equals("private")) {
            mTitle.setText("聚合单聊");
        } else if (type.equals("discussion")) {
            mTitle.setText("聚合讨论组");
        } else if (type.equals("system")) {
            mTitle.setText("聚合系统会话");
        } else {
            mTitle.setText("聚合");
        }

    }

    @OnClick(R.id.back)
    public void onClick() {
        AppManager.getAppManager().finishActivity(this);
    }
}

