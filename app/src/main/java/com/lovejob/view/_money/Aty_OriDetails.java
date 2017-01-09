package com.lovejob.view._money;

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
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.MyGirdView;
import com.v.rapiddev.views.MyListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.type;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_OriWork_To_WriteView_WriteComm;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_OriWork_To_WriteView_WriteReComm;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob
 * Package_Name:com.lovejob.view._money
 * Created on 2016-12-07 19:06
 * (报名的)工作详情
 */

public class Aty_OriDetails extends BaseActivity {
    @Bind(R.id.actionbar_back)
    ImageView actionbarBack;
    @Bind(R.id.actionbar_title)
    TextView actionbarTitle;
    @Bind(R.id.actionbar_save)
    TextView actionbarSave;
    @Bind(R.id.actionbar_shared)
    ImageView actionbarShared;
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
    @Bind(R.id.tv_oridetails_want)
    TextView tvOridetailsWant;
    @Bind(R.id.gv_oridetails_contentimg)
    MyGirdView gvOridetailsContentimg;
    @Bind(R.id.hitview_detaile)
    LinearLayout hitviewDetaile;
    @Bind(R.id.tv_oridetails_location)
    TextView tvOridetailsLocation;
    @Bind(R.id.tv_oridetails_countdown)
    TextView tvOridetailsCountdown;
    @Bind(R.id.tv_oridetails_number)
    TextView tvOridetailsNumber;
    @Bind(R.id.gv_oridetails_alreadySignPerson)
    GridView gvOridetailsAlreadySignPerson;
    @Bind(R.id.tv_oridetails_price)
    TextView tvOridetailsPrice;
    @Bind(R.id.tv_oridetails_phonenuber)
    TextView tvOridetailsPhonenuber;
    @Bind(R.id.lv_oridetails_comm)
    MyListView lvOridetailsComm;
    @Bind(R.id.tv_oridetails_tocomm)
    TextView tvOridetailsTocomm;
    @Bind(R.id.view_hite)
    View viewHite;
    @Bind(R.id.sv_aty_ori_main)
    PullToRefreshScrollView svAtyOriMain;
    @Bind(R.id.img_oridetails_chat)
    ImageView imgOridetailsChat;
    @Bind(R.id.img_oridetails_call)
    ImageView imgOridetailsCall;
    @Bind(R.id.img_oridetails_grad)
    ImageView imgOridetailsGrad;
    @Bind(R.id.hiteView)
    RelativeLayout hiteView;
    private String workId;
    boolean isGetWork = false;//是否可以报名
    private FastAdapter<String> adapter_contentImg, adapter_alreadySignInPersonImg;

    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter_comm;

    private FastAdapter<Data_Comm_2_2> adapter_comm_2;
    private String phoneNumber;
    private boolean isphone;

    @Override
    public void onCreate_(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.oridetails);
        ButterKnife.bind(this);
        /**
         * 设置actionbar
         */
        setactionbar();
        /**
         * 获取默认参数
         */
        getDefaultParams();

        /**
         * 设置Sv相关属性
         */
        setSVInfos();

        initAdapater();
        getDetails();
    }

    /**
     * 初始化适配器
     */
    private void initAdapater() {
        adapter_contentImg = new FastAdapter<String>(this, R.layout.item_gv_oridetails_img) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_gv_oridetails_img, position);
                ImageView img_contentimg = (ImageView) viewHolder.getView(R.id.img_contentimg);
                Glide.with(context).load(getItem(position)).placeholder(R.mipmap.ic_launcher).dontAnimate()
                        .into(img_contentimg);
                return viewHolder.getConvertView();
            }
        };
        gvOridetailsContentimg.setAdapter(adapter_contentImg);


        adapter_alreadySignInPersonImg = new FastAdapter<String>(this, R.layout.item_lv_f_money_gridview) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_f_money_gridview, position);
                CircleImageView img_item_lv_f_money_gridview = (CircleImageView) viewHolder.getView(R.id.img_item_lv_f_money_gridview);
                Glide.with(context).load(getItem(position)).dontAnimate()
                        .into(img_item_lv_f_money_gridview);
                return viewHolder.getConvertView();
            }
        };
        gvOridetailsAlreadySignPerson.setAdapter(adapter_alreadySignInPersonImg);


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
//                    adapter_comm.notifyDataSetChanged();
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
        lvOridetailsComm.setAdapter(adapter_comm);


        //评论的回复按钮
        lvOridetailsComm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.showToast(context, "给" + adapter_comm.getItem(position).getObserverName() + "回复");
                reQuestPid = adapter_comm.getItem(position).getQuestionPid();
                reSendComm(adapter_comm.getItem(position).getObserverName());
//                Intent intent = new Intent(context, WriteView.class);
//                Bundle b = new Bundle();
//                b.putString("hite", "给" + adapter_comm.getItem(position).getObserverName() + "回复");
//                b.putInt("textSize", 50);
//                b.putInt("requestCode", RequestCode.RequsetCode_Comm_ToWrite);
//                b.putString("context", "");
//
//                intent.putExtra("data", b);
//                startActivityForResult(intent, RequestCode.RequsetCode_Comm_ToWrite);
            }
        });
    }

    private String reQuestPid, userId, userName;

    private void getDetails() {
        callList.add(LoveJob.getWorkDetails(workId, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                svAtyOriMain.onRefreshComplete();
                ThePerfectGirl.UserInfoDTO userInfo = thePerfectGirl.getData().getWorkInfoDTO().getReleaseInfo();
                    userId = userInfo.getUserId();
                    userName = userInfo.getRealName();
                isphone = thePerfectGirl.getData().getWorkInfoDTO().isPhone();
                Glide.with(context).load(StaticParams.ImageURL + userInfo.getPortraitId()+"!logo").placeholder(R.mipmap.ic_launcher).dontAnimate()
                        .into(imgOridetailsUserVo);
                tvOridetailsUsername.setText(userInfo.getRealName() + "".trim());
                tvOridetailsPosition.setText(userInfo.getPosition() + "".trim());
                tvOridetailsTitle.setText(thePerfectGirl.getData().getWorkInfoDTO().getTitle());
                tvOridetailsWant.setText(thePerfectGirl.getData().getWorkInfoDTO().getContent());
                V.d("getPictrueId:" + thePerfectGirl.getData().getWorkInfoDTO().getPictrueId());
                //TODO 工作图片
                String[] img = null;
                try {
                    img = thePerfectGirl.getData().getWorkInfoDTO().getPictrueId().toString().split("\\|");
                } catch (Throwable throwable) {
                    gvOridetailsContentimg.setVisibility(View.GONE);
                }
                if (img != null && img.length > 0 && !TextUtils.isEmpty(thePerfectGirl.getData().getWorkInfoDTO().getPictrueId().toString())) {
                    V.d("空");
                    V.d("img.size:" + img.length + ",显示GridView");
                    hitviewDetaile.setVisibility(View.VISIBLE);
                    setGridView(115, gvOridetailsContentimg, adapter_contentImg, img.length);
                    for (int i = 0; i < img.length; i++) {
                        adapter_contentImg.addItem(StaticParams.ImageURL + img[i].trim()+"!logo");
                    }
                } else {
                    hitviewDetaile.setVisibility(View.GONE);
                }
                imgOridetailsUserleavl.setImageResource(getUserLever(userInfo.getLevel()));
//                adapter_contentImg.notifyDataSetChanged();
                if (thePerfectGirl.getData().getWorkInfoDTO().getShowApplyBtn() == 2) {
                    imgOridetailsGrad.setImageResource(R.mipmap.button_focus_suspend_cancle1);
                    isGetWork = false;
                } else {
                    isGetWork = true;
                }
                tvOridetailsLocation.setText(thePerfectGirl.getData().getWorkInfoDTO().getAddress());
                tvOridetailsCountdown.setText("  " + thePerfectGirl.getData().getWorkInfoDTO().getDeadlineDec());
                tvOridetailsNumber.setText("已有" + thePerfectGirl.getData().getWorkInfoDTO().getApplyCount() + "人抢单");
                //TODO 抢单人头像

                tvOridetailsPrice.setText(thePerfectGirl.getData().getWorkInfoDTO().getSalary() + "元" + thePerfectGirl.getData().getWorkInfoDTO().getPaymentDec());
//                tvOridetailsPhonenuber.setText(thePerfectGirl.getData().getWorkInfoDTO().getContactPhone());
                String s = thePerfectGirl.getData().getWorkInfoDTO().getContactPhone();
                String s3 = s.substring(0, s.length() - 4);
                tvOridetailsPhonenuber.setText(s3+"****");
                phoneNumber = thePerfectGirl.getData().getWorkInfoDTO().getContactPhone();
                //TODO 点评列表

                List<ThePerfectGirl.UserInfoDTO> lists = thePerfectGirl.getData().getWorkInfoDTO().getEmployeeInfo();
                int size = 0;
                if (lists != null) {
                    size = lists.size();
                    for (int i = 0; i < thePerfectGirl.getData().getWorkInfoDTO().getEmployeeInfo().size(); i++) {
                        adapter_alreadySignInPersonImg.addItem(StaticParams.ImageURL + thePerfectGirl.getData().getWorkInfoDTO().getEmployeeInfo().get(i).getPortraitId()+"!logo");
                    }
                    adapter_alreadySignInPersonImg.notifyDataSetChanged();
                }
                setGridView(34, gvOridetailsAlreadySignPerson, adapter_alreadySignInPersonImg, size);
                getCommList();
            }

            @Override
            public void onError(String msg) {
                svAtyOriMain.onRefreshComplete();
                Utils.showToast(context, msg);
            }
        }));
    }


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

    //获取用户等级对应的资源文件
    private int getUserLever(int level) {
        int resid = 0;
        switch (level) {
            case 1:
                resid = R.mipmap.icon_level_v1_42;
                break;

            case 2:
                resid = R.mipmap.icon_level_v1_13;
                break;

            case 3:
                resid = R.mipmap.icon_level_v1_86;
                break;

            case 4:
                resid = R.mipmap.icon_level_v1_57;
                break;

            case 5:
                resid = R.mipmap.icon_level_v1_12;
                break;

            default:
                resid = R.mipmap.icon_level_v1_42;
        }
        return resid;
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
                    imgOridetailsGrad.setImageResource(R.mipmap.button_focus_suspend_cancle1);
                } else {
                    imgOridetailsGrad.setImageResource(R.mipmap.button_focus_suspend_off);
                }
                isGetWork = !isGetWork;
                Utils.showToast(context, "操作成功");
                adapter_alreadySignInPersonImg.removeAll();
                adapter_contentImg.removeAll();
                getDetails();
            }

            @Override
            public void onError(String msg) {
                dialog.dismiss();
                Utils.showToast(context, msg);
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


    private void setSVInfos() {
        svAtyOriMain.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        svAtyOriMain.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                adapter_contentImg.removeAll();
//                adapter_alreadySignInPersonImg.removeAll();
//                getDetails();
                getDetails();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    /**
     * 判断是否显示（悬浮窗口 聊天、打电话、抢单）
     */
    private void getDefaultParams() {
        workId = getIntent().getStringExtra("workId");

        if (workId == null) {
            Utils.showToast(this, "数据异常,请重新登陆");
            return;
        }
        ButterKnife.bind(this);
        context = this;

        if (!getIntent().getBooleanExtra("isEdit", false)) {
            hiteView.setVisibility(View.INVISIBLE);
            viewHite.setVisibility(View.VISIBLE);
        } else {
            hiteView.setVisibility(View.VISIBLE);
            viewHite.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 初始化Actionbar
     */
    private void setactionbar() {
        actionbarSave.setVisibility(View.GONE);
        actionbarTitle.setTextSize(16);
        actionbarTitle.setText("工作详情");
        actionbarTitle.setTextColor(Color.WHITE);

    }

    @Override
    public void onResume_() {

    }

    @Override
    public void onDestroy_() {

    }

    @OnClick({R.id.actionbar_back, R.id.img_oridetails_userlogo, R.id.img_oridetails_chat, R.id.img_oridetails_call, R.id.img_oridetails_grad, R.id.tv_oridetails_tocomm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.img_oridetails_userlogo:
                V.d("用户头像被点击");
                break;
            case R.id.img_oridetails_chat:
                V.d("聊天");
                //打开单聊对话界面
                if (!StaticParams.isConnectChetService) {
                    ToastUtils.showToast(context, "您未连接到聊天服务器，可能是网络异常，请退出重新登录");
                    return;
                }
                startActivity(MainActivityMs.mIMKit.getChattingActivityIntent(userId));
                break;
            case R.id.img_oridetails_call:
                V.d("打电话");
                if (isphone){
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
                }else {
                    Utils.showToast(context,"未录取前不能打电话");
                    break;
                }

            case R.id.img_oridetails_grad:
                V.d("抢单");
                doSignInOrCancel();
                break;

            case R.id.tv_oridetails_tocomm:
                //提问点评
                sendComm();
                break;
        }
    }

    private void sendComm() {
        Intent intent = new Intent(context, WriteView.class);
        intent.putExtra("title", "请输入您要评论的内容");
        intent.putExtra("content", "");
        intent.putExtra("requestCode", RequestCode_OriWork_To_WriteView_WriteComm);
        AppManager.getAppManager().toNextPage(intent, RequestCode_OriWork_To_WriteView_WriteComm);
    }

    public void reSendComm(String name) {
        Intent intent = new Intent(context, WriteView.class);
        intent.putExtra("title", "给" + name + "回复");
        intent.putExtra("content", "");
        intent.putExtra("requestCode", RequestCode_OriWork_To_WriteView_WriteReComm);
        AppManager.getAppManager().toNextPage(intent, RequestCode_OriWork_To_WriteView_WriteReComm);
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
