package cn.yy.freewalker.ui.activity.chat;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.R;
import cn.yy.freewalker.config.UrlConfig;
import cn.yy.freewalker.data.DBDataGroupChatMsg;
import cn.yy.freewalker.data.DBDataSingleChatMsg;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.GroupChatMsgEntity;
import cn.yy.freewalker.entity.db.SingleChatMsgEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.event.NearbyUserCartEvent;
import cn.yy.freewalker.entity.event.OnUserAvatarClickEvent;
import cn.yy.freewalker.ui.activity.auth.UserInfoActivity;
import cn.yy.freewalker.ui.adapter.binder.ChatLeftTextBinder;
import cn.yy.freewalker.ui.adapter.binder.ChatRightTextBinder;
import cn.yy.freewalker.ui.adapter.binder.ChatTimeBinder;
import cn.yy.freewalker.ui.adapter.FreePagerAdapter;
import cn.yy.freewalker.entity.model.ChatLeftTextBean;
import cn.yy.freewalker.entity.model.ChatRightTextBean;
import cn.yy.freewalker.entity.model.ChatTimeBean;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.fragment.face.FaceInputFragment;
import cn.yy.freewalker.utils.ChatUiHelper;
import cn.yy.freewalker.utils.DateUtils;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.array.ConnectStates;
import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.entity.LocationInfo;
import cn.yy.sdk.ble.entity.SingleChatInfo;
import cn.yy.sdk.ble.observer.ConnectListener;
import cn.yy.sdk.ble.observer.ReceiveMsgListener;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author Raul
 * @version 1.0
 * @date 2020/6/3 22:00
 */
public class SingleChatActivity extends BaseActivity implements ConnectListener, ReceiveMsgListener {

    private static final String TAG = "SingleChatActivity";

    /* views */
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
    @BindView(R.id.tv_not_connect_tip)
    TextView tvNotConnectTip;

    private MultiTypeAdapter mChatAdapter;
    private FreePagerAdapter mVpAdapter;

    private ArrayList<Object> mChatItems = new ArrayList<>();
    private Long lastTime = 0L;

    /* data */
    private UserDbEntity mUser;
    private int mDestUserId;
    private String mDestUserPhotoUrl;
    private String mDestUserName;

    @OnClick({R.id.btn_back, R.id.iv_input_type, R.id.iv_input_face, R.id.et_input_text, R.id.iv_input_loc, R.id.btn_send})
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
                String msg = mInputEt.getText().toString();
                showRightChat(msg);
                mInputEt.setText("");

                BM.getManager().sendSingleChatMsg(mUser.userId, mDestUserId, msg);

                SingleChatMsgEntity singleChatMsgEntity = new SingleChatMsgEntity(System.currentTimeMillis(),mUser.userId,
                        mDestUserId,msg,true);
                DBDataSingleChatMsg.save(singleChatMsgEntity);


                // 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
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
    public void connectStateChange(int state) {
        updateViewByConnectState(state);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_chat;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    /**
     * 附近的人相关事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NearbyCardEvent(OnUserAvatarClickEvent event) {

    }


    @Override
    public void receiveGroupMsg(GroupChatInfo groupChatInfo) {

    }

    @Override
    public void receiveSingleMsg(SingleChatInfo singleChatInfo) {
        if (singleChatInfo.userId == mDestUserId) {
            showLeftChat(singleChatInfo.content);
        }
    }

    @Override
    public void receiveLocationMsg(LocationInfo locationInfo) {

    }

    @Override
    protected void initData() {
        mUser = DBDataUser.getLoginUser(SingleChatActivity.this);
        mDestUserId = getIntent().getExtras().getInt("destUserId");
        YLog.e(TAG,"mDestUserId:" + mDestUserId);
        YLog.e(TAG,"mUser.userId:" + mUser.userId);

        UserDbEntity userDbEntity = DBDataUser.getUserInfoByUserId(mDestUserId);
        if (userDbEntity == null) {
            mDestUserPhotoUrl = "";
            mDestUserName = "??";
        } else {
            mDestUserPhotoUrl = UrlConfig.IMAGE_HOST + userDbEntity.avatar;
            mDestUserName = userDbEntity.name;
        }

        //初始化群聊消息
        List<SingleChatMsgEntity> listMsg = DBDataSingleChatMsg.getAllGroupChatMsg(mUser.userId, mDestUserId);
        for (SingleChatMsgEntity chatMsgEntity : listMsg) {
            //自己的
            if (chatMsgEntity.isSelf) {
                mChatItems.add(new ChatRightTextBean(chatMsgEntity.content, UrlConfig.IMAGE_HOST + mUser.avatar));
                //别人的
            } else {
                mChatItems.add(new ChatLeftTextBean(chatMsgEntity.destUserId, mDestUserName, chatMsgEntity.content, mDestUserPhotoUrl));
            }
        }

        mChatAdapter = new MultiTypeAdapter();
        mChatAdapter.register(ChatTimeBean.class, new ChatTimeBinder());
        mChatAdapter.register(ChatLeftTextBean.class, new ChatLeftTextBinder());
        mChatAdapter.register(ChatRightTextBean.class, new ChatRightTextBinder());

        mChatAdapter.setItems(mChatItems);

        FaceInputFragment inputFragment = FaceInputFragment.newInstance();
        inputFragment.setOnOutputListener(bean -> {
                    editTextShowEmoji(bean.unicode, bean.faceId);
                }
        );

        ArrayList<Fragment> fList = new ArrayList<>();
        fList.add(inputFragment);
        mVpAdapter = new FreePagerAdapter(fList, getSupportFragmentManager());
    }


    @Override
    protected void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .keyboardEnable(true) // 解决软键盘与底部输入框冲突问题
                .statusBarView(R.id.v_status_bar)
                .init();
        initChatUi();

        mUserTitleTv.setText(mDestUserName);
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
                if (mInputEt.getText().length() > 0 && !mSendBtn.isShown()) {
                    mSendBtn.setVisibility(View.VISIBLE);
                    mLocIv.setVisibility(View.GONE);
                } else if (mInputEt.getText().length() <= 0) {
                    mSendBtn.setVisibility(View.GONE);
                    mLocIv.setVisibility(View.VISIBLE);
                }
            }
        });
        updateViewByConnectState(BM.getManager().getConnectState());
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
        LocalApp.getInstance().getEventBus().register(this);
        BM.getManager().registerReceiveMsgListener(this);
        BM.getManager().registerConnectListener(this);
    }

    @Override
    protected void causeGC() {
        LocalApp.getInstance().getEventBus().unregister(this);
        BM.getManager().unRegisterReceiveMsgListener(this);
        BM.getManager().unRegisterConnectListener(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /*输入框显示表情*/
    private void editTextShowEmoji(String unicode, int faceId) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), faceId, null);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            SpannableString spannableString = new SpannableString(unicode);
            spannableString.setSpan(imageSpan, 0, unicode.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mInputEt.append(spannableString);
        }
    }

    /**
     * 显示左边的聊天
     *
     * @param chatText
     */
    private void showLeftChat(String chatText) {
        mChatItems.add(new ChatLeftTextBean(mDestUserId, "", chatText, mDestUserPhotoUrl));
        mChatAdapter.notifyDataSetChanged();

        mChatRv.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }


    /**
     * 显示右边的聊天
     *
     * @param chatText
     */
    private void showRightChat(String chatText) {
        if (System.currentTimeMillis() - lastTime >= 60 * 1000) {
            mChatItems.add(new ChatTimeBean(DateUtils.getCurrentTime()));
            lastTime = System.currentTimeMillis();
        }

        mChatItems.add(new ChatRightTextBean(chatText, UrlConfig.IMAGE_HOST + mUser.avatar));
        mChatAdapter.notifyDataSetChanged();

        mChatRv.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }

    private void updateViewByConnectState(int state) {
        if (state >= ConnectStates.WORKED) {
            tvNotConnectTip.setVisibility(View.GONE);
        } else {
            tvNotConnectTip.setVisibility(View.VISIBLE);
        }
    }

}
