package cn.yy.freewalker.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 10:56
 */
public class SystemU {

    /**
     * 获取是否打开了位置服务
     * @param context
     * @return
     */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

}
