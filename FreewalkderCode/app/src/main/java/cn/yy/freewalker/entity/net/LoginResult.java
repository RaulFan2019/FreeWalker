package cn.yy.freewalker.entity.net;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/4 0:19
 */
public class LoginResult {


    /**
     * id : 3
     * token : token 凭证串
     */

    public int id;
    public String token;
    /**
     * createdAt : 2020-09-04 08:48:31
     * deleted : 0
     * roleId : 0
     * tel : 15221798774
     * userName : 15221798774
     */

    public String createdAt;
    public int deleted;
    public int roleId;
    public String tel;
    public String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
