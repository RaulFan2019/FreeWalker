package cn.yy.freewalker.ui.fragment.main;

import android.os.Message;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.adapter.FreePagerAdapter;
import cn.yy.freewalker.ui.fragment.BaseFragment;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 0:02
 */
public class MainChatFragment extends BaseFragment {

    @BindView(R.id.vp_chat_page)
    ViewPager mChatVp;
    @BindView(R.id.tl_chat_type)
    TabLayout mChatTl;

    private FreePagerAdapter mVpAdapter;

    /* 构造函数 */
    public static MainChatFragment newInstance() {
        MainChatFragment fragment = new MainChatFragment();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_chat;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initParams() {

        ArrayList<Fragment> fList = new ArrayList<>();
        fList.add(GroupChatListFragment.newInstance());
        fList.add(SingleChatListFragment.newInstance());
        mVpAdapter = new FreePagerAdapter(fList, getFragmentManager());

        mChatVp.setAdapter(mVpAdapter);
        mChatVp.setCurrentItem(0);
        mChatTl.setupWithViewPager(mChatVp);

        mChatTl.getTabAt(0).setText("群聊");
        mChatTl.getTabAt(1).setText("单聊");
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
