package cn.yy.sdk.ble.array;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2019/5/13 13:24
 */
public class ConnectStates {



    public static final int DISCONNECT = 0x01;                              //断开连接

    public static final int CONNECTING = 0x02;                              //正在连接

    public static final int CONNECT_FAIL = 0x03;                            //尝试连接，但连接失败

    public static final int CONNECTED = 0x04;                               //已连接(仅Ble Gatt 连接成功，不代表已经能正常工作)

    public static final int WORKED = 0x05;                                  //工作状态

}
