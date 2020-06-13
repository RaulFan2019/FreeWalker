package cn.yy.freewalker.ui.adapter.binder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cn.yy.freewalker.R;
import cn.yy.freewalker.bean.ChatRoomBean;
import cn.yy.freewalker.ui.adapter.holder.ChatRoomHolder;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/13 下午4:13
 */
public class ChatRoomBinder extends ItemViewBinder<ChatRoomBean,ChatRoomHolder> {
    @NonNull
    @Override
    protected ChatRoomHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ChatRoomHolder(inflater.inflate(R.layout.item_group_info,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomHolder holder, @NonNull ChatRoomBean chatRoomBean) {
        holder.showGroupName(chatRoomBean.name);
    }
}
