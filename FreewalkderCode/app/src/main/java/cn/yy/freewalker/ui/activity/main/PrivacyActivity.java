package cn.yy.freewalker.ui.activity.main;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/5/30 15:00
 */
public class PrivacyActivity extends BaseActivity {

    public static final String PRIVACY_TYPE = "privacyType";

    public static final int PRIVACY_TYPE_USER_AGREEMENT = 1;
    public static final int PRIVACY_TYPE_PRIVACY = 2;


    private int mType;

    @BindView(R.id.tv_title_user_agreement)
    TextView tvTitleUserAgreement;                                  //用户协议标题
    @BindView(R.id.line_title_user_agreement)
    View lineTitleUserAgreement;                                    //用户协议下划线
    @BindView(R.id.tv_title_privacy)
    TextView tvTitlePrivacy;                                        //用户隐私标题
    @BindView(R.id.line_title_privacy)
    View lineTitlePrivacy;                                          //用户隐私下划线

    @BindView(R.id.ll_user_agreement)
    LinearLayout llUserAgreement;                                   //用户协议布局
    @BindView(R.id.ll_privacy)
    LinearLayout llPrivacy;                                         //用户隐私布局

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_privacy;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }


    @OnClick({R.id.btn_back, R.id.ll_title_user_agreement, R.id.ll_title_privacy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.btn_back:
                finish();
                break;
            //用户协议
            case R.id.ll_title_user_agreement:
                mType = PRIVACY_TYPE_USER_AGREEMENT;
                updateViewsByPrivacyType();
                break;
            //隐私政策
            case R.id.ll_title_privacy:
                mType = PRIVACY_TYPE_PRIVACY;
                updateViewsByPrivacyType();
                break;
        }
    }

    @Override
    protected void initData() {
        mType = getIntent().getIntExtra(PRIVACY_TYPE,PRIVACY_TYPE_PRIVACY);
    }

    @Override
    protected void initViews() {
        updateViewsByPrivacyType();
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    /**
     * 根据协议类型更新页面
     */
    private void updateViewsByPrivacyType(){
        if (mType == PRIVACY_TYPE_PRIVACY){
            tvTitlePrivacy.setTextColor(getResources().getColor(R.color.tv_accent));
            lineTitlePrivacy.setVisibility(View.VISIBLE);
            llPrivacy.setVisibility(View.VISIBLE);

            tvTitleUserAgreement.setTextColor(getResources().getColor(R.color.tv_secondly));
            llUserAgreement.setVisibility(View.GONE);
            lineTitleUserAgreement.setVisibility(View.GONE);
        }else {
            tvTitlePrivacy.setTextColor(getResources().getColor(R.color.tv_secondly));
            lineTitlePrivacy.setVisibility(View.GONE);
            llPrivacy.setVisibility(View.GONE);

            tvTitleUserAgreement.setTextColor(getResources().getColor(R.color.tv_accent));
            llUserAgreement.setVisibility(View.VISIBLE);
            lineTitleUserAgreement.setVisibility(View.VISIBLE);
        }
    }

}
