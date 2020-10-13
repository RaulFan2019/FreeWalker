package cn.yy.sdk.ble;

import android.os.Handler;
import android.os.Message;

import cn.yy.sdk.ble.array.NotifyActions;


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


}
