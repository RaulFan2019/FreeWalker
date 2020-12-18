package cn.yy.sdk.ble.entity;

import java.util.ArrayList;
import java.util.List;

import cn.yy.sdk.ble.utils.BLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/11/27 22:11
 */
public class GroupPkgEntity {

    private static final String TAG = "GroupPkgEntity";

    public static final int INSERT_OK = 0x01;
    public static final int INSERT_FINISH = 0x02;
    public static final int INSERT_GIVE_UP = 0x03;


    public int targetSize;
    public byte port;
    public List<Byte> listData;


    public GroupPkgEntity() {
        init();
    }


    public void init() {
        this.targetSize = 0;
        this.port = -1;
        this.listData = new ArrayList<>();
    }


    /**
     * 插入头数据
     * @return
     */
    public int insertPkgHead(final byte[] data){
        this.port = data[3];
        this.targetSize = data[2] - 1;
        this.listData = new ArrayList<>();
        //数据塞入content里
        for (int i = 4 ; i < data.length; i++){
            this.listData.add(data[i]);
        }

        BLog.e(TAG,"insertPkgHead targetSize:" + targetSize + "this.listData.size():" + this.listData.size());

        if (targetSize == this.listData.size()){
            return INSERT_FINISH;
        }else if (targetSize < this.listData.size()){
            init();
            return INSERT_GIVE_UP;
        }else {
            return INSERT_OK;
        }
    }


    /**
     * 插入剩余数据
     * @return
     */
    public int insertPkgLeft(final byte[] data){
        //数据塞入content里
        for (int i = 0 ; i < data.length; i++){
            this.listData.add(data[i]);
        }

        BLog.e(TAG,"insertPkgLeft targetSize:" + targetSize + "this.listData.size():" + this.listData.size());
        if (targetSize == this.listData.size()){
            return INSERT_FINISH;
        }else if (targetSize < this.listData.size()){
            init();
            return INSERT_GIVE_UP;
        }else {
            return INSERT_OK;
        }
    }


}
