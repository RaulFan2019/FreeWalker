package cn.yy.freewalker.ui.adapter.holder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/8 下午9:25
 */
public class ChatFaceHolder extends RecyclerView.ViewHolder {
//    @BindView(R.id.iv_item_chat_face)
    ImageView mFaceIv;
    public ChatFaceHolder(@NonNull View itemView) {
        super(itemView);
//        ButterKnife.bind(this,itemView);
        mFaceIv = itemView.findViewById(R.id.iv_item_chat_face);
    }

    public void setFaceIcon(int faceId){
        mFaceIv.setImageResource(faceId);
    }
}
