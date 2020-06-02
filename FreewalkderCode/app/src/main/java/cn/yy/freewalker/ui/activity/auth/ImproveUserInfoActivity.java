package cn.yy.freewalker.ui.activity.auth;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.FileConfig;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.common.CircularImage;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogSingleSelect;


/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/1 22:13
 */
public class ImproveUserInfoActivity extends BaseActivity {


    /* contains */
    private static final String TAG = "ImproveUserInfoActivity";

    private static final int RESULT_TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CUT_PHOTO = 2;


    @BindView(R.id.img_avatar)
    CircularImage imgAvatar;                                    //头像
    @BindView(R.id.et_nickname)
    EditText etNickname;                                        //昵称输入框
    @BindView(R.id.et_sex)
    EditText etSex;                                             //性别输入框
    @BindView(R.id.et_age)
    EditText etAge;                                             //年龄输入框
    @BindView(R.id.et_like)
    EditText etLike;                                            //喜好输入框
    @BindView(R.id.et_profession)
    EditText etProfession;                                      //职业输入框
    @BindView(R.id.et_height)
    EditText etHeight;                                          //身高输入框
    @BindView(R.id.et_weight)
    EditText etWeight;                                          //体重输入框
    @BindView(R.id.btn_submit)
    Button btnSubmit;                                           //提交按钮

    DialogBuilder mDialogBuilder;

    /* data */
    private List<String> listPhotoMode = new ArrayList<>();     //照片选择模式

    private Uri mAvatarPhotoUri;
    private String mAvatarPhotoPath;
    private File mAvatarFile;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_improve_user_info;
    }


    @OnClick({R.id.btn_avatar, R.id.ll_sex, R.id.ll_age, R.id.ll_like, R.id.ll_profession,
            R.id.ll_height, R.id.ll_weight, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //点击头像
            case R.id.btn_avatar:
                onAvatarClick();
                break;
            case R.id.ll_sex:
                break;
            case R.id.ll_age:
                break;
            case R.id.ll_like:
                break;
            case R.id.ll_profession:
                break;
            case R.id.ll_height:
                break;
            case R.id.ll_weight:
                break;
            case R.id.btn_submit:
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_TAKE_PICTURE:
                if (mAvatarFile != null && mAvatarFile.exists())
                    startPhotoZoom(mAvatarPhotoUri);
                break;
            case RESULT_LOAD_IMAGE:
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        startPhotoZoom(uri);
                    }
                }
                break;
            case RESULT_CUT_PHOTO:
                if (resultCode == RESULT_OK && null != data) {// 裁剪返回
                    if (mAvatarPhotoPath != null && mAvatarPhotoPath.length() != 0) {
                        Bitmap bitmap = BitmapFactory.decodeFile(mAvatarPhotoPath);
                        //给头像设置图片源
                        imgAvatar.setImageBitmap(bitmap);

                    }
                }
                break;
        }
    }

    @Override
    protected void initData() {
        mDialogBuilder = new DialogBuilder();
        //photoMode
        listPhotoMode.add(getString(R.string.auth_dialog_tx_improve_photo_mode_camera));
        listPhotoMode.add(getString(R.string.auth_dialog_tx_improve_photo_mode_storage));
        listPhotoMode.add(getString(R.string.app_action_cancel));
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {
        listPhotoMode.clear();
    }

    /**
     * 改变头像
     */
    private void onAvatarClick() {
        mDialogBuilder.showSingleSelectDialog(ImproveUserInfoActivity.this,
                getString(R.string.auth_dialog_title_improve_select_photo_mode),
                listPhotoMode);
        mDialogBuilder.setSingleSelectDialogListener(new DialogSingleSelect.onItemClickListener() {
            @Override
            public void onConfirmBtnClick(int pos) {
                //相机拍摄
                if (pos == 0) {
                    checkPermissionByPhotoCamera();
                    //相册选择
                } else if (pos == 1) {
                    checkPermissionByPhotoStorage();
                }
            }
        });
    }


    /**
     * 检查进入相册需要的权限
     */
    private void checkPermissionByPhotoStorage() {
        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        new ToastView(ImproveUserInfoActivity.this,
                                getString(R.string.auth_error_permission_photo_mode_storage),
                                -1);
                    }
                })
                .start();
    }


    /**
     * 检查拍照需要的权限
     */
    private void checkPermissionByPhotoCamera() {
        final String[] permissions = {Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        doCamera();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        new ToastView(ImproveUserInfoActivity.this,
                                getString(R.string.auth_error_permission_photo_mode_camera),
                                -1);
                    }
                })
                .start();
    }


    /**
     * 拍照
     */
    private void doCamera(){

        try {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String sdcardState = Environment.getExternalStorageState();
            String sdcardPathDir = FileConfig.PHOTO_PATH;
            mAvatarFile = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                // 文件夹是否存在
                File fileDir = new File(sdcardPathDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                // 是否有headImg文件
                long l = System.currentTimeMillis();
                mAvatarFile = new File(sdcardPathDir + l + ".JPEG");
            }

            if (mAvatarFile != null) {
                mAvatarPhotoPath = mAvatarFile.getPath();
                mAvatarPhotoUri = Uri.fromFile(mAvatarFile);
                if (Build.VERSION.SDK_INT >= 24) {
                    mAvatarPhotoUri = FileProvider.getUriForFile(this,"cn.yy.freewalker.fileProvider", mAvatarFile);
                } else {
                    mAvatarPhotoUri = Uri.fromFile(mAvatarFile);
                }
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mAvatarPhotoUri);

                startActivityForResult(openCameraIntent, RESULT_TAKE_PICTURE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
//        intent.putExtra("return-data", true);

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String address = sDateFormat.format(new Date());

        Uri imageUri = Uri.parse(FileConfig.cutFileUri + address + ".JPEG");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 不启用人脸识别
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("return-data", false);
        intent.putExtra("fileurl", FileConfig.PHOTO_PATH + address + ".JPEG");

        mAvatarPhotoPath = FileConfig.PHOTO_PATH + address + ".JPEG";

        startActivityForResult(intent, RESULT_CUT_PHOTO);
    }

}
