package com.lovejob;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lovejob.model.StaticParams;
import com.umeng.analytics.MobclickAgent;
import com.v.rapiddev.dialogs.core.MaterialDialog;
import com.v.rapiddev.http.okhttp3.Call;

import java.util.ArrayList;
import java.util.List;


/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob_Android
 * Package_Name:com.lovejob_android
 * Created on 2016-10-03 09:43
 */

public abstract class BaseFragment extends Fragment {
    public Activity context;
    public MaterialDialog dialog;
    public List<Call> callList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        StaticParams.ROWS = "5";
        return initView(inflater, container, savedInstanceState);
    }

    public abstract View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadData();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPause(getActivity());
        System.gc();
        System.runFinalization();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Call calls : callList) {
            if (calls != null && !calls.isCanceled()) calls.cancel();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            loadData();
//        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 进入该方法时表示当前fragment是可见的，可以加载用户数据 注意：请做标示，当数据加载完一次后第二次不去加载
     */
    public abstract void loadData();
}
