package cn.yy.freewalker.ui.adapter.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.widget.listview.SwipeMenuLayout;
import cn.yy.freewalker.utils.ImageU;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/13 下午4:23
 */
public class ChatPersonHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.iv_channel_icon)
    ImageView ivAvatar;
    @BindView(R.id.tv_person_name)
    TextView mNameTv;
    @BindView(R.id.tv_last_chat)
    TextView tvLastChat;
    @BindView(R.id.tv_last_time)
    TextView tvLastTime;

    @BindView(R.id.ll_del_friend)
    LinearLayout mDeleteBtn;
    @BindView(R.id.btn_shield_confirm)
    public Button mDelConfirmBtn;
    @BindView(R.id.ll_shield_friend)
    public LinearLayout mShieldBtn;
    @BindView(R.id.ll_fun)
    LinearLayout mFunLl;
    @BindView(R.id.rl_person_content)
    public RelativeLayout mPersonRl;

    public ChatPersonHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        initEvent();
    }

    public void showName(String name){
        mNameTv.setText(name);
    }

    public void showPhoto(String photoUrl){
        ImageU.loadUserImage(photoUrl, ivAvatar);
    }

    public void showContent(String content){
        tvLastChat.setText(content);
    }

    public void showLastTime(String lastTime){
        tvLastTime.setText(lastTime);
    }

    private void initEvent(){
        mDeleteBtn.setOnClickListener(v -> {
            if(mFunLl.isShown()){
                mFunLl.setVisibility(View.GONE);
                mDelConfirmBtn.setVisibility(View.VISIBLE);
            }
        });


        ((SwipeMenuLayout)itemView).setOnCloseListener(() -> {
            if(!mFunLl.isShown()){
                mFunLl.setVisibility(View.VISIBLE);
                mDelConfirmBtn.setVisibility(View.GONE);
            }
        });
    }
}
