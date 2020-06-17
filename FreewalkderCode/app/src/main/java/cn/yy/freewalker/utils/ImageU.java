package cn.yy.freewalker.utils;

import android.widget.ImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/11 23:18
 */
public class ImageU {

    /**
     * 缓冲用户头像图片
     *
     * @param avatar
     * @param imageView
     */
    public static void loadUserImage(final String avatar, final ImageView imageView) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setCrop(true)
                .setLoadingDrawableId(R.drawable.avatar_default)
                .setFailureDrawableId(R.drawable.avatar_default)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setSize(100, 100)
                .build();
        x.image().bind(imageView, avatar, imageOptions);
    }

    /**
     * 缓冲用户头像图片
     *
     * @param avatar
     * @param imageView
     */
    public static void loadPhoto(final String avatar, final ImageView imageView) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setCrop(true)
                .setLoadingDrawableId(R.color.bg_base_gray)
                .setFailureDrawableId(R.color.bg_base_gray)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setSize(500, 500)
                .build();
        x.image().bind(imageView, avatar, imageOptions);
    }
}
