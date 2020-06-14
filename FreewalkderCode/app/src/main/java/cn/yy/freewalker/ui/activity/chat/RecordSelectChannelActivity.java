package cn.yy.freewalker.ui.activity.chat;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.device.DeviceSettingChannelActivity;
import cn.yy.freewalker.ui.adapter.DeviceSettingChannelRvAdapter;

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

    DeviceSettingChannelRvAdapter adapter;


    /* data */
    private int mChannel = 19;

    private int mState = STATE_INIT;


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
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);

        rvChannel.setLayoutManager(layoutManager);

        adapter = new DeviceSettingChannelRvAdapter(RecordSelectChannelActivity.this,
                mChannel,
                new DeviceSettingChannelRvAdapter.onChannelClick() {
                    @Override
                    public void onClick(int channel) {
                        mChannel = channel;
                        //开始录音
                        if (mState == STATE_INIT){
                           //TODO
                        //切换频道
                        }else {
                            //TODO
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

}
