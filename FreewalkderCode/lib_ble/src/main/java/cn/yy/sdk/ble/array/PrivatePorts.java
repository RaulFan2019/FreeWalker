package cn.yy.sdk.ble.array;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/24 14:57
 */
public class PrivatePorts {

    public static final byte GET_SYSTEM_INFO = 0x64;                        //获取系统信息
    public static final byte GET_VERSION_INFO = 0x67;                       //获取版本信息

    public static final byte SET_CHANNEL = 0x65;                            //设置频道
    public static final byte SET_NAME = 0x68;                               //设置名称

    public static final byte SET_SYSTEM = 0x66;                             //设置系统信息

    public static final byte PPT = 0x70;                                    //PPT

    public static final byte TEXT_MESSAGE = 0x01;                           //发送文本消息
    public static final byte QUERY_MESSAGE = 0x03;                          //查询消息

    public static final byte TYPE_TEXT_MESSAGE_GROUP_CHAT = 0x01;           //群组消息
    public static final byte TYPE_TEXT_MESSAGE_SINGLE_CHAT = 0x02;          //单独消息
    public static final byte TYPE_QUERY_MESSAGE_LOCATION = 0x03;            //查询位置信息
    public static final byte TYPE_TEXT_MESSAGE_LOCATION = 0x04;              //发送位置信息

}
