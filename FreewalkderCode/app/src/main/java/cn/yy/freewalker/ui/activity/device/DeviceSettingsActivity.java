package cn.yy.freewalker.ui.activity.device;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
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
import cn.yy.sdk.ble.BM;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 11:52
 */
public class DeviceSettingsActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {


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


    /* data */
    private UserDbEntity mUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_settings;
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
    }

    @Override
    protected void initViews() {
        tvDevice.setText(BM.getManager().getConnectName());
        tvName.setText("Raul的耳机");

        if (BM.getManager().getDeviceSystemInfo() != null) {
            tvChannel.setText(String.valueOf(BM.getManager().getDeviceSystemInfo().currChannel + 1));
        } else {
            BindDeviceDbEntity dbEntity = DBDataDevice.findDeviceByUser(mUser.userId, BM.getManager().getConnectMac());
            tvChannel.setText(String.valueOf(dbEntity.lastChannel + 1));
        }

        tvVolume.setText("6");
        sbVolume.setProgress(6);

        sbVolume.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
