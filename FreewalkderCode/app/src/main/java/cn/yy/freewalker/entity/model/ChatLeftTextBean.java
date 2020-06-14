package cn.yy.freewalker.entity.model;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/4 上午12:10
 */
public class ChatLeftTextBean {
    public String chatText;
    public String photoUrl;

    public ChatLeftTextBean(String chatText, String photoUrl) {
        this.chatText = chatText;
        this.photoUrl = photoUrl;
    }

    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
