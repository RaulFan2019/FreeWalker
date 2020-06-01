package cn.yy.freewalker.data.db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/30 14:50
 */
public class SpAppData {

    private static final String PREFERENCE_FILE = "cn.yy.freewalker.Pref";


    private static final String PRIVACY = "privacy";                            //用户隐私

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(final Context context) {
        return context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
    }

    /**
     * 设置用户隐私
     *
     * @param context 上下文
     */
    public static void setPrivacy(final Context context, final boolean privacy) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PRIVACY, privacy);
        editor.commit();
    }

    /**
     * 获取用户隐私
     *
     * @param context 上下文
     * @return 返回用户是否同意隐私
     */
    public static boolean getPrivacy(final Context context) {
        return getSharedPreferences(context).getBoolean(PRIVACY, false);
    }
}
