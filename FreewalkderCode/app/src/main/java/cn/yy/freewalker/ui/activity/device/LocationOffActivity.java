package cn.yy.freewalker.ui.activity.device;

import android.content.Intent;
import android.os.Message;
import android.provider.Settings;
import android.view.View;

import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.utils.SystemU;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 10:56
 */
public class LocationOffActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_location_off;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }


    @OnClick({R.id.btn_back, R.id.btn_open_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_open_location:
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SystemU.isLocationEnabled(LocationOffActivity.this)) {
            startActivity(FindActivity.class);
            finish();
        }
    }

    @Override
    protected void causeGC() {

    }


}
