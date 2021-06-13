package cn.yy.freewalker.ui.activity.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.View;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.UrlConfig;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.image.smoth.SmoothImageView;
import cn.yy.freewalker.utils.ImageU;
import cn.yy.freewalker.utils.YLog;


/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/26 11:27
 */
public class ImageShowActivity extends BaseActivity {

    private static final String TAG = "ImageShowActivity";

    @BindView(R.id.iv_photo)
    cn.yy.freewalker.ui.widget.image.smoth.SmoothImageView ivPhoto;

    String url;
    DialogBuilder mDialogBuilder;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_show;
    }


    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        url = getIntent().getExtras().getString("url");
        boolean isLocalPath = getIntent().getExtras().getBoolean("isLocalPath");
        boolean canDelete = getIntent().getExtras().getBoolean("canDelete");
        mDialogBuilder = new DialogBuilder();

        if (isLocalPath) {
            Uri uri = Uri.fromFile(new File(url));
            ivPhoto.setImageURI(uri);
        } else {
            ImageU.loadPhoto(url, ivPhoto);
        }

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

}
