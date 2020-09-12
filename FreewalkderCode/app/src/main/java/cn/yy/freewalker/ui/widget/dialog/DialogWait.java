package cn.yy.freewalker.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import cn.yy.freewalker.R;
import cn.yy.freewalker.utils.DensityU;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/7/28 12:02
 */
public class DialogWait {

    public Dialog mDialog;

    LinearLayout llBase;
    TextView tvContent;


    /**
     * 初始化
     *
     * @param context
     */
    public DialogWait(final Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_wait);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        tvContent = mDialog.findViewById(R.id.tv_content);

    }

    /**
     * 显示
     */
    public void show( final String content) {
        tvContent.setText(content);
        mDialog.show();
    }

    /**
     * 显示
     */
    public void show(final String content, final boolean cancelable) {
        tvContent.setText(content);

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);

        mDialog.show();
    }

}
