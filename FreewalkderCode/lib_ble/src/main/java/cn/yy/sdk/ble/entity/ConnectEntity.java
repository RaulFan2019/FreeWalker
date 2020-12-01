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
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.channels.Channel;
import java.time.Year;
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
    private static final int MSG_NOTIFY_PRIVATE_DEBUG = 0x04;                 //notify 特征值
    private static final int MSG_GET_SYSTEM_INFO = 0x05;                  //获取系统配置

    private static final int MSG_SET_CHANNEL = 0x06;                      //设置channel
    private static final int MSG_SEND_GROUP_CHAT_MSG = 0x07;              //发送groupChat 数据
    private static final int MSG_SEND_SINGLE_CHAT_MSG = 0x08;             //发送单聊消息

    private static final int MSG_QUERY_NEARBY_USERS = 0x09;               //查询附近的人

    private static final int MSG_SEND_LOCATION_INFO = 0x10;               //发送位置信息

    private static final int MSG_SET_SIGNAL = 0x11;                        //设置系统信息


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
    private BluetoothGattCharacteristic mNotifyC;                      //YY Notify 特征
    private BluetoothGattCharacteristic mWriteC;                       //YY wirte  特征
    private BluetoothGattCharacteristic mDebugC;                       //Debug 特征

    private final Object LOCK = new Object();

    private GroupPkgEntity mGroupPkg;                                 //拼包信息

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
                //debug notify
                case MSG_NOTIFY_PRIVATE_DEBUG:
                    notifyPrivateDebug();
                    break;
                case MSG_GET_SYSTEM_INFO:
                    writeGetSystemInfo();
                    break;
                //设置频道
                case MSG_SET_CHANNEL:
                    writeSetChannel((ChannelInfo) msg.obj);
                    break;
                //发送群消息
                case MSG_SEND_GROUP_CHAT_MSG:
                    writeSendGroupChatMsg((GroupChatInfo) msg.obj);
                    break;
                case MSG_SEND_SINGLE_CHAT_MSG:
                    writeSendSingleChatMsg((SingleChatInfo) msg.obj);
                    break;
                //查询附近的人
                case MSG_QUERY_NEARBY_USERS:
                    writeQueryNearbyUsers();
                    break;
                //发送位置信息
                case MSG_SEND_LOCATION_INFO:
                    writeSendLocationInfo((LocationInfo) msg.obj);
                    break;
                //设置信号
                case MSG_SET_SIGNAL:
                    writeSetSignal((Boolean) msg.obj);
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
        this.mDeviceSystemInfo = null;
        this.mGroupPkg = new GroupPkgEntity();

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
                mDebugC = gattService.getCharacteristic(GattUUIDs.UUID_DEBUG_C);
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
     * 查询附近的用户
     */
    public void queryNearbyUsers() {
        sendMsg(MSG_QUERY_NEARBY_USERS, null, 0);
    }


    public void writeQueryNearbyUsers() {
        mHandler.removeMessages(MSG_QUERY_NEARBY_USERS);
        byte[] data = new byte[5];

        data[0] = (byte) 0xFE;
        data[1] = (byte) 0x95;
        data[2] = (byte) 2;
        //port
        data[3] = PrivatePorts.QUERY_MESSAGE;
        //type
        data[4] = PrivatePorts.TYPE_QUERY_MESSAGE_LOCATION;

        mWriteC.setValue(data);
        boolean writeSuccess = mBluetoothGatt.writeCharacteristic(mWriteC);
        if (!writeSuccess) {
            sendMsg(MSG_QUERY_NEARBY_USERS, null, DELAY_REPEAT_WRITE);
        }
    }


    /**
     * 设置信号是否是增强模式
     *
     * @param isEnhance
     */
    public void setSignal(final boolean isEnhance) {
        sendMsg(MSG_SET_SIGNAL, isEnhance, 0);
    }

    public void writeSetSignal(final boolean isEnhance) {
        mHandler.removeMessages(MSG_SET_SIGNAL);

        byte[] data = new byte[6];

        data[0] = (byte) 0xFE;
        data[1] = (byte) 0x95;
        //length
        data[2] = 0x03;
        //port
        data[3] = PrivatePorts.SET_SYSTEM;
        //power
        if (isEnhance) {
            data[4] = 0x16;
            mDeviceSystemInfo.power = 0x16;
        } else {
            data[4] = 0x11;
            mDeviceSystemInfo.power = 0x11;
        }
        //max channel
        data[5] = 30;

        mWriteC.setValue(data);
        boolean writeSuccess = mBluetoothGatt.writeCharacteristic(mWriteC);
        if (!writeSuccess) {
            sendMsg(MSG_SET_SIGNAL, isEnhance, DELAY_REPEAT_WRITE);
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
     *
     * @param groupChatInfo
     */
    public void writeSendGroupChatMsg(final GroupChatInfo groupChatInfo) {
        mHandler.removeMessages(MSG_SEND_GROUP_CHAT_MSG);
        byte[] contentBytes = groupChatInfo.content.getBytes();
        byte[] userIdBytes = ByteU.intToBytes(groupChatInfo.userId);
        int length = 2 + 4 + contentBytes.length;
        byte[] data = new byte[length + 3];

        data[0] = (byte) 0xFE;
        data[1] = (byte) 0x95;
        data[2] = (byte) length;
        //port
        data[3] = PrivatePorts.TEXT_MESSAGE;
        //type
        data[4] = PrivatePorts.TYPE_TEXT_MESSAGE_GROUP_CHAT;
        //user Id
        data[5] = userIdBytes[0];
        data[6] = userIdBytes[1];
        data[7] = userIdBytes[2];
        data[8] = userIdBytes[3];

        //content
        for (int i = 0; i < contentBytes.length; i++) {
            data[9 + i] = contentBytes[i];
        }

        mWriteC.setValue(data);
        boolean writeSuccess = mBluetoothGatt.writeCharacteristic(mWriteC);
        if (!writeSuccess) {
            sendMsg(MSG_SEND_GROUP_CHAT_MSG, groupChatInfo, DELAY_REPEAT_WRITE);
        }
    }


    /**
     * 发送单聊消息
     *
     * @param userId     本人ID
     * @param destUserId 目标ID
     * @param content    消息内容
     */
    public void sendSingleChatMsg(final int userId, final int destUserId, final String content) {
        SingleChatInfo singleChatInfo = new SingleChatInfo(userId, destUserId, content);
        sendMsg(MSG_SEND_SINGLE_CHAT_MSG, singleChatInfo, 0);
    }


    public void writeSendSingleChatMsg(final SingleChatInfo singleChatInfo) {
        mHandler.removeMessages(MSG_SEND_SINGLE_CHAT_MSG);
        byte[] contentBytes = singleChatInfo.content.getBytes();
        byte[] userIdBytes = ByteU.intToBytes(singleChatInfo.userId);
        byte[] destUserIdBytes = ByteU.intToBytes(singleChatInfo.destUserId);

        int length = 2 + 4 + 4 + contentBytes.length;
        byte[] data = new byte[length + 3];

        data[0] = (byte) 0xFE;
        data[1] = (byte) 0x95;
        data[2] = (byte) length;
        //port
        data[3] = PrivatePorts.TEXT_MESSAGE;
        //type
        data[4] = PrivatePorts.TYPE_TEXT_MESSAGE_SINGLE_CHAT;
        //user Id
        data[5] = userIdBytes[0];
        data[6] = userIdBytes[1];
        data[7] = userIdBytes[2];
        data[8] = userIdBytes[3];
        //dest user id
        data[9] = destUserIdBytes[0];
        data[10] = destUserIdBytes[1];
        data[11] = destUserIdBytes[2];
        data[12] = destUserIdBytes[3];

        //content
        for (int i = 0; i < contentBytes.length; i++) {
            data[13 + i] = contentBytes[i];
        }

        mWriteC.setValue(data);
        boolean writeSuccess = mBluetoothGatt.writeCharacteristic(mWriteC);
        if (!writeSuccess) {
            sendMsg(MSG_SEND_SINGLE_CHAT_MSG, singleChatInfo, DELAY_REPEAT_WRITE);
        }
    }

    /**
     * 发送位置信息
     */
    public void sendLocationInfo(final int userId, final double latitude, final double longtitude,
                                 final int gender, final int age, final int sex, final int job,
                                 final int height, final int weight, final String userName) {

        int sendLatitude = (int) ((latitude + 90) * 1000000);
        int sendLongtitude = (int) ((longtitude + 180) * 1000000);
        BLog.e(TAG, "sendLocationInfo sendLatitude:" + sendLatitude);
        BLog.e(TAG, "sendLocationInfo sendLongtitude:" + sendLongtitude);

        LocationInfo locationInfo = new LocationInfo(mDeviceSystemInfo.currChannel,
                userId, sendLatitude, sendLongtitude,
                gender, age, sex, job, height, weight, userName);
        sendMsg(MSG_SEND_LOCATION_INFO, locationInfo, 0);
    }


    public void writeSendLocationInfo(final LocationInfo locationInfo) {
        BLog.e(TAG, "writeSendLocationInfo userId:" + locationInfo.userId);
        mHandler.removeMessages(MSG_SEND_LOCATION_INFO);
        byte[] nameBytes = locationInfo.userName.getBytes();
        byte[] userIdBytes = ByteU.intToBytes(locationInfo.userId);
        byte[] latitudeBytes = ByteU.intToBytes(locationInfo.latitude);
        byte[] longtitudeBytes = ByteU.intToBytes(locationInfo.longtitude);
        int length = 2 + 19 + nameBytes.length;
        byte[] data = new byte[length + 3];

        data[0] = (byte) 0xFE;
        data[1] = (byte) 0x95;
        data[2] = (byte) length;
        //port
        data[3] = PrivatePorts.TEXT_MESSAGE;
        //type
        data[4] = PrivatePorts.TYPE_TEXT_MESSAGE_LOCATION;
        //channel
        data[5] = (byte) locationInfo.channel;
        //user Id
        data[6] = userIdBytes[0];
        data[7] = userIdBytes[1];
        data[8] = userIdBytes[2];
        data[9] = userIdBytes[3];
        //latitude
        data[10] = latitudeBytes[0];
        data[11] = latitudeBytes[1];
        data[12] = latitudeBytes[2];
        data[13] = latitudeBytes[3];
        //longtitude
        data[14] = longtitudeBytes[0];
        data[15] = longtitudeBytes[1];
        data[16] = longtitudeBytes[2];
        data[17] = longtitudeBytes[3];
        //gender
        data[18] = (byte) locationInfo.gender;
        //age
        data[19] = (byte) locationInfo.age;
        //sex
        data[20] = (byte) locationInfo.sex;
        //job
        data[21] = (byte) locationInfo.job;
        //height
        data[22] = (byte) locationInfo.height;
        //weight
        data[23] = (byte) locationInfo.weight;
        //userNam
        for (int i = 0; i < nameBytes.length; i++) {
            data[24 + i] = nameBytes[i];
        }

        mWriteC.setValue(data);
        boolean writeSuccess = mBluetoothGatt.writeCharacteristic(mWriteC);
        if (!writeSuccess) {
            sendMsg(MSG_SEND_LOCATION_INFO, locationInfo, DELAY_REPEAT_WRITE);
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
        } else if (GattUUIDs.UUID_DEBUG_C.equals(characteristic.getUuid())) {
            if (characteristic.getValue() != null) {
                byte[] data = characteristic.getValue();
                if (data == null) {
                    return;
                }
//                BLog.e(TAG, "debug data:" + ByteU.bytesToHexString(data));
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
        //抓到头信息
        if (data.length >= 4
                && data[0] == (byte) 0xFE && data[1] == (byte) 0x95) {
            if (mGroupPkg.insertPkgHead(data) == GroupPkgEntity.INSERT_FINISH) {
                analysisGroupData();
            }
            //value
        } else {
            if (mGroupPkg.insertPkgLeft(data) == GroupPkgEntity.INSERT_FINISH) {
                analysisGroupData();
            }
        }
    }


    /**
     * 解析拼包完成的数据
     */
    private void analysisGroupData() {
        switch (mGroupPkg.port) {
            //设备系统信息
            case PrivatePorts.GET_SYSTEM_INFO:
                mHandler.removeMessages(MSG_GET_SYSTEM_INFO);
                mState = ConnectStates.WORKED;
                mDeviceSystemInfo = new DeviceSystemInfo(mGroupPkg.listData.get(0),
                        mGroupPkg.listData.get(1), mGroupPkg.listData.get(2), mGroupPkg.listData.get(3));
                NotifyManager.getManager().notifyStateChange(mState);
                break;
            //设置频道成功
            case PrivatePorts.SET_CHANNEL:
                BLog.e(TAG, "receive SET_CHANNEL channel:" + mGroupPkg.listData.get(0) + ",priority:" + mGroupPkg.listData.get(1));
                mDeviceSystemInfo.currChannel = mGroupPkg.listData.get(0);
                mDeviceSystemInfo.priority = mGroupPkg.listData.get(1);
                mHandler.removeMessages(MSG_SET_CHANNEL);
                NotifyManager.getManager().notifySwitchChannelOK();
                break;
            //接收消息
            case PrivatePorts.TEXT_MESSAGE:
                switch (mGroupPkg.listData.get(8)) {
                    //fe95 11 01 fec192bf 0000 91 0d 01 00000009 616263
                    //群聊消息
                    case PrivatePorts.TYPE_TEXT_MESSAGE_GROUP_CHAT:
                        BLog.e(TAG, "receive GROUP MESSAGE");
                        try {
                            int userId = (int) ByteU.bytesToLong(
                                    new byte[]{mGroupPkg.listData.get(9), mGroupPkg.listData.get(10),
                                            mGroupPkg.listData.get(11), mGroupPkg.listData.get(12)});
                            byte[] contentB = new byte[mGroupPkg.targetSize - 13];
                            for (int i = 0; i < contentB.length; i++) {
                                contentB[i] = mGroupPkg.listData.get(i + 13);
                            }
                            String content = new String(contentB, "UTF-8");
                            GroupChatInfo groupChatInfo = new GroupChatInfo(userId, content);
                            NotifyManager.getManager().notifyReceiveGroupMsg(groupChatInfo);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    //0000000f 0000000a 68656c70
                    case PrivatePorts.TYPE_TEXT_MESSAGE_SINGLE_CHAT:
                        BLog.e(TAG, "receive SINGLE MESSAGE");
                        try {
                            int userId = (int) ByteU.bytesToLong(
                                    new byte[]{mGroupPkg.listData.get(9), mGroupPkg.listData.get(10),
                                            mGroupPkg.listData.get(11), mGroupPkg.listData.get(12)});
                            int destUserId = (int) ByteU.bytesToLong(
                                    new byte[]{mGroupPkg.listData.get(13), mGroupPkg.listData.get(14),
                                            mGroupPkg.listData.get(15), mGroupPkg.listData.get(16)});
                            byte[] contentB = new byte[mGroupPkg.targetSize - 17];
                            for (int i = 0; i < contentB.length; i++) {
                                contentB[i] = mGroupPkg.listData.get(i + 17);
                            }
                            String content = new String(contentB, "UTF-8");
                            SingleChatInfo singleChatInfo = new SingleChatInfo(userId, destUserId, content);
                            NotifyManager.getManager().notifyReceiveSingleChatMsg(singleChatInfo);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    //获取位置信息
                    //17 0000000a 073a2058 11f9bd00 07 02 01 02 02 e88c83e6809de8bf9c00
                    case PrivatePorts.TYPE_TEXT_MESSAGE_LOCATION:
                        BLog.e(TAG, "receive LOCATION MESSAGE");
                        try {
                            int channel = mGroupPkg.listData.get(9);
                            int userId = (int) ByteU.bytesToLong(
                                    new byte[]{mGroupPkg.listData.get(10), mGroupPkg.listData.get(11),
                                            mGroupPkg.listData.get(12), mGroupPkg.listData.get(13)});
                            int latitude = (int) ByteU.bytesToLong(
                                    new byte[]{mGroupPkg.listData.get(14), mGroupPkg.listData.get(15),
                                            mGroupPkg.listData.get(16), mGroupPkg.listData.get(17)});
                            int longtitude = (int) ByteU.bytesToLong(
                                    new byte[]{mGroupPkg.listData.get(18), mGroupPkg.listData.get(19),
                                            mGroupPkg.listData.get(20), mGroupPkg.listData.get(21)});
                            int gender = mGroupPkg.listData.get(22);
                            int age = mGroupPkg.listData.get(23);
                            int sex = mGroupPkg.listData.get(24);
                            int job = mGroupPkg.listData.get(25);
                            int height = mGroupPkg.listData.get(26);
                            int weight = mGroupPkg.listData.get(27);

                            byte[] userNameB = new byte[mGroupPkg.targetSize - 28];
                            for (int i = 0; i < userNameB.length; i++) {
                                userNameB[i] = mGroupPkg.listData.get(i + 28);
                            }
                            String userName = new String(userNameB, "UTF-8");

                            BLog.e(TAG, "userName:" + userName);
                            NotifyManager.getManager().notifyReceiveLocationMsg(new LocationInfo(channel, userId, latitude,
                                    longtitude, gender, age, sex, job, height, weight, userName));

                        } catch (UnsupportedEncodingException e) {
                            BLog.e(TAG, "e:" + e.getMessage());
                            e.printStackTrace();
                        }


                        break;
                }
                //查询消息
            case PrivatePorts.QUERY_MESSAGE:
                switch (mGroupPkg.listData.get(8)) {
                    //查询位置的消息
                    case PrivatePorts.TYPE_QUERY_MESSAGE_LOCATION:
                        NotifyManager.getManager().notifyQueryLocation();
                        break;
                }
                break;
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
            notifyPrivateDebug();

        }
    }


    /**
     * notify debug
     */
    private void notifyPrivateDebug() {
        BLog.e(TAG, "notifyPrivateDebug");
        if (!setCharacteristicNotification(mDebugC, true)) {
            notifyErrorTimes++;
            if (notifyErrorTimes > 5) {
                sendMsg(MSG_REPEAT_CONNECT, null, DELAY_REPEAT_CONNECT);
            } else {
                sendMsg(MSG_NOTIFY_PRIVATE_DEBUG, null, DELAY_REPEAT_NOTIFY);
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

