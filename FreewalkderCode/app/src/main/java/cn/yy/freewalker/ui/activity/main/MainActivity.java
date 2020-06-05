package cn.yy.freewalker.ui.activity.main;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.fragment.main.MainChatFragment;
import cn.yy.freewalker.ui.fragment.main.MainDeviceFragment;
import cn.yy.freewalker.ui.fragment.main.MainMeFragment;
import cn.yy.freewalker.ui.fragment.main.MainNearbyFragment;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/5 23:30
 */
public class MainActivity extends BaseActivity {


    public static final int TAB_DEVICE = 0x01;
    public static final int TAB_CHAT = 0x02;
    public static final int TAB_NEARBY = 0x03;
    public static final int TAB_ME = 0x04;

    @BindView(R.id.ll_fragment_root)
    LinearLayout llFragmentRoot;

    @BindView(R.id.iv_device)
    View ivDevice;                                              //设备图标
    @BindView(R.id.tv_device)
    TextView tvDevice;                                          //设备文本
    @BindView(R.id.iv_chat)
    View ivChat;                                                //聊天图标
    @BindView(R.id.tv_chat)
    TextView tvChat;                                            //聊天文本
    @BindView(R.id.iv_nearby)
    View ivNearby;                                              //附近图标
    @BindView(R.id.tv_nearby)
    TextView tvNearby;                                          //附近文本
    @BindView(R.id.iv_me)
    View ivMe;                                                  //我的图标
    @BindView(R.id.tv_me)
    TextView tvMe;                                              //我的文本

    /* local data */
    private long exitTime = 0;


    private MainDeviceFragment fragmentDevice;
    private MainChatFragment fragmentChat;
    private MainNearbyFragment fragmentNearby;
    private MainMeFragment fragmentMe;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @OnClick({R.id.ll_device, R.id.ll_chat, R.id.ll_nearby, R.id.ll_me})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_device:
                selectTab(TAB_DEVICE);
                break;
            case R.id.ll_chat:
                selectTab(TAB_CHAT);
                break;
            case R.id.ll_nearby:
                selectTab(TAB_NEARBY);
                break;
            case R.id.ll_me:
                selectTab(TAB_ME);
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
    protected void causeGC() {

    }


    /**
     * 选择TAB
     */
    private void selectTab(int tab) {
        resetBtn();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (tab) {
            //切换到分析
            case TAB_DEVICE:
                ivDevice.setBackgroundResource(R.drawable.icon_main_tab_device_selected);
                tvDevice.setTextColor(getResources().getColor(R.color.tv_accent));
                if (fragmentDevice == null) {
                    fragmentDevice = MainDeviceFragment.newInstance();
                    transaction.add(R.id.ll_fragment_root, fragmentDevice);
                } else {
                    transaction.show(fragmentDevice);
                }
                break;
            //切换到运动
            case TAB_CHAT:
                ivChat.setBackgroundResource(R.drawable.icon_main_tab_chat_selected);
                tvChat.setTextColor(getResources().getColor(R.color.tv_accent));
                if (fragmentChat == null) {
                    fragmentChat = MainChatFragment.newInstance();
                    transaction.add(R.id.ll_fragment_root, fragmentChat);
                } else {
                    transaction.show(fragmentChat);
                }
                break;
            case TAB_NEARBY:
                ivNearby.setBackgroundResource(R.drawable.icon_main_tab_nearby_selected);
                tvNearby.setTextColor(getResources().getColor(R.color.tv_accent));
                if (fragmentNearby == null) {
                    fragmentNearby = MainNearbyFragment.newInstance();
                    transaction.add(R.id.ll_fragment_root, fragmentNearby);
                } else {
                    transaction.show(fragmentNearby);
                }
                break;
            case TAB_ME:
                ivMe.setBackgroundResource(R.drawable.icon_main_tab_me_selected);
                tvMe.setTextColor(getResources().getColor(R.color.tv_accent));
                if (fragmentMe == null) {
                    fragmentMe = MainMeFragment.newInstance();
                    transaction.add(R.id.ll_fragment_root, fragmentMe);
                } else {
                    transaction.show(fragmentMe);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }


    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (fragmentDevice != null) {
            transaction.hide(fragmentDevice);
        }
        if (fragmentChat != null) {
            transaction.hide(fragmentChat);
        }
        if (fragmentNearby != null) {
            transaction.hide(fragmentNearby);
        }
        if (fragmentMe != null) {
            transaction.hide(fragmentMe);
        }
    }


    /**
     * 清除掉所有的选中状态。
     */
    private void resetBtn() {
        ivDevice.setBackgroundResource(R.drawable.icon_main_tab_device_normal);
        ivChat.setBackgroundResource(R.drawable.icon_main_tab_chat_normal);
        ivNearby.setBackgroundResource(R.drawable.icon_main_tab_nearby_normal);
        ivMe.setBackgroundResource(R.drawable.icon_main_tab_me_normal);

        tvDevice.setTextColor(getResources().getColor(R.color.tv_thirdly));
        tvChat.setTextColor(getResources().getColor(R.color.tv_thirdly));
        tvNearby.setTextColor(getResources().getColor(R.color.tv_thirdly));
        tvMe.setTextColor(getResources().getColor(R.color.tv_thirdly));
    }

    /**
     * 按2次退出本页面
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 3000) {
            Toast.makeText(getApplicationContext(), "再按一次退出英文识别功能", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
