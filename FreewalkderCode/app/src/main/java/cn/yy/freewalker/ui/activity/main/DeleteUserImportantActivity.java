package cn.yy.freewalker.ui.activity.main;

import android.os.Bundle;
import android.os.Message;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/22 21:51
 */
public class DeleteUserImportantActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_delete_user_important;
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
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
