package cn.yy.freewalker.ui.activity.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.network.RequestBuilder;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.auth.LoginActivity;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.utils.AppU;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/22 21:42
 */
public class DeleteUserActivity extends BaseActivity implements TextWatcher {

    private static final int MSG_DELETE_USER_OK = 0x01;
    private static final int MSG_DELETE_USER_ERROR = 0x02;

    @BindView(R.id.et_mail)
    EditText etMail;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_delete_user;
    }

    @OnClick({R.id.btn_back, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_delete:
                requestDeleteUser();
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!etMail.getText().toString().isEmpty()
                && etMail.getText().toString().contains("@")) {
            btnDelete.setEnabled(true);
        } else {
            btnDelete.setEnabled(false);
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what){
            case MSG_DELETE_USER_OK:
                AppU.jumpToLogin(DeleteUserActivity.this);
                new ToastView(DeleteUserActivity.this, getString(R.string.privacy_delete_user_ok), -1);
                break;
            case MSG_DELETE_USER_ERROR:
                new ToastView(DeleteUserActivity.this, (String)msg.obj, -1);
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        etMail.addTextChangedListener(this);
        //说明文本
        String s1 = getString(R.string.privacy_keyword_delete_user_important);
        String tip = String.format(getString(R.string.privacy_delete_user_important), s1);
        final int index1 = tip.indexOf(s1);

        SpannableString spannableString = new SpannableString(tip);
        spannableString.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        startActivity(DeleteUserImportantActivity.class);
                                    }
                                },
                index1, index1 + s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF414F")),
                index1, index1 + s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(),
                index1, index1 + s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTip.setText(spannableString);
        tvTip.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    private void requestDeleteUser(){
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.deleteUser(DeleteUserActivity.this,etMail.getText().toString());
                mCancelable = x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            mHandler.sendEmptyMessage(MSG_DELETE_USER_OK);
                        }else {
                            mHandler.obtainMessage(MSG_DELETE_USER_ERROR, result.msg).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mHandler.obtainMessage(MSG_DELETE_USER_ERROR, ex.getMessage()).sendToTarget();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }



    @Override
    public void afterTextChanged(Editable s) {

    }
}
