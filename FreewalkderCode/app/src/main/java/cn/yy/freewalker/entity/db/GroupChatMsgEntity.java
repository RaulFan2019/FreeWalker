package cn.yy.freewalker.entity.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/29 14:19
 */
@Table(name = "group_chat_msg")
public class GroupChatMsgEntity {

    @Column(name = "groupChatId", isId = true, autoGen = false)
    public long groupChatId;
    @Column(name = "userId")
    public int userId;
    @Column(name = "channel")
    public int channel;
    @Column(name = "destUserId")
    public int destUserId;
    @Column(name = "content")
    public String content;


    public GroupChatMsgEntity() {
    }

    public GroupChatMsgEntity(long groupChatId, int userId, int channel, int destUserId, String content) {
        this.groupChatId = groupChatId;
        this.userId = userId;
        this.channel = channel;
        this.destUserId = destUserId;
        this.content = content;
    }
}
