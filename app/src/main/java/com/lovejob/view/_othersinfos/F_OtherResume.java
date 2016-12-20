package com.lovejob.view._othersinfos;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.adapter.MyAdpater;
import com.lovejob.view.MyViewPager;
import com.lovejob.view._othersinfos.resumeson.F_EverWork;
import com.lovejob.view._othersinfos.resumeson.F_FriendImpression;
import com.lovejob.view._othersinfos.resumeson.F_PersonalResume;
import com.lovejob.view._othersinfos.resumeson.F_ShowWorks;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/17.
 */

public class F_OtherResume extends BaseFragment {
    View view;
    @Bind(R.id.tv_f_resume_basics)
    TextView tvFResumeBasics;
    @Bind(R.id.rl_resume_tab1)
    RelativeLayout rlResumeTab1;
    @Bind(R.id.tv_f_resume_show)
    TextView tvFResumeShow;
    @Bind(R.id.rl_resume_tab2)
    RelativeLayout rlResumeTab2;
    @Bind(R.id.tv_f_resume_friend)
    TextView tvFResumeFriend;
    @Bind(R.id.rl_resume_tab3)
    RelativeLayout rlResumeTab3;
    @Bind(R.id.tv_f_resume_already)
    TextView tvFResumeAlready;
    @Bind(R.id.rl_resume_tab4)
    RelativeLayout rlResumeTab4;
    @Bind(R.id.vp_other_resume_fragment)
    MyViewPager vpOtherResumeFragment;
    private ArrayList<Fragment> fragments;
    private ArrayList<TextView> textViews;
    private MyAdpater myAdpater;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_other_resume, null);

        ButterKnife.bind(this, view);
        initFragMent();
        return view;
    }

    @Override
    public void loadData() {

    }

    private void initFragMent() {
        fragments = new ArrayList<Fragment>();
        Fragment Tab_1 = new F_PersonalResume();
        Fragment Tab_2 = new F_ShowWorks();
        Fragment Tab_3 = new F_FriendImpression();
        Fragment Tab_4 = new F_EverWork();
        fragments.add(Tab_1);
        fragments.add(Tab_2);
        fragments.add(Tab_3);
        fragments.add(Tab_4);
        textViews = new ArrayList<TextView>();
        textViews.add(tvFResumeBasics);
        textViews.add(tvFResumeShow);
        textViews.add(tvFResumeFriend);
        textViews.add(tvFResumeAlready);
        myAdpater = new MyAdpater(getActivity().getSupportFragmentManager(),fragments);
        vpOtherResumeFragment.setAdapter(myAdpater);
        vpOtherResumeFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int item = vpOtherResumeFragment.getCurrentItem();
                switch (item){
                    case 0:
                        setSelect(0);
                        break;
                    case 1:
                        setSelect(1);
                        break;
                    case 2:
                        setSelect(2);
                        break;
                    case 3:
                        setSelect(3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_resume_tab1, R.id.rl_resume_tab2, R.id.rl_resume_tab3, R.id.rl_resume_tab4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_resume_tab1:
                setSelect(0);
                break;
            case R.id.rl_resume_tab2:
                setSelect(1);
                break;
            case R.id.rl_resume_tab3:
                setSelect(2);
                break;
            case R.id.rl_resume_tab4:
                setSelect(3);
                break;
        }
    }

    private void setSelect(int i) {

        for (int j = 0; j <textViews.size() ; j++) {

            if (i != j) {
                textViews.get(j).setTextColor(Color.parseColor("#969696"));
            } else {
                textViews.get(j).setTextColor(Color.parseColor("#50b8f1"));
            }
        }
        vpOtherResumeFragment.setCurrentItem(i);
    }
}
