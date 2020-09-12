package cn.yy.freewalker.entity.net;

import java.io.Serializable;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/11 0:20
 */
public class PhotoResult implements Serializable {

    
    /**
     * imgUrl : 图片路径
     */

    public String imgUrl;
    /**
     * createdAt : 2020-09-11 08:37:47
     * deleted : 0
     * id : 10
     * ownerId : 10
     */

    public String createdAt;
    public int deleted;
    public int id;
    public int ownerId;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
