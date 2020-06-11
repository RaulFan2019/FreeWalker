package cn.yy.freewalker.ui.activity.main;

import android.content.Context;
import android.os.FileObserver;
import android.os.Message;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTextChanged;
import cn.yy.freewalker.R;
import cn.yy.freewalker.adapter.ChatFaceBinder;
import cn.yy.freewalker.adapter.ChatLeftTextBinder;
import cn.yy.freewalker.adapter.ChatRightTextBinder;
import cn.yy.freewalker.adapter.ChatTimeBinder;
import cn.yy.freewalker.adapter.ChatUserInfoBinder;
import cn.yy.freewalker.adapter.FacePagerAdapter;
import cn.yy.freewalker.bean.ChatFaceBean;
import cn.yy.freewalker.bean.ChatLeftTextBean;
import cn.yy.freewalker.bean.ChatRightTextBean;
import cn.yy.freewalker.bean.ChatTimeBean;
import cn.yy.freewalker.bean.ChatUserInfoBean;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.fragment.face.FaceInputFragment;
import cn.yy.freewalker.ui.widget.common.KeyboardLayout;
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

    private MultiTypeAdapter mChatAdapter;
    private FacePagerAdapter mVpAdapter;

    private ArrayList<Object> mChatItems = new ArrayList<>();
    private ChatUiHelper mHelper;

    @OnClick({R.id.btn_back, R.id.iv_input_type, R.id.iv_input_face, R.id.et_input_text})
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
            default:
        }
    }

    @OnLongClick({R.id.tv_input_speak})
    public void onLongClick(View view){
        switch (view.getId()){
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
        mChatItems.add(new ChatRightTextBean("你好[呲牙]", ""));
        mChatItems.add(new ChatLeftTextBean("你好你好你好你好你好你好你好你好[微笑][微笑][微笑][微笑][微笑]", ""));
        mChatItems.add(new ChatRightTextBean("你好[呲牙]", ""));
        mChatItems.add(new ChatLeftTextBean("你好你好你好你好你好你好你好你好[微笑][微笑][微笑][微笑][微笑]", ""));
        mChatItems.add(new ChatRightTextBean("你好[呲牙]", ""));
        mChatItems.add(new ChatLeftTextBean("你好[微笑][微笑][微笑][微笑][微笑]", ""));
        mChatItems.add(new ChatRightTextBean("你好[呲牙]", ""));
        mChatItems.add(new ChatLeftTextBean("你好[微笑][微笑][微笑][微笑][微笑]", ""));
        mChatItems.add(new ChatRightTextBean("你好[呲牙]", ""));
        mChatItems.add(new ChatLeftTextBean("你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好[微笑][微笑][微笑][微笑][微笑]", ""));
        mChatItems.add(new ChatRightTextBean("你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好[呲牙]", ""));
        mChatAdapter.setItems(mChatItems);

        FaceInputFragment inputFragment = FaceInputFragment.newInstance();
        inputFragment.setOnOutputListener(bean -> {

                    mInputEt.append(bean.unicode);
                }
        );

        ArrayList<Fragment> fList = new ArrayList<>();
        fList.add(inputFragment);
        mVpAdapter = new FacePagerAdapter(fList, getSupportFragmentManager());
    }


    @Override
    protected void initViews() {

        initChatUI();

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
            }
        });
    }

    private void initChatUI(){
        mHelper = ChatUiHelper.with(this)
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

    private void initFaceView(int height) {
        ViewGroup.LayoutParams lp = mFaceLl.getLayoutParams();
        lp.height = height;
        mFaceLl.setLayoutParams(lp);
    }

}
