package cn.yy.freewalker.data;

import android.content.Context;

import org.xutils.ex.DbException;

import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.net.LoginResult;
import cn.yy.freewalker.entity.net.UserInfoResult;


/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/5 14:37
 */
public class DBDataUser {


    /**
     * 注册到本地数据库
     *
     * @param context
     * @param userId
     */
    public static void register(final Context context, final int userId) {
        UserDbEntity userDbEntity = getUserInfoByUserId(userId);
        if (userDbEntity == null) {
            userDbEntity = new UserDbEntity(userId);
            save(userDbEntity);
        }
        SPDataUser.setAccount(context, userDbEntity.userId);
    }

    /**
     * 用户登录
     */
    public static void login(final Context context, final LoginResult loginResult,
                             final UserInfoResult userInfoResult, final String phone) {
        UserDbEntity userDbEntity = getUserInfoByUserId(loginResult.id);

        //首次登录
        if (userDbEntity == null) {
            userDbEntity = new UserDbEntity(loginResult, userInfoResult, phone);
            save(userDbEntity);
        } else {
            userDbEntity.update(loginResult, userInfoResult);
            update(userDbEntity);
        }
        SPDataUser.setAccount(context, userDbEntity.userId);
    }


    /**
     * 获得登录用户
     *
     * @param context
     * @return
     */
    public static UserDbEntity getLoginUser(final Context context) {
        return getUserInfoByUserId(SPDataUser.getAccount(context));
    }


    /**
     * 获取token
     * @param context
     */
//    public static String getToken(final Context context){
//        UserDbEntity userDbEntity = getUserInfoByUserId(SPDataUser.getAccount(context));
//        if (userDbEntity != null){
//            return userDbEntity.token;
//        }else {
//            return "";
//        }
//    }

    /**
     * 获取用户信息
     */
    public static UserDbEntity getUserInfoByUserId(final int userId) {
        try {
            return LocalApp.getInstance().getDb()
                    .selector(UserDbEntity.class)
                    .where("userId", "=", userId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存
     *
     * @param userDbEntity
     */
    public static synchronized void save(final UserDbEntity userDbEntity) {
        try {
            LocalApp.getInstance().getDb().save(userDbEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新
     *
     * @param userDbEntity
     */
    public static synchronized void update(final UserDbEntity userDbEntity) {
        try {
            LocalApp.getInstance().getDb().update(userDbEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
