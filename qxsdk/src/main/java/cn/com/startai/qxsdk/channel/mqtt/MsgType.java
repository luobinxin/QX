package cn.com.startai.qxsdk.channel.mqtt;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public interface MsgType {

    String TYPE_GET_BROKER_HOST = "0x8000";

    String TYPE_ACTIVATE = "0x8001";
    String TYPE_BIND = "0x8002";
    String TYPE_UNACTIVATE = "0x8003";
    String TYPE_UNBIND = "0x8004";
    String TYPE_GET_BIND_LIST = "0x8005";

    String TYPE_UPDATE_REMARK = "0x8015";
    String TYPE_GET_LATEST_APP_VERSION = "0x8016";
    String TYPE_REGISTER = "0x8017";
    String TYPE_LOGIN = "0x8018";
    String TYPE_UPDATE_DEVICE_INFO = "0x8019";

    String TYPE_UPDATE_USER_INFO = "0x8020";
    String TYPE_GET_IDENTIFY_CODE = "0x8021";
    String TYPE_CHECK_IDENTIFY_CODE = "0x8022";
    String TYPE_SEND_EMAIL = "0x8023";
    String TYPE_GET_USER_INFO = "0x8024";
    String TYPE_UPDATE_LOGIN_PWD = "0x8025";
    String TYPE_RESET_LOGIN_PWD = "0x8026";
    String TYPE_LOGIN_WITH_THIRD_ACCOUNT = "0x8027";
    String TYPE_THIRD_PAYMENT_UNIFIED_ORDER = "0x8028";

    String TYPE_GET_REAL_ORDER_PAY_STATUS = "0x8031";
    String TYPE_GET_ALIPAY_AUTH_INFO = "0x8033";
    String TYPE_BIND_MOBILE_NUM = "0x8034";
    String TYPE_GET_WEATHER_INFO = "0x8035";
    String TYPE_UNBIND_THIRD_ACCOUNT = "0x8036";
    String TYPE_BINDTHIRDACCOUNT = "0x8037";
    String TYPE_GET_BIND_LIST_BY_PAGE = "0x8038";
    String TYPE_BIND_EMAIL = "0x8039";

    String TYPE_PASSTHROUGH = "0x8200";

    String TYPE_DEVICE_ONLINE = "0x9998";
    String TYPE_DEVICE_OFFLINE = "0x9999";




}
