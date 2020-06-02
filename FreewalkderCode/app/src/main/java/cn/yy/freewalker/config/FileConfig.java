package cn.yy.freewalker.config;

import android.net.Uri;
import android.os.Environment;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/2 23:49
 */
public class FileConfig {

    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory().getPath() + "/FreeWalker/";

    public static final String PHOTO_PATH = DEFAULT_PATH + "photo/";
    public final static String cutFileUri = "file://" + "/" + PHOTO_PATH;
}
