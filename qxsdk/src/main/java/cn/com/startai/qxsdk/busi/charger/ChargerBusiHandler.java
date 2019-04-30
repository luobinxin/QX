package cn.com.startai.qxsdk.busi.charger;


import org.json.JSONException;
import org.json.JSONObject;

import cn.com.startai.qxsdk.busi.charger.entity.BorrowOrGiveBack;
import cn.com.startai.qxsdk.busi.charger.entity.GetStoreInfo;
import cn.com.startai.qxsdk.busi.charger.entity.GetChargerInfo;
import cn.com.startai.qxsdk.busi.charger.entity.RequestRecharge;
import cn.com.startai.qxsdk.busi.charger.entity.GetBalanceAndDeposit;
import cn.com.startai.qxsdk.busi.charger.entity.GetOrderList;
import cn.com.startai.qxsdk.busi.charger.entity.GetOrderDetail;
import cn.com.startai.qxsdk.busi.charger.entity.PayForOrderWithBalance;
import cn.com.startai.qxsdk.busi.charger.entity.GetNearbyStores;
import cn.com.startai.qxsdk.busi.charger.entity.GetNearbyStoresByArea;
import cn.com.startai.qxsdk.busi.charger.entity.GetTransactionDetails;
import cn.com.startai.qxsdk.busi.charger.entity.GetUserChargingStatus;
import cn.com.startai.qxsdk.busi.charger.entity.GetChargerFee;
import cn.com.startai.qxsdk.busi.charger.entity.GetDepositFeeRule;
import cn.com.startai.qxsdk.channel.mqtt.busi.MiofTag;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class ChargerBusiHandler {

    private IChargerBusiListener listener;

    public ChargerBusiHandler(IChargerBusiListener callback) {
        this.listener = callback;
    }

    public void handMessage(String topic, String msg) {

        String msgtype = "";
        try {

            JSONObject jsonObject = new JSONObject(msg);
            msgtype = jsonObject.getString(MiofTag.TAG_MSGTYPE);

        } catch (JSONException e) {
            e.printStackTrace();
            QXLog.e(TAG, "JSON format is not correct");
        }

        switch (msgtype) {


            case ChargerMsgType.TYPE_BORROW_OR_GIVEBACK: //借|还 充电宝
                BorrowOrGiveBack.handlerMsg(msg, listener);
                break;


            case ChargerMsgType.TYPE_GET_STORE_INFO: //查询网点信息
                GetStoreInfo.handlerMsg(msg, listener);
                break;

            case ChargerMsgType.TYPE_GET_CHARGER_INFO: //查询机柜信息
                GetChargerInfo.handlerMsg(msg, listener);
                break;

            case ChargerMsgType.TYPE_REQUEST_RECHARGE: //请求充值
                RequestRecharge.handlerMsg(msg, listener);

                break;
            case ChargerMsgType.TYPE_GET_BALANCE_AND_DEPOSIT: //查询押金和余额返回
                GetBalanceAndDeposit.handlerMsg(msg, listener);

                break;
            case ChargerMsgType.TYPE_GET_ORDER_LIST://请求查询订单列表
                GetOrderList.handlerMsg(msg, listener);
                break;
            case ChargerMsgType.TYPE_GET_ORDER_DETAIL://请求查询订单详情
                GetOrderDetail.handlerMsg(msg, listener);
                break;
            case ChargerMsgType.TYPE_PAY_FOR_ORDER_WITH_BALANCE: //使用余额支付订单
                PayForOrderWithBalance.handlerMsg(msg, listener);

                break;
            case ChargerMsgType.TYPE_GET_NEARBY_STORES: //获取附近店铺信息
                GetNearbyStores.handlerMsg(msg, listener);

                break;
            case ChargerMsgType.TYPE_GET_NEARBY_STORE_BYAREA: //按区域查询门店信息
                GetNearbyStoresByArea.handlerMsg(msg, listener);

                break;
            case ChargerMsgType.TYPE_GET_TRANSACTION_DETAILS: //获取交易明细
                GetTransactionDetails.handlerMsg(msg, listener);

                break;

            case ChargerMsgType.TYPE_GET_USER_CHARGING_STATUS: // 查询用户借还状态
                GetUserChargingStatus.handlerMsg(msg, listener);

                break;
            case ChargerMsgType.TYPE_GET_CHARGER_FEE: // 查询机柜费率
                GetChargerFee.handlerMsg(msg, listener);

                break;
            case ChargerMsgType.TYPE_GET_DEPOSIT_FEE_RULE: // 查询押金费率
                GetDepositFeeRule.handlerMsg(msg, listener);

                break;
            default:

                break;
        }

    }

}
