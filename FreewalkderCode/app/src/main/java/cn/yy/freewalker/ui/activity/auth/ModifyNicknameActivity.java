package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
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
 * @date 2020/6/14 11:17
 */
public class ModifyNicknameActivity extends BaseActivity {

    private static final int MSG_SET_USER_INFO_OK = 0x03;
    private static final int MSG_SET_USER_INFO_ERROR = 0x04;

    /* views */
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_tx_size)
    TextView tvTxSize;
    @BindView(R.id.btn_save)
    Button btnSave;

    private UserDbEntity mUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_modify_nickname;
    }


    @OnClick({R.id.btn_back, R.id.btn_close, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_close:
                etName.setText("");
                break;
            case R.id.btn_save:
                requestModifyName();
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_SET_USER_INFO_ERROR:
                new ToastView(ModifyNicknameActivity.this, (String) msg.obj, -1);
                break;
            case MSG_SET_USER_INFO_OK:
                new ToastView(ModifyNicknameActivity.this, getString(R.string.app_toast_modify_ok), -1);
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvTxSize.setText(etName.getText().toString().length() + "/8");
                if (etName.getText().toString().isEmpty()) {
                    btnSave.setEnabled(false);
                } else {
                    btnSave.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = DBDataUser.getLoginUser(ModifyNicknameActivity.this);
        etName.setText(mUser.name);
        tvTxSize.setText(mUser.name.length() + "/8");
    }

    @Override
    protected void causeGC() {

    }


    /**
     * 请求更改姓名
     */
    private void requestModifyName() {
        mUser.name = etName.getText().toString();
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.setUserInfo(ModifyNicknameActivity.this,
                        mUser.userId, mUser.name, mUser.genderIndex, mUser.genderOriIndex, mUser.avatar, mUser.ageIndex,
                        mUser.heightIndex, mUser.weightIndex, mUser.professionIndex);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            DBDataUser.update(mUser);
                            mHandler.sendEmptyMessage(MSG_SET_USER_INFO_OK);
                        } else {
                            mHandler.obtainMessage(MSG_SET_USER_INFO_ERROR, result.msg).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mHandler.obtainMessage(MSG_SET_USER_INFO_ERROR, ex.getMessage()).sendToTarget();
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

}
