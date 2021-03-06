package cn.yy.sdk.ble.subject;

import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.entity.LocationInfo;
import cn.yy.sdk.ble.entity.SingleChatInfo;
import cn.yy.sdk.ble.observer.ChannelListener;
import cn.yy.sdk.ble.observer.ReceiveMsgListener;

/**
 * Created by Raul.Fan on 2017/3/29.
 */
public interface ReceiveMsgSubject {

    /**
     * 增加订阅者
     *
     * @param observer
     */
    public void attach(ReceiveMsgListener observer);

    /**
     * 删除订阅者
     *
     * @param observer
     */
    public void detach(ReceiveMsgListener observer);


    /**
     * 通知订阅者更新  群消息
     */
    public void notifyReceiveGroupMsg(GroupChatInfo groupChatInfo);

    /**
     * 通知订阅者更新 单人消息
     */
    public void notifyReceiveSingleMsg(SingleChatInfo groupChatInfo);

    /**
     *
     * @param locationInfo
     */
    public void notifyReceiveLocationMsg(LocationInfo locationInfo);
}
