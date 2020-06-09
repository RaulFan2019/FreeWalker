package cn.yy.freewalker.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cn.yy.freewalker.R;
import cn.yy.freewalker.adapter.holder.ChatFaceHolder;
import cn.yy.freewalker.bean.ChatFaceBean;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/8 下午9:31
 */
public class ChatFaceBinder extends ItemViewBinder<ChatFaceBean, ChatFaceHolder> {
    @NonNull
    @Override
    protected ChatFaceHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ChatFaceHolder(inflater.inflate(R.layout.item_chat_face,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatFaceHolder chatFaceHolder, @NonNull ChatFaceBean chatFaceBean) {
        chatFaceHolder.setFaceIcon(chatFaceBean.faceId);
    }
}
