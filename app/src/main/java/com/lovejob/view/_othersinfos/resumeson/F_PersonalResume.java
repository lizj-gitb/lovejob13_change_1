package com.lovejob.view._othersinfos.resumeson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.adapter.PhotoAdapter;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.ms.MainActivityMs;
import com.lovejob.view.cityselector.cityselector.utils.ToastUtils;
import com.v.rapiddev.adpater.RecyclerItemClickListener;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Administrator on 2016/11/17.
 */
public class F_PersonalResume extends BaseFragment {
    View view;
    View view1;
    String usePid, useType,userName;




    private ThePerfectGirl.ResumeDTO girl;
    private TextView tvInforName,tvInforSex,tvInforHeight,tvInforAge,tvInforAddress,tvInforEducation,tvInforMajor,
            tvInforSchool,tvInforExperience,tvInforIndustry,tvInforSkill,tvInforWorke,tvInforMine;
    private TextView tvCommInfoBusiness,tvCommInfoWebsite,tvCommInfoCommcode,tvCommInfoScale,tvCommInfoAddress,tvCommInfoName;
    private RecyclerView gvCommpanyInfoImg;
    private CircleImageView imgUserHead;
    private ImageView buttonchat,buttonguanzhu;


    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        useType = getActivity().getIntent().getStringExtra("userType");
        usePid = getActivity().getIntent().getStringExtra("userId");

        if (useType.equals("0")) {
            view1 = inflater.inflate(R.layout.item_vp_resume, null);
            tvInforName = (TextView) view1.findViewById(R.id.tv_infor_name);
            tvInforSex = (TextView) view1.findViewById(R.id.tv_infor_sex);
            tvInforHeight = (TextView) view1.findViewById(R.id.tv_infor_height);
            tvInforAge = (TextView) view1.findViewById(R.id.tv_infor_age);
            tvInforAddress = (TextView) view1.findViewById(R.id.tv_infor_address);
            tvInforEducation = (TextView) view1.findViewById(R.id.tv_infor_education);
            tvInforMajor = (TextView) view1.findViewById(R.id.tv_infor_major);
            tvInforSchool = (TextView) view1.findViewById(R.id.tv_infor_school);
            tvInforExperience = (TextView) view1.findViewById(R.id.tv_infor_experience);
            tvInforIndustry = (TextView) view1.findViewById(R.id.et_infor_industry);
            tvInforSkill = (TextView) view1.findViewById(R.id.et_infor_skill);
            tvInforWorke = (TextView) view1.findViewById(R.id.et_infor_worke);
            tvInforMine = (TextView) view1.findViewById(R.id.tv_infor_mine);
            imgUserHead = (CircleImageView) view1.findViewById(R.id.img_user_head);
            buttonchat = (ImageView) view1.findViewById(R.id.img_phone);
            buttonguanzhu = (ImageView)view1.findViewById(R.id.img_guanzhu);
             addData();
            setonClickListener();
            return view1;

        }
        view = inflater.inflate(R.layout.item_vp_resumecompany, null);
        tvCommInfoBusiness = (TextView) view.findViewById(R.id.et_comm_info_business);
        tvCommInfoWebsite = (TextView) view.findViewById(R.id.et_comm_info_website);
        tvCommInfoCommcode = (TextView) view.findViewById(R.id.et_comm_info_commcode);
        tvCommInfoScale = (TextView) view.findViewById(R.id.et_comm_info_scale);
        tvCommInfoAddress = (TextView) view.findViewById(R.id.et_comm_info_address);
        tvCommInfoName = (TextView) view.findViewById(R.id.et_comm_info_name);
         gvCommpanyInfoImg =(RecyclerView)view.findViewById(R.id.gv_commpany_info_img);

        addComData();

        return view;

    }

    private void setonClickListener() {
        buttonchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usePid != null && !TextUtils.isEmpty(usePid)) {
                    //打开单聊对话界面
                    if (!StaticParams.isConnectChetService) {
                        ToastUtils.showToast(context, "您未连接到聊天服务器，可能是网络异常，请退出重新登录");
                        return;
                    }
                    startActivity(MainActivityMs.mIMKit.getChattingActivityIntent(usePid));
                } else {
                    Utils.showToast(context, "请重新登录后再试");
                    V.e("用户Id未获取成功");
                }
            }
        });
    }

    private void addComData() {
        LoveJob.getResumeList(usePid, new OnAllParameListener() {
            @Override
            public void onSuccess(final ThePerfectGirl thePerfectGirl) {
                girl = thePerfectGirl.getData().getResumeDTO();
                if (girl==null)return;
                tvCommInfoName.setText(girl.getName());
                tvCommInfoAddress.setText(girl.getAddress());
                tvCommInfoScale.setText(girl.getScale());
                tvCommInfoCommcode.setText(girl.getOrganizationCode());
                tvCommInfoWebsite.setText(girl.getWebsite());
                tvCommInfoBusiness.setText(girl.getMainBusiness());
                if (!TextUtils.isEmpty(thePerfectGirl.getData().getResumeDTO().getBusinessLicense()) && thePerfectGirl.getData().getResumeDTO().getBusinessLicense() != null) {
//                    gvCommpanyInfoImg.setVisibility(View.VISIBLE);
                    final ArrayList<String> selectedPhotos = new ArrayList<>();
                    String[] l = thePerfectGirl.getData().getResumeDTO().getBusinessLicense().split("\\|");
                    for (int i = 0; i < l.length; i++) {
                        selectedPhotos.add(StaticParams.ImageURL + l[i]+"!logo");
                    }
                    final PhotoAdapter photoAdapter = new PhotoAdapter(context, selectedPhotos, true);
                    gvCommpanyInfoImg.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
                    gvCommpanyInfoImg.setAdapter(photoAdapter);
                    gvCommpanyInfoImg.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position1) {

                            final ArrayList<String> photos = new ArrayList<>();
                            String[] l = thePerfectGirl.getData().getResumeDTO().getBusinessLicense().split("\\|");
                            for (int i = 0; i < l.length; i++) {
                                photos.add(StaticParams.ImageURL + l[i]+"!logo");
                            }
                            PhotoPreview.builder()
                                    //http://oejyij5hl.bkt.clouddn.com/lovejob_195107463214808463580161563989879.jpg
                                    .setPhotos(photos)
                                    .setCurrentItem(position1)
                                    .setShowDeleteButton(false)
                                    .start(context);

                        }
                    }));
                }
            }
            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });

    }

    private void addData() {
        LoveJob.getResumeList(usePid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getResumeDTO() != null) {
                    girl = thePerfectGirl.getData().getResumeDTO();
                    userName = girl.getName();
                    tvInforName.setText(girl.getName());
                    tvInforSex.setText(girl.getSex() == 0 ? "女" : "男");
                    tvInforHeight.setText(girl.getHeight() + "cm");
                    tvInforAge.setText(girl.getAge() + "".trim());
                    tvInforAddress.setText(girl.getAddress());
                    tvInforEducation.setText(girl.getEducation());
                    tvInforMajor.setText(girl.getMajor());
                    tvInforSchool.setText(girl.getSchool());
                    tvInforExperience.setText(girl.getEducationExperience());
                    tvInforIndustry.setText(girl.getIndustryDirection());
                    tvInforSkill.setText(girl.getSkill());
                    tvInforWorke.setText(girl.getExperience());
                    tvInforMine.setText(girl.getPersonalEvaluation());
                    Glide.with(context).load(StaticParams.ImageURL+thePerfectGirl.getData().getResumeDTO().getPortraitId()+"!logo").dontAnimate().placeholder(R.drawable.ic_launcher).into(imgUserHead);
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }






}
