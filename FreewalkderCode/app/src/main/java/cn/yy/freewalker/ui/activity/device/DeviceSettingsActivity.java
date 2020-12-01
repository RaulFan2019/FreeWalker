package cn.yy.freewalker.ui.activity.device;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.data.DBDataDevice;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.observer.ChannelListener;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 11:52
 */
public class DeviceSettingsActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, ChannelListener {


    /* contains */

    /* views */
    @BindView(R.id.tv_device)
    TextView tvDevice;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_volume)
    TextView tvVolume;
    @BindView(R.id.sb_volume)
    SeekBar sbVolume;
    @BindView(R.id.tv_channel)
    TextView tvChannel;
    @BindView(R.id.cb_power)
    CheckBox cbPower;

    /* data */
    private UserDbEntity mUser;
    private DialogBuilder mDialogBuilder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_settings;
    }


    @Override
    public void switchChannelOk() {
        tvChannel.setText(String.valueOf(BM.getManager().getDeviceSystemInfo().currChannel + 1));
    }

    @OnClick({R.id.btn_back, R.id.ll_device, R.id.ll_name, R.id.ll_update, R.id.ll_channel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_device:
                //TODO
                break;
            case R.id.ll_name:
                startActivity(DeviceSettingsNameActivity.class);
                break;
            case R.id.ll_update:
                startActivity(DeviceOtaActivity.class);
                break;
            case R.id.ll_channel:
                startActivity(DeviceSettingChannelActivity.class);
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvVolume.setText(progress + "");
    }

    @Override
    protected void initData() {
        mUser = DBDataUser.getLoginUser(DeviceSettingsActivity.this);
        mDialogBuilder = new DialogBuilder();
    }

    @Override
    protected void initViews() {
        tvDevice.setText(BM.getManager().getConnectName());
        tvName.setText("Raul的耳机");

        tvVolume.setText("6");
        sbVolume.setProgress(6);

        sbVolume.setOnSeekBarChangeListener(this);

        cbPower.setOnCheckedChangeListener((buttonView, isChecked) -> {
            BM.getManager().setSignal(isChecked);
            if (isChecked) {
                showPowerEnhanceDialog();
            }
        });
    }

    @Override
    protected void doMyCreate() {
        BM.getManager().registerChannelListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BM.getManager().getDeviceSystemInfo() != null) {
            tvChannel.setText(String.valueOf(BM.getManager().getDeviceSystemInfo().currChannel + 1));
            if (BM.getManager().getDeviceSystemInfo().power == 0x16) {
                cbPower.setChecked(true);
            }
        } else {
            BindDeviceDbEntity dbEntity = DBDataDevice.findDeviceByUser(mUser.userId, BM.getManager().getConnectMac());
            tvChannel.setText(String.valueOf(dbEntity.lastChannel + 1));
        }

    }

    @Override
    protected void causeGC() {
        BM.getManager().unRegisterChannelListener(this);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    /**
     * 显示强度Dialog
     */
    private void showPowerEnhanceDialog() {
        mDialogBuilder.showSingleMsgDialog(DeviceSettingsActivity.this,
                getString(R.string.device_tip_setting_power_title),
                getString(R.string.device_tip_setting_power_content),
                getString(R.string.app_action_confirm));
    }

}
