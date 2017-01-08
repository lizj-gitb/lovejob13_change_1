package com.lovejob.ms;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lovejob.BaseFragment;
import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.controllers.task.LoveJob;
import com.lovejob.controllers.task.OnAllParameListener;
import com.lovejob.model.MyOnClickListener;
import com.lovejob.model.StaticParams;
import com.lovejob.model.ThePerfectGirl;
import com.lovejob.model.Utils;
import com.lovejob.view._home.DynDetailsAty;
import com.lovejob.view._home.SendDynamic;
import com.lovejob.view._home.dyndetailstabs.NewsDetails;
import com.lovejob.view._userinfo.myserver.ServiceActivity;
import com.lovejob.view._userinfo.myserver.ServiceMyActivity;
import com.lovejob.view.cityselector.CityPickerActivity;
import com.v.rapiddev.adpater.FFViewHolder;
import com.v.rapiddev.adpater.FastAdapter;
import com.v.rapiddev.dialogs.core.MaterialDialog;
import com.v.rapiddev.http.okhttp3.Call;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.pulltorefresh.PullToRefreshBase;
import com.v.rapiddev.pulltorefresh.PullToRefreshScrollView;
import com.v.rapiddev.utils.Utils_RapidDev;
import com.v.rapiddev.utils.V;
import com.v.rapiddev.views.CircleImageView;
import com.v.rapiddev.views.InputMethodLayout;
import com.v.rapiddev.views.MyListView;
import com.v.rapiddev.views.MyTextVIew;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lovejob.model.StaticParams.RequestCode.RequestCode_F_Home_TO_DynDetails;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:LoveJob3
 * Package_Name:com.lovejob.view._home
 * Created on 2016-11-25 16:46
 */
public class F_HomeMs extends BaseFragment {
    View view;
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
    @Bind(R.id.gv_home)
    GridView gvHome;
    @Bind(R.id.lv_home)
    MyListView lvHome;
    @Bind(R.id.editkeybox)
    InputMethodLayout editkeybox;
    @Bind(R.id.sv_home)
    PullToRefreshScrollView sv_home;
    private FastAdapter<ThePerfectGirl.InformationInfo> adapter_news;
    private Call call_news, call_dynList, call_toDynGoodOrBad;
    private FastAdapter<ThePerfectGirl.DynamicDTO> adapter_dynList;
    private int page = 1;
    private String userInputSearchText;
    private String userPid;
    private MaterialDialog dialog;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_home, null);
        ButterKnife.bind(this, view);
        /**
         * 设置默认显示的城市
         */
        setActionbar();
        /**
         * 初始化适配器
         */
        initAdapter();
        /**
         * 设置edittext状态监听
         */
        setKeyBoxListener();
        /**
         * 设置下拉刷新相关监听
         */
        setOnRefreshListener();
        /**
         * 设置新闻Item点击事件
         */
        setNewsItemClickListener();
        /**
         * 设置动态列表每一个item的点击事件
         */
        setDynItemClickListener();
        AppPreferences appPreferences = new AppPreferences(context);
        userPid = appPreferences.getString(StaticParams.FileKey.__USERPID__, "");
        dialog = Utils.showProgressDliago(context, "请稍后……");
        /**
         * 填充新闻数据
         */
        addDataToNewsList();
        /**
         * 填充动态列表
         */
        addDataToDynList();
        return view;
    }


    private void setDynItemClickListener() {

        lvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                V.d("动态ID：" + adapter_dynList.getItem(i).getDynamicPid());
                Intent intent = new Intent(context, DynDetailsAty.class);
                intent.putExtra("dynPid", adapter_dynList.getItem(i).getDynamicPid());
                startActivityForResult(intent, RequestCode_F_Home_TO_DynDetails);
                context.overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
                intent = null;
            }
        });

    }

    private void setNewsItemClickListener() {
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Utils.showToast(context, "点击了 " + adapter_news.getItem(i).getTitle() + "，该新闻Id为：" + adapter_news.getItem(i).getPid());
                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("newsId", adapter_news.getItem(i).getPid());
                startActivityForResult(intent, StaticParams.RequestCode.RequestCode_F_Home_TO_NewsDetails);
                context.overridePendingTransition(R.anim.base_slide_right_in, R.anim.slide_out_to_right);
            }
        });
    }


    private void setOnRefreshListener() {
        sv_home.setMode(PullToRefreshBase.Mode.BOTH);
        sv_home.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                StaticParams.ROWS = "5";
                userInputSearchText = null;
                if (adapter_dynList != null && adapter_dynList.getList().size() > 0) {
                    adapter_dynList.removeAll();
                }
                addDataToDynList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                StaticParams.ROWS = "5";
                addDataToDynList();
            }
        });
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
                    userInputSearchText = keyText;
                    keyText = null;
                    Utils_RapidDev.closeKeybord(actionbarEtSearch, context);
                    actionbarEtSearch.setText("");
                    if (adapter_dynList != null && adapter_dynList.getList().size() > 0) {
                        adapter_dynList.removeAll();
                        adapter_dynList.notifyDataSetChanged();
                    }
                    addDataToDynList();
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
                    userInputSearchText = actionbarEtSearch.getText().toString();
                    actionbarEtSearch.setText("");
                    Utils_RapidDev.closeKeybord(actionbarEtSearch, context);
                    if (adapter_dynList != null && adapter_dynList.getList().size() > 0) {
                        adapter_dynList.removeAll();
                        adapter_dynList.notifyDataSetChanged();
                    }
                    addDataToDynList();
                } else {
                    Utils.showToast(context, "输入不能为空，请重新输入");
                }
            }
        });
    }

    private void addDataToDynList() {
        AppPreferences appPreferences = new AppPreferences(context);
        String address = appPreferences.getString(StaticParams.FileKey.__City__, "");
        String lng = appPreferences.getString(StaticParams.FileKey.__LONGITUDE__, "");
        String lat = appPreferences.getString(StaticParams.FileKey.__LATITUDE__, "");

        call_dynList = LoveJob.getDynList(String.valueOf(page), address, lng, lat, userInputSearchText, new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {
                if (sv_home != null) sv_home.onRefreshComplete();
//                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
//                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                if (thePerfectGirl.getData() == null || thePerfectGirl.getData().getDynamicDTOList() == null || thePerfectGirl.getData().getDynamicDTOList().size() == 0) {
                    Utils.showToast(context, "没有更多信息");
                    if (dialog != null) dialog.dismiss();
                    return;
                }
                for (int i = 0; i < thePerfectGirl.getData().getDynamicDTOList().size(); i++) {
                    adapter_dynList.addItem(thePerfectGirl.getData().getDynamicDTOList().get(i));
                }
                adapter_dynList.notifyDataSetChanged();
                if (dialog != null) dialog.dismiss();
            }

            @Override
            public void onError(String msg) {
                if (sv_home != null) sv_home.onRefreshComplete();
                if (dialog != null) dialog.dismiss();
                V.e(msg);
            }
        });
//        address = null;
//        lng = null;
//        lat = null;
//        appPreferences = null;
//        userInputSearchText = null;
    }

    private void addDataToNewsList() {
        call_news = LoveJob.getNewsList("2", new OnAllParameListener() {
            @Override
            public void onSuccess(ThePerfectGirl thePerfectGirl) {

                for (int i = 0; i < thePerfectGirl.getData().getInformationInfos().size(); i++) {
                    adapter_news.addItem(thePerfectGirl.getData().getInformationInfos().get(i));
                }
                adapter_news.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                V.e("新闻列表为空");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter_dynList.notifyDataSetChanged();
    }

    @Override
    public void loadData() {

    }

    private void initAdapter() {
        adapter_news = new FastAdapter<ThePerfectGirl.InformationInfo>(context, R.layout.item_gv_home) {
            @Override
            public View getViewHolder(int position, View convertView, ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_gv_home, position);
                ImageView view = viewHolder.getView (R.id.item_gv_img);
//               Glide.with(context).load(StaticParams.ImageNewsURL + getItem(position).getPictrueid()).dontAnimate().into(view);
                ((TextView) viewHolder.getView(R.id.item_tv_tv)).setText(getItem(position).getTitle());
                return viewHolder.getConvertView();
            }
        };
        gvHome.setAdapter(adapter_news);


        adapter_dynList = new FastAdapter<ThePerfectGirl.DynamicDTO>(context, R.layout.item_lv_home) {
            @Override
            public View getViewHolder(final int position, View convertView, final ViewGroup parent) {
                FFViewHolder viewHolder = FFViewHolder.get(context, convertView, parent, R.layout.item_lv_home, position);

                //添加基础数据
                ThePerfectGirl.DynamicDTO dynamicDTO = getItem(position);
                ThePerfectGirl.UserInfoDTO userReleseInfo = dynamicDTO.getReleaseInfo();
                //用户头像
                CircleImageView userLogo = (CircleImageView) viewHolder.getView(R.id.roundview);
                String url = StaticParams.ImageURL + userReleseInfo.getPortraitId ();
//                Log.d ("F_Home", "url:"+url);
//                Log.d ("F_Home", "width:"+userLogo.getWidth ());
//                userLogo.measure (0,0);
                Glide.with(context).load(url).dontAnimate().placeholder(R.drawable.ic_launcher).into(userLogo);
                //性别
                int resSex = userReleseInfo.getSexDec().equals("男") ? R.mipmap.icon_male : R.mipmap.icon_famale;
                ((ImageView) viewHolder.getView(R.id.img_sex)).setImageResource(resSex);
                //姓名
                ((TextView) viewHolder.getView(R.id.tv_userName)).setText(userReleseInfo.getRealName());
                //等级
                ((ImageView) viewHolder.getView(R.id.img_level)).setImageResource(getUserLever(userReleseInfo.getLevel()));

                //距离
                ((TextView) viewHolder.getView(R.id.tv_distence)).setText(dynamicDTO.getDistance());
                //职位
                ((TextView) viewHolder.getView(R.id.tv_userjob)).setText(userReleseInfo.getCompany() + "/" + userReleseInfo.getPosition());
                //TODO 在职状态

                //文本
                String content = dynamicDTO.getContent();
                MyTextVIew tv_content = (MyTextVIew) viewHolder.getView(R.id.tv_content);
                TextView isShowMoreText = (TextView) viewHolder.getView(R.id.isShowMoreText);
                if (content.length() > 75) {
                    tv_content.setText(content.substring(0, 70) + "...");
                    isShowMoreText.setVisibility(View.VISIBLE);
                } else {
                    isShowMoreText.setVisibility(View.GONE);
                    tv_content.setText("     " + content);
                }
                //是否点亮 评论 差评 或 点赞 按钮图标  isPointGood=2 什么都可以干   1：差评亮  0：点赞亮
                //评论
                ImageView img_comment = (ImageView) viewHolder.getView(R.id.img_comment);
                TextView tv_comment_num = (TextView) viewHolder.getView(R.id.tv_comment_num);
                //评论数
                tv_comment_num.setText(String.valueOf(dynamicDTO.getCommentCount()));
                //差评
                ImageView img_bad = (ImageView) viewHolder.getView(R.id.img_bad);
                TextView tv_bad_num = (TextView) viewHolder.getView(R.id.tv_bad_num);
                //差评
                tv_bad_num.setText(String.valueOf(dynamicDTO.getBadCount()));

                //点赞
                ImageView img_good = (ImageView) viewHolder.getView(R.id.img_good);
                TextView tv_good_num = (TextView) viewHolder.getView(R.id.tv_good_num);
                //点赞数
                tv_good_num.setText(String.valueOf(dynamicDTO.getGoodCount()));
                switch (dynamicDTO.getIsPointGood()) {
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


                //需要点击事件的控件
                TextView enter = (TextView) viewHolder.getView(R.id.enter);//进入服务
                LinearLayout bt_comment = (LinearLayout) viewHolder.getView(R.id.bt_comment);//评论
                LinearLayout bt_bad = (LinearLayout) viewHolder.getView(R.id.bt_bad);//差评
                LinearLayout bt_good = (LinearLayout) viewHolder.getView(R.id.bt_good);//点赞

                /**
                 * 设置可点击的点击事件
                 */
                setDynItemIconClickListener(userLogo, enter, bt_comment, bt_bad, bt_good, tv_content, isShowMoreText, position);

//                MyGirdView myGirdView = (MyGirdView) viewHolder.getView(R.id.gv_home_item);

                RecyclerView recyclerView = (RecyclerView) viewHolder.getView(R.id.recycler_view_home);


                //**********

//                FastAdapter<String> adapter_g = new FastAdapter<String>(context, R.layout.item_image) {
//                    @Override
//                    public View getViewHolder(int position1, View convertView1, ViewGroup parent1) {
//                        FFViewHolder ffViewHolder = FFViewHolder.get(context, convertView1, parent1, R.layout.item_image, position1);
//                        ImageView imageView = (ImageView) ffViewHolder.getView(R.id.imageview);
//                        Glide.with(context).load(getItem(position1)).into(imageView);
//                        return ffViewHolder.getConvertView();
//                    }
//                };
//
//                myGirdView.setAdapter(adapter_g);
//                final ArrayList<String> selectedPhotos = new ArrayList<>();
//                String[] imgs = getItem(position).getPictrueid().split("\\|");
//                if (imgs.length > 0 && !TextUtils.isEmpty(imgs[0])) {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    for (int i = 0; i < imgs.length; i++) {
//                        selectedPhotos.add(StaticParams.ImageURL + imgs[i]);
//                    }
//                } else {
//                    recyclerView.setVisibility(View.GONE);
//                }
//                final PhotoAdapter photoAdapter = new PhotoAdapter(context, selectedPhotos, true);
//                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
//                recyclerView.setAdapter(photoAdapter);
//
//                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        PhotoPreview.builder()
//                                .setPhotos(selectedPhotos)
//                                .setCurrentItem(position)
//                                .setShowDeleteButton(false)
//                                .start(context);
//
//                    }
//                }));


                //**********
                return viewHolder.getConvertView();
            }
        };

        lvHome.setAdapter(adapter_dynList);
    }

    /**
     * @param enter      进入服务窗
     * @param bt_comment 评论按钮
     * @param bt_bad     差评按钮
     * @param bt_good    点赞按钮
     */
    private void setDynItemIconClickListener(CircleImageView userLogo, TextView enter, LinearLayout bt_comment,
                                             LinearLayout bt_bad, LinearLayout bt_good,
                                             final TextView tv_content, final TextView isShowMoreText, final int posstion) {
        userLogo.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onclickListener(View v) {
                V.d("进入个人详情：" + adapter_dynList.getItem(posstion).getReleaseInfo().getRealName());
            }
        });
        enter.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onclickListener(View v) {
                V.d("进入服务窗：" + adapter_dynList.getItem(posstion).getReleaseInfo().getRealName());
                if (adapter_dynList.getItem(posstion).getReleaseInfo().getUserId().equals(userPid)) {
                    //进入自己
                    startActivity(new Intent(context, ServiceMyActivity.class));
                } else {
                    Intent intent = new Intent(context, ServiceActivity.class);
                    intent.putExtra("userId", adapter_dynList.getItem(posstion).getReleaseInfo().getUserId());
                    startActivity(intent);
                }
            }
        });
        bt_comment.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onclickListener(View v) {
                V.d("评论：" + adapter_dynList.getItem(posstion).getReleaseInfo().getRealName());
                Intent intent = new Intent(context, DynDetailsAty.class);
                intent.putExtra("isComm", true);
                intent.putExtra("dynPid", adapter_dynList.getItem(posstion).getDynamicPid());
                startActivity(intent);
            }
        });
        bt_bad.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onclickListener(View v) {
                V.d("差评：" + adapter_dynList.getItem(posstion).getReleaseInfo().getRealName());
                call_toDynGoodOrBad = LoveJob.toDynGoodOrBad(adapter_dynList.getItem(posstion).getDynamicPid(), 0, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        adapter_dynList.removeAll();
                        addDataToDynList();
                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                });
            }
        });
        bt_good.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onclickListener(View v) {
                V.d("点赞");
                call_toDynGoodOrBad = LoveJob.toDynGoodOrBad(adapter_dynList.getItem(posstion).getDynamicPid(), 1, new OnAllParameListener() {
                    @Override
                    public void onSuccess(ThePerfectGirl thePerfectGirl) {
                        adapter_dynList.removeAll();

                        addDataToDynList();

                    }

                    @Override
                    public void onError(String msg) {
                        Utils.showToast(context, msg);
                    }
                });
            }
        });

        isShowMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowMoreText.getText().toString().equals("全文")) {
                    V.d("展开全文：" + adapter_dynList.getItem(posstion).getReleaseInfo().getRealName());
                    tv_content.setText(adapter_dynList.getItem(posstion).getContent());
                    isShowMoreText.setText("收起");
                } else {
                    tv_content.setText(adapter_dynList.getItem(posstion).getContent().substring(0, 70));
                    isShowMoreText.setText("全文");
                }
//                Utils.setListViewHeightBasedOnChildren(lvHome);
//                adapter_dynList.notifyDataSetChanged();
            }
        });
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (call_news != null && !call_news.isCanceled()) call_news.cancel();
        if (call_dynList != null && !call_dynList.isCanceled()) call_dynList.cancel();
        if (call_toDynGoodOrBad != null && !call_toDynGoodOrBad.isCanceled())
            call_toDynGoodOrBad.cancel();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.actionbar_lt_location, R.id.actionbar_lt_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_lt_location:
                //打开行政区域选择页面
                startActivityForResult(new Intent(context, CityPickerActivity.class), StaticParams.RequestCode.RequestCode_F_Home_TO_CitySelector);
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
                startActivityForResult(new Intent(context, SendDynamic.class), StaticParams.RequestCode.RequestCode_F_Home_TO_SendDyn);
            }
        });
        layout_send_work_short.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                V.d("发布创意工作");
                window.dismiss();
//                startActivity(new Intent(context, Aty_SendMoneyWork.class));
            }
        });
        layout_send_work_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                V.d("发布长期工作");
                window.dismiss();
//                startActivity(new Intent(context, Aty_SendMoneyWork.class));
            }
        });
    }

    private void setActionbar() {
        String city = MyApplication.getCity();
        if (!TextUtils.isEmpty(city) ){
            actionbarTvLocationtext.setText(city);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticParams.RequestCode.RequestCode_F_Home_TO_CitySelector) {
            V.d("从城市选择页面返回");
            actionbarTvLocationtext.setText(new AppPreferences(context).getString(StaticParams.FileKey.__City__, "定位失败"));
            if (adapter_dynList != null && adapter_dynList.getList().size() > 0) {
                adapter_dynList.removeAll();
                adapter_dynList.notifyDataSetChanged();
            }
            addDataToDynList();
        } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Home_TO_NewsDetails) {
            V.d("从新闻详情页面返回");
        } else if (requestCode == StaticParams.RequestCode.RequestCode_F_Home_TO_SendDyn) {
            V.d("从发布动态页面返回");
            if (adapter_dynList != null && adapter_dynList.getList().size() > 0) {
                adapter_dynList.removeAll();
//                adapter_dynList.notifyDataSetChanged();
            }
            addDataToDynList();
        }
    }
}
