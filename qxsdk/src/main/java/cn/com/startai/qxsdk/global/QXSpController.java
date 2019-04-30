package cn.com.startai.qxsdk.global;

import android.text.TextUtils;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.channel.mqtt.entity.BrokerHost;
import cn.com.startai.qxsdk.channel.mqtt.client.QXMqttConfig;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXShareUtils;
import cn.com.startai.qxsdk.utils.area.AreaLocation;


/**
 * sp管理类
 * Created by Robin on 2018/5/11.
 * qq: 419109715 彬影
 */

public class QXSpController {


    private static final String SP_LASTGET_0X800_TIME = "SP_LASTGET_0X800_TIME";

    /**
     * 获取上次同步 0x8000的时间
     *
     * @return
     */
    public static long getLastGetBrokerHostrespTime() {

        long aLong = QXShareUtils.getInstance().getLong(SP_LASTGET_0X800_TIME, 0);

        return aLong;
    }

    /**
     * 保存此次同步节点的时间
     *
     * @param time
     */
    public static void setLastGetBrokerHostrespTime(long time) {

        QXShareUtils.getInstance().putLong(SP_LASTGET_0X800_TIME, time);

    }

    private static final String SP_LAST_CHECH_EXPIRE_TIME = "SP_LAST_CHECH_EXPIRE_TIME";

    /**
     * 获取上次登录登录 时效时间
     *
     * @return
     */
    public static long getsetLastCheckExpireTime() {

        long aLong = QXShareUtils.getInstance().getLong(SP_LAST_CHECH_EXPIRE_TIME, 0);

        return aLong;
    }

    /**
     * 保存此次检查登录 时效时间
     *
     * @param time
     */
    public static void setLastCheckExpireTime(long time) {

        QXShareUtils.getInstance().putLong(SP_LAST_CHECH_EXPIRE_TIME, time);

    }


    private static final String SP_LOCATION = "SP_LOCATION";

    /**
     * 获取上次同步 0x8000的时间
     *
     * @return
     */
    public static AreaLocation getLocation() {

        String str = QXShareUtils.getInstance().getString(SP_LOCATION, "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return QXJsonUtils.fromJson(str, AreaLocation.class);
    }

    /**
     * 保存此次同步节点的时间
     *
     * @param areaLocation
     */
    public static void setLocation(AreaLocation areaLocation) {
        if (areaLocation == null) {

            QXShareUtils.getInstance().putString(SP_LOCATION, "");
        } else {
            QXShareUtils.getInstance().putString(SP_LOCATION, QXJsonUtils.toJson(areaLocation));
        }

    }


    private static final String SP_AREA_NODE = "SP_AREA_NODE";

    /**
     * 查所有
     *
     * @return
     */
    public static BrokerHost.Resp.ContentBean getAllAreaNodeBean() {

        String s = QXShareUtils.getInstance().getString(SP_AREA_NODE, "");
        if (TextUtils.isEmpty(s)) {
            return null;
        } else {
            BrokerHost.Resp.ContentBean contentBean = QXJsonUtils.fromJson(s, BrokerHost.Resp.ContentBean.class);
            if (contentBean == null) {
                return null;
            } else {
                if (contentBean.getNode() == null || contentBean.getNode().size() == 0) {
                    return null;
                }
            }
            return QXJsonUtils.fromJson(s, BrokerHost.Resp.ContentBean.class);
        }
    }

    /**
     * 保存0x8000区域节点信息
     *
     * @param contentBean
     */
    public static void setAreaNodeBeans(BrokerHost.Resp.ContentBean contentBean) {
        if (contentBean == null) {
            QXShareUtils.getInstance().putString(SP_AREA_NODE, "");
            return;
        }
        String s = QXJsonUtils.toJson(contentBean);
        QXShareUtils.getInstance().putString(SP_AREA_NODE, s);
    }


    private static final String SP_CLIENTID = "SP_CLIENTID";

    /**
     * 查本地的clientid
     *
     * @return
     */
    public static String getClientid() {
        return QXShareUtils.getInstance().getString(SP_CLIENTID, "");
    }

    /**
     * 保存clientid到本地
     */
    public static void setClientid(String clientid) {
        QXShareUtils.getInstance().putString(SP_CLIENTID, clientid);
    }


    private static final String SP_ACTIVITE = "SP_ACTIVITE";

    /**
     * 查本地激活状态
     *
     * @return
     */
    public static boolean getIsActivite() {
        //如果appid变化了会重新激活
        boolean aBoolean = QXShareUtils.getInstance().getBoolean(SP_ACTIVITE + QXMqttConfig.getAppid() + QXMqttConfig.getSn(QX.getInstance().getApp()), false);
        return aBoolean;
    }

    /**
     * 保存激活状态
     */
    public static void setIsActivite(boolean isActivite) {
        QXShareUtils.getInstance().putBoolean(SP_ACTIVITE + QXMqttConfig.getAppid() + QXMqttConfig.getSn(QX.getInstance().getApp()), isActivite);
    }


    private static final String SP_APPID = "SP_APPID";

    /**
     * 获取appid
     *
     * @return
     */
    public static String getAppid() {
        return QXShareUtils.getInstance().getString(SP_APPID, "");
    }

    public static void setAppid(String appid) {

        QXShareUtils.getInstance().putString(SP_APPID, appid);
    }


    private static final String SP_IS_TOPICSUB = "SP_IS_TOPICSUB";

    public static boolean isSub(String clientid, String topic) {
        boolean aBoolean = QXShareUtils.getInstance().getBoolean(SP_IS_TOPICSUB + clientid + topic, false);
        return aBoolean;
    }

    public static void setIsSub(String clientid, String topic, boolean isSub) {
        QXShareUtils.getInstance().putBoolean(SP_IS_TOPICSUB + clientid + topic, isSub);
    }

    public static void removeIsSub(String clientId, String topic) {
        QXShareUtils.getInstance().remove(SP_IS_TOPICSUB + clientId + topic);
    }

    /**
     * 清除所有sp缓存消息
     */
    public static void clearAllSp() {

        QXShareUtils.getInstance().clear();
    }
}
