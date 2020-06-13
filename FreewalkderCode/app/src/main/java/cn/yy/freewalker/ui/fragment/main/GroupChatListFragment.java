package cn.yy.freewalker.ui.fragment.main;

import android.os.Bundle;
import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.yy.freewalker.R;
import cn.yy.freewalker.bean.ChatRoomBean;
import cn.yy.freewalker.ui.adapter.binder.ChatRoomBinder;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/11 下午11:52
 */
public class GroupChatListFragment extends BaseFragment {

    @BindView(R.id.iv_group_chat_list)
    RecyclerView mChatListRv;


    private MultiTypeAdapter mAdapter;

    private ArrayList<Object> mRoomItems = new ArrayList<>();

    public static GroupChatListFragment newInstance() {

        Bundle args = new Bundle();

        GroupChatListFragment fragment = new GroupChatListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_chat_list;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initParams() {
        mAdapter  = new MultiTypeAdapter();
        mAdapter.register(ChatRoomBean.class,new ChatRoomBinder());
        mAdapter.setItems(mRoomItems);

        for(int i =0;i < 20;i++){
            mRoomItems.add(new ChatRoomBean("频道"+i,i));
        }
        mChatListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mChatListRv.setAdapter(mAdapter);
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
