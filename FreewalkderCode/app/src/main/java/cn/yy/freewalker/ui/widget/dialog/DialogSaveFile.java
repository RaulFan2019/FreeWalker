package cn.yy.freewalker.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 21:41
 */
public class DialogSaveFile {

    public Dialog mDialog;

    TextView btnConfirm;
    TextView btnCancel;
    EditText et;

    onBtnClickListener mListener;


    public interface onBtnClickListener {
        void onConfirmBtnClick(String fileName);

        void onCancelBtnClick();
    }

    /**
     * 初始化
     *
     * @param context
     */
    public DialogSaveFile(Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_save_file);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        btnConfirm = (TextView) mDialog.findViewById(R.id.tv_confirm);
        btnCancel = (TextView) mDialog.findViewById(R.id.tv_cancel);
        et = mDialog.findViewById(R.id.et);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onConfirmBtnClick(et.getText().toString());
                    mDialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCancelBtnClick();
                    mDialog.dismiss();
                }
            }
        });
    }

    /**
     * 显示
     */
    public void show(){
        mDialog.show();
    }


    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setListener(onBtnClickListener listener) {
        mListener = listener;
    }


}
