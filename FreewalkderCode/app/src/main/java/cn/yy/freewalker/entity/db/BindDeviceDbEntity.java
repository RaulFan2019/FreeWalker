package cn.yy.freewalker.entity.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 11:06
 */
@Table(name = "bindDevice")
public class BindDeviceDbEntity {

    @Column(name = "deviceId", isId = true, autoGen = false)
    public long deviceId;
    @Column(name = "userId")
    public int userId;                                         //用户Id
    @Column(name = "deviceName")
    public String deviceName;                                   //设备名称
    @Column(name = "deviceType")
    public int deviceType;                                      //设备类型
    @Column(name = "deviceMac")
    public String deviceMac;                                    //设备地址


    public BindDeviceDbEntity() {
    }

    public BindDeviceDbEntity(long deviceId, int userId, String deviceName,
                              int deviceType, String deviceMac) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceMac = deviceMac;
    }
}
