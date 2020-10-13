package cn.yy.freewalker.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.ChatRoomBean;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/13 下午4:23
 */
public class ChatRoomHolder extends RecyclerView.ViewHolder {
//    @BindView(R.id.tv_group_name)
    TextView mRoomNameTv;
    View icChannel;

    public ChatRoomHolder(@NonNull View itemView) {
        super(itemView);
//        ButterKnife.bind(this,itemView);
        mRoomNameTv = itemView.findViewById(R.id.tv_group_name);
        icChannel = itemView.findViewById(R.id.iv_channel_icon);
    }

    public void updateUI(ChatRoomBean chat){
        mRoomNameTv.setText(chat.name);
        icChannel.setBackgroundResource(chat.iconRes);
    }
}
