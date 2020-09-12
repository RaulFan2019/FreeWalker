package cn.yy.freewalker;

import android.app.Application;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/28 16:27
 */
public class LocalApp extends Application {

    private static final String TAG = "LocalApp";

    public static Context applicationContext;                       //整个APP的上下文
    private static LocalApp instance;                               //Application

    /* local data about db */
    public static final String DB_NAME = "freewalker.db";
    public static final int DB_VERSION = 2;


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
