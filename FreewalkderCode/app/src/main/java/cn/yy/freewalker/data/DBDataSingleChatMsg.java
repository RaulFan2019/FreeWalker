package cn.yy.freewalker.data;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.entity.db.GroupChatMsgEntity;
import cn.yy.freewalker.entity.db.SingleChatMsgEntity;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/29 14:21
 */
public class DBDataSingleChatMsg {

    private static final String TAG ="DBDataSingleChatMsg";

    /**
     * 保存
     *
     * @param singleChatMsgEntity
     */
    public static void save(final SingleChatMsgEntity singleChatMsgEntity) {
        YLog.e(TAG,"save");
        try {
            LocalApp.getInstance().getDb().save(singleChatMsgEntity);
        } catch (DbException e) {
            YLog.e(TAG,"save e:" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 更新
     * @param singleChatMsgEntity
     */
    public static void update(final SingleChatMsgEntity singleChatMsgEntity){
        try {
            LocalApp.getInstance().getDb().update(singleChatMsgEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取所有 群聊消息
     * @param userId
     * @return
     */
    public static List<SingleChatMsgEntity> getAllGroupChatMsg(final int userId, final int destUserId){
        List<SingleChatMsgEntity> listResult = new ArrayList<>();

        try {
            List<SingleChatMsgEntity> listTmp = LocalApp.getInstance().getDb().selector(SingleChatMsgEntity.class)
                    .where("userId","=",userId).and("destUserId","=",destUserId)
                    .findAll();
            if (listTmp != null){
                listResult.addAll(listTmp);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return listResult;
    }

}
