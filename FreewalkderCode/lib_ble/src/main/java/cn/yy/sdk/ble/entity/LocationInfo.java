package cn.yy.sdk.ble.entity;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/28 23:14
 */
public class LocationInfo {

    public int channel;
    public int userId;
    public int latitude;
    public int longtitude;
    public int gender;
    public int age;
    public int sex;
    public int job;
    public int height;
    public int weight;
    public String userName;

    public LocationInfo(int channel, int userId, int latitude,
                        int longtitude, int gender, int age, int sex, int job, int height, int weight, String userName) {
        this.channel = channel;
        this.userId = userId;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.gender = gender;
        this.age = age;
        this.sex = sex;
        this.job = job;
        this.height = height;
        this.weight = weight;
        this.userName = userName;
    }
}
