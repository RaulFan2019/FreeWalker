package cn.yy.freewalker.entity.model;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/13 下午4:14
 */
public class ChatPersonBean {


    public String photoUrl;
    public String name;
    public int id;
    public String lastContent;
    public String lastTime;

    public ChatPersonBean(String name, String photoUrl, int id, String lastContent, String lastTime) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.id = id;
        this.lastContent = lastContent;
        this.lastTime = lastTime;
    }
}
