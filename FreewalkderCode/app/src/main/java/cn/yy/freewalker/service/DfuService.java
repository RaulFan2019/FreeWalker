package cn.yy.freewalker.service;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;

import cn.yy.freewalker.ui.activity.device.NotificationActivity;
import no.nordicsemi.android.dfu.DfuBaseService;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2021/6/12 20:29
 */
public class DfuService extends DfuBaseService {

    @Override
    protected Class<? extends Activity> getNotificationTarget() {
        return NotificationActivity.class;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}