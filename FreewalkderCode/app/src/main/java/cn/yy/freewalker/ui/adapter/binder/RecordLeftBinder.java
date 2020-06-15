package cn.yy.freewalker.ui.adapter.binder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.RecordLeftBean;
import cn.yy.freewalker.ui.adapter.holder.RecordLeftHolder;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午9:28
 */
public class RecordLeftBinder extends ItemViewBinder<RecordLeftBean, RecordLeftHolder> {
    @NonNull
    @Override
    protected RecordLeftHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new RecordLeftHolder(inflater.inflate(R.layout.item_record_left,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull RecordLeftHolder recordLeftHolder, @NonNull RecordLeftBean recordLeftBean) {
        recordLeftHolder.setLeftLengthTv(recordLeftBean.recordLength+"");
        if(recordLeftBean.isSelect){
            recordLeftHolder.setSelectImg(R.drawable.btn_pick_selected);
        }else {
            recordLeftHolder.setSelectImg(R.drawable.btn_pick_normal);
        }
    }
}
