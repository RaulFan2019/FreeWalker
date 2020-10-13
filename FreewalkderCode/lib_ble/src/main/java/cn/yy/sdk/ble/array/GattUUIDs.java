package cn.yy.sdk.ble.array;

import java.util.UUID;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2019/5/13 13:25
 */
public class GattUUIDs {

    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";


    /* private Notify service */
    public static final String YIIDA_NOTIFY_SERVICE = "0000FFE0-0000-1000-8000-00805f9b34fb";            //notify 私有服务
    public static final String YIIDA_NOTIFY_C = "0000FFE4-0000-1000-8000-00805f9b34fb";                  //Notify 特征值

    public final static UUID UUID_YIIDA_NOTIFY_SERVICE = UUID.fromString(YIIDA_NOTIFY_SERVICE);          //UUID of Notify 私有服务
    public final static UUID UUID_YIIDA_NOTIFY_C = UUID.fromString(YIIDA_NOTIFY_C);                      //UUID of Notify 特征值


    /* private write service */
    public static final String YIIDA_WRITE_SERVICE = "0000FFE5-0000-1000-8000-00805f9b34fb";            //write 私有服务
    public static final String YIIDA_WRITE_C = "0000FFE9-0000-1000-8000-00805f9b34fb";                  //write 特征值

    public static final UUID UUID_WRITE_SERVICE = UUID.fromString(YIIDA_WRITE_SERVICE);           //UUID of Write 私有服务
    public static final UUID UUID_WRITE_C = UUID.fromString(YIIDA_WRITE_C);                       //UUID of Write 特征值

}
