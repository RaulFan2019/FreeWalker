package cn.yy.sdk.ble.entity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/24 22:11
 */
public class SingleChatInfo {

    public int userId;
    public int destUserId;
    public String content;

    public SingleChatInfo() {
    }

    public SingleChatInfo(int userId, int destUserId, String content) {
        this.userId = userId;
        this.destUserId = destUserId;
        this.content = content;
    }
}
