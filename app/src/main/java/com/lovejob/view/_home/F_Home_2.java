package com.lovejob.view._home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.lovejob.AppConfig;
import com.lovejob.BaseFragment;
import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.OnUpLoadImagesListener;
import com.lovejob.controllers.adapter.MyAdpater;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.HandlerUtils;
import com.lovejob.model.ImageModle;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.ThreadPoolUtils;
import com.lovejob.view._home.dyndetailstabs.NewsDetails;
import com.lovejob.view._job.SendJob;
import com.lovejob.view._money.Aty_SendMoneyWork;
import com.lovejob.view._money.MyTextVIew;
import com.lovejob.view._othersinfos.Others;
import com.lovejob.view._userinfo.myserver.ServiceActivity;
import com.lovejob.view._userinfo.myserver.ServiceMyActivity;
import com.lovejob.view.cityselector.CityPickerActivity;
import com.v.rapiddev.dialogs.core.util.DialogUtils;
import com.v.rapiddev.dialogs.zdialog.OnDialogItemClickListener;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lovejob.model.StaticParams.FileKey.__USERNAME__;
import static com.lovejob.model.StaticParams.FileKey.__USERPIC__;
import static com.lovejob.model.StaticParams.FileKey.__USERSEX__;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_F_Home_TO_DynDetails;
import static com.lovejob.model.StaticParams.RequestCode.RequestCode_F_Home_TO_SendDyn;

/**
 * ClassType:
 * User:wenyunzhao   todo
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
    private boolean isDataFromFile = false;
    private String userPid;
    private boolean isImagesUpLoadSuccess = true;
    private ThePerfectGirl.DynamicDTO sendDynDetailsDTO;

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

        com.v.rapiddev.preferences.AppPreferences appPreferences = new com.v.rapiddev.preferences.AppPreferences(context);
        userPid = appPreferences.getString(StaticParams.FileKey.__USERPID__, "");
        LoveJob.getSystemVersion(new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                String v = thePerfectGirl.getData().getAboutusDTO().getVersion();
                if (!Utils.getAppVersionName(context).equals(v)) {
                    ZDialog.showZDlialog(context, "提示", "系统版本有更新，是否现在更新？", "现在更新", "稍后更新", new OnDialogItemClickListener() {
                        @Override
                        public void onLeftButtonClickListener() {
                            V.d("更新");
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.lovejob#opened");
                            intent.setData(content_url);
                            startActivity(intent);
                        }

                        @Override
                        public void onRightButtonClickListener() {
                            V.d("取消");
                        }
                    });
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
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
                        if (!isImagesUpLoadSuccess) {
                            isDataFromFile = true;
                            mListAdapter.add(0, sendDynDetailsDTO);
//                            isImagesUpLoadSuccess = !isImagesUpLoadSuccess;
                        }
                        isDataFromFile = false;
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
        isDataFromFile = false;
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
                Glide.with(mContext).load(StaticParams.ImageNewsURL + item.getPictrueid()).dontAnimate().into((ImageView) helper.getView(R.id.item_gv_img));
                ((TextView) helper.getView(R.id.item_tv_tv)).setText(item.getTitle());
            }
        };
        mNewsAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        gv_home_news.setAdapter(mNewsAdapter);
        gv_home_news.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {

                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("newsId", ((ThePerfectGirl.InformationInfo) adapter.getData().get(position)).getPid());
                context.startActivityForResult(intent, StaticParams.RequestCode.RequestCode_F_Home_TO_NewsDetails);
                context.overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
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


    private void initlistAdapter() {
        mListAdapter = new BaseQuickAdapter<ThePerfectGirl.DynamicDTO, BaseViewHolder>(R.layout.item_rv_list, null) {
            @Override
            protected void convert(final BaseViewHolder viewHolder, final ThePerfectGirl.DynamicDTO item) {
                //添加基础数据
                ThePerfectGirl.UserInfoDTO userReleseInfo = item.getReleaseInfo();
                //用户头像
                CircleImageView userLogo = (CircleImageView) viewHolder.getView(R.id.roundview);
                if (isDataFromFile) {
                    Glide.with(mContext).load(new File(userReleseInfo.getPortraitId())).placeholder(R.mipmap.ic_launcher).dontAnimate().into(userLogo);
                } else {
                    Glide.with(mContext).load(StaticParams.ImageURL + userReleseInfo.getPortraitId()).dontAnimate().into(userLogo);
                }

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
                            if (isDataFromFile) {
                                imageInfos.add(new ImageInfo(backImgs[i], backImgs[i]));
                            } else {
                                imageInfos.add(new ImageInfo(StaticParams.ImageURL + backImgs[i] + "!logo", StaticParams.ImageURL + backImgs[i]));
                            }

                        }
                    }
                }
                isDataFromFile = false;
                NineGridView nine = (NineGridView) viewHolder.getView(R.id.images);
                if (imageInfos.size() == 0) {
                    nine.setVisibility(View.GONE);
                }
                nine.setAdapter(new NineGridViewClickAdapter(mContext, imageInfos));
                View view = viewHolder.getConvertView();
                //进入详情页面
                 /*用户头像*/
                view.findViewById(R.id.roundview).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        if (item.getDynamicPid() == null || TextUtils.isEmpty(item.getDynamicPid())) {
                            com.lovejob.model.Utils.showToast(context, "动态发布中，请稍后再试");
                            return;
                        }
                        Logger.d("进入" + item.getReleaseInfo().getRealName() + "的个人中心");
                        V.d("进入个人详情：" + item.getReleaseInfo().getRealName());
                        if (userPid.equals(item.getReleaseInfo().getUserId())) {

                        } else {
                            Intent intent = new Intent(context, Others.class);
                            intent.putExtra("userType", item.getReleaseInfo().getType());
                            intent.putExtra("userId", item.getReleaseInfo().getUserId());
                            startActivity(intent);
                        }
                    }
                });
                 /*进入服务窗*/
                view.findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        Logger.d("进入" + item.getReleaseInfo().getRealName() + "的服务窗");
                        if (item.getDynamicPid() == null || TextUtils.isEmpty(item.getDynamicPid())) {
                            com.lovejob.model.Utils.showToast(context, "动态发布中，请稍后再试");
                            return;
                        }
                        if (item.getReleaseInfo().getUserId().equals(userPid)) {
                            //进入自己
                            startActivity(new Intent(context, ServiceMyActivity.class));
                        } else {
                            Intent intent = new Intent(context, ServiceActivity.class);
                            intent.putExtra("userId", item.getReleaseInfo().getUserId());
                            startActivity(intent);
                        }
                    }
                });
                 /*评论*/
                view.findViewById(R.id.bt_comment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        Logger.d("给" + item.getReleaseInfo().getRealName() + "评论");
                        if (item.getDynamicPid() == null || TextUtils.isEmpty(item.getDynamicPid())) {
                            com.lovejob.model.Utils.showToast(context, "动态发布中，请稍后再试");
                            return;
                        }
                        Intent intent = new Intent(context, DynDetailsAty.class);
                        intent.putExtra("isComm", true);
                        intent.putExtra("dynPid", item.getDynamicPid());
                        startActivity(intent);
                    }
                });
                 /*差评*/
                view.findViewById(R.id.bt_bad).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        Logger.d("给" + item.getReleaseInfo().getRealName() + "差评");
                        if (item.getDynamicPid() == null || TextUtils.isEmpty(item.getDynamicPid())) {
                            com.lovejob.model.Utils.showToast(context, "动态发布中，请稍后再试");
                            return;
                        }
                        int resId = item.getIsPointGood() == 0 ? R.mipmap.icon_bad_common : R.mipmap.icon_bad_on;
                        viewHolder.setImageResource(R.id.img_bad, resId);
                        LoveJob.toDynGoodOrBad(item.getDynamicPid(), 0, new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                item.setIsPointGood(thePerfectGirl.getData().getPoints());
                                viewHolder.setText(R.id.tv_bad_num, String.valueOf(thePerfectGirl.getData().getCount()));
//                                ThePerfectGirl.DynamicDTO dynamicDTO = item;
//                                int res = 0;
//                                if (dynamicDTO.getIsPointGood() == 0) {
//                                    // 0 c\hap   1 keyi点赞
//                                    res = R.mipmap.icon_bad_common;
//                                } else {
//                                    res = R.mipmap.icon_bad_on;
//                                }
//                                dynamicDTO.setIsPointGood(thePerfectGirl.getData().getPoints());
//                                ((BaseQuickAdapter) mListAdapter).getData().set(viewHolder.getPosition(), dynamicDTO);
//                                View view = mRvHomeList.getChildAt(viewHolder.getPosition());
//                                TextView t = (TextView) view.findViewById(R.id.tv_bad_num);
//                                ImageView imageView = (ImageView) view.findViewById(R.id.img_bad);
//                                t.setText(String.valueOf(thePerfectGirl.getData().getCount()));
//                                imageView.setImageResource(res);
                            }

                            @Override
                            public void onError(String msg) {
                                com.lovejob.model.Utils.showToast(context, msg);
                                int resId = item.getIsPointGood() == 0 ? R.mipmap.icon_bad_on : R.mipmap.icon_bad_common;
                                viewHolder.setImageResource(R.id.img_bad, resId);
                            }
                        });
                    }
                });
                /*点赞*/
                view.findViewById(R.id.bt_good).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*用户头像*/
                        Logger.d("给" + item.getReleaseInfo().getRealName() + "点赞");
                        if (item.getDynamicPid() == null || TextUtils.isEmpty(item.getDynamicPid())) {
                            com.lovejob.model.Utils.showToast(context, "动态发布中，请稍后再试");
                            return;
                        }
                        int resId_bad = item.getIsPointGood() == 1 ? R.mipmap.icon_good_common : R.mipmap.icon_good_on;
                        viewHolder.setImageResource(R.id.img_good, resId_bad);
                        LoveJob.toDynGoodOrBad(item.getDynamicPid(), 1, new OnAllParameListener() {
                            @Override
                            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                                item.setIsPointGood(thePerfectGirl.getData().getPoints());
                                viewHolder.setText(R.id.tv_good_num, String.valueOf(thePerfectGirl.getData().getCount()));
//                                ThePerfectGirl.DynamicDTO oo = item;
//                                int res = 0;
//                                oo.setIsPointGood(thePerfectGirl.getData().getPoints());
//                                if (item.getIsPointGood() == 1) {
//                                    // 0 c\hap   1 keyi点赞
//                                    res = R.mipmap.icon_good_on;
//                                } else {
//                                    res = R.mipmap.icon_good_common;
//                                }
//                                ((BaseQuickAdapter) mListAdapter).getData().set(viewHolder.getPosition(), oo);
//                                View view = mRvHomeList.getChildAt(viewHolder.getPosition());
//                                TextView t = (TextView) view.findViewById(R.id.tv_good_num);
//                                ImageView imageView = (ImageView) view.findViewById(R.id.img_good);
//
//                                t.setText(String.valueOf(thePerfectGirl.getData().getCount()));
//                                imageView.setImageResource(res);
                            }

                            @Override
                            public void onError(String msg) {
                                com.lovejob.model.Utils.showToast(context, msg);
                                int resId_bad = item.getIsPointGood() == 1 ? R.mipmap.icon_good_on : R.mipmap.icon_good_common;
                                viewHolder.setImageResource(R.id.img_good, resId_bad);
                            }
                        });
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

                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        V.d("动态ID：" + item.getDynamicPid());
                        if (item.getDynamicPid() == null || TextUtils.isEmpty(item.getDynamicPid())) {
                            com.lovejob.model.Utils.showToast(context, "动态发布中，请稍后再试");
                            return;
                        }
                        Intent intent = new Intent(context, DynDetailsAty.class);
                        intent.putExtra("dynPid", item.getDynamicPid());
                        context.startActivityForResult(intent, RequestCode_F_Home_TO_DynDetails);
                        context.overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
                        intent = null;
                    }
                });
            }
        };
        /*设置加载动画*/
        mListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mListAdapter.isFirstOnly(false);
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
    public void onResume() {
        super.onResume();
        String city = MyApplication.getCity();
        if (!TextUtils.isEmpty(city)) {
            mActionbarTvLocationtext.setText(city);
        }
        if (!MyApplication.getAppPreferences().getString(StaticParams.FileKey.__City__, "").equals("西安市")) {
            MyApplication.getAppPreferences().put(StaticParams.FileKey.__City__, "西安市");
            mActionbarTvLocationtext.setText("西安市");
//            page = 1;
//            mSwipeLayout.setRefreshing(true);
//            addDataToDynList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context.unregisterReceiver(broadcastReceiver);
        ButterKnife.unbind(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("sendDynDTO", sendDynDetailsDTO);
    }

    @OnClick({R.id.actionbar_lt_location, R.id.actionbar_lt_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_lt_location:
                //打开行政区域选择页面
                context.startActivityForResult(new Intent(context, CityPickerActivity.class), StaticParams.RequestCode.RequestCode_F_Home_TO_CitySelector);
                break;
            case R.id.actionbar_lt_more:
                //展开泡泡窗口
                showPopWindow(view);
                break;
        }
    }

    private void showPopWindow(View popview) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popview, null);
        LinearLayout layout_send_dyn = (LinearLayout) contentView.findViewById(R.id.layout_sendDyn);
        LinearLayout layout_send_work_short = (LinearLayout) contentView.findViewById(R.id.layout_sendwork_short);
        LinearLayout layout_send_work_long = (LinearLayout) contentView.findViewById(R.id.layout_sendwork_long);
        final PopupWindow window = new PopupWindow(contentView,
                340,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        window.setContentView(contentView);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.showAsDropDown(popview);
        layout_send_dyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                V.d("发布动态");
                window.dismiss();
                //TODO 跳转发布动态页面
                if (isImagesUpLoadSuccess) {
                    context.startActivityForResult(new Intent(context, SendDynamic.class), StaticParams.RequestCode.RequestCode_F_Home_TO_SendDyn);
                } else {
                    com.lovejob.model.Utils.showToast(context, "正在发布动态，请稍后");
                }
            }
        });
        layout_send_work_short.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                V.d("发布创意工作");
                window.dismiss();
                startActivity(new Intent(context, Aty_SendMoneyWork.class));
            }
        });
        layout_send_work_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                V.d("发布长期工作");
                window.dismiss();
                startActivity(new Intent(context, SendJob.class));
            }
        });
    }

    public class MyActvityResult extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {

            if (intent.getAction().equals("com.lovejob.onactivityresult")) {
                V.d("F_Home接收到广播");
                int requestCode = intent.getIntExtra("requestCode", -1);
                if (F_Home_2.this.isVisible()) {
                    if (requestCode == StaticParams.RequestCode.RequestCode_F_Home_TO_CitySelector) {
                        V.d("从城市选择页面返回");
                        mActionbarTvLocationtext.setText(new com.v.rapiddev.preferences.AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败"));
                        mListAdapter.setNewData(null);
                        addDataToDynList();
                    } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Home_TO_NewsDetails) {
                        V.d("从新闻详情页面返回");
                    } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Home_TO_SendDyn) {
                        V.d("从发布动态页面返回,开始发布动态");
                        if (intent.getBooleanExtra("isRefresh", false)) {
                            //构建新数据
                            List<String> images = intent.getStringArrayListExtra("sendDynData");
                            if (images != null && images.size() > 0) {

                                ThreadPoolUtils.getInstance().addTask(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            final GlideDrawable drawable = Glide.with(context).load(R.drawable.uploading).into(60, 60).get();
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mActionbarImgMore.setImageDrawable(drawable);
                                                }
                                            });
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });


                                isImagesUpLoadSuccess = false;
                                StaticParams.isHaveNotDoSomeThing = true;
                                senDyn(images, intent.getStringExtra("address"), intent.getStringExtra("content"),
                                        intent.getStringExtra("lat"), intent.getStringExtra("lng"));
                                StringBuffer stringBuffer = new StringBuffer();
                                for (int i = 0; i < images.size(); i++) {
                                    stringBuffer.append(images.get(i)).append("|");
                                }
                                isDataFromFile = true;
                                String username = new AppPreferences(context).getString(__USERNAME__, "");
                                String sex = new AppPreferences(context).getString(__USERSEX__, "男");
                                String pic = new AppPreferences(context).getString(__USERPIC__, "");
                                sendDynDetailsDTO = new ThePerfectGirl.DynamicDTO(
                                        0, 0, intent.getStringExtra("content"), "", 0, "", "", "", "", 0, false, 0, 0.0, 0.0,
                                        stringBuffer.toString(), "", new ThePerfectGirl.UserInfoDTO(
                                        "", 0, "", 0, 0, "公司", 0, 0, 0, 0, 0.0, 0, "在职", 0, 0, 0, "", pic, "职位", 0, 0, username, 0, sex, "", "", "", null, 0
                                ), ""
                                );
                                mListAdapter.add(0, sendDynDetailsDTO);
                            } else {
                                mSwipeLayout.setRefreshing(true);
                                addDataToDynList();
                            }
                        }
                        V.d("isRefresh:" + intent.getBooleanExtra("isRefresh", false));
                    }
                }
            }
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

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


    private void senDyn(final List<String> selectedPhotos, final String address, final String content, final String lat, final String lng) {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < selectedPhotos.size(); i++) {
            files.add(new File(selectedPhotos.get(i)));
        }
        com.lovejob.model.Utils.ImageCo(files, context, true, new OnUpLoadImagesListener() {
            @Override
            public void onSucc(List<ImageModle> imageModleList) {
//                dialog.setContent("发布中……");
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < imageModleList.size(); i++) {
                    stringBuffer.append(imageModleList.get(i).getSmallFileName()).append("|");
                }
                LoveJob.sendDyn(address, content, lat, lng, stringBuffer.toString(), new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        isImagesUpLoadSuccess = true;
                        StaticParams.isHaveNotDoSomeThing = false;
                        mActionbarImgMore.setImageResource(R.mipmap.more_01);
                        new com.v.rapiddev.preferences.AppPreferences(context).put(StaticParams.FileKey.__DynamicContent__, "");
                        com.lovejob.model.Utils.showToast(context, "图片上传成功");
                        mSwipeLayout.setRefreshing(true);
                        page = 1;
                        addDataToDynList();
                        isDataFromFile = false;

                    }

                    @Override
                    public void onError(String msg) {
                        try {
                            com.lovejob.model.Utils.showToast(context, msg);

                            ZDialog.showZDlialog(context, "提示", "动态发布失败，是否发起重试？", "重试", "取消", R.mipmap.icon_shang, new OnDialogItemClickListener() {
                                @Override
                                public void onLeftButtonClickListener() {
                                    senDyn(selectedPhotos, address, content, lat, lng);
                                }

                                @Override
                                public void onRightButtonClickListener() {
                                    mActionbarImgMore.setImageResource(R.mipmap.more_01);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError() {
                com.lovejob.model.Utils.showToast(context, "图片上传失败，请稍后再试");
                ZDialog.showZDlialog(context, "提示", "动态发布失败，是否发起重试？", "重试", "取消", R.mipmap.icon_shang, new OnDialogItemClickListener() {
                    @Override
                    public void onLeftButtonClickListener() {
                        senDyn(selectedPhotos, address, content, lat, lng);
                    }

                    @Override
                    public void onRightButtonClickListener() {
                        mActionbarImgMore.setImageResource(R.mipmap.more_01);
                    }
                });
            }
        });
    }
}
