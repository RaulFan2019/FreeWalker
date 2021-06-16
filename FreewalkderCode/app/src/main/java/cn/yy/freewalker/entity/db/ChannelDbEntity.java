package cn.yy.freewalker.entity.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/24 18:33
 */
@Table(name = "channel")
public class ChannelDbEntity {

    @Column(name = "channelId", isId = true, autoGen = false)
    public long channelId;
    @Column(name = "deviceMac")
    public String deviceMac;                                                    //耳机mac
    @Column(name = "channel")
    public int channel;                                                         //频道号
    @Column(name = "pwd")
    public String pwd;                                                          //密码
    @Column(name = "priority")
    public int priority;                                                        //权限

    public ChannelDbEntity() {
    }

    public ChannelDbEntity(long channelId, String deviceMac, int channel, String pwd, int priority) {
        this.channelId = channelId;
        this.deviceMac = deviceMac;
        this.channel = channel;
        this.pwd = pwd;
        this.priority = priority;
    }
}
