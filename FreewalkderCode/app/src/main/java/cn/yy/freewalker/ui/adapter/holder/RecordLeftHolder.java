package cn.yy.freewalker.ui.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午9:29
 */
public class RecordLeftHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_record_length_left)
    TextView mLeftLengthTv;
    @BindView(R.id.iv_record_select_left)
    ImageView mLeftSelectIv;
    public RecordLeftHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setLeftLengthTv(String length) {
        this.mLeftLengthTv.setText(length);
    }

    public void setSelectImg(int imgId){
        mLeftSelectIv.setImageResource(imgId);
    }
}
