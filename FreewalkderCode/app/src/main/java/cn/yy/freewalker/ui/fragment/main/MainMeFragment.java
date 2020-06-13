package cn.yy.freewalker.ui.fragment.main;

import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.common.CircularImage;
import cn.yy.freewalker.utils.DensityU;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 0:03
 */
public class MainMeFragment extends BaseFragment {


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
            R.id.ll_clear, R.id.ll_about, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_avatar:
                break;
            case R.id.ll_photo:
                break;
            case R.id.ll_record:
                break;
            case R.id.ll_feedback:
                break;
            case R.id.ll_clear:
                break;
            case R.id.ll_about:
                break;
            case R.id.btn_logout:
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initParams() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) cardAvatar.getLayoutParams();
        params.leftMargin = (DensityU.getScreenWidth(getActivity()) - DensityU.dip2px(getActivity(),80)) / 2;
        cardAvatar.setLayoutParams(params);
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

}
