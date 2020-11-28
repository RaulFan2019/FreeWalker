package cn.yy.sdk.ble.array;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2019/5/13 13:25
 */
public class NotifyActions {

    public static final int CONNECT_STATE = 0x01;               //连接状态变化

    public static final int BLE_STATE = 0x02;                   //蓝牙状态变化

    public static final int SWITCH_CHANNEL = 0x03;              //切换频道

    public static final int RECEIVE_GROUP_MSG = 0x04;              //收到群消息

    public static final int RECEIVE_SINGLE_MSG = 0x05;              //收到单聊消息

    public static final int QUERY_LOCATION = 0x06;              //查询位置信息

    public static final int RECEIVE_LOCATION_INFO = 0x07;           //收到位置信息
}
