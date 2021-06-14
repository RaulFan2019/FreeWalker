package cn.yy.freewalker.ui.activity.device;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.data.DBDataDevice;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.entity.net.CheckDeviceVersion;
import cn.yy.freewalker.network.RequestBuilder;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.observer.ChannelListener;
import cn.yy.sdk.ble.utils.BLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 11:52
 */
public class DeviceSettingsActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, ChannelListener {


    /* contains */
    private static final String TAG = "DeviceSettingsActivity";

    private static final int MSG_CHECK_DEVICE_VERSION_OK = 0x01;
    private static final int MSG_CHECK_DEVICE_VERSION_ERROR = 0x02;

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

    @BindView(R.id.tv_battery)
    TextView tvBattery;

    @BindView(R.id.cb_long_mode)
    CheckBox cbLongMode;

    @BindView(R.id.cb_ppt)
    CheckBox cbPPT;
    @BindView(R.id.rg_ppt)
    RadioGroup rgPPT;
    @BindView(R.id.rb_ppt_high)
    RadioButton rbPPTHigh;
    @BindView(R.id.rb_ppt_normal)
    RadioButton rbPPTNormal;
    @BindView(R.id.rb_ppt_low)
    RadioButton rbPPTLow;

    /* data */
    private UserDbEntity mUser;
    private DialogBuilder mDialogBuilder;
    private CheckDeviceVersion mCheckDeviceVersion;

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
                getDeviceVersion();
                break;
            case R.id.ll_channel:
                startActivity(DeviceSettingChannelActivity.class);
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what){
            case MSG_CHECK_DEVICE_VERSION_OK:
                if (mCheckDeviceVersion.isUpdate){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("checkVersion", mCheckDeviceVersion);
                    startActivity(DeviceOtaActivity.class, bundle);
                }else {
                    new ToastView(DeviceSettingsActivity.this,getString(R.string.device_toast_need_not_update), -1);
                }
                break;
            case MSG_CHECK_DEVICE_VERSION_ERROR:
                new ToastView(DeviceSettingsActivity.this, (String) msg.obj, -1);
                break;
        }
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
        tvDevice.setText("FreeWLK_" + BM.getManager().getConnectMac().substring(5).replace(":",""));

        tvVolume.setText("6");
        sbVolume.setProgress(6);

        sbVolume.setOnSeekBarChangeListener(this);

        cbLongMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte power = 17;
                if (cbLongMode.isChecked()){
                    power = 22;
                }
                BM.getManager().setSignal(power);
                if (cbLongMode.isChecked()) {
                    showPowerEnhanceDialog();
                }
                boolean isOpenPPT = (BM.getManager().getDeviceSystemInfo().pptAutoHold == 1);
                updatePPTModeViews(isOpenPPT,power);
            }
        });
        cbPPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BM.getManager().setPPTModeIsOpen(cbPPT.isChecked());
                updatePPTModeViews(cbPPT.isChecked(),BM.getManager().getDeviceSystemInfo().power);
            }
        });
        rbPPTLow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    BM.getManager().setSignal((byte) 12);
                }
            }
        });
        rbPPTNormal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    BM.getManager().setSignal((byte)17);
                }
            }
        });
        rbPPTHigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    BM.getManager().setSignal((byte)22);
                }
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
        BindDeviceDbEntity dbEntity = DBDataDevice.findDeviceByUser(mUser.userId, BM.getManager().getConnectMac());
        if (dbEntity.deviceName != null) {
            tvName.setText(dbEntity.deviceName);
        } else {
            tvName.setText("");
        }

        if (BM.getManager().getDeviceSystemInfo() != null) {
            tvChannel.setText(String.valueOf(BM.getManager().getDeviceSystemInfo().currChannel + 1));
            //电量
            double battery = (BM.getManager().getDeviceSystemInfo().voltage - 3253)/9.47;
            tvBattery.setText((int)battery + "%");
            //PPT模式
            boolean isOpen = (BM.getManager().getDeviceSystemInfo().pptAutoHold == 1);
            updatePPTModeViews(isOpen,
                    BM.getManager().getDeviceSystemInfo().power);
        } else {
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


    /**
     * 更新PPT模式
     */
    private void updatePPTModeViews(boolean isOpenPPT, int txPower){
        YLog.e(TAG,"updatePPTModeViews isOpenPPT:" + isOpenPPT + ",txPower:" + txPower);
        cbPPT.setChecked(isOpenPPT);
        cbLongMode.setEnabled(!isOpenPPT);

        if (isOpenPPT){
            rgPPT.setVisibility(View.VISIBLE);
            if (txPower >= 22){
                rbPPTHigh.setChecked(true);
            }else if (txPower >= 17){
                rbPPTNormal.setChecked(true);
            }else {
                rbPPTLow.setChecked(true);
            }
        }else {
            rgPPT.setVisibility(View.GONE);
        }

        //远距离模式
        if (isOpenPPT){
            cbLongMode.setChecked(false);
        }else {
            if (txPower == 22){
                cbLongMode.setChecked(true);
            }else {
                cbLongMode.setChecked(false);
            }
        }
    }

    private void getDeviceVersion(){
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.checkDeviceVersion(DeviceSettingsActivity.this,
                        BM.getManager().getFwVersion()-1);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200){
                            YLog.e(TAG, "success:" + result.data);
                            mCheckDeviceVersion = JSON.parseObject(result.data, CheckDeviceVersion.class);
                            mHandler.sendEmptyMessage(MSG_CHECK_DEVICE_VERSION_OK);
                        }else {
                            mHandler.obtainMessage(MSG_CHECK_DEVICE_VERSION_ERROR,result.msg).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        YLog.e(TAG, "result.msg onError:" + ex.getMessage());
                        mHandler.obtainMessage(MSG_CHECK_DEVICE_VERSION_ERROR,ex.getMessage()).sendToTarget();
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
