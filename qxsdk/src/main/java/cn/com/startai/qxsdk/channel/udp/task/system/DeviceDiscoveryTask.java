package cn.com.startai.qxsdk.channel.udp.task.system;

import android.app.Application;

import cn.com.startai.qxsdk.channel.udp.SocketSecureKey;
import cn.com.startai.qxsdk.channel.udp.task.OnUdpTaskCallBack;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.global.QXParamManager;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.util.MacUtil;
import cn.com.swain.baselib.util.StrUtil;
import cn.com.swain.baselib.util.WiFiUtil;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.task.SocketResponseTask;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/1 0001
 * desc :
 */
public class DeviceDiscoveryTask extends SocketResponseTask {

    private OnUdpTaskCallBack mOnUdpTaskCallBack;

    private Application app;

    public DeviceDiscoveryTask(OnUdpTaskCallBack mOnUdpTaskCallBack, Application app) {
        this.mOnUdpTaskCallBack = mOnUdpTaskCallBack;
        this.app = app;
        Tlog.e(TAG, " new DeviceDiscoveryTask ");
    }

    @Override
    protected void doTask(SocketDataArray mSocketDataArray) {

        byte[] protocolParams = null;
        try {
            protocolParams = mSocketDataArray.getProtocolParams();
        } catch (Exception e) {
            Tlog.e(e.getMessage());
        }
        if (protocolParams == null) {
            return;
        }

        if (protocolParams.length < 46) {
            Tlog.v(TAG, " length error : " + protocolParams.length);
            if (mOnUdpTaskCallBack != null) {
                mOnUdpTaskCallBack.onDeviceDiscoveryResult(mSocketDataArray.getID(), false, null);
            }
            return;
        }

        boolean result = SocketSecureKey.Util.resultIsOk(protocolParams[0]);

        DeviceBean devicebean = new DeviceBean();
        devicebean.setIp((String) mSocketDataArray.getObj());
        devicebean.setPort(mSocketDataArray.getArg());

        devicebean.setModel(protocolParams[1]);
        devicebean.setMac(MacUtil.byteToMacStr(protocolParams, 2));

        String name = new String(protocolParams, 8, 32);
        devicebean.setName(name.trim().replaceAll("\\s*", ""));
        if (StrUtil.isSpecialName(devicebean.getName())) {
            devicebean.setName(null);
            devicebean.checkName();
        }

        int pointMainVersion = 8 + 32;
        devicebean.setMainVersion(protocolParams[pointMainVersion] & 0xFF);
        devicebean.setSubVersion(protocolParams[pointMainVersion + 1] & 0xFF);
//        protocolParams[pointMainVersion + 2] ; 语言

        byte protocolParam;

        protocolParam = protocolParams[pointMainVersion + 3];
        devicebean.setHasAdmin(SocketSecureKey.Util.isTrue((byte) (protocolParam & 0x01)));
        devicebean.setBindNeedPwd(SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 1) & 0x01)));
        devicebean.setAdmin(SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 2) & 0x01)));


        boolean wanBindTrue = SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 5) & 0x01));
        devicebean.setWanBind(wanBindTrue);
        devicebean.setLanBind(wanBindTrue || SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 4) & 0x01)));

//          bit 4  1 局域网绑定
//        bit 5    1 广域网绑定
        protocolParam = protocolParams[pointMainVersion + 4];
        devicebean.setWanState(SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 1) & 0x01)));
        devicebean.setActivateState(SocketSecureKey.Util.isTrue((byte) (protocolParam & 0x01)));

        int rssi = protocolParams[pointMainVersion + 5];
        if (rssi > 0) {
            rssi -= 100;
        }
        devicebean.setRssi(rssi);

        if (protocolParams.length >= (46 + 16)) {
            String ssid = new String(protocolParams, 46, 16);
            devicebean.setSsid(ssid.trim().replaceAll("\\s*", ""));
        } else {
            try {
                devicebean.setSsid(WiFiUtil.getConnectedWiFiSSID(app));
            } catch (Exception e) {
                Tlog.w(TAG, " DeviceDiscoveryTask.WiFiUtil.getConnectedWiFiSSID error", e);
            }
        }


        byte myCutsom = QXParamManager.getInstance().getmCustom();
        byte myProduct = QXParamManager.getInstance().getmProduct();
        boolean needCustomerFilter = QXParamManager.getInstance().isNeedCustomerFilter();


        Tlog.v(TAG, "lanDiscovery:" + String.valueOf(devicebean));

        Tlog.e(TAG, " my product:" + myProduct
                + " device product:" + mSocketDataArray.getProtocolProduct()
                + " my custom:" + myCutsom
                + " device custom:" + mSocketDataArray.getProtocolCustom());

        devicebean.setProduct(mSocketDataArray.getProtocolProduct());
        devicebean.setCustomer(mSocketDataArray.getProtocolCustom());

        if (needCustomerFilter) {
            if (myCutsom == mSocketDataArray.getProtocolCustom() &&
                    myProduct == mSocketDataArray.getProtocolProduct()) {
                if (mOnUdpTaskCallBack != null) {
                    mOnUdpTaskCallBack.onDeviceDiscoveryResult(mSocketDataArray.getID(), result, devicebean);
                }

            } else {
                Tlog.e(TAG, " find one device ,but not match custom or product");
            }

        } else {
            if (mOnUdpTaskCallBack != null) {
                mOnUdpTaskCallBack.onDeviceDiscoveryResult(mSocketDataArray.getID(), result, devicebean);
            }
        }


    }

}
