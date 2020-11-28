package cn.yy.sdk.ble.subject;

import java.util.ArrayList;
import java.util.List;

import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.entity.SingleChatInfo;
import cn.yy.sdk.ble.observer.QueryMsgListener;
import cn.yy.sdk.ble.observer.ReceiveMsgListener;


/**
 * Created by Raul.Fan on 2017/3/29.
 */
public class QueryMsgSubjectImp implements QueryMsgSubject {


    private List<QueryMsgListener> mObservers = new ArrayList<QueryMsgListener>();

    @Override
    public void attach(QueryMsgListener observer) {
        mObservers.add(observer);
    }

    @Override
    public void detach(QueryMsgListener observer) {
        mObservers.remove(observer);
    }


    @Override
    public void notifyQueryLocationMsg() {
        for (QueryMsgListener observer : mObservers) {
            observer.queryLocationMsg();
        }
    }
}
