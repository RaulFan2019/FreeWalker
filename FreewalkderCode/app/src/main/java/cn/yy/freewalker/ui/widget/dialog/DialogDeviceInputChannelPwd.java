package cn.yy.freewalker.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.WheelView;

import java.util.ArrayList;

import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.widget.common.InputPwdBox;
import cn.yy.freewalker.ui.widget.common.ToastView;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/8 23:45
 */
public class DialogDeviceInputChannelPwd {

    public Dialog mDialog;

    public TextView tvRemovePass;
    public TextView tvConfirm;
    public InputPwdBox inputPwdBox;

    public onConfirmListener mListener;

    public interface onConfirmListener {
        void onConfirm(String pwd);
        void onRemove();
    }

    /**
     * 初始化
     *
     * @param context
     */
    public DialogDeviceInputChannelPwd(final Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_device_setting_channel_pwd);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        tvRemovePass = mDialog.findViewById(R.id.tv_remove);
        tvConfirm = mDialog.findViewById(R.id.tv_confirm);
        inputPwdBox = mDialog.findViewById(R.id.inputBox);

        tvConfirm.setOnClickListener(v -> {
            if (mListener != null) {
                String input = inputPwdBox.getInputContent();
                if (input == null || input.length() < 6) {
                    new ToastView(context, context.getString(R.string.app_toast_channel_pwd_length_error), -1);
                } else if (input.equals("000000")){
                    new ToastView(context, context.getString(R.string.app_toast_channel_pwd_data_error), -1);
                }else {
                    mListener.onConfirm(inputPwdBox.getInputContent());
                    mDialog.dismiss();
                }
            }
        });

        tvRemovePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onRemove();
                    mDialog.dismiss();
                }
            }
        });
    }

    public void init() {
        inputPwdBox.init();
    }

    /**
     * 显示
     */
    public void show(final String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            inputPwdBox.init();
        } else {
            inputPwdBox.setContent(pwd);
        }
        mDialog.show();
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    public void setListener(onConfirmListener listener) {
        this.mListener = listener;
    }
}
