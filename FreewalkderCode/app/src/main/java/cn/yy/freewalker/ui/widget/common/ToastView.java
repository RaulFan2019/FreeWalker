package cn.yy.freewalker.ui.widget.common;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/31 11:50
 */
public class ToastView {


    Toast toast;                                            //Toast 本体

    /**
     * @param context
     * @param text
     */
    public ToastView(Context context, String text, int imageRes) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_toast_view, null);

        View iv = view.findViewById(R.id.view);
        TextView tv = (TextView) view.findViewById(R.id.tv);

        if (imageRes == -1) {
            iv.setVisibility(View.GONE);
        } else {
            iv.setVisibility(View.VISIBLE);
            iv.setBackgroundResource(imageRes);
        }

        tv.setText(text);

        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);

        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.show();
    }
}
