package cn.yy.freewalker.ui.fragment.main;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;


import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.common.AmapNearbyUserView;
import cn.yy.freewalker.ui.widget.radarview.RadarView;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 0:03
 */
public class MainNearbyFragment extends BaseFragment implements AMapLocationListener {


    private static final String TAG = "MainNearbyFragment";

    private static final int MSG_STOP_SCAN = 0x01;
    private static final int MSG_SHOW_TEST_USER = 0x02;

    private static final long INTERVAL_SHOW_TEST_USER = 2 * 1000;
    private static final long INTERVAL_SCAN = 5 * 1000;

    //GPS获取参数
    private static final int GPSInterval = 2000;//获取GPS信息时间频率 单位:毫秒
    private static final int GpsAccuracy = 250;//精度控制
    private static final int LIMIT_MIN_PACE = 50;//速度过滤参数(最快速度)

    /* views */
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.btn_scan)
    LinearLayout btnScan;
    @BindView(R.id.radar)
    RadarView scanView;
//    @BindView(R.id.ll_radar)
//    LinearLayout llRadar;

    /* data */
    AMap mAMap;                                                     //地图
    AMapLocation mLocation;                                         //当前位置
    boolean mFirstLocation = true;                                   //首次定位


    /* 定位相关 */
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption = null;


    /* 构造函数 */
    public static MainNearbyFragment newInstance() {
        MainNearbyFragment fragment = new MainNearbyFragment();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_nearby;
    }

    @OnClick({R.id.btn_zoom_in, R.id.btn_zoom_out, R.id.btn_location, R.id.btn_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //地图放大
            case R.id.btn_zoom_in:
                mAMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            //地图缩小
            case R.id.btn_zoom_out:
                mAMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;
            //地图回到Gps位置
            case R.id.btn_location:
                if (mLocation != null) {
                    mAMap.animateCamera(CameraUpdateFactory.changeLatLng(
                            new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));
                }
                break;
            case R.id.btn_scan:
                mHandler.sendEmptyMessageDelayed(MSG_SHOW_TEST_USER, INTERVAL_SHOW_TEST_USER);
                mHandler.sendEmptyMessageDelayed(MSG_STOP_SCAN, INTERVAL_SCAN);
                scanView.setVisibility(View.VISIBLE);
                btnScan.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_STOP_SCAN:
                scanView.setVisibility(View.INVISIBLE);
                btnScan.setVisibility(View.VISIBLE);
//                showTestMarker();
                break;
            //Add Test Data
            case MSG_SHOW_TEST_USER:
                 showTestMarker();


                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        //定位不成功，扔掉
        if (location.getErrorCode() != AMapLocation.LOCATION_SUCCESS) {
            return;
        }

        mLocation = location;
        if (mFirstLocation) {
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            mAMap.animateCamera(CameraUpdateFactory.changeLatLng(
                    new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));
            mFirstLocation = false;
        }

    }

    @Override
    protected void initParams() {
        mapView.onCreate(mSavedInstanceState);
        initLocationClient();
        setUpMap();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void causeGC() {
        mapView.onDestroy();
    }

    @Override
    protected void onVisible() {
        mFirstLocation = true;
        // 启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onInVisible() {
        // 启动定位
        mLocationClient.stopLocation();
    }

    private void initLocationClient() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            mLocationOption.setInterval(GPSInterval);//定位间隔
            mLocationOption.setNeedAddress(false);//不需要返回位置信息
            mLocationOption.setOnceLocation(false);//持续定位
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
        }

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
//            mAMap.setLocationSource(this);
//            mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            mAMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.getUiSettings().setCompassEnabled(true);
            initMyLocation();

        }
//        initMyLocation();
    }


    private void initMyLocation() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.parseColor("#FF414F"));
        myLocationStyle.radiusFillColor(Color.parseColor("#77FF414F"));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);
        mAMap.setMyLocationEnabled(true);
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
    }


    private void showTestMarker(){
        if (mLocation != null) {
            AmapNearbyUserView nearbyUserView1 = new AmapNearbyUserView(getActivity());
            nearbyUserView1.bindView(1,"贝吉塔","");
            mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromView(nearbyUserView1)));

            AmapNearbyUserView nearbyUserView2 = new AmapNearbyUserView(getActivity());
            nearbyUserView2.bindView(0,"孙悟空","");
            mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(mLocation.getLatitude() + 0.001, mLocation.getLongitude()  + 0.001))
                    .icon(BitmapDescriptorFactory.fromView(nearbyUserView2)));

            AmapNearbyUserView nearbyUserView3 = new AmapNearbyUserView(getActivity());
            nearbyUserView3.bindView(0,"短笛","");
            mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(mLocation.getLatitude() + 0.002, mLocation.getLongitude()  - 0.002))
                    .icon(BitmapDescriptorFactory.fromView(nearbyUserView3)));

            AmapNearbyUserView nearbyUserView4 = new AmapNearbyUserView(getActivity());
            nearbyUserView4.bindView(0,"魔人布欧","");
            mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(mLocation.getLatitude() + 0.001, mLocation.getLongitude()  + 0.002))
                    .icon(BitmapDescriptorFactory.fromView(nearbyUserView4)));
        }

    }
}
