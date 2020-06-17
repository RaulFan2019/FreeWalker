package cn.yy.freewalker.ui.adapter.listener;

import android.view.View;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/16 下午10:46
 */
public interface OnItemListener {
    void onLongClick(View view,int pos);
    void onClick(View view,int pos);
}
