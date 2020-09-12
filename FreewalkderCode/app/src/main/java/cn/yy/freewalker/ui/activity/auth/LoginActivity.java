package cn.yy.freewalker.ui.activity.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gyf.immersionbar.ImmersionBar;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.entity.net.LoginResult;
import cn.yy.freewalker.entity.net.UserInfoResult;
import cn.yy.freewalker.network.RequestBuilder;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.main.MainActivity;
import cn.yy.freewalker.ui.activity.main.PrivacyActivity;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/30 23:08
 */
public class LoginActivity extends BaseActivity implements TextWatcher {

    /* contains */
    private static final String TAG = "LoginActivity";

    private static final int MSG_LOGIN_OK = 0x01;
    private static final int MSG_LOGIN_ERROR = 0x02;
    private static final int MSG_GET_USER_INFO_OK = 0x03;
    private static final int MSG_GET_USER_INFO_ERROR = 0x04;


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

    /* data */
    private boolean mIsShowPwd = false;
    private LoginResult mLoginResult;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_login;
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_LOGIN_OK:
                requestGetUserInfo();
                break;
            case MSG_LOGIN_ERROR:
                new ToastView(LoginActivity.this, (String) msg.obj, -1);
                break;
            case MSG_GET_USER_INFO_OK:
                UserDbEntity user = DBDataUser.getLoginUser(LoginActivity.this);
                if (user.name == null || user.name.isEmpty()) {
                    startActivity(ImproveUserInfoActivity.class);
                } else {
                    startActivity(MainActivity.class);
                    finish();
                }
                break;
            case MSG_GET_USER_INFO_ERROR:
                new ToastView(LoginActivity.this, (String) msg.obj, -1);
                break;
        }
    }


    @OnClick({R.id.btn_forget_pwd, R.id.btn_register, R.id.btn_login, R.id.v_eyes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //点击忘记密码
            case R.id.btn_forget_pwd:
                startActivity(ForgetPwdActivity.class);
                break;
            //点击注册
            case R.id.btn_register:
                startActivity(RegisterActivity.class);
                break;
            //点击登录
            case R.id.btn_login:
                requestLogin();
                break;
            //点击查看密码
            case R.id.v_eyes:
                mIsShowPwd = !mIsShowPwd;
                if (mIsShowPwd) {
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
    }

    /**
     * 输入框内容发生变化
     *
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
                                        Intent intent = new Intent(LoginActivity.this, PrivacyActivity.class);
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
                                        Intent intent = new Intent(LoginActivity.this, PrivacyActivity.class);
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
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }


    /**
     * 请求登录
     */
    private void requestLogin() {
        //检查账号
        String mAccount = etMobile.getText().toString();
        //判断账号是否为空
        if (TextUtils.isEmpty(mAccount)) {
            new ToastView(LoginActivity.this, getString(R.string.auth_error_account_empty), -1);
            return;
        }
        String pwd = etPwd.getText().toString();
        //判断密码是否为空
        if (TextUtils.isEmpty(pwd)) {
            new ToastView(LoginActivity.this, getString(R.string.auth_error_pwd_empty), -1);
            return;
        }

        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.loginByPwd(LoginActivity.this, mAccount, pwd);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        YLog.e(TAG, "onSuccess:" + result.msg + "," + result.data);
                        if (result.result) {
                            mLoginResult = JSON.parseObject(result.data, LoginResult.class);
                            mHandler.sendEmptyMessage(MSG_LOGIN_OK);
                        } else {
                            mHandler.obtainMessage(MSG_LOGIN_ERROR, result.msg).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        YLog.e(TAG, "onError:" + ex.getMessage());
                        mHandler.obtainMessage(MSG_LOGIN_ERROR, ex.getMessage()).sendToTarget();
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


    /**
     * 请求用户信息
     */
    private void requestGetUserInfo() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.getUserInfo(LoginActivity.this, mLoginResult.id, mLoginResult.token);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        YLog.e(TAG, "onSuccess:" + result.msg + "," + result.data);
                        if (result.result) {
                            UserInfoResult userInfo = JSON.parseObject(result.data, UserInfoResult.class);
                            DBDataUser.login(LoginActivity.this, mLoginResult, userInfo, etMobile.getText().toString());
                            mHandler.sendEmptyMessage(MSG_GET_USER_INFO_OK);
                        } else {
                            mHandler.obtainMessage(MSG_GET_USER_INFO_ERROR, result.msg).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        YLog.e(TAG, "onError:" + ex.getMessage());
                        mHandler.obtainMessage(MSG_GET_USER_INFO_ERROR, ex.getMessage()).sendToTarget();
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
