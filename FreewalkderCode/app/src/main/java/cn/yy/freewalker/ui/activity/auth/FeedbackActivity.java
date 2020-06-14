package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/14 21:46
 */
public class FeedbackActivity extends BaseActivity {



    /* views */
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_question_detail)
    EditText etQuestionDetail;
    @BindView(R.id.et_contact)
    EditText etContact;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_feedback;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

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

    @OnClick({R.id.btn_back, R.id.btn_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                break;
            case R.id.btn_feedback:
                break;
        }
    }
}
