package cn.yy.freewalker.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import cn.yy.freewalker.data.DBDataGroupChatMsg;
import cn.yy.freewalker.data.DBDataSingleChatMsg;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.data.SPDataUser;
import cn.yy.freewalker.entity.db.GroupChatMsgEntity;
import cn.yy.freewalker.entity.db.SingleChatMsgEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.entity.LocationInfo;
import cn.yy.sdk.ble.entity.SingleChatInfo;
import cn.yy.sdk.ble.observer.QueryMsgListener;
import cn.yy.sdk.ble.observer.ReceiveMsgListener;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/28 22:41
 */
public class UserDeviceCallbackService extends Service implements QueryMsgListener, AMapLocationListener,ReceiveMsgListener {

    private static final String TAG = "UserDeviceCallbackService";


    private UserDbEntity mUser;

    /* 定位相关 */
    AMapLocation mLocation;                                         //当前位置
    boolean mFirstLocation = true;                                   //首次定位
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption = null;
    private static final int GPSInterval = 2000;//获取GPS信息时间频率 单位:毫秒

    //自定义弱引用Handler
    private MyHandler mHandler;


    @Override
    public void onLocationChanged(AMapLocation location) {
        //定位不成功，扔掉
        if (location.getErrorCode() != AMapLocation.LOCATION_SUCCESS) {
            return;
        }
        mLocation = location;
    }

    @Override
    public void queryLocationMsg() {
        if (mLocation != null){
            //是否分享自己的位置
            if (SPDataUser.getIsShare(UserDeviceCallbackService.this)){
                YLog.e(TAG,"send Latitude:" + mLocation.getLatitude());
                YLog.e(TAG,"send Longitude:" + mLocation.getLongitude());
                BM.getManager().sendLocationInfo(mUser.userId,mLocation.getLatitude(),
                        mLocation.getLongitude(),mUser.genderIndex,mUser.ageIndex,mUser.genderOriIndex,mUser.professionIndex,
                        mUser.heightIndex,mUser.weightIndex,mUser.name);
            }
        }
    }

    @Override
    public void receiveGroupMsg(GroupChatInfo groupChatInfo) {
        //检查用户的消息是否被屏蔽了
        UserDbEntity userDbEntity = DBDataUser.getUserInfoByUserId(groupChatInfo.userId);
//        if (userDbEntity != null && userDbEntity.isShield){
//            return;
//        }
        GroupChatMsgEntity groupChatMsgEntity = new GroupChatMsgEntity(System.currentTimeMillis(), mUser.userId,
                BM.getManager().getDeviceSystemInfo().currChannel, groupChatInfo.userId, groupChatInfo.content);
        DBDataGroupChatMsg.save(groupChatMsgEntity);
    }

    @Override
    public void receiveSingleMsg(SingleChatInfo singleChatInfo) {
        YLog.e(TAG,"receiveSingleMsg destUserId:" + singleChatInfo.userId + ",mUser.userId:" + mUser.userId);
        //检查用户的消息是否被屏蔽了
        UserDbEntity userDbEntity = DBDataUser.getUserInfoByUserId(singleChatInfo.userId);
        if (userDbEntity != null && userDbEntity.isShield){
            return;
        }
        SingleChatMsgEntity singleChatMsgEntity = new SingleChatMsgEntity(System.currentTimeMillis(), mUser.userId,
                singleChatInfo.userId, singleChatInfo.content, false);
        DBDataSingleChatMsg.save(singleChatMsgEntity);
    }

    @Override
    public void receiveLocationMsg(LocationInfo locationInfo) {

    }

    /**
     * 内部Handler
     */
    private class MyHandler extends Handler {
        private WeakReference<UserDeviceCallbackService> mOuter;

        private MyHandler(UserDeviceCallbackService service) {
            mOuter = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UserDeviceCallbackService outer = mOuter.get();
            if (outer != null) {
                switch (msg.what) {

                }
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new MyHandler(this);
        mUser = DBDataUser.getLoginUser(UserDeviceCallbackService.this);
        BM.getManager().registerQueryMsgListener(this);
        BM.getManager().registerReceiveMsgListener(this);
        initLocationClient();
        mLocationClient.startLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BM.getManager().unRegisterQueryMsgListener(this);
        BM.getManager().unRegisterReceiveMsgListener(this);
        mLocationClient.stopLocation();
    }

    private void initLocationClient() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(UserDeviceCallbackService.this);
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
}
