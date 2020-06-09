package cn.yy.freewalker.ui.fragment.main;

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
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.fragment.BaseFragment;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 0:03
 */
public class MainNearbyFragment extends BaseFragment implements AMapLocationListener {


    private static final int MSG_RETURN_TO_MY_LOCATION = 0x01;

    //GPS获取参数
    private static final int GPSInterval = 2000;//获取GPS信息时间频率 单位:毫秒
    private static final int GpsAccuracy = 250;//精度控制
    private static final int LIMIT_MIN_PACE = 50;//速度过滤参数(最快速度)

    /* views */
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.btn_scan)
    LinearLayout btnScan;


    /* data */
    AMap mAMap;                                                     //地图
    AMapLocation mLocation;                                         //当前位置
    boolean mFirstLocation = true;                                   //首次定位


    /* 定位相关 */
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption = null;


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
                mHandler.sendEmptyMessage(MSG_RETURN_TO_MY_LOCATION);
                break;
            case R.id.btn_scan:
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            //回到我的位置
            case MSG_RETURN_TO_MY_LOCATION:

                break;
        }
    }


    @Override
    public void onLocationChanged(AMapLocation location) {
        //定位不成功，扔掉
        if (location.getErrorCode() != AMapLocation.LOCATION_SUCCESS) {
            return;
        }
        //缓存定位结果扔掉
        if (location.getLocationType() == AMapLocation.LOCATION_TYPE_FIX_CACHE) {
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
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            mAMap.getUiSettings().setZoomControlsEnabled(false);
        }

        initGps();

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
        locationClient.startLocation();
    }

    @Override
    protected void onInVisible() {
        // 启动定位
        locationClient.stopLocation();
    }


    /**
     * 初始化GPS取点服务
     */
    private void initGps() {
        locationClient = new AMapLocationClient(getActivity());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为仅设备模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        // 设置定位监听
        locationClient.setLocationListener(this);

        locationOption.setInterval(GPSInterval);//定位间隔
        locationOption.setNeedAddress(false);//不需要返回位置信息
        locationOption.setOnceLocation(false);//持续定位
        // 设置定位参数
        locationClient.setLocationOption(locationOption);

    }

}
