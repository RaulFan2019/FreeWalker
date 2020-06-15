package cn.yy.freewalker.entity.model;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午9:22
 */
public class RecordRightBean {
    public int recordLength;
    public boolean isSelect;

    public RecordRightBean(int recordLength, boolean isSelect) {
        this.recordLength = recordLength;
        this.isSelect = isSelect;
    }
}
