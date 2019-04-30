package cn.com.startai.qxsdk.busi.charger;

import cn.com.startai.qxsdk.channel.mqtt.busi.DistributeParam;

/**
 * Created by Robin on 2018/12/27.
 * qq: 419109715 彬影
 */

public class ChargerDistributeParam extends DistributeParam {

    public static boolean BORROWORGIVEBACK_DISTRIBUTE = false;//借|还 充电宝
    public static boolean GETSTOREINFO_DISTRIBUTE = false;//查询网点信息
    public static boolean GETCHARGERINFO_DISTRIBUTE = false;//查询机柜信息
    public static boolean REQUESTRECHARGE_DISTRIBUTE = false;//请求充值
    public static boolean GETBALANCEANDDEPOSIT_DISTRIBUTE = false;//请求查询余额
    public static boolean GETORDERLIST_DISTRIBUTE = false;//查询订单列表
    public static boolean GETORDERDETAIL_DISTRIBUTE = false;//查询订单详情
    public static boolean PAYFORORDERWITHBALANCE_DISTRIBUTE = false;//请求用余额支付订单
    public static boolean GETNEARBYSTORES_DISTRIBUTE = false;//获取附近店铺信息
    public static boolean GETNEARBYSTORESBYAREA_DISTRIBUTE = false;//按区域查询门店信息
    public static boolean GETTRANSACTIONDETAILS_DISTRIBUTE = false;//获取交易明细
    public static boolean GETUSERCHARGINGSTATUS_DISTRIBUTE = false;//查询用户借还状态交易明细
    public static boolean GETCHARGERFEE_DISTRIBUTE = false;//查询机柜费率
    public static boolean GETDEPOSITFEERULE_DISTRIBUTE = false;//查询押金费率


    public static boolean isDistribute(String msgType) {
        switch (msgType) {
            case ChargerMsgType.TYPE_BORROW_OR_GIVEBACK:
                return BORROWORGIVEBACK_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_STORE_INFO:
                return GETSTOREINFO_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_CHARGER_INFO:
                return GETCHARGERINFO_DISTRIBUTE;
            case ChargerMsgType.TYPE_REQUEST_RECHARGE:
                return REQUESTRECHARGE_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_BALANCE_AND_DEPOSIT:
                return GETBALANCEANDDEPOSIT_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_ORDER_LIST:
                return GETORDERLIST_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_ORDER_DETAIL:
                return GETORDERDETAIL_DISTRIBUTE;
            case ChargerMsgType.TYPE_PAY_FOR_ORDER_WITH_BALANCE:
                return PAYFORORDERWITHBALANCE_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_NEARBY_STORES:
                return GETNEARBYSTORES_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_NEARBY_STORE_BYAREA:
                return GETNEARBYSTORESBYAREA_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_TRANSACTION_DETAILS:
                return GETTRANSACTIONDETAILS_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_USER_CHARGING_STATUS:
                return GETUSERCHARGINGSTATUS_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_CHARGER_FEE:
                return GETCHARGERFEE_DISTRIBUTE;
            case ChargerMsgType.TYPE_GET_DEPOSIT_FEE_RULE:
                return GETDEPOSITFEERULE_DISTRIBUTE;
            default:
                return DistributeParam.isDistribute(msgType);

        }
    }
}
