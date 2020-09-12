package cn.yy.freewalker.entity.net;

import org.xutils.http.annotation.HttpResponse;

import cn.yy.freewalker.network.BaseResponseParser;


/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/4 16:54
 */
@HttpResponse(parser = BaseResponseParser.class)
public class BaseResult {

    public int code;                             //返回码
    public boolean result;                       //错误码
    public String msg;                           //错误消息
    public String data;                          //数据

    public BaseResult() {
    }

    public BaseResult(int code, boolean result, String msg, String data) {
        this.code = code;
        this.result = result;
        this.msg = msg;
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
