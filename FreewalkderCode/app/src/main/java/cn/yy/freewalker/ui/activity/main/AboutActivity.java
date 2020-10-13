package cn.yy.freewalker.ui.activity.main;

import android.os.Message;
import android.view.View;

import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/22 20:55
 */
public class AboutActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_about;
    }


    @OnClick({R.id.btn_back, R.id.ll_privacy, R.id.ll_power})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_privacy:
                startActivity(PrivacyActivity.class);
                break;
            case R.id.ll_power:
                startActivity(PowerCenterActivity.class);
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
