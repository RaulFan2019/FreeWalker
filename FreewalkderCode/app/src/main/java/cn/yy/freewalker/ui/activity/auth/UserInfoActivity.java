package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSON;
import com.gyf.immersionbar.ImmersionBar;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.UrlConfig;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.entity.net.PhotoResult;
import cn.yy.freewalker.entity.net.UserInfoResult;
import cn.yy.freewalker.network.NetworkExceptionHelper;
import cn.yy.freewalker.network.RequestBuilder;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.chat.SingleChatActivity;
import cn.yy.freewalker.ui.widget.common.CircularImage;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.utils.DensityU;
import cn.yy.freewalker.utils.ImageU;
import cn.yy.freewalker.utils.UserInfoU;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/14 22:12
 */
public class UserInfoActivity extends BaseActivity {

    private static final String TAG = "UserInfoActivity";

    private static final int MSG_GET_USER_INFO_OK = 0x01;
    private static final int MSG_GET_USER_INFO_ERROR = 0x02;
    private static final int MSG_GET_PHOTO_OK = 0x03;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_profession)
    TextView tvProfession;
    @BindView(R.id.tv_like)
    TextView tvLike;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.iv_avatar)
    CircularImage ivAvatar;
    @BindView(R.id.card_avatar)
    CardView cardAvatar;
    @BindView(R.id.fl_user)
    FrameLayout flUser;
    @BindView(R.id.ll_photo)
    LinearLayout llPhoto;

    @BindView(R.id.img_photo_1)
    ImageView imgPhoto1;
    @BindView(R.id.img_photo_2)
    ImageView imgPhoto2;
    @BindView(R.id.img_photo_3)
    ImageView imgPhoto3;
    @BindView(R.id.img_photo_4)
    ImageView imgPhoto4;

    @BindView(R.id.cb_shield)
    CheckBox cbShield;

    private List<ImageView> listImageView = new ArrayList<>();

    /* data */
    private int mDestUserId;
    private UserInfoResult mDestUserInfo;
    private List<PhotoResult> listPhoto = new ArrayList<>();

    private UserDbEntity mUser;
    private UserDbEntity mDestUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_user_info;
    }


    @OnClick({R.id.btn_back, R.id.btn_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_chat:
                Bundle bundle = new Bundle();
                bundle.putInt("destUserId", mDestUserId);
                startActivity(SingleChatActivity.class, bundle);
                break;
        }
    }


    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_GET_USER_INFO_OK:
                updateViewsByUserInfo();
                requestUserPhoto();
                break;
            case MSG_GET_USER_INFO_ERROR:
                new ToastView(UserInfoActivity.this, getString(R.string.auth_error_get_user_info), -1);
                finish();
                break;
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
        }
    }

    @Override
    protected void initData() {
        mDestUserId = getIntent().getExtras().getInt("destUserId");
        mUser = DBDataUser.getLoginUser(UserInfoActivity.this);
        mDestUser = DBDataUser.getUserInfoByUserId(mDestUserId);

        listImageView.add(imgPhoto1);
        listImageView.add(imgPhoto2);
        listImageView.add(imgPhoto3);
        listImageView.add(imgPhoto4);
    }

    @Override
    protected void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(false)
                .init();

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) cardAvatar.getLayoutParams();
        params.leftMargin = (DensityU.getScreenWidth(UserInfoActivity.this) - DensityU.dip2px(UserInfoActivity.this, 80)) / 2;
        cardAvatar.setLayoutParams(params);

        cbShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDestUser != null){
                    mDestUser.isShield = cbShield.isChecked();
                    DBDataUser.update(mDestUser);
                }
            }
        });
    }

    @Override
    protected void doMyCreate() {
        requestUserInfo();
    }

    @Override
    protected void causeGC() {

    }


    /**
     * 请求获取用户信息
     */
    private void requestUserInfo() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.getOtherUserInfo(UserInfoActivity.this,
                        mDestUserId, mUser.token);
                x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        YLog.e(TAG, "onSuccess:" + result.msg + "," + result.data);
                        if (result.code == 200) {
                            mDestUserInfo = JSON.parseObject(result.data, UserInfoResult.class);
                            //保存用户信息
                            DBDataUser.saveOrUpdateUserInfo(mDestUserId, mDestUserInfo);
                            mDestUser = DBDataUser.getUserInfoByUserId(mDestUserId);

                            mHandler.sendEmptyMessage(MSG_GET_USER_INFO_OK);
                        } else {
                            mHandler.obtainMessage(MSG_GET_USER_INFO_ERROR,
                                    NetworkExceptionHelper.getErrorMsgByCode(UserInfoActivity.this, result.code,result.msg))
                                    .sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        YLog.e(TAG, "onError:" + ex.getMessage());
                        mHandler.obtainMessage(MSG_GET_USER_INFO_ERROR, ex.getMessage()).sendToTarget();
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
                RequestParams params = RequestBuilder.getUserPhoto(UserInfoActivity.this, mDestUserId);
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
     * 根据用户信息更新UI
     */
    private void updateViewsByUserInfo() {
        ImageU.loadUserImage(UrlConfig.IMAGE_HOST + mDestUserInfo.avatar, ivAvatar);
        tvName.setText(mDestUserInfo.nickName);

        tvAge.setText(UserInfoU.getAgeStr(mDestUserInfo.age));
        tvHeight.setText(UserInfoU.getHeightStr(mDestUserInfo.bodyLong));
        tvWeight.setText(UserInfoU.getHeightStr(mDestUserInfo.bodyWeight));
        tvGender.setText(UserInfoU.getGenderStr(UserInfoActivity.this, mDestUserInfo.gender));
        tvLike.setText(UserInfoU.getGenderOriStr(UserInfoActivity.this, mDestUserInfo.genderOri));
        tvProfession.setText(UserInfoU.getJobStr(UserInfoActivity.this, mDestUserInfo.job));

        if (mDestUser != null){
            cbShield.setChecked(mDestUser.isShield);
        }
    }
}
