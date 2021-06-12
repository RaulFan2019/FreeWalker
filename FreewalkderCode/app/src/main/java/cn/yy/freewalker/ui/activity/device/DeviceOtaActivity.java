package cn.yy.freewalker.ui.activity.device;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.net.CheckDeviceVersion;
import cn.yy.freewalker.service.DfuService;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.common.CircleProgress;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.utils.FileU;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceController;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 23:47
 */
public class DeviceOtaActivity extends BaseActivity {


    /* contains */
    private static final String TAG = "DeviceOtaActivity";

    private static final int MSG_DOWNLOAD_FILE_OK = 0x00;                      //下载文件成功
    private static final int MSG_DOWNLOAD_FILE_ERROR = 0x01;                   //下载文件失败
    private static final int MSG_DOWNLOAD_FILE_PROGRESS = 0x02;                //下载文件进度更新

    /* views */
    @BindView(R.id.ll_need)
    LinearLayout llNeed;
    @BindView(R.id.tv_need_new_version)
    TextView tvNeedNewVersion;
    @BindView(R.id.tv_need_curr_version)
    TextView tvNeedCurrVersion;

    @BindView(R.id.ll_ing)
    LinearLayout llIng;
    @BindView(R.id.tv_ing_tip)
    TextView tvIngTip;
    @BindView(R.id.pb_ing)
    CircleProgress pbIng;

    @BindView(R.id.ll_newest)
    LinearLayout llNewest;
    @BindView(R.id.tv_newest_curr_version)
    TextView tvNewestCurrVersion;

    /* data */
    private CheckDeviceVersion mCheckDeviceVersion;
    private int mProgress = 0;
    private String mTargetMac;
    private String mTargetName;
    private String mFilePath;

    /* dfu */
    DfuServiceController controller;

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
            //下载文件失败
            case MSG_DOWNLOAD_FILE_ERROR:
                new ToastView(DeviceOtaActivity.this, getString(R.string.device_toast_update_error_by_download_file), -1);
                break;
            //更细下载文件进度
            case MSG_DOWNLOAD_FILE_PROGRESS:
                tvIngTip.setText(R.string.device_tip_ota_downlad_file_ing);
                pbIng.setValue(msg.arg1);
                break;
            //下载文件完成
            case MSG_DOWNLOAD_FILE_OK:
                mFilePath = (String)msg.obj;
                updateNow();
                break;
            //ota 进度
//            case MSG_OTA_PROGRESS:
//                mProgress += 5;
//                pbIng.setValue(mProgress);
//                if (mProgress == 100) {
//                    new ToastView(DeviceOtaActivity.this, getString(R.string.device_toast_ota_finish), -1);
//                    llNeed.setVisibility(View.GONE);
//                    llIng.setVisibility(View.GONE);
//                    llNewest.setVisibility(View.VISIBLE);
//                } else {
//                    mHandler.sendEmptyMessageDelayed(MSG_OTA_PROGRESS, 1000);
//                }
//                break;
        }
    }

    private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter(){

        @Override
        public void onDeviceConnecting(String deviceAddress) {
            super.onDeviceConnecting(deviceAddress);
            YLog.e(TAG,"onDeviceConnecting:" + deviceAddress);
        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            super.onDfuAborted(deviceAddress);
            YLog.e(TAG,"onDfuAborted");
        }

        @Override
        public void onDfuCompleted(String deviceAddress) {
            super.onDfuCompleted(deviceAddress);
            YLog.e(TAG,"onDfuCompleted:" + deviceAddress);
            new ToastView(DeviceOtaActivity.this, getString(R.string.device_toast_ota_finish), -1);
            llNeed.setVisibility(View.GONE);
            llIng.setVisibility(View.GONE);
            llNewest.setVisibility(View.VISIBLE);
            BM.getManager().addNewConnect(mTargetMac);
        }

        @Override
        public void onDfuProcessStarting(String deviceAddress) {
            super.onDfuProcessStarting(deviceAddress);
            YLog.e(TAG,"onDfuProcessStarting:" + deviceAddress);
        }

        @Override
        public void onDeviceConnected(String deviceAddress) {
            super.onDeviceConnected(deviceAddress);
            YLog.e(TAG,"onDeviceConnected:" + deviceAddress);
        }



        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            super.onProgressChanged(deviceAddress, percent, speed, avgSpeed, currentPart, partsTotal);
            YLog.e(TAG,"percent:" + percent);
            tvIngTip.setText(R.string.device_tip_ota_ing);
            pbIng.setValue(percent);
        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            super.onError(deviceAddress, error, errorType, message);
            YLog.e(TAG,"onError:" + message);
        }
    };

    @Override
    protected void initData() {
        mCheckDeviceVersion = (CheckDeviceVersion) getIntent().getSerializableExtra("checkVersion");
        mTargetMac = BM.getManager().getConnectMac();
        mTargetName = BM.getManager().getConnectName();
    }

    @Override
    protected void initViews() {
        String mOldVersion = "V" + BM.getManager().getFwVersion()/256 + "." + BM.getManager().getFwVersion()%256;
        tvNeedCurrVersion.setText(String.format(getString(R.string.device_tip_ota_update_tip), mOldVersion));
        tvNeedNewVersion.setText(String.format(getString(R.string.device_tip_ota_has_new_version), mCheckDeviceVersion.versionName));
        tvNewestCurrVersion.setText(String.format(getString(R.string.device_tip_ota_newest), mCheckDeviceVersion.versionName));
    }

    @Override
    protected void doMyCreate() {
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void causeGC() {
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
        //离开的时候重新连接
        BM.getManager().addNewConnect(mTargetMac);
    }

    /**
     * 开始ota
     */
    private void startOta() {
        llNeed.setVisibility(View.GONE);
        llIng.setVisibility(View.VISIBLE);
        llNewest.setVisibility(View.GONE);

        //需要断开设备
        BM.getManager().disConnectDevice();
        downloadFile();
    }

    /**
     * 下载文件
     */
    private void downloadFile() {
        x.task().post(() -> {
            RequestParams params = new RequestParams(mCheckDeviceVersion.devUrl);
            params.setCancelFast(true);
            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    YLog.e(TAG, "downloadFil path:" + result.getAbsolutePath());
                    Message msg = new Message();
                    msg.what = MSG_DOWNLOAD_FILE_OK;
                    msg.obj = result.getAbsolutePath();
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    YLog.e(TAG, "downloadFile onError:" + ex.getMessage());
                    mHandler.sendEmptyMessage(MSG_DOWNLOAD_FILE_ERROR);
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                }

                @Override
                public void onWaiting() {
                }

                @Override
                public void onStarted() {
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    Message msg = new Message();
                    msg.what = MSG_DOWNLOAD_FILE_PROGRESS;
                    msg.arg1 = (int) (current * 100.0 / total);
                    mHandler.sendMessage(msg);
                }
            });
        });
    }


    /***
     * 开始升级
     */
    private void updateNow() {
        final boolean keepBond = false;
        final DfuServiceInitiator starter = new DfuServiceInitiator(mTargetMac)
                .setDeviceName(mTargetName)
                .setKeepBond(keepBond)
                .setForceDfu(false)
                .setPacketsReceiptNotificationsEnabled(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                .setPrepareDataObjectDelay(400)
                .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
        // but be aware of this: https://devzone.nordicsemi.com/question/100609/sdk-12-bootloader-erased-after-programming/
        // and other issues related to this experimental service.

        // Init packet is required by Bootloader/DFU from SDK 7.0+ if HEX or BIN file is given above.
        // In case of a ZIP file, the init packet (a DAT file) must be included inside the ZIP file.
        final Uri mFileStreamUri = FileU.getUriForFile(DeviceOtaActivity.this, new File(mFilePath));
        starter.setZip(mFileStreamUri, mFilePath);
        controller = starter.start(this, DfuService.class);
        tvIngTip.setText(R.string.device_tip_ota_ing);
        pbIng.setValue(0);
        // You may use the controller to pause, resume or abort the DFU process.
    }

}
