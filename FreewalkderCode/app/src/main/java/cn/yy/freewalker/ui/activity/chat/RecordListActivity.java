package cn.yy.freewalker.ui.activity.chat;

import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.RecordLeftBean;
import cn.yy.freewalker.entity.model.RecordRightBean;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.adapter.binder.RecordLeftBinder;
import cn.yy.freewalker.ui.adapter.binder.RecordRightBinder;
import cn.yy.freewalker.ui.adapter.listener.OnItemListener;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogChoice;
import cn.yy.freewalker.ui.widget.dialog.DialogSaveFile;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午8:41
 */
public class RecordListActivity extends BaseActivity {


    @BindView(R.id.tv_del_voice)
    TextView mDelVoiceTv;
    @BindView(R.id.tv_save_voice)
    TextView mSaveVoiceTv;
    @BindView(R.id.ll_operate_voice)
    LinearLayout mOperateLl;
    @BindView(R.id.rv_voice_list)
    RecyclerView mVoiceListRv;
    @BindView(R.id.tv_title_right)
    TextView mTitleRightTv;
    @BindView(R.id.tv_record_title)
    TextView tvTitle;

    private MultiTypeAdapter mAdapter;

    private ArrayList<Object> mRecordItems = new ArrayList<>();

    private ArrayList<Object> mChooseItems = new ArrayList<>();

    private DialogBuilder mBuilder;

    private String title = "";

    @OnClick({R.id.btn_back, R.id.tv_del_voice, R.id.tv_save_voice, R.id.tv_title_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_del_voice:
//                delChooseItem();
                showDelDialog(-1);
                break;
            case R.id.tv_save_voice:
//                quiteEditModel();
                showSaveDialog();
                break;
            case R.id.tv_title_right:
                chooseAll();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_list;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        mAdapter = new MultiTypeAdapter();

        RecordLeftBinder leftBinder = new RecordLeftBinder();
        leftBinder.setItemListener(new OnItemListener() {
            @Override
            public void onLongClick(View view, int pos) {
                showPopMenu(view, pos);
//                showItemMenu(view, pos);
            }

            @Override
            public void onClick(View view, int pos) {
                RecordLeftBean bean = (RecordLeftBean) mRecordItems.get(pos);
                if (bean.isSelect) {
                    bean.isSelect = false;
                    mChooseItems.remove(mRecordItems.get(pos));
                } else {
                    bean.isSelect = true;
                    mChooseItems.add(mRecordItems.get(pos));
                }
                mAdapter.notifyItemChanged(pos);

            }
        });

        RecordRightBinder rightBinder = new RecordRightBinder();
        rightBinder.setItemListener(new OnItemListener() {
            @Override
            public void onLongClick(View view, int pos) {
                showPopMenu(view, pos);
            }

            @Override
            public void onClick(View view, int pos) {
                RecordRightBean bean = (RecordRightBean) mRecordItems.get(pos);
                if (bean.isSelect) {
                    mChooseItems.remove(mRecordItems.get(pos));
                } else {
                    mChooseItems.add(mRecordItems.get(pos));
                }
                bean.isSelect = !bean.isSelect;
                mAdapter.notifyItemChanged(pos);
            }
        });

        mAdapter.register(RecordLeftBean.class, leftBinder);
        mAdapter.register(RecordRightBean.class, rightBinder);

        mRecordItems.add(new RecordLeftBean(1, 23, false));
        mRecordItems.add(new RecordRightBean(2, 30, false));
        mRecordItems.add(new RecordLeftBean(3, 40, false));
        mRecordItems.add(new RecordLeftBean(4, 59, false));
        mRecordItems.add(new RecordRightBean(5, 10, false));
        mAdapter.setItems(mRecordItems);

        title = getIntent().getExtras().getString("title");
    }

    @Override
    protected void initViews() {
        mBuilder  = new DialogBuilder();
        mVoiceListRv.setLayoutManager(new LinearLayoutManager(this));
        mVoiceListRv.setAdapter(mAdapter);

        tvTitle.setText(title);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    private void showSaveDialog(){
        mBuilder.showSaveFileDialog(this);
        mBuilder.setSaveFileDialogListener(new DialogSaveFile.onBtnClickListener() {
            @Override
            public void onConfirmBtnClick(String fileName) {
                quiteEditModel();
            }

            @Override
            public void onCancelBtnClick() {

            }
        });
    }

    private void showDelDialog(int pos){
        mBuilder.showChoiceDialog(this, getResources().getString(R.string.chat_dialog_msg_del_record),getResources().getString(R.string.chat_action_delete_file),getResources().getString(R.string.app_action_cancel));
        mBuilder.setChoiceDialogListener(new DialogChoice.onBtnClickListener() {
            @Override
            public void onConfirmBtnClick() {
                if(pos == -1){
                    delChooseItem();
                }else {
                    delSingleItem(pos);
                }
            }

            @Override
            public void onCancelBtnClick() {

            }
        });
    }

    private void showPopMenu(View v, int pos) {
//        QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
//        builder.textColor(R.attr.app_skin_common_title_text_color);
//        QMUISkinHelper.setSkinValue(textView, builder);
//        builder.release();
        QMUIPopups.quickAction(this,
                QMUIDisplayHelper.dp2px(this, 35),
                QMUIDisplayHelper.dp2px(this, 48))
                .shadow(true)
                .preferredDirection(QMUIPopup.DIRECTION_TOP)
                .bgColor(getResources().getColor(R.color.bg_base_gray))
//                .arrow(true)
                .skinManager(QMUISkinManager.defaultInstance(this))
                .edgeProtection(QMUIDisplayHelper.dp2px(this, 20))
                .addAction(new QMUIQuickAction.Action().text("删除")
                        .onClick(new QMUIQuickAction.OnClickListener() {
                    @Override
                    public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                        quickAction.dismiss();
                        showDelDialog(pos);
                    }
                }))
                .addAction(new QMUIQuickAction.Action().text("多选").onClick(new QMUIQuickAction.OnClickListener() {
                    @Override
                    public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                        quickAction.dismiss();
                        entryEditModel();
                    }
                }))
                .show(v);
    }

    private void showItemMenu(View view,int pos){
//        LinearLayout linearLayout = new LinearLayout(this);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(QMUIDisplayHelper.dp2px(this,60),QMUIDisplayHelper.dp2px(this,30));
//        linearLayout.setLayoutParams(params);
//        LayoutInflater inflater = LayoutInflater.from(this);
//       View v =  inflater.inflate(R.layout.item_popups_menu,linearLayout);
        TextView textView = new TextView(this);
        textView.setLineSpacing(QMUIDisplayHelper.dp2px(this, 4), 1.0f);
        int padding = QMUIDisplayHelper.dp2px(this, 20);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText("QMUIBasePopup 可以设置其位置以及显示和隐藏的动画");
        textView.setTextColor(ContextCompat.getColor(this,R.color.tv_error));
        textView.setBackgroundColor(ContextCompat.getColor(this,R.color.tv_mostly));
                //QMUIResHelper.getAttrColor(this, R.attr.app_skin_common_title_text_color));
//        QMUISkinValueBuilder builder = QMUISkinValueBuilder.acquire();
//        builder.background(R.drawable.icon_voice);
//        QMUISkinHelper.setSkinValue(textView,builder);
//        builder.release();
        QMUIPopups.popup(this, QMUIDisplayHelper.dp2px(this, 250))
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .view(textView)
                .skinManager(QMUISkinManager.defaultInstance(this))
                .edgeProtection(QMUIDisplayHelper.dp2px(this, 20))
                .offsetX(QMUIDisplayHelper.dp2px(this, 20))
                .offsetYIfBottom(QMUIDisplayHelper.dp2px(this, 5))
                .shadow(true)
                .arrow(true)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Toast.makeText(RecordListActivity.this, "onDismiss", Toast.LENGTH_SHORT).show();
                    }
                })
                .show(view);
    }

    /*进入多选模式*/
    private void entryEditModel() {
        for (int i = 0; i < mRecordItems.size(); i++) {
            Object item = mRecordItems.get(i);
            if (item instanceof RecordLeftBean) {
                ((RecordLeftBean) item).isEditModel = true;
            }
            if (item instanceof RecordRightBean) {
                ((RecordRightBean) item).isEditModel = true;
            }
        }
        mAdapter.notifyDataSetChanged();
        mTitleRightTv.setVisibility(View.VISIBLE);
        mOperateLl.setVisibility(View.VISIBLE);
    }

    /*退出多选模式*/
    private void quiteEditModel() {
        for (int i = 0; i < mRecordItems.size(); i++) {
            Object item = mRecordItems.get(i);
            if (item instanceof RecordLeftBean) {
                ((RecordLeftBean) item).isEditModel = false;
            }
            if (item instanceof RecordRightBean) {
                ((RecordRightBean) item).isEditModel = false;
            }
        }
        mAdapter.notifyDataSetChanged();
        mTitleRightTv.setVisibility(View.GONE);
        mOperateLl.setVisibility(View.GONE);
    }

    /*全选*/
    private void chooseAll() {
        for (int i = 0; i < mRecordItems.size(); i++) {
            Object item = mRecordItems.get(i);
            if (item instanceof RecordLeftBean) {
                ((RecordLeftBean) item).isSelect = true;
            }
            if (item instanceof RecordRightBean) {
                ((RecordRightBean) item).isSelect = true;
            }
            mChooseItems.add(item);
        }
        mAdapter.notifyDataSetChanged();
    }

    /*删除选中的Item*/
    private void delSingleItem(int pos) {
        mRecordItems.remove(pos);
        mAdapter.notifyDataSetChanged();
    }

    /*删除选中*/
    private void delChooseItem() {
        for (Object bean : mChooseItems) {
            mRecordItems.remove(bean);
        }
        mAdapter.notifyDataSetChanged();
    }
}
