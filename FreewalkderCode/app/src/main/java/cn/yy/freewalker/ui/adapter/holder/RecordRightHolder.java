package cn.yy.freewalker.ui.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午9:29
 */
public class RecordRightHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_record_length_right)
    TextView mRightLengthTv;
    @BindView(R.id.iv_record_select_right)
    ImageView mRightSelectIv;
    public RecordRightHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setRightLengthTv(String length) {
        this.mRightLengthTv.setText(length);
    }

    public void setSelectImg(int imgId){
        mRightSelectIv.setImageResource(imgId);
    }
}
