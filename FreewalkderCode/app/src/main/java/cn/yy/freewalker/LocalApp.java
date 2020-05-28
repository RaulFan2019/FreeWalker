package cn.yy.freewalker;

import android.app.Application;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
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
    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    private EventBus eventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();

        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(false);
    }
}
