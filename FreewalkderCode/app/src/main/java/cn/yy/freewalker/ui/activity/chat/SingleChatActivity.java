package cn.yy.freewalker.ui.activity.chat;

import android.graphics.drawable.Drawable;
import android.os.Message;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.adapter.binder.ChatLeftTextBinder;
import cn.yy.freewalker.ui.adapter.binder.ChatRightTextBinder;
import cn.yy.freewalker.ui.adapter.binder.ChatTimeBinder;
import cn.yy.freewalker.ui.adapter.FreePagerAdapter;
import cn.yy.freewalker.bean.ChatLeftTextBean;
import cn.yy.freewalker.bean.ChatRightTextBean;
import cn.yy.freewalker.bean.ChatTimeBean;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.fragment.face.FaceInputFragment;
import cn.yy.freewalker.utils.ChatUiHelper;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/3 22:00
 */
public class SingleChatActivity extends BaseActivity {
    @BindView(R.id.tv_chat_user_title)
    TextView mUserTitleTv;
    @BindView(R.id.rv_chat_list)
    RecyclerView mChatRv;
    @BindView(R.id.et_input_text)
    EditText mInputEt;
    @BindView(R.id.tv_input_speak)
    TextView mSpeakTv;
    @BindView(R.id.ll_face_view)
    LinearLayout mFaceLl;
    @BindView(R.id.ll_chat_bottom)
    LinearLayout mBottonLl;
    @BindView(R.id.vp_face_input)
    ViewPager mFaceVp;
    @BindView(R.id.iv_input_face)
    ImageView mInputTypeIv;
    @BindView(R.id.ll_chat_content)
    LinearLayout mContentLl;
    @BindView(R.id.btn_send)
    Button mSendBtn;
    @BindView(R.id.iv_input_loc)
    ImageView mLocIv;

    private MultiTypeAdapter mChatAdapter;
    private FreePagerAdapter mVpAdapter;

    private ArrayList<Object> mChatItems = new ArrayList<>();
    private Long lastTime = 0L;

    @OnClick({R.id.btn_back, R.id.iv_input_type, R.id.iv_input_face, R.id.et_input_text,R.id.iv_input_loc,R.id.btn_send})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.iv_input_type:
                mSpeakTv.setVisibility(mSpeakTv.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.iv_input_face:

                break;
            case R.id.et_input_text:

                break;
            case R.id.iv_input_loc:

                break;
            case R.id.btn_send:
                    showRightChat(mInputEt.getText().toString());
                    mInputEt.setText("");
                break;
            default:
        }
    }

    @OnLongClick({R.id.tv_input_speak})
    public void onLongClick(View view) {
        switch (view.getId()) {
            case R.id.tv_input_speak:

                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_chat;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        mChatAdapter = new MultiTypeAdapter();
        mChatAdapter.register(ChatTimeBean.class, new ChatTimeBinder());
        mChatAdapter.register(ChatLeftTextBean.class, new ChatLeftTextBinder());
        mChatAdapter.register(ChatRightTextBean.class, new ChatRightTextBinder());

        mChatItems.add(new ChatTimeBean("12:18"));
        mChatItems.add(new ChatLeftTextBean("你好[微笑][微笑][微笑][微笑][微笑]", ""));

        mChatAdapter.setItems(mChatItems);

        FaceInputFragment inputFragment = FaceInputFragment.newInstance();
        inputFragment.setOnOutputListener(bean -> {
                    editTextShowEmoji(bean.unicode,bean.faceId);
                }
        );

        ArrayList<Fragment> fList = new ArrayList<>();
        fList.add(inputFragment);
        mVpAdapter = new FreePagerAdapter(fList, getSupportFragmentManager());
    }


    @Override
    protected void initViews() {

        initChatUi();

        mChatRv.setLayoutManager(new LinearLayoutManager(this));
        mChatRv.setAdapter(mChatAdapter);

        mFaceVp.setAdapter(mVpAdapter);
        mFaceVp.setCurrentItem(0);

        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mInputEt.getText().length() > 0 && !mSendBtn.isShown()){
                    mSendBtn.setVisibility(View.VISIBLE);
                    mLocIv.setVisibility(View.GONE);
                }else if(mInputEt.getText().length() <=0){
                    mSendBtn.setVisibility(View.GONE);
                    mLocIv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initChatUi() {
       ChatUiHelper.with(this)
                .bindEditText(mInputEt)
                .bindContentLayout(mContentLl)
                .bindBottomLayout(mBottonLl)
                .bindEmojiLayout(mFaceLl)
                .bindToEmojiButton(mInputTypeIv);
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /*输入框显示表情*/
    private void editTextShowEmoji(String unicode,int faceId) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), faceId, null);
        if(drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(drawable,ImageSpan.ALIGN_BOTTOM);
            SpannableString spannableString = new SpannableString(unicode);
            spannableString.setSpan(imageSpan,0,unicode.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mInputEt.append(spannableString);
        }
    }

    private void showLeftChat(String chatText){
        mChatItems.add(new ChatLeftTextBean(chatText, ""));
        mChatAdapter.notifyDataSetChanged();
    }

    private void showRightChat(String chatText){
        if(System.currentTimeMillis() - lastTime >= 60 * 1000){
            mChatItems.add(new ChatTimeBean("00:00"));//TODO
        }

        mChatItems.add(new ChatRightTextBean(chatText,""));
        mChatAdapter.notifyDataSetChanged();
    }
}
