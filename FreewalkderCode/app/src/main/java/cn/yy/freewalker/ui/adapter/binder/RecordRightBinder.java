package cn.yy.freewalker.ui.adapter.binder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.RecordRightBean;
import cn.yy.freewalker.ui.adapter.holder.RecordRightHolder;
import cn.yy.freewalker.ui.adapter.listener.OnRecordItemListener;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午9:28
 */
public class RecordRightBinder extends ItemViewBinder<RecordRightBean, RecordRightHolder> {
    private OnRecordItemListener listener;
    @NonNull
    @Override
    protected RecordRightHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new RecordRightHolder(inflater.inflate(R.layout.item_record_right,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull RecordRightHolder recordRightHolder, @NonNull RecordRightBean recordRightBean) {
        recordRightHolder.setRightLengthTv(recordRightBean.recordLength+"");
        recordRightHolder.setEditModel(recordRightBean.isEditModel);
        if(recordRightBean.isEditModel) {
            if (recordRightBean.isSelect) {
                recordRightHolder.setSelectImg(R.drawable.btn_pick_selected);
            } else {
                recordRightHolder.setSelectImg(R.drawable.btn_pick_normal);
            }
        }
        recordRightHolder.calRecordLayoutWidth(recordRightBean.recordLength);
        if(listener != null) {
            recordRightHolder.mRightRecordLl.setOnClickListener(v->listener.onClick(v,getPosition(recordRightHolder)));
            recordRightHolder.mRightRecordLl.setOnLongClickListener(v -> {
                listener.onLongClick(v,getPosition(recordRightHolder));
                return false;
            });
        }
    }

    public void setItemListener(OnRecordItemListener listener){
        this.listener = listener;
    }

}
