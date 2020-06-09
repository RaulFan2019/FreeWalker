package cn.yy.freewalker.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/8 下午2:48
 */
public class ChatTimeHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_chat_time)
    TextView mChatTimeTv;

    public ChatTimeHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setTime(String time){
        mChatTimeTv.setText(time);
    }

}
