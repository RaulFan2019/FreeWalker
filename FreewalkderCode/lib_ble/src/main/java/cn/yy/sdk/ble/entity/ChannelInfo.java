package cn.yy.sdk.ble.entity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/24 14:41
 */
public class ChannelInfo {

    public int channel;
    public int priority;
    public int[] pwd;

    public ChannelInfo(int channel, int priority, int[] pwd) {
        this.channel = channel;
        this.priority = priority;
        this.pwd = pwd;
    }
}
