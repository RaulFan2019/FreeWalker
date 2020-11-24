package cn.yy.sdk.ble.entity;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Method;
import java.nio.channels.Channel;
import java.util.UUID;

import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.NotifyManager;
import cn.yy.sdk.ble.array.ConnectStates;
import cn.yy.sdk.ble.array.GattUUIDs;
import cn.yy.sdk.ble.array.PrivatePorts;
import cn.yy.sdk.ble.utils.BLog;
import cn.yy.sdk.ble.utils.ByteU;

import static android.bluetooth.BluetoothDevice.PHY_LE_1M_MASK;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2019/5/13 15:22
 * <p>
 * ble 连接具体的管理类
 */
//@android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ConnectEntity {


    private static final String TAG = "ConnectEntity";                    //TAG
    /* contains of time */
    private static final long DELAY_REPEAT_CONNECT = 1000;                //尝试重新连接的延迟
    private static final long DELAY_REPEAT_DISCOVER = 7 * 1000;           //发现服务的超时判断
    private static final long DELAY_REPEAT_READ = 200;                    //重复读取延迟
    private static final long DELAY_REPEAT_WRITE = 500;                   //重复写入延迟
    private static final long DELAY_REPEAT_NOTIFY = 500;                  //重复写入延迟
    private static final long DELAY_WAIT_CMD = 1000;                       //若发送请求没有响应，再发一次命令

    /* contains of msg */
    private static final int MSG_REPEAT_CONNECT = 0x01;                   //重新连接
    private static final int MSG_DISCOVER_SERVICE = 0x02;                 //重新发现服务
    private static final int MSG_NOTIFY_PRIVATE_C = 0x03;                 //notify 特征值
    private static final int MSG_GET_SYSTEM_INFO = 0x04;                  //获取系统配置

    private static final int MSG_SET_CHANNEL = 0x05;                      //设置channel
    private static final int MSG_SEND_GROUP_CHAT_MSG = 0x06;              //发送groupChat 数据

    /* local data of system */
    private Application mContext;                                          //上下文
    private BluetoothAdapter mBluetoothAdapter;                            //蓝牙适配器

    /* local data about state */
    public boolean mNeedConnect = true;                                    //断开后是否需要重连
    private int mState = ConnectStates.DISCONNECT;                          //连接状态

    /* local data about device*/
    public String mAddress = "";                                             //连接的mac地址
    private String mName;                                                    //设备名称
    private BluetoothGatt mBluetoothGatt;                                    //GATT实例
    private BluetoothGattCallback mGattCallback;                             //GATT回调

    private DeviceSystemInfo mDeviceSystemInfo;                             //系统信息

    private int notifyErrorTimes = 0;                                       //无法notify的次数
    private int mRepeatConnectTimes = 0;

    /* local characteristic*/
    private BluetoothGattCharacteristic mNotifyC;                      //Yiida Notify 特征
    private BluetoothGattCharacteristic mWriteC;                       //Yiida wirte  特征

    private final Object LOCK = new Object();

    private int mLastLength;
    private byte mLastPort;


    /**
     * 消息队列
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!mNeedConnect) {
                return;
            }
            switch (msg.what) {
                //重连
                case MSG_REPEAT_CONNECT:
                    repeatConnect();
                    break;
                //发现服务
                case MSG_DISCOVER_SERVICE:
                    repeatDiscoverGatt();
                    break;
                //notify 私有服务
                case MSG_NOTIFY_PRIVATE_C:
                    notifyPrivateService();
                    break;
                case MSG_GET_SYSTEM_INFO:
                    writeGetSystemInfo();
                    break;
                //设置频道
                case MSG_SET_CHANNEL:
                    writeSetChannel((ChannelInfo) msg.obj);
                    break;
            }
        }
    };

    /**
     * single instance TcpClient
     */
    private static ConnectEntity mBleClient = null;

    private ConnectEntity() {
    }

    public static ConnectEntity getInstance() {
        if (mBleClient == null) {
            synchronized (ConnectEntity.class) {
                mBleClient = new ConnectEntity();
            }
        }
        return mBleClient;
    }


    /**
     * 初始化
     *
     * @param context  上下文
     * @param name     设备名称
     * @param mac      设备地址
     * @param mAdapter
     */
    public void init(Application context, final String name, final String mac, final BluetoothAdapter mAdapter) {
        this.mAddress = mac;
        this.mBluetoothAdapter = mAdapter;
        this.mContext = context;
        this.mName = name;
        this.mNeedConnect = true;
        this.mState = ConnectStates.CONNECTING;

        mHandler.removeCallbacksAndMessages(null);

        NotifyManager.getManager().notifyStateChange(mState);

        //初始化回调
        initCallback();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBluetoothGatt = mBluetoothAdapter.getRemoteDevice(mac)
                        .connectGatt(context, false, mGattCallback, BluetoothDevice.TRANSPORT_LE, PHY_LE_1M_MASK/*, handler*/);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBluetoothGatt = mBluetoothAdapter.getRemoteDevice(mac).connectGatt(context, false, mGattCallback,
                        BluetoothDevice.TRANSPORT_LE);
            } else {
                mBluetoothGatt = mBluetoothAdapter.getRemoteDevice(mac).connectGatt(context, false, mGattCallback);
            }

        } catch (IllegalArgumentException ex) {
            sendMsg(MSG_REPEAT_CONNECT, null, DELAY_REPEAT_CONNECT);
        }
    }


    /**
     * 为了重连初始化
     */
    public void initForReConnect() {
        BLog.e(TAG, "initForReConnect");
        this.mNeedConnect = true;
        this.mState = ConnectStates.CONNECTING;

        mHandler.removeCallbacksAndMessages(null);
        NotifyManager.getManager().notifyStateChange(mState);

        //初始化回调
        initCallback();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBluetoothGatt = mBluetoothAdapter.getRemoteDevice(mAddress)
                        .connectGatt(mContext, false, mGattCallback, BluetoothDevice.TRANSPORT_LE, PHY_LE_1M_MASK);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBluetoothGatt = mBluetoothAdapter.getRemoteDevice(mAddress).connectGatt(mContext, false, mGattCallback,
                        BluetoothDevice.TRANSPORT_LE);
            } else {
                mBluetoothGatt = mBluetoothAdapter.getRemoteDevice(mAddress).connectGatt(mContext, false, mGattCallback);
            }

        } catch (IllegalArgumentException ex) {
            sendMsg(MSG_REPEAT_CONNECT, null, DELAY_REPEAT_CONNECT);
        }
    }

    /**
     * 主动断开连接
     */
    public void disConnect() {
        BLog.e(TAG, "disConnect");
        mState = ConnectStates.DISCONNECT;
        NotifyManager.getManager().notifyStateChange(mState);
        mAddress = "";
        mNeedConnect = false;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mBluetoothGatt != null) {
            BLog.e(TAG, "mBluetoothGatt.disConnect");
            mBluetoothGatt.disconnect();
        }
    }

    /**
     * 获取当前连接状态
     *
     * @return 连接状态
     */
    public int getState() {
        return mState;
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    public String getName() {
        return mName;
    }

    public DeviceSystemInfo getSystemInfo() {
        return mDeviceSystemInfo;
    }

    /**
     * 初始化GATT回调
     */

    private void initCallback() {
        mGattCallback = null;
        mGattCallback = new BluetoothGattCallback() {
            //连接状态发生改变
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                ConnectEntity.this.onConnectionStateChange(gatt, status, newState);
            }

            //发现服务
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                ConnectEntity.this.onServicesDiscovered(gatt, status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int
                    status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    onCharacteristicReadSuccess(characteristic);
                } else {
                    onCharacteristicReadFail(characteristic);
                }
            }

            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                ConnectEntity.this.onCharacteristicChanged(characteristic);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    writeCharacteristicSuccess(characteristic);
                } else {
                    writeCharacteristicFail(characteristic);
                }
            }
        };
    }

    /**
     * 连接状态发生变化
     */
    private synchronized void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
        BLog.v(TAG, "onConnectionStateChange status:" + status + ",newState:" + newState);
        // 连接失败判断
        if (status != BluetoothGatt.GATT_SUCCESS) {
            BLog.i(TAG, "<" + mAddress + ">" + "status !GATT_SUCCESS ,mNeedConnect:" + mNeedConnect);
            //若需要重连
            if (mNeedConnect) {
                mState = ConnectStates.CONNECT_FAIL;
                NotifyManager.getManager().notifyStateChange(mState);
                sendMsg(MSG_REPEAT_CONNECT, null, DELAY_REPEAT_CONNECT);
            }
            return;
        }
        //已连接
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            BLog.i(TAG, "<" + mAddress + ">" + "Connected to GATT server.");
            BLog.i(TAG, "<" + mAddress + ">" + "Attempting to start service discovery");
            mRepeatConnectTimes = 0;
            mHandler.removeCallbacksAndMessages(null);

            if (mBluetoothGatt != null) {
                mBluetoothGatt.discoverServices();
            }
            //若发现服务超时，重新发现服务
            sendMsg(MSG_DISCOVER_SERVICE, null, DELAY_REPEAT_DISCOVER);
            //连接断开
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            BLog.e(TAG, "<" + mAddress + ">" + "Disconnected from GATT server." + ",mNeedConnect:" + mNeedConnect);
            mState = ConnectStates.DISCONNECT;
            NotifyManager.getManager().notifyStateChange(mState);
            if (!mNeedConnect) {
                synchronized (LOCK) {
                    if (mBluetoothGatt != null) {
                        refreshDeviceCache();
                        try {
                            BLog.e(TAG, "BluetoothGatt close");
                            mBluetoothGatt.close();
                        } catch (final Throwable t) {
                            // ignore
                        }
                        mBluetoothGatt = null;
                    }
                }
            } else {
                sendMsg(MSG_REPEAT_CONNECT, null, DELAY_REPEAT_CONNECT);
            }
        }
    }

    /**
     * 发现GATT 服务结束
     *
     * @param gatt
     * @param status
     */
    private synchronized void onServicesDiscovered(BluetoothGatt gatt, int status) {
        mHandler.removeMessages(MSG_DISCOVER_SERVICE);
        mHandler.removeMessages(MSG_REPEAT_CONNECT);
        BM.getManager().stopScan();

        // 发现服务失败
        if (status != BluetoothGatt.GATT_SUCCESS) {
            sendMsg(MSG_REPEAT_CONNECT, null, DELAY_REPEAT_CONNECT);
            return;
        }
        //去除重复回调
        if (mState == ConnectStates.WORKED) {
            return;
        }
        BLog.e(TAG, "real connected");
        mState = ConnectStates.CONNECTED;
        NotifyManager.getManager().notifyStateChange(mState);

        //扫描服务
        for (BluetoothGattService gattService : gatt.getServices()) {
            BLog.e(TAG, "<" + mAddress + ">" + "UUID " + gattService.getUuid().toString());

            //发现 Yiida notify 私有服务
            if (gattService.getUuid().equals(GattUUIDs.UUID_NOTIFY_SERVICE)) {
                mNotifyC = gattService.getCharacteristic(GattUUIDs.UUID_NOTIFY_C);
            }
            //发现 Yiida write 私有服务
            if (gattService.getUuid().equals(GattUUIDs.UUID_WRITE_SERVICE)) {
                mWriteC = gattService.getCharacteristic(GattUUIDs.UUID_WRITE_C);
            }

        }
        BLog.e(TAG, "stop scan");
        //若无法获取Yiida Notify 特征，则视为无法正常工作
        if (mNotifyC == null || mWriteC == null) {
            sendMsg(MSG_REPEAT_CONNECT, null, DELAY_REPEAT_CONNECT);
        } else {
            sendMsg(MSG_NOTIFY_PRIVATE_C, null, DELAY_REPEAT_NOTIFY);
        }
    }


    /**
     * 设置频道
     *
     * @param channel
     * @param priority
     * @param pwdStr
     */
    public void setChannel(final int channel, final int priority, final String pwdStr) {
        ChannelInfo channelInfo = null;
        if (pwdStr.isEmpty()) {
            channelInfo = new ChannelInfo(channel, priority, null);
        } else {
            String[] listPwdStr = pwdStr.split(",");
            int[] pwd = new int[6];
            for (int i = 0; i < listPwdStr.length; i++) {
                pwd[i] = Integer.parseInt(listPwdStr[i]);
            }
            channelInfo = new ChannelInfo(channel, priority, pwd);
        }
        sendMsg(MSG_SET_CHANNEL, channelInfo, 0);
    }

    /**
     * 写入设置频道
     *
     * @param channelInfo
     */
    public void writeSetChannel(final ChannelInfo channelInfo) {
        mHandler.removeMessages(MSG_SET_CHANNEL);
        byte[] data;
        if (channelInfo.pwd == null) {
            data = new byte[6];
        } else {
            data = new byte[22];
        }
        data[0] = (byte) 0xFE;
        data[1] = (byte) 0x95;
        //length
        if (channelInfo.pwd == null) {
            data[2] = 0x03;
        } else {
            data[2] = 0x13;
        }
        //port
        data[3] = PrivatePorts.SET_CHANNEL;
        //channel
        data[4] = (byte) channelInfo.channel;
        //priority
        data[5] = (byte) channelInfo.priority;
        //pwd
        if (channelInfo.pwd != null) {
            data[6] = 0x12;
            data[7] = 0x12;
            data[8] = 0x12;
            data[9] = 0x12;
            data[10] = 0x12;
            data[11] = 0x12;
            data[12] = 0x12;
            data[13] = 0x12;
            data[14] = 0x12;
            data[15] = 0x12;
            data[16] = (byte) channelInfo.pwd[0];
            data[17] = (byte) channelInfo.pwd[1];
            data[18] = (byte) channelInfo.pwd[2];
            data[19] = (byte) channelInfo.pwd[3];
            data[20] = (byte) channelInfo.pwd[4];
            data[21] = (byte) channelInfo.pwd[5];
        }


        mWriteC.setValue(data);
        boolean writeSuccess = mBluetoothGatt.writeCharacteristic(mWriteC);
        if (!writeSuccess) {
            sendMsg(MSG_SET_CHANNEL, channelInfo, DELAY_REPEAT_WRITE);
        }
    }



    /**
     * 发送群组消息
     *
     * @param userId  用户ID
     * @param content 消息内容
     */
    public void sendGroupChatMsg(final int userId, final String content) {
        GroupChatInfo groupChatInfo = new GroupChatInfo(userId, content);
        sendMsg(MSG_SEND_GROUP_CHAT_MSG, groupChatInfo, 0);
    }


    /**
     * 写入发送群组消息
     * @param groupChatInfo
     */
    public void writeSendGroupChatMsg(final GroupChatInfo groupChatInfo){
        mHandler.removeMessages(MSG_SEND_GROUP_CHAT_MSG);
        byte[] contentBytes = groupChatInfo.content.getBytes();
        byte[] userIdBytes = ByteU.intToBytes(groupChatInfo.userId);
        int length = 3 + 2 + 4 + contentBytes.length;
        byte[] data = new byte[length];
        data[0] = (byte) 0xFE;
        data[1] = (byte) 0x95;
        data[3] = (byte)length;
        //port
        data[4] = PrivatePorts.TEXT_MESSAGE;
        //type
        data[5] = PrivatePorts.TYPE_TEXT_MESSAGE_GROUP_CHAT;
        //user Id
        data[6] = userIdBytes[3];
        data[7] = userIdBytes[2];
        data[8] = userIdBytes[1];
        data[9] = userIdBytes[0];
        //content
        for (int i = 0 ; i < contentBytes.length; i++){
            data[10 + i] = contentBytes[i];
        }

        mWriteC.setValue(data);
        boolean writeSuccess = mBluetoothGatt.writeCharacteristic(mWriteC);
        if (!writeSuccess) {
            sendMsg(MSG_SEND_GROUP_CHAT_MSG, groupChatInfo, DELAY_REPEAT_WRITE);
        }
    }


    /**
     * 读取数据成功
     *
     * @param characteristic
     */
    private synchronized void onCharacteristicReadSuccess(final BluetoothGattCharacteristic characteristic) {
        BLog.v(TAG, "onCharacteristicReadSuccess");
    }

    /**
     * 读取数据失败
     *
     * @param characteristic
     */
    private synchronized void onCharacteristicReadFail(final BluetoothGattCharacteristic characteristic) {
        BLog.v(TAG, "onCharacteristicReadFail");
    }

    /**
     * 读取notify数据的结果
     *
     * @param characteristic
     */
    private synchronized void onCharacteristicChanged(final BluetoothGattCharacteristic characteristic) {
        if (GattUUIDs.UUID_NOTIFY_C.equals(characteristic.getUuid())) {
            if (characteristic.getValue() != null) {
                byte[] data = characteristic.getValue();
                if (data == null) {
                    return;
                }
                analysisPrivateData(data);
            }
        }
    }

    /**
     * 写入数据成功
     *
     * @param characteristic
     */
    private synchronized void writeCharacteristicSuccess(final BluetoothGattCharacteristic characteristic) {
        BLog.e(TAG, "writeCharacteristicSuccess:" + ByteU.bytesToHexString(characteristic.getValue()));
    }


    /**
     * 写入数据失败
     *
     * @param characteristic
     */
    private synchronized void writeCharacteristicFail(final BluetoothGattCharacteristic characteristic) {
//        Log.v(TAG, "writeCharacteristicFail");

    }


    /**
     * 解析私有服务的notify数据
     *
     * @param data
     */
    private void analysisPrivateData(final byte[] data) {
        BLog.e(TAG, "analysisData:" + ByteU.bytesToHexString(data));
        if (data.length >= 4
                && data[0] == (byte) 0xFE && data[1] == (byte) 0x95) {
            switch (data[3]) {
                case PrivatePorts.GET_SYSTEM_INFO:
                    mLastPort = PrivatePorts.GET_SYSTEM_INFO;
                    mLastLength = data[2];
                    mHandler.removeMessages(MSG_GET_SYSTEM_INFO);
                    mState = ConnectStates.WORKED;
                    NotifyManager.getManager().notifyStateChange(mState);
                    break;
                //设置频道
                case PrivatePorts.SET_CHANNEL:
                    mHandler.removeMessages(MSG_SET_CHANNEL);
                    NotifyManager.getManager().notifySwitchChannelOK();
                    mLastPort = PrivatePorts.SET_CHANNEL;
                    mLastLength = data[2];
                    break;

            }
            //value
        } else if (mLastPort != 0 && mLastLength == data.length) {
            switch (mLastPort) {
                case PrivatePorts.GET_SYSTEM_INFO:
                    mDeviceSystemInfo = new DeviceSystemInfo(data[0], data[1], data[2]);
                    break;
                //设置频道
                case PrivatePorts.SET_CHANNEL:

                    break;
            }
        }
    }

    /**
     * 发送消息给Handler
     *
     * @param what
     * @param object
     * @param delay
     */
    private void sendMsg(final int what, final Object object, final long delay) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessageDelayed(msg, delay);
    }

    /**
     * 发送消息给Handler
     *
     * @param what
     * @param object
     * @param delay
     */
    private void sendMsg(final int what, final int arg, final Object object, final long delay) {
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = arg;
        msg.obj = object;
        mHandler.sendMessageDelayed(msg, delay);
    }


    /**
     * Method to clear the device cache
     *
     * @return boolean
     */
    public boolean refreshDeviceCache() {
        final BluetoothGatt gatt = mBluetoothGatt;
        if (gatt == null) // no need to be connected
            return false;

        BLog.e(TAG, "Refreshing device cache...");
        BLog.e(TAG, "gatt.refresh() (hidden)");
        /*
         * There is a refresh() method in BluetoothGatt class but for now it's hidden.
         * We will call it using reflections.
         */
        try {
            final Method refresh = gatt.getClass().getMethod("refresh");
            //noinspection ConstantConditions
            return (Boolean) refresh.invoke(gatt);
        } catch (final Exception e) {
            BLog.e(TAG, "An exception occurred while refreshing device:" + e);
            BLog.e(TAG, "gatt.refresh() method not found");
        }
        return false;
    }

    /**
     * 尝试重新连接
     * 原因1. 连接错误
     * 原因2. 连接断开，尝试恢复
     * 原因3. 蓝牙开关导致的重新连接
     */
    private void repeatConnect() {
        mRepeatConnectTimes++;
        if (mRepeatConnectTimes > 5) {
            BM.getManager().recovery();
        } else {
            initForReConnect();
        }
    }

    /**
     * 再次尝试发现服务
     * 原因1. 请求了发现服务，但是没有响应
     */
    private void repeatDiscoverGatt() {
        mBluetoothGatt.discoverServices();
        sendMsg(MSG_DISCOVER_SERVICE, null, DELAY_REPEAT_DISCOVER);
    }

    /**
     * 监听私有服务
     */
    private void notifyPrivateService() {
        if (!setCharacteristicNotification(mNotifyC, true)) {
            notifyErrorTimes++;
            if (notifyErrorTimes > 3) {
                sendMsg(MSG_REPEAT_CONNECT, null, DELAY_REPEAT_CONNECT);
            } else {
                sendMsg(MSG_NOTIFY_PRIVATE_C, null, DELAY_REPEAT_NOTIFY);
            }
        } else {
            sendMsg(MSG_GET_SYSTEM_INFO, null, 0);
        }
    }


    /**
     * 获取系统配置
     */
    private void writeGetSystemInfo() {
        BLog.e(TAG, "writeGetSystemInfo");
        mHandler.removeMessages(MSG_GET_SYSTEM_INFO);
        byte[] data = new byte[4];

        data[0] = (byte) 0xFE;
        data[1] = (byte) 0x95;
        //length
        data[2] = 0x01;
        //port
        data[3] = PrivatePorts.GET_SYSTEM_INFO;

        mWriteC.setValue(data);
        boolean writeSuccess = mBluetoothGatt.writeCharacteristic(mWriteC);
        if (!writeSuccess) {
            sendMsg(MSG_GET_SYSTEM_INFO, null, DELAY_REPEAT_WRITE);
        } else {
            sendMsg(MSG_GET_SYSTEM_INFO, null, DELAY_WAIT_CMD);
        }
    }

    /**
     * 设置当指定characteristic值变化时，发出通知
     *
     * @param characteristic
     * @param enable
     */
    private boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        boolean notifySuccess = true;
        boolean writeDescriptor = true;
        notifySuccess = mBluetoothGatt.setCharacteristicNotification(characteristic, enable);
        if (enable) {

            BluetoothGattDescriptor descriptor = characteristic
                    .getDescriptor(UUID.fromString(GattUUIDs.CLIENT_CHARACTERISTIC_CONFIG));

            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            writeDescriptor = mBluetoothGatt.writeDescriptor(descriptor);
        }
        return (notifySuccess && writeDescriptor);
    }

}

