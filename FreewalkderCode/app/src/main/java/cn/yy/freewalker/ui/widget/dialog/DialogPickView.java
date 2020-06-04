package cn.yy.freewalker.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.Wheel3DView;
import com.cncoderx.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/4 19:52
 */
public class DialogPickView {

    public Dialog mDialog;

    TextView tvTitle;
    TextView tvConfirm;
    Wheel3DView wheel3DView;

    int selectIndex;

    public onConfirmListener mListener;

    public interface onConfirmListener {
        void onConfirm(int index);
    }

    /**
     * 初始化
     *
     * @param context
     */
    public DialogPickView(final Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_pick_view);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        tvTitle = mDialog.findViewById(R.id.tv_title);
        tvConfirm = mDialog.findViewById(R.id.tv_confirm);
        wheel3DView = mDialog.findViewById(R.id.wheel3d);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onConfirm(selectIndex);
                }
                mDialog.dismiss();
            }
        });
    }


    /**
     * 显示
     * @param title
     */
    public void show(final String title , final ArrayList<String> listData, final int defaultIndex){
        tvTitle.setText(title);
        wheel3DView.setEntries(listData);
        wheel3DView.setCurrentIndex(defaultIndex);
        selectIndex = defaultIndex;

        wheel3DView.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                selectIndex = newIndex;
            }
        });

        mDialog.show();
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    public void setListener(onConfirmListener listener){
        this.mListener = listener;
    }

}
