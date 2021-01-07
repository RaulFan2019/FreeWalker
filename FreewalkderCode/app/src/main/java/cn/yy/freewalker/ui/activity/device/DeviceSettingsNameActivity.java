package cn.yy.freewalker.ui.activity.device;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.YearMonth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.data.DBDataDevice;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.sdk.ble.BM;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 23:26
 */
public class DeviceSettingsNameActivity extends BaseActivity implements TextWatcher {


    /* views */
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_tx_size)
    TextView tvTxSize;

    UserDbEntity mUser;
    BindDeviceDbEntity mDevice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_settings_name;
    }


    @OnClick({R.id.btn_back, R.id.v_close, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.btn_back:
                finish();
                break;
            //清空
            case R.id.v_close:
                etName.setText("");
                break;
            //保存
            case R.id.btn_save:
                String name = etName.getText().toString();
                mDevice.deviceName = name;
                DBDataDevice.update(mDevice);

                BM.getManager().setDeviceName(name);
                new ToastView(DeviceSettingsNameActivity.this, getString(R.string.app_toast_modify_ok), -1);
                finish();
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvTxSize.setText(etName.getText().toString().length() + "/8");
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        mUser = DBDataUser.getLoginUser(DeviceSettingsNameActivity.this);
        mDevice = DBDataDevice.findDeviceByUser(mUser.userId, BM.getManager().getConnectMac());

        if (mDevice.deviceName != null){
            etName.setText(mDevice.deviceName);
        }else {
            etName.setText("");
        }
        tvTxSize.setText(etName.getText().toString().length() + "/8");

    }

    @Override
    protected void initViews() {
        etName.addTextChangedListener(this);
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
