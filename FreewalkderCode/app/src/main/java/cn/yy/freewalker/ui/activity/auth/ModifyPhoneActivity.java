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

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.network.RequestBuilder;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.common.ToastView;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/14 11:56
 */
public class ModifyPhoneActivity extends BaseActivity implements TextWatcher {


    private static final int MSG_GET_CAPTCHA_OK = 0x01;               //请求验证码成功
    private static final int MSG_GET_CAPTCHA_ERROR = 0x02;            //请求验证码失败
    private static final int MSG_CAPTCHA_COUNT_DOWN = 0x03;           //验证码倒计时
    private static final int MSG_MODIFY_OK = 0x04;                  //修改成功
    private static final int MSG_MODIFY_ERROR = 0x05;               //修改失败

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verification)
    EditText etVerification;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @BindView(R.id.tv_send_verification)
    TextView tvSendVerification;                                //发送验证码文本
    @BindView(R.id.v_eyes)
    View vEyes;                                                 //密码眼睛

    /* data */
    private boolean mIsShowPwd = false;
    private int mCaptchaCount;                                    //倒数计时

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_modify_phone;
    }


    @OnClick({R.id.btn_back, R.id.btn_submit, R.id.tv_send_verification, R.id.v_eyes,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            case R.id.btn_submit:
                requestModifyPhone();
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
                new ToastView(ModifyPhoneActivity.this, getString(R.string.auth_toast_verification_request_ok), -1);
                break;
            //获取验证码错误
            case MSG_GET_CAPTCHA_ERROR:
                new ToastView(ModifyPhoneActivity.this, (String) msg.obj, -1);
                break;
            //倒数验证码
            case MSG_CAPTCHA_COUNT_DOWN:
                mCaptchaCount--;
                updateCaptchaView();
                break;
            case MSG_MODIFY_OK:
                new ToastView(ModifyPhoneActivity.this, getString(R.string.auth_tip_mofify_phone_ok), -1);
                finish();
                break;
            case MSG_MODIFY_ERROR:
                new ToastView(ModifyPhoneActivity.this, (String) msg.obj, -1);
                break;
        }
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etPhone.getText().toString().isEmpty()
                || etPwd.getText().toString().isEmpty()
                || etVerification.getText().toString().isEmpty()) {
            btnSubmit.setEnabled(false);
        } else {
            btnSubmit.setEnabled(true);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        //输入框监听
        etPhone.addTextChangedListener(this);
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
        String mAccount = etPhone.getText().toString();
        //判断账号是否为空
        if (TextUtils.isEmpty(mAccount)) {
            new ToastView(ModifyPhoneActivity.this, getString(R.string.auth_error_account_empty), -1);
            return;
        }
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.getCaptcha(ModifyPhoneActivity.this, mAccount);
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
    private void requestModifyPhone() {
        String phone = etPhone.getText().toString();
        String mCaptcha = etVerification.getText().toString();
        String mPwd = etPwd.getText().toString();

        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.modifyPhoneByPwd(ModifyPhoneActivity.this, phone, mCaptcha, mPwd);
                mCancelable = x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            UserDbEntity user = DBDataUser.getLoginUser(ModifyPhoneActivity.this);
                            user.phone = phone;
                            DBDataUser.update(user);
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
