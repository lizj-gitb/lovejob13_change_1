package com.lovejob.view._job;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.lovejob.BaseFragment;
import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view._home.dyndetailstabs.NewsDetails;
import com.lovejob.view._othersinfos.Others;
import com.lovejob.view.cityselector.CityPickerActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.dialogs.core.MaterialDialog;
import com.v.rapiddev.dialogs.zdialog.OnDialogItemClickListener;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.Utils_RapidDev;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.InputMethodLayout;
import com.v.rapiddev.views.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class F_Job extends BaseFragment {
    @Bind(R.id.actionbar_tv_locationtext)
    TextView actionbarTvLocationtext;
    @Bind(R.id.actionbar_lt_location)
    LinearLayout actionbarLtLocation;
    @Bind(R.id.actionbar_img_search)
    ImageView actionbarImgSearch;
    @Bind(R.id.actionbar_et_search)
    EditText actionbarEtSearch;
    @Bind(R.id.actionbar_img_search_right)
    ImageView actionbarImgSearchRight;
    @Bind(R.id.actionbar_img_more)
    ImageView actionbarImgMore;
    @Bind(R.id.actionbar_lt_more)
    LinearLayout actionbarLtMore;
    @Bind(R.id.roll_view_pager)
    RollPagerView rollViewPager;
    @Bind(R.id.tab_1)
    TextView tab1;
    @Bind(R.id.tab_2)
    TextView tab2;
    @Bind(R.id.tab_3)
    TextView tab3;
    @Bind(R.id.tab_4)
    TextView tab4;
    @Bind(R.id.lv_job)
    MyListView lvJob;
    @Bind(R.id.pull_to_refresh_job)
    PullToRefreshScrollView pullToRefreshJob;
    @Bind(R.id.editkeybox)
    InputMethodLayout editkeybox;
    private View mainView;
    private List<Call> calls = new ArrayList<>();
    private List<ThePerfectGirl.InformationInfo> newsList;
    private int page = 1;//请求的页数 默认1
    private String address;//定位的地址 可为空
    private String jobName;//用户搜索框的内容 可为空
    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter_lv;
    private boolean isAddDate = true;//用户是否为第一次进入该页面
    private MaterialDialog dialog;
    private String userPid;
    private MyActvityResult broadcastReceiver;
    private boolean isFirstIntoPage = true;//是否为第一次进入页面

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.f_job, null);
        ButterKnife.bind(this, mainView);
//        StaticParams.ROWS = "5";
        /**
         * 设置适配器相关
         */
        setActionbar();
        /**
         * 设置edittext状态监听
         */
        setKeyBoxListener();
        /**
         * 设置新闻点击事件
         */
        setNewsItemClickListener();
        /**
         * 初始化工作列表适配器
         */
        initAdapter();
        /**
         * 设置下拉刷新相关设置
         */
        setRefreshListener();
        /**
         * 设置lv点击事件
         */
        setWorkItemListener();

        address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败");
        actionbarTvLocationtext.setText(address);
        //判断当前用户是否为第一次进入该页面 第一次加载数据  第二次不加载
//        if (isAddDate) {
            /**
             * 加载新闻数据
             */
            addDataToNewsList();
            /**
             * 加载工作列表
             */
            adDataToJobList();
//        }
        broadcastReceiver = new MyActvityResult();
        IntentFilter intentFilter = new IntentFilter("com.lovejob.onactivityresult");
        context.registerReceiver(broadcastReceiver, intentFilter);
        AppPreferences appPreferences = new AppPreferences(context);
        userPid = appPreferences.getString(StaticParams.FileKey.__USERPID__, "");
        return mainView;
    }


    private void setWorkItemListener() {
        lvJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                V.d("您点击了ID为" + adapter_lv.getItem(i).getPid() + "的工作");
                Intent intent = new Intent(getActivity(), JobDetails.class);
                intent.putExtra("workId", adapter_lv.getItem(i).getPid());
                startActivity(intent);
            }
        });
    }

    private void setRefreshListener() {
        pullToRefreshJob.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshJob.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                StaticParams.ROWS = "5";
                jobName = null;
                address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "未知");
                adapter_lv.removeAll();
                adapter_lv.notifyDataSetChanged();
                adDataToJobList();
                addDataToNewsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
//                StaticParams.ROWS = "20";
                adDataToJobList();
            }
        });
    }

    private void setActionbar() {
        address = MyApplication.getCity();
        actionbarImgMore.setImageResource(R.mipmap.topbar_edit);
        actionbarImgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SendJob.class);
                context.startActivityForResult(intent, StaticParams.RequestCode.RequestCode_F_Job_TO_SendJob);
            }
        });

    }

    private void initAdapter() {
        adapter_lv = new FastAdapter<ThePerfectGirl.WorkInfoDTO>(context, R.layout.item_lv_job) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_job, position);
                Glide.with(context).load((StaticParams.QiNiuYunUrl + getItem(position).getReleaseInfo().getPortraitId())).dontAnimate().placeholder(R.drawable.ic_launcher).into((CircleImageView) viewHolder.getView(R.id.img_job_logo));
                ((TextView) viewHolder.getView(R.id.tv_job_title)).setText(getItem(position).getTitle() == null ? "用户未填写" : getItem(position).getTitle());
                ((TextView) viewHolder.getView(R.id.tv_job_com)).setText(getItem(position).getReleaseInfo().getCompany() == null ? "公司名称未填写" : getItem(position).getReleaseInfo().getCompany());

                ((TextView) viewHolder.getView(R.id.tv_job_location)).setText(TextUtils.isEmpty(getItem(position).getAddress()) ? "工作地点未填写" : getItem(position).getAddress());

                ((TextView) viewHolder.getView(R.id.tv_job_money)).setText((getItem(position).getSalary() + "元" + getItem(position).getPaymentDec()) == null ? "用户未填写" : getItem(position).getSalary() + "/月");// getItem(position).getPaymentDec()

                ((TextView) viewHolder.getView(R.id.tv_job_alreadysinperson)).setText(getItem(position).getApplyCount() + "" == null ? "" : getItem(position).getApplyCount() + "/人已报名".trim());
                String s1 = String.format("%tF%n", getItem(position).getReleaseDate());
                ((TextView) viewHolder.getView(R.id.tv_job_day)).setText(s1.substring(5, s1.length()));
                ((TextView) viewHolder.getView(R.id.tv_job_eyenum)).setText(getItem(position).getCount() + "" == null ? "0" : getItem(position).getCount() + "");
                ImageView xuanshang = ((ImageView) viewHolder.getView(R.id.img_job_shang));
                ImageView jianghu = ((ImageView) viewHolder.getView(R.id.img_job_lingpai));
                TextView tv_job_xuanshangjinqian = ((TextView) viewHolder.getView(R.id.tv_job_xuanshangjinqian));
                if (getItem(position).getList() != null && getItem(position).getList().size() > 0) {
                    if (getItem(position).getList().size() == 1) {
                        if (getItem(position).getList().get(0).getType().equals("0")) {
                            //只返回一个悬赏令
                            xuanshang.setVisibility(View.VISIBLE);
                            tv_job_xuanshangjinqian.setText("¥:" + getItem(position).getList().get(0).getMoney());
                            jianghu.setVisibility(View.GONE);
                        }

                        if (getItem(position).getList().get(0).getType().equals("1")) {
                            //只返回一个江湖令
                            xuanshang.setVisibility(View.GONE);
                            tv_job_xuanshangjinqian.setVisibility(View.INVISIBLE);
                        }
                    }


                    if (getItem(position).getList().size() == 2) {
                        //隐藏一个的令牌
                        xuanshang.setVisibility(View.VISIBLE);
                        jianghu.setVisibility(View.VISIBLE);
                        tv_job_xuanshangjinqian.setVisibility(View.VISIBLE);
                        for (int i = 0; i < getItem(position).getList().size(); i++) {
                            if (getItem(position).getList().get(i).getType().equals("0")) {
                                tv_job_xuanshangjinqian.setText("¥:" + getItem(position).getList().get(i).getMoney());
                            }
                        }
                    }
                } else {
                    xuanshang.setVisibility(View.GONE);
                    tv_job_xuanshangjinqian.setVisibility(View.INVISIBLE);
                    jianghu.setVisibility(View.GONE);
                }
                /**
                 * 设置用户头像、悬赏令、江湖令的点击事件
                 */
                setOnItemViewClickListener(viewHolder, position);

                if (isFirstIntoPage) {
                    //当进入页面是，获得焦点防止页面自动下跳
                    actionbarImgMore.setFocusable(true);
                    actionbarImgMore.setFocusableInTouchMode(true);
                }
                return viewHolder.getConvertView();
            }
        };
        lvJob.setAdapter(adapter_lv);
    }

    /**
     * item中每个可点击控件的事件监听
     *
     * @param viewHolder
     * @param position
     */
    private void setOnItemViewClickListener(final FFViewHolder viewHolder, final int position) {
        ((CircleImageView) viewHolder.getView(R.id.img_job_logo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                V.d("进入用户" + adapter_lv.getItem(position).getReleaseInfo().getRealName() + "的个人中心");
                if (userPid.equals(adapter_lv.getItem(position).getReleaseInfo().getUserId())){

                }else {
                    Intent intent = new Intent(context, Others.class);
                    intent.putExtra("userType",adapter_lv.getItem(position).getReleaseInfo().getType());
                    intent.putExtra("userId", adapter_lv.getItem(position).getReleaseInfo().getUserId());
                    startActivity(intent);
                }
            }
        });


        ((LinearLayout) viewHolder.getView(R.id.xuanshang)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                V.d("工作 " + adapter_lv.getItem(position).getTitle() + "的悬赏令被点击");
                ZDialog.showZDlialog(context, "是否接令", "悬赏" + adapter_lv.getItem(position).getList()
                        .get(0).getMoney() + "元帮助" + adapter_lv.getItem(position).getReleaseInfo()
                        .getCompany() + "招聘" + adapter_lv.getItem(position).getTitle()
                        + "，招聘到合适的人上岗10天签订合同后，方能向商家索要赏金哦~", "接令", "取消", R.mipmap.icon_shang, new OnDialogItemClickListener() {
                    @Override
                    public void onLeftButtonClickListener() {
                        V.d("接令");
                        dialog = Utils.showProgressDliago(context, "正在抢令");
                        calls.add(LoveJob.getJobToken_1("0", adapter_lv.getItem(position).getPid(), new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                dialog.dismiss();
                                Utils.showToast(context, "恭喜您抢到了！快去帮企业招人吧~");
                            }

                            @Override
                            public void onError(String msg) {
                                dialog.dismiss();
                                Utils.showToast(context, msg);
                            }
                        }));
                    }

                    @Override
                    public void onRightButtonClickListener() {

                    }
                });
            }
        });


        ((LinearLayout) viewHolder.getView(R.id.jianghu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                V.d("工作 " + adapter_lv.getItem(position).getTitle() + "的江湖令被点击");


                ZDialog.showZDlialog(context, "确认分享吗", "请在分享成功后及时返回app以保证您分享所得收益正常到账", "分享", "取消", R.mipmap.icon_changqigongzuo_jianghu, new OnDialogItemClickListener() {
                    @Override
                    public void onLeftButtonClickListener() {
                        //分享
                        toShard(adapter_lv.getItem(position).getPid());
                    }

                    @Override
                    public void onRightButtonClickListener() {
                        Utils.showToast(context, "您取消了分享");
                    }
                });
            }
        });
    }

    private void setNewsItemClickListener() {
        rollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.showToast(context, "内容：" + newsList.get(position).getContent());
                String newsId = newsList.get(position).getPid();
                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("newsId", newsId);
                context.startActivityForResult(intent, StaticParams.RequestCode.RequestCode_F_Job_TO_NewsDetails);
                context.overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (rollViewPager != null)
            rollViewPager.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rollViewPager != null)
            rollViewPager.resume();
    }

    /**
     * 进入该方法时表示当前fragment是可见的，可以加载用户数据 注意：请做标示，当数据加载完一次后第二次不去加载
     */
    @Override
    public void loadData() {
//        address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败");
//        actionbarTvLocationtext.setText(address);
//        //判断当前用户是否为第一次进入该页面 第一次加载数据  第二次不加载
//        if (isAddDate) {
//            /**
//             * 加载新闻数据
//             */
//            addDataToNewsList();
//            /**
//             * 加载工作列表
//             */
//            adDataToJobList();
//        }
    }

    private void adDataToJobList() {
        calls.add(LoveJob.getWorkList("0", page, address, jobName, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                if (isAddDate) pullToRefreshJob.scrollTo(0,0);
                isAddDate = false;
                pullToRefreshJob.onRefreshComplete();
                List<ThePerfectGirl.WorkInfoDTO> workInfoDTOs = thePerfectGirl.getData().getWorkInfoDTOs();
                if (workInfoDTOs == null || workInfoDTOs.size() == 0) {
                    Utils.showToast(context, "没有更多工作信息");
                    return;
                }
                for (int i = 0; i < workInfoDTOs.size(); i++) {
                    adapter_lv.addItem(workInfoDTOs.get(i));
                }
                adapter_lv.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                pullToRefreshJob.onRefreshComplete();
                Utils.showToast(context, msg);
            }
        }));
    }

    private void addDataToNewsList() {
        calls.add(LoveJob.getNewsList("1", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                isAddDate = false;
                if (thePerfectGirl.getData().getInformationInfos() != null)
                    newsList = new ArrayList<ThePerfectGirl.InformationInfo>();
                for (int i = 0; i < thePerfectGirl.getData().getInformationInfos().size(); i++) {
                    newsList.add(thePerfectGirl.getData().getInformationInfos().get(i));
                }
                rollViewPager.setAdapter(new TestNomalAdapter(newsList));
            }

            @Override
            public void onError(String msg) {
                V.e("job页面新闻数据加载失败：" + msg);
            }
        }));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (Call mCall : calls) {
            if (mCall != null && !mCall.isCanceled()) mCall.cancel();
        }
        calls = null;
        context.unregisterReceiver(broadcastReceiver);
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.actionbar_lt_location, R.id.actionbar_img_search_right, R.id.actionbar_img_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_lt_location:
                //定位
                context.startActivityForResult(new Intent(context, CityPickerActivity.class), StaticParams.RequestCode.RequestCode_F_Job_TO_CitySelector);
                break;
            case R.id.actionbar_img_search_right:
                break;
        }
    }

    private class TestNomalAdapter extends StaticPagerAdapter {
        private List<ThePerfectGirl.InformationInfo> imgs;

        public TestNomalAdapter(List<ThePerfectGirl.InformationInfo> newsList) {
            this.imgs = newsList;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            Glide.with(context).load(StaticParams.QiNiuYunUrl_News + imgs.get(position).getPictrueid()).dontAnimate().placeholder(R.drawable.ic_launcher).into(view);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return imgs.size();
        }
    }


    private void setKeyBoxListener() {
        editkeybox.setOnkeyboarddStateListener(new InputMethodLayout.onKeyboardsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    case InputMethodLayout.KEYBOARD_STATE_SHOW:
                        V.d("软键盘被打开");
                        actionbarImgSearch.setVisibility(View.GONE);
                        actionbarImgSearchRight.setVisibility(View.VISIBLE);
                        break;
                    case InputMethodLayout.KEYBOARD_STATE_HIDE:
                        V.d("软键盘被关闭");
                        if (!TextUtils.isEmpty(actionbarEtSearch.getText().toString())) {
                            actionbarImgSearch.setVisibility(View.GONE);
                            actionbarImgSearchRight.setVisibility(View.VISIBLE);
                        } else {
                            actionbarImgSearch.setVisibility(View.VISIBLE);
                            actionbarImgSearchRight.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });
        //设置键盘上的搜索按钮点击事件
        actionbarEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String keyText = actionbarEtSearch.getText().toString();
                if (keyText != null && !TextUtils.isEmpty(keyText.trim())) {
                    V.d("搜索：" + keyText);
                    jobName = keyText;
                    keyText = null;
                    Utils_RapidDev.closeKeybord(actionbarEtSearch, context);
                    actionbarEtSearch.setText("");
                    if (adapter_lv != null && adapter_lv.getList().size() > 0) {
                        adapter_lv.removeAll();
                        adapter_lv.notifyDataSetChanged();
                    }
                    adDataToJobList();
                } else {
                    Utils.showToast(context, "输入不能为空，请重新输入");
                }
                return true;
            }
        });

//        actionbarEtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() == 0) {
//                    actionbarImgSearch.setVisibility(View.VISIBLE);
//                    actionbarImgSearchRight.setVisibility(View.GONE);
//                } else {
//                    actionbarImgSearch.setVisibility(View.GONE);
//                    actionbarImgSearchRight.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
        actionbarImgSearchRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(actionbarEtSearch.getText().toString())) {
                    jobName = actionbarEtSearch.getText().toString();
                    actionbarEtSearch.setText("");
                    Utils_RapidDev.closeKeybord(actionbarEtSearch, context);
                    if (adapter_lv != null && adapter_lv.getList().size() > 0) {
                        adapter_lv.removeAll();
                        adapter_lv.notifyDataSetChanged();
                    }
                    adDataToJobList();
                } else {
                    Utils.showToast(context, "输入不能为空，请重新输入");
                }
            }
        });

    }

    private void toShard(final String workPid) {
//        new ShareAction(context).withTargetUrl("http://192.168.3.8:8081/test?toOtherActivity=0&otherId=" + workPid)
        new ShareAction(context).withText(workPid)

//                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//
                .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
//                        Utils.showToast(context, "分享成功");
                        V.d("分享成功");
                        dialog = Utils.showProgressDliago(context, "请稍后……");
                        calls.add(LoveJob.getJobToken_1("1", workPid, new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                dialog.dismiss();
                                Utils.showToast(context, "分享成功");
                            }

                            @Override
                            public void onError(String msg) {
                                dialog.dismiss();
                                Utils.showToast(context, msg);
                            }
                        }));
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        V.d("分享错误");
                        Utils.showToast(context, "分享错误");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        V.d("分享被取消");
                        Utils.showToast(context, "分享被取消");
                    }
                }).open();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (F_Job.this.isVisible()) {
//            if (requestCode == StaticParams.RequestCode.RequestCode_F_Job_TO_CitySelector) {
//                address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败");
//                actionbarTvLocationtext.setText(address);
//                V.d("从城市选择页面返回");
//                if (adapter_lv != null && adapter_lv.getList().size() > 0) {
//                    adapter_lv.removeAll();
//                    adapter_lv.notifyDataSetChanged();
//                }
//                adDataToJobList();
//            } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Job_TO_NewsDetails) {
//                V.d("从job页面进入了新闻详情页面的返回");
//            } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Job_TO_SendJob) {
//                V.d("从job页面进入发布长期工作页面的返回");
//            }
//        }
//    }

    public class MyActvityResult extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.lovejob.onactivityresult")) {
                V.d("F_Job接收到广播");
                int requestCode = intent.getIntExtra("requestCode", -1);
                Intent data = intent.getSelector();
                if (F_Job.this.isVisible()) {
                    if (requestCode == StaticParams.RequestCode.RequestCode_F_Job_TO_CitySelector) {
                        address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败");
                        actionbarTvLocationtext.setText(address);
                        V.d("从城市选择页面返回");
                        if (adapter_lv != null && adapter_lv.getList().size() > 0) {
                            adapter_lv.removeAll();
                            adapter_lv.notifyDataSetChanged();
                        }
                        adDataToJobList();
                    } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Job_TO_NewsDetails) {
                        V.d("从job页面进入了新闻详情页面的返回");
                    } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Job_TO_SendJob) {
                        V.d("从job页面进入发布长期工作页面的返回");
                    }
                }
            }
        }
    }
}