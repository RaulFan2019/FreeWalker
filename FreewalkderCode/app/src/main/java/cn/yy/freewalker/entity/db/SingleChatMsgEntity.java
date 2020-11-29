package cn.yy.freewalker.entity.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/29 14:19
 */
@Table(name = "single_chat_msg")
public class SingleChatMsgEntity {

    @Column(name = "singleChatId", isId = true, autoGen = false)
    public long singleChatId;
    @Column(name = "userId")
    public int userId;
    @Column(name = "destUserId")
    public int destUserId;
    @Column(name = "content")
    public String content;
    @Column(name = "isSelf")
    public boolean isSelf;


    public SingleChatMsgEntity() {
    }

    public SingleChatMsgEntity(long singleChatId, int userId, int destUserId,
                               String content, boolean isSelf) {
        this.singleChatId = singleChatId;
        this.userId = userId;
        this.destUserId = destUserId;
        this.content = content;
        this.isSelf = isSelf;
    }
}
