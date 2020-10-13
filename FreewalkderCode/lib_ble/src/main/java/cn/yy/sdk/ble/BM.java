package cn.yy.sdk.ble;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.os.Build;
import android.os.LocaleList;

import java.io.File;
import java.util.Locale;

import cn.yy.sdk.ble.array.ConnectStates;
import cn.yy.sdk.ble.entity.ConnectEntity;
import cn.yy.sdk.ble.observer.ConnectListener;
import cn.yy.sdk.ble.subject.ConnectSubjectImp;
import cn.yy.sdk.ble.utils.AppU;
import cn.yy.sdk.ble.utils.BLog;


/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2019/5/13 13:14
 */
public class BM {

    private static final String TAG = "BM";

    /* 全局变量 */
    private static BM instance;                                   //唯一实例

    private Application mContext;                                  //上下文
    private String mPkgName;                                       //包名

    private boolean debug;                                         //是否打印日志

    private String mConnectMac;                                    //连接地址
    private String mConnectName;                                   //连接名称

    /* 蓝牙管理 */
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    /* callback 管理*/
    private ConnectSubjectImp mConnectSub;                          //连接状态变化的订阅管理

    private BM() {
    }

    /**
     * 获取堆栈管理的单一实例
     */
    public static BM getManager() {
        if (instance == null) {
            instance = new BM();
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param application app
     * @return 是否已经初始化
     */
    public boolean init(Application application) {
        BLog.v(TAG, "init");
        mConnectMac = "";
        mContext = application;
        mPkgName = AppU.getPackageName(mContext);

        initCallback();
        initBleAdapter();
        return true;
    }

    /**
     * 初始化所有的callback 管理对象
     */
    private void initCallback() {
        mConnectSub = new ConnectSubjectImp();
    }


    /* =============================== Device State ============================================= */

    /**
     * 获取连接状态
     * 1. 若设备未连接或还未尝试连接设备，则显示断开状态
     * 2. 若设备处于尝试连接状态或已连接状态，则显示当前状态
     *
     * @return 返回连接状态
     */
    public int getConnectState() {
        return ConnectEntity.getInstance().getState();
    }

    public String getConnectMac() {
        return mConnectMac;
    }

    public String getConnectName() {
        return mConnectName;
    }

    /* =============================== Callback =============================================== */

    /**
     * 断开连接
     * 1. 断开蓝牙连接，并且将连接对象设为空
     */
    public void disConnectDevice() {
        BLog.e(TAG, "disConnectDevice");
        ConnectEntity.getInstance().disConnect();
        stopScan();
    }

    /**
     * 重新建立连接
     *
     * @param address
     */
    public void replaceConnect(final String address) {
        mConnectMac = address;
        if (!mConnectMac.equals("")) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }


    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }


    /**
     * 增加一个新的连接
     *
     * @param address
     */
    public void addNewConnect(final String address) {
        BLog.e(TAG, "mConnectMac:" + address);
        if (address == null) {
            return;
        }
        mConnectMac = address;
        //若已连接的设备与目标设备不一致，则断开连接
        if (!ConnectEntity.getInstance().mAddress.equals(mConnectMac)) {
            ConnectEntity.getInstance().disConnect();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            if (ConnectEntity.getInstance().getState() < ConnectStates.CONNECTING){
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }else {
                ConnectEntity.getInstance().mNeedConnect = true;
            }
        }
    }


    /**
     * 增加一个新的连接
     *
     * @param address
     */
    public void addNewConnect(final String address, final String deviceName, final boolean needScan) {
        BLog.v(TAG, "addNewConnect:" + address);
        if (address == null) {
            return;
        }
        mConnectMac = address;

        //若已连接的设备与目标设备不一致，则断开连接
        if (!ConnectEntity.getInstance().mAddress.equals(mConnectMac)) {
            ConnectEntity.getInstance().disConnect();
            if (needScan) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                ConnectEntity.getInstance().init(mContext, deviceName, mConnectMac, mBluetoothAdapter);
            }
        } else {
            ConnectEntity.getInstance().mNeedConnect = true;
        }
    }

    /**
     * 恢复
     */
    public void recovery() {
        BLog.e(TAG, "recovery");
        disConnectDevice();
        initBleAdapter();
        if (!mConnectMac.equals("")) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        if (mBluetoothAdapter != null && mLeScanCallback != null) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    public String getPkgName() {
        return mPkgName;
    }

    /**
     * 初始化蓝牙适配器
     */
    private void initBleAdapter() {
        bluetoothManager = (BluetoothManager) mContext.getSystemService(mContext.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                BLog.v(TAG, "onLeScan:<" + device.getAddress() + ">");
                if (device.getAddress().equals(mConnectMac)) {
                    mConnectName = device.getName();
                    ConnectEntity.getInstance().init(mContext, mConnectName, mConnectMac, mBluetoothAdapter);
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }
        };
    }




    /**
     * 【消息】【注册】连接状态
     *
     * @param observer 连接状态监听回调
     */
    public void registerConnectListener(ConnectListener observer) {
        mConnectSub.attach(observer);
    }

    /**
     * 【消息】【注销】连接状态
     *
     * @param observer 连接状态监听回调
     */
    public void unRegisterConnectListener(ConnectListener observer) {
        mConnectSub.detach(observer);
    }


    /**
     * 【消息】【发布】连接状态
     *
     * @param connectState 连接状态
     */
    protected void notifyStateChange(final int connectState) {
        mConnectSub.notify(connectState);
    }


}