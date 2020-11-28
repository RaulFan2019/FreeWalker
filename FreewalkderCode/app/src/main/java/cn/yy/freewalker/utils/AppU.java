package cn.yy.freewalker.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import cn.yy.freewalker.data.SPDataUser;
import cn.yy.freewalker.service.UserDeviceCallbackService;
import cn.yy.freewalker.ui.activity.auth.LoginActivity;
import cn.yy.sdk.ble.BM;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/14 22:09
 */
public class AppU {


    /**
     * 登出
     *
     * @param act
     */
    public static void jumpToLogin(Activity act) {
        SPDataUser.setAccount(act, SPDataUser.DEFAULT_ACCOUNT);

        BM.getManager().disConnectDevice();

        Intent intent = new Intent(act, LoginActivity.class);
        act.startActivity(intent);
        act.finishAffinity();

        Intent serviceI = new Intent(act, UserDeviceCallbackService.class);
        act.stopService(serviceI);
    }


    /**
     * 获取版本
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 3;
    }
}
