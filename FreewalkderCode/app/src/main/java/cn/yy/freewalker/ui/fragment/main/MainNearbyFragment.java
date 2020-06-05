package cn.yy.freewalker.ui.fragment.main;

import android.os.Message;

import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.fragment.BaseFragment;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 0:03
 */
public class MainNearbyFragment extends BaseFragment {

    /* 构造函数 */
    public static MainNearbyFragment newInstance() {
        MainNearbyFragment fragment = new MainNearbyFragment();
        return fragment;
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_nearby;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initParams() {

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
