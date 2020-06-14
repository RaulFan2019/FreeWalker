package cn.yy.freewalker.ui.adapter.binder;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.adapter.holder.ChatTextHolder;
import cn.yy.freewalker.entity.model.ChatLeftTextBean;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/4 上午12:08
 */
public class ChatLeftTextBinder extends ItemViewBinder<ChatLeftTextBean, ChatTextHolder> {
    @NonNull
    @Override
    protected ChatTextHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ChatTextHolder(inflater.inflate(R.layout.item_chat_text_left,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatTextHolder holder, @NonNull ChatLeftTextBean item) {
        holder.setContentText(item.chatText);
        holder.setUserPhoto(item.photoUrl);
    }
}
