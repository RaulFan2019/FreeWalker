package cn.yy.freewalker.ui.activity.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.TextView;
import android.widget.Toast;

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
 * @date 2020/5/30 23:08
 */
public class LoginActivity extends BaseActivity implements TextWatcher {

    /* contains */
    private static final String TAG = "LoginActivity";


    /* views */
    @BindView(R.id.et_mobile)
    EditText etMobile;                                      //手机号输入框
    @BindView(R.id.et_pwd)
    EditText etPwd;                                         //密码输入框
    @BindView(R.id.v_eyes)
    View vEyes;                                             //密码是否隐藏
    @BindView(R.id.btn_login)
    Button btnLogin;                                        //登录按钮
    @BindView(R.id.v_enter)
    View vEnter;                                            //登录按钮的图标
    @BindView(R.id.tv_tip)
    TextView tvTip;                                         //提示文本

    ToastView viewToast;                                    //Toast

    /* data */
    private boolean mIsShowPwd = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_login;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }


    @OnClick({R.id.btn_forget_pwd, R.id.btn_register, R.id.btn_login,R.id.v_eyes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //点击忘记密码
            case R.id.btn_forget_pwd:
                startActivity(ForgetPwdActivity.class);
                break;
            //点击注册
            case R.id.btn_register:
                startActivity(ImproveUserInfoActivity.class);
                finish();
                break;
            //点击登录
            case R.id.btn_login:
                //TODO
                break;
            //点击查看密码
            case R.id.v_eyes:
                mIsShowPwd = !mIsShowPwd;
                if (mIsShowPwd){
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                }else {
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
    }

    /**
     * 输入框内容发生变化
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etMobile.getText().toString().isEmpty() || etPwd.getText().toString().isEmpty()) {
            btnLogin.setEnabled(false);
            vEnter.setBackgroundResource(R.drawable.icon_enter_disable);
        } else {
            btnLogin.setEnabled(true);
            vEnter.setBackgroundResource(R.drawable.icon_enter_normal);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

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
                                        Intent intent = new Intent(LoginActivity.this, PrivacyActivity.class);
                                        intent.putExtra(PrivacyActivity.PRIVACY_TYPE,PrivacyActivity.PRIVACY_TYPE_USER_AGREEMENT);
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
                                        Intent intent = new Intent(LoginActivity.this, PrivacyActivity.class);
                                        intent.putExtra(PrivacyActivity.PRIVACY_TYPE,PrivacyActivity.PRIVACY_TYPE_PRIVACY);
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
}
