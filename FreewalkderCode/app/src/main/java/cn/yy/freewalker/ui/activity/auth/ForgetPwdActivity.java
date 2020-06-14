package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/31 11:15
 */
public class ForgetPwdActivity extends BaseActivity implements TextWatcher {



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
                //TODO
                break;
            //密码眼睛
            case R.id.v_eyes:
                mIsShowPwd = !mIsShowPwd;
                if (mIsShowPwd){
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                }else {
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            //点击确定
            case R.id.btn_confirm:
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

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
}
