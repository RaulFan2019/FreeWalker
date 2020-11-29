package cn.yy.freewalker.ui.adapter.holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.event.OnUserAvatarClickEvent;
import cn.yy.freewalker.ui.widget.faceView.QQFaceTextView;
import cn.yy.freewalker.utils.ImageU;

/**
 * @author raul
 * @version 1.0
 * @date 2020/6/4 上午12:11
 */
public class ChatTextHolder extends RecyclerView.ViewHolder {

    public ImageView mChatUserPhotoIv;
    public QQFaceTextView mChatContentQtv;
    public TextView tvUserName;

    public ChatTextHolder(@NonNull View itemView) {
        super(itemView);
//        ButterKnife.bind(this,itemView);
        mChatUserPhotoIv = itemView.findViewById(R.id.iv_user_photo);
        mChatContentQtv = itemView.findViewById(R.id.qtv_chat_content);
        tvUserName = itemView.findViewById(R.id.tv_name);
        ButterKnife.bind(this, itemView);

        mChatUserPhotoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalApp.getInstance().getEventBus().post(new OnUserAvatarClickEvent());
            }
        });
    }

    public void setContentText(String text) {
        mChatContentQtv.setText(text);
    }

    public void setUserPhoto(String photoUrl) {
        ImageU.loadUserImage(photoUrl, mChatUserPhotoIv);
    }

    public void setUserName(final String userName) {
        tvUserName.setText(userName);
    }
}
