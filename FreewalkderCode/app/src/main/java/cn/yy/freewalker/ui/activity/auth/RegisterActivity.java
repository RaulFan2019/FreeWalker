package cn.yy.freewalker.ui.activity.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.main.PrivacyActivity;
import cn.yy.freewalker.ui.widget.common.ToastView;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/31 10:51
 */
public class RegisterActivity extends BaseActivity implements TextWatcher {

    /* contains */
    private static final int MSG_GET_CAPTCHA_OK = 0x01;               //请求验证码成功
    private static final int MSG_GET_CAPTCHA_ERROR = 0x02;            //请求验证码失败
    private static final int MSG_CAPTCHA_COUNT_DOWN = 0x03;           //验证码倒计时


    /* views */
    @BindView(R.id.et_mobile)
    EditText etMobile;                                           //手机号输入
    @BindView(R.id.et_verification)
    EditText etVerification;                                     //验证码输入
    @BindView(R.id.tv_send_verification)
    TextView tvSendVerification;                                 //发送验证码文本
    @BindView(R.id.et_pwd)
    EditText etPwd;                                              //密码输入
    @BindView(R.id.v_eyes)
    View vEyes;                                                  //密码眼睛
    @BindView(R.id.btn_register)
    Button btnRegister;                                          //注册按钮
    @BindView(R.id.v_enter)
    View vEnter;                                                 //注册按钮上的图标
    @BindView(R.id.tv_tip)
    TextView tvTip;                                              //提示文字


    /* data */
    private boolean mIsShowPwd = false;

    private String mAccount;                                      //账号
    private int mCaptchaCount;                                    //倒数计时


    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_register;
    }


    @OnClick({R.id.tv_send_verification, R.id.v_eyes, R.id.btn_register, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //发送验证码
            case R.id.tv_send_verification:
                requestSendVerification();
                break;
            //密码眼睛
            case R.id.v_eyes:
                mIsShowPwd = !mIsShowPwd;
                if (mIsShowPwd) {
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            //点击注册
            case R.id.btn_register:
                requestRegister();
                break;
            //返回登录
            case R.id.btn_login:
                startActivity(LoginActivity.class);
                finish();
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etMobile.getText().toString().isEmpty()
                || etPwd.getText().toString().isEmpty()
                || etVerification.getText().toString().isEmpty()) {
            btnRegister.setEnabled(false);
            vEnter.setBackgroundResource(R.drawable.icon_enter_disable);
        } else {
            btnRegister.setEnabled(true);
            vEnter.setBackgroundResource(R.drawable.icon_enter_normal);
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .keyboardEnable(true) // 解决软键盘与底部输入框冲突问题
                .init();
        //协议说明文本
        String s1 = getString(R.string.privacy_keyword_user_agreement);
        String s2 = getString(R.string.privacy_keyword_privacy);

        String tip = String.format(getString(R.string.auth_tip_privacy), s1, s2);
        final int index1 = tip.indexOf(s1);
        int index2 = tip.indexOf(s2);
        SpannableString spannableString = new SpannableString(tip);
        spannableString.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        Intent intent = new Intent(RegisterActivity.this, PrivacyActivity.class);
                                        intent.putExtra(PrivacyActivity.PRIVACY_TYPE, PrivacyActivity.PRIVACY_TYPE_USER_AGREEMENT);
                                        startActivity(intent);
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
                                        Intent intent = new Intent(RegisterActivity.this, PrivacyActivity.class);
                                        intent.putExtra(PrivacyActivity.PRIVACY_TYPE, PrivacyActivity.PRIVACY_TYPE_PRIVACY);
                                        startActivity(intent);
                                    }
                                },
                index2, index2 + s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF414F")),
                index2, index2 + s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(),
                index2, index2 + s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTip.setText(spannableString);
        tvTip.setMovementMethod(LinkMovementMethod.getInstance());
        //输入框监听
        etMobile.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        etVerification.addTextChangedListener(this);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 请求获取验证码
     */
    private void requestSendVerification() {
        //检查账号
        mAccount = etMobile.getText().toString();
        //判断账号是否为空
        if (TextUtils.isEmpty(mAccount)) {
            new ToastView(RegisterActivity.this, getString(R.string.auth_error_account_empty), -1);
            return;
        }
        //TODO
        tvSendVerification.setClickable(false);
        mCaptchaCount = 60;
        updateCaptchaView();
        mHandler.sendEmptyMessageDelayed(MSG_CAPTCHA_COUNT_DOWN, 1000);
    }


    /**
     * 请求注册
     */
    private void requestRegister() {
        //TODO

        new ToastView(RegisterActivity.this, getString(R.string.auth_toast_login_ok), R.drawable.icon_finished);
        startActivity(ImproveUserInfoActivity.class);
    }

    /**
     * 更新验证码UI
     */
    private void updateCaptchaView() {
        if (mCaptchaCount > 0) {
            String second = mCaptchaCount + "";
            String text = String.format(getString(R.string.auth_action_send_verification_again), second);
            tvSendVerification.setText(text);
            mHandler.sendEmptyMessageDelayed(MSG_CAPTCHA_COUNT_DOWN, 1000);
        } else {
            tvSendVerification.setText(getString(R.string.auth_action_send_verification));
            tvSendVerification.setClickable(true);
        }
    }

}
