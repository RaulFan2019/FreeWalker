package cn.yy.freewalker.ui.activity.auth;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.FileConfig;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.main.MainActivity;
import cn.yy.freewalker.ui.widget.common.CircularImage;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogPickView;
import cn.yy.freewalker.ui.widget.dialog.DialogSingleSelect;
import cn.yy.freewalker.ui.widget.dialog.DialogTagSelect;


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
    private ArrayList<String> listAgeSelect = new ArrayList<>();//年龄选择列表
    private List<String> listSexSelect = new ArrayList<>();     //性别选择

    private List<String> listLikeSelect = new ArrayList<>();     //喜欢选择
    private ArrayList<String> listProfessionSelect = new ArrayList<>();//职业选择
    private ArrayList<String> listHeightSelect = new ArrayList<>();//身高选择列表
    private ArrayList<String> listWeightSelect = new ArrayList<>();//体重选择列表

    private Uri mAvatarPhotoUri;                                //头像uri
    private String mAvatarPhotoPath;                            //头像文件地址
    private File mAvatarFile;                                   //头像文件

    private String mNickName;                                   //昵称
    private String mSex;                                        //性别
    private String mAge;                                        //年龄
    private String mLike;                                       //喜欢
    private String mProfession;                                 //职业
    private String mHeight;                                     //身高
    private String mWeight;                                     //体重

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_improve_user_info;
    }


    @OnClick({R.id.btn_avatar, R.id.v_sex, R.id.v_age, R.id.v_like, R.id.v_profession,
            R.id.v_height, R.id.v_weight, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //点击头像
            case R.id.btn_avatar:
                onAvatarClick();
                break;
            //选择性别
            case R.id.v_sex:
                onSexSelectClick();
                break;
            case R.id.v_age:
                onAgeSelectClick();
                break;
            case R.id.v_like:
                onLikeSelectClick();
                break;
            case R.id.v_profession:
                onProfessionSelectClick();
                break;
            case R.id.v_height:
                onHeightSelectClick();
                break;
            case R.id.v_weight:
                onWeightSelectClick();
                break;
            case R.id.btn_submit:
                startActivity(MainActivity.class);
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
                        inputIsComplete();
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
        //sex
        listSexSelect.add(getString(R.string.auth_tx_sex_male));
        listSexSelect.add(getString(R.string.auth_tx_sex_female));
        listSexSelect.add(getString(R.string.auth_tx_sex_other));

        //age
        for (int i = 1; i < 100; i++) {
            listAgeSelect.add(i + "");
        }
        //like
        listLikeSelect.add(getString(R.string.auth_tx_like_male));
        listLikeSelect.add(getString(R.string.auth_tx_like_female));
        listLikeSelect.add(getString(R.string.auth_tx_like_all));
        listLikeSelect.add(getString(R.string.auth_tx_like_other));

        //职业选择
        listProfessionSelect.add(getString(R.string.auth_tx_profession_1));
        listProfessionSelect.add(getString(R.string.auth_tx_profession_2));
        listProfessionSelect.add(getString(R.string.auth_tx_profession_3));
        listProfessionSelect.add(getString(R.string.auth_tx_profession_4));
        listProfessionSelect.add(getString(R.string.auth_tx_profession_5));
        listProfessionSelect.add(getString(R.string.auth_tx_profession_6));
        listProfessionSelect.add(getString(R.string.auth_tx_profession_7));
        listProfessionSelect.add(getString(R.string.auth_tx_profession_8));
        listProfessionSelect.add(getString(R.string.auth_tx_profession_9));
        listProfessionSelect.add(getString(R.string.auth_tx_profession_10));


        //height
        listHeightSelect.add("150 ~ 160");
        listHeightSelect.add("160 ~ 170");
        listHeightSelect.add("170 ~ 180");
        listHeightSelect.add("180 ~ 190");
        listHeightSelect.add("190 ~ 200");
        //weight
        listWeightSelect.add("30 ~ 40");
        listWeightSelect.add("40 ~ 50");
        listWeightSelect.add("50 ~ 60");
        listWeightSelect.add("60 ~ 70");
        listWeightSelect.add("70 ~ 80");
        listWeightSelect.add("80 ~ 90");
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
     * 选择性别
     */
    private void onSexSelectClick() {
        mDialogBuilder.showSingleSelectDialog(ImproveUserInfoActivity.this,
                getString(R.string.auth_dialog_title_improve_select_sex),
                listSexSelect);
        mDialogBuilder.setSingleSelectDialogListener(new DialogSingleSelect.onItemClickListener() {
            @Override
            public void onConfirmBtnClick(int pos) {
                mSex = listSexSelect.get(pos);
                etSex.setText(mSex);
                inputIsComplete();
            }
        });
    }


    /**
     * 选择年龄
     */
    private void onAgeSelectClick() {
        mDialogBuilder.showPickViewDialog(ImproveUserInfoActivity.this,
                getString(R.string.auth_dialog_title_improve_select_age), listAgeSelect, 20);
        mDialogBuilder.setPickViewDialogListener(new DialogPickView.onConfirmListener() {
            @Override
            public void onConfirm(int index) {
                mAge = listAgeSelect.get(index);
                etAge.setText(mAge);
                inputIsComplete();
            }
        });
    }


    /**
     * 选择职业
     */
    private void onLikeSelectClick() {
        mDialogBuilder.showSingleSelectDialog(ImproveUserInfoActivity.this,
                getString(R.string.auth_dialog_title_improve_select_like),
                listLikeSelect);
        mDialogBuilder.setSingleSelectDialogListener(new DialogSingleSelect.onItemClickListener() {
            @Override
            public void onConfirmBtnClick(int pos) {
                mLike = listLikeSelect.get(pos);
                etLike.setText(mLike);
                inputIsComplete();
            }
        });
    }

    /**
     * 选择喜欢
     */
    private void onProfessionSelectClick() {
        mDialogBuilder.showTagSelectDialog(ImproveUserInfoActivity.this,
                getString(R.string.auth_dialog_title_improve_select_profession),
                listProfessionSelect);
        mDialogBuilder.setTagSelectDialogListener(new DialogTagSelect.onConfirmListener() {
            @Override
            public void onConfirm(int index) {
                mProfession = listProfessionSelect.get(index);
                etProfession.setText(mProfession);
                inputIsComplete();
            }
        });
    }


    /**
     * 选择身高
     */
    private void onHeightSelectClick() {
        mDialogBuilder.showPickViewDialog(ImproveUserInfoActivity.this,
                getString(R.string.auth_dialog_title_improve_select_height), listHeightSelect, 2);
        mDialogBuilder.setPickViewDialogListener(new DialogPickView.onConfirmListener() {
            @Override
            public void onConfirm(int index) {
                mHeight = listHeightSelect.get(index);
                etHeight.setText(mHeight);
                inputIsComplete();
            }
        });
    }

    /**
     * 选择体重
     */
    private void onWeightSelectClick() {
        mDialogBuilder.showPickViewDialog(ImproveUserInfoActivity.this,
                getString(R.string.auth_dialog_title_improve_select_weight), listWeightSelect, 3);
        mDialogBuilder.setPickViewDialogListener(new DialogPickView.onConfirmListener() {
            @Override
            public void onConfirm(int index) {
                mWeight = listWeightSelect.get(index);
                etWeight.setText(mWeight);
                inputIsComplete();
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
    private void doCamera() {

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
                    mAvatarPhotoUri = FileProvider.getUriForFile(this, "cn.yy.freewalker.fileProvider", mAvatarFile);
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


    /**
     * 检查是否输入完全
     */
    private void inputIsComplete() {

        mNickName = etNickname.getText().toString();

        if (mAvatarPhotoPath != null
                && mNickName != null
                && mSex != null
                && mAge != null
                && mLike != null
                && mProfession != null
                && mHeight != null
                && mWeight != null) {
            btnSubmit.setEnabled(true);
        } else {
            btnSubmit.setEnabled(false);
        }
    }
}
