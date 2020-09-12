package cn.yy.freewalker.entity.model;

import cn.yy.freewalker.entity.net.PhotoResult;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/16 22:52
 */
public class PhotoSelectBean {

    public PhotoResult photo;
    public boolean isSelected;

    public PhotoSelectBean(PhotoResult photo, boolean isSelected) {
        this.photo = photo;
        this.isSelected = isSelected;
    }
}
