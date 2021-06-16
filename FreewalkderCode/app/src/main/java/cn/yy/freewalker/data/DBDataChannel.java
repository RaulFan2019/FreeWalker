package cn.yy.freewalker.data;

import org.xutils.ex.DbException;

import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.entity.db.ChannelDbEntity;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/24 18:37
 */
public class DBDataChannel {

    private static final String TAG = "DBDataChannel";


    /**
     * 获取频道信息
     * @param deviceMac
     * @param channel
     * @return
     */
    public static ChannelDbEntity getChannel(final String deviceMac, final int channel) {
        try {
            return LocalApp.getInstance().getDb()
                    .selector(ChannelDbEntity.class)
                    .where("deviceMac", "=", deviceMac).and("channel", "=", channel).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 保存
     *
     * @param channelDbEntity 频道对象
     */
    public static void save(final ChannelDbEntity channelDbEntity) {
        try {
            LocalApp.getInstance().getDb().save(channelDbEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新
     *
     * @param channelDbEntity 设备对象
     */
    public static void update(final ChannelDbEntity channelDbEntity) {
        try {
            LocalApp.getInstance().getDb().update(channelDbEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
