package cn.yy.freewalker.ui.fragment.main;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.R;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.event.NearbyUserCartEvent;
import cn.yy.freewalker.entity.net.UserInfoResult;
import cn.yy.freewalker.ui.activity.chat.SingleChatActivity;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.common.CircularImage;
import cn.yy.freewalker.utils.ImageU;
import cn.yy.freewalker.utils.UserInfoU;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.entity.LocationInfo;

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

    /* data */
    private int mDestUserId;
    private int mChannel;

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
                BM.getManager().setChannel(mChannel, 5, "");

                Bundle bundle = new Bundle();
                bundle.putInt("destUserId", mDestUserId);
                startActivity(SingleChatActivity.class, bundle);
                break;
            case R.id.btn_close:
                LocalApp.getInstance().getEventBus().post(new NearbyUserCartEvent(NearbyUserCartEvent.CLOSE, null));
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

        tvName.setText(user.nickName);
        tvAge.setText(UserInfoU.getAgeStr(user.age));
        tvHeight.setText(UserInfoU.getHeightStr(user.bodyLong));
        tvWeight.setText(UserInfoU.getHeightStr(user.bodyWeight));
        tvGender.setText(UserInfoU.getGenderStr(getActivity(), user.gender));
        tvLike.setText(UserInfoU.getGenderOriStr(getActivity(), user.genderOri));
        tvProfession.setText(UserInfoU.getJobStr(getActivity(), user.job));
    }

    public void updateViews(LocationInfo user) {
        mChannel = user.channel;
        mDestUserId = user.userId;
        ImageU.loadUserImage("", ivAvatar);

        tvName.setText(user.userName);
        tvAge.setText(UserInfoU.getAgeStr(user.age));
        tvHeight.setText(UserInfoU.getHeightStr(user.height));
        tvWeight.setText(UserInfoU.getHeightStr(user.weight));
        tvGender.setText(UserInfoU.getGenderStr(getActivity(), user.gender));
        tvLike.setText(UserInfoU.getGenderOriStr(getActivity(), user.sex));
        tvProfession.setText(UserInfoU.getJobStr(getActivity(), user.job));

        DBDataUser.saveOrUpdateUserInfo(user.userId, user);
    }

}
