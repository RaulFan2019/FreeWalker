package cn.yy.sdk.ble.entity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/24 22:11
 */
public class GroupChatInfo {

    public int userId;
    public String content;

    public GroupChatInfo() {
    }

    public GroupChatInfo(int userId, String content) {
        this.userId = userId;
        this.content = content;
    }
}
