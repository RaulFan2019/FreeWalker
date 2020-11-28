package cn.yy.freewalker.ui.activity.main;

import android.content.Intent;
import android.os.Environment;
import android.os.Message;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.FileConfig;
import cn.yy.freewalker.entity.event.NearbyUserCartEvent;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.entity.net.CheckVersionResult;
import cn.yy.freewalker.network.RequestBuilder;
import cn.yy.freewalker.service.UserDeviceCallbackService;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.chat.SingleChatActivity;
import cn.yy.freewalker.ui.fragment.main.MainChatFragment;
import cn.yy.freewalker.ui.fragment.main.MainDeviceFragment;
import cn.yy.freewalker.ui.fragment.main.MainMeFragment;
import cn.yy.freewalker.ui.fragment.main.MainNearbyFragment;
import cn.yy.freewalker.ui.fragment.main.MainNearbyUserCardFragment;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogChoice;
import cn.yy.freewalker.utils.FileU;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/5 23:30
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    public static final int TAB_DEVICE = 0x01;
    public static final int TAB_CHAT = 0x02;
    public static final int TAB_NEARBY = 0x03;
    public static final int TAB_ME = 0x04;

    private static final int MSG_NEED_UPDATE_APP = 0x01;

    private static final int MSG_DOWNLOAD_FILE_OK = 0x03;                      //下载文件成功
    private static final int MSG_DOWNLOAD_FILE_ERROR = 0x04;                   //下载文件失败
    private static final int MSG_DOWNLOAD_FILE_PROGRESS = 0x05;                //下载文件进度更新

    @BindView(R.id.ll_fragment_root)
    LinearLayout llFragmentRoot;
    @BindView(R.id.ll_user_card)
    LinearLayout llFragmentUserCard;
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

    private CheckVersionResult mCheckVersionRE;

    private DialogBuilder mDialogBuilder;

    private MainDeviceFragment fragmentDevice;
    private MainChatFragment fragmentChat;
    private MainNearbyFragment fragmentNearby;
    private MainMeFragment fragmentMe;

    private MainNearbyUserCardFragment fragmentNearbyUserCard;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_NEED_UPDATE_APP:
                showUpdateDialog();
                break;
            case MSG_DOWNLOAD_FILE_OK:
                mDialogBuilder.dismissWaitDialog();
                install7((String) msg.obj);
                break;
            case MSG_DOWNLOAD_FILE_PROGRESS:
                mDialogBuilder.showWaitDialog(MainActivity.this, getString(R.string.app_tip_download_app) + msg.arg1 + "%");
                break;
            case MSG_DOWNLOAD_FILE_ERROR:
                mDialogBuilder.dismissWaitDialog();
                new ToastView(MainActivity.this, getString(R.string.app_toast_download_app_error), -1);
                if (mCheckVersionRE.updateFlag){
                    finish();
                    System.exit(0);
                }
                break;
        }
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

    /**
     * 附近的人相关事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NearbyCardEvent(NearbyUserCartEvent event) {
        if (event.type == NearbyUserCartEvent.SHOW) {
            fragmentNearbyUserCard.updateViews(event.locationInfo);
            llFragmentUserCard.setVisibility(View.VISIBLE);
        } else if (event.type == NearbyUserCartEvent.CLOSE) {
            if (fragmentNearbyUserCard != null) {
                llFragmentUserCard.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    protected void initData() {
        mDialogBuilder = new DialogBuilder();
    }

    @Override
    protected void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .statusBarView(R.id.v_status_bar)
                .init();
    }

    @Override
    protected void doMyCreate() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragmentNearbyUserCard = MainNearbyUserCardFragment.newInstance();
        transaction.add(R.id.ll_user_card, fragmentNearbyUserCard);
        transaction.commitAllowingStateLoss();

        selectTab(TAB_DEVICE);
        saveMapFile1();
        saveMapFile2();

        LocalApp.getInstance().getEventBus().register(this);

        postCheckVersion();

        startService(UserDeviceCallbackService.class);
    }

    @Override
    protected void causeGC() {
        LocalApp.getInstance().getEventBus().unregister(this);
    }


    @Override
    public void onBackPressed() {

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
                ImmersionBar.with(this)
                        .statusBarDarkFont(true)
                        .init();
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
                ImmersionBar.with(this)
                        .statusBarDarkFont(true)
                        .init();
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
                ImmersionBar.with(this)
                        .statusBarDarkFont(true)
                        .init();
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
                ImmersionBar.with(this)
                        .statusBarDarkFont(false)
                        .init();
                ivMe.setBackgroundResource(R.drawable.icon_main_tab_me_selected);
                tvMe.setTextColor(ContextCompat.getColor(this, R.color.tv_accent));
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

    private void saveMapFile1() {
        try {
            // 先获取系统默认的文档存放根目录
            File parent_path = Environment.getExternalStorageDirectory();
            File dir = new File(parent_path.getAbsoluteFile(), "data");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir.getAbsoluteFile(), "style.data");
            if (file.exists()) {
                return;
            }
            //读取数据文件
            InputStream open = this.getResources().getAssets().open("styleMap/style.data");

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            int len;
            byte[] buf = new byte[1024];
            while ((len = open.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMapFile2() {
        try {
            // 先获取系统默认的文档存放根目录
            File parent_path = Environment.getExternalStorageDirectory();
            File dir = new File(parent_path.getAbsoluteFile(), "data");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir.getAbsoluteFile(), "style_extra.data");
            if (file.exists()) {
                return;
            }
            //读取数据文件
            InputStream open = this.getResources().getAssets().open("styleMap/style_extra.data");

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            int len;
            byte[] buf = new byte[1024];
            while ((len = open.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查版本更新
     */
    private void postCheckVersion() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.checkAppVersion(MainActivity.this);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        Log.e(TAG, "postCheckVersion result:" + result.data);
                        if (result.code == 200) {
                            mCheckVersionRE = JSON.parseObject(result.data, CheckVersionResult.class);
                            if (mCheckVersionRE.isUpdate) {
                                mHandler.sendEmptyMessage(MSG_NEED_UPDATE_APP);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e(TAG, "postCheckVersion onError:" + ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }


    /**
     * 显示更新对话框
     */
    private void showUpdateDialog() {
        mDialogBuilder.showChoiceDialog(MainActivity.this,
                "检测到新版本\n" + mCheckVersionRE.versionName + "是否更新？",
                "更新", "取消", !mCheckVersionRE.updateFlag);
        mDialogBuilder.setChoiceDialogListener(new DialogChoice.onBtnClickListener() {
            @Override
            public void onConfirmBtnClick() {
                downloadFile();
            }

            @Override
            public void onCancelBtnClick() {

            }
        });
    }

    /**
     * 下载文件
     */
    private void downloadFile() {
        mDialogBuilder.showWaitDialog(MainActivity.this, "正在下载软件..", !mCheckVersionRE.updateFlag);
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams(mCheckVersionRE.appUrl);
                params.setCancelFast(true);
                params.setSaveFilePath(FileConfig.DOWNLOAD_PATH);
                params.setAutoRename(true);
                mCancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
                    @Override
                    public void onSuccess(File result) {
                        Message msg = new Message();
                        msg.what = MSG_DOWNLOAD_FILE_OK;
                        msg.obj = result.getAbsolutePath();
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        YLog.e(TAG, "ex:" + ex.getMessage());
                        Message msg = new Message();
                        msg.what = MSG_DOWNLOAD_FILE_ERROR;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }

                    @Override
                    public void onWaiting() {
                    }

                    @Override
                    public void onStarted() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        Message msg = new Message();
                        msg.what = MSG_DOWNLOAD_FILE_PROGRESS;
                        msg.arg1 = (int) (current * 100.0 / total);
                        mHandler.sendMessage(msg);
                    }
                });
            }
        });
    }


    private void install7(final String path) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FileU.setIntentDataAndType(MainActivity.this, intent,
                "application/vnd.android.package-archive", file, true);
        startActivity(intent);
    }


}
