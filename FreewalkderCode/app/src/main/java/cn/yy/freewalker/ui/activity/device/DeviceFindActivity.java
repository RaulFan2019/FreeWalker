package cn.yy.freewalker.ui.activity.device;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.DeviceConfig;
import cn.yy.freewalker.data.DBDataDevice;
import cn.yy.freewalker.data.SPDataUser;
import cn.yy.freewalker.data.SpAppData;
import cn.yy.freewalker.entity.model.DeviceBleScan;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.adapter.DeviceScanListAdapter;
import cn.yy.freewalker.ui.widget.common.RippleView;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.utils.SystemU;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.array.ConnectStates;
import cn.yy.sdk.ble.observer.ConnectListener;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 22:20
 */
public class DeviceFindActivity extends BaseActivity implements AdapterView.OnItemClickListener, ConnectListener {

    /* contains */
    private static final String TAG = "DeviceFindActivity";


    private static final int MSG_STOP_SCAN = 0x01;                  //停止扫描消息
    private static final int INTERVAL_SCAN = 3 * 1000;              //扫描时间

    private static final int MSG_STOP_CONNECT = 0x02;               //停止连接时间
    private static final int INTERVAL_STOP_CONNECT = 3 * 1000;      //停止连接时间间隔

    //权限
    final String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    public static final int OPEN = 0;
    public static final int SCANNING = 1;                           //正在扫描
    public static final int SCAN_FAIL = 2;                          //扫描失败
    public static final int CONNECTING = 3;                         //正在连接
    public static final int CONNECT_FAIL = 4;                       //连接失败
    public static final int CONNECTED = 5;                          //连接成功

    /* view */
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll_tip)
    LinearLayout llTip;
    @BindView(R.id.ripple_scan)
    RippleView rpv;

    DeviceScanListAdapter adapter;
    DialogBuilder mDialogBuilder;
    @BindView(R.id.ll_open)
    LinearLayout llOpen;
    @BindView(R.id.ll_find)
    LinearLayout llFind;

    /* local data */
    private int mState = 0;
    private BluetoothAdapter mBluetoothAdapter;                     //蓝牙适配器

    private List<DeviceBleScan> listScan = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_find;
    }

    @OnClick({R.id.btn_add_now, R.id.ll_tip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_now:
                mState = SCANNING;
                llFind.setVisibility(View.VISIBLE);
                llOpen.setVisibility(View.GONE);
                startScan();
                break;
            case R.id.ll_tip:
                showScanAgainDialog();
                break;
        }
    }


    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            //停止扫描
            case MSG_STOP_SCAN:
                stopScan();
                break;
            //停止连接
            case MSG_STOP_CONNECT:
                stopConnect();
                break;
        }
    }


    @Override
    public void connectStateChange(int state) {
        if (state >= ConnectStates.WORKED) {
            DBDataDevice.addNewDevice(SPDataUser.getAccount(DeviceFindActivity.this),
                    BM.getManager().getConnectMac(),BM.getManager().getConnectName());
            mHandler.removeMessages(MSG_STOP_CONNECT);
            new ToastView(DeviceFindActivity.this, getString(R.string.device_tip_connect_ok), -1);
            finish();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            },500);
        }
    }

    /**
     * BLE 扫描回调
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            //筛选设备名称不为空 && 设备名称前缀为 LIGHTEN
            if (device.getName() != null && device.getName().equals("FreeWLK")) {
                for (DeviceBleScan bs : listScan) {
                    if ((bs.device.getAddress().equals(device.getAddress()))) {
                        bs.rssi = rssi;
                        return;
                    }
                }
                DeviceBleScan bs = new DeviceBleScan(device, rssi);
                listScan.add(bs);
            }
        }
    };

    /**
     * 列表点击
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startConnect(listScan.get(position));
    }

    @Override
    protected void initData() {
        mDialogBuilder = new DialogBuilder();
    }

    @Override
    protected void initViews() {
        adapter = new DeviceScanListAdapter(listScan);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    protected void doMyCreate() {
        BM.getManager().registerConnectListener(this);
        checkBLEFeature();
    }

    @Override
    protected void causeGC() {
        BM.getManager().unRegisterConnectListener(this);
    }


    /**
     * 检查BLE是否可用
     */
    private void checkBLEFeature() {
        //判断是否支持蓝牙4.0
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            new ToastView(this, getString(R.string.device_error_not_support), -1);
            finish();
        }
        //申请权限
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(data -> {
                })
                .onDenied(data -> {
                    new ToastView(this, getString(R.string.device_tip_location_permission), -1);
                    finish();
                })
                .start();
        //获取蓝牙适配器
        if (mBluetoothAdapter == null) {
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        //判断是否支持蓝牙
        if (mBluetoothAdapter == null) {
            //不支持
            Toast.makeText(this, "本设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            //打开蓝牙
            if (!mBluetoothAdapter.isEnabled()) {
                startActivity(BluetoothOffActivity.class);
                finish();
            }
        }
        //是否打开了位置服务
        if (!SystemU.isLocationEnabled(DeviceFindActivity.this)) {
            startActivity(LocationOffActivity.class);
            finish();
        }
    }

    /**
     * 开始扫描
     */
    private void startScan() {
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        rpv.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(MSG_STOP_SCAN, INTERVAL_SCAN);
        llTip.setVisibility(View.GONE);
    }


    /**
     * 停止扫描
     */
    private void stopScan() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        rpv.setVisibility(View.INVISIBLE);
        if (listScan.size() == 0) {
            showScanAgainDialog();
        } else {
            llTip.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 开始连接
     */
    private void startConnect(final DeviceBleScan deviceBleScan) {
        BM.getManager().addNewConnect(deviceBleScan.device.getAddress(), "", false);
        new ToastView(DeviceFindActivity.this, getString(R.string.device_toast_connecting), -1);
        mHandler.sendEmptyMessageDelayed(MSG_STOP_CONNECT, INTERVAL_STOP_CONNECT);
    }


    /**
     * 停止连接
     */
    private void stopConnect() {
        showConnectErrorDialog();
    }

    /**
     * 显示再次扫描对话框
     */
    private void showScanAgainDialog() {
        ArrayList<String> listSelect = new ArrayList<>();
        listSelect.add(getString(R.string.device_action_dialog_try_again));
        listSelect.add(getString(R.string.device_action_dialog_exit));
        mDialogBuilder.showSingleSelectDialog(DeviceFindActivity.this,
                getString(R.string.device_title_dialog_scan_none), listSelect);
        mDialogBuilder.setSingleSelectDialogListener(pos -> {
            //重试
            if (pos == 0) {
                startScan();
            } else {
                finish();
            }
        });
    }


    /**
     * 显示再次扫描对话框
     */
    private void showConnectErrorDialog() {
        ArrayList<String> listSelect = new ArrayList<>();
        listSelect.add(getString(R.string.device_action_dialog_try_again));
        listSelect.add(getString(R.string.device_action_dialog_exit));
        mDialogBuilder.showSingleSelectDialog(DeviceFindActivity.this,
                getString(R.string.device_title_dialog_connect_fail), listSelect);
        mDialogBuilder.setSingleSelectDialogListener(pos -> {
            //重试
            if (pos == 0) {
                startScan();
            } else {
                finish();
            }
        });
    }


}
