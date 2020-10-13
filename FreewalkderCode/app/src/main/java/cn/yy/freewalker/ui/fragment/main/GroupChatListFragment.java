package cn.yy.freewalker.ui.fragment.main;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.ChatFaceBean;
import cn.yy.freewalker.entity.model.ChatRoomBean;
import cn.yy.freewalker.ui.activity.chat.GroupChatActivity;
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
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(ChatRoomBean.class, new ChatRoomBinder());
        mAdapter.setItems(mRoomItems);

        for (int i = 1; i < 31; i++) {
            switch (i){
                case 1:
                case 2:
                case 3:
                    mRoomItems.add(new ChatRoomBean(i, getString(R.string.chat_tx_channel) + i + " | " + getString(R.string.device_channel_1),R.drawable.icon_channel_public));
                    break;
                case 4:
                    mRoomItems.add(new ChatRoomBean(i, getString(R.string.chat_tx_channel) + i + " | " + getString(R.string.device_channel_4),R.drawable.icon_channel_community));
                    break;
                case 5:
                    mRoomItems.add(new ChatRoomBean(i, getString(R.string.chat_tx_channel) + i + " | " + getString(R.string.device_channel_5),R.drawable.icon_channel_campus));
                    break;
                case 6:
                    mRoomItems.add(new ChatRoomBean(i, getString(R.string.chat_tx_channel) + i + " | " + getString(R.string.device_channel_6),R.drawable.icon_channel_business));
                    break;
                case 7:
                    mRoomItems.add(new ChatRoomBean(i, getString(R.string.chat_tx_channel) + i + " | " + getString(R.string.device_channel_7),R.drawable.icon_channel_love));
                    break;
                case 8:
                    mRoomItems.add(new ChatRoomBean(i, getString(R.string.chat_tx_channel) + i + " | " + getString(R.string.device_channel_8),R.drawable.icon_channel_job));
                    break;
                case 9:
                    mRoomItems.add(new ChatRoomBean(i, getString(R.string.chat_tx_channel) + i + " | " + getString(R.string.device_channel_9),R.drawable.icon_channel_travel));
                    break;
                case 10:
                    mRoomItems.add(new ChatRoomBean(i, getString(R.string.chat_tx_channel) + i + " | " + getString(R.string.device_channel_10),R.drawable.icon_channel_traffic));
                    break;
                default:
                    mRoomItems.add(new ChatRoomBean(i, getString(R.string.chat_tx_channel) + i,R.drawable.icon_channel_normal));
                    break;
            }

        }
        mChatListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mChatListRv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {

            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                view.setOnClickListener(v -> {
                    int position = mChatListRv.getChildAdapterPosition(view);
                    Object bean = mRoomItems.get(position);
                    if (bean instanceof ChatRoomBean) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("room",(ChatRoomBean)mRoomItems.get(position));
                        startActivity(GroupChatActivity.class, bundle);
                    }
                });
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {

            }
        });
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
