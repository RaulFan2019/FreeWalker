package cn.yy.freewalker.ui.adapter.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.ChatPersonBean;
import cn.yy.freewalker.ui.adapter.holder.ChatPersonHolder;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/13 下午4:13
 */
public class ChatPersonBinder extends ItemViewBinder<ChatPersonBean, ChatPersonHolder> {

    private OnPersonClickListener listener;

    @NonNull
    @Override
    protected ChatPersonHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        return new ChatPersonHolder(inflater.inflate(R.layout.item_person_chat_info, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatPersonHolder holder, @NonNull ChatPersonBean chatRoomBean) {
        holder.showName(chatRoomBean.name);
        holder.showPhoto(chatRoomBean.photoUrl);
        holder.mDelConfirmBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelConfirm(v,getPosition(holder));
            }
        });

        holder.mShieldBtn.setOnClickListener(v -> {
            if(listener != null){
                listener.onShield(v,getPosition(holder));
            }
        });

        holder.mPersonRl.setOnClickListener(v -> {
            if(listener != null){
                listener.onItem(v,getPosition(holder));
            }
        });
    }

    public void setOnItemListener(OnPersonClickListener listener) {
        this.listener = listener;
    }

    public interface OnPersonClickListener {
        void onDelConfirm(View v,int pos);

        void onShield(View view,int pos);

        void onItem(View view,int pos);
    }
}
