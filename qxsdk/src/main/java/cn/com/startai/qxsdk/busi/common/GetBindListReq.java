package cn.com.startai.qxsdk.busi.common;

/**
 * Created by Robin on 2019/4/15.
 * 419109715@qq.com 彬影
 */
public class GetBindListReq {

    private boolean isFromLocal;

    public boolean isFromLocal() {
        return isFromLocal;
    }

    public void setFromLocal(boolean fromLocal) {
        isFromLocal = fromLocal;
    }

    @Override
    public String toString() {
        return "GetBindListReq{" +
                "isFromLocal=" + isFromLocal +
                '}';
    }

    public GetBindListReq(boolean isFromLocal) {
        this.isFromLocal = isFromLocal;
    }
}
