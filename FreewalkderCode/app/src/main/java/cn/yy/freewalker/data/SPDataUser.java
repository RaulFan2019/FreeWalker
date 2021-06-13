package cn.yy.freewalker.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Raul.fan on 2018/2/8 0008.
 * Mail:raul.fan@139.com
 * QQ: 35686324
 */

public class SPDataUser {

    private static final String ACCOUNT = "account";
    private static final String IS_SHARE_LOC = "isShareLoc";

    public static final int DEFAULT_ACCOUNT = -1;



    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    /**
     * 设置登录用户ID
     * @param context
     * @param account
     */
    public static void setAccount(final Context context, final int account){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(ACCOUNT, account);
        editor.commit();
    }

    /**
     * 获取登录用户
     * @param context
     * @return
     */
    public static int getAccount(final Context context){
        return getSharedPreferences(context).getInt(ACCOUNT, DEFAULT_ACCOUNT);
    }

    /**
     * 设置是否公开位置
     * @param context
     * @param isShareLoc
     */
    public static void setIsShareLoc(final Context context, final boolean isShareLoc){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_SHARE_LOC, isShareLoc);
        editor.commit();
    }

    /**
     * 获取是否公开位置
     * @param context
     * @return
     */
    public static boolean getIsShare(final Context context){
        return getSharedPreferences(context).getBoolean(IS_SHARE_LOC, true);
    }

}
