package cn.yy.freewalker.ui.adapter.binder;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.adapter.holder.ChatTextHolder;
import cn.yy.freewalker.entity.model.ChatRightTextBean;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/4 上午12:08
 */
public class ChatRightTextBinder extends ItemViewBinder<ChatRightTextBean, ChatTextHolder> {
    @NonNull
    @Override
    protected ChatTextHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ChatTextHolder(inflater.inflate(R.layout.item_chat_text_right,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatTextHolder holder, @NonNull ChatRightTextBean item) {
        holder.setContentText(item.chatText);
        holder.setUserPhoto(item.photoUrl);
    }
}
