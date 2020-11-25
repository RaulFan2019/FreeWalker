package cn.yy.freewalker.entity.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import cn.yy.freewalker.entity.net.LoginResult;
import cn.yy.freewalker.entity.net.UserInfoResult;


/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/5 14:26
 */
@Table(name = "user")
public class UserDbEntity {

    @Column(name = "userId", isId = true, autoGen = false)
    public int userId;
    @Column(name = "token")
    public String token;
    @Column(name = "avatar")
    public String avatar;
    @Column(name = "name")
    public String name;
    @Column(name = "genderIndex")
    public int genderIndex;
    @Column(name = "genderOriIndex")
    public int genderOriIndex;
    @Column(name = "ageIndex")
    public int ageIndex;
    @Column(name = "professionIndex")
    public int professionIndex;
    @Column(name = "heightIndex")
    public int heightIndex;
    @Column(name = "weightIndex")
    public int weightIndex;

    @Column(name = "phone")
    public String phone;

    public UserDbEntity() {
    }


    public UserDbEntity(int userId) {
        this.userId = userId;
        this.avatar = "";
        this.name = "";
    }


    public UserDbEntity(final LoginResult loginResult, final UserInfoResult userInfoResult, final String phone){
        this.userId = loginResult.id;
        this.token = loginResult.token;

        this.avatar = userInfoResult.avatar;
        this.name = userInfoResult.nickName;
        this.genderIndex = userInfoResult.gender;
        this.genderOriIndex = userInfoResult.genderOri;
        this.ageIndex = userInfoResult.age;
        this.professionIndex = userInfoResult.job;
        this.heightIndex = userInfoResult.bodyLong;
        this.weightIndex = userInfoResult.bodyWeight;
        this.phone = phone;
    }


    /**
     * 更新用户信息
     * @param loginResult
     * @param userInfoResult
     */
    public void update(final LoginResult loginResult, final UserInfoResult userInfoResult){
        this.token = loginResult.token;

        this.avatar = userInfoResult.avatar;
        this.name = userInfoResult.nickName;
        this.genderIndex = userInfoResult.gender;
        this.genderOriIndex = userInfoResult.genderOri;
        this.ageIndex = userInfoResult.age;
        this.professionIndex = userInfoResult.job;
        this.heightIndex = userInfoResult.bodyLong;
        this.weightIndex = userInfoResult.bodyWeight;
    }

}
