package cn.com.startai.qxsdk.connect.mqtt;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.R;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public class MsgType {

    public static final String TYPE_GET_BROKER_HOST = "0x8000";

    public static final String TYPE_ACTIVATE = "0x8001";
    public static final String TYPE_BIND = "0x8002";
    public static final String TYPE_UNACTIVATE = "0x8003";
    public static final String TYPE_UNBIND = "0x8004";
    public static final String TYPE_GET_BIND_LIST = "0x8005";

    public static final String TYPE_UPDATE_REMARK = "0x8015";
    public static final String TYPE_GET_LATEST_APP_VERSION = "0x8016";
    public static final String TYPE_REGISTER = "0x8017";
    public static final String TYPE_LOGIN = "0x8018";
    public static final String TYPE_UPDATE_DEVICE_INFO = "0x8019";

    public static final String TYPE_UPDATE_USER_INFO = "0x8020";
    public static final String TYPE_GET_SMS_CODE = "0x8021";
    public static final String TYPE_CHECK_SMS_CODE = "0x8022";
    public static final String TYPE_SEND_EMAIL = "0x8023";
    public static final String TYPE_GET_USER_INFO = "0x8024";
    public static final String TYPE_UPDATE_LOGIN_PWD = "0x8025";
    public static final String TYPE_RESET_LOGIN_PWD = "0x8026";
    public static final String TYPE_LOGIN_WITH_THIRD_ACCOUNT = "0x8027";
    public static final String TYPE_THIRD_PAYMENT_UNIFIED_ORDER = "0x8028";

    public static final String TYPE_GET_REAL_ORDER_PAY_STATUS = "0x8031";
    public static final String TYPE_GET_ALIPAY_AUTH_INFO = "0x8033";
    public static final String TYPE_BIND_MOBILE_NUM = "0x8034";
    public static final String TYPE_GET_WEATHER_INFO = "0x8035";
    public static final String TYPE_UNBIND_THIRD_ACCOUNT = "0x8036";
    public static final String TYPE_BINDTHIRDACCOUNT = "0x8037";
    public static final String TYPE_GET_BIND_LIST_BY_PAGE = "0x8038";
    public static final String TYPE_BIND_EMAIL = "0x8039";

    public static final String TYPE_PASSTHROUGH = "0x8200";

    public static final String TYPE_DEVICE_ONLINE = "0x9998";
    public static final String TYPE_DEVICE_OFFLINE = "0x9999";




}
