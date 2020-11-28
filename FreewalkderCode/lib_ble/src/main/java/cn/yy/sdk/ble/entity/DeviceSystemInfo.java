package cn.yy.sdk.ble.entity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/24 19:38
 */
public class DeviceSystemInfo {

    public int dataVersion;
    public int currChannel;
    public int priority;

    public DeviceSystemInfo() {
    }

    public DeviceSystemInfo(int dataVersion, int currChannel, int priority) {
        this.dataVersion = dataVersion;
        this.currChannel = currChannel;
        this.priority = priority;
    }
}