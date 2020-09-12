package cn.yy.freewalker.ui.activity.auth;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.network.RequestBuilder;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.widget.common.ToastView;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/14 21:46
 */
public class FeedbackActivity extends BaseActivity implements TextWatcher {

    private static final int MSG_FEED_BACK_OK = 0x01;
    private static final int MSG_FEED_BACK_ERROR = 0x02;

    /* views */
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_question_detail)
    EditText etQuestionDetail;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.btn_feedback)
    Button btnFeedback;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_feedback;
    }


    @OnClick({R.id.btn_back, R.id.btn_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_feedback:
                requestFeedback();
                break;
        }
    }

    /**
     * 输入框内容发生变化
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etQuestionDetail.getText().toString().isEmpty()
                || etPhone.getText().toString().isEmpty()
                || etContact.getText().toString().isEmpty()) {
            btnFeedback.setEnabled(false);
        } else {
            btnFeedback.setEnabled(true);
        }
    }


    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what){
            case MSG_FEED_BACK_OK:
                new ToastView(FeedbackActivity.this,getString(R.string.auth_toast_feedback_ok),-1);
                break;
            case MSG_FEED_BACK_ERROR:
                new ToastView(FeedbackActivity.this, (String)msg.obj, -1);
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        etContact.addTextChangedListener(this);
        etPhone.addTextChangedListener(this);
        etQuestionDetail.addTextChangedListener(this);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    /**
     * 请求建议与反馈
     */
    private void requestFeedback(){
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestBuilder.feedback(FeedbackActivity.this,etContact.getText().toString(),
                        etPhone.getText().toString(), etQuestionDetail.getText().toString());
                mCancelable = x.http().post(params, new Callback.CommonCallback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (result.code == 200){
                            mHandler.sendEmptyMessage(MSG_FEED_BACK_OK);
                        }else {
                            mHandler.obtainMessage(MSG_FEED_BACK_ERROR,result.msg).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        mHandler.obtainMessage(MSG_FEED_BACK_ERROR,ex.getMessage()).sendToTarget();
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
