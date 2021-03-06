package cn.yy.freewalker.ui.adapter.binder;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.adapter.holder.ChatUserInfoHolder;
import cn.yy.freewalker.entity.model.ChatUserInfoBean;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/4 上午12:08
 */
public class ChatUserInfoBinder extends ItemViewBinder<ChatUserInfoBean, ChatUserInfoHolder> {
    @NonNull
    @Override
    protected ChatUserInfoHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ChatUserInfoHolder(inflater.inflate(R.layout.item_chat_user_info,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatUserInfoHolder holder, @NonNull ChatUserInfoBean item) {
        holder.mGenderTv.setText(item.gender);
        holder.mAgeTv.setText(item.age);
        holder.mLikeTv.setText(item.like);
        holder.mJobTv.setText(item.job);
    }

}
