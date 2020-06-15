package cn.yy.freewalker.entity.model;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午9:22
 */
public class RecordLeftBean {
    public int recordLength;
    public boolean isSelect;

    public RecordLeftBean(int recordLength, boolean isSelect) {
        this.recordLength = recordLength;
        this.isSelect = isSelect;
    }
}
