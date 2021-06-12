package cn.yy.freewalker.ui.activity.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.main.WelcomeActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2021/6/12 20:31
 */
public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If this activity is the root activity of the task, the app is not running
        if (isTaskRoot()) {
            // Start the app before finishing
//            final Intent intent = new Intent(this, BaseActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtras(getIntent().getExtras()); // copy all extras
//            startActivity(intent);
            final Intent parentIntent = new Intent(this, BaseActivity.class);
            parentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            final Intent startAppIntent = new Intent(this, DeviceOtaActivity.class);
            if (getIntent() != null && getIntent().getExtras() != null)
                startAppIntent.putExtras(getIntent().getExtras());
            startActivities(new Intent[] { parentIntent, startAppIntent });
        }

        // Now finish, which will drop you to the activity at which you were at the top of the task stack
        finish();
    }
}
