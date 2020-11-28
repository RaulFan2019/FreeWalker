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
import cn.yy.freewalker.data.DBDataChannel;
import cn.yy.freewalker.data.DBDataDevice;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.entity.db.ChannelDbEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.adapter.DeviceSettingChannelRvAdapter;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogDeviceInputChannelPwd;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.observer.ChannelListener;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/8 20:39
 */
public class DeviceSettingChannelActivity extends BaseActivity implements ChannelListener {

    /* contains */
    private static final String TAG = "DeviceSettingChannelActivity";

    /* views */
    @BindView(R.id.tv_channel)
    TextView tvChannel;
    @BindView(R.id.rv_channel)
    RecyclerView rvChannel;


    /* data */
    ChannelDbEntity mChannel;
    int mChanelIndex = 0;

    DeviceSettingChannelRvAdapter adapter;
    DialogBuilder mDialogBuilder;

    private ArrayList<String> listAuthSelect = new ArrayList<>();           //权限选择列表
    private UserDbEntity mUser;

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
    public void switchChannelOk() {
        YLog.e(TAG,"switchChannelOk");
        updateChannelData(BM.getManager().getDeviceSystemInfo().currChannel + 1);
        mChanelIndex = BM.getManager().getDeviceSystemInfo().currChannel + 1;

        initAdapter();
        tvChannel.setText(mChannel.channel + "");
        BindDeviceDbEntity dbEntity = DBDataDevice.findDeviceByUser(mUser.userId, BM.getManager().getConnectMac());
        dbEntity.lastChannel = mChannel.channel - 1;
        DBDataDevice.update(dbEntity);
    }

    @Override
    protected void initData() {
        mDialogBuilder = new DialogBuilder();
        for (int i = 0; i < 10; i++) {
            listAuthSelect.add(i + "");
        }
        mUser = DBDataUser.getLoginUser(DeviceSettingChannelActivity.this);

        if (BM.getManager().getDeviceSystemInfo() != null) {
            mChanelIndex = BM.getManager().getDeviceSystemInfo().currChannel + 1;
        } else {
            BindDeviceDbEntity dbEntity = DBDataDevice.findDeviceByUser(mUser.userId, BM.getManager().getConnectMac());
            mChanelIndex = dbEntity.lastChannel + 1;
        }
    }

    @Override
    protected void initViews() {
        tvChannel.setText(String.valueOf(mChanelIndex));

        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);

        rvChannel.setLayoutManager(layoutManager);

        initAdapter();



    }


    /**
     * 初始化adapter
     */
    private void initAdapter(){
        adapter = new DeviceSettingChannelRvAdapter(DeviceSettingChannelActivity.this,
                mChanelIndex,
                channel -> {
                    mChanelIndex = channel;
                    updateChannelData(mChanelIndex);
                    BM.getManager().setChannel(mChannel.channel - 1, mChannel.priority, mChannel.pwd);
                });
        rvChannel.setAdapter(adapter);
    }


    @Override
    protected void doMyCreate() {
        BM.getManager().registerChannelListener(this);
    }

    @Override
    protected void causeGC() {
        BM.getManager().unRegisterChannelListener(this);
    }


    /**
     * 密码输入
     */
    private void onPwdInputClick() {
        if (mChanelIndex >= 10) {
            mDialogBuilder.showDeviceChannelPwdDialog(DeviceSettingChannelActivity.this);
            mDialogBuilder.setDeviceChannelPwdDialogListener(new DialogDeviceInputChannelPwd.onConfirmListener() {
                @Override
                public void onConfirm(String pwd) {
                    String pwdStr = "";
                    for (int i = 0; i < pwd.length(); i++) {
                        if (i == 0) {
                            pwdStr += pwd.charAt(i);
                        } else {
                            pwdStr += "," + pwd.charAt(i);
                        }
                    }
                    mChannel.pwd = pwdStr;
                    DBDataChannel.update(mChannel);
                    BM.getManager().setChannel(mChannel.channel - 1, mChannel.priority, mChannel.pwd);
                }
            });
        } else {
            new ToastView(DeviceSettingChannelActivity.this, getString(R.string.device_tip_channel_can_not_set_pwd_and_priority), -1);
        }

    }

    /**
     * 选择年龄
     */
    private void onAuthSelectClick() {
        if (mChanelIndex >= 10) {
            mDialogBuilder.showPickViewDialog(DeviceSettingChannelActivity.this,
                    getString(R.string.device_action_setting_channel_auth), listAuthSelect, mChannel.priority);
            mDialogBuilder.setPickViewDialogListener(index -> {
                mChannel.priority = index;
                DBDataChannel.update(mChannel);
                BM.getManager().setChannel(mChannel.channel - 1, mChannel.priority, mChannel.pwd);
            });
        } else {
            new ToastView(DeviceSettingChannelActivity.this, getString(R.string.device_tip_channel_can_not_set_pwd_and_priority), -1);
        }
    }


    /**
     * 更新频道信息
     */
    private void updateChannelData(final int channel){
        mChannel = DBDataChannel.getChannel(mUser.userId, channel);
        if (mChannel == null) {
            mChannel = new ChannelDbEntity(System.currentTimeMillis(), mUser.userId, channel, "", 5);
            DBDataChannel.save(mChannel);
        }
    }
}
