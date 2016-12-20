package com.lovejob.baidumap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lovejob.R;
import com.lovejob.baidumap.base.ViewHolder;
import com.lovejob.baidumap.base.WBaseAdapter;
import com.lovejob.baidumap.utils.CameraUtils;
import com.lovejob.baidumap.utils.FileUtils;
import com.v.rapiddev.utils.V;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassType:
 * User:wenyunzhao
 * ProjectName:lovejob11
 * Package_Name:com.lovejob.baidumap
 * Created on 2016-12-10 12:39
 */

public class BaiDuMapCitySelctor extends Activity implements AdapterView.OnItemClickListener {

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
//                Toast.makeText(getApplicationContext(), "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", Toast.LENGTH_SHORT).show();
                V.e("未监测到可用的百度key");
                finish();
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
//                Toast.makeText(getApplicationContext(), "key 验证成功! 功能可以正常使用", Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                V.e("网络异常，拉取poi资料失败");
                finish();
//                Toast.makeText(getApplicationContext(), "网络出错", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private SDKReceiver mReceiver;

    private int activityResultCode=-1;
    private MapView mMapView;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private BaiduMap mBaiduMap;
    /**
     * POI信息列表
     */
    private ArrayList<PoiInfo> mInfoList;
    /**
     * 当前物理坐标
     */
    private Point mCenterPoint;
    /**
     * 当前地理坐标
     */
    private LatLng mLoactionLatLng;
    /**
     * 当前选中地理坐标
     */
    private LatLng mCurrentSelected;
    /**
     * 是否第一次定位
     */
    public boolean isFirstLoc = true;
    /**
     * 地理编码
     */
    private GeoCoder mGeoCoder;
    private PlaceListAdapter mAdapter;
    private LocationClient mLocationClient;
    private ImageView mImageViewPointer;
    private String mMapSavePath;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor mSelectIco = BitmapDescriptorFactory.fromResource(R.mipmap.icon_geo);
    private BitmapDescriptor mPointer = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_chatbox);
    private BitmapDescriptor mCurrentMarker;
    private String mAddress;
    private String mName;
    private BDLocation mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baidumapselector);

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        initView();
        initSettingMapView();
        activityResultCode=getIntent().getIntExtra("requestCode",-1);
    }

    private void initView() {
//        TextView txt_title = (TextView) findViewById(R.id.txt_title);
//        txt_title.setText("位置");
//        txt_title.setOnClickListener(this);
//        TextView txt_right = (TextView) findViewById(R.id.txt_right);
//        txt_right.setVisibility(View.VISIBLE);
//        txt_right.setText("发送");
//        txt_right.setOnClickListener(this);


        mMapView = (MapView) findViewById(R.id.mapview);
        mListView = (ListView) findViewById(R.id.lv_content);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
    }

    private void initSettingMapView() {
        // 初始化地图
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(new MyBDLocationListner());
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);// 打开gps
//        option.setCoorType(CoordinateType.BD09LL); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocationClient.setLocOption(option);
//        mLocationClient.start();


        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();


        //初始化缩放比例
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMapTouchListener(new MyMapTouchListener());

        // 初始化POI信息列表
        mInfoList = new ArrayList<PoiInfo>();

        // 初始化当前MapView中心屏幕坐标(物理坐标)
        mMapView.post(new Runnable() {

            @Override
            public void run() {
                mCenterPoint = mBaiduMap.getMapStatus().targetScreen;
                int x = mCenterPoint.x;
                int y = mCenterPoint.y;

                mImageViewPointer = new ImageView(getApplicationContext());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_location_chatbox);
                mImageViewPointer.setImageBitmap(bitmap);
                mImageViewPointer.setX(x - bitmap.getWidth() / 2);
                mImageViewPointer.setY(y - bitmap.getHeight());
                ViewGroup parent = (ViewGroup) mMapView.getParent();
                parent.addView(mImageViewPointer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            }
        });
        //初始化当前地理坐标
        mLoactionLatLng = mBaiduMap.getMapStatus().target;

        // 地理编码
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new MyGetGeoCoderResultListener());

        //// 周边位置列表
        mAdapter = new PlaceListAdapter(getApplicationContext(), mInfoList, R.layout.listitem_place);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private class PlaceListAdapter extends WBaseAdapter<PoiInfo> {

        private int notifyTip;

        public PlaceListAdapter(Context context, List<PoiInfo> data, int layoutId) {
            super(context, data, layoutId);
            notifyTip = -1;
        }

        public void setNotifyTip(int position) {
            notifyTip = position;
            notifyDataSetChanged();
        }

        @Override
        public void convert(ViewHolder holder, PoiInfo t, int position) {
            holder.setText(R.id.place_name, t.name);
            holder.setText(R.id.place_adress, t.address);

            //mLocation
            // 根据重新加载的时候第position条item是否是当前所选择的，选择加载不同的图片
            ImageView imageView = holder.getView(R.id.place_select);
            if (notifyTip == position) {
                imageView.setImageResource(R.mipmap.ic_contact_list_selected);
            } else {
                imageView.setImageDrawable(null);
            }
        }
    }

    // 定位监听器
    private class MyBDLocationListner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData data = new MyLocationData.Builder()//
                    // .direction(mCurrentX)//
                    .accuracy(0)//location.getRadius()
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            mBaiduMap.setMyLocationData(data);

            double mLantitude = location.getLatitude();
            double mLongtitude = location.getLongitude();
            mLoactionLatLng = new LatLng(mLantitude, mLongtitude);

            // 是否第一次定位
            if (isFirstLoc) {
                isFirstLoc = false;
                // 实现动画跳转
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLoactionLatLng);
                mBaiduMap.animateMapStatus(u);
                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption()).location(mLoactionLatLng));
                mLocation = location;
            }
        }
    }

    // 地图触摸事件监听器
    private class MyMapTouchListener implements BaiduMap.OnMapTouchListener {

        @Override
        public void onTouch(MotionEvent event) {
            // TODO BaiduMap onTouch
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mCenterPoint == null) {
                    return;
                }
                // 获取当前MapView中心屏幕坐标对应的地理坐标
                LatLng currentLatLng = mBaiduMap.getProjection().fromScreenLocation(mCenterPoint);
                //发起反地理编码检索
                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption()).location(currentLatLng));
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    ;

    // 地理编码监听器
    private class MyGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            //TODO 地理编码
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有找到检索结果
            }
            // 获取反向地理编码结果
            else {
                // 当前位置信息
                PoiInfo mCurentInfo = new PoiInfo();
                mCurentInfo.address = result.getAddress();
                mCurentInfo.location = result.getLocation();
                mCurentInfo.name = "[位置]";
                mCurentInfo.city = result.getAddressDetail().city;

                mInfoList.clear();
                mInfoList.add(mCurentInfo);

                // 将周边信息加入表
                if (result.getPoiList() != null) {
                    mInfoList.addAll(result.getPoiList());
                }

                // 通知适配数据已改变
                mAdapter.setNotifyTip(0);
                mListView.setSelection(0);
                mBaiduMap.clear();
                mProgressBar.setVisibility(View.GONE);
                runShakeAnimation(mImageViewPointer);

                //初始选中信息
                mCurrentSelected = result.getLocation();
                mAddress = result.getAddress();
            }
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            //TODO 地理编码
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检索到结果
            }
            // 获取地理编码结果
        }
    }

    ;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Item点击事件

        // 通知是适配器第position个item被选择了
        mAdapter.setNotifyTip(position);

        mBaiduMap.clear();
        PoiInfo info = mAdapter.getItem(position);
        mCurrentSelected = info.location;
        V.d("getAddrStr:" + mLocation.getAddrStr());
        mName = info.name;
        mAddress = info.address;


        V.d("mAddress:" + mAddress);
        V.d("info.address:" + info.address);
        V.d("name:" + info.name);
        V.d("city:" + info.city);


        V.d("getAddress:" + mLocation.getAddress().address);
        //用户点击关闭当前页面，将数据送到下个页面   只需要一条详细的定位信即可
        StringBuffer stringBuffer = new StringBuffer();//最后送出该页面的参数
        String province = "";//省
        String city = "";//市
        String district = "";//区

        //item上的详细信息
        String details = info.address;


        String name = "";
        try {
            details = details.substring(details.lastIndexOf("省"));
            details = details.substring(1, details.length());

            details = details.substring(details.lastIndexOf("市"));
            details = details.substring(1, details.length());

            details = details.substring(details.lastIndexOf("区"));
            details = details.substring(1, details.length());


        } catch (Exception e) {
            Log.e("LoveJob", "e1:" + e.toString());

            try {
                details = details.substring(details.lastIndexOf("市"));
                details = details.substring(1, details.length());

                details = details.substring(details.lastIndexOf("区"));
                details = details.substring(1, details.length());
            } catch (Exception e1) {
                Log.e("LoveJob", "e2:" + e.toString());
                try {
                    details = details.substring(details.lastIndexOf("区"));
                    details = details.substring(1, details.length());
                } catch (Exception e2) {
                    Log.e("LoveJob", "e3:" + e.toString());
                }
            }
        }


        //省
        province = mLocation.getProvince();
        //市
        city = mLocation.getCity();
        //区
        district = mLocation.getDistrict();

        //详细路径
        V.d("==========>>>>>>" + details);

        //名称
        name = info.name;

        stringBuffer.append(province)
                .append(city)
                .append(district)
                .append(details);
        if (!name.equals("[位置]")) {
            stringBuffer.append(name);
        }
        V.d("-------------->>>>>>>>>>>>>>>" + stringBuffer.toString());

        Intent intent = new Intent();
        intent.putExtra("content", stringBuffer.toString());
        setResult(activityResultCode, intent);
        finish();

//		// 动画跳转
//		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mCurrentSelected);
//		mBaiduMap.animateMapStatus(u);
//		runShakeAnimation(mImageViewPointer);
//
//		// 添加覆盖物
//		addOverlay(mCurrentSelected, mSelectIco, 0.5f, 0.5f);


//		Uri mUri = Uri.parse("geo:"+la.latitude+","+la.longitude);
//		Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
//		startActivity(mIntent);
    }

    /**
     * 添加覆盖物
     */
    private void addOverlay(LatLng la, BitmapDescriptor descriptor, float anchorX, float anchorY) {
        MarkerOptions ooA = new MarkerOptions().position(la).icon(descriptor);
        if (anchorX > 0 && anchorY > 0) {
            ooA.anchor(anchorX, anchorY);
        }
        mBaiduMap.addOverlay(ooA);
    }

    /**
     * 截图当前地图
     */
    private void screenshotsMap() {
        chageIcon(R.mipmap.icon_null); //隐藏当前位置
        mBaiduMap.clear(); //清空绘制
        addOverlay(mCurrentSelected, mPointer, 0, 0); //绘制指针
        mMapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {

                    public void onSnapshotReady(Bitmap snapshot) {
                        mMapSavePath = FileUtils.createTmpFile().getAbsolutePath();
                        CameraUtils.saveBitmap2Path(mMapSavePath, snapshot);

                        LatLng latLng = mCurrentSelected; //经纬度信息
                        double latitude = latLng.latitude;
                        double longitude = latLng.longitude;

                        String path = mMapSavePath; //图片路径
                        String name = mName; //标题
                        String address = mAddress; //地址详情

                        // 打开坐标位置
//						Intent intent = new Intent(getApplicationContext(), ShowPositionActivity.class);
//						intent.putExtra("latitude", latitude);
//						intent.putExtra("longitude", longitude);
//						intent.putExtra("name", name);
//						intent.putExtra("address", address);
//						startActivity(intent);
//						finish();
                    }
                });
            }
        }, 200);
    }

    public void runShakeAnimation(View view) {
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 10);
        anim.setDuration(500);
        anim.setInterpolator(new CycleInterpolator(1));
        view.startAnimation(anim);
    }

    private void chageIcon(int resId) {
        if (mBaiduMap != null) {
            mCurrentMarker = null;
            if (resId > 0) {
                mCurrentMarker = BitmapDescriptorFactory.fromResource(resId);
            }
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));
        }
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mGeoCoder.destroy();

        mCurrentMarker = null;
        mSelectIco = null;
        mPointer = null;

        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.txt_title:
//                finish();
//                break;
//            case R.id.txt_right:
//                screenshotsMap();
//                break;
//        }
//    }
}

