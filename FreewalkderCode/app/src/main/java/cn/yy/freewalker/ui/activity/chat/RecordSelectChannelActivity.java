package cn.yy.freewalker.ui.activity.chat;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.data.DBDataChannel;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.ChannelDbEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.device.DeviceSettingChannelActivity;
import cn.yy.freewalker.ui.adapter.DeviceSettingChannelRvAdapter;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogChoice;
import cn.yy.freewalker.ui.widget.dialog.DialogSingleSelect;
import cn.yy.sdk.ble.BM;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/14 22:58
 */
public class RecordSelectChannelActivity extends BaseActivity {


    private static final int STATE_INIT = 0x01;
    private static final int STATE_RECORD_ING = 0x02;


    @BindView(R.id.rv_channel)
    RecyclerView rvChannel;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.fl_recording)
    FrameLayout flRecording;


    DeviceSettingChannelRvAdapter adapter;
    DialogBuilder mDialogBuilder;
    /* data */
    private int mChannel = -1;

    private int mState = STATE_INIT;

    private UserDbEntity mUser;
    List<ChannelDbEntity> listChannel = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_select_channel;
    }


    @OnClick({R.id.btn_back, R.id.ll_record_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            //录音文件
            case R.id.ll_record_file:
                startActivity(RecordFileListActivity.class);
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        mDialogBuilder = new DialogBuilder();
        mUser = DBDataUser.getLoginUser(RecordSelectChannelActivity.this);
        //初始化频道
        for (int i = 0; i < 30; i++) {
            ChannelDbEntity channelDbEntity = DBDataChannel.getChannel(BM.getManager().getConnectMac(), i);
            if (channelDbEntity == null) {
                channelDbEntity = new ChannelDbEntity(System.currentTimeMillis(), BM.getManager().getConnectMac(), i, "", 5);
                DBDataChannel.save(channelDbEntity);
            }
            listChannel.add(channelDbEntity);
        }
    }

    @Override
    protected void initViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);

        rvChannel.setLayoutManager(layoutManager);

        adapter = new DeviceSettingChannelRvAdapter(RecordSelectChannelActivity.this,
                mChannel,listChannel,
                new DeviceSettingChannelRvAdapter.onChannelClick() {
                    @Override
                    public void onClick(int channel) {
                        //开始录音
                        if (mState == STATE_INIT) {
                            showStartRecordDialog(channel);

                        } else {
                            //切换频道
                            if (mChannel != channel){
                                showChangeChannelDialog(channel);
                            // 进入频道/停止录音
                            }else {
                                showChannelActionDialog(channel);
                            }

                        }

                    }
                });

        rvChannel.setAdapter(adapter);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }


    /**
     * 显示开始录音对话框
     */
    private void showStartRecordDialog(final int channel) {
        mDialogBuilder.showChoiceDialog(RecordSelectChannelActivity.this, getString(R.string.chat_title_dialog_record),
                getString(R.string.chat_tip_dialog_record_start), getString(R.string.chat_action_dialog_record_start)
                , getString(R.string.app_action_cancel));
        mDialogBuilder.setChoiceDialogListener(new DialogChoice.onBtnClickListener() {
            @Override
            public void onConfirmBtnClick() {
                mChannel = channel;
                tvTip.setVisibility(View.GONE);
                flRecording.setVisibility(View.VISIBLE);
                mState = STATE_RECORD_ING;

            }

            @Override
            public void onCancelBtnClick() {

            }
        });
    }


    /**
     * 显示开始录音对话框
     */
    private void showChangeChannelDialog(final int channel) {
        mDialogBuilder.showChoiceDialog(RecordSelectChannelActivity.this, getString(R.string.chat_title_dialog_record_change_channel),
                getString(R.string.chat_tip_dialog_record_change_channel), getString(R.string.chat_action_dialog_record_change_channel)
                , getString(R.string.app_action_cancel));
        mDialogBuilder.setChoiceDialogListener(new DialogChoice.onBtnClickListener() {
            @Override
            public void onConfirmBtnClick() {
                mChannel = channel;
            }

            @Override
            public void onCancelBtnClick() {

            }
        });
    }


    private void showChannelActionDialog(final int channel){
        List<String> listAction = new ArrayList<>();
        listAction.add(getString(R.string.chat_action_dialog_record_into));
        listAction.add(getString(R.string.chat_action_dialog_record_stop));
        mDialogBuilder.showSingleSelectDialog(RecordSelectChannelActivity.this,listAction);
        mDialogBuilder.setSingleSelectDialogListener(new DialogSingleSelect.onItemClickListener() {
            @Override
            public void onConfirmBtnClick(int pos) {
                //进入录音界面
                if (pos == 0){
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getString(R.string.chat_title_channel) + mChannel);
                    startActivity(RecordListActivity.class, bundle);
                //停止录音
                }else {
                    tvTip.setVisibility(View.VISIBLE);
                    flRecording.setVisibility(View.GONE);
                    mState = STATE_INIT;
                }
            }
        });
    }
}
