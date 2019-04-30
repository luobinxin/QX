package cn.com.startai.qxsdk.busi.common;

import cn.com.startai.qxsdk.global.QXError;

/**
 * Created by Robin on 2019/3/8.
 * 419109715@qq.com 彬影
 */
public class BaseResp extends QXError {

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_ERROR = 0;
    public static final int RESULT_STATUS = -1;

    private int result;

    public BaseResp() {
    }

    public BaseResp(int result) {
        this.result = result;
    }

    public BaseResp(int result, String errorCode) {
        super(errorCode);
        this.result = result;

    }

    public BaseResp(int result, String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
        this.result = result;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "result=" + result +
                '}';
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
