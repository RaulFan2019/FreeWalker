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
    public int power;
    public int voltage;                 //电压
    public int pptAutoHold;             //ppt模式是否打开
    public int maxChannel;

    public DeviceSystemInfo() {
    }

    public DeviceSystemInfo(int dataVersion, int currChannel, int priority, int power, int voltage,
                            int pptAutoHold, int maxChannel) {
        this.dataVersion = dataVersion;
        this.currChannel = currChannel;
        this.priority = priority;
        this.power = power;
        this.voltage = voltage;
        this.pptAutoHold = pptAutoHold;
        this.maxChannel = maxChannel;
    }
}
