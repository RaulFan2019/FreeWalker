package cn.yy.freewalker.entity.event;

import cn.yy.freewalker.entity.net.UserInfoResult;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/11 23:13
 */
public class NearbyUserCartEvent {

    public static final int SHOW = 1;
    public static final int CLOSE = 2;
    public static final int SHIELD = 3;
    public static final int CHAT = 4;

    public int type;
    public UserInfoResult user;

    public NearbyUserCartEvent() {
    }

    public NearbyUserCartEvent(int type, UserInfoResult user) {
        this.type = type;
        this.user = user;
    }
}
