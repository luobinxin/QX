package cn.com.startai.qxsdk.connect.mqtt;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public class MsgCw {

    public static final String CW_0x01 = "0x01";
    public static final String CW_0x02 = "0x02";
    public static final String CW_0x03 = "0x03";
    public static final String CW_0x04 = "0x04";
    public static final String CW_0x05 = "0x05";
    public static final String CW_0x06 = "0x06";
    public static final String CW_0x07 = "0x07";
    public static final String CW_0x08 = "0x08";
    public static final String CW_0x09 = "0x09";
    public static final String CW_0x10 = "0x10";
    public static final String CW_0x12 = "0x12";
    public static final String CW_alarm = "alarm";

    public static String getReturnMsgCw(String msgcw) {
        switch (msgcw) {
            case CW_0x01:
                return CW_0x02;
            case CW_0x03:
                return CW_0x04;
            case CW_0x05:
                return CW_0x06;
            case CW_0x07:
                return CW_0x08;
            default:
                return msgcw;
        }
    }

}
