package cn.com.startai.qxsdk.event;


import cn.com.startai.qxsdk.global.QXError;

public interface IOnCallListener {

    /**
     * 消息发送成功
     *
     * @param
     */
    void onSuccess();

    void onFailed(QXError error);

}
