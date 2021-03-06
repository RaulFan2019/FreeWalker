package cn.yy.sdk.ble.observer;

import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.entity.LocationInfo;
import cn.yy.sdk.ble.entity.SingleChatInfo;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/25 22:46
 */
public interface ReceiveMsgListener {

    void receiveGroupMsg(GroupChatInfo groupChatInfo);

    void receiveSingleMsg(SingleChatInfo singleChatInfo);

    void receiveLocationMsg(LocationInfo locationInfo);
}
