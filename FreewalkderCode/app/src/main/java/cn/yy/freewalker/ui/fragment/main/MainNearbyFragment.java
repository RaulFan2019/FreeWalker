package cn.yy.freewalker.ui.fragment.main;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.amap.api.maps.UiSettings;
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
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.common.AmapNearbyUserView;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.radarview.RadarView;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.array.ConnectStates;
import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.entity.LocationInfo;
import cn.yy.sdk.ble.entity.SingleChatInfo;
import cn.yy.sdk.ble.observer.ReceiveMsgListener;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 0:03
 */
public class MainNearbyFragment extends BaseFragment implements AMapLocationListener, ReceiveMsgListener {


    private static final String TAG = "MainNearbyFragment";

    private static final int MSG_STOP_SCAN = 0x01;
    private static final int MSG_NEXT_SCAN = 0x02;
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
    @BindView(R.id.ll_control)
    LinearLayout llControl;
//    @BindView(R.id.ll_radar)
//    LinearLayout llRadar;

    /* data */
    AMap mAMap;                                                     //地图

    List<Marker> listMarker = new ArrayList<>();
    List<LatLng> listLat = new ArrayList<>();
    List<LocationInfo> listScan = new ArrayList<>();

    private int mScanTimes = 0;
    private boolean mIsScanning = false;

    /* 定位相关 */
    AMapLocation mLocation;                                         //当前位置
    boolean mFirstLocation = true;                                   //首次定位
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


    @Override
    public void receiveLocationMsg(LocationInfo locationInfo) {
        YLog.e(TAG, "receiveLocationMsg locationInfo.latitude:" + locationInfo.latitude
                + ",locationInfo.longtitude:" + locationInfo.longtitude);
        double latitude = (locationInfo.latitude / 1000000.0) - 90;
        double longitude = (locationInfo.longtitude / 1000000.0) - 180;

        listLat.add(new LatLng(latitude, longitude));
        listScan.add(locationInfo);

        showAddMarker(listScan.size() - 1);
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
            //开始扫描
            case R.id.btn_scan:
                if (BM.getManager().getConnectState() < ConnectStates.WORKED) {
                    new ToastView(mContext, getString(R.string.nearby_toast_scan_device_not_connected_error), -1);
                    return;
                }

                listScan.clear();
                listLat.clear();
                for (Marker maker : listMarker) {
                    maker.destroy();
                }
                listMarker.clear();

//                llControl.setVisibility(View.GONE);
                mAMap.getUiSettings().setAllGesturesEnabled(false);
                mIsScanning = true;

                mScanTimes = 0;
                mHandler.sendEmptyMessageDelayed(MSG_NEXT_SCAN, INTERVAL_SCAN);
                scanView.setVisibility(View.VISIBLE);
                btnScan.setVisibility(View.GONE);

                BM.getManager().queryNearbyUsers();
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_STOP_SCAN:
                mAMap.getUiSettings().setAllGesturesEnabled(true);
//                llControl.setVisibility(View.GONE);
                scanView.setVisibility(View.INVISIBLE);
                btnScan.setVisibility(View.VISIBLE);
                showMarker();
                break;
            case MSG_NEXT_SCAN:
                mScanTimes++;
                if (mScanTimes > 30) {
                    mAMap.getUiSettings().setAllGesturesEnabled(true);
//                    llControl.setVisibility(View.GONE);
                    scanView.setVisibility(View.INVISIBLE);
                    btnScan.setVisibility(View.VISIBLE);
                    mIsScanning = false;
                } else {
                    int currChannel = BM.getManager().getDeviceSystemInfo().currChannel;
                    currChannel++;
                    if (currChannel > 29) {
                        currChannel = 0;
                    }
                    BM.getManager().setChannel(currChannel, 5, "");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BM.getManager().queryNearbyUsers();
                        }
                    }, 200);
                    mHandler.sendEmptyMessageDelayed(MSG_NEXT_SCAN, INTERVAL_SCAN);
                }
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
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));
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
        BM.getManager().registerReceiveMsgListener(this);
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
        if (mapView != null) {
            mapView.onDestroy();
        }
        BM.getManager().unRegisterReceiveMsgListener(this);
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
            mAMap.setOnMarkerClickListener(marker -> {

                YLog.e(TAG, "mIsScanning:" + mIsScanning);
                if (mIsScanning){
                    return true;
                }
                for (int i = 0; i < listLat.size(); i++) {
                    if (listLat.get(i).latitude == marker.getPosition().latitude
                            && listLat.get(i).longitude == marker.getPosition().longitude) {
                        LocalApp.getInstance().getEventBus().post(
                                new NearbyUserCartEvent(NearbyUserCartEvent.SHOW, listScan.get(i)));
                        break;
                    }
                }

                return false;
            });

            initMyLocation();

        }
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

    /**
     * 增加一个 marker
     */
    private void showAddMarker(int index) {
        AmapNearbyUserView nearbyUserView = new AmapNearbyUserView(getActivity());
        nearbyUserView.bindView(listScan.get(index));
        Marker marker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(listLat.get(index))
                .icon(BitmapDescriptorFactory.fromView(nearbyUserView)));
        YLog.e(TAG, "addMarker marker.getPosition().latitude:" + marker.getPosition().latitude + ",marker.getPosition().longitude:" + marker.getPosition().longitude);

        listMarker.add(marker);
    }


    /**
     * 一次显示所有marker
     */
    private void showMarker() {
        if (mLocation != null) {
            for (int i = 0; i < listScan.size(); i++) {
                AmapNearbyUserView nearbyUserView = new AmapNearbyUserView(getActivity());
                nearbyUserView.bindView(listScan.get(i));
                Marker marker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(listLat.get(i))
                        .icon(BitmapDescriptorFactory.fromView(nearbyUserView)));
                listMarker.add(marker);
            }
        }
    }

    @Override
    public void receiveGroupMsg(GroupChatInfo groupChatInfo) {

    }

    @Override
    public void receiveSingleMsg(SingleChatInfo singleChatInfo) {

    }

}
