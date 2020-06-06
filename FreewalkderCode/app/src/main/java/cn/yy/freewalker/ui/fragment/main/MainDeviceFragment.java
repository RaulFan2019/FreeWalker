package cn.yy.freewalker.ui.fragment.main;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.DeviceConfig;
import cn.yy.freewalker.entity.adapter.BindDeviceAdapterEntity;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.ui.activity.device.ScanActivity;
import cn.yy.freewalker.ui.adapter.DeviceListAdapter;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogChoice;
import cn.yy.freewalker.ui.widget.listview.ListViewUnbindDevice;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 0:02
 */
public class MainDeviceFragment extends BaseFragment implements DeviceListAdapter.onListItemDeleteListener, AdapterView.OnItemClickListener {


    /* contains */
    private static final String TAG = "MainDeviceFragment";

    /* view */
    @BindView(R.id.tv_none)
    TextView tvNone;
    @BindView(R.id.lv_device)
    ListViewUnbindDevice lvDevice;



    DeviceListAdapter adapter;
    DialogBuilder mDialogBuilder;


    /* data */
    List<BindDeviceAdapterEntity> listDevice = new ArrayList<>();


    /* 构造函数 */
    public static MainDeviceFragment newInstance() {
        MainDeviceFragment fragment = new MainDeviceFragment();
        return fragment;
    }


    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        startActivity(ScanActivity.class);
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
                listDevice.remove(index);
                adapter.notifyDataSetChanged();
                if (listDevice.size() == 0) {
                    tvNone.setVisibility(View.VISIBLE);
                    lvDevice.setVisibility(View.GONE);
                }

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

        if (listDevice.get(position).status == DeviceConfig.ConnectStates.DISCONNECT) {

            for (BindDeviceAdapterEntity entity : listDevice) {
                if (entity.deviceDbEntity.deviceId == listDevice.get(position).deviceDbEntity.deviceId) {
                    entity.status = DeviceConfig.ConnectStates.WORKED;
                } else {
                    entity.status = DeviceConfig.ConnectStates.DISCONNECT;
                }
            }
            adapter.notifyDataSetChanged();
        }else {
            //TODO 跳转到设备详情
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_device;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initParams() {
        //Test START
        //Add device
        listDevice.add(new BindDeviceAdapterEntity(new BindDeviceDbEntity(System.currentTimeMillis(),
                "15221798774", "FW001-001", DeviceConfig.Type.BLACK, "aa:bb:aa:bb:aa:bb"),
                DeviceConfig.ConnectStates.WORKED));
        listDevice.add(new BindDeviceAdapterEntity(new BindDeviceDbEntity(System.currentTimeMillis() + 1,
                "15221798774", "FW001-002", DeviceConfig.Type.RED, "aa:bb:aa:bb:aa:dd"),
                DeviceConfig.ConnectStates.DISCONNECT));
        //Test END


        adapter = new DeviceListAdapter(getActivity(), listDevice);
        adapter.setListener(this);
        lvDevice.setOnItemClickListener(this);
        lvDevice.setAdapter(adapter);

        mDialogBuilder = new DialogBuilder();
    }

    @Override
    protected void causeGC() {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInVisible() {

    }


}
