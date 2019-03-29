package cn.com.startai.qxsdk.global;


import cn.com.startai.qxsdk.event.IOnCallListener;

/**
 * Created by Robin on 2019/3/12.
 * 419109715@qq.com 彬影
 */
public class QXCallbackManager {


    /**
     * 回调消息发送的结果
     *
     * @param result   消息发送成功
     * @param listener 监听
     * @param errCode  错误信息
     */
    public static void callbackCallResult(final boolean result, final IOnCallListener listener, final String errCode) {
        if (listener != null) {
            if (result) {
                listener.onSuccess();
            } else {
                QXError qxError = new QXError(errCode);
                listener.onFailed(qxError);
            }
        }
    }

    /**
     * 回调消息发送的结果
     *
     * @param result   消息发送成功
     * @param listener 监听
     * @param errCode  错误信息
     */
    public static void callbackCallResult(final boolean result, final IOnCallListener listener, final String errCode, final String errorMsg) {
        if (listener != null) {
            if (result) {
                listener.onSuccess();
            } else {
                QXError qxError = new QXError(errCode, errorMsg);
                listener.onFailed(qxError);
            }
        }
    }
}
