package cn.yy.freewalker.network;

import android.app.Activity;

import org.xutils.ex.HttpException;

import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.utils.AppU;
import cn.yy.freewalker.utils.YLog;


/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/4 17:34
 */
public class NetworkExceptionHelper {

    private static final String TAG = "NetworkExceptionHelper";

    /**
     * 检查网络错误原因
     *
     * @param ex
     * @return
     */
    public static void checkThrowable(final Activity context, final Throwable ex) {
        YLog.e(TAG, "checkThrowable ");
        String error = "";
        try {
            HttpException httpEx = (HttpException) ex;
            int errorCode = httpEx.getCode();
            YLog.e(TAG, "checkThrowable errorCode" + errorCode);

            switch (errorCode) {
                case 404:
                    error = context.getString(R.string.app_error_net_not_found);
                    break;
                case 500:
                    error = context.getString(R.string.app_error_net_server);
                    break;
                default:
                    error = context.getString(R.string.app_error_net_check);
            }
        } catch (ClassCastException exception) {
            error = context.getString(R.string.app_error_net_check);
        }
        new ToastView(context, error, -1);
        YLog.e(TAG, "checkThrowable errorText" + error);
        return;
    }


    /**
     * 获得网络错误信息
     *
     * @param context
     * @param ex
     */
    public static String getThrowableMsg(final Activity context, final Throwable ex) {
        String error = "";
        try {
            HttpException httpEx = (HttpException) ex;
            int errorCode = httpEx.getCode();
            switch (errorCode) {
                case 404:
                    error = context.getString(R.string.app_error_net_not_found);
                    break;
                case 500:
                    error = context.getString(R.string.app_error_net_server);
                    break;
                default:
                    error = context.getString(R.string.app_error_net_check);
            }
        } catch (ClassCastException exception) {
            error = context.getString(R.string.app_error_net_check);
        }
        return error;
    }


    public static void checkErrorByCode(final Activity context, final int errorCode, final String msg) {
//        new ToastView(context, msg, -1);
//        if (errorCode == 1011 || errorCode == 1012) {
//            new ToastView(context, context.getString(R.string.app_error_net_authorization_fail), -1);
//            AppU.jumpToLogin(context);
//        }
    }

    public static String getErrorMsgByCode(final Activity context, final int errorCode, final String msg) {
        if (errorCode == 555){
            AppU.jumpToLogin(context);
        }
        return msg;

    }
}
