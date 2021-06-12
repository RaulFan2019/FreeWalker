package cn.yy.freewalker.entity.net;

import java.io.Serializable;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2021/6/12 18:04
 */
public class CheckDeviceVersion implements Serializable {


    /**
     * devUrl : https://blechat.oss-cn-hangzhou.aliyuncs.com//main/35151dbc5eba450d8a8b9ce8eacaf1efhead.zip
     * devType : 1
     * versionName : 1.2
     * isUpdate : true
     * versionCode : 258
     */

    public String devUrl;
    public String devType;
    public String versionName;
    public boolean isUpdate;
    public int versionCode;
}
