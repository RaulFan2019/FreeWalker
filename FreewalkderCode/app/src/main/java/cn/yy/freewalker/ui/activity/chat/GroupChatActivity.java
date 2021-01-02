package cn.yy.freewalker.ui.activity.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
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

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import cn.yy.freewalker.data.DBDataDevice;
import cn.yy.freewalker.data.DBDataGroupChatMsg;
import cn.yy.freewalker.data.DBDataUser;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.entity.db.GroupChatMsgEntity;
import cn.yy.freewalker.entity.db.UserDbEntity;
import cn.yy.freewalker.entity.event.OnUserAvatarClickEvent;
import cn.yy.freewalker.entity.model.ChatLeftTextBean;
import cn.yy.freewalker.entity.model.ChatRightTextBean;
import cn.yy.freewalker.entity.model.ChatRoomBean;
import cn.yy.freewalker.entity.model.ChatTimeBean;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.activity.auth.UserInfoActivity;
import cn.yy.freewalker.ui.adapter.FreePagerAdapter;
import cn.yy.freewalker.ui.adapter.binder.ChatLeftTextBinder;
import cn.yy.freewalker.ui.adapter.binder.ChatRightTextBinder;
import cn.yy.freewalker.ui.adapter.binder.ChatTimeBinder;
import cn.yy.freewalker.ui.adapter.listener.OnItemListener;
import cn.yy.freewalker.ui.fragment.face.FaceInputFragment;
import cn.yy.freewalker.ui.widget.common.ToastView;
import cn.yy.freewalker.utils.ChatUiHelper;
import cn.yy.freewalker.utils.DateUtils;
import cn.yy.freewalker.utils.YLog;
import cn.yy.sdk.ble.BM;
import cn.yy.sdk.ble.array.ConnectStates;
import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.entity.LocationInfo;
import cn.yy.sdk.ble.entity.SingleChatInfo;
import cn.yy.sdk.ble.observer.ChannelListener;
import cn.yy.sdk.ble.observer.ConnectListener;
import cn.yy.sdk.ble.observer.ReceiveMsgListener;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author Raul
 * @version 1.0
 * @date 2020/6/3 22:00
 */
public class GroupChatActivity extends BaseActivity implements ConnectListener, ReceiveMsgListener, ChannelListener {

    private static final String TAG = "GroupChatActivity";

    @BindView(R.id.tv_chat_user_title)
    TextView mUserTitleTv;
    @BindView(R.id.rv_chat_list)
    RecyclerView mChatRv;

    @BindView(R.id.ll_input)
    LinearLayout llInput;
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
    @BindView(R.id.tv_channel_not_match)
    TextView tvChannelNotMatch;

    private MultiTypeAdapter mChatAdapter;
    private FreePagerAdapter mVpAdapter;

    private ArrayList<Object> mChatItems = new ArrayList<>();
    private Long lastTime = 0L;
    private UserDbEntity mUser;                                                     //用户信息
    private ChatRoomBean mRoom;                                                     //房间信息
    private BindDeviceDbEntity mDeviceDbEntity;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_chat;
    }


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
                if (mDeviceDbEntity == null ||
                        mRoom.id != (mDeviceDbEntity.lastChannel + 1)) {
                    new ToastView(GroupChatActivity.this, getString(R.string.chat_error_group_channel_not_same), -1);
                } else {
                    String msg = mInputEt.getText().toString();
                    showRightChat(msg);
                    BM.getManager().sendGroupChatMsg(mUser.userId, msg);
                    mInputEt.setText("");

                    // 隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
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
    protected void myHandleMsg(Message msg) {

    }

    /**
     * 连接事件
     *
     * @param state
     */
    @Override
    public void connectStateChange(int state) {
        updateViewByConnectState(state);
    }


    @Override
    public void receiveGroupMsg(GroupChatInfo groupChatInfo) {
        String userName = "??";
        String photoUrl = "";
        UserDbEntity userDbEntity = DBDataUser.getUserInfoByUserId(groupChatInfo.userId);
        if (userDbEntity != null) {
            userName = userDbEntity.name;
            photoUrl = UrlConfig.IMAGE_HOST + userDbEntity.avatar;
        }

        showLeftChat(groupChatInfo.userId, userName, photoUrl, groupChatInfo.content);
    }

    @Override
    public void receiveSingleMsg(SingleChatInfo singleChatInfo) {

    }

    @Override
    public void receiveLocationMsg(LocationInfo locationInfo) {

    }

    /**
     * 附近的人相关事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NearbyCardEvent(OnUserAvatarClickEvent event) {
//        startActivity(UserInfoActivity.class);
    }

    @Override
    public void switchChannelOk() {
        updateViewByConnectState(BM.getManager().getConnectState());
    }

    @Override
    protected void initData() {
        mChatAdapter = new MultiTypeAdapter();
        mChatAdapter.register(ChatTimeBean.class, new ChatTimeBinder());
        ChatLeftTextBinder leftTextBinder = new ChatLeftTextBinder();

        mRoom = (ChatRoomBean) getIntent().getExtras().get("room");
        mUser = DBDataUser.getLoginUser(GroupChatActivity.this);
        mDeviceDbEntity = DBDataDevice.findDeviceByUser(mUser.userId, BM.getManager().getConnectMac());
        //初始化群聊消息
        List<GroupChatMsgEntity> listMsg = DBDataGroupChatMsg.getAllGroupChatMsg(mUser.userId, (mRoom.id - 1));
        for (GroupChatMsgEntity chatMsgEntity : listMsg) {
            //自己的
            if (chatMsgEntity.destUserId == mUser.userId) {
                mChatItems.add(new ChatRightTextBean(chatMsgEntity.content, UrlConfig.IMAGE_HOST + mUser.avatar));
                //别人的
            } else {
                String userName = "??";
                String photoUrl = "";
                UserDbEntity userDbEntity = DBDataUser.getUserInfoByUserId(chatMsgEntity.destUserId);
                if (userDbEntity != null) {
                    userName = userDbEntity.name;
                    photoUrl = UrlConfig.IMAGE_HOST + userDbEntity.avatar;
                }
                mChatItems.add(new ChatLeftTextBean(chatMsgEntity.destUserId, userName, chatMsgEntity.content, photoUrl));
            }

        }

        leftTextBinder.setOnItemClick(new OnItemListener() {
            @Override
            public void onLongClick(View view, int pos) {

            }

            @Override
            public void onClick(View view, int pos) {
                ChatLeftTextBean leftTextBean = (ChatLeftTextBean) mChatItems.get(pos);

                Bundle bundle = new Bundle();
                bundle.putInt("destUserId", leftTextBean.userId);
                startActivity(UserInfoActivity.class, bundle);
            }
        });

        ChatRightTextBinder rightTextBinder = new ChatRightTextBinder();

        mChatAdapter.register(ChatLeftTextBean.class, leftTextBinder);
        mChatAdapter.register(ChatRightTextBean.class, rightTextBinder);

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

        mUserTitleTv.setText(mRoom.name);

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
                Editable editable = mInputEt.getText();
                int bytesLen = mInputEt.getText().toString().getBytes().length;
                int strLen = editable.length();
                if (bytesLen > 180) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, strLen - 1);
                    mInputEt.setText(newStr);
                    editable = mInputEt.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
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


    private void updateViewByConnectState(int state) {
        if (state >= ConnectStates.WORKED) {
            tvNotConnectTip.setVisibility(View.GONE);
            if ((BM.getManager().getDeviceSystemInfo().currChannel + 1) != mRoom.id) {
                tvChannelNotMatch.setVisibility(View.VISIBLE);
                llInput.setVisibility(View.GONE);
            } else {
                tvChannelNotMatch.setVisibility(View.GONE);
                llInput.setVisibility(View.VISIBLE);
            }
        } else {
            tvNotConnectTip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void doMyCreate() {
        LocalApp.getInstance().getEventBus().register(this);
        BM.getManager().registerConnectListener(this);
        BM.getManager().registerReceiveMsgListener(this);
        BM.getManager().registerChannelListener(this);
    }

    @Override
    protected void causeGC() {
        LocalApp.getInstance().getEventBus().unregister(this);
        BM.getManager().unRegisterConnectListener(this);
        BM.getManager().unRegisterReceiveMsgListener(this);
        BM.getManager().unRegisterChannelListener(this);
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
     */
    private void showLeftChat(int userId, String userName, final String photoUrl, String chatText) {
        GroupChatMsgEntity groupChatMsgEntity = new GroupChatMsgEntity(System.currentTimeMillis(), mUser.userId,
                mRoom.id - 1, userId, chatText);
        DBDataGroupChatMsg.save(groupChatMsgEntity);

        mChatItems.add(new ChatLeftTextBean(userId, userName, chatText, photoUrl));
        mChatAdapter.notifyDataSetChanged();

        mChatRv.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }


    /**
     * 显示右边的聊天
     *
     * @param chatText
     */
    private void showRightChat(String chatText) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= 60 * 1000) {
            mChatItems.add(new ChatTimeBean(DateUtils.getCurrentTime()));
        }

        GroupChatMsgEntity groupChatMsgEntity = new GroupChatMsgEntity(System.currentTimeMillis(), mUser.userId,
                mRoom.id - 1, mUser.userId, chatText);
        DBDataGroupChatMsg.save(groupChatMsgEntity);

        mChatItems.add(new ChatRightTextBean(chatText, UrlConfig.IMAGE_HOST + mUser.avatar));
        mChatAdapter.notifyDataSetChanged();

        mChatRv.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }


}
