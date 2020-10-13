package cn.yy.freewalker.ui.activity.main;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/22 21:18
 */
public class PowerCenterActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_user_power_center;
    }

    @OnClick({R.id.btn_back, R.id.ll_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_delete:
                startActivity(DeleteUserActivity.class);
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

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
    protected void causeGC() {

    }


}
