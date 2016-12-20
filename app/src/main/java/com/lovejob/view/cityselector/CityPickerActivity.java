package com.lovejob.view.cityselector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lovejob.MyApplication;
import com.lovejob.R;
import com.lovejob.model.StaticParams;
import com.lovejob.view.cityselector.cityselector.adapter.CityListAdapter;
import com.lovejob.view.cityselector.cityselector.adapter.ResultListAdapter;
import com.lovejob.view.cityselector.cityselector.db.DBManager;
import com.lovejob.view.cityselector.cityselector.model.City;
import com.lovejob.view.cityselector.cityselector.model.LocateState;
import com.lovejob.view.cityselector.cityselector.view.SideLetterBar;
import com.v.rapiddev.preferences.AppPreferences;
import com.v.rapiddev.utils.V;

import java.util.List;


/**
 * ClassType:仿美团定位界面
 * User:wenyunzhao
 * ProjectName:CitySelector
 * Package_Name:com.lovejob.cityselector
 * Created on 2016-10-08 17:47
 */
public class CityPickerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_PICK_CITY = 2333;
    public static final String KEY_PICKED_CITY = "picked_city";
    private Activity context;
    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ImageView backBtn;
    private ViewGroup emptyView;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;
    private DBManager dbManager;

    private BroadcastReceiver broadcastReceiver = null;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        context = this;
        appPreferences = new AppPreferences(context);
        /*
        获取数据
         */
        initData();
        /*
        初始化view
         */
        initView();
        /*
        开启定位
         */
        initLocation();
    }

    private void initLocation() {
        V.d("start location");
        MyApplication.mLocationClient.start();
        broadcastReceiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter("com.lovejob.location");
        registerReceiver(broadcastReceiver, intentFilter);
        V.d("定位监听已开启");
//        mLocationClient = new AMapLocationClient(this);
//        AMapLocationClientOption option = new AMapLocationClientOption();
//        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        option.setOnceLocation(true);
//        mLocationClient.setLocationOption(option);
//        mLocationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                if (aMapLocation != null) {
//                    if (aMapLocation.getErrorCode() == 0) {
//                        String city = aMapLocation.getCity();
//                        String district = aMapLocation.getDistrict();
//                        Log.e("onLocationChanged", "city: " + city);
//                        Log.e("onLocationChanged", "district: " + district);
//                        String location = StringUtils.extractLocation(city, district);
//                        mCityAdapter.updateLocateState(LocateState.SUCCESS, location);
//                    } else {
//                        //定位失败
//                        mCityAdapter.updateLocateState(LocateState.FAILED, null);
//                    }
//                }
//            }
//        });
//        mLocationClient.startLocation();
    }

    private String city = null;

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String City = intent.getExtras().getString("city");
            V.d("接收到:" + City);
            if (City.length() > 0) {
//                String city = aMapLocation.getCity();
//                String district = aMapLocation.getDistrict();
                V.d("city: " + City);
//                Log.d("district: " + district);
//                String location = XXUtils.extractLocation(city, district);
                mCityAdapter.updateLocateState(LocateState.SUCCESS, City);
                city = City;
            } else {
                mCityAdapter.updateLocateState(LocateState.FAILED, null);
            }
        }

    }

    private void initData() {
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(this, mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                back(name);
            }

            @Override
            public void onLocateClick() {
                V.e("重新定位...");
                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
                //TODO 开启定位
                MyApplication.mLocationClient.start();
            }
        });

        mResultAdapter = new ResultListAdapter(this, null);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview_all_city);
        mListView.setAdapter(mCityAdapter);

        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = dbManager.searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(mResultAdapter.getItem(position).getName());
            }
        });

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        backBtn = (ImageView) findViewById(R.id.back);

        clearBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void back(String city) {
//        ToastUtils.showToast(this, "点击的城市：" + city);
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, city);
        //TODO 将定位的数据写入共享参数
        appPreferences.put(StaticParams.FileKey.__City__,city);
        setResult(StaticParams.RequestCode.RequestCode_F_Home_TO_CitySelector, data);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_clear:
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                mResultListView.setVisibility(View.GONE);
                break;
            case R.id.back:
                Intent data = new Intent();
                data.putExtra(KEY_PICKED_CITY, city == null ? "" : city);
                //TODO 将定位的数据写入共享参数
               appPreferences.put(StaticParams.FileKey.__City__,city);
                setResult(StaticParams.RequestCode.RequestCode_F_Home_TO_CitySelector, data);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null) unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
