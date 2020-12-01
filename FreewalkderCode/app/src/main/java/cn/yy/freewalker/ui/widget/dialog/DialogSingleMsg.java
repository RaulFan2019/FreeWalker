package cn.yy.freewalker.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 21:41
 */
public class DialogSingleMsg {

    public Dialog mDialog;

    TextView tvTitle;
    TextView tvContent;
    TextView btnConfirm;

    /**
     * 初始化
     *
     * @param context
     */
    public DialogSingleMsg(Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_single_msg);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_title);
        tvContent = mDialog.findViewById(R.id.tv_content);
        btnConfirm = (TextView) mDialog.findViewById(R.id.tv_confirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }

    /**
     * 显示
     *
     * @param title
     * @param confirm
     */
    public void show(final String title, final String content, final String confirm) {
        tvTitle.setText(title);
        tvContent.setText(content);
        btnConfirm.setText(confirm);

        mDialog.show();
    }
}
