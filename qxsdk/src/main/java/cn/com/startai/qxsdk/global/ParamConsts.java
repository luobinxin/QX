package cn.com.startai.qxsdk.global;

/**
 * Created by Robin on 2019/3/29.
 * 419109715@qq.com 彬影
 */
public interface ParamConsts {

     byte PRODUCT_TRIGGER_BLE = 0x00; // triggerHomeBle
     byte PRODUCT_TRIGGER_WIFI = 0x02; // triggerHomeWiFi
     byte PRODUCT_SOCKET_PM90 = 0x04;// PM90
     byte PRODUCT_GROWROOMATE = 0x06;// 英国插座
     byte PRODUCT_SOCKET_RPX = 0x08;// RPX
     byte PRODUCT_NB_AIRTEMP = 0x0A;// NB-iot 供暖
     byte PRODUCT_PASS_THROUGH = 0x00;
     byte PRODUCT_MUSIK = 0x08; //musik

     byte CUSTOM_WAN = 0x00;//万总
     byte CUSTOM_LI = 0x02;// 李总
     byte CUSTOM_STARTAI = 0x08; // startai

    /**
     * 第一版本协议
     */
     byte VERSION_0 = 0x00;

    /**
     * 这个版本的协议增加了序列号
     */
     byte VERSION_SEQ = 0x01;
}
