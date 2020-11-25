package cn.yy.freewalker.ui.fragment.main;

import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.data.DBDataDevice;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.adapter.BindDeviceAdapterEntity;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.ui.activity.device.DeviceSettingsActivity;
import cn.yy.freewalker.ui.activity.device.DeviceFindActivity;
import cn.yy.freewalker.ui.adapter.DeviceListAdapter;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogChoice;
import cn.yy.freewalker.ui.widget.listview.ListViewUnbindDevice;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.array.ConnectStates;
import cn.yy.sdk.ble.observer.ConnectListener;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 0:02
 */
public class MainDeviceFragment extends BaseFragment implements DeviceListAdapter.onListItemDeleteListener,
        AdapterView.OnItemClickListener, ConnectListener {


    /* contains */
    private static final String TAG = "MainDeviceFragment";

    private static final int MSG_UPDATE_LIST = 0x01;

    /* view */
    @BindView(R.id.tv_none)
    TextView tvNone;
    @BindView(R.id.lv_device)
    ListViewUnbindDevice lvDevice;

    DeviceListAdapter adapter;
    DialogBuilder mDialogBuilder;


    /* data */
    List<BindDeviceAdapterEntity> listDevice = new ArrayList<>();
    UserDbEntity mUser;

    /* 构造函数 */
    public static MainDeviceFragment newInstance() {
        MainDeviceFragment fragment = new MainDeviceFragment();
        return fragment;
    }


    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        startActivity(DeviceFindActivity.class);
    }


    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what){
            case MSG_UPDATE_LIST:
                adapter.notifyDataSetChanged();
                if (listDevice.size() == 0) {
                    tvNone.setVisibility(View.VISIBLE);
                    lvDevice.setVisibility(View.GONE);
                }else {
                    tvNone.setVisibility(View.GONE);
                    lvDevice.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void connectStateChange(int state) {
        for (BindDeviceAdapterEntity entity : listDevice){
            if (entity.deviceDbEntity.deviceMac.equals(BM.getManager().getConnectMac())){
                entity.status = BM.getManager().getConnectState();
            }else {
                entity.status = ConnectStates.DISCONNECT;
            }
        }
        mHandler.sendEmptyMessage(MSG_UPDATE_LIST);
    }

    /**
     * 解绑设备
     *
     * @param index
     */
    @Override
    public void onDelete(final int index) {
        YLog.e(TAG, "onDelete:" + index);
        mDialogBuilder.showChoiceDialog(getActivity(),
                getString(R.string.device_tip_ask_unbind),
                getString(R.string.device_action_unbind_in_dialog),
                getString(R.string.app_action_cancel));
        mDialogBuilder.setChoiceDialogListener(new DialogChoice.onBtnClickListener() {
            @Override
            public void onConfirmBtnClick() {
                if (BM.getManager().getConnectMac().equals(listDevice.get(index).deviceDbEntity.deviceMac)){
                    BM.getManager().disConnectDevice();
                }
                DBDataDevice.delete(listDevice.get(index).deviceDbEntity);
                listDevice.remove(index);
                mHandler.sendEmptyMessage(MSG_UPDATE_LIST);
            }

            @Override
            public void onCancelBtnClick() {

            }
        });

    }

    /**
     * 点击Item
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (listDevice.get(position).status < ConnectStates.CONNECTED) {
            for (BindDeviceAdapterEntity entity : listDevice) {
                if (entity.deviceDbEntity.deviceId == listDevice.get(position).deviceDbEntity.deviceId) {
                    BM.getManager().addNewConnect(entity.deviceDbEntity.deviceMac,entity.deviceDbEntity.deviceName,true);
                } else {
                    BM.getManager().disConnectDevice();
                }
            }
            adapter.notifyDataSetChanged();
        }else {
            startActivity(DeviceSettingsActivity.class);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_device;
    }


    @Override
    protected void initParams() {
        adapter = new DeviceListAdapter(getActivity(), listDevice);
        adapter.setListener(this);
        lvDevice.setOnItemClickListener(this);
        lvDevice.setAdapter(adapter);

        mDialogBuilder = new DialogBuilder();

        BM.getManager().registerConnectListener(this);
    }

    @Override
    protected void causeGC() {
        BM.getManager().unRegisterConnectListener(this);
    }

    @Override
    protected void onVisible() {
        mUser = DBDataUser.getLoginUser(mContext);
        List<BindDeviceDbEntity> listDb = DBDataDevice.getAllDeviceByUser(mUser.userId);
        listDevice.clear();
        for (BindDeviceDbEntity entity : listDb){
            BindDeviceAdapterEntity adapterEntity = new BindDeviceAdapterEntity(entity, ConnectStates.DISCONNECT);
            if (adapterEntity.deviceDbEntity.deviceMac.equals(BM.getManager().getConnectMac())){
                adapterEntity.status = BM.getManager().getConnectState();
            }
            listDevice.add(adapterEntity);
        }
        mHandler.sendEmptyMessage(MSG_UPDATE_LIST);

    }

    @Override
    protected void onInVisible() {

    }



}
