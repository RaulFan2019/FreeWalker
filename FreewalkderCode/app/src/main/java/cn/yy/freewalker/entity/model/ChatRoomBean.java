package cn.yy.freewalker.entity.model;

import java.io.Serializable;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/13 下午4:14
 */
public class ChatRoomBean implements Serializable {

    public int id;
    public String name;
    public int iconRes;

    public ChatRoomBean(int id, String name, int iconRes) {
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
    }
}
