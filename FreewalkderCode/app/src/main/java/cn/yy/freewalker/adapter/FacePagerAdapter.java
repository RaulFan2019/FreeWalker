package cn.yy.freewalker.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/9 下午10:23
 */
public class FacePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> mList;
    public FacePagerAdapter(ArrayList<Fragment> list, FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }
}
