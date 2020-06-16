package cn.yy.freewalker.ui.adapter.holder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.adapter.listener.OnRecordItemListener;
import cn.yy.freewalker.utils.ChatUiHelper;
import cn.yy.freewalker.utils.DensityU;

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
    @BindView(R.id.ll_record_length_left_bg)
    public LinearLayout mLeftRecordLl;
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

    public void setEditModel(boolean editModel){
        mLeftSelectIv.setVisibility(editModel?View.VISIBLE:View.GONE);
    }

    public void calRecordLayoutWidth(int length){
        ChatUiHelper.calRecordLayoutWidth(itemView.getContext(),mLeftRecordLl,length);
    }

}
