package cn.yy.freewalker.ui.adapter.holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/6 下午9:35
 */
public class ChatUserInfoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_chat_user_gender)
    public TextView mGenderTv;
    @BindView(R.id.tv_chat_user_age)
    public TextView mAgeTv;
    @BindView(R.id.tv_chat_user_like)
    public TextView mLikeTv;
    @BindView(R.id.tv_chat_user_job)
    public TextView mJobTv;

    public ChatUserInfoHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
