package cn.yy.freewalker.ui.activity.device;

import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
import cn.yy.sdk.ble.array.ConnectStates;
import cn.yy.sdk.ble.observer.ChannelListener;
import cn.yy.sdk.ble.observer.ConnectListener;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/8 20:39
 */
public class DeviceSettingChannelActivity extends BaseActivity implements ChannelListener, ConnectListener {

    /* contains */
    private static final String TAG = "DeviceSettingChannelActivity";

    /* views */
    @BindView(R.id.tv_channel)
    TextView tvChannel;
    @BindView(R.id.rv_channel)
    RecyclerView rvChannel;


    /* data */
    List<ChannelDbEntity> listChannel = new ArrayList<>();
    int mChanelIndex = -1;

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
                if (mChanelIndex > 9) {
                    onPwdInputClick();
                }
                break;
            case R.id.btn_set_auth:
                if (mChanelIndex > 9) {
                    onAuthSelectClick();
                }
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }


    @Override
    public void switchChannelOk() {
        YLog.e(TAG, "switchChannelOk");
        mChanelIndex = BM.getManager().getDeviceSystemInfo().currChannel;
        tvChannel.setText(String.valueOf(listChannel.get(mChanelIndex).channel + 1));
        initAdapter();
        //更改用户最后一次频道记录
        BindDeviceDbEntity dbEntity = DBDataDevice.findDeviceByUser(mUser.userId, BM.getManager().getConnectMac());
        dbEntity.lastChannel = mChanelIndex;
        DBDataDevice.update(dbEntity);
    }

    @Override
    protected void initData() {
        mDialogBuilder = new DialogBuilder();
        for (int i = 0; i < 10; i++) {
            listAuthSelect.add(i + "");
        }
        mUser = DBDataUser.getLoginUser(DeviceSettingChannelActivity.this);

        //初始化频道
        for (int i = 0; i < 30; i++) {
            ChannelDbEntity channelDbEntity = DBDataChannel.getChannel(BM.getManager().getConnectMac(), i);
            if (channelDbEntity == null) {
                channelDbEntity = new ChannelDbEntity(System.currentTimeMillis(), BM.getManager().getConnectMac(), i, "", 5);
                DBDataChannel.save(channelDbEntity);
            }
            listChannel.add(channelDbEntity);
        }

        if (BM.getManager().getDeviceSystemInfo() != null) {
            mChanelIndex = BM.getManager().getDeviceSystemInfo().currChannel;
        } else {
            BindDeviceDbEntity dbEntity = DBDataDevice.findDeviceByUser(mUser.userId, BM.getManager().getConnectMac());
            mChanelIndex = dbEntity.lastChannel;
        }
    }

    @Override
    protected void initViews() {
        tvChannel.setText(String.valueOf(mChanelIndex + 1));

        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);

        rvChannel.setLayoutManager(layoutManager);

        initAdapter();
    }


    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new DeviceSettingChannelRvAdapter(DeviceSettingChannelActivity.this,
                mChanelIndex, listChannel,
                channel -> {
                    mChanelIndex = channel;

                    BM.getManager().setChannel(listChannel.get(mChanelIndex).channel,
                            listChannel.get(mChanelIndex).priority,
                            listChannel.get(mChanelIndex).pwd);
                });
        rvChannel.setAdapter(adapter);
    }


    @Override
    protected void doMyCreate() {
        BM.getManager().registerChannelListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BM.getManager().getConnectState() < ConnectStates.WORKED){
            finish();
            new ToastView(DeviceSettingChannelActivity.this, getString(R.string.device_has_disconnect),-1);
        }
        BM.getManager().registerConnectListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BM.getManager().unRegisterConnectListener(this);
    }

    @Override
    protected void causeGC() {
        BM.getManager().unRegisterChannelListener(this);
    }


    /**
     * 密码输入
     */
    private void onPwdInputClick() {
        ChannelDbEntity channelDbEntity = listChannel.get(mChanelIndex);
        if (mChanelIndex >= 10) {
            mDialogBuilder.showDeviceChannelPwdDialog(DeviceSettingChannelActivity.this, channelDbEntity.pwd);
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

                    ChannelDbEntity channelDbEntity = listChannel.get(mChanelIndex);
                    channelDbEntity.pwd = pwdStr;
                    listChannel.set(mChanelIndex, channelDbEntity);
                    DBDataChannel.update(channelDbEntity);
                    adapter.notifyDataSetChanged();
                    BM.getManager().setChannel(listChannel.get(mChanelIndex).channel,
                            listChannel.get(mChanelIndex).priority, listChannel.get(mChanelIndex).pwd);
                }

                @Override
                public void onRemove() {
                    ChannelDbEntity channelDbEntity = listChannel.get(mChanelIndex);
                    channelDbEntity.pwd = "";
                    listChannel.set(mChanelIndex, channelDbEntity);
                    DBDataChannel.update(channelDbEntity);
                    adapter.notifyDataSetChanged();
                    BM.getManager().setChannel(listChannel.get(mChanelIndex).channel,
                            listChannel.get(mChanelIndex).priority, listChannel.get(mChanelIndex).pwd);
                }
            });
        } else {
            new ToastView(DeviceSettingChannelActivity.this, getString(R.string.device_tip_channel_can_not_set_pwd_and_priority), -1);
        }

    }

    /**
     * 选择权限
     */
    private void onAuthSelectClick() {
        if (mChanelIndex >= 10) {
            mDialogBuilder.showPickViewDialog(DeviceSettingChannelActivity.this,
                    getString(R.string.device_action_setting_channel_auth), listAuthSelect, listChannel.get(mChanelIndex).priority);
            mDialogBuilder.setPickViewDialogListener(index -> {
                ChannelDbEntity channelDbEntity = listChannel.get(mChanelIndex);
                channelDbEntity.priority = index;
                listChannel.set(mChanelIndex, channelDbEntity);
                DBDataChannel.update(channelDbEntity);
                BM.getManager().setChannel(listChannel.get(mChanelIndex).channel,
                        listChannel.get(mChanelIndex).priority, listChannel.get(mChanelIndex).pwd);

                adapter.notifyDataSetChanged();
            });
        } else {
            new ToastView(DeviceSettingChannelActivity.this, getString(R.string.device_tip_channel_can_not_set_pwd_and_priority), -1);
        }
    }

    @Override
    public void connectStateChange(int state) {
        if (state < ConnectStates.WORKED){
            finish();
            new ToastView(DeviceSettingChannelActivity.this, getString(R.string.device_has_disconnect),-1);
        }
    }
}
