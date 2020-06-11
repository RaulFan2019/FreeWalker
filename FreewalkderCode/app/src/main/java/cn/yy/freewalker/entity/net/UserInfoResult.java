package cn.yy.freewalker.entity.net;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/11 22:31
 */
public class UserInfoResult {

    public String avatar;
    public String name;
    public String gander;
    public String age;
    public String profession;
    public String like;
    public String height;
    public String weight;

    public UserInfoResult(String avatar, String name, String gander,
                          String age, String profession, String like, String height, String weight) {
        this.avatar = avatar;
        this.name = name;
        this.gander = gander;
        this.age = age;
        this.profession = profession;
        this.like = like;
        this.height = height;
        this.weight = weight;
    }
}
