package cn.yy.freewalker.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 21:41
 */
public class DialogChoice {

    public Dialog mDialog;

    TextView tvTitle;
    TextView tvContent;
    TextView btnConfirm;
    TextView btnCancel;

    onBtnClickListener mListener;


    public interface onBtnClickListener {
        void onConfirmBtnClick();

        void onCancelBtnClick();
    }

    /**
     * 初始化
     *
     * @param context
     */
    public DialogChoice(Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_choice);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_title);
        tvContent = mDialog.findViewById(R.id.tv_content);
        btnConfirm = (TextView) mDialog.findViewById(R.id.tv_confirm);
        btnCancel = (TextView) mDialog.findViewById(R.id.tv_cancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onConfirmBtnClick();
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
     * @param title
     * @param confirm
     */
    public void show(final String title , final String confirm , final String cancel){
        tvTitle.setText(title);
        tvContent.setVisibility(View.GONE);
        btnCancel.setText(cancel);
        btnConfirm.setText(confirm);

        mDialog.show();
    }

    /**
     * 显示
     * @param title
     * @param confirm
     */
//    public void show(final String title , final String content, final String confirm , final String cancel){
//        tvTitle.setText(title);
//        tvContent.setVisibility(View.VISIBLE);
//        tvContent.setText(content);
//        btnCancel.setText(cancel);
//        btnConfirm.setText(confirm);
//
//        mDialog.show();
//    }

    /**
     * 显示
     * @param title
     * @param confirm
     */
    public void show(final String title ,final String confirm ,
                     final String cancel, final boolean cancelable){

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);

        tvTitle.setText(title);
        tvContent.setVisibility(View.VISIBLE);
        btnCancel.setText(cancel);

        if (cancelable){
            btnCancel.setVisibility(View.VISIBLE);
        }else {
            btnCancel.setVisibility(View.GONE);
        }
        btnConfirm.setText(confirm);

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
