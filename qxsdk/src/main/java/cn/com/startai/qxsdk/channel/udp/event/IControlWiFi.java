package cn.com.startai.qxsdk.channel.udp.event;

import cn.com.startai.qxsdk.db.bean.DeviceBean;

/**
 * 内部wifi接口
 * author: Guoqiang_Sun
 * date : 2018/6/6 0006
 * desc :
 */
public interface IControlWiFi {


    /**
     * 请求休眠
     *
     * @param mac
     * @param userID
     * @param token
     */
    void appSleep(String mac, String userID, int token);


    /**
     * 请求token
     *
     * @param mac
     * @param userID
     */
    void requestToken(String mac, String userID);


    /**
     * 内部 wifi接口回调
     */
    interface IWiFiResultCallBack {


        /**
         * 回调 设备需要重新请求token
         *
         * @param mac
         * @param userID
         */
        void onResultNeedRequestToken(String mac, String userID);

        /**
         * 回调 设备可以进行连接通信
         *
         * @param mac         设备mac
         * @param loginUserID userid
         * @param token       toktn
         */
        void onResultCanControlDevice(String mac, String loginUserID, int token);

        /**
         * 回调连接设备超时
         *
         * @param deviceBean
         */
        void onResultConnectDevice(int result, DeviceBean deviceBean);
    }

}
