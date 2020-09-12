package cn.yy.freewalker.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.yy.freewalker.R;
import cn.yy.freewalker.data.SpAppData;
import cn.yy.freewalker.ui.activity.main.PrivacyActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/30 11:19
 */
public class DialogPrivacy {

    public Dialog mDialog;

    TextView tvMsg;
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
    public DialogPrivacy(final Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_privacy);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        btnConfirm =  mDialog.findViewById(R.id.btn_confirm);
        btnCancel = mDialog.findViewById(R.id.btn_cancel);
        tvMsg = mDialog.findViewById(R.id.tv_msg);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onConfirmBtnClick();
                }
                SpAppData.setPrivacy(context, true);
                mDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCancelBtnClick();
                }
                mDialog.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });

        String s1 = context.getString(R.string.privacy_keyword_user_agreement);
        String s2 = context.getString(R.string.privacy_keyword_privacy);

        String tip = String.format(context.getString(R.string.privacy_dialog_msg), s1, s2);
        final int index1 = tip.indexOf(s1);
        int index2 = tip.indexOf(s2);
        SpannableString spannableString = new SpannableString(tip);
        spannableString.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        Intent intent = new Intent(context, PrivacyActivity.class);
                                        intent.putExtra(PrivacyActivity.PRIVACY_TYPE,PrivacyActivity.PRIVACY_TYPE_USER_AGREEMENT);
                                        context.startActivity(intent);
                                    }
                                },
                index1, index1 + s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF414F")),
                index1, index1 + s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(),
                index1, index1 + s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        Intent intent = new Intent(context, PrivacyActivity.class);
                                        intent.putExtra(PrivacyActivity.PRIVACY_TYPE,PrivacyActivity.PRIVACY_TYPE_PRIVACY);
                                        context.startActivity(intent);
                                    }
                                },
                index2, index2 + s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF414F")),
                index2, index2 + s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(),
                index2, index2 + s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvMsg.setText(spannableString);
        tvMsg.setMovementMethod(LinkMovementMethod.getInstance());

    }

    /**
     * 显示
     */
    public void show() {
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        mDialog.show();
    }

    /**
     * 设置监听器
     *
     * @param
     */
    public void setListener(onBtnClickListener listener) {
        mListener = listener;
    }

}
