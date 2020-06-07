package cn.yy.freewalker.ui.activity.device;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.common.CircleProgress;
import cn.yy.freewalker.ui.widget.common.ToastView;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 23:47
 */
public class DeviceOtaActivity extends BaseActivity {


    /* contains */
    private static final int MSG_OTA_PROGRESS = 0x01;


    @BindView(R.id.ll_need)
    LinearLayout llNeed;
    @BindView(R.id.tv_need_new_version)
    TextView tvNeedNewVersion;
    @BindView(R.id.tv_need_curr_version)
    TextView tvNeedCurrVersion;

    @BindView(R.id.ll_ing)
    LinearLayout llIng;
    @BindView(R.id.pb_ing)
    CircleProgress pbIng;

    @BindView(R.id.ll_newest)
    LinearLayout llNewest;
    @BindView(R.id.tv_newest_curr_version)
    TextView tvNewestCurrVersion;

    /* data */
    private String mOldVersion = "V1.0.0";
    private String mNewVersion = "V1.1.0";

    private int mProgress = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_ota;
    }


    @OnClick({R.id.btn_back, R.id.btn_ota})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_ota:
                startOta();
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            //ota 进度
            case MSG_OTA_PROGRESS:
                mProgress += 5;
                pbIng.setValue(mProgress);
                if (mProgress == 100) {
                    new ToastView(DeviceOtaActivity.this, getString(R.string.device_toast_ota_finish), -1);
                    llNeed.setVisibility(View.GONE);
                    llIng.setVisibility(View.GONE);
                    llNewest.setVisibility(View.VISIBLE);
                } else {
                    mHandler.sendEmptyMessageDelayed(MSG_OTA_PROGRESS, 1000);
                }
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        tvNeedCurrVersion.setText(String.format(getString(R.string.device_tip_ota_update_tip), mOldVersion));
        tvNeedNewVersion.setText(String.format(getString(R.string.device_tip_ota_has_new_version), mNewVersion));
        tvNewestCurrVersion.setText(String.format(getString(R.string.device_tip_ota_newest), mNewVersion));
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    /**
     * 开始ota
     */
    private void startOta() {
        llNeed.setVisibility(View.GONE);
        llIng.setVisibility(View.VISIBLE);
        llNewest.setVisibility(View.GONE);
        mHandler.sendEmptyMessage(MSG_OTA_PROGRESS);
    }

}
