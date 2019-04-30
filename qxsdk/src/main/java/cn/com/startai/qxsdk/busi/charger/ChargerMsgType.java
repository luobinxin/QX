package cn.com.startai.qxsdk.busi.charger;

import cn.com.startai.qxsdk.channel.mqtt.MsgType;

/**
 * Created by Robin on 2019/4/17.
 * 419109715@qq.com 彬影
 */
public interface ChargerMsgType extends MsgType {

    String TYPE_BORROW_OR_GIVEBACK = "0x8301";//借 还 充电宝
    String TYPE_GET_STORE_INFO = "0x8304";//查询网点信息
    String TYPE_GET_CHARGER_INFO = "0x8305";//查询机柜信息
    String TYPE_REQUEST_RECHARGE = "0x8307";//请求充值
    String TYPE_GET_BALANCE_AND_DEPOSIT = "0x8308";//查询押金和余额
    String TYPE_GET_ORDER_LIST = "0x8309";//查询订单列表
    String TYPE_GET_ORDER_DETAIL = "0x8310";//查询订单详情
    String TYPE_PAY_FOR_ORDER_WITH_BALANCE = "0x8311";//用余额支付订单
    String TYPE_GET_NEARBY_STORES = "0x8312";//获取附近店铺信息
    String TYPE_GET_NEARBY_STORE_BYAREA = "0x8313";//按区域查询门店信息
    String TYPE_GET_TRANSACTION_DETAILS = "0x8314";//获取交易明细
    String TYPE_GET_USER_CHARGING_STATUS = "0x8315";//查询用户借还状态
    String TYPE_GET_CHARGER_FEE = "0x8316";//查询机柜费率
    String TYPE_GET_DEPOSIT_FEE_RULE = "0x8317";//查询押金费率


}
