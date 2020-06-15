package cn.yy.freewalker.ui.activity.chat;

import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.RecordLeftBean;
import cn.yy.freewalker.entity.model.RecordRightBean;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.adapter.binder.RecordLeftBinder;
import cn.yy.freewalker.ui.adapter.binder.RecordRightBinder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午8:41
 */
public class RecordListActivity extends BaseActivity {
    @BindView(R.id.tv_del_voice)
    TextView mDelVoiceTv;
    @BindView(R.id.tv_save_voice)
    TextView mSaveVoiceTv;
    @BindView(R.id.ll_operate_voice)
    LinearLayout mOperateLl;
    @BindView(R.id.rv_voice_list)
    RecyclerView mVoiceListRv;


    private MultiTypeAdapter mAdapter;

    private ArrayList<Object> mRecordItems = new ArrayList<>();

    @OnClick({R.id.tv_del_voice, R.id.tv_save_voice})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_del_voice:

                break;
            case R.id.tv_save_voice:

                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_list;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(RecordLeftBean.class, new RecordLeftBinder());
        mAdapter.register(RecordRightBean.class,new RecordRightBinder());

        mRecordItems.add(new RecordLeftBean(23,true));
        mRecordItems.add(new RecordRightBean(30,false));
        mAdapter.setItems(mRecordItems);
    }

    @Override
    protected void initViews() {
        mVoiceListRv.setLayoutManager(new LinearLayoutManager(this));
        mVoiceListRv.setAdapter(mAdapter);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }
}
