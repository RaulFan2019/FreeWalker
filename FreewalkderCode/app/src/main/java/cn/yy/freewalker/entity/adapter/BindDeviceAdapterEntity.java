package cn.yy.freewalker.entity.adapter;

import cn.yy.freewalker.entity.db.BindDeviceDbEntity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 11:09
 */
public class BindDeviceAdapterEntity {


    public BindDeviceDbEntity deviceDbEntity;
    public int status;

    public BindDeviceAdapterEntity() {
    }

    public BindDeviceAdapterEntity(BindDeviceDbEntity deviceDbEntity, int status) {
        this.deviceDbEntity = deviceDbEntity;
        this.status = status;
    }
}
