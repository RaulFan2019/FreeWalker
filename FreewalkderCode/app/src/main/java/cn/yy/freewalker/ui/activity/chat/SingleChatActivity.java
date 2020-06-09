package cn.yy.freewalker.ui.activity.chat;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.adapter.ChatLeftTextBinder;
import cn.yy.freewalker.adapter.ChatRightTextBinder;
import cn.yy.freewalker.adapter.ChatTimeBinder;
import cn.yy.freewalker.adapter.FacePagerAdapter;
import cn.yy.freewalker.bean.ChatLeftTextBean;
import cn.yy.freewalker.bean.ChatRightTextBean;
import cn.yy.freewalker.bean.ChatTimeBean;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.fragment.face.FaceInputFragment;
import cn.yy.freewalker.ui.widget.common.KeyboardLayout;
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
    @BindView(R.id.vp_face_input)
    ViewPager mFaceVp;
    @BindView(R.id.iv_input_face)
    ImageView mInputTypeIv;
    @BindView(R.id.kbl_input)
    KeyboardLayout mInputKbl;

    private MultiTypeAdapter mChatAdapter;
    private FacePagerAdapter mVpAdapter;

    private boolean mShowFaceInput = false;
    private ArrayList<Object> mChatItems = new ArrayList<>();

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
                mShowFaceInput = !mShowFaceInput;
//                //输入法打开情况下
                if (mInputKbl.isKeyboardActive()) {
                    //若表情打开的
                    if (mShowFaceInput) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                        showFaceInput(true);
                    } else {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        showFaceInput(false);
                    }
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mInputEt.getApplicationWindowToken(), 0);
                    //输入法未打开
                } else {
                    if (mShowFaceInput) {
                        // 设置为不会调整大小，以便输入弹起时布局不会改变。若不设置此属性，输入法弹起时布局会闪一下
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                        showFaceInput(true);
                    } else {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        showFaceInput(false);
                    }
                }
                break;
            case R.id.et_input_text:
                mInputKbl.postDelayed(() -> {
                    mShowFaceInput = false;
                    showFaceInput(false);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }, 250);
                break;
            default:
        }
    }

    private void showFaceInput(boolean show){
        if(show) {
            mFaceLl.setVisibility(View.VISIBLE);
            mInputTypeIv.setImageResource(R.drawable.btn_keyboard);
        }else {
            mFaceLl.setVisibility(View.GONE);
            mInputTypeIv.setImageResource(R.drawable.btn_emoji);
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

        ArrayList<Fragment> fList = new ArrayList<>();
        fList.add(FaceInputFragment.newInstance());
        mVpAdapter = new FacePagerAdapter(fList,getSupportFragmentManager());

    }


    @Override
    protected void initViews() {
        // 起初的布局可自动调整大小
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mChatRv.setLayoutManager(new LinearLayoutManager(this));
        mChatRv.setAdapter(mChatAdapter);

        mFaceVp.setAdapter(mVpAdapter);
        mFaceVp.setCurrentItem(0);
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
