package com.lovejob.view._job;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.lovejob.view._home.F_Home_2;
import com.lovejob.view._home.dyndetailstabs.NewsDetails;
import com.lovejob.view._othersinfos.Others;
import com.lovejob.view.cityselector.CityPickerActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.dialogs.zdialog.OnDialogItemClickListener;
import com.v.rapiddev.dialogs.zdialog.ZDialog;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.InputMethodLayout;
import com.zwy.pulltorefresh.BaseQuickAdapter;
import com.zwy.pulltorefresh.BaseViewHolder;
import com.zwy.pulltorefresh.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建时间：2016-12-26 21:02
 * 创建人：赵文贇
 * 类描述：
 * 包名：lovejob11
 */

public class F_Job_2 extends BaseFragment {
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
    @Bind(R.id.rv_job)
    RecyclerView mRvJob;
    @Bind(R.id.pull_to_refresh_job)
    PullToRefreshScrollView mPullToRefreshJob;
    @Bind(R.id.editkeybox)
    InputMethodLayout mEditkeybox;
    private View mainView;
    private MyAdapter mRvAdapter;
    private RollPagerView gv_home_news;
    private int page = 1;//请求的页数 默认1
    private String address;//定位的地址 可为空
    private String jobName;//用户搜索框的内容 可为空
    private TestNomalAdapter newAdapter;
    private boolean isAdd = false;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.f_job_2, null);
        ButterKnife.bind(this, mainView);
        /**
         * 设置适配器相关
         */
        setActionbar();
        mRvAdapter = new MyAdapter(null);
        mRvJob.setLayoutManager(new LinearLayoutManager(context));
        mRvJob.setAdapter(mRvAdapter);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
//        mRvJob.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        mRvAdapter.addHeaderView(getNewsView());
//        mRvAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adDataToJobList();
        setRefreshListener();
        setOnItemClickListener();
        return mainView;
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

    private void setOnItemClickListener() {
        mRvJob.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), JobDetails.class);
                intent.putExtra("workId", mRvAdapter.getItem(position).getPid());
                startActivity(intent);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.jianghu:

                        ZDialog.showZDlialog(context, "确认分享吗", "请在分享成功后及时返回app以保证您分享所得收益正常到账", "分享", "取消", R.mipmap.icon_changqigongzuo_jianghu, new OnDialogItemClickListener() {
                            @Override
                            public void onLeftButtonClickListener() {
                                //分享
                                toShard(mRvAdapter.getItem(position).getPid());
                            }

                            @Override
                            public void onRightButtonClickListener() {
                                Utils.showToast(context, "您取消了分享");
                            }
                        });
                        break;
                    case R.id.xuanshang:
                        ZDialog.showZDlialog(context, "是否接令", "悬赏" + mRvAdapter.getItem(position).getList()
                                .get(0).getMoney() + "元帮助" + mRvAdapter.getItem(position).getReleaseInfo()
                                .getCompany() + "招聘" + mRvAdapter.getItem(position).getTitle()
                                + "，招聘到合适的人上岗10天签订合同后，方能向商家索要赏金哦~", "接令", "取消", R.mipmap.icon_shang, new OnDialogItemClickListener() {
                            @Override
                            public void onLeftButtonClickListener() {
                                V.d("接令");
                                dialog = Utils.showProgressDliago(context, "正在抢令");
                                callList.add(LoveJob.getJobToken_1("0", mRvAdapter.getItem(position).getPid(), new OnAllParameListener() {
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
                        break;
                    case R.id.img_job_logo:
                        if (new AppPreferences(context).getString(StaticParams.FileKey.__USERPID__, "")
                                .equals(mRvAdapter.getItem(position).getReleaseInfo().getUserId())) {

                        } else {
                            Intent intent = new Intent(context, Others.class);
                            intent.putExtra("userType", mRvAdapter.getItem(position).getReleaseInfo().getType());
                            intent.putExtra("userId", mRvAdapter.getItem(position).getReleaseInfo().getUserId());
                            startActivity(intent);
                        }
                        break;

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gv_home_news != null)
            gv_home_news.resume();
        String city = MyApplication.getCity();
        if (!TextUtils.isEmpty(city)) {
            mActionbarTvLocationtext.setText(city);
        }

    }

    private void toShard(final String workPid) {
//        new ShareAction(context).withTargetUrl("http://192.168.3.8:8081/test?toOtherActivity=0&otherId=" + workPid)
        new ShareAction(context).withText(workPid)

//                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .withTargetUrl("http://xiechaobin.xicp.io/AAAa/text.html?otherId=" + workPid + "&toOtherActivity=1")
                .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
//                        Utils.showToast(context, "分享成功");
                        V.d("分享成功");
                        dialog = Utils.showProgressDliago(context, "请稍后……");
                        callList.add(LoveJob.getJobToken_1("1", workPid, new OnAllParameListener() {
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

    private void setActionbar() {
        address = MyApplication.getCity();
        mActionbarImgMore.setImageResource(R.mipmap.topbar_edit);
        mActionbarImgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SendJob.class);
                context.startActivityForResult(intent, StaticParams.RequestCode.RequestCode_F_Job_TO_SendJob);
            }
        });

    }

    private void adDataToJobList() {
        callList.add(LoveJob.getWorkList("0", page, address, jobName, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
//                if (isAddDate) pullToRefreshJob.scrollTo(0,0);
                mPullToRefreshJob.onRefreshComplete();
                List<ThePerfectGirl.WorkInfoDTO> workInfoDTOs = thePerfectGirl.getData().getWorkInfoDTOs();
                if (workInfoDTOs == null || workInfoDTOs.size() == 0) {
                    Utils.showToast(context, "没有更多工作信息");
                    return;
                }
                if (isAdd) {
                    isAdd = false;
                    mRvAdapter.addData(workInfoDTOs);
                } else {
                    mRvAdapter.setNewData(workInfoDTOs);
                }
//                for (int i = 0; i < workInfoDTOs.size(); i++) {
//                    adapter_lv.addItem(workInfoDTOs.get(i));
//                }
//                adapter_lv.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                mPullToRefreshJob.onRefreshComplete();
                Utils.showToast(context, msg);
            }
        }));
    }

    /**
     * 进入该方法时表示当前fragment是可见的，可以加载用户数据 注意：请做标示，当数据加载完一次后第二次不去加载
     */
    @Override
    public void loadData() {

    }

    private void addDataToNewsList() {
        callList.add(LoveJob.getNewsList("1", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                ArrayList<ThePerfectGirl.InformationInfo> newsList = null;
                if (thePerfectGirl.getData().getInformationInfos() != null)
                    newsList = new ArrayList<ThePerfectGirl.InformationInfo>();
                for (int i = 0; i < thePerfectGirl.getData().getInformationInfos().size(); i++) {
                    newsList.add(thePerfectGirl.getData().getInformationInfos().get(i));
                }
                newAdapter = new TestNomalAdapter(newsList);
                gv_home_news.setAdapter(newAdapter);
            }

            @Override
            public void onError(String msg) {
                V.e("job页面新闻数据加载失败：" + msg);
            }
        }));
    }

    private View getNewsView() {
        View view = LayoutInflater.from(context).inflate(R.layout.vp_news_job, null);
        gv_home_news = (RollPagerView) view.findViewById(R.id.roll_view_pager);
//        initNewsInfo(gv_home_news);
        addDataToNewsList();
        gv_home_news.setOnItemClickListener(new com.jude.rollviewpager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String newsId = newAdapter.getItem(position).getPid();
                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("newsId", newsId);
                context.startActivityForResult(intent, StaticParams.RequestCode.RequestCode_F_Job_TO_NewsDetails);
                context.overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.actionbar_lt_location, R.id.actionbar_lt_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_lt_location:
                context.startActivityForResult(new Intent(context, CityPickerActivity.class), StaticParams.RequestCode.RequestCode_F_Job_TO_CitySelector);
                break;
            case R.id.actionbar_lt_more:
                break;
        }
    }

    private void setRefreshListener() {
        mPullToRefreshJob.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshJob.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                StaticParams.ROWS = "5";
                jobName = null;
                address = new AppPreferences(context).getString(StaticParams.FileKey.__City__, "未知");
//                mRvAdapter.re();
//                adapter_lv.notifyDataSetChanged();
                mRvAdapter.setNewData(null);
                adDataToJobList();
                addDataToNewsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
//                StaticParams.ROWS = "20";
                isAdd = true;
                adDataToJobList();
            }
        });
    }

    private class MyAdapter extends BaseQuickAdapter<ThePerfectGirl.WorkInfoDTO, BaseViewHolder> {

        public MyAdapter(List<ThePerfectGirl.WorkInfoDTO> data) {
            super(R.layout.item_lv_job, data);
        }

        /**
         * Implement this method and use the helper to adapt the view to the given item.
         *
         * @param helper A fully initialized helper.
         * @param item   The item that needs to be displayed.
         */
        @Override
        protected void convert(BaseViewHolder helper, ThePerfectGirl.WorkInfoDTO item) {
            Glide.with(context).load((StaticParams.QiNiuYunUrl + item.getReleaseInfo().getPortraitId())).dontAnimate().placeholder(R.drawable.ic_launcher).into((CircleImageView) helper.getView(R.id.img_job_logo));

            ((TextView) helper.getView(R.id.tv_job_title)).setText(item.getTitle() == null ? "用户未填写" : item.getTitle());
            ((TextView) helper.getView(R.id.tv_job_com)).setText(item.getReleaseInfo().getCompany() == null ? "公司名称未填写" : item.getReleaseInfo().getCompany());

            ((TextView) helper.getView(R.id.tv_job_location)).setText(TextUtils.isEmpty(item.getAddress()) ? "工作地点未填写" : item.getAddress());

            ((TextView) helper.getView(R.id.tv_job_money)).setText((item.getSalary() + "元" + item.getPaymentDec()) == null ? "用户未填写" : item.getSalary() + "/月");// getItem(position).getPaymentDec()

            ((TextView) helper.getView(R.id.tv_job_alreadysinperson)).setText(item.getApplyCount() + "" == null ? "" : item.getApplyCount() + "/人已报名".trim());
            String s1 = String.format("%tF%n", item.getReleaseDate());
            ((TextView) helper.getView(R.id.tv_job_day)).setText(s1.substring(5, s1.length()));
            ((TextView) helper.getView(R.id.tv_job_eyenum)).setText(item.getCount() + "" == null ? "0" : item.getCount() + "");
            ImageView xuanshang = ((ImageView) helper.getView(R.id.img_job_shang));
            ImageView jianghu = ((ImageView) helper.getView(R.id.img_job_lingpai));
            TextView tv_job_xuanshangjinqian = ((TextView) helper.getView(R.id.tv_job_xuanshangjinqian));
            ThePerfectGirl.WorkInfoDTO sssssddd = item;
            if (item.getList() != null && item.getList().size() > 0) {
                if (item.getList().size() == 1) {
                    if (item.getList().get(0).getType().equals("0")) {
                        //只返回一个悬赏令
                        xuanshang.setVisibility(View.VISIBLE);
                        tv_job_xuanshangjinqian.setText("¥:" + item.getList().get(0).getMoney());
                        jianghu.setVisibility(View.GONE);
                    }

                    if (item.getList().get(0).getType().equals("1")) {
                        //只返回一个江湖令
                        jianghu.setVisibility(View.VISIBLE);
                        xuanshang.setVisibility(View.GONE);
                        tv_job_xuanshangjinqian.setVisibility(View.GONE);
                    }
                }


                if (item.getList().size() == 2) {
                    //隐藏一个的令牌
                    xuanshang.setVisibility(View.VISIBLE);
                    jianghu.setVisibility(View.VISIBLE);
                    tv_job_xuanshangjinqian.setVisibility(View.VISIBLE);
                    for (int i = 0; i < item.getList().size(); i++) {
                        if (item.getList().get(i).getType().equals("0")) {
                            tv_job_xuanshangjinqian.setText("¥:" + item.getList().get(i).getMoney());
                        }
                    }
                }
            } else {
                xuanshang.setVisibility(View.GONE);
                tv_job_xuanshangjinqian.setVisibility(View.GONE);
                jianghu.setVisibility(View.GONE);
            }
//            /**
//             * 设置用户头像、悬赏令、江湖令的点击事件
//             */
//            setOnItemViewClickListener(viewHolder, position);
            helper.addOnClickListener(R.id.img_job_logo);
            helper.addOnClickListener(R.id.xuanshang);
            helper.addOnClickListener(R.id.jianghu);
        }
    }

    private class TestNomalAdapter extends StaticPagerAdapter {
        private List<ThePerfectGirl.InformationInfo> imgs;

        public TestNomalAdapter(List<ThePerfectGirl.InformationInfo> newsList) {
            this.imgs = newsList;
        }

        public ThePerfectGirl.InformationInfo getItem(int p) {
            return imgs.get(p);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            Glide.with(context).load(StaticParams.QiNiuYunUrl_News + imgs.get(position).getPictrueid())
                    .dontAnimate().into(view);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return imgs.size();
        }
    }
}
