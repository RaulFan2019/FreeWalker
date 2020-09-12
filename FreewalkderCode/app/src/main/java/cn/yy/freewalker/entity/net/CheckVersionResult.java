package cn.yy.freewalker.entity.net;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/9/10 23:28
 */
public class CheckVersionResult {


    /**
     * isUpdate : true
     * versionCode : 2
     * updatedes : 我是更新内容
     * versionName : 1.0.2
     * appUrl : http://app 下载路径
     * updateFlag : true
     */

    public boolean isUpdate;
    public String versionCode;
    public String updatedes;
    public String versionName;
    public String appUrl;
    public boolean updateFlag;

    public boolean isIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdatedes() {
        return updatedes;
    }

    public void setUpdatedes(String updatedes) {
        this.updatedes = updatedes;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public boolean isUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(boolean updateFlag) {
        this.updateFlag = updateFlag;
    }
}
