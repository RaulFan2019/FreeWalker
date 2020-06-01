package cn.yy.freewalker.ui.widget.dialog;

import android.content.Context;

/**
 * Created by Raul.fan on 2017/6/18 0018.
 */

public class DialogBuilder {

    private DialogPrivacy dialogPrivacy;

    /**
     * 显示隐私询问对话框
     *
     * @param context 上下文
     */
    public void showPrivacyDialog(final Context context) {
        if (dialogPrivacy == null) {
            dialogPrivacy = new DialogPrivacy(context);
        }
        dialogPrivacy.show();
    }


    /**
     * 设置隐私询问对话框的监听
     * @param listener
     */
    public void setPrivacyDialogListener(DialogPrivacy.onBtnClickListener listener){
        dialogPrivacy.setListener(listener);
    }

}
