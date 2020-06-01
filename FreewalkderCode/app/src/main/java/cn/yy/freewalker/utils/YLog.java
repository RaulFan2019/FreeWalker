package cn.yy.freewalker.utils;

import static cn.yy.freewalker.BuildConfig.TEST;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/2/13 14:26
 */
public class YLog {

    public static void v(String TAG, String msg) {
        if (TEST) {
            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                android.util.Log.v(TAG, msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    android.util.Log.v(TAG, logContent);
                }
                android.util.Log.v(TAG, msg);
            }
        }
    }


    public static void d(String TAG, String msg) {
        if (TEST) {
            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                android.util.Log.d(TAG, msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    android.util.Log.d(TAG, logContent);
                }
                android.util.Log.d(TAG, msg);
            }
        }
    }


    public static void i(String TAG, String msg) {
        if (TEST) {
            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                android.util.Log.i(TAG, msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    android.util.Log.i(TAG, logContent);
                }
                android.util.Log.i(TAG, msg);
            }
        }
    }

    public static void w(String TAG, String msg) {
        if (TEST) {
            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                android.util.Log.w(TAG, msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    android.util.Log.w(TAG, logContent);
                }
                android.util.Log.w(TAG, msg);
            }
        }
    }

    public static void e(String TAG, String msg) {
        android.util.Log.e(TAG, msg);
        if (TEST) {
            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                android.util.Log.e(TAG, msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    android.util.Log.e(TAG, logContent);
                }
                android.util.Log.e(TAG, msg);
            }
        }
    }

}
