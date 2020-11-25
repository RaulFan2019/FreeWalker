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
    public static final String NOTIFY_SERVICE = "00001901-0000-1000-8000-00805f9b34fb";             //notify 私有服务
    public static final String NOTIFY_C = "00001a03-0000-1000-8000-00805f9b34fb";                   //Notify 特征值
    public static final String DEBUG_C = "00001a02-0000-1000-8000-00805f9b34fb";                    //debug 特征值

    public final static UUID UUID_NOTIFY_SERVICE = UUID.fromString(NOTIFY_SERVICE);                 //UUID of Notify 私有服务
    public final static UUID UUID_NOTIFY_C = UUID.fromString(NOTIFY_C);                             //UUID of Notify 特征值
    public final static UUID UUID_DEBUG_C = UUID.fromString(DEBUG_C);                              //UUID of debug 特征值


    /* private write service */
    public static final String WRITE_SERVICE = "00001901-0000-1000-8000-00805f9b34fb";              //write 私有服务
    public static final String WRITE_C = "00001a01-0000-1000-8000-00805f9b34fb";                    //write 特征值

    public static final UUID UUID_WRITE_SERVICE = UUID.fromString(WRITE_SERVICE);                   //UUID of Write 私有服务
    public static final UUID UUID_WRITE_C = UUID.fromString(WRITE_C);                               //UUID of Write 特征值

}
