package cn.yy.freewalker.data;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.entity.db.BindDeviceDbEntity;
import cn.yy.freewalker.entity.db.GroupChatMsgEntity;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/29 14:21
 */
public class DBDataGroupChatMsg {

    /**
     * 保存
     *
     * @param chatMsgEntity 设备对象
     */
    public static void save(final GroupChatMsgEntity chatMsgEntity) {
        try {
            LocalApp.getInstance().getDb().save(chatMsgEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新
     * @param chatMsgEntity
     */
    public static void update(final GroupChatMsgEntity chatMsgEntity){
        try {
            LocalApp.getInstance().getDb().update(chatMsgEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取所有 群聊消息
     * @param userId
     * @param channel
     * @return
     */
    public static List<GroupChatMsgEntity> getAllGroupChatMsg(final int userId, final int channel){
        List<GroupChatMsgEntity> listResult = new ArrayList<>();

        try {
            List<GroupChatMsgEntity> listTmp = LocalApp.getInstance().getDb().selector(GroupChatMsgEntity.class)
                    .where("userId","=",userId).and("channel","=",channel).findAll();
            if (listTmp != null){
                listResult.addAll(listTmp);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return listResult;
    }

}
