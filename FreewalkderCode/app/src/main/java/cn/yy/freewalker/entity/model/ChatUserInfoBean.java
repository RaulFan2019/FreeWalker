package cn.yy.freewalker.entity.model;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/6 下午9:33
 */
public class ChatUserInfoBean {
    public String gender;
    public String age;
    public String like;
    public String job;

    public ChatUserInfoBean(String gender, String age, String like, String job) {
        this.gender = gender;
        this.age = age;
        this.like = like;
        this.job = job;
    }
}
