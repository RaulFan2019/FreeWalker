package cn.yy.freewalker.network;

import com.alibaba.fastjson.JSON;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cn.yy.freewalker.LocalApp;
import cn.yy.freewalker.entity.net.BaseResult;
import cn.yy.freewalker.utils.YLog;


/**
 * Created by Raul.Fan on 20/6/4.
 */
public class BaseResponseParser implements ResponseParser<String> {

    private static final String TAG = "BaseResponseParser";
    public static final int ERROR_CODE_NONE = 0;
    public static final int ERROR_CODE_JSON_EXCEPTION = 1;


    @Override
    public Object parse(Type resultType, Class resultClass, String result) throws Throwable {
        YLog.e(TAG,"result:" + result);
        BaseResult response = new BaseResult();

        //若包含 “{”
        result = result.substring(result.indexOf("{"));
        try {
            response = JSON.parseObject(result, BaseResult.class);
        } catch (com.alibaba.fastjson.JSONException e) {
            response.result = false;
            response.msg = "JSON ERROR";
        }
        return response;
    }

    @Override
    public void beforeRequest(UriRequest request) throws Throwable {

    }

    @Override
    public void afterRequest(UriRequest request) throws Throwable {

    }
}
