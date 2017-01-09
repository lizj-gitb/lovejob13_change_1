package com.lovejob.view._money;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
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
import com.lovejob.model.MyOnClickListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view._home.dyndetailstabs.NewsDetails;
import com.lovejob.view._othersinfos.Others;
import com.lovejob.view.cityselector.CityPickerActivity;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.base.AppManager;
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

import static com.lovejob.model.StaticParams.RequestCode.RequestCode_F_Money_TO_SendMoneyWork;

public class F_Money extends BaseFragment {
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
    MyListView lvmoney;
    @Bind(R.id.pull_to_refresh_job)
    PullToRefreshScrollView pullToRefreshJob;
    @Bind(R.id.editkeybox_money)
    InputMethodLayout editkeyboxMoney;
    private View mainView;
    private boolean isAddData = true;
    private String address = "";
    private List<Call> calls = new ArrayList<>();
    private List<ThePerfectGirl.InformationInfo> newsList;
    private FastAdapter<ThePerfectGirl.WorkInfoDTO> adapter_lv_main;//主适配器
    private FastAdapter<String> adapter_lv_gridview;//listview中嵌套的gridview适配器
    private String time;
    private int page = 1;
    private String userPid;
    private String jobName;
    private MyActvityResult broadcastReceiver;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.f_money, null);
        ButterKnife.bind(this, mainView);
//        StaticParams.ROWS = "5";
        /**
         * 设置actionbar相关
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
        /**
         * 添加新闻
         */
        addDataToNewsList();

        address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败");
        actionbarTvLocationtext.setText(address);
        /**
         * 添加列表数据
         */
        addListData();

        broadcastReceiver = new MyActvityResult();
        IntentFilter intentFilter = new IntentFilter("com.lovejob.onactivityresult");
        context.registerReceiver(broadcastReceiver, intentFilter);
        AppPreferences appPreferences = new AppPreferences(context);
        userPid = appPreferences.getString(StaticParams.FileKey.__USERPID__, "");
        return mainView;
    }


    private void setWorkItemListener() {
        lvmoney.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                V.d("兼职工作页面的Item被点击");
                if (adapter_lv_main.getItem(position).getType() == 2) {
                    V.d("跳转兼职工作详情页面");
                    Intent intentpar = new Intent(getActivity(), Aty_ParDetails.class);
                    intentpar.putExtra("workId", adapter_lv_main.getItem(position).getPid());
                    intentpar.putExtra("isEdit", adapter_lv_main.getItem(position).getShowApplyBtn() == 0 ? false : true);
                    startActivity(intentpar);
                } else if (adapter_lv_main.getItem(position).getType() == 1) {
                    V.d("跳转创意工作详情页面");
                    Intent intentDri = new Intent(getActivity(), Aty_OriDetails.class);
                    intentDri.putExtra("workId", adapter_lv_main.getItem(position).getPid());
                    intentDri.putExtra("isEdit", adapter_lv_main.getItem(position).getShowApplyBtn() == 0 ? false : true);
                    startActivity(intentDri);
                }
            }
        });
    }



    private void initAdapter() {
        adapter_lv_main = new FastAdapter<ThePerfectGirl.WorkInfoDTO>(getActivity(), R.layout.item_lv_f_money_main) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_f_money_main, position);
//                //gv_f_money_alreadySignPerson
                time = getItem(position).getFirstRefreshTime() + "".trim();
//                //发布者头像
                Glide.with(context).load(StaticParams.ImageURL + getItem(position).getReleaseInfo().getPortraitId().toString().trim()+"!logo").dontAnimate().placeholder(R.drawable.ic_launcher).into((CircleImageView) viewHolder.getView(R.id.img_item_lv_f_money_main_userlogo));
//                //工作标题
                ((TextView) viewHolder.getView(R.id.tv_item_lv_f_money_main_title)).setText(getItem(position).getTitle());
//
//                //工作酬金
                ((TextView) viewHolder.getView(R.id.tv_item_lv_f_money_main_price)).setText(getItem(position).getSalary());
                ((TextView) viewHolder.getView(R.id.payment)).setText("元/" + getItem(position).getPaymentDec());
//
//
                String s1 = String.format("%tF%n", getItem(position).getReleaseDate());
                String s2 = String.format("%tR%n", getItem(position).getReleaseDate());
//
////                V.d("s1:" + s1);
////                V.d("s2:" + s2);
//                //设置日期
                ((TextView) viewHolder.getView(R.id.tv_item_lv_f_money_main_day)).setText(s1.substring(5, s1.length()));
//
//                //设置时间
                ((TextView) viewHolder.getView(R.id.tv_item_lv_f_money_main_time)).setText(s2);
//
//
//                //设置倒计时信息
                ((TextView) viewHolder.getView(R.id.tv_item_lv_f_money_main_countdown)).setText("倒计时:" + getItem(position).getDeadlineDec());
////
//                //设置工作地点
                ((TextView) viewHolder.getView(R.id.tv_item_lv_f_money_main_location)).setText(getItem(position).getAddress());
//
//
//                //已报名人数
                ((TextView) viewHolder.getView(R.id.tv_item_lv_f_money_main_alreadysign)).setText(getItem(position).getApplyCount() + "人已报名");
//
//                //根据报名状态设置默认图标(报名/取消)
                ImageView imgItemLvFMoneyMainIsGrad = (ImageView) viewHolder.getView(R.id.img_item_lv_f_money_main_isGrad);
                switch (getItem(position).getShowApplyBtn()) {
                    case 0:
                        imgItemLvFMoneyMainIsGrad.setVisibility(View.INVISIBLE);
                        break;

                    case 1:
                        //设置未报名图标
                        imgItemLvFMoneyMainIsGrad.setVisibility(View.VISIBLE);
                        imgItemLvFMoneyMainIsGrad.setImageResource(R.mipmap.icon_buy);
                        break;

                    case 2:
                        //设置已报名图标
                        imgItemLvFMoneyMainIsGrad.setVisibility(View.VISIBLE);
                        imgItemLvFMoneyMainIsGrad.setImageResource(R.mipmap.icon_cancle);
                        break;
                }


                GridView gv_f_money_alreadySignPerson = (GridView) viewHolder.getView(R.id.gv_f_money_alreadySignPerson);
//                 /*
//                    初始化listview嵌套的gridview的适配器
//                     */
                initGridviewAdpater();
                gv_f_money_alreadySignPerson.setAdapter(adapter_lv_gridview);
                int maxSize = 0;
                if (getItem(position).getEmployeeInfo() != null) {
                    maxSize = getItem(position).getEmployeeInfo().size() > 3 ? 3 : getItem(position).getEmployeeInfo().size();
                    setGridView(gv_f_money_alreadySignPerson, maxSize);
                    for (int i = 0; i < maxSize; i++) {
                        if (getItem(position).getEmployeeInfo().get(i) != null) {
                            adapter_lv_gridview.addItem(getItem(position).getEmployeeInfo().get(i).getPortraitId() + "".trim());
                        } else {
                            adapter_lv_gridview.addItem(null);
                        }
                    }
                    adapter_lv_gridview.notifyDataSetChanged();
                } else {
                    setGridView(gv_f_money_alreadySignPerson, maxSize);
                }

                /*
                设置listviewItem中的点击事件
                 */
                setItemClickListener(viewHolder.getConvertView(), position, getItem(position).getShowApplyBtn());
                //当进入页面是，获得焦点防止页面自动下跳
                actionbarImgMore.setFocusable(true);
                actionbarImgMore.setFocusableInTouchMode(true);
                return viewHolder.getConvertView();
            }
        };
        lvmoney.setAdapter(adapter_lv_main);
    }

    private void setItemClickListener(final View convertView, final int posstion, final int showApplyBtn) {
        //发布者头像点击事件
        CircleImageView imgItemLvFMoneyMainUserlogo = (CircleImageView) convertView.findViewById(R.id.img_item_lv_f_money_main_userlogo);
//        GridView gridView = (GridView) convertView.findViewById(R.id.gv_f_money_alreadySignPerson);
        //抢单按钮点击事件
        final ImageView imageView_buy = (ImageView) convertView.findViewById(R.id.img_item_lv_f_money_main_isGrad);
        imgItemLvFMoneyMainUserlogo.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onclickListener(View v) {
                V.d("进入用户" + adapter_lv_main.getItem(posstion).getReleaseInfo().getRealName() + "的个人中心");
                if (userPid.equals(adapter_lv_main.getItem(posstion).getReleaseInfo().getUserId())) {

                } else {
                    Intent intent = new Intent(context, Others.class);
                    intent.putExtra("userType", adapter_lv_main.getItem(posstion).getReleaseInfo().getType());
                    intent.putExtra("userId", adapter_lv_main.getItem(posstion).getReleaseInfo().getUserId());
                    startActivity(intent);
                }
            }
        });
        imageView_buy.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onclickListener(View v) {
                doSignInOrCancel(adapter_lv_main.getItem(posstion).getShowApplyBtn(), posstion);
            }
        });
    }

    boolean isGetWork = true;

    private void doSignInOrCancel(int showApplyBtn, final int posstion) {
        isGetWork = showApplyBtn == 1 ? true : false;
        View view = lvmoney.getChildAt(posstion);
        final GridView gridView = (GridView) view.findViewById(R.id.gv_f_money_alreadySignPerson);
        final TextView textView = (TextView) view.findViewById(R.id.tv_item_lv_f_money_main_alreadysign);
        final ImageView imageView = (ImageView) view.findViewById(R.id.img_item_lv_f_money_main_isGrad);
        FastAdapter<String> adapter = ((FastAdapter<String>) gridView.getAdapter());
        if (isGetWork) {
            imageView.setImageResource(R.mipmap.icon_cancle);
            adapter.addItem("");
        } else {
            imageView.setImageResource(R.mipmap.icon_buy);
            try {
                adapter.remove(
                        adapter.getList()
                                .lastIndexOf(((FastAdapter<ThePerfectGirl.WorkInfoDTO>) lvmoney.getAdapter())
                                        .getItem(posstion).getReleaseInfo().getPortraitId()));
            } catch (Exception e) {

            }
        }
        adapter.notifyDataSetChanged();

        callList.add(LoveJob.getGrabForm(adapter_lv_main.getItem(posstion).getPid(), isGetWork, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                Utils.showToast(context, "操作成功");
                int maxSize = 0;
                textView.setText(thePerfectGirl.getData().getWorkInfoDTO().getApplyCount() + "人已报名");

                ThePerfectGirl.WorkInfoDTO workInfoDTO = adapter_lv_main.getItem(posstion);//适配器里的
                ThePerfectGirl.WorkInfoDTO workInfoDTO_back = thePerfectGirl.getData().getWorkInfoDTO();//网络返回的

                adapter_lv_main.getList().get(posstion).setShowApplyBtn(workInfoDTO_back.getShowApplyBtn());

            }

            @Override
            public void onError(String msg) {
                Utils.showToast(context, msg);
                if (isGetWork) {
                    imageView.setImageResource(R.mipmap.icon_buy);
                } else {
                    imageView.setImageResource(R.mipmap.icon_cancle);
                }
            }
        }));
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView(GridView gridView, int size) {
        V.d("size:" + size);
        int length = 30;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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
        gridView.setAdapter(adapter_lv_gridview);
    }

    private void initGridviewAdpater() {
        adapter_lv_gridview = new FastAdapter<String>(getActivity(), R.layout.item_lv_f_money_gridview) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_f_money_gridview, position);
                Glide.with(context).load(StaticParams.ImageURL + getItem(position)+"!logo").dontAnimate().into((CircleImageView) viewHolder.getView(R.id.img_item_lv_f_money_gridview));

                return viewHolder.getConvertView();
            }
        };
    }

    private void setRefreshListener() {
        pullToRefreshJob.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshJob.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                V.d("下拉");
                StaticParams.ROWS = "5";
                page = 1;
                jobName = null;
                time = null;
                address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "未知");
                adapter_lv_main.removeAll();
                adapter_lv_main.notifyDataSetChanged();
                addListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                V.d("上啦");
                page++;
//                StaticParams.ROWS = "20";
                addListData();
            }
        });
    }

    private void setNewsItemClickListener() {
        rollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String newsId = newsList.get(position).getPid();
                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("newsId", newsId);
                context.startActivityForResult(intent, StaticParams.RequestCode.RequestCode_F_Money_TO_NewsDetails);
                context.overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
            }
        });
    }

    private void setActionbar() {
        address = MyApplication.getCity();
        actionbarImgMore.setImageResource(R.mipmap.topbar_edit);
    }

    private void setKeyBoxListener() {
        editkeyboxMoney.setOnkeyboarddStateListener(new InputMethodLayout.onKeyboardsChangeListener() {
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
                V.d("输入框的搜索按钮被点击");
                String keyText = actionbarEtSearch.getText().toString();
                if (keyText != null && !TextUtils.isEmpty(keyText.trim())) {
                    V.d("搜索：" + keyText);
                    jobName = keyText;
                    keyText = null;
                    Utils_RapidDev.closeKeybord(actionbarEtSearch, context);
                    actionbarEtSearch.setText("");
                    if (adapter_lv_main != null && adapter_lv_main.getList().size() > 0) {
                        adapter_lv_main.removeAll();
                        adapter_lv_main.notifyDataSetChanged();
                    }
                    addListData();
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
                V.d("输入框右边的搜索按钮被点击");
                if (!TextUtils.isEmpty(actionbarEtSearch.getText().toString())) {
                    jobName = actionbarEtSearch.getText().toString();
                    actionbarEtSearch.setText("");
                    Utils_RapidDev.closeKeybord(actionbarEtSearch, context);
                    if (adapter_lv_main != null && adapter_lv_main.getList().size() > 0) {
                        adapter_lv_main.removeAll();
                        adapter_lv_main.notifyDataSetChanged();
                    }
                    addListData();
                } else {
                    Utils.showToast(context, "输入不能为空，请重新输入");
                }
            }
        });

    }

    /**
     * 进入该方法时表示当前fragment是可见的，可以加载用户数据 注意：请做标示，当数据加载完一次后第二次不去加载
     */
    @Override
    public void loadData() {
        if (isAddData) {

        }
    }

    private void addListData() {
        calls.add(LoveJob.getWorkList("1", page, address, jobName, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                pullToRefreshJob.onRefreshComplete();
//                if (isAddData) pullToRefreshJob.scrollTo(0, 0);
                isAddData = false;
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getWorkInfoDTOs() != null) {
                    for (int i = 0; i < thePerfectGirl.getData().getWorkInfoDTOs().size(); i++) {
                        adapter_lv_main.addItem(thePerfectGirl.getData().getWorkInfoDTOs().get(i));
                    }
                    adapter_lv_main.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg) {
                pullToRefreshJob.onRefreshComplete();
            }
        }, time));
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
        String city = MyApplication.getCity();
        if (!TextUtils.isEmpty(city)) {
            actionbarTvLocationtext.setText(city);
        }
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

    @OnClick({R.id.actionbar_lt_location, R.id.actionbar_lt_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_lt_location:
                V.d("行政区域选择");
                context.startActivityForResult(new Intent(context, CityPickerActivity.class), StaticParams.RequestCode.RequestCode_F_Money_TO_CitySelector);
                break;
            case R.id.actionbar_lt_more:
                V.d("更多");
                AppManager.getAppManager().toNextPage(new Intent(context, Aty_SendMoneyWork.class), RequestCode_F_Money_TO_SendMoneyWork);
                break;
        }
    }

    private void addDataToNewsList() {
        calls.add(LoveJob.getNewsList("2", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                isAddData = false;
                if (thePerfectGirl.getData().getInformationInfos() != null)
                    newsList = new ArrayList<ThePerfectGirl.InformationInfo>();
                for (int i = 0; i < thePerfectGirl.getData().getInformationInfos().size(); i++) {
                    newsList.add(thePerfectGirl.getData().getInformationInfos().get(i));
                }
                rollViewPager.setAdapter(new TestNomalAdapter(newsList));
            }

            @Override
            public void onError(String msg) {
                V.e("money页面新闻数据加载失败：" + msg);
            }
        }));
    }

    private class TestNomalAdapter extends StaticPagerAdapter {
        private List<ThePerfectGirl.InformationInfo> imgs;

        public TestNomalAdapter(List<ThePerfectGirl.InformationInfo> newsList) {
            this.imgs = newsList;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            Glide.with(context).load(StaticParams.ImageNewsURL + imgs.get(position).getPictrueid()).dontAnimate().into(view);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return imgs.size();
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (F_Money.this.isVisible()) {
//            if (requestCode == StaticParams.RequestCode.RequestCode_F_Money_TO_CitySelector) {
//                address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败");
//                actionbarTvLocationtext.setText(address);
//                V.d("从城市选择页面返回");
//                if (adapter_lv_main != null && adapter_lv_main.getList().size() > 0) {
//                    adapter_lv_main.removeAll();
//                    adapter_lv_main.notifyDataSetChanged();
//                }
//                addListData();
//            } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Money_TO_NewsDetails) {
//                V.d("从money页面进入了新闻详情页面的返回");
//            }
//        }
//    }

    public class MyActvityResult extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.lovejob.onactivityresult")) {
                V.d("F_Money接收到广播");
                int requestCode = intent.getIntExtra("requestCode", -1);
                Intent data = intent.getSelector();

                if (F_Money.this.isVisible()) {
                    if (requestCode == StaticParams.RequestCode.RequestCode_F_Money_TO_CitySelector) {
                        address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败");
                        actionbarTvLocationtext.setText(address);
                        V.d("从城市选择页面返回");
                        if (adapter_lv_main != null && adapter_lv_main.getList().size() > 0) {
                            adapter_lv_main.removeAll();
                            adapter_lv_main.notifyDataSetChanged();
                        }
                        addListData();
                    } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Money_TO_NewsDetails) {
                        V.d("从money页面进入了新闻详情页面的返回");
                    } else if (requestCode == RequestCode_F_Money_TO_SendMoneyWork) {
                        V.d("从发布兼职工作页面返回到赚点现钱首页");
                    }
                }
            }
        }
    }


}