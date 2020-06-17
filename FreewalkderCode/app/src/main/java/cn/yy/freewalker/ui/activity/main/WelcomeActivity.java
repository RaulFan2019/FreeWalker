package cn.yy.freewalker.ui.activity.main;

import android.Manifest;
import android.os.Message;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

import cn.yy.freewalker.R;
import cn.yy.freewalker.data.db.SpAppData;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.auth.LoginActivity;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogPrivacy;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/30 10:53
 */
public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";

    DialogBuilder mDialogBuilder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_welcome;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        mDialogBuilder = new DialogBuilder();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void doMyCreate() {
        // 防止多次启动
        if (!isTaskRoot()) {
            finish();
            return;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //检查是否询问过隐私问题
        if (!SpAppData.getPrivacy(WelcomeActivity.this)) {
            mDialogBuilder.showPrivacyDialog(WelcomeActivity.this);
            mDialogBuilder.setPrivacyDialogListener(new DialogPrivacy.onBtnClickListener() {
                @Override
                public void onConfirmBtnClick() {
                    checkPermissions();
                }

                @Override
                public void onCancelBtnClick() {

                }
            });
        } else {
            checkPermissions();
        }

    }

    @Override
    protected void causeGC() {

    }

    /**
     * 检查权限
     */
    private void checkPermissions(){
        final String[] permissions = new String []{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.REQUEST_INSTALL_PACKAGES,
                Manifest.permission.CAMERA

        };
        //申请权限
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        launch();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        launch();
                    }
                })
                .start();
    }


    /**
     * 启动App
     */
    private void launch(){
        startActivity(LoginActivity.class);
    }

}
