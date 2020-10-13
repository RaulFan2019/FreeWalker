package cn.yy.sdk.ble.subject;

import cn.yy.sdk.ble.observer.ConnectListener;

/**
 * Created by Raul.Fan on 2017/3/29.
 */
public interface ConnectSubject {

    /**
     * 增加订阅者
     * @param observer
     */
    public void attach(ConnectListener observer);
    /**
     * 删除订阅者
     * @param observer
     */
    public void detach(ConnectListener observer);
    /**
     * 通知订阅者更新消息
     */
    public void notify(int connectState);

}
