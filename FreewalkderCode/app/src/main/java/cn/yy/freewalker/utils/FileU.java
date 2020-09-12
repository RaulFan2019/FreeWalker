package cn.yy.freewalker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/10 23:45
 */
public class FileU {

    public static void setIntentDataAndType(Context context,
                                            Intent intent,
                                            String type,
                                            File file,
                                            boolean writeAble) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }


    public static Uri getUriForFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getUriForFile24(context, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    private static Uri getUriForFile24(Context context, File file) {
        return androidx.core.content.FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileProvider", file);
    }


}
