package com.lovejob.view._job;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view.WriteView;
import com.lovejob.view.payinfoviews.PayView;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyGirdView;
import com.v.rapiddev.views.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imageloader.utils.L;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.lovejob.model.StaticParams.RequestCode.RequestCode_JobDetailsReComm_WriteView;

public class JobDetails extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
    @Bind(R.id.tv_jobdetails_title)
    TextView tvJobdetailsTitle;
    @Bind(R.id.tv_jobdetails_company)
    TextView tvJobdetailsCompany;
    @Bind(R.id.tv_jobdetails_boss)
    TextView tvJobdetailsBoss;
    @Bind(R.id.img_jobdetails_boos_head)
    CircleImageView imgJobdetailsBoosHead;
    @Bind(R.id.tv_jobdetails_data)
    TextView tvJobdetailsData;
    @Bind(R.id.tv_jobdetails_data_time)
    TextView tvJobdetailsDataTime;
    @Bind(R.id.tv_jobdetails_worktime)
    TextView tvJobdetailsWorktime;
    @Bind(R.id.tv_jobdetails_address)
    TextView tvJobdetailsAddress;
    @Bind(R.id.tv_jobdetails_count)
    TextView tvJobdetailsCount;
    @Bind(R.id.tv_jobdetails_sex)
    TextView tvJobdetailsSex;
    @Bind(R.id.tv_jobdetails_experience)
    TextView tvJobdetailsExperience;
    @Bind(R.id.tv_jobdetails_education)
    TextView tvJobdetailsEducation;
    @Bind(R.id.tv_jobdetails_skill)
    TextView tvJobdetailsSkill;
    @Bind(R.id.tv_jobdetails_content)
    TextView tvJobdetailsContent;
    @Bind(R.id.tv_jobdetails_price)
    TextView tvJobdetailsPrice;
    @Bind(R.id.tv_jobdetails_mode)
    TextView tvJobdetailsMode;
    @Bind(R.id.tv_jobdetails_phone)
    TextView tvJobdetailsPhone;
    @Bind(R.id.alreadySignPersonNumber)
    TextView alreadySignPersonNumber;
    @Bind(R.id.alreadySignText)
    TextView alreadySignText;
    @Bind(R.id.tv_jobdetails_surplus1)
    TextView tvJobdetailsSurplus1;
    @Bind(R.id.tv_jobdetails_surplus2)
    TextView tvJobdetailsSurplus2;
    @Bind(R.id.tv_jobdetails_surplus3)
    TextView tvJobdetailsSurplus3;
    @Bind(R.id.token_up)
    LinearLayout tokenUp;
    @Bind(R.id.tv_jobdetails_money)
    TextView tvJobdetailsMoney;
    @Bind(R.id.tv_jobdetails_surplus4)
    TextView tvJobdetailsSurplus4;
    @Bind(R.id.tv_jobdetails_surplus5)
    TextView tvJobdetailsSurplus5;
    @Bind(R.id.tv_jobdetails_surplus6)
    TextView tvJobdetailsSurplus6;
    @Bind(R.id.bt_jobdetails_own_lingpai)
    RelativeLayout btJobdetailsOwnLingpai;
    @Bind(R.id.lv_jobdetails_comm)
    MyListView lvJobdetailsComm;
    @Bind(R.id.defaluttext)
    TextView defaluttext;
    @Bind(R.id.tv_jobdetails_tocomm)
    TextView tvJobdetailsTocomm;
    @Bind(R.id.img_jobdetails_chat)
    ImageView imgJobdetailsChat;
    @Bind(R.id.img_jobdetails_call)
    ImageView imgJobdetailsCall;
    @Bind(R.id.img_jobdetails_apply)
    ImageView imgJobdetailsApply;
    @Bind(R.id.bt_jobdetails_others)
    RelativeLayout btJobdetailsOthers;
    @Bind(R.id.img_jobdetails_buy)
    ImageView imgJobdetailsBuy;
    @Bind(R.id.buytoken)
    RelativeLayout buytoken;
    @Bind(R.id.gv_jobdetails_alreadysiginperson)
    MyGirdView gv_jobdetails_alreadysiginperson;
    @Bind(R.id.sv_job)
    PullToRefreshScrollView sv_job;
    private String workId;
    private String userId, userName;
    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter_commlist;//评论列表
    private String phoneNumber;
    boolean isGetWork = false;//是否可以报名
    boolean isPhone = false;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.jobdetails_2);
        ButterKnife.bind(this);
        getDefultParams();
        V.d("workId:" + workId);
        initAdapter();
        setOnCommListItemClickListener();
        //获取详情数据
        getData();
        //获取该工作评论列表
        getCommList();
        setRefreshListener();
        //当进入页面是，头像象限获得焦点防止页面自动下跳
        imgJobdetailsBoosHead.setFocusable(true);
        imgJobdetailsBoosHead.setFocusableInTouchMode(true);
    }

    private void setRefreshListener() {
        sv_job.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        sv_job.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //TODo 下拉刷新
                adapter_commlist.removeAll();
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    private void setOnCommListItemClickListener() {
        lvJobdetailsComm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                V.d(adapter_commlist.getItem(i).getObserverName());
                Intent intent = new Intent(context, WriteView.class);
                intent.putExtra("title", "回复" + adapter_commlist.getItem(i).getObserverName() + "的评论");
                intent.putExtra("content", "");
                intent.putExtra("requestCode", RequestCode_JobDetailsReComm_WriteView);
                intent.putExtra("writeType", -1);
                intent.putExtra("observerPid", adapter_commlist.getItem(i).getObserverPid());
                intent.putExtra("questionPid", adapter_commlist.getItem(i).getQuestionPid());

                startActivityForResult(intent, RequestCode_JobDetailsReComm_WriteView);
            }
        });
    }

    private void initAdapter() {
        adapter_commlist = new FastAdapter<ThePerfectGirl.WorkInfoDTO>(context, R.layout.item_jobcomm_lv) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_jobcomm_lv, position);
                ((TextView) viewHolder.getView(R.id.tv_name_comm)).setText(getItem(position).getObserverName() + ":");
                ((TextView) viewHolder.getView(R.id.tv_content_comm)).setText(getItem(position).getObserverContent());

                //TODO 回复列表没加载
                LinearLayout lt_recomm = (LinearLayout) viewHolder.getView(R.id.lt_recomm);
                if (TextUtils.isEmpty(getItem(position).getAnswerName()) || TextUtils.isEmpty(getItem(position).getAnswerContent())) {
                    lt_recomm.setVisibility(View.GONE);
                } else {
                    lt_recomm.setVisibility(View.VISIBLE);
                    ((TextView) viewHolder.getView(R.id.tv_name_recomm)).setText(getItem(position).getAnswerName());
                    ((TextView) viewHolder.getView(R.id.tv_content_recomm)).setText("回复：" + getItem(position).getAnswerContent());
                }
                return viewHolder.getConvertView();
            }
        };
        lvJobdetailsComm.setAdapter(adapter_commlist);
    }

    private void getCommList() {
        callList.add(LoveJob.getWorkCommList(workId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                List<ThePerfectGirl.WorkInfoDTO> workinfodtos = thePerfectGirl.getData().getWorkInfoDTOs();
                if (workinfodtos != null && workinfodtos.size() > 0) {
                    defaluttext.setVisibility(View.GONE);
                    lvJobdetailsComm.setVisibility(View.VISIBLE);
                    for (int i = 0; i < workinfodtos.size(); i++) {
                        adapter_commlist.addItem(workinfodtos.get(i));
                    }
                    adapter_commlist.notifyDataSetChanged();
                } else {
                    defaluttext.setVisibility(View.VISIBLE);
                    lvJobdetailsComm.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String msg) {

            }
        }));
    }

    private void getData() {
        callList.add(LoveJob.getJobDetails(workId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                sv_job.onRefreshComplete();
                phoneNumber = thePerfectGirl.getData().getWorkInfoDTO().getContactPhone();
                userName = thePerfectGirl.getData().getWorkInfoDTO().getReleaseInfo().getRealName();
                isPhone = thePerfectGirl.getData().getWorkInfoDTO().isPhone();
                if (thePerfectGirl.getData().getWorkInfoDTO().getReleaseInfo().getUserId().equals(new AppPreferences(context).getString(StaticParams.FileKey.__USERPID__, ""))) {
                    //可以购买
                    buytoken.setVisibility(View.VISIBLE);
                    btJobdetailsOthers.setVisibility(View.GONE);
                    btJobdetailsOwnLingpai.setVisibility(View.VISIBLE);

                } else {
                    //不可申请
                    buytoken.setVisibility(View.GONE);
                    btJobdetailsOthers.setVisibility(View.VISIBLE);
                    btJobdetailsOwnLingpai.setVisibility(View.GONE);
                }
                Glide.with(context).load(StaticParams.QiNiuYunUrl + thePerfectGirl.getData().getWorkInfoDTO().getReleaseInfo().getPortraitId()).dontAnimate()
                        .placeholder(R.drawable.ic_launcher).into(imgJobdetailsBoosHead);
                userId = thePerfectGirl.getData().getWorkInfoDTO().getReleaseInfo().getUserId();
                actionbarSave.setVisibility(View.GONE);
                actionbarTitle.setText(thePerfectGirl.getData().getWorkInfoDTO().getTitle());
                actionbarTitle.setTextSize(16);
                actionbarTitle.setTextColor(Color.WHITE);
                tvJobdetailsTitle.setText(thePerfectGirl.getData().getWorkInfoDTO().getTitle());
                tvJobdetailsAddress.setText(thePerfectGirl.getData().getWorkInfoDTO().getReleaseInfo().getAddress());
                tvJobdetailsBoss.setText(thePerfectGirl.getData().getWorkInfoDTO().getReleaseInfo().getRealName());
                tvJobdetailsCompany.setText(thePerfectGirl.getData().getWorkInfoDTO().getReleaseInfo().getCompany());
                //TODO 返回时间
                String s1 = String.format("%tF%n", thePerfectGirl.getData().getWorkInfoDTO().getReleaseDate());
                String s2 = String.format("%tR%n", thePerfectGirl.getData().getWorkInfoDTO().getReleaseDate());
                tvJobdetailsData.setText(s1.substring(5, s1.length()));
                tvJobdetailsDataTime.setText(s2);
                tvJobdetailsWorktime.setText(thePerfectGirl.getData().getWorkInfoDTO().getWorkHours() + "".trim());
                tvJobdetailsAddress.setText(thePerfectGirl.getData().getWorkInfoDTO().getAddress());
                tvJobdetailsCount.setText(thePerfectGirl.getData().getWorkInfoDTO().getNumber() + "".trim());
                tvJobdetailsSex.setText(thePerfectGirl.getData().getWorkInfoDTO().getSexDec());
                tvJobdetailsExperience.setText(thePerfectGirl.getData().getWorkInfoDTO().getExperienceDec());
                tvJobdetailsEducation.setText(thePerfectGirl.getData().getWorkInfoDTO().getEducationDec());
                tvJobdetailsSkill.setText(thePerfectGirl.getData().getWorkInfoDTO().getSkill());
                tvJobdetailsContent.setText(thePerfectGirl.getData().getWorkInfoDTO().getContent());
                tvJobdetailsPrice.setText(thePerfectGirl.getData().getWorkInfoDTO().getSalary() + "/月");
//                tvJobdetailsMode.setText("/月");
//                tvJobdetailsMode.setText("/月" + thePerfectGirl.getData().getWorkInfoDTO().getPaymentDec());
//                tvJobdetailsPhone.setText(thePerfectGirl.getData().getWorkInfoDTO().getContactPhone() + "".trim());
                String s = thePerfectGirl.getData().getWorkInfoDTO().getContactPhone();
                String s3 = s.substring(0, s.length() - 4);
                tvJobdetailsPhone.setText(s3+"****");
                if (thePerfectGirl.getData().getWorkInfoDTO().getShowApplyBtn() == 2) {
                    imgJobdetailsApply.setImageResource(R.mipmap.button_focus_suspend_cancle1);
                    isGetWork = false;
                } else {
                    isGetWork = true;
                }
                int alreadySiginPersonNumber = 0;
                FastAdapter<String> adapter_alreadySiginPerson = new FastAdapter<String>(context, R.layout.item_jobdetails_alreadysiginperson) {
                    @Override
                    public View getViewHolder(int position, View convertView, ViewGroup parent) {
                        FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_jobdetails_alreadysiginperson, position);
                        Glide.with(context).load(getItem(position)).placeholder(R.drawable.ic_launcher).into((CircleImageView) viewHolder.getView(R.id.iv_item_jobdetails_gridview));
                        return viewHolder.getConvertView();
                    }
                };

                gv_jobdetails_alreadysiginperson.setAdapter(adapter_alreadySiginPerson);

                List<ThePerfectGirl.UserInfoDTO> person = thePerfectGirl.getData().getWorkInfoDTO().getEmployeeInfo();
                setGridView(adapter_alreadySiginPerson, gv_jobdetails_alreadysiginperson, person.size());
                for (int i = 0; i < person.size(); i++) {
                    alreadySiginPersonNumber++;
                    //gv添加一个item
                    adapter_alreadySiginPerson.addItem(StaticParams.QiNiuYunUrl + person.get(i).getPortraitId());
                }
                alreadySignPersonNumber.setText(String.valueOf(alreadySiginPersonNumber));

                //悬赏令
                List<ThePerfectGirl.WorkTokenDTO> rewardTokens = thePerfectGirl.getData().getWorkInfoDTO().getRewardToken();
                if (rewardTokens == null || rewardTokens.size() == 0 || rewardTokens.get(0) == null) {
                    //设置悬赏令的数量和金额为0
                    tvJobdetailsMoney.setText("0元/个");
                    tvJobdetailsSurplus4.setText("0");
                } else {
                    tvJobdetailsMoney.setText(rewardTokens.get(0).getMoney() + "元/个");
                    tvJobdetailsSurplus4.setText(rewardTokens.get(0).getCount());
                }

                //江湖令
                List<Integer> riveToken = thePerfectGirl.getData().getWorkInfoDTO().getRiveToken();
                if (riveToken == null || riveToken.size() == 0) {
                    //设置每一个江湖令的数量为0  金额为固定值
                    //1元
                    tvJobdetailsSurplus1.setText("0");
                    //10元
                    tvJobdetailsSurplus2.setText("0");
                    //100元
                    tvJobdetailsSurplus3.setText("0");
                    //1000元
                    tvJobdetailsSurplus5.setText("0");
                    //10000元
                    tvJobdetailsSurplus6.setText("0");
                } else {
                    //1元
                    tvJobdetailsSurplus1.setText(String.valueOf(riveToken.get(0)));
                    //10元
                    tvJobdetailsSurplus2.setText(String.valueOf(riveToken.get(1)));
                    //100元
                    tvJobdetailsSurplus3.setText(String.valueOf(riveToken.get(2)));
                    //1000元
                    tvJobdetailsSurplus5.setText(String.valueOf(riveToken.get(3)));
                    //10000元
                    tvJobdetailsSurplus6.setText(String.valueOf(riveToken.get(4)));
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        }));
    }

    private void getDefultParams() {
        workId = getIntent().getStringExtra("workId");
    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView(BaseAdapter adapter, GridView gridView, int size) {
        V.d("size:" + size);
        int length = 30;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数
//        if (siz < 9) {
//            gridView.setVisibility(View.VISIBLE);
//        }
        gridView.setAdapter(adapter);
    }

    @OnClick({R.id.actionbar_back, R.id.img_jobdetails_chat, R.id.img_jobdetails_call, R.id.img_jobdetails_apply, R.id.img_jobdetails_buy, R.id.tv_jobdetails_tocomm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.img_jobdetails_chat:
                V.d("聊天");
                //打开单聊对话界面
                if (RongIM.getInstance() != null && userId != null && !TextUtils.isEmpty(userId)) {
                    RongIM.getInstance().startPrivateChat(this, userId, userName);
                } else {
                    Utils.showToast(context, "请重新登录后再试");
                    V.e("用户Id未获取成功");
                }
                break;
            case R.id.img_jobdetails_call:
                //启动聚合会话列表界面(暂时没用到)
//                if (RongIM.getInstance() != null)
//                    RongIM.getInstance().startSubConversationList(this, Conversation.ConversationType.GROUP);
                if (isPhone){
                    V.d("打电话");

                    Intent intent_phone = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + phoneNumber);
                    intent_phone.setData(data);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        V.e("用户权限缺失");
                        return;
                    }
                    startActivity(intent_phone);
                    break;
                }else {
                    Utils.showToast(context,"未录取前不能打电话");
                    break;
                }

            case R.id.img_jobdetails_apply:
                V.d("申请");
                doSignInOrCancel();

//                LoveJob.getGrabForm(workId, new OnAllParameListener() {
//                    @Override
//                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                        Utils.showToast(context, "操作成功");
//                        if (isGetWork) {
//                            imgJobdetailsApply.setImageResource(R.mipmap.button_focus_suspend_cancle1);
//                        } else {
//                            imgJobdetailsApply.setImageResource(R.mipmap.button_focus_suspend_off);
//                        }
//                        isGetWork = !isGetWork;
//                    }
//
//                    @Override
//                    public void onError(String msg) {
//                        Utils.showToast(context, msg);
//                    }
//                });
                //TODO
                break;
            case R.id.img_jobdetails_buy:
                V.d("购买令牌");
                Intent intent_buy = new Intent(context, PayView.class);
                intent_buy.putExtra("workPid", workId);
                startActivity(intent_buy);
                AppManager.getAppManager().finishActivity(context);
                break;

            case R.id.tv_jobdetails_tocomm:
//                tvJobdetailsTocomm.
//                  title = intent.getStringExtra("title");
//                content = intent.getStringExtra("content");
//                requestCode = intent.getIntExtra("requestCode", -1);
//                writeType = intent.getIntExtra("writeType", -1);
                Intent intent = new Intent(context, WriteView.class);
                intent.putExtra("title", "请输入评论/回复内容");
                intent.putExtra("content", "");
                intent.putExtra("writeType", -1);
                intent.putExtra("requestCode", StaticParams.RequestCode.RequestCode_JobDetails_WriteView);

                startActivityForResult(intent, StaticParams.RequestCode.RequestCode_JobDetails_WriteView);
                break;
        }
    }

    private void doSignInOrCancel() {
        dialog = Utils.showProgressDliago(context, isGetWork ? "正在为您报名,请稍后" : "正在取消申请");
        Map<String, String> map = new HashMap<String, String>();
        map.put("workPid", workId);
        map.put("isApply", isGetWork ? "true" : "false");
        callList.add(LoveJob.getGrabForm(workId, isGetWork, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                dialog.dismiss();
                if (isGetWork) {
                    imgJobdetailsApply.setImageResource(R.mipmap.button_focus_suspend_cancle1);
                } else {
                    imgJobdetailsApply.setImageResource(R.mipmap.button_focus_suspend_off);
                }
                isGetWork = !isGetWork;
                Utils.showToast(context, "操作成功");
//                adapter_commlist.removeAll();
                getData();
            }

            @Override
            public void onError(String msg) {
                dialog.dismiss();
                Utils.showToast(context, msg);
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case StaticParams.RequestCode.RequestCode_JobDetails_WriteView:
                V.d("从点评输入页面返回,发起评论内容为：" + data.getStringExtra("content"));
                callList.add(LoveJob.sendJobDetailsComm(workId, data.getStringExtra("content"), new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                        getData();
                        adapter_commlist.removeAll();
                        getCommList();
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                }));
                break;
            case RequestCode_JobDetailsReComm_WriteView:
                V.d("从输入回复页面返回,发起回复");
                callList.add(LoveJob.sendJobDetailsComm(workId, data.getStringExtra("observerPid"), data.getStringExtra("questionPid"), data.getStringExtra("content"), new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        V.d("回复成功");
                        adapter_commlist.removeAll();
                        getCommList();
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                }));
                break;
        }
    }
}