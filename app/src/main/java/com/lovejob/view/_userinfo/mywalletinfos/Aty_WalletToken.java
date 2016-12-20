package com.lovejob.view._userinfo.mywalletinfos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.pulltorefresh.PullToRefreshListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Aty_WalletToken extends BaseActivity {
    @Bind(R.id.img_moneydetails_back)
    ImageView imgMoneydetailsBack;
    @Bind(R.id.lv_wallet_token)
    PullToRefreshListView lvWalletToken;
    Activity context;
    FastAdapter<ThePerfectGirl.WorkTokenDTO> adapter;
    private Call call_getMyToken;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_wallet_token);
        ButterKnife.bind(this);
        context = this;
        initAdapter();
    }

    private void addData() {

        call_getMyToken = LoveJob.getMyToken(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getUserTokenList() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getUserTokenList().size(); i++) {
                        adapter.addItem(thePerfectGirl.getData().getUserTokenList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.WorkTokenDTO>(context, R.layout.item_lv_wallettoken) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_wallettoken, position);
                ((TextView) viewHolder.getView(R.id.tv_wallet_money)).setText(getItem(position).getMoney() + "￥");
                String ps = getItem(position).getPosition();
//                String pss = ps.substring(ps.lastIndexOf("/"));

                ((TextView) viewHolder.getView(R.id.tv_wallet_posstion)).setText(ps.substring(ps.lastIndexOf("/") + 1));

                if (getItem(position).getEnoughDay() == "false") {
                    ((ImageView) viewHolder.getView(R.id.img_wallet_tuixian)).setImageResource(R.mipmap.tuixian_hui);
                }
                ((ImageView) viewHolder.getView(R.id.img_wallet_tuixian)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoveJob.WorkTokenBack(getItem(position).getPid(), new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                Utils.showToast(context, "退现成功");
                                adapter.removeAll();
                                addData();
                            }

                            @Override
                            public void onError(String msg) {
                                Utils.showToast(context, msg);
                            }
                        });
                    }
                });
                return viewHolder.getConvertView();
            }
        };
        lvWalletToken.setAdapter(adapter);

        lvWalletToken.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(context, Aty_TokenDetails.class);
                intent.putExtra("worktokenpid", adapter.getItem(position - 1).getPid());
                intent.putExtra("workPid", adapter.getItem(position - 1).getWorkPid());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume_() {
        adapter.removeAll();
        addData();
    }

    @Override
    public void onDestroy_() {
        if (call_getMyToken != null && call_getMyToken.isCanceled())
            call_getMyToken.cancel();
    }



    @OnClick(R.id.img_moneydetails_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }
}
