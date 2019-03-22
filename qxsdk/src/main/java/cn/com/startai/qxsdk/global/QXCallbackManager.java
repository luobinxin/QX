package cn.com.startai.qxsdk.global;


import cn.com.startai.qxsdk.event.IonCallListener;

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
     * @param QXError  错误信息
     */
    public static void callbackCallResult(final boolean result, final IonCallListener listener, final QXError QXError) {
        if (listener != null) {
            if (result) {
                listener.onSuccess();
            } else {
                listener.onFailed(QXError);
            }
        }
    }


}
