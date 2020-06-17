package cn.yy.freewalker.ui.fragment.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.FileConfig;
import cn.yy.freewalker.ui.activity.auth.FeedbackActivity;
import cn.yy.freewalker.ui.activity.auth.ImproveUserInfoActivity;
import cn.yy.freewalker.ui.activity.auth.UserPhotoAlbumActivity;
import cn.yy.freewalker.ui.activity.auth.UserSettingsActivity;
import cn.yy.freewalker.ui.activity.chat.RecordSelectChannelActivity;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.common.CircularImage;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogSingleSelect;
import cn.yy.freewalker.utils.AppU;
import cn.yy.freewalker.utils.DensityU;
import cn.yy.freewalker.utils.YLog;

import static android.app.Activity.RESULT_OK;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 0:03
 */
public class MainMeFragment extends BaseFragment {

    private static final String TAG = "MainMeFragment";

    private static final int RESULT_TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CUT_PHOTO = 2;


    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.fl_user)
    FrameLayout flUser;
    @BindView(R.id.iv_avatar)
    CircularImage ivAvatar;
    @BindView(R.id.card_avatar)
    CardView cardAvatar;
    @BindView(R.id.cb_loc)
    CheckBox cbLoc;
    @BindView(R.id.img_photo_1)
    ImageView imgPhoto1;

    DialogBuilder mDialogBuilder;

    /* data */
    private Uri mAvatarPhotoUri;                                //头像uri
    private String mAvatarPhotoPath;                            //头像文件地址
    private File mAvatarFile;                                   //头像文件

    private List<String> listPhotoMode = new ArrayList<>();     //照片选择模式

    /* 构造函数 */
    public static MainMeFragment newInstance() {
        MainMeFragment fragment = new MainMeFragment();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_me;
    }


    @OnClick({R.id.card_avatar, R.id.ll_photo, R.id.ll_record, R.id.ll_feedback,
            R.id.ll_clear, R.id.ll_about, R.id.btn_logout,R.id.ll_add_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //点击头像
            case R.id.card_avatar:
                startActivity(UserSettingsActivity.class);
                break;
            case R.id.ll_photo:
                startActivity(UserPhotoAlbumActivity.class);
                break;
            case R.id.ll_record:
                startActivity(RecordSelectChannelActivity.class);
                break;
            //建议与反馈
            case R.id.ll_feedback:
                startActivity(FeedbackActivity.class);
                break;
            case R.id.ll_clear:
                //TODO
                break;
            case R.id.ll_about:
                break;
            case R.id.btn_logout:
                AppU.jumpToLogin(getActivity());
                break;
            case R.id.ll_add_photo:
                addPhoto();
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                        imgPhoto1.setImageBitmap(bitmap);
                    }
                }
                break;
        }
    }

    @Override
    protected void initParams() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) cardAvatar.getLayoutParams();
        params.leftMargin = (DensityU.getScreenWidth(getActivity()) - DensityU.dip2px(getActivity(),80)) / 2;
        cardAvatar.setLayoutParams(params);

        mDialogBuilder = new DialogBuilder();
        //photoMode
        listPhotoMode.add(getString(R.string.auth_dialog_tx_improve_photo_mode_camera));
        listPhotoMode.add(getString(R.string.auth_dialog_tx_improve_photo_mode_storage));
        listPhotoMode.add(getString(R.string.app_action_cancel));

    }

    @Override
    protected void causeGC() {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInVisible() {

    }


    /**
     * 改变头像
     */
    private void addPhoto() {
        mDialogBuilder.showSingleSelectDialog(getActivity(),
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
     * 检查拍照需要的权限
     */
    private void checkPermissionByPhotoCamera() {

        final String[] permissions = {Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        AndPermission.with(getActivity())
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        YLog.e(TAG,"onGranted");
                        doCamera();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        YLog.e(TAG,"onAction");
                        new ToastView(getActivity(),
                                getString(R.string.auth_error_permission_photo_mode_camera),
                                -1);
                    }
                })
                .start();
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
                        new ToastView(getActivity(),
                                getString(R.string.auth_error_permission_photo_mode_storage),
                                -1);
                    }
                })
                .start();
    }

    /**
     * 拍照
     */
    private void doCamera() {
        YLog.e(TAG,"doCamera");

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
                    mAvatarPhotoUri = FileProvider.getUriForFile(getActivity(), "cn.yy.freewalker.fileProvider", mAvatarFile);
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
