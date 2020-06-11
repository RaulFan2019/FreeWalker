package cn.yy.freewalker.ui.fragment.main;

import android.os.Message;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.event.NearbyUserCartEvent;
import cn.yy.freewalker.entity.net.UserInfoResult;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.common.CircularImage;
import cn.yy.freewalker.utils.ImageU;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/11 22:30
 */
public class MainNearbyUserCardFragment extends BaseFragment {


    @BindView(R.id.iv_avatar)
    CircularImage ivAvatar;
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

    /* 构造函数 */
    public static MainNearbyUserCardFragment newInstance() {
        MainNearbyUserCardFragment fragment = new MainNearbyUserCardFragment();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_nearby_user_card;
    }

    @OnClick({R.id.btn_shield, R.id.btn_chat, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_shield:
                //TODO
                break;
            case R.id.btn_chat:
                //TODO
                break;
            case R.id.btn_close:
                LocalApp.getInstance().getEventBus().post(new NearbyUserCartEvent(NearbyUserCartEvent.CLOSE,null));
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initParams() {

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

    public void updateViews(UserInfoResult user) {

        ImageU.loadUserImage(user.avatar, ivAvatar);

        tvName.setText(user.name);
        tvAge.setText(user.age);
        tvHeight.setText(user.height);
        tvWeight.setText(user.weight);
        tvGender.setText(user.gander);
        tvLike.setText(user.like);
        tvProfession.setText(user.profession);
    }


}
