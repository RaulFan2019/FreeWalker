package cn.yy.freewalker.network;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONArray;
import org.xutils.http.RequestParams;

import cn.yy.freewalker.config.UrlConfig;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.utils.AppU;
import cn.yy.freewalker.utils.SHA256U;
import cn.yy.freewalker.utils.SystemU;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/4 17:52
 */
public class RequestBuilder {

    private static final String TAG = "RequestBuilder";


    public static RequestParams checkAppVersion(final Context context) {
        RequestParams params = new RequestParams(UrlConfig.CHECK_APP_VERSION);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("versionCode", AppU.getVersionCode(context));
        params.addBodyParameter("appType", 1);
        return params;

    }

    /**
     * 获取验证码
     *
     * @param context
     * @param tel
     * @return
     */
    public static RequestParams getCaptcha(final Context context, final String tel) {
        RequestParams params = new RequestParams(UrlConfig.GET_CAPTCHA);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("tel", tel);
        return params;
    }

    /**
     * 获取验证码
     *
     * @param context
     * @param tel
     * @return
     */
    public static RequestParams register(final Context context, final String tel,
                                         final String smsCode, final String pwd) {
        RequestParams params = new RequestParams(UrlConfig.REGISTER);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("tel", tel);
        params.addBodyParameter("smsCode", smsCode);
        params.addBodyParameter("pwd", SHA256U.getSHA256(pwd));
        return params;
    }


    /**
     * 密码登录
     *
     * @param account
     * @param pwd
     * @return
     */
    public static RequestParams loginByPwd(final Context context, final String account, final String pwd) {
        RequestParams params = new RequestParams(UrlConfig.LOGIN_BY_PWD);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("tel", account);
        params.addBodyParameter("pwd", SHA256U.getSHA256(pwd));

        return params;
    }


    /**
     * 通过验证码
     *
     * @param context
     * @param account
     * @param code
     * @param pwd
     * @return
     */
    public static RequestParams modifyPwdByCaptcha(final Context context, final String account, final String code,
                                                   final String pwd) {

        RequestParams params = new RequestParams(UrlConfig.MODIFY_PWD_BY_SMS_CODE);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("tel", account);
        params.addBodyParameter("smsCode", code);
        params.addBodyParameter("newPwd", SHA256U.getSHA256(pwd));

        return params;
    }


    /**
     * 修改手机号
     *
     * @param context
     * @param account
     * @param code
     * @param pwd
     * @return
     */
    public static RequestParams modifyPhoneByPwd(final Context context, final String account, final String code,
                                                 final String pwd) {

        RequestParams params = new RequestParams(UrlConfig.MODIFY_PHONE_BY_PWD);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("newTel", account);
        params.addBodyParameter("newPwd", SHA256U.getSHA256(pwd));
        params.addBodyParameter("newSmsCode", code);
        params.addBodyParameter("userId", DBDataUser.getLoginUser(context).userId);
        params.addBodyParameter("token", DBDataUser.getLoginUser(context).token);

        return params;

    }

    /**
     * 验证码登录
     *
     * @param account
     * @param smsCode
     * @return
     */
    public static RequestParams loginBySmsCode(final Context context, final String account, final String smsCode) {
        RequestParams params = new RequestParams(UrlConfig.LOGIN_BY_PWD);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("tel", account);
        params.addBodyParameter("smsCode", smsCode);

        return params;
    }


    /**
     * 获取用户信息
     *
     * @param context
     * @param userId
     * @param token
     * @return
     */
    public static RequestParams getUserInfo(final Context context, final int userId, final String token) {
        RequestParams params = new RequestParams(UrlConfig.GET_USER_INFO);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("userId", userId);
        params.addBodyParameter("token", token);

        return params;
    }

    public static RequestParams setUserInfo(final Context context, final int userId,
                                            final String nickName, final int gender, final int genderOri,
                                            final String avatar, final int age, final int height, final int weight,
                                            final int jobIndex) {
        RequestParams params = new RequestParams(UrlConfig.SET_USER_INFO);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("token", DBDataUser.getLoginUser(context).token);

        params.addBodyParameter("userId", userId);
        params.addBodyParameter("nickName", nickName);
        params.addBodyParameter("gender", gender);
        params.addBodyParameter("genderOri", genderOri);
        params.addBodyParameter("avatar", avatar);
        params.addBodyParameter("age", age);
        params.addBodyParameter("bodyLong", height);
        params.addBodyParameter("bodyWeight", weight);
        params.addBodyParameter("job", jobIndex);

        return params;
    }


    /**
     * 获取用户照片
     *
     * @param context
     * @param targetUserId
     * @return
     */
    public static RequestParams getUserPhoto(final Context context, final int targetUserId) {

        RequestParams params = new RequestParams(UrlConfig.GET_USER_PHOTO);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("token", DBDataUser.getLoginUser(context).token);
        params.addBodyParameter("userId", DBDataUser.getLoginUser(context).userId);
        params.addBodyParameter("targetUserId", targetUserId);

        return params;
    }


    /**
     * 增加用户图片
     * @param context
     * @param url
     * @return
     */
    public static RequestParams addUserPhoto(final Context context, final String url) {

        RequestParams params = new RequestParams(UrlConfig.ADD_USER_PHOTO);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("token", DBDataUser.getLoginUser(context).token);
        params.addBodyParameter("userId", DBDataUser.getLoginUser(context).userId);
        params.addBodyParameter("imgUrl", url);
        return params;
    }


    /**
     * 删除用户照片
     * @param context
     * @param url
     * @return
     */
    public static RequestParams deleteUserPhoto(final Context context, final int id){
        RequestParams params = new RequestParams(UrlConfig.DELETE_USER_PHOTO);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");

        params.addBodyParameter("token", DBDataUser.getLoginUser(context).token);
        params.addBodyParameter("userId", DBDataUser.getLoginUser(context).userId);
        params.addBodyParameter("id", id);
        return params;
    }

    /**
     * 建议与反馈
     * @param context
     * @param tel
     * @param devType
     * @param content
     * @return
     */
    public static RequestParams feedback(final Context context, final String tel, final String devType,
                                         final String content){
        RequestParams params = new RequestParams(UrlConfig.FEED_BACK);

        params.addBodyParameter("l", SystemU.isZh(context) ? "zh_CN" : "en_US");
        params.addBodyParameter("devType", devType);
        params.addBodyParameter("content", content);
        params.addBodyParameter("tel", tel);
        return params;
    }
}

