package cn.yy.freewalker.utils;

import android.app.Activity;
import android.content.Intent;

import cn.yy.freewalker.data.sp.SPDataUser;
import cn.yy.freewalker.ui.activity.auth.LoginActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/14 22:09
 */
public class AppU {

    public static void jumpToLogin(Activity act) {
        SPDataUser.setAccount(act, SPDataUser.DEFAULT_ACCOUNT);
        Intent intent = new Intent(act, LoginActivity.class);
        act.startActivity(intent);
//
//
        act.finishAffinity();
    }

}
