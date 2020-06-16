package cn.yy.freewalker.entity.model;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/15 下午9:22
 */
public class RecordLeftBean {
    public int id;
    public int recordLength;
    public boolean isSelect;
    public boolean isEditModel;

    public RecordLeftBean(int id,int recordLength, boolean isSelect) {
        this.id = id;
        this.recordLength = recordLength;
        this.isSelect = isSelect;
    }
}
