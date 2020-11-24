package cn.yy.freewalker.entity.model;

import android.bluetooth.BluetoothDevice;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2018/9/20 9:51
 */
public class DeviceBleScan {

    public BluetoothDevice device;                                          //设备
    public int rssi;                                                        //信号强度

    public DeviceBleScan(BluetoothDevice device, int rssi) {
        this.device = device;
        this.rssi = rssi;
    }

}
