package cn.yy.freewalker.ui.activity.device;

import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.adapter.DeviceSettingChannelRvAdapter;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogDeviceInputChannelPwd;
import cn.yy.freewalker.ui.widget.dialog.DialogPickView;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/8 20:39
 */
public class DeviceSettingChannelActivity extends BaseActivity {

    /* contains */
    private static final String TAG = "DeviceSettingChannelActivity";

    /* views */
    @BindView(R.id.tv_channel)
    TextView tvChannel;
    @BindView(R.id.rv_channel)
    RecyclerView rvChannel;


    DeviceSettingChannelRvAdapter adapter;
    DialogBuilder mDialogBuilder;

    /* data */
    private ArrayList<String> listAuthSelect = new ArrayList<>();           //权限选择列表

    private int mChannel = 19;
    private int mAuth = 4;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_setting_channel;
    }


    @OnClick({R.id.btn_set_pwd, R.id.btn_set_auth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set_pwd:
                onPwdInputClick();
                break;
            case R.id.btn_set_auth:
                onAuthSelectClick();
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        mDialogBuilder = new DialogBuilder();
        for (int i = 0; i < 10; i++) {
            listAuthSelect.add(i + "");
        }
    }

    @Override
    protected void initViews() {
        tvChannel.setText(mChannel + "");

        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);

        rvChannel.setLayoutManager(layoutManager);

        adapter = new DeviceSettingChannelRvAdapter(DeviceSettingChannelActivity.this,
                mChannel,
                new DeviceSettingChannelRvAdapter.onChannelClick() {
                    @Override
                    public void onClick(int channel) {
                        mChannel = channel;
                        tvChannel.setText(mChannel + "");
                    }
                });

        rvChannel.setAdapter(adapter);

    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }


    /**
     * 密码输入
     */
    private void onPwdInputClick() {
        mDialogBuilder.showDeviceChannelPwdDialog(DeviceSettingChannelActivity.this);
        mDialogBuilder.setDeviceChannelPwdDialogListener(new DialogDeviceInputChannelPwd.onConfirmListener() {
            @Override
            public void onConfirm(String pwd) {
                YLog.e(TAG, "pwd:" + pwd);
            }
        });
    }

    /**
     * 选择年龄
     */
    private void onAuthSelectClick() {
        mDialogBuilder.showPickViewDialog(DeviceSettingChannelActivity.this,
                getString(R.string.device_action_setting_channel_auth), listAuthSelect, mAuth);
        mDialogBuilder.setPickViewDialogListener(new DialogPickView.onConfirmListener() {
            @Override
            public void onConfirm(int index) {
                mAuth = index;
            }
        });
    }

}
