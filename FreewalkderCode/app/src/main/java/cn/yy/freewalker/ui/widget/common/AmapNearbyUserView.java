package cn.yy.freewalker.ui.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/11 0:29
 */
public class AmapNearbyUserView extends FrameLayout {

    Context mContext;

    View vSex;
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
        vSex = findViewById(R.id.v_sex);
        vAvatar = findViewById(R.id.img_avatar);
        tvName = findViewById(R.id.tv_name);
    }

    public void bindView(int sex, String name, String avatar) {
        if (sex == 1){
            vSex.setBackgroundResource(R.drawable.icon_male);
        }else {
            vSex.setBackgroundResource(R.drawable.icon_female);
        }
        tvName.setText(name);
        vAvatar.setImageResource(R.drawable.avatar_default);
    }

}
