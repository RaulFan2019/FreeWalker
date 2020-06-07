package cn.yy.freewalker.ui.activity.device;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.view.View;

import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 10:55
 */
public class BluetoothOffActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_bluetooth_off;
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    startActivity(FindActivity.class);
                    finish();
                }
            }
        }
    };

    @Override
    protected void myHandleMsg(Message msg) {

    }


    @OnClick({R.id.btn_back, R.id.openBluetooth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.openBluetooth:
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBtIntent);
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void doMyCreate() {
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void causeGC() {
        unregisterReceiver(mReceiver);
    }

}
