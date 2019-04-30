package cn.com.startai.qxsdk.busi.charger;

import android.text.TextUtils;

import cn.com.startai.qxsdk.QXUserManager;
import cn.com.startai.qxsdk.busi.charger.entity.BorrowOrGiveBack;
import cn.com.startai.qxsdk.busi.charger.entity.GetBalanceAndDeposit;
import cn.com.startai.qxsdk.busi.charger.entity.GetChargerFee;
import cn.com.startai.qxsdk.busi.charger.entity.GetChargerInfo;
import cn.com.startai.qxsdk.busi.charger.entity.GetDepositFeeRule;
import cn.com.startai.qxsdk.busi.charger.entity.GetNearbyStores;
import cn.com.startai.qxsdk.busi.charger.entity.GetNearbyStoresByArea;
import cn.com.startai.qxsdk.busi.charger.entity.GetOrderDetail;
import cn.com.startai.qxsdk.busi.charger.entity.GetOrderList;
import cn.com.startai.qxsdk.busi.charger.entity.GetStoreInfo;
import cn.com.startai.qxsdk.busi.charger.entity.GetTransactionDetails;
import cn.com.startai.qxsdk.busi.charger.entity.GetUserChargingStatus;
import cn.com.startai.qxsdk.busi.charger.entity.PayForOrderWithBalance;
import cn.com.startai.qxsdk.busi.charger.entity.RequestRecharge;
import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.channel.mqtt.busi.QXMqttDataCreater;

/**
 * Created by Robin on 2019/4/17.
 * 419109715@qq.com 彬影
 */
public class ChargerDataCreater extends QXMqttDataCreater {


    public static BaseData getBorrowOrGiveBackData(BorrowOrGiveBack.Req req) {
        if (TextUtils.isEmpty(req.getImei())) {
            logParamEmpty("imei");
            return null;
        }
        if (TextUtils.isEmpty(req.getUserId())) {
            req.setUserId(QXUserManager.getInstance().getUserId());
        }
        return getBaseServerData(ChargerMsgType.TYPE_BORROW_OR_GIVEBACK, req);
    }

    public static BaseData getStoreInfoData(GetStoreInfo.Req Req) {

        return getBaseServerData(ChargerMsgType.TYPE_GET_STORE_INFO, Req);
    }

    public static BaseData getChargerInfoData(GetChargerInfo.Req req) {
        if (TextUtils.isEmpty(req.getImei())) {
            logParamEmpty("imei");
            return null;
        }
        return getBaseServerData(ChargerMsgType.TYPE_GET_CHARGER_INFO, req);
    }

    public static BaseData getRequestRechargeData(RequestRecharge.Req req) {

        if (TextUtils.isEmpty(req.getUserId())) {
            req.setUserId(QXUserManager.getInstance().getUserId());
        }
        return getBaseServerData(ChargerMsgType.TYPE_REQUEST_RECHARGE, req);
    }

    public static BaseData getBalanceAndDepositData() {

        GetBalanceAndDeposit.Req req = new GetBalanceAndDeposit.Req();
        req.setUserId(QXUserManager.getInstance().getUserId());
        return getBaseServerData(ChargerMsgType.TYPE_GET_BALANCE_AND_DEPOSIT, req);
    }

    public static BaseData getOrderListData(GetOrderList.Req req) {
        if (TextUtils.isEmpty(req.getUserId())) {
            req.setUserId(QXUserManager.getInstance().getUserId());
        }

        return getBaseServerData(ChargerMsgType.TYPE_GET_ORDER_LIST, req);
    }

    public static BaseData getOrderDetailData(GetOrderDetail.Req req) {
        if (TextUtils.isEmpty(req.getNo())) {
            logParamEmpty("no");
            return null;
        }
        return getBaseServerData(ChargerMsgType.TYPE_GET_ORDER_DETAIL, req);
    }

    public static BaseData getPayForOrderWithBalanceData(PayForOrderWithBalance.Req req) {
        if (TextUtils.isEmpty(req.getNo())) {
            logParamEmpty("no");
            return null;
        }
        return getBaseServerData(ChargerMsgType.TYPE_PAY_FOR_ORDER_WITH_BALANCE, req);
    }

    public static BaseData getNearbyStoresData(GetNearbyStores.Req req) {

        return getBaseServerData(ChargerMsgType.TYPE_GET_NEARBY_STORES, req);
    }

    public static BaseData getNearbyStoresByAreaData(GetNearbyStoresByArea.Req req) {

        return getBaseServerData(ChargerMsgType.TYPE_GET_NEARBY_STORE_BYAREA, req);
    }

    public static BaseData getTransactionDetailsData(GetTransactionDetails.Req req) {
        if (TextUtils.isEmpty(req.getUserId())) {
            req.setUserId(QXUserManager.getInstance().getUserId());
        }

        return getBaseServerData(ChargerMsgType.TYPE_GET_TRANSACTION_DETAILS, req);
    }

    public static BaseData getUserChargingStatusData() {
        GetUserChargingStatus.Req req = new GetUserChargingStatus.Req();
        req.setUserId(QXUserManager.getInstance().getUserId());
        return getBaseServerData(ChargerMsgType.TYPE_GET_USER_CHARGING_STATUS, req);
    }

    public static BaseData getChargerFeeData(GetChargerFee.Req req) {
        if (TextUtils.isEmpty(req.getImei())) {
            logParamEmpty("imei");
            return null;
        }
        return getBaseServerData(ChargerMsgType.TYPE_GET_CHARGER_FEE, req);
    }

    public static BaseData getDepositFeeRuleData() {
        GetDepositFeeRule.Req req = new GetDepositFeeRule.Req();
        req.setUserId(QXUserManager.getInstance().getUserId());
        return getBaseServerData(ChargerMsgType.TYPE_GET_DEPOSIT_FEE_RULE, req);
    }


}
