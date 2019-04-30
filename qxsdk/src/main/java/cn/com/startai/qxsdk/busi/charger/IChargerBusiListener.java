package cn.com.startai.qxsdk.busi.charger;

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
import cn.com.startai.qxsdk.event.IQXBusiResultListener;

public interface IChargerBusiListener extends IQXBusiResultListener {


    /**
     * 借充电宝结果回调
     *
     * @param resp 成功信息
     */

    void onBorrowOrGiveBackResult(BorrowOrGiveBack.Resp resp);


    /**
     * 查询网点信息
     *
     * @param resp
     */
    void onGetStoreInfoResult(GetStoreInfo.Resp resp);


    /**
     * 查询机柜信息
     *
     * @param resp
     */
    void onGetChargerInfoResult(GetChargerInfo.Resp resp);


    /**
     * 请求充值 返回结果
     *
     * @param resp
     */
    void onRequestRechargeResult(RequestRecharge.Resp resp);

    /**
     * 查询押金和余额 返回
     *
     * @param resp
     */
    void onGetBalanceAndDepositResult(GetBalanceAndDeposit.Resp resp);

    /**
     * 查询订单列表 返回
     *
     * @param resp
     */
    void onGetOrderListResult(GetOrderList.Resp resp);

    /**
     * 查询订单详情 返回
     *
     * @param resp
     */
    void onGetOrderDetailResult(GetOrderDetail.Resp resp);

    /**
     * 用余额支付订单 返回
     *
     * @param resp
     */
    void onPayForOrderWithBalanceResult(PayForOrderWithBalance.Resp resp);

    /**
     * 获取附近店铺信息 返回
     *
     * @param resp
     */
    void onGetNearbyStoresResult(GetNearbyStores.Resp resp);

    /**
     * 按区域查询门店信息 返回
     *
     * @param resp
     */
    void onGetNearbyStoreByAreaResult(GetNearbyStoresByArea.Resp resp);

    /**
     * 获取交易明细 返回
     *
     * @param resp
     */
    void onGetTransactionDetailsResult(GetTransactionDetails.Resp resp);


    /**
     * 查询用户借还状态 返回
     *
     * @param resp
     */
    void onGetUserChargingStatusResult(GetUserChargingStatus.Resp resp);

    /**
     * 查询机柜费率
     */
    void onGetChargerFeeResult(GetChargerFee.Resp resp);

    /**
     * 查询押金返回费率
     */
    void onGetDepositFeeRuleResult(GetDepositFeeRule.Resp resp);
}
