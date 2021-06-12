package cn.yy.freewalker.config;

import static cn.yy.freewalker.BuildConfig.HOST;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/3 16:51
 */
public class UrlConfig {

    public static final String IMAGE_HOST = "https://blechat.oss-cn-hangzhou.aliyuncs.com";

    //检查版本
    public static final String CHECK_APP_VERSION = HOST + "/appVersion/checkLastVer";
    //获取设备版本
    public static final String GET_DEVICE_VERSION = "http://47.110.149.158/blechat/bleVersion/checkLastVer";
    //获取验证码
    public static final String GET_CAPTCHA = HOST + "/smsCode/sendLoginSms";
    //注册
    public static final String REGISTER = HOST + "/user/register";
    //通过密码登录
    public static final String LOGIN_BY_PWD = HOST + "/user/loginPwd";
    //通过验证码登录
    public static final String LOGIN_BY_SMS_CODE = HOST + "/user/loginSmsCode";
    //通过验证码修改密码
    public static final String MODIFY_PWD_BY_SMS_CODE = HOST + "/user/forgetPwd";
    //通过密码修改手机
    public static final String MODIFY_PHONE_BY_PWD = HOST + "/user/modTel";
    //获取用户信息
    public static final String GET_USER_INFO = HOST + "/userInfo/getInfo";
    public static final String GET_OTHER_USER_INFO = HOST + "/userInfo/getOtherInfo";
    //设置用户信息
    public static final String SET_USER_INFO = HOST + "/userInfo/setInfo";
    //获取用户照片
    public static final String GET_USER_PHOTO = HOST + "/userImg/listByUserId";
    //增加用户照片
    public static final String ADD_USER_PHOTO = HOST + "/userImg/add";
    //删除用户照片
    public static final String DELETE_USER_PHOTO = HOST + "/userImg/delBatch";
    //建议与反馈
    public static final String FEED_BACK = HOST + "/feedBack/add";
    //删除用户
    public static final String DELETE_USER = HOST + "/delUser/add";

}
