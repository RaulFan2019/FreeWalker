package cn.yy.freewalker.ui.activity.main;

import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.yy.freewalker.R;
import cn.yy.freewalker.adapter.ChatLeftTextBinder;
import cn.yy.freewalker.adapter.ChatRightTextBinder;
import cn.yy.freewalker.bean.ChatLeftTextBean;
import cn.yy.freewalker.bean.ChatRightTextBean;
import cn.yy.freewalker.ui.activity.BaseActivity;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author  zhao
 * @date  2020/6/3 22:00
 * @version 1.0
 */
public class SingleChatActivity extends BaseActivity {
    @BindView(R.id.tv_chat_user_title)
    TextView mUserTitleTv;
    @BindView(R.id.rv_chat_list)
    RecyclerView mChatRv;

    private MultiTypeAdapter mChatAdapter;
    private ArrayList<Object> mChatItems = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_chat;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        mChatAdapter = new MultiTypeAdapter();
        mChatAdapter.register(ChatLeftTextBean.class,new ChatLeftTextBinder());
        mChatAdapter.register(ChatRightTextBean.class,new ChatRightTextBinder());

        mChatItems.add(new ChatLeftTextBean("你好",""));
        mChatAdapter.setItems(mChatItems);
    }

    @Override
    protected void initViews() {
        mChatRv.setLayoutManager(new LinearLayoutManager(this));


        mChatRv.setAdapter(mChatAdapter);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
