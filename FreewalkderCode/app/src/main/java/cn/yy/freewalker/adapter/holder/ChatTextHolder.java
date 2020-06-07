package cn.yy.freewalker.adapter.holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.widget.textview.QQFaceTextView;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/4 上午12:11
 */
public class ChatTextHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_user_photo)
    ImageView mChatUserPhotoIv;
    @BindView(R.id.qtv_chat_content)
    QQFaceTextView mChatContentQtv;

    public ChatTextHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setContentText(String text){
        mChatContentQtv.setText(text);
    }

    public void setUserPhoto(String photoUrl){
        x.image().bind(mChatUserPhotoIv,photoUrl);
    }
}
