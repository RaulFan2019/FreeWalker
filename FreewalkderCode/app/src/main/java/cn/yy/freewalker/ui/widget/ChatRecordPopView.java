package cn.yy.freewalker.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/17 23:23
 */
public class ChatRecordPopView extends LinearLayout {

    Context mContext;

    TextView tvDelete;
    TextView tvSelect;

    OnBtnClick mListener;

    public interface OnBtnClick{
        void onDelete();
        void onSelect();
    }

    public ChatRecordPopView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_chat_record_pop_view, this, true);

        initWidget();
    }

    public ChatRecordPopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_chat_record_pop_view, this, true);

        initWidget();
    }


    private void initWidget(){
        tvDelete = findViewById(R.id.tv_delete);
        tvSelect = findViewById(R.id.tv_select);

        tvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onDelete();
                }
            }
        });

        tvSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onSelect();
                }
            }
        });
    }

    public void setListener(OnBtnClick listener){
        this.mListener = listener;
    }
}
