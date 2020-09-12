package cn.yy.freewalker.entity.net;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/11 22:31
 */
public class UserInfoResult {


    /**
     * genderOri : 0
     * createdAt : 2020-09-04 08:56:26
     * deleted : 0
     * gender : 0
     * nickName : 
     * bodyLong : 3
     * id : 11
     * avatar : 
     * ownerId : 10
     * job : 4
     * bodyWeight : 5
     * age : 5
     */

    public int id;
    public String createdAt;
    public int deleted;
    public int ownerId;
    public int genderOri;
    public int gender;
    public String nickName;
    public int bodyLong;
    public String avatar;
    public int job;
    public int bodyWeight;
    public int age;

    public UserInfoResult(){

    }

    public UserInfoResult(int id, String createdAt, int deleted, int ownerId, int genderOri,
                          int gender, String nickName, int bodyLong, String avatar,
                          int job, int bodyWeight, int age) {
        this.id = id;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.ownerId = ownerId;
        this.genderOri = genderOri;
        this.gender = gender;
        this.nickName = nickName;
        this.bodyLong = bodyLong;
        this.avatar = avatar;
        this.job = job;
        this.bodyWeight = bodyWeight;
        this.age = age;
    }

    public UserInfoResult(int genderOri, int gender, String nickName, int bodyLong,
                          String avatar, int job, int bodyWeight, int age) {
        this.genderOri = genderOri;
        this.gender = gender;
        this.nickName = nickName;
        this.bodyLong = bodyLong;
        this.avatar = avatar;
        this.job = job;
        this.bodyWeight = bodyWeight;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getGenderOri() {
        return genderOri;
    }

    public void setGenderOri(int genderOri) {
        this.genderOri = genderOri;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getBodyLong() {
        return bodyLong;
    }

    public void setBodyLong(int bodyLong) {
        this.bodyLong = bodyLong;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public int getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(int bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
