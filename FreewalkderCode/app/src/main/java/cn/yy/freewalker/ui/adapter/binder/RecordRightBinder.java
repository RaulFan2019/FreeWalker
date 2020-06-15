package cn.yy.freewalker.ui.adapter.binder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.RecordRightBean;
import cn.yy.freewalker.ui.adapter.holder.RecordRightHolder;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午9:28
 */
public class RecordRightBinder extends ItemViewBinder<RecordRightBean, RecordRightHolder> {
    @NonNull
    @Override
    protected RecordRightHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new RecordRightHolder(inflater.inflate(R.layout.item_record_right,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull RecordRightHolder recordRightHolder, @NonNull RecordRightBean recordRightBean) {
        recordRightHolder.setRightLengthTv(recordRightBean.recordLength+"");
        if (recordRightBean.isSelect){
            recordRightHolder.setSelectImg(R.drawable.btn_pick_selected);
        }else {
            recordRightHolder.setSelectImg(R.drawable.btn_pick_normal);
        }
    }
}
