package cn.yy.freewalker.ui.widget.dialog;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.fan on 2017/6/18 0018.
 */

public class DialogBuilder {

    private DialogPrivacy dialogPrivacy;
    private DialogSingleSelect dialogSingleSelect;
    private DialogPickView dialogPickView;
    private DialogTagSelect dialogTagSelect;

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
     *
     * @param listener
     */
    public void setPrivacyDialogListener(DialogPrivacy.onBtnClickListener listener) {
        dialogPrivacy.setListener(listener);
    }


    /**
     * 显示单选对话框的结果
     *
     * @param context
     * @param title
     * @param data
     */
    public void showSingleSelectDialog(final Context context, final String title, final List<String> data) {
        if (dialogSingleSelect == null) {
            dialogSingleSelect = new DialogSingleSelect(context);
        }
        dialogSingleSelect.show(title, data);
    }


    /**
     * 设置单选对话框的监听器
     *
     * @param listener
     */
    public void setSingleSelectDialogListener(DialogSingleSelect.onItemClickListener listener) {
        dialogSingleSelect.setListener(listener);
    }


    /**
     * 显示pickview 对话框
     *
     * @param context
     * @param title
     * @param data
     */
    public void showPickViewDialog(final Context context, final String title,
                                   final ArrayList<String> data
            , final int defaultIndex) {
        if (dialogPickView == null) {
            dialogPickView = new DialogPickView(context);
        }
        dialogPickView.show(title, data, defaultIndex);
    }


    public void setPickViewDialogListener(DialogPickView.onConfirmListener listener){
        dialogPickView.setListener(listener);
    }


    /**
     * 显示pickview 对话框
     *
     * @param context
     * @param title
     * @param data
     */
    public void showTagSelectDialog(final Context context, final String title, final ArrayList<String> data) {
        if (dialogTagSelect == null) {
            dialogTagSelect = new DialogTagSelect(context);
        }
        dialogTagSelect.show(title, data);
    }


    public void setTagSelectDialogListener(DialogTagSelect.onConfirmListener listener){
        dialogTagSelect.setListener(listener);
    }

}
