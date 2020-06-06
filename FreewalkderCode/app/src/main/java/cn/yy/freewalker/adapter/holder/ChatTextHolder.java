package cn.yy.freewalker.adapter.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/4 上午12:11
 */
public class ChatTextHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_user_photo)
    ImageView mChatUserPhotoIv;
    @BindView(R.id.tv_chat_content)
    TextView mChatContentTv;

    public ChatTextHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setContentText(String text){
        mChatContentTv.setText(text);
    }

    public void setUserPhoto(String photoUrl){
        x.image().bind(mChatUserPhotoIv,photoUrl);
    }
}
