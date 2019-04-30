package cn.com.startai.qxsdk.busi.charger;

import android.app.Application;

import java.nio.charset.Charset;

import cn.com.startai.qxsdk.QXBusiManager;
import cn.com.startai.qxsdk.busi.charger.entity.BorrowOrGiveBack;
import cn.com.startai.qxsdk.busi.charger.entity.GetStoreInfo;
import cn.com.startai.qxsdk.busi.charger.entity.GetChargerInfo;
import cn.com.startai.qxsdk.busi.charger.entity.RequestRecharge;
import cn.com.startai.qxsdk.busi.charger.entity.GetOrderList;
import cn.com.startai.qxsdk.busi.charger.entity.GetOrderDetail;
import cn.com.startai.qxsdk.busi.charger.entity.PayForOrderWithBalance;
import cn.com.startai.qxsdk.busi.charger.entity.GetNearbyStores;
import cn.com.startai.qxsdk.busi.charger.entity.GetNearbyStoresByArea;
import cn.com.startai.qxsdk.busi.charger.entity.GetTransactionDetails;
import cn.com.startai.qxsdk.busi.charger.entity.GetChargerFee;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.event.IQXCallListener;
import cn.com.startai.qxsdk.global.QXInitParam;

/**
 * 充电宝相关业务
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class ChargerBusiManager extends QXBusiManager implements OnMqttTaskCallBack {

//    private ChargerBusiManager() {
//    }
//
//    public static ChargerBusiManager getInstance() {
//        return SingleTonHoulder.singleTonInstance;
//    }
//
//
//    private static class SingleTonHoulder {
//        private static final ChargerBusiManager singleTonInstance = new ChargerBusiManager();
//    }

    private IChargerBusiListener chargerBusiListener;

    public IChargerBusiListener getChargerBusiListener() {
        return chargerBusiListener;
    }

    public void setChargerBusiListener(IChargerBusiListener chargerBusiListener) {
        this.chargerBusiListener = chargerBusiListener;
        setQXBusiListener(chargerBusiListener);
    }

    private ChargerBusiHandler chargerBusiHandler;

    @Override
    public void init(Application app, QXInitParam qxInitParam) {
        chargerBusiHandler = new ChargerBusiHandler(chargerBusiListener);
        super.init(app, qxInitParam);
    }

    @Override
    public void onMessageArrived(String topic, byte[] message) {
        super.onMessageArrived(topic, message);
        if (chargerBusiHandler != null) {
            chargerBusiHandler.handMessage(topic, new String(message, Charset.forName("utf-8")));
        }

    }


    /**
     * 借|还 充电宝
     *
     * @param req
     * @param listener
     */
    public void borrowOrGiveBack(BorrowOrGiveBack.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getBorrowOrGiveBackData(req), listener);
    }


    /**
     * 查询网点信息
     *
     * @param req      门店id 经纬度
     * @param listener
     */
    public void getStoreInfo(GetStoreInfo.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getStoreInfoData(req), listener);
    }

    /**
     * 查询机柜信息
     *
     * @param req
     * @param listener
     */
    public void getChargerInfo(GetChargerInfo.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getChargerInfoData(req), listener);
    }

    /**
     * 请求充值
     *
     * @param listener
     */
    public void requestRecharge(RequestRecharge.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getRequestRechargeData(req), listener);
    }

    /**
     * 请求查询余额
     *
     * @param listener
     */
    public void getBalanceAndDeposit(IQXCallListener listener) {
        doSend(ChargerDataCreater.getBalanceAndDepositData(), listener);
    }

    /**
     * 查询订单列表
     *
     * @param req
     */
    public void getOrderList(GetOrderList.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getOrderListData(req), listener);
    }

    /**
     * 查询订单详情
     *
     * @param req
     */
    public void getOrderDetail(GetOrderDetail.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getOrderDetailData(req), listener);
    }

    /**
     * 请求用余额支付订单
     *
     * @param req
     */
    public void payForOrderWithBalance(PayForOrderWithBalance.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getPayForOrderWithBalanceData(req), listener);
    }

    /**
     * 获取附近店铺信息
     */
    public void getNearbyStores(GetNearbyStores.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getNearbyStoresData(req), listener);
    }

    /**
     * 按区域查询门店信息
     */
    public void getNearbyStoresByArea(GetNearbyStoresByArea.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getNearbyStoresByAreaData(req), listener);
    }

    /**
     * 获取交易明细
     */
    public void getTransactionDetails(GetTransactionDetails.Req req, IQXCallListener listener) {
        doSend(ChargerDataCreater.getTransactionDetailsData(req), listener);
    }

    /**
     * 查询用户借还状态交易明细
     */
    public void getUserChargingStatus(IQXCallListener listener) {
        doSend(ChargerDataCreater.getUserChargingStatusData(), listener);
    }

    /**
     * 查询机柜费率
     */
    public void getChargerFee(GetChargerFee.Req req, IQXCallListener listener) {

        doSend(ChargerDataCreater.getChargerFeeData(req), listener);
    }

    /**
     * 查询押金费率
     */
    public void getDepositFeeRule(IQXCallListener listener) {
        doSend(ChargerDataCreater.getDepositFeeRuleData(), listener);
    }
}
