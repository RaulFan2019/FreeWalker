package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/14 11:17
 */
public class ModifyNicknameActivity extends BaseActivity {


    /* views */
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_tx_size)
    TextView tvTxSize;
    @BindView(R.id.btn_save)
    Button btnSave;

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
                break;
            case R.id.btn_save:
                break;
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
        etName.setText("天才樱木花道");
        tvTxSize.setText("5/8");

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvTxSize.setText(etName.getText().toString().length() + "/8");
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
    protected void causeGC() {

    }

}
