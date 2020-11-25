package cn.yy.sdk.ble.array;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/24 14:57
 */
public class PrivatePorts {

    public static final byte GET_SYSTEM_INFO = 0x64;                        //包长度错误

    public static final byte SET_CHANNEL = 0x65;                            //包长度错误

    public static final byte TEXT_MESSAGE = 0x01;                           //发送文本消息

    public static final byte TYPE_TEXT_MESSAGE_GROUP_CHAT = 0x01;           //群组消息
    public static final byte TYPE_TEXT_MESSAGE_SINGLE_CHAT = 0x02;          //单独消息


}
