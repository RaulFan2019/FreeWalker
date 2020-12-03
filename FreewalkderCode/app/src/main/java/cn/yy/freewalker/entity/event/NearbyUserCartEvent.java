package cn.yy.freewalker.entity.event;

import cn.yy.freewalker.entity.net.UserInfoResult;
import cn.yy.sdk.ble.entity.LocationInfo;

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
    public LocationInfo locationInfo;
//    public UserInfoResult user;

    public NearbyUserCartEvent() {
    }

    public NearbyUserCartEvent(int type, LocationInfo locationInfo) {
        this.type = type;
        this.locationInfo = locationInfo;
    }
}
