package cn.yy.sdk.ble;

import android.os.Handler;
import android.os.Message;

import cn.yy.sdk.ble.array.NotifyActions;
import cn.yy.sdk.ble.entity.GroupChatInfo;
import cn.yy.sdk.ble.entity.LocationInfo;
import cn.yy.sdk.ble.entity.SingleChatInfo;


/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2019/5/13 15:31
 */
public class NotifyManager {

    private static NotifyManager instance;//唯一实例

    private NotifyManager() {

    }

    /**
     * 获取堆栈管理的单一实例
     */
    public static NotifyManager getManager() {
        if (instance == null) {
            instance = new NotifyManager();
        }
        return instance;
    }

    Handler mNotifyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //发布连接状态变化
                case NotifyActions.CONNECT_STATE:
                    BM.getManager().notifyStateChange((Integer) msg.obj);
                    break;
                //切换频道事件
                case NotifyActions.SWITCH_CHANNEL:
                    BM.getManager().notifyChannelSwitchOk();
                    break;
                case NotifyActions.RECEIVE_GROUP_MSG:
                    BM.getManager().notifyReceiveGroupMsg((GroupChatInfo) msg.obj);
                    break;
                case NotifyActions.RECEIVE_SINGLE_MSG:
                    BM.getManager().notifyReceiveSingleMsg((SingleChatInfo) msg.obj);
                    break;
                case NotifyActions.QUERY_LOCATION:
                    BM.getManager().notifyQueryLocationMsg();
                    break;
                case NotifyActions.RECEIVE_LOCATION_INFO:
                    BM.getManager().notifyReceiveLocationMsg((LocationInfo) msg.obj);
                    break;

            }
        }
    };

    /**
     * 发布连接状态变化
     *
     * @param state
     */
    public synchronized void notifyStateChange(final int state) {
        Message msg = mNotifyHandler.obtainMessage(NotifyActions.CONNECT_STATE);
        msg.obj = state;
        mNotifyHandler.sendMessage(msg);
    }

    /**
     * 发布连接状态变化
     */
    public synchronized void notifySwitchChannelOK() {
        Message msg = mNotifyHandler.obtainMessage(NotifyActions.SWITCH_CHANNEL);
        mNotifyHandler.sendMessage(msg);
    }


    public synchronized void notifyReceiveGroupMsg(GroupChatInfo groupChatInfo) {
        Message msg = mNotifyHandler.obtainMessage(NotifyActions.RECEIVE_GROUP_MSG);
        msg.obj = groupChatInfo;
        mNotifyHandler.sendMessage(msg);
    }

    public synchronized void notifyReceiveSingleChatMsg(SingleChatInfo singleChatInfo) {
        Message msg = mNotifyHandler.obtainMessage(NotifyActions.RECEIVE_SINGLE_MSG);
        msg.obj = singleChatInfo;
        mNotifyHandler.sendMessage(msg);
    }

    public synchronized void notifyQueryLocation() {
        Message msg = mNotifyHandler.obtainMessage(NotifyActions.QUERY_LOCATION);
        mNotifyHandler.sendMessage(msg);
    }

    public synchronized void notifyReceiveLocationMsg(LocationInfo locationInfo) {
        Message msg = mNotifyHandler.obtainMessage(NotifyActions.RECEIVE_LOCATION_INFO);
        msg.obj = locationInfo;
        mNotifyHandler.sendMessage(msg);
    }
}
