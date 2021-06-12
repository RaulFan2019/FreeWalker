package cn.yy.freewalker.ui.activity.auth;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.FileConfig;
import cn.yy.freewalker.config.UrlConfig;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.model.PhotoSelectBean;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.entity.net.PhotoResult;
import cn.yy.freewalker.network.NetworkExceptionHelper;
import cn.yy.freewalker.network.RequestBuilder;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.common.ImageShowActivity;
import cn.yy.freewalker.ui.adapter.PhotoSelectAdapter;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogChoice;
import cn.yy.freewalker.ui.widget.dialog.DialogSaveFile;
import cn.yy.freewalker.ui.widget.dialog.DialogSingleSelect;
import cn.yy.freewalker.utils.ImageU;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/16 23:05
 */
public class UserPhotoAlbumActivity extends BaseActivity implements PhotoSelectAdapter.onItemClickChangedListener {


    private static final String TAG = "UserPhotoAlbumActivity";

    private static final int RESULT_TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CUT_PHOTO = 2;

    private static final int MSG_UPLOAD_PHOTO_OK = 0x01;
    private static final int MSG_UPLOAD_PHOTO_ERROR = 0x02;
    private static final int MSG_DELETE_PHOTO_OK = 0x03;
    private static final int MSG_DELETE_PHOTO_ERROR = 0x04;
    private static final int MSG_GET_PHOTO_OK = 0x05;

    /* view */
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.gv_photo)
    GridView gvPhoto;
    @BindView(R.id.btn_delete)
    Button btnDelete;

    DialogBuilder mDialogBuilder;                                                   //对话框制造器
    PhotoSelectAdapter adapter;

    private Uri mAvatarPhotoUri;                                                    //头像uri
    private String mAvatarPhotoPath;                                                //头像文件地址
    private File mAvatarFile;                                                       //头像文件

    List<PhotoSelectBean> listPhoto = new ArrayList<>();                             //相册列表
    private List<String> listPhotoMode = new ArrayList<>();                          //照片选择模式
    private boolean mIsSelectedMode = false;                                         //是否是选择模式


    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_user_photo_album;
    }


    @OnClick({R.id.btn_back, R.id.tv_action, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_action:
                mIsSelectedMode = !mIsSelectedMode;
                if (mIsSelectedMode) {
                    tvAction.setText(getString(R.string.app_action_cancel));
                    btnDelete.setVisibility(View.VISIBLE);
                } else {
                    tvAction.setText(getString(R.string.app_action_delete));
                    btnDelete.setVisibility(View.GONE);
                }

                adapter = new PhotoSelectAdapter(UserPhotoAlbumActivity.this, listPhoto, mIsSelectedMode);
                adapter.setListener(this);
                gvPhoto.setAdapter(adapter);

                YLog.e(TAG,"url:" + listPhoto.get(0).photo.imgUrl);
                YLog.e(TAG,"url:" + listPhoto.get(1).photo.imgUrl);
                Bundle bundle = new Bundle();
                bundle.putString("url", listPhoto.get(0).photo.imgUrl);
                bundle.putBoolean("isLocalPath", false);
                bundle.putBoolean("canDelte", false);
                startActivity(ImageShowActivity.class, bundle);

                break;
            //删除图片
            case R.id.btn_delete:
                showDeleteDialog();
                break;
        }
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
                        requestUploadFile();
                    }
                }
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_GET_PHOTO_OK:
                adapter.notifyDataSetChanged();
                break;
            case MSG_UPLOAD_PHOTO_OK:
                new ToastView(UserPhotoAlbumActivity.this, getString(R.string.app_tip_upload_ok), -1);
                break;
            case MSG_UPLOAD_PHOTO_ERROR:
                new ToastView(UserPhotoAlbumActivity.this, (String) msg.obj, -1);
                break;
            case MSG_DELETE_PHOTO_OK:
                requestUserPhoto();
                tvAction.setText(getString(R.string.app_action_delete));
                btnDelete.setVisibility(View.GONE);
                break;
            case MSG_DELETE_PHOTO_ERROR:
                new ToastView(UserPhotoAlbumActivity.this, (String) msg.obj, -1);
                break;
        }
    }


    @Override
    public void onItemClick(int index) {
        if (mIsSelectedMode) {
            PhotoSelectBean photoSelectBean = listPhoto.get(index);
            photoSelectBean.isSelected = !photoSelectBean.isSelected;
            listPhoto.set(index, photoSelectBean);
            adapter.notifyDataSetChanged();
        } else {
            //点击加号
            if (index == listPhoto.size()) {
                addPhoto();
            } else {
                //TODO?
            }
        }
    }

    @Override
    public void onItemLongClick(int index) {
    }

    @Override
    protected void initData() {
        //photoMode
        listPhotoMode.add(getString(R.string.auth_dialog_tx_improve_photo_mode_camera));
        listPhotoMode.add(getString(R.string.auth_dialog_tx_improve_photo_mode_storage));
        listPhotoMode.add(getString(R.string.app_action_cancel));

        List<PhotoResult> listPhotoOri = (List<PhotoResult>) getIntent().getSerializableExtra("photo");

        for (PhotoResult photo : listPhotoOri) {
            listPhoto.add(new PhotoSelectBean(photo, false));
        }
    }

    @Override
    protected void initViews() {
        mDialogBuilder = new DialogBuilder();

        adapter = new PhotoSelectAdapter(UserPhotoAlbumActivity.this, listPhoto, mIsSelectedMode);
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
    private void showDeleteDialog() {
        mDialogBuilder.showChoiceDialog(UserPhotoAlbumActivity.this, getString(R.string.auth_title_dialog_delete_photo),
                getString(R.string.app_action_delete), getString(R.string.app_action_cancel));
        mDialogBuilder.setChoiceDialogListener(new DialogChoice.onBtnClickListener() {
            @Override
            public void onConfirmBtnClick() {
                requestDeletePhoto();
            }

            @Override
            public void onCancelBtnClick() {

            }
        });
    }

    private void requestDeletePhoto() {
        List<PhotoSelectBean> listDelete = new ArrayList<>();
        for (PhotoSelectBean bean : listPhoto) {
            if (bean.isSelected) {
                listDelete.add(bean);
            }
        }

        String ids = "";
        for (int i = 0; i < listDelete.size(); i++) {
            if (i == 0){
                ids += listDelete.get(i).photo.id;
            }else {
                ids += "," + listDelete.get(i).photo.id;
            }
        }

        String finalIds = ids;
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.deleteUserPhoto(UserPhotoAlbumActivity.this, finalIds);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            listPhoto.clear();
                            listPhoto.removeAll(listDelete);
                            mIsSelectedMode = false;
                            mHandler.sendEmptyMessage(MSG_DELETE_PHOTO_OK);
                        } else {
                            mHandler.obtainMessage(MSG_DELETE_PHOTO_ERROR,
                                    NetworkExceptionHelper.getErrorMsgByCode(UserPhotoAlbumActivity.this, result.code, result.msg))
                                    .sendToTarget();

                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mHandler.obtainMessage(MSG_DELETE_PHOTO_ERROR, ex.getMessage()).sendToTarget();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });


    }


    /**
     * 改变头像
     */
    private void addPhoto() {
        mDialogBuilder.showSingleSelectDialog(UserPhotoAlbumActivity.this,
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

        AndPermission.with(UserPhotoAlbumActivity.this)
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
                        new ToastView(UserPhotoAlbumActivity.this,
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
                        new ToastView(UserPhotoAlbumActivity.this,
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
                    mAvatarPhotoUri = FileProvider.getUriForFile(UserPhotoAlbumActivity.this, "cn.yy.freewalker.fileProvider", mAvatarFile);
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
     * 上传文件
     */
    private void requestUploadFile() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams("http://admin.yytalkie.com/prod-api/res/upFile");
                params.setMultipart(true);
                params.addBodyParameter("file", new File(mAvatarPhotoPath));

                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            String url = result.data;
                            requestAddUserPhoto(url);
                        } else {
                            mHandler.obtainMessage(MSG_UPLOAD_PHOTO_ERROR, result.msg).sendToTarget();
                        }


                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mHandler.obtainMessage(MSG_UPLOAD_PHOTO_ERROR, ex.getMessage()).sendToTarget();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });

            }
        });
    }


    /**
     * 增加用户图片
     */
    private void requestAddUserPhoto(final String url) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.addUserPhoto(UserPhotoAlbumActivity.this, url);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            requestUserPhoto();
                            mHandler.sendEmptyMessage(MSG_UPLOAD_PHOTO_OK);
                        } else {
                            mHandler.obtainMessage(MSG_UPLOAD_PHOTO_ERROR, NetworkExceptionHelper
                                    .getErrorMsgByCode(UserPhotoAlbumActivity.this, result.code, result.msg))
                                    .sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mHandler.obtainMessage(MSG_UPLOAD_PHOTO_ERROR, ex.getMessage()).sendToTarget();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

    /**
     * 获取用户照片
     */
    private void requestUserPhoto() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.getUserPhoto(UserPhotoAlbumActivity.this,
                        DBDataUser.getLoginUser(UserPhotoAlbumActivity.this).userId);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            listPhoto.clear();
                            List<PhotoResult> list = JSON.parseArray(result.data, PhotoResult.class);
                            if (list != null) {
                                for (PhotoResult photo : list) {
                                    listPhoto.add(new PhotoSelectBean(photo, false));
                                }
                            }
                            mHandler.sendEmptyMessage(MSG_GET_PHOTO_OK);
                        } else {
                            NetworkExceptionHelper.getErrorMsgByCode(UserPhotoAlbumActivity.this, result.code, result.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

}
