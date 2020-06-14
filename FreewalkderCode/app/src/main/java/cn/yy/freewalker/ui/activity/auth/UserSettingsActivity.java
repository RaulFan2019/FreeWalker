package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.common.CircularImage;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/13 23:17
 */
public class UserSettingsActivity extends BaseActivity {


    @BindView(R.id.iv_avatar)
    CircularImage ivAvatar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_user_settings;
    }


    @OnClick({R.id.btn_back, R.id.ll_avatar, R.id.ll_nickname, R.id.ll_gender,
            R.id.ll_age, R.id.ll_like, R.id.ll_profession, R.id.ll_height, R.id.ll_weight, R.id.ll_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_avatar:
                //TODO
                break;
            case R.id.ll_nickname:
                //TODO
                break;
            case R.id.ll_gender:
                //TODO
                break;
            case R.id.ll_age:
                //TODO
                break;
            case R.id.ll_like:
                //TODO
                break;
            case R.id.ll_profession:
                //TODO
                break;
            case R.id.ll_height:
                //TODO
                break;
            case R.id.ll_weight:
                //TODO
                break;
            case R.id.ll_phone:
                //TODO
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
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }


}
