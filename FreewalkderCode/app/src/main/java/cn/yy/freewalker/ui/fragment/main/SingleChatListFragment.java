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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.UrlConfig;
import cn.yy.freewalker.data.DBDataSingleChatMsg;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.SingleChatMsgEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.model.ChatPersonBean;
import cn.yy.freewalker.ui.activity.chat.SingleChatActivity;
import cn.yy.freewalker.ui.adapter.binder.ChatPersonBinder;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.utils.TimeU;
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

    private UserDbEntity mUser;
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
        mUser = DBDataUser.getLoginUser(mContext);

        mAdapter = new MultiTypeAdapter();
        ChatPersonBinder binder = new ChatPersonBinder();
        binder.setOnItemListener(new ChatPersonBinder.OnPersonClickListener() {
            @Override
            public void onDelConfirm(View v, int pos) {
                DBDataSingleChatMsg.deleteByUser(mUser.userId, ((ChatPersonBean)mFriendItems.get(pos)).id);
                mFriendItems.remove(pos);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onShield(View view, int pos) {
                ChatPersonBean bean = (ChatPersonBean) mFriendItems.get(pos);
                UserDbEntity friend = DBDataUser.getUserInfoByUserId(bean.id);
                friend.isShield = !bean.isShield;
                DBDataUser.update(friend);
                //更新列表
                bean.isShield = !bean.isShield;
                if (bean.isShield){
                    bean.txShield = getActivity().getString(R.string.app_action_shield_cancel);
                }else {
                    bean.txShield = getActivity().getString(R.string.app_action_shield);
                }
                mFriendItems.set(pos, bean);
                mAdapter.notifyDataSetChanged();

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
            List<SingleChatMsgEntity> listMsg = DBDataSingleChatMsg.getAllGroupChatMsg(mUser.userId, dbEntity.userId);
            String content = "";
            String time = "";
            if (listMsg.size() > 0) {
                content = listMsg.get(listMsg.size() - 1).content;
                time = TimeU.timeToTimeStr(listMsg.get(listMsg.size() - 1).singleChatId, TimeU.FORMAT_TYPE_2);
            }
            if (dbEntity.isShield){
                mFriendItems.add(new ChatPersonBean(dbEntity.name, UrlConfig.IMAGE_HOST + dbEntity.avatar,
                        dbEntity.userId, content, time, dbEntity.isShield, getActivity().getString(R.string.app_action_shield_cancel)));
            }else {
                mFriendItems.add(new ChatPersonBean(dbEntity.name, UrlConfig.IMAGE_HOST + dbEntity.avatar,
                        dbEntity.userId, content, time, dbEntity.isShield,getActivity().getString(R.string.app_action_shield)));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onInVisible() {

    }
}
