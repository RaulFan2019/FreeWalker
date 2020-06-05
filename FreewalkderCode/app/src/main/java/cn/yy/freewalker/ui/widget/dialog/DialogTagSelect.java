package cn.yy.freewalker.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.WheelView;
import com.jacksen.taggroup.ITag;
import com.jacksen.taggroup.ITagBean;
import com.jacksen.taggroup.OnTagClickListener;
import com.jacksen.taggroup.SuperTagGroup;
import com.jacksen.taggroup.SuperTagUtil;

import java.util.ArrayList;
import java.util.Random;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/5 0:38
 */
public class DialogTagSelect {

    public Dialog mDialog;

    Context mContext;

    TextView tvTitle;
    TextView tvConfirm;
    SuperTagGroup tagGroup;

    int selectIndex = -1;

    public onConfirmListener mListener;

    public interface onConfirmListener {
        void onConfirm(int index);
    }

    /**
     * 初始化
     *
     * @param context
     */
    public DialogTagSelect(final Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_tag_select);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        this.mContext = context;

        tvTitle = mDialog.findViewById(R.id.tv_title);
        tvConfirm = mDialog.findViewById(R.id.tv_confirm);
        tagGroup = mDialog.findViewById(R.id.tag_group);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && selectIndex != -1) {
                    mListener.onConfirm(selectIndex);
                }
                mDialog.dismiss();
            }
        });
    }


    /**
     * 显示
     *
     * @param title
     */
    public void show(final String title, final ArrayList<String> listData) {
        tvTitle.setText(title);
        tagGroup.removeAllViews();
        for (int i = 0; i < listData.size(); i++) {
            ITagBean.Builder builder = new ITagBean.Builder();
            ITagBean tagBean = builder.setTag(listData.get(i))
                    .setCornerRadius(SuperTagUtil.dp2px(mContext, 17.0f))
                    .setHorizontalPadding(SuperTagUtil.dp2px(mContext, 10))
                    .create();
            tagGroup.appendTag(tagBean);
        }

        tagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public boolean onTagClick(int position, ITag tag) {
                selectIndex = position;
                return false;
            }

            @Override
            public void onSelected(SparseArray<View> selectedViews) {

            }
        });

        mDialog.show();
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    public void setListener(onConfirmListener listener) {
        this.mListener = listener;
    }

}
