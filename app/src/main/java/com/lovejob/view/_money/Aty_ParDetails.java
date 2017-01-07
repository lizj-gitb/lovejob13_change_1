package com.lovejob.view._money;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseActivity;
import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.model.bean.Data_Comm_2_2;
import com.lovejob.ms.MainActivityMs;
import com.lovejob.view.WriteView;
import com.lovejob.view.cityselector.cityselector.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lovejob.model.StaticParams.RequestCode.RequestCode_OriWork_To_WriteView_WriteComm;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_OriWork_To_WriteView_WriteReComm;

/**
 * Created by Administrator on 2016/10/31.
 */
public class Aty_ParDetails extends BaseActivity {
    @Bind(R.id.img_par_back)
    ImageView imgParBack;
    @Bind(R.id.img_oridetails_userlogo)
    CircleImageView imgOridetailsUserVo;
    @Bind(R.id.tv_oridetails_username)
    TextView tvOridetailsUsername;
    @Bind(R.id.img_oridetails_userleavl)
    ImageView imgOridetailsUserleavl;
    @Bind(R.id.tv_oridetails_position)
    TextView tvOridetailsPosition;
    @Bind(R.id.tv_oridetails_title)
    TextView tvOridetailsTitle;
    @Bind(R.id.tv_pardetails_time)
    TextView tvPardetailsTime;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.tv_pardetails_location)
    TextView tvPardetailsLocation;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.tv_pardetails_count)
    TextView tvPardetailsCount;
    @Bind(R.id.tv_pardetails_sex)
    TextView tvPardetailsSex;
    @Bind(R.id.tv_pardetails_experience)
    TextView tvPardetailsExperience;
    @Bind(R.id.tv_pardetails_education)
    TextView tvPardetailsEducation;
    @Bind(R.id.tv_pardetails_skill)
    TextView tvPardetailsSkill;
    @Bind(R.id.tv_pardetails_content)
    TextView tvPardetailsContent;
    @Bind(R.id.tv_pardetails_countdown)
    TextView tvPardetailsCountdown;
    @Bind(R.id.tv_pardetails_number)
    TextView tvPardetailsNumber;
    @Bind(R.id.gv_oridetails_alreadySignPerson)
    GridView gvOridetailsAlreadySignPerson;
    @Bind(R.id.tv_pardetails_price)
    TextView tvPardetailsPrice;
    @Bind(R.id.tv_pardetails_phonenuber)
    TextView tvPardetailsPhonenuber;
    @Bind(R.id.lv_pardetails_comm)
    MyListView lvPardetailsComm;
    @Bind(R.id.tv_pardetails_tocomm)
    TextView tvPardetailsTocomm;
    @Bind(R.id.view_hite)
    View viewHite;
    @Bind(R.id.sv_aty_ori_main)
    PullToRefreshScrollView svAtyOriMain;
    @Bind(R.id.img_pardetails_chat)
    ImageView imgPardetailsChat;
    @Bind(R.id.img_pardetails_call)
    ImageView imgPardetailsCall;
    @Bind(R.id.img_pardetails_grad)
    ImageView imgPardetailsGrad;
    @Bind(R.id.hiteView)
    RelativeLayout hiteView;
    private String workId = null;
    private Activity context;
    private FastAdapter<String> adapter_contentImg, adapter_alreadySignInPersonImg;
    boolean isGetWork = false;//是否可以报名
    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter_comm;
    private FastAdapter<Data_Comm_2_2> adapter_comm_2;
    private String phoneNumber;
    private String userId, userName;
    private boolean isphone;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.aty_pardetails);
        workId = getIntent().getStringExtra("workId");
        if (workId == null) {
            Utils.showToast(this, "数据异常,请重新登陆");
            return;
        }
        ButterKnife.bind(this);
        context = this;
        if (!getIntent().getBooleanExtra("isEdit", false)) {
            hiteView.setVisibility(View.INVISIBLE);

        } else {
            hiteView.setVisibility(View.VISIBLE);

        }

        /**
         * 设置Sv相关属性
         */
        setSVInfos();
        /**
         * 初始化适配器
         */
        initAdapater();
        getDetails();
    }

    @Override
    public void onResume_() {
        MobclickAgent.onPageStart("SplashScreen"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);
    }

    @Override
    public void onDestroy_() {

    }

    private void setSVInfos() {
        svAtyOriMain.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        svAtyOriMain.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getDetails();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

    }

    private void getDetails() {
        Map<String, String> map = new HashMap<>();
        map.put("workPid", workId);

        callList.add(LoveJob.getWorkDetails(workId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                svAtyOriMain.onRefreshComplete();
                ThePerfectGirl.WorkInfoDTO workinfoDto = thePerfectGirl.getData().getWorkInfoDTO();
                ThePerfectGirl.UserInfoDTO userinfo = workinfoDto.getReleaseInfo();
                userId = userinfo.getUserId();
                userName = userinfo.getRealName();
                isphone = workinfoDto.isPhone();
                Glide.with(context).load(StaticParams.QiNiuYunUrl
                        + userinfo.getPortraitId()).placeholder(R.mipmap.ic_launcher).dontAnimate().into(imgOridetailsUserVo);
                tvOridetailsUsername.setText(userinfo.getRealName());
                //imgOridetailsUserleavl
                tvOridetailsPosition.setText(userinfo.getPosition());
                tvOridetailsTitle.setText(workinfoDto.getTitle());
                tvPardetailsTime.setText(workinfoDto.getWorkHours());
                tvPardetailsLocation.setText(workinfoDto.getAddress());
                tvPardetailsCount.setText(String.valueOf(workinfoDto.getNumber()));
                tvPardetailsSex.setText(workinfoDto.getSexDec());
                tvPardetailsExperience.setText(workinfoDto.getExperienceDec());
                tvPardetailsEducation.setText(workinfoDto.getEducationDec());
                tvPardetailsSkill.setText(workinfoDto.getSkill());
                tvPardetailsContent.setText(workinfoDto.getContent());
                if (workinfoDto.getShowApplyBtn() == 2) {
                    imgPardetailsGrad.setImageResource(R.mipmap.button_focus_suspend_cancle1);
                    isGetWork = false;
                } else {
                    isGetWork = true;
                }
                tvPardetailsCountdown.setText("  " + workinfoDto.getDeadlineDec());
                tvPardetailsNumber.setText("已有" + workinfoDto.getApplyCount() + "人抢单");
                //TODO 抢单人头像
                phoneNumber = workinfoDto.getContactPhone();
                tvPardetailsPrice.setText(workinfoDto.getSalary() + "元/" + workinfoDto.getPaymentDec());
//                tvPardetailsPhonenuber.setText(workinfoDto.getContactPhone());
                String s = workinfoDto.getContactPhone();
                String s3 = s.substring(0, s.length() - 4);
                tvPardetailsPhonenuber.setText(s3 + "****");
                //TODO 点评列表
                List<ThePerfectGirl.UserInfoDTO> lists = workinfoDto.getEmployeeInfo();
                int size = 0;
                if (lists != null) {
                    size = lists.size();
                    for (int i = 0; i < workinfoDto.getEmployeeInfo().size(); i++) {
                        adapter_alreadySignInPersonImg.addItem(StaticParams.QiNiuYunUrl + workinfoDto.getEmployeeInfo().get(i).getPortraitId());
                    }
                    adapter_alreadySignInPersonImg.notifyDataSetChanged();
                }
                setGridView(35, gvOridetailsAlreadySignPerson, adapter_alreadySignInPersonImg, size);
                getCommList();
            }

            @Override
            public void onError(String msg) {
                svAtyOriMain.onRefreshComplete();
            }
        }));
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView(int imgSize, GridView gridView, BaseAdapter adapter, int size) {
        int length = imgSize;
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
        if (size == 0) {
            gridView.setVisibility(View.GONE);
        } else {
            gridView.setVisibility(View.VISIBLE);
        }
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initAdapater() {
        /**
         * 初始化内容图片加载的gridview适配器
         */
//        initContentImgGridView();
        /**
         * 初始化已抢单人的头像gridview适配器
         */
        initAleadySignInPersonImgGridView();
        /**
         * 加载评论列表
         */
        initCommAdapater();

    }

    //    private String reSponseCommPid;
    private String reQuestPid;


    private void getCommList() {
        callList.add(LoveJob.getOriCommList(workId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData().getWorkInfoDTOs() != null) {
                    adapter_comm.removeAll();
                    for (int i = 0; i < thePerfectGirl.getData().getWorkInfoDTOs().size(); i++) {
                        adapter_comm.addItem(thePerfectGirl.getData().getWorkInfoDTOs().get(i));
                    }
                    adapter_comm.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
            }
        }));
    }

    /**
     * 初始化评论列表   包括二级列表
     */
    private void initCommAdapater() {
        //一级适配器
        adapter_comm = new FastAdapter<ThePerfectGirl.WorkInfoDTO>(context, R.layout.item_lv_oridetails_comm) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_oridetails_comm, position);
                LinearLayout linearLayout = viewHolder.getView(R.id.lt_oridetails_2_2);
                linearLayout.setVisibility(View.VISIBLE);
                MyListView myListView = viewHolder.getView(R.id.lv_oridetails_comm_2);
                initAdapater_2_2(myListView);
                ((TextView) viewHolder.getView(R.id.tv_item_name_oridetails)).setText(getItem(position).getObserverName() + ":");
                ((TextView) viewHolder.getView(R.id.tv_item_content_oridetails)).setText(getItem(position).getObserverContent());
                if (getItem(position).getAnswerName() != null && getItem(position).getAnswerContent() != null) {
                    V.d("谁知显示lt，加载数据评论列表——2的数据，刷新主次listview");
                    ((LinearLayout) viewHolder.getView(R.id.lt_oridetails_2_2)).setVisibility(View.VISIBLE);
                    adapter_comm_2.addItem(new Data_Comm_2_2(getItem(position).getAnswerName() + "", getItem(position).getAnswerContent() + ""));
                    adapter_comm_2.notifyDataSetChanged();
                    adapter_comm.notifyDataSetChanged();
                } else {
                    V.d("隐藏次listview的lt");
                    ((LinearLayout) viewHolder.getView(R.id.lt_oridetails_2_2)).setVisibility(View.GONE);
                }
                return viewHolder.getConvertView();
            }

            //2级适配器
            private void initAdapater_2_2(MyListView myListView) {
                adapter_comm_2 = new FastAdapter<Data_Comm_2_2>(context, R.layout.item_2_2) {
                    @Override
                    public View getViewHolder(int position, View convertView, ViewGroup parent) {
                        FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_2_2, position);
                        TextView tv_name = viewHolder.getView(R.id.tv_oridetails_2_2_name);
                        TextView tv_content = viewHolder.getView(R.id.tv_oridetails_2_2_content);
                        tv_name.setText(getItem(position).getAnswerName());
                        tv_content.setText("回复：" + getItem(position).getAnswerContent());
                        return viewHolder.getConvertView();
                    }
                };
                myListView.setAdapter(adapter_comm_2);
            }
        };
        lvPardetailsComm.setAdapter(adapter_comm);
        lvPardetailsComm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.showToast(context, "给" + adapter_comm.getItem(position).getObserverName() + "回复");
                reQuestPid = adapter_comm.getItem(position).getQuestionPid();
                reSendComm(adapter_comm.getItem(position).getObserverName());
//                Intent intent = new Intent(context, Aty_View_Write.class);
//                Bundle b = new Bundle();
//                b.putString("hite", "给" + adapter_comm.getItem(position).getObserverName() + "回复");
//                b.putInt("textSize", 50);
//                b.putInt("requestCode", StaticParams.RequestCode.RequsetCode_Comm_ToWrite);
//                b.putString("context", "");
//
//                intent.putExtra("data", b);
//                startActivityForResult(intent, StaticParams.RequestCode.RequsetCode_Comm_ToWrite);
            }
        });
    }

    public void reSendComm(String name) {
        Intent intent = new Intent(context, WriteView.class);
        intent.putExtra("title", "给" + name + "回复");
        intent.putExtra("content", "");
        intent.putExtra("requestCode", RequestCode_OriWork_To_WriteView_WriteReComm);
        AppManager.getAppManager().toNextPage(intent, RequestCode_OriWork_To_WriteView_WriteReComm);
    }

    private void sendComm() {
        Intent intent = new Intent(context, WriteView.class);
        intent.putExtra("title", "请输入您要评论的内容");
        intent.putExtra("content", "");
        intent.putExtra("requestCode", RequestCode_OriWork_To_WriteView_WriteComm);
        AppManager.getAppManager().toNextPage(intent, RequestCode_OriWork_To_WriteView_WriteComm);
    }

    private void initAleadySignInPersonImgGridView() {
        adapter_alreadySignInPersonImg = new FastAdapter<String>(this, R.layout.item_lv_f_money_gridview) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_f_money_gridview, position);
                Glide.with(context).load(getItem(position)).into((CircleImageView) viewHolder.getView(R.id.img_item_lv_f_money_gridview));

                return viewHolder.getConvertView();
            }
        };
        gvOridetailsAlreadySignPerson.setAdapter(adapter_alreadySignInPersonImg);
    }

//    private void initContentImgGridView() {
//        adapter_contentImg = new FastAdapter<String>(this, R.layout.item_gv_oridetails_img) {
//            @Override
//            public View getViewHolder(int position, View convertView, ViewGroup parent) {
//                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_gv_oridetails_img, position);
//                ImageView img_contentimg = (ImageView) viewHolder.getView(R.id.img_contentimg);
//                img_contentimg.setImageResource(R.drawable.test_money_1);
//                if (!TextUtils.isEmpty(getItem(position))) {
//                    MyApplication.imageloader.displayImage(getItem(position), img_contentimg, ImageMode.DefaultImage,
//                            true, true);
//
//                } else {
//                    com.lovejob.utils.V.d("加载默认图片");
//                    MyApplication.imageloader.displayImage(StaticParam.DefaultUserVo, img_contentimg, ImageMode.DefaultImage,
//                            true, true);
//                }
//
//                return viewHolder.getConvertView();
//            }
//        };
//        gvOridetailsContentimg.setAdapter(adapter_contentImg);
//    }


    @Override
    protected void onPause() {
        V.d("~~~~~~~~~~~~~~~~~onPause~~~~~~~~~~~~~~~~~");
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.img_par_back, R.id.tv_pardetails_tocomm, R.id.img_pardetails_chat, R.id.img_pardetails_call, R.id.img_pardetails_grad})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_par_back:
                finish();
                break;
            case R.id.tv_pardetails_tocomm:
                Utils.showToast(this, "点评");
                sendComm();
                break;
            case R.id.img_pardetails_chat:
                Utils.showToast(this, "聊天");
                    //打开单聊对话界面
                if (!StaticParams.isConnectChetService) {
                    ToastUtils.showToast(context, "您未连接到聊天服务器，可能是网络异常，请退出重新登录");
                    return;
                }
                startActivity(MainActivityMs.mIMKit.getChattingActivityIntent(userId));
                break;
            case R.id.img_pardetails_call:
                if (isphone) {
                    if (TextUtils.isEmpty(phoneNumber)) {
                        Utils.showToast(context, "请稍后再试");
                        return;
                    }
                    Intent intent_phone = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + phoneNumber);
                    intent_phone.setData(data);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        V.e("用户权限缺失");
                        return;
                    }
                    startActivity(intent_phone);
                    break;
                } else {
                    Utils.showToast(context, "未录取前不能打电话");
                    break;
                }
            case R.id.img_pardetails_grad:
                doSignInOrCancel();
                break;
        }
    }
//

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
                    imgPardetailsGrad.setImageResource(R.mipmap.button_focus_suspend_cancle1);
                } else {
                    imgPardetailsGrad.setImageResource(R.mipmap.button_focus_suspend_off);
                }
                isGetWork = !isGetWork;
                Utils.showToast(context, "操作成功");
                adapter_alreadySignInPersonImg.removeAll();
                try {
                    adapter_contentImg.removeAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getDetails();
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
        if (data == null) {
            return;
        }
        String content = data.getStringExtra("content");
        switch (requestCode) {
            case RequestCode_OriWork_To_WriteView_WriteComm:
                //发送评论内容
                V.d("评论内容：" + content);
                callList.add(LoveJob.sendOriWorkComm(workId, null, content, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        V.d("评论成功");
                        Utils.showToast(context, "评论成功");

//                        adapter_alreadySignInPersonImg.removeAll();
//                        adapter_comm.removeAll();
//                        adapter_comm_2.removeAll();
//                        adapter_contentImg.removeAll();
//                        getDetails();
                        getCommList();
                        adapter_comm.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                }));
                break;
            case RequestCode_OriWork_To_WriteView_WriteReComm:
                V.d("给" + reQuestPid + "回复的内容：" + content);
                callList.add(LoveJob.sendOriWorkComm(workId, reQuestPid, content, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        V.d("回复成功");
                        Utils.showToast(context, "回复成功");
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
