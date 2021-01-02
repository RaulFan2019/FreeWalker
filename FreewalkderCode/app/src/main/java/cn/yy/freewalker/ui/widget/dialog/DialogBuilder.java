package cn.yy.freewalker.ui.widget.dialog;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.fan on 2017/6/18 0018.
 */

public class DialogBuilder {


    private DialogWait dialogWait;
    private DialogPrivacy dialogPrivacy;
    private DialogSingleSelect dialogSingleSelect;
    private DialogPickView dialogPickView;
    private DialogTagSelect dialogTagSelect;
    private DialogChoice dialogChoice;
    private DialogDeviceInputChannelPwd dialogDeviceInputChannelPwd;
    private DialogSaveFile dialogSaveFile;
    private DialogSingleMsg dialogSingleMsg;

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
     * 显示单选对话框的结果
     *
     * @param context
     * @param data
     */
    public void showSingleSelectDialog(final Context context, final List<String> data) {
        if (dialogSingleSelect == null) {
            dialogSingleSelect = new DialogSingleSelect(context);
        }
        dialogSingleSelect.show(data);
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


    public void setPickViewDialogListener(DialogPickView.onConfirmListener listener) {
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


    public void setTagSelectDialogListener(DialogTagSelect.onConfirmListener listener) {
        dialogTagSelect.setListener(listener);
    }


    /**
     * 显示简单消息对话框
     * @param context
     * @param title
     * @param content
     * @param confirm
     */
    public void showSingleMsgDialog(final Context context, final String title, final String content,
                                    final String confirm) {
        if (dialogSingleMsg == null) {
            dialogSingleMsg = new DialogSingleMsg(context);
        }
        dialogSingleMsg.show(title, content, confirm);
    }

    /**
     * 显示选择对话框
     *
     * @param context
     * @param title
     * @param confirm
     * @param cancel
     */
    public void showChoiceDialog(final Context context, final String title,
                                 final String confirm, final String cancel) {
        if (dialogChoice == null) {
            dialogChoice = new DialogChoice(context);
        }
        dialogChoice.show(title, confirm, cancel);
    }

    /**
     * 显示选择对话框
     *
     * @param context
     * @param title
     * @param confirm
     * @param cancel
     */
    public void showChoiceDialog(final Context context, final String title,
                                 final String confirm, final String cancel, final boolean cancelable) {
        if (dialogChoice == null) {
            dialogChoice = new DialogChoice(context);
        }
        dialogChoice.show(title, confirm, cancel, cancelable);
    }


    /**
     * 显示选择对话框
     *
     * @param context
     * @param title
     * @param confirm
     * @param cancel
     */
    public void showChoiceDialog(final Context context, final String title, final String content,
                                 final String confirm, final String cancel) {
        if (dialogChoice == null) {
            dialogChoice = new DialogChoice(context);
        }
        dialogChoice.show(title, confirm, cancel);
    }

    public void setChoiceDialogListener(DialogChoice.onBtnClickListener listener) {
        dialogChoice.setListener(listener);
    }

    /**
     * 显示设备频道密码对话框
     *
     * @param context
     */
    public void showDeviceChannelPwdDialog(final Context context) {
        if (dialogDeviceInputChannelPwd == null) {
            dialogDeviceInputChannelPwd = new DialogDeviceInputChannelPwd(context);
        }else {
            dialogDeviceInputChannelPwd.init();
        }

        dialogDeviceInputChannelPwd.show();
    }


    public void setDeviceChannelPwdDialogListener(DialogDeviceInputChannelPwd.onConfirmListener listener) {
        dialogDeviceInputChannelPwd.setListener(listener);
    }


    /**
     * 显示设备频道密码对话框
     *
     * @param context
     */
    public void showSaveFileDialog(final Context context) {
        if (dialogSaveFile == null) {
            dialogSaveFile = new DialogSaveFile(context);
        }
        dialogSaveFile.show();
    }


    public void setSaveFileDialogListener(DialogSaveFile.onBtnClickListener listener) {
        dialogSaveFile.setListener(listener);
    }


    /**
     * 显示同步进度对话框
     *
     * @param context
     * @param content
     */
    public void showWaitDialog(final Context context, final String content) {
        if (dialogWait == null) {
            dialogWait = new DialogWait(context);
        }
        dialogWait.show(content);
    }

    /**
     * 显示同步进度对话框
     *
     * @param context
     * @param content
     */
    public void showWaitDialog(final Context context, final String content, final boolean cancelable) {
        if (dialogWait == null) {
            dialogWait = new DialogWait(context);
        }
        dialogWait.show(content, cancelable);
    }


    /**
     * 同步对话框消失
     */
    public void dismissWaitDialog() {
        if (dialogWait != null) {
            dialogWait.mDialog.dismiss();
        }
    }


}
