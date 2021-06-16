package cn.yy.freewalker.data;

import android.content.Context;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.config.DeviceConfig;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;


/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/24 11:34
 */
public class DBDataDevice {


    private static final String TAG = "DBDataDevice";

    /**
     * 增加一个新的设备
     *
     * @param userId
     */
    public static void addNewDevice(final int userId, final String deviceAddress,
                                    final String deviceName) {
        YLog.e(TAG, "addNewDevice:" + deviceAddress);
        BindDeviceDbEntity deviceDbEntity = findDeviceByUser(userId, deviceAddress);

        int channel = 0;
        if (BM.getManager().getDeviceSystemInfo() != null) {
            channel = BM.getManager().getDeviceSystemInfo().currChannel;
        }

        if (deviceDbEntity == null) {
            deviceDbEntity = new BindDeviceDbEntity(System.currentTimeMillis(),
                    userId, deviceName, DeviceConfig.Type.BLACK, deviceAddress, channel);
            save(deviceDbEntity);
            //首次连接需要重启设备
            BM.getManager().resetDevice();
        }else {
            deviceDbEntity.lastChannel = channel;
            update(deviceDbEntity);
        }
    }


    /**
     * 找到设备
     *
     * @param userId
     */
    public static BindDeviceDbEntity findDeviceByUser(final int userId, final String deviceAddress) {
        try {
            return LocalApp.getInstance().getDb()
                    .selector(BindDeviceDbEntity.class)
                    .where("userId", "=", userId)
                    .and("deviceMac", "=", deviceAddress).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 保存
     *
     * @param deviceDbEntity 设备对象
     */
    public static void save(final BindDeviceDbEntity deviceDbEntity) {
        try {
            LocalApp.getInstance().getDb().save(deviceDbEntity);
        } catch (DbException e) {
            e.printStackTrace();
            YLog.e(TAG, "save e:" + e.getMessage());
        }
    }


    public static void update(final BindDeviceDbEntity deviceDbEntity){
        try {
            LocalApp.getInstance().getDb().update(deviceDbEntity);
        } catch (DbException e) {
            e.printStackTrace();
            YLog.e(TAG, "save e:" + e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param deviceDbEntity 设备对象
     */
    public static void delete(final BindDeviceDbEntity deviceDbEntity) {
        try {
            LocalApp.getInstance().getDb().delete(deviceDbEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取所有设备列表
     *
     * @param userId
     */
    public static List<BindDeviceDbEntity> getAllDeviceByUser(final int userId) {
        List<BindDeviceDbEntity> listResult = new ArrayList<>();

        try {
            List<BindDeviceDbEntity> listTmp = LocalApp.getInstance().getDb()
                    .selector(BindDeviceDbEntity.class).where("userId", "=", userId)
                    .findAll();
            if (listTmp != null) {
                listResult.addAll(listTmp);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return listResult;
    }
}
