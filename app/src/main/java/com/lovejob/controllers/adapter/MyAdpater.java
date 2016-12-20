package com.lovejob.controllers.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:Lovejob_Android
 * Package_Name:com.lovejob_android.adpater_vp_ft
 * Created on 2016-09-30 14:58
 */

public class MyAdpater extends FragmentPagerAdapter {
    //继承FragmentPagerAdapter类 ,并自定义的构造器
    private List<Fragment> fragments;

    public MyAdpater(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {


        return fragments.get(position);

    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }



}