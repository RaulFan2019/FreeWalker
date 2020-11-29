package cn.yy.freewalker.entity.model;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/4 上午12:10
 */
public class ChatLeftTextBean {


    public int userId;
    public String chatText;
    public String photoUrl;
    public String userName;

    public ChatLeftTextBean(int userId, String userName, String chatText, String photoUrl) {
        this.userId = userId;
        this.userName = userName;
        this.chatText = chatText;
        this.photoUrl = photoUrl;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
