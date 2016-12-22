package com.lovejob.view._home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lovejob.AppConfig;
import com.lovejob.BaseFragment;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.view._home.dyndetailstabs.NewsDetails;
import com.lovejob.view._money.MyTextVIew;
import com.v.rapiddev.utils.V;
import com.zwy.Utils;
import com.zwy.activitymanage.AppManager;
import com.zwy.logger.Logger;
import com.zwy.nineimageslook.ImageInfo;
import com.zwy.nineimageslook.NineGridView;
import com.zwy.nineimageslook.preview.NineGridViewClickAdapter;
import com.zwy.preferences.AppPreferences;
import com.zwy.pulltorefresh.BaseQuickAdapter;
import com.zwy.pulltorefresh.BaseViewHolder;
import com.zwy.pulltorefresh.listener.OnItemClickListener;
import com.zwy.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.view._home
 * Created on 2016-11-25 16:46
 */
public class F_Home_2 extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    View view;
    @Bind(R.id.actionbar_tv_locationtext)
    TextView mActionbarTvLocationtext;
    @Bind(R.id.actionbar_lt_location)
    LinearLayout mActionbarLtLocation;
    @Bind(R.id.actionbar_img_search)
    ImageView mActionbarImgSearch;
    @Bind(R.id.actionbar_et_search)
    EditText mActionbarEtSearch;
    @Bind(R.id.actionbar_img_search_right)
    ImageView mActionbarImgSearchRight;
    @Bind(R.id.actionbar_img_more)
    ImageView mActionbarImgMore;
    @Bind(R.id.actionbar_lt_more)
    LinearLayout mActionbarLtMore;
    @Bind(R.id.rv_home_list)
    RecyclerView mRvHomeList;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    private MyActvityResult broadcastReceiver;
    private BaseQuickAdapter<ThePerfectGirl.InformationInfo, BaseViewHolder> mNewsAdapter;//新闻适配器
    private BaseQuickAdapter<ThePerfectGirl.DynamicDTO, BaseViewHolder> mListAdapter;//列表适配器
    private boolean isBackFromCitySelector = false;
    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_home_2, null);
        ButterKnife.bind(this, view);

        broadcastReceiver = new MyActvityResult();
        IntentFilter intentFilter = new IntentFilter("com.lovejob.onactivityresult");
        context.registerReceiver(broadcastReceiver, intentFilter);
        /**
         * 设置页面默认数据
         */
        setDefaultParam();
        initlistAdapter();
        /**
         * 获取新闻
         */
        addDataToNewsList();
        /**
         * 给列表添加数据
         */
        addDataToDynList();
        mSwipeLayout.setOnRefreshListener(this);
        mListAdapter.setOnLoadMoreListener(this);
        return view;
    }
    private int page = 1;
    private String userInputSearchText;
    private void addDataToDynList() {
        com.v.rapiddev.preferences.AppPreferences appPreferences = new com.v.rapiddev.preferences.AppPreferences(context);
        String address = appPreferences.getString(StaticParams.FileKey.__City__, "");
        String lng = appPreferences.getString(StaticParams.FileKey.__LONGITUDE__, "");
        String lat = appPreferences.getString(StaticParams.FileKey.__LATITUDE__, "");

        callList.add(LoveJob.getDynList(String.valueOf(page), address, lng, lat, userInputSearchText, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (thePerfectGirl.getData() != null && thePerfectGirl.getData().getDynamicDTOList() != null
                        && thePerfectGirl.getData().getDynamicDTOList().size() > 0) {
//                        try {
                    //是否正在刷新
                    if (mSwipeLayout.isRefreshing()) {
                        //设置数据
                        mListAdapter.setNewData(thePerfectGirl.getData().getDynamicDTOList());
                        //关闭动画
                        mSwipeLayout.setRefreshing(false);
                        //开启加载更多
                        mListAdapter.setEnableLoadMore(true);
                    } else {
                        mListAdapter.addData(thePerfectGirl.getData().getDynamicDTOList());
                        mListAdapter.loadMoreComplete();
                    }
                    mSwipeLayout.setEnabled(true);

                } else {
                    //开启加载更多
                    mListAdapter.loadMoreEnd(false);
                    if (isBackFromCitySelector) {
//                            mListAdapter.setNewData(null);
                        isBackFromCitySelector = false;
                        if (mSwipeLayout.isRefreshing()) {
                            mListAdapter.setNewData(null);
                        }
                    }
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout.setEnabled(true);
                }
            }

            @Override
            public void onError(String msg) {
                mListAdapter.loadMoreComplete();
                //关闭动画
                mSwipeLayout.setRefreshing(false);
                //开启加载更多
                mListAdapter.setEnableLoadMore(true);
                mListAdapter.loadMoreFail();
                page = page - 1;
                mSwipeLayout.setEnabled(true);
            }
        }));
    }

    private void addDataToNewsList() {
        callList.add(LoveJob.getNewsList("0", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                for (int i = 0; i < thePerfectGirl.getData().getInformationInfos().size(); i++) {
//                    adapter_news.addItem(thePerfectGirl.getData().getInformationInfos().get(i));
//                }
//                adapter_news.notifyDataSetChanged();
                List<ThePerfectGirl.InformationInfo> mInformationInfos = thePerfectGirl.getData().getInformationInfos();
                mNewsAdapter.setNewData(mInformationInfos);
            }

            @Override
            public void onError(String msg) {
                V.e("新闻列表为空");
            }
        }));
    }

    /**
     * 上啦加载更多事件
     */
    @Override
    public void onLoadMoreRequested() {
        mSwipeLayout.setEnabled(false);
        page++;
        addDataToDynList();
    }

    /**
     * 下拉刷新事件
     */
    @Override
    public void onRefresh() {
        //设置不可加载更多
        mListAdapter.setEnableLoadMore(false);
        page = 1;
        addDataToNewsList();
        addDataToDynList();
    }

    private void setDefaultParam() {
//        AppPreferences preferences =new AppPreferences(context);
//        mActionbarTvLocationtext.setText(preferences.getString(AppConfig.PreferencesKey.LocationKey.City, "点击定位"));
//        mActionbarEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                String keyText = mActionbarEtSearch.getText().toString();
//                Utils.closeKeybord(mActionbarEtSearch, mContext);
//                if (keyText != null && !TextUtils.isEmpty(keyText.trim())) {
//                    //TODO 刷新适配器 重新拉取数据
//                    userName = keyText;
//                    mActionbarEtSearch.setText("");
//                    mSwipeLayout.setRefreshing(true);
//                    addDataToDynList();
//                } else {
//                    Toast.error("输入不能为空");
//                }
//                return false;
//            }
//        });
//        mSwipeLayout.setRefreshing(true);
    }
    private View getNewsView() {
        View view = LayoutInflater.from(context).inflate(R.layout.news_home, null);
        RecyclerView gv_home_news = (RecyclerView) view.findViewById(R.id.gv_home_news);
        initNewsInfo(gv_home_news);
        return view;
    }
    private void initNewsInfo(RecyclerView gv_home_news) {
        gv_home_news.setLayoutManager(new GridLayoutManager(context, 3));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        gv_home_news.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        mNewsAdapter = new BaseQuickAdapter<ThePerfectGirl.InformationInfo, BaseViewHolder>(R.layout.item_home_news, null) {
            /**
             * Implement this method and use the helper to adapt the view to the given item.
             *
             * @param helper A fully initialized helper.
             * @param item   The item that needs to be displayed.
             */
            @Override
            protected void convert(BaseViewHolder helper, ThePerfectGirl.InformationInfo item) {
                Glide.with(mContext).load(StaticParams.QiNiuYunUrl_News + item.getPictrueid()).dontAnimate().into((ImageView) helper.getView(R.id.item_gv_img));
                ((TextView) helper.getView(R.id.item_tv_tv)).setText(item.getTitle());
            }
        };
        mNewsAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        gv_home_news.setAdapter(mNewsAdapter);
        gv_home_news.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
//                Intent intent = new Intent(context, NewsDetails.class);
//                intent.putExtra("newsId", ((CxwlResponseData.InformationInfo) adapter.getItem(position)).getPid());
//                AppManager.getAppManager().toNextPage(intent, ActivityRequestCode.R_C_FHome_NewsDetails);
            }

//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                android.widget.Toast.makeText(getApplicationContext(), "hahah:" + ((TestData) adapter.getItem(position)).getTitle(), android.widget.Toast.LENGTH_LONG).show();
//            }
        });
    }

    /**
     * 进入该方法时表示当前fragment是可见的，可以加载用户数据 注意：请做标示，当数据加载完一次后第二次不去加载
     */
    @Override
    public void loadData() {

    }


    private void initlistAdapter(){
        mListAdapter = new BaseQuickAdapter<ThePerfectGirl.DynamicDTO, BaseViewHolder>(R.layout.item_rv_list, null) {
            @Override
            protected void convert(final BaseViewHolder viewHolder, final ThePerfectGirl.DynamicDTO item) {
//                Glide.with(mContext).load(AppConfig.QiNiuYunUrl+item.getReleaseInfo().getPortraitId())
//                        .placeholder(R.mipmap.ic_launcher).into((CircleImageView) helper.getView(R.id.roundview));
                //添加基础数据
                ThePerfectGirl.UserInfoDTO userReleseInfo = item.getReleaseInfo();
                //用户头像
                CircleImageView userLogo = (CircleImageView) viewHolder.getView(R.id.roundview);
                String url = StaticParams.QiNiuYunUrl + userReleseInfo.getPortraitId();
//                Log.d ("F_Home", "url:"+url);
//                Log.d ("F_Home", "width:"+userLogo.getWidth ());
//                userLogo.measure (0,0);
                Glide.with(mContext).load(url).dontAnimate().placeholder(R.mipmap.ic_launcher).into(userLogo);
                //性别
                int resSex = userReleseInfo.getSexDec().equals("男") ? R.mipmap.icon_male : R.mipmap.icon_famale;
                ((ImageView) viewHolder.getView(R.id.img_sex)).setImageResource(resSex);
                //姓名
                ((TextView) viewHolder.getView(R.id.tv_userName)).setText(userReleseInfo.getRealName());
                //等级
                ((ImageView) viewHolder.getView(R.id.img_level)).setImageResource(getUserLever(userReleseInfo.getLevel()));

                //距离
                ((TextView) viewHolder.getView(R.id.tv_distence)).setText(item.getDistance());
                String commpl = TextUtils.isEmpty(userReleseInfo.getCompany()) ? "公司未填写" : userReleseInfo.getCompany();
                final String positions = TextUtils.isEmpty(userReleseInfo.getPosition()) ? "职位未填写" : userReleseInfo.getPosition();
                String textview_commple = commpl + "/" + positions;
                //职位
                ((MyTextVIew) viewHolder.getView(R.id.tv_userjob)).setText(textview_commple.trim());
                //TODO 在职状态
                TextView vv = (TextView) viewHolder.getView(R.id.jobstate);
                if (!userReleseInfo.getJobState().equals("在职")) {
                    vv.setVisibility(View.VISIBLE);
                    vv.setText(userReleseInfo.getJobState());
                } else {
                    vv.setVisibility(View.INVISIBLE);
                }
                //文本 TODO
//                String content = dynamicDTO.getContent();
                final TextView tv_content = (TextView) viewHolder.getView(R.id.tv_content);
//                tv_content.setText("这段时间被首页布局要气死了，真是曰了狗了，不是数据错乱就是一大片空白，只求一死，啊啊啊啊啊。这段时间被首页布局要气死了，真是曰了狗了，不是数据错乱就是一大片空白，只求一死，啊啊啊啊啊。这段时间被首页布局要气死了，真是曰了狗了，不是数据错乱就是一大片空白，只求一死，啊啊啊啊啊。这段时间被首页布局要气死了，真是曰了狗了，不是数据错乱就是一大片空白，只求一死，啊啊啊啊啊。");
                tv_content.setText(item.getContent());
                final TextView isShowMoreText = (TextView) viewHolder.getView(R.id.isShowMoreText);
                if (tv_content.length() > 90) {
                    isShowMoreText.setVisibility(View.VISIBLE);
                } else {
                    isShowMoreText.setVisibility(View.GONE);
//                    tv_content.setText("     " + content);
                }
                //是否点亮 评论 差评 或 点赞 按钮图标  isPointGood=2 什么都可以干   1：差评亮  0：点赞亮
                //评论
                ImageView img_comment = (ImageView) viewHolder.getView(R.id.img_comment);
                TextView tv_comment_num = (TextView) viewHolder.getView(R.id.tv_comment_num);
                //评论数
                tv_comment_num.setText(String.valueOf(item.getCommentCount()));
                //差评
                final ImageView img_bad = (ImageView) viewHolder.getView(R.id.img_bad);
                TextView tv_bad_num = (TextView) viewHolder.getView(R.id.tv_bad_num);
                //差评
                tv_bad_num.setText(String.valueOf(item.getBadCount()));

                //点赞
                final ImageView img_good = (ImageView) viewHolder.getView(R.id.img_good);
                TextView tv_good_num = (TextView) viewHolder.getView(R.id.tv_good_num);
                //点赞数
                tv_good_num.setText(String.valueOf(item.getGoodCount()));
                switch (item.getIsPointGood()) {
                    case 0:
//                        img_good.setImageResource(R.mipmap.icon_good_on);
                        img_bad.setImageResource(R.mipmap.icon_bad_on);
                        img_good.setImageResource(R.mipmap.icon_good_common);

                        break;
                    case 1:
//                        img_bad.setImageResource(R.mipmap.icon_bad_on);
                        img_good.setImageResource(R.mipmap.icon_good_on);
                        img_bad.setImageResource(R.mipmap.icon_bad_common);

                        break;
                    case 2:
                        img_bad.setImageResource(R.mipmap.icon_bad_common);
                        img_good.setImageResource(R.mipmap.icon_good_common);
                        break;
                }

                List<ImageInfo> imageInfos = new ArrayList<>();
                if (item.getPictrueid() != null && !TextUtils.isEmpty(item.getPictrueid())) {
                    String[] backImgs = item.getPictrueid().split("\\|");
                    for (int i = 0; i < backImgs.length; i++) {
                        if (!TextUtils.isEmpty(backImgs[i])) {
                            imageInfos.add(new ImageInfo(StaticParams.QiNiuYunUrl + backImgs[i], StaticParams.QiNiuYunUrl + backImgs[i]));
                        }
                    }
                }
                NineGridView nine = (NineGridView) viewHolder.getView(R.id.images);
                if (imageInfos.size() == 0) {
                    nine.setVisibility(View.GONE);
                }
                nine.setAdapter(new NineGridViewClickAdapter(mContext, imageInfos));
                View view = viewHolder.getConvertView();
                //进入详情页面
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.d("进入" + item.getReleaseInfo().getRealName() + "详情页面");
////                        Toast.success("进入" + item.getReleaseInfo().getRealName() + "详情页面");
//                        Intent intent = new Intent(mContext, Dyntails.class);
//                        intent.putExtra("dynPid", item.getDynamicPid());
//                        AppManager.getAppManager().toNextPage(intent, false);
                    }
                });
                //设置item中的icon点击事件
                 /*用户头像*/
                view.findViewById(R.id.roundview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        Logger.d("进入" + item.getReleaseInfo().getRealName() + "的个人中心");
//                        Toast.success("进入" + item.getReleaseInfo().getRealName() + "的个人中心");
                    }
                });
                 /*进入服务窗*/
                view.findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        Logger.d("进入" + item.getReleaseInfo().getRealName() + "的服务窗");
//                        Toast.success("进入" + item.getReleaseInfo().getRealName() + "的服务窗");
                    }
                });
                 /*评论*/
                view.findViewById(R.id.bt_comment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        Logger.d("给" + item.getReleaseInfo().getRealName() + "评论");
//                        Toast.success("给" + item.getReleaseInfo().getRealName() + "评论");
//                        Intent intent =new Intent(mContext,Dyntails.class);
//                        intent.putExtra("index",1);
//                        intent.putExtra("dynPid", item.getDynamicPid());
//
//                        AppManager.getAppManager().toNextPage(intent,false);
                    }
                });
                 /*差评*/
                view.findViewById(R.id.bt_bad).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        Logger.d("给" + item.getReleaseInfo().getRealName() + "差评");
//                        switch (item.getIsPointGood()) {
//                            case 0:
//                                //差评过
//                                img_bad.setImageResource(R.mipmap.icon_bad_common);
//                                ApiClient.toDynBad(item.getDynamicPid());
//                                break;
//                            case 1:
//                                //点过赞
//                                Toast.error("您已点赞");
//                                break;
//                            case 2:
//                                //点亮差评
//                                img_bad.setImageResource(R.mipmap.icon_bad_on);
//                                //调用点赞接口
//                                ApiClient.toDynBad(item.getDynamicPid());
//                                break;
//                        }
//                        mViewHolder = viewHolder;
//                        mItem = item;
                    }
                });
                /*点赞*/
                view.findViewById(R.id.bt_good).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        Logger.d("给" + item.getReleaseInfo().getRealName() + "点赞");
//                        switch (item.getIsPointGood()) {
//                            case 0:
//                                //差评过
//                                Toast.error("您已差评");
//                                break;
//                            case 1:
//                                //点过赞
//                                img_good.setImageResource(R.mipmap.icon_good_common);
//                                //取消点赞接口
//                                ApiClient.toDynGood(item.getDynamicPid());
//                                break;
//                            case 2:
//                                //点亮点赞
//                                img_good.setImageResource(R.mipmap.icon_good_on);
//                                //调用点赞接口
//                                ApiClient.toDynGood(item.getDynamicPid());
//                                break;
//                        }
//                        mViewHolder = viewHolder;
//                        mItem = item;
                    }
                });
                /*全文*/
//                isShowMoreText =(TextView)    view.findViewById(R.id.isShowMoreText);
                isShowMoreText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isShowMoreText.getText().toString().equals("全文")) {
                            tv_content.setMaxLines(20);
                            isShowMoreText.setText("收起");
                        } else {
                            tv_content.setMaxLines(4);
                            isShowMoreText.setText("全文");
                        }
                    }
                });
            }
        };
        /*设置加载动画*/
        mListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
//        mRvList.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        /*设置适配器*/
        mRvHomeList.setAdapter(mListAdapter);
        /*设置头布局*/
        mListAdapter.addHeaderView(getNewsView());
        /*设置加载动画*/
//        mRvList.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        mRvHomeList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context.unregisterReceiver(broadcastReceiver);
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.actionbar_lt_location, R.id.actionbar_lt_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_lt_location:
                break;
            case R.id.actionbar_lt_more:
                break;
        }
    }

    public class MyActvityResult extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.lovejob.onactivityresult")) {
                V.d("F_Home接收到广播");
                int requestCode = intent.getIntExtra("requestCode", -1);
                Intent data = intent.getSelector();
//                if (F_Home_2.this.isVisible()) {
//                    if (requestCode == StaticParams.RequestCode.RequestCode_F_Home_TO_CitySelector) {
//                        V.d("从城市选择页面返回");
//                        actionbarTvLocationtext.setText(new AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败"));
//                        if (adapter_dynList != null && adapter_dynList.getList().size() > 0) {
//                            adapter_dynList.removeAll();
//                            adapter_dynList.notifyDataSetChanged();
//                        }
//                        addDataToDynList();
//                    } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Home_TO_NewsDetails) {
//                        V.d("从新闻详情页面返回");
//                    } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Home_TO_SendDyn) {
//                        V.d("从发布动态页面返回");
//                        if (intent.getBooleanExtra("isRefresh", false)) {
//                            if (adapter_dynList != null && adapter_dynList.getList().size() > 0) {
//                                adapter_dynList.removeAll();
//                            }
//                            addDataToDynList();
//                        }
//                        V.d("isRefresh:" + intent.getBooleanExtra("isRefresh", false));
//                    }
//                }
            }
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

//        if(parent.getChildPosition(view) != 0)
            outRect.right = space;
            outRect.top = 1;
            outRect.left = space;

        }
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
}
