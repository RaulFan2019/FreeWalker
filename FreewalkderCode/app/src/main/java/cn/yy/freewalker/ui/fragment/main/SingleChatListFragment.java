package cn.yy.freewalker.ui.fragment.main;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.UrlConfig;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.model.ChatPersonBean;
import cn.yy.freewalker.ui.activity.chat.SingleChatActivity;
import cn.yy.freewalker.ui.adapter.binder.ChatPersonBinder;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/11 下午11:52
 */
public class SingleChatListFragment extends BaseFragment {


    /* views */
    @BindView(R.id.iv_single_chat_list)
    RecyclerView mChatListRv;
    @BindView(R.id.rl_no_content_tip)
    RelativeLayout mNoContentRl;

    private MultiTypeAdapter mAdapter;

    private ArrayList<Object> mFriendItems = new ArrayList<>();

    public static SingleChatListFragment newInstance() {
        SingleChatListFragment fragment = new SingleChatListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_single_chat_list;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initParams() {
        mAdapter = new MultiTypeAdapter();
        ChatPersonBinder binder = new ChatPersonBinder();
        binder.setOnItemListener(new ChatPersonBinder.OnPersonClickListener() {
            @Override
            public void onDelConfirm(View v, int pos) {
                mFriendItems.remove(pos);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onShield(View view, int pos) {
                ChatPersonBean bean = (ChatPersonBean) mFriendItems.get(pos);
                Toast.makeText(getContext(), "屏蔽好友：" + bean.name, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItem(View view, int pos) {
                ChatPersonBean bean = (ChatPersonBean) mFriendItems.get(pos);

                Bundle bundle = new Bundle();
                bundle.putInt("destUserId", bean.id);
                startActivity(SingleChatActivity.class, bundle);
            }
        });

        mAdapter.register(ChatPersonBean.class, binder);
        mAdapter.setItems(mFriendItems);

        mChatListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mChatListRv.setAdapter(mAdapter);
    }

    @Override
    protected void causeGC() {

    }

    @Override
    protected void onVisible() {
        List<UserDbEntity> listFriends = DBDataUser.getAllFriends(mContext);
        mFriendItems.clear();
        for (UserDbEntity dbEntity : listFriends) {
            mFriendItems.add(new ChatPersonBean(dbEntity.name, UrlConfig.IMAGE_HOST + dbEntity.avatar, dbEntity.userId));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onInVisible() {

    }
}
