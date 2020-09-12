package cn.yy.freewalker.ui.fragment.main;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
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
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.event.NearbyUserCartEvent;
import cn.yy.freewalker.entity.net.UserInfoResult;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.common.AmapNearbyUserView;
import cn.yy.freewalker.ui.widget.radarview.RadarView;
import cn.yy.freewalker.utils.YLog;

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
    List<UserInfoResult> listUser = new ArrayList<>();
    List<LatLng> listLat = new ArrayList<>();

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
        if(mapView != null) {
            mapView.onDestroy();
        }
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
            mAMap.setCustomMapStyle(
                    new com.amap.api.maps.model.CustomMapStyleOptions()
                            .setEnable(true)
                            .setStyleDataPath(Environment.getExternalStoragePublicDirectory("data").getPath() + "/style.data")
                            .setStyleExtraPath(Environment.getExternalStoragePublicDirectory("data").getPath() + "style_extra.data")

            );
            mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    for (int i = 0; i < listLat.size(); i++) {
                        if (listLat.get(i).latitude == marker.getPosition().latitude
                            && listLat.get(i).longitude == marker.getPosition().longitude){
                            LocalApp.getInstance().getEventBus().post(
                                    new NearbyUserCartEvent(NearbyUserCartEvent.SHOW, listUser.get(i)));
                            break;
                        }
                    }

                    return false;
                }
            });

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


    private void showTestMarker() {
        if (mLocation != null) {
//            listUser.add(new UserInfoResult("贝吉塔", 0, 1, "", "工程师",
//                    "160~170", "60~70", "25"));
//            listUser.add(new UserInfoResult("孙悟空", 0, 2, "", "律师",
//                    "160~170", "60~70", "25"));
//            listUser.add(new UserInfoResult("短笛", 0, 1, "", "建筑师",
//                    "160~170", "60~70", "25"));
//            listUser.add(new UserInfoResult("魔人布欧", 1, 3, "", "销售员",
//                    "160~170", "60~70", "25"));


            listLat.add(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
            listLat.add(new LatLng(mLocation.getLatitude() + 0.001, mLocation.getLongitude() + 0.001));
            listLat.add(new LatLng(mLocation.getLatitude() + 0.002, mLocation.getLongitude() + 0.002));
            listLat.add(new LatLng(mLocation.getLatitude() - 0.001, mLocation.getLongitude() + 0.003));
            listLat.add(new LatLng(mLocation.getLatitude() - 0.002, mLocation.getLongitude() + 0.002));

            for (int i = 0; i < listUser.size(); i++) {
                AmapNearbyUserView nearbyUserView = new AmapNearbyUserView(getActivity());
                nearbyUserView.bindView(listUser.get(i));
                mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(listLat.get(i))
                        .icon(BitmapDescriptorFactory.fromView(nearbyUserView)));
            }
        }

    }
}
