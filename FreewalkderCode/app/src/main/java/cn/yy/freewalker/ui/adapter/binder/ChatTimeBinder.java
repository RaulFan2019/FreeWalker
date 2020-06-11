package cn.yy.freewalker.ui.adapter.binder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.adapter.holder.ChatTimeHolder;
import cn.yy.freewalker.bean.ChatTimeBean;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/8 下午2:49
 */
public class ChatTimeBinder extends ItemViewBinder<ChatTimeBean, ChatTimeHolder> {
    @NonNull
    @Override
    protected ChatTimeHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ChatTimeHolder(inflater.inflate(R.layout.item_chat_time,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatTimeHolder chatTimeHolder, @NonNull ChatTimeBean chatTimeBean) {

    }
}
