package cn.yy.freewalker.ui.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.net.UserInfoResult;
import cn.yy.freewalker.utils.ImageU;
import cn.yy.sdk.ble.entity.LocationInfo;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/11 0:29
 */
public class AmapNearbyUserView extends FrameLayout {

    Context mContext;

    LinearLayout llBg;
    CircularImage vAvatar;
    TextView tvName;

    public AmapNearbyUserView(@NonNull Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_amap_nearby_user, this, true);

        initWidget();
    }

    public AmapNearbyUserView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_amap_nearby_user, this, true);
        initWidget();
    }

    private void initWidget() {
        llBg = findViewById(R.id.ll_card);
//        vSex = findViewById(R.id.v_sex);
        vAvatar = findViewById(R.id.img_avatar);
        tvName = findViewById(R.id.tv_name);
    }

    public void bindView(int sex, String name, String avatar) {
        if (sex == 1) {
            llBg.setBackgroundResource(R.drawable.bg_nearby_map_marker_male);
        } else {
            llBg.setBackgroundResource(R.drawable.bg_nearby_map_marker_female);
        }
        tvName.setText(name);
        vAvatar.setImageResource(R.drawable.avatar_default);
    }

    public void bindView(UserInfoResult user) {
        ImageU.loadUserImage(user.avatar, vAvatar);
        tvName.setText(user.nickName);
        if (user.gender == 0) {
            llBg.setBackgroundResource(R.drawable.bg_nearby_map_marker_male);
        } else {
            llBg.setBackgroundResource(R.drawable.bg_nearby_map_marker_female);
        }
    }


    public void bindView(LocationInfo locationInfo){
        ImageU.loadUserImage("", vAvatar);
        tvName.setText(locationInfo.userName);
        if (locationInfo.gender == 0) {
            llBg.setBackgroundResource(R.drawable.bg_nearby_map_marker_male);
        } else {
            llBg.setBackgroundResource(R.drawable.bg_nearby_map_marker_female);
        }
    }

}
