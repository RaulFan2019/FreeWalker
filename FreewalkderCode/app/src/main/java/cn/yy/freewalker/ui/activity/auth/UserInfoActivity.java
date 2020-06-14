package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.common.CircularImage;
import cn.yy.freewalker.utils.DensityU;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/14 22:12
 */
public class UserInfoActivity extends BaseActivity {


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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_user_info;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) cardAvatar.getLayoutParams();
        params.leftMargin = (DensityU.getScreenWidth(UserInfoActivity.this) - DensityU.dip2px(UserInfoActivity.this,80)) / 2;
        cardAvatar.setLayoutParams(params);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back, R.id.btn_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                break;
            case R.id.btn_chat:
                break;
        }
    }
}
