package com.lovejob.view._userinfo.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.views.MyListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/1.
 */
public class Aty_Help extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.img_help_search)
    ImageView imgHelpSearch;
    @Bind(R.id.lv_help)
    MyListView lvHelp;
    @Bind(R.id.help_feedback)
    LinearLayout helpFeedback;
    FastAdapter<ThePerfectGirl.AdviceInfor> adapter;


    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_help);
        ButterKnife.bind(this);

    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.AdviceInfor>(context, R.layout.item_lv_suggest) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_suggest, position);
                ((TextView) viewHolder.getView(R.id.tv_suggest_num)).setText((position + 1) + ".");
                ((TextView) viewHolder.getView(R.id.tv_suggest_content)).setText(getItem(position).getContent());
                return viewHolder.getConvertView();
            }
        };
        lvHelp.setAdapter(adapter);
    }

    private void addData() {
        callList.add(LoveJob.getSuggest(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getAdviceInforList() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getAdviceInforList().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getAdviceInforList().get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        }));
    }

    @Override
    public void onResume_() {
        addData();
        initAdapter();
    }

    @Override
    public void onDestroy_() {

    }


    @OnClick({R.id.back, R.id.img_help_search, R.id.help_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.img_help_search:
                break;
            case R.id.help_feedback:
                startActivity(new Intent(context, Aty_WriteSuggest.class));
                break;
        }
    }
}
