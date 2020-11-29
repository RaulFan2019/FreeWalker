package cn.yy.freewalker;

import android.app.Application;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import cn.yy.freewalker.data.DBDataDevice;
import cn.yy.freewalker.data.SPDataUser;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.array.ConnectStates;
import cn.yy.sdk.ble.observer.ChannelListener;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/28 16:27
 */
public class LocalApp extends Application implements ChannelListener {

    private static final String TAG = "LocalApp";

    public static Context applicationContext;                       //整个APP的上下文
    private static LocalApp instance;                               //Application

    /* local data about db */
    public static final String DB_NAME = "freewalker.db";
    public static final int DB_VERSION = 5;


    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    private EventBus eventBus;


    /**
     * 获取 LocalApplication
     *
     * @return
     */
    public static LocalApp getInstance() {
        if (instance == null) {
            instance = new LocalApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();

        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(false);
        //初始化蓝牙
        BM.getManager().init(this);
        BM.getManager().setDebug(true);

        BM.getManager().registerChannelListener(this);
    }


    @Override
    public void switchChannelOk() {
        if (BM.getManager().getConnectState() >= ConnectStates.WORKED
                && BM.getManager().getDeviceSystemInfo() != null) {
            BindDeviceDbEntity deviceDbEntity = DBDataDevice.findDeviceByUser(SPDataUser.getAccount(this),
                    BM.getManager().getConnectMac());
            deviceDbEntity.lastChannel = BM.getManager().getDeviceSystemInfo().currChannel;
            DBDataDevice.update(deviceDbEntity);
        }
    }

    /**
     * 获取EventBus
     *
     * @return
     */
    public EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = EventBus.builder()
                    .sendNoSubscriberEvent(false)
                    .logNoSubscriberMessages(false)
                    .build();
        }
        return eventBus;
    }

    /**
     * 获取数据库操作库
     *
     * @return
     */
    public DbManager getDb() throws DbException {
        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig()
                    .setDbName(DB_NAME)
                    .setDbVersion(DB_VERSION)
                    .setDbOpenListener(db -> {
                        // 开启WAL
                        db.getDatabase().enableWriteAheadLogging();
                    });
        }
        if (db == null) {
            db = x.getDb(daoConfig);
        }
        return db;
    }

}
