package cn.yy.sdk.ble.subject;

import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.entity.SingleChatInfo;
import cn.yy.sdk.ble.observer.QueryMsgListener;
import cn.yy.sdk.ble.observer.ReceiveMsgListener;

/**
 * Created by Raul.Fan on 2017/3/29.
 */
public interface QueryMsgSubject {

    /**
     * 增加订阅者
     * @param observer
     */
    public void attach(QueryMsgListener observer);
    /**
     * 删除订阅者
     * @param observer
     */
    public void detach(QueryMsgListener observer);


    /**
     *  查询位置消息
     */
    public void notifyQueryLocationMsg();

}
