package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.PhotoSelectBean;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.adapter.PhotoSelectAdapter;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogChoice;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/16 23:05
 */
public class UserPhotoAlbumActivity extends BaseActivity implements PhotoSelectAdapter.onItemClickChangedListener {


    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.gv_photo)
    GridView gvPhoto;
    @BindView(R.id.btn_delete)
    Button btnDelete;

    PhotoSelectAdapter adapter;
    List<PhotoSelectBean> listPhoto = new ArrayList<>();                             //相册列表

    private boolean mIsSelectedMode = false;                                         //是否是选择模式

    DialogBuilder mDialogBuilder;                                                    //对话框制造器

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_user_photo_album;
    }


    @OnClick({R.id.btn_back, R.id.tv_action,R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_action:
                mIsSelectedMode = !mIsSelectedMode;
                if (mIsSelectedMode){
                    tvAction.setText(getString(R.string.app_action_cancel));
                    btnDelete.setVisibility(View.VISIBLE);
                }else {
                    tvAction.setText(getString(R.string.app_action_delete));
                    btnDelete.setVisibility(View.GONE);
                }

                adapter = new PhotoSelectAdapter(UserPhotoAlbumActivity.this, listPhoto,mIsSelectedMode);
                adapter.setListener(this);
                gvPhoto.setAdapter(adapter);
                break;
            //删除图片
            case R.id.btn_delete:
                showDeleteDialog();
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }


    @Override
    public void onItemClick(int index) {
        if (mIsSelectedMode){
            PhotoSelectBean photoSelectBean = listPhoto.get(index);
            photoSelectBean.isSelected = !photoSelectBean.isSelected;
            listPhoto.set(index,photoSelectBean);

            adapter.notifyDataSetChanged();

        }else {
            //TODO 选择照片
        }
    }

    @Override
    public void onItemLongClick(int index) {
        mIsSelectedMode = true;
        PhotoSelectBean photoSelectBean = listPhoto.get(index);
        photoSelectBean.isSelected = true;
        listPhoto.set(index,photoSelectBean);
        adapter = new PhotoSelectAdapter(UserPhotoAlbumActivity.this, listPhoto,mIsSelectedMode);
        adapter.setListener(this);
        gvPhoto.setAdapter(adapter);

        tvAction.setText(getString(R.string.app_action_cancel));
        btnDelete.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        //TEST
        listPhoto.add(new PhotoSelectBean("https://i02piccdn.sogoucdn.com/279af73bd9db16df",false));
        listPhoto.add(new PhotoSelectBean("https://i01piccdn.sogoucdn.com/53fdfc70e7628cd2",false));
        listPhoto.add(new PhotoSelectBean("https://i02piccdn.sogoucdn.com/348aa040619e97ee",false));
        listPhoto.add(new PhotoSelectBean("https://i04piccdn.sogoucdn.com/7127a91366b3bace",false));
        listPhoto.add(new PhotoSelectBean("https://i02piccdn.sogoucdn.com/0c2f53b2b13e3186",false));
        listPhoto.add(new PhotoSelectBean("https://i03piccdn.sogoucdn.com/282e09a25758f6ca",false));
    }

    @Override
    protected void initViews() {
        mDialogBuilder = new DialogBuilder();

        adapter = new PhotoSelectAdapter(UserPhotoAlbumActivity.this, listPhoto,mIsSelectedMode);
        gvPhoto.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }


    /**
     * 显示删除对话框
     */
    private void showDeleteDialog(){
        mDialogBuilder.showChoiceDialog(UserPhotoAlbumActivity.this,getString(R.string.auth_title_dialog_delete_photo),
                getString(R.string.app_action_delete),getString(R.string.app_action_cancel));
        mDialogBuilder.setChoiceDialogListener(new DialogChoice.onBtnClickListener() {
            @Override
            public void onConfirmBtnClick() {
                List<PhotoSelectBean> listTmp = new ArrayList<>();
                for (PhotoSelectBean bean : listPhoto){
                    if (!bean.isSelected){
                        listTmp.add(bean);
                    }
                }
                listPhoto.clear();
                listPhoto.addAll(listTmp);
                mIsSelectedMode = false;
                adapter = new PhotoSelectAdapter(UserPhotoAlbumActivity.this, listPhoto,mIsSelectedMode);
                adapter.setListener(UserPhotoAlbumActivity.this);
                gvPhoto.setAdapter(adapter);
                tvAction.setText(getString(R.string.app_action_delete));
                btnDelete.setVisibility(View.GONE);
            }

            @Override
            public void onCancelBtnClick() {

            }
        });
    }
}
