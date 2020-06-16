package cn.yy.freewalker.entity.model;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/16 22:52
 */
public class PhotoSelectBean {

    public String url;
    public boolean isSelected;

    public PhotoSelectBean(String url, boolean isSelected) {
        this.url = url;
        this.isSelected = isSelected;
    }
}
