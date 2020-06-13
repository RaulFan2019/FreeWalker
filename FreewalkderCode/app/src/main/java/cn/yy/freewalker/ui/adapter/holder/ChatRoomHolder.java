package cn.yy.freewalker.ui.adapter.holder;

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
 * @date 2020/6/13 下午4:23
 */
public class ChatRoomHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_group_name)
    TextView mRoomNameTv;

    public ChatRoomHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void showGroupName(String name){
        mRoomNameTv.setText(name);
    }
}
