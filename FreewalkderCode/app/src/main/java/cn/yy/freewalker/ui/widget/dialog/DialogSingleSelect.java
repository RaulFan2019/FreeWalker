package cn.yy.freewalker.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.adapter.DialogSingleSelectListAdapter;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/2 22:25
 */
public class DialogSingleSelect {

    public Dialog mDialog;

    TextView tvTitle;
    ListView lv;

    onItemClickListener mListener;

    public interface onItemClickListener {
        void onConfirmBtnClick(int pos);
    }

    /**
     * 初始化
     *
     * @param context
     */
    public DialogSingleSelect(final Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_single_select);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        tvTitle = mDialog.findViewById(R.id.tv_title);
        lv = mDialog.findViewById(R.id.lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null){
                    mListener.onConfirmBtnClick(position);
                }
                mDialog.dismiss();
            }
        });
    }

    /**
     * 显示
     * @param title
     */
    public void show(final String title , final List<String> listData){
        tvTitle.setText(title);

        DialogSingleSelectListAdapter adapter = new DialogSingleSelectListAdapter(listData);
        lv.setAdapter(adapter);

        mDialog.show();
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * 设置监听器
     *
     * @param
     */
    public void setListener(onItemClickListener listener) {
        mListener = listener;
    }

}
