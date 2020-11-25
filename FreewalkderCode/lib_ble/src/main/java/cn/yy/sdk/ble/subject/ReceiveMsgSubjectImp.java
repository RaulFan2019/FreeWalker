package cn.yy.sdk.ble.subject;

import java.util.ArrayList;
import java.util.List;

import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.observer.ConnectListener;
import cn.yy.sdk.ble.observer.ReceiveMsgListener;


/**
 * Created by Raul.Fan on 2017/3/29.
 */
public class ReceiveMsgSubjectImp implements ReceiveMsgSubject {


    private List<ReceiveMsgListener> mObservers = new ArrayList<ReceiveMsgListener>();

    @Override
    public void attach(ReceiveMsgListener observer) {
        mObservers.add(observer);
    }

    @Override
    public void detach(ReceiveMsgListener observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyReceiveGroupMsg(GroupChatInfo groupChatInfo) {
        for (ReceiveMsgListener observer : mObservers) {
            observer.receiveGroupMsg(groupChatInfo);
        }
    }
}
