package cn.yy.sdk.ble.subject;

import java.util.ArrayList;
import java.util.List;

import cn.yy.sdk.ble.observer.ChannelListener;
import cn.yy.sdk.ble.observer.ConnectListener;


/**
 * Created by Raul.Fan on 2017/3/29.
 */
public class ChannelEventSubjectImp implements ChannelEventSubject {


    private List<ChannelListener> mObservers = new ArrayList<ChannelListener>();

    @Override
    public void attach(ChannelListener observer) {
        mObservers.add(observer);
    }

    @Override
    public void detach(ChannelListener observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyChannelSwitchOk() {
        for (ChannelListener observer : mObservers) {
            observer.switchChannelOk();
        }
    }
}
