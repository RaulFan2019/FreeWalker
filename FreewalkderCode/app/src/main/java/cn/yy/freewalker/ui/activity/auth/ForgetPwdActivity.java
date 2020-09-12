package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

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
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/31 11:15
 */
public class ForgetPwdActivity extends BaseActivity implements TextWatcher {


    private static final int MSG_GET_CAPTCHA_OK = 0x01;               //请求验证码成功
    private static final int MSG_GET_CAPTCHA_ERROR = 0x02;            //请求验证码失败
    private static final int MSG_CAPTCHA_COUNT_DOWN = 0x03;           //验证码倒计时
    private static final int MSG_MODIFY_OK = 0x04;                  //修改成功
    private static final int MSG_MODIFY_ERROR = 0x05;               //修改失败

    /* views */
    @BindView(R.id.et_mobile)
    EditText etMobile;                                          //手机输入框
    @BindView(R.id.et_verification)
    EditText etVerification;                                    //验证码输入框
    @BindView(R.id.tv_send_verification)
    TextView tvSendVerification;                                //发送验证码文本
    @BindView(R.id.et_pwd)
    EditText etPwd;                                             //密码输入框
    @BindView(R.id.v_eyes)
    View vEyes;                                                 //密码眼睛
    @BindView(R.id.btn_confirm)
    Button btnConfirm;                                          //确定按钮

    /* data */
    private boolean mIsShowPwd = false;


    private String mAccount;                                      //账号
    private int mCaptchaCount;                                    //倒数计时

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_forget_pwd;
    }


    @OnClick({R.id.btn_back, R.id.tv_send_verification, R.id.v_eyes, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.btn_back:
                finish();
                break;
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
            //点击确定
            case R.id.btn_confirm:
                requestModifyPwd();
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            //获取验证码成功
            case MSG_GET_CAPTCHA_OK:
                tvSendVerification.setClickable(false);
                mCaptchaCount = 60;
                updateCaptchaView();
                new ToastView(ForgetPwdActivity.this, getString(R.string.auth_toast_verification_request_ok), -1);
                break;
            //获取验证码错误
            case MSG_GET_CAPTCHA_ERROR:
                new ToastView(ForgetPwdActivity.this, (String) msg.obj, -1);
                break;
            //倒数验证码
            case MSG_CAPTCHA_COUNT_DOWN:
                mCaptchaCount--;
                updateCaptchaView();
                break;
            case MSG_MODIFY_OK:
                new ToastView(ForgetPwdActivity.this, getString(R.string.auth_tip_mofify_pwd_ok), -1);
                finish();
                break;
            case MSG_MODIFY_ERROR:
                new ToastView(ForgetPwdActivity.this, (String) msg.obj, -1);
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etMobile.getText().toString().isEmpty()
                || etPwd.getText().toString().isEmpty()
                || etVerification.getText().toString().isEmpty()) {
            btnConfirm.setEnabled(false);
        } else {
            btnConfirm.setEnabled(true);
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
                .statusBarView(R.id.v_status_bar)
                .init();
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
            new ToastView(ForgetPwdActivity.this, getString(R.string.auth_error_account_empty), -1);
            return;
        }
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.getCaptcha(ForgetPwdActivity.this, mAccount);
                mCancelable = x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            mHandler.sendEmptyMessage(MSG_GET_CAPTCHA_OK);
                        } else {
                            mHandler.obtainMessage(MSG_GET_CAPTCHA_ERROR, result.msg).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mHandler.obtainMessage(MSG_GET_CAPTCHA_ERROR, ex.getMessage()).sendToTarget();
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
     * 请求修改密码
     */
    private void requestModifyPwd() {
        mAccount = etMobile.getText().toString();
        String mCaptcha = etVerification.getText().toString();
        String mPwd = etPwd.getText().toString();

        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.modifyPwdByCaptcha(ForgetPwdActivity.this, mAccount, mCaptcha, mPwd);
                mCancelable = x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            mHandler.sendEmptyMessage(MSG_MODIFY_OK);
                        } else {
                            mHandler.obtainMessage(MSG_MODIFY_ERROR, result.msg).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mHandler.obtainMessage(MSG_MODIFY_ERROR, ex.getMessage()).sendToTarget();
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
