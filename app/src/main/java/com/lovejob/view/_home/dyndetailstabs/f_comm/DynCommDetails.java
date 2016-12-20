package com.lovejob.view._home.dyndetailstabs.f_comm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ClassType:动态评论的回复列表
 * User:wenyunzhao
 * ProjectName:Lovejob_Android
 * Package_Name:com.lovejob.v.trunk.modular_home.dyndetailstabs.f_comm
 * Created on 2016-11-18 12:03
 */

public class DynCommDetails extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.iv_dyncommitemlist_userlogo)
    CircleImageView ivDyncommitemlistUserlogo;
    @Bind(R.id.tv_dyncommitemlist_username)
    TextView tvDyncommitemlistUsername;
    @Bind(R.id.iv_dyncommitemlist_good)
    ImageView ivDyncommitemlistGood;
    @Bind(R.id.tv_dyncommitemlist_good)
    TextView tvDyncommitemlistGood;
    @Bind(R.id.lt_dyncommitemlist_good)
    LinearLayout ltDyncommitemlistGood;
    @Bind(R.id.tv_dyncommitemlist_content)
    TextView tvDyncommitemlistContent;
    @Bind(R.id.tv_dyncommitemlist_time)
    TextView tvDyncommitemlistTime;
    @Bind(R.id.tv_dyncommitemlist_resend)
    TextView tvDyncommitemlistResend;
    @Bind(R.id.lv_dyncommitemlist)
    ListView lvDyncommitemlist;
    @Bind(R.id.tv_isdefault)
    TextView tvIsdefault;
    @Bind(R.id.et_dyncommitemlist_edittext)
    EditText etDyncommitemlistEdittext;
    @Bind(R.id.tv_dyncommitemlist_emoy)
    TextView tvDyncommitemlistEmoy;
    @Bind(R.id.tv_dyncommitemlist_reComm)
    TextView tvDyncommitemlistReComm;

    private String dynamicCommentPid, name;
    private Activity context;
    private FastAdapter<ThePerfectGirl.DynamicCommentDetailDTO> adapter;
    private boolean isAddMainData = true;//是否添加评论头部
    private String userid;
    private Call call_toDynCommListGood, call_getCommListItem_Resendlist, call_sendReComm;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.dyncommitemlist);
        ButterKnife.bind(this);
        context = this;
        dynamicCommentPid = getIntent().getStringExtra("dynamicCommentPid");
        name = getIntent().getStringExtra("name");
        userid = getIntent().getStringExtra("uid");

        if (TextUtils.isEmpty(dynamicCommentPid)) {
            Utils.showToast(context, "未接收到相关评论ID");
            return;
        }
        setActionbar();
        /**
         * 初始化适配器
         */
        initAdapter();
    }

    private void setActionbar() {
        actionbarTitle.setText("评论详情");
        actionbarSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvIsdefault.setVisibility(View.GONE);
                lvDyncommitemlist.setVisibility(View.VISIBLE);
                String content = etDyncommitemlistEdittext.getText().toString();
                call_sendReComm = LoveJob.sendReComm(dynamicCommentPid, content, userid, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        Utils.showToast(context, "回复成功");
                        etDyncommitemlistEdittext.setText("");
                        adapter.removeAll();
                        addDataTolistview();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                });
            }
        });
    }

    private void initAdapter() {
        adapter = new FastAdapter<ThePerfectGirl.DynamicCommentDetailDTO>(context, R.layout.item_dyndetails_commentlist_2) {
            @Override
            public View getViewHolder(final int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_dyndetails_commentlist_2, position);
                CircleImageView iv_dyncommlist_userlogo = (CircleImageView) viewHolder.getView(R.id.iv_dyncommlist_userlogo);
                Glide.with(context).load(StaticParams.QiNiuYunUrl + getItem(position).getPortraitid()).into(iv_dyncommlist_userlogo);
//                MyApplication.imageloader.displayImage(StaticParam.QiNiuYunUrl + getItem(position).getPortraitid(), iv_dyncommlist_userlogo,
//                        ImageMode.DefaultImage, true, true);
                //回复人姓名
                ((TextView) viewHolder.getView(R.id.name1)).setText(getItem(position).getRealName());
                //被回复人姓名
                ((TextView) viewHolder.getView(R.id.tv_dyncommlist_username)).setText(name);

                //回复内容点赞按钮
                ((LinearLayout) viewHolder.getView(R.id.lt_dyncommlist_good)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        V.d("请求给动态：" + getItem(position).getRealName() + "点赞");
                        sendGood(getItem(position).getCommentDetailPid(), false);
                    }
                });
                ImageView iv_dyncommlist_good = ((ImageView) viewHolder.getView(R.id.iv_dyncommlist_good));
                //自己是否点赞
                if (getItem(position).getIsPointGood()) {
                    iv_dyncommlist_good.setImageResource(R.mipmap.icon_good_on);
                } else {
                    iv_dyncommlist_good.setImageResource(R.mipmap.icon_good_common);
                }
                //点赞数量
                ((TextView) viewHolder.getView(R.id.tv_dyncommlist_good)).setText(String.valueOf(getItem(position).getCount()));
                //内容
                ((TextView) viewHolder.getView(R.id.tv_dyncommlist_content)).setText(getItem(position).getDetailContent());
                ((LinearLayout) viewHolder.getView(R.id.isInview)).setVisibility(View.INVISIBLE);
                return viewHolder.getConvertView();
            }
        };
        lvDyncommitemlist.setAdapter(adapter);
    }

    private void sendGood(String dynId, final boolean isAddMainData_) {
        call_toDynCommListGood = LoveJob.toDynCommListGood(dynId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                isAddMainData = isAddMainData_;
                adapter.removeAll();
                addDataTolistview();
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, "点赞失败，" + msg);
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void addDataTolistview() {
        call_getCommListItem_Resendlist = LoveJob.getCommListItem_Resendlist(dynamicCommentPid, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                ThePerfectGirl.UserInfoDTO uInfo = thePerfectGirl.getData().getDynamicCommentDTO().getReleaseInfo();
                ThePerfectGirl.DynamicCommentDTO commDto = thePerfectGirl.getData().getDynamicCommentDTO();
                List<ThePerfectGirl.DynamicCommentDetailDTO> replyDto = thePerfectGirl.getData().getDynamicCommentDTO().getReplyList();
                if (isAddMainData) {
                    //主评论的用户头像
                    Glide.with(context).load(StaticParams.QiNiuYunUrl + uInfo.getPortraitId()).into(ivDyncommitemlistUserlogo);

                    //用户姓名
                    tvDyncommitemlistUsername.setText(uInfo.getRealName());
                    //是否点赞
                    if (commDto.isPointGood()) {
                        ivDyncommitemlistGood.setImageResource(R.mipmap.icon_good_on);
                    } else {
                        ivDyncommitemlistGood.setImageResource(R.mipmap.icon_good_common);
                    }
                    //点赞数量
                    tvDyncommitemlistGood.setText(String.valueOf(commDto.getCount()));
                    //评论内容
                    tvDyncommitemlistContent.setText(commDto.getCommentContent());
                    //友好时间
                    tvDyncommitemlistTime.setText(commDto.getCreateDateDec());
                    isAddMainData = false;
                }
                if (replyDto == null) {
                    lvDyncommitemlist.setVisibility(View.GONE);
                    tvIsdefault.setVisibility(View.VISIBLE);
                    return;
                } else {
                    lvDyncommitemlist.setVisibility(View.VISIBLE);
                    tvIsdefault.setVisibility(View.GONE);
                    for (int i = 0; i < replyDto.size(); i++) {
                        adapter.addItem(replyDto.get(i));
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        });
    }

    @Override
    public void onResume_() {
        adapter.removeAll();
        addDataTolistview();
    }

    @Override
    public void onDestroy_() {
        if (call_toDynCommListGood != null && !call_toDynCommListGood.isCanceled())
            call_toDynCommListGood.cancel();
        if (call_getCommListItem_Resendlist != null && !call_getCommListItem_Resendlist.isCanceled())
            call_getCommListItem_Resendlist.cancel();
        if (call_sendReComm != null && !call_sendReComm.isCanceled())
            call_sendReComm.cancel();

    }

    @OnClick({R.id.actionbar_back, R.id.iv_dyncommitemlist_userlogo, R.id.lt_dyncommitemlist_good, R.id.tv_dyncommitemlist_emoy, R.id.tv_dyncommitemlist_reComm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(context);
                break;
            case R.id.iv_dyncommitemlist_userlogo:
//                Utils.showToast(context, "用户个人中心");
                break;
            case R.id.lt_dyncommitemlist_good:
                Utils.showToast(context, "点赞");
                sendGood(dynamicCommentPid, true);
                break;
            case R.id.tv_dyncommitemlist_emoy:
                Utils.showToast(context, "敬请期待");
                break;
            case R.id.tv_dyncommitemlist_reComm:
                tvIsdefault.setVisibility(View.GONE);
                lvDyncommitemlist.setVisibility(View.VISIBLE);
                String content = etDyncommitemlistEdittext.getText().toString();
                call_sendReComm = LoveJob.sendReComm(dynamicCommentPid, content, userid, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        Utils.showToast(context, "回复成功");
                        etDyncommitemlistEdittext.setText("");
                        adapter.removeAll();
                        addDataTolistview();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                });
                break;
        }
    }
}
