package cn.yy.freewalker.ui.fragment.main;

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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
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
import cn.yy.freewalker.config.UrlConfig;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.data.SPDataUser;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.entity.net.PhotoResult;
import cn.yy.freewalker.network.NetworkExceptionHelper;
import cn.yy.freewalker.network.RequestBuilder;
import cn.yy.freewalker.ui.activity.auth.FeedbackActivity;
import cn.yy.freewalker.ui.activity.auth.UserPhotoAlbumActivity;
import cn.yy.freewalker.ui.activity.auth.UserSettingsActivity;
import cn.yy.freewalker.ui.activity.chat.RecordSelectChannelActivity;
import cn.yy.freewalker.ui.activity.main.AboutActivity;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.common.CircularImage;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.ui.widget.dialog.DialogBuilder;
import cn.yy.freewalker.ui.widget.dialog.DialogSingleSelect;
import cn.yy.freewalker.utils.AppU;
import cn.yy.freewalker.utils.DensityU;
import cn.yy.freewalker.utils.ImageU;
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

    private static final int MSG_GET_PHOTO_OK = 0x01;
    private static final int MSG_UPLOAD_PHOTO_OK = 0x02;
    private static final int MSG_UPLOAD_PHOTO_ERROR = 0x03;


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
    @BindView(R.id.img_photo_2)
    ImageView imgPhoto2;
    @BindView(R.id.img_photo_3)
    ImageView imgPhoto3;
    @BindView(R.id.img_photo_4)
    ImageView imgPhoto4;

    DialogBuilder mDialogBuilder;

    /* data */
    private Uri mAvatarPhotoUri;                                //头像uri
    private String mAvatarPhotoPath;                            //头像文件地址
    private File mAvatarFile;                                   //头像文件

    private List<String> listPhotoMode = new ArrayList<>();     //照片选择模式
    private List<PhotoResult> listPhoto = new ArrayList<>();

    private UserDbEntity mUser;

    private List<ImageView> listImageView = new ArrayList<>();

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
            R.id.ll_clear, R.id.ll_about, R.id.btn_logout, R.id.ll_add_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //点击头像
            case R.id.card_avatar:
                startActivity(UserSettingsActivity.class);
                break;
            case R.id.ll_photo:
                Bundle bundle = new Bundle();
                bundle.putSerializable("photo", (Serializable) listPhoto);
                bundle.putInt("self", 1);
                startActivity(UserPhotoAlbumActivity.class, bundle);
                break;
            //录音
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
                startActivity(AboutActivity.class);
                break;
            case R.id.btn_logout:
                AppU.jumpToLogin(getActivity());
                break;
            //增加照片
            case R.id.ll_add_photo:
                addPhoto();
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_GET_PHOTO_OK:
                for (int i = 0; i < 4; i++) {
                    if (listPhoto.size() > i) {
                        listImageView.get(i).setVisibility(View.VISIBLE);
                        ImageU.loadPhoto(UrlConfig.IMAGE_HOST + listPhoto.get(i).imgUrl, listImageView.get(i));
                    } else {
                        listImageView.get(i).setVisibility(View.GONE);
                    }
                }
                break;
            case MSG_UPLOAD_PHOTO_OK:
                new ToastView(getActivity(), getString(R.string.app_tip_upload_ok), -1);
                break;
            case MSG_UPLOAD_PHOTO_ERROR:
                new ToastView(getActivity(), (String) msg.obj, -1);
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
    protected void initParams() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) cardAvatar.getLayoutParams();
        params.leftMargin = (DensityU.getScreenWidth(getActivity()) - DensityU.dip2px(getActivity(), 80)) / 2;
        cardAvatar.setLayoutParams(params);

        listImageView.add(imgPhoto1);
        listImageView.add(imgPhoto2);
        listImageView.add(imgPhoto3);
        listImageView.add(imgPhoto4);

        cbLoc.setChecked(SPDataUser.getIsShare(getActivity()));
        cbLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPDataUser.setIsShareLoc(getActivity(), cbLoc.isChecked());
            }
        });

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
        mUser = DBDataUser.getLoginUser(getActivity());

        ImageU.loadUserImage(UrlConfig.IMAGE_HOST + mUser.avatar, ivAvatar);
        tvName.setText(mUser.name);

        requestUserPhoto();
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
                        YLog.e(TAG, "onGranted");
                        doCamera();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        YLog.e(TAG, "onAction");
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
        YLog.e(TAG, "doCamera");

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


    /**
     * 获取用户照片
     */
    private void requestUserPhoto() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.getUserPhoto(getActivity(), DBDataUser.getLoginUser(getActivity()).userId);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        YLog.e(TAG, "requestUserPhoto onSuccess:" + result.data);
                        if (result.code == 200) {
                            listPhoto.clear();
                            List<PhotoResult> list = JSON.parseArray(result.data, PhotoResult.class);
                            if (list != null) {
                                listPhoto.addAll(list);
                            }
                            mHandler.sendEmptyMessage(MSG_GET_PHOTO_OK);
                        } else {
                            NetworkExceptionHelper.getErrorMsgByCode(getActivity(), result.code, result.msg);
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
                RequestParams params = RequestBuilder.addUserPhoto(getActivity(), url);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200) {
                            requestUserPhoto();
                            mHandler.sendEmptyMessage(MSG_UPLOAD_PHOTO_OK);
                        } else {
                            mHandler.obtainMessage(MSG_UPLOAD_PHOTO_ERROR, NetworkExceptionHelper.getErrorMsgByCode(getActivity(), result.code, result.msg)).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        YLog.e(TAG, "requestAddUserPhoto onError:" + ex.getMessage());
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

}
