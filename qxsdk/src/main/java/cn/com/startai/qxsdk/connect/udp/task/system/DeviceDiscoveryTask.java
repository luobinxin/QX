package cn.com.startai.qxsdk.connect.udp.task.system;

import android.app.Application;

import cn.com.startai.qxsdk.connect.udp.SocketSecureKey;
import cn.com.startai.qxsdk.connect.udp.bean.LanDeviceInfo;
import cn.com.startai.qxsdk.connect.udp.task.OnUdpTaskCallBack;
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

        LanDeviceInfo mWiFiDevice = new LanDeviceInfo();
        mWiFiDevice.ip = (String) mSocketDataArray.getObj();
        mWiFiDevice.port = mSocketDataArray.getArg();

        mWiFiDevice.model = protocolParams[1];
        mWiFiDevice.mac = MacUtil.byteToMacStr(protocolParams, 2);

        String name = new String(protocolParams, 8, 32);
        mWiFiDevice.name = name.trim().replaceAll("\\s*", "");
        if (StrUtil.isSpecialName(mWiFiDevice.name)) {
            mWiFiDevice.name = null;
            mWiFiDevice.checkName();

//            int random = (int) ((Math.random() * 9 + 1) * 100000);
//            mWiFiDevice.name = "UNKNOWN" + random;

        }

        int pointMainVersion = 8 + 32;
        mWiFiDevice.mainVersion = protocolParams[pointMainVersion] & 0xFF;
        mWiFiDevice.subVersion = protocolParams[pointMainVersion + 1] & 0xFF;
//        protocolParams[pointMainVersion + 2] ; 语言

        byte protocolParam;

        protocolParam = protocolParams[pointMainVersion + 3];
        mWiFiDevice.hasAdmin = SocketSecureKey.Util.isTrue((byte) (protocolParam & 0x01));
        mWiFiDevice.bindNeedPwd = SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 1) & 0x01));
        mWiFiDevice.isAdmin = SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 2) & 0x01));

//        mWiFiDevice.isBind = (((protocolParam >> 4) & 0x01) >= 0x01);

        boolean wanBindTrue = SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 5) & 0x01));
        mWiFiDevice.isWanBind = wanBindTrue;
        mWiFiDevice.isLanBind = wanBindTrue || SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 4) & 0x01));

//          bit 4  1 局域网绑定
//        bit 5    1 广域网绑定
        protocolParam = protocolParams[pointMainVersion + 4];
        mWiFiDevice.hasRemote = SocketSecureKey.Util.isTrue((byte) ((protocolParam >> 1) & 0x01));
        mWiFiDevice.hasActivate = SocketSecureKey.Util.isTrue((byte) (protocolParam & 0x01));

        int rssi = protocolParams[pointMainVersion + 5];
        if (rssi > 0) {
            rssi -= 100;
        }
        mWiFiDevice.rssi = rssi;

        if (protocolParams.length >= (46 + 16)) {
            String ssid = new String(protocolParams, 46, 16);
            mWiFiDevice.ssid = ssid.trim().replaceAll("\\s*", "");
        } else {
            try {
                mWiFiDevice.ssid = WiFiUtil.getConnectedWiFiSSID(app);
            } catch (Exception e) {
                Tlog.w(TAG, " DeviceDiscoveryTask.WiFiUtil.getConnectedWiFiSSID error", e);
            }
        }


        byte myCutsom = QXParamManager.getInstance().getmCustom();
        byte myProduct = QXParamManager.getInstance().getmProduct();
        boolean needCustomerFilter = QXParamManager.getInstance().isNeedCustomerFilter();


        Tlog.v(TAG, "lanDiscovery:" + String.valueOf(mWiFiDevice));

        Tlog.e(TAG, " my product:" + myProduct
                + " device product:" + mSocketDataArray.getProtocolProduct()
                + " my custom:" + myCutsom
                + " device custom:" + mSocketDataArray.getProtocolCustom());

        mWiFiDevice.product = mSocketDataArray.getProtocolProduct();
        mWiFiDevice.customer = mSocketDataArray.getProtocolCustom();

        if (needCustomerFilter) {
            if (myCutsom == mSocketDataArray.getProtocolCustom() &&
                    myProduct == mSocketDataArray.getProtocolProduct()) {
                if (mOnUdpTaskCallBack != null) {
                    mOnUdpTaskCallBack.onDeviceDiscoveryResult(mSocketDataArray.getID(), result, mWiFiDevice);
                }

            } else {
                Tlog.e(TAG, " find one device ,but not match custom or product");
            }

        } else {
            if (mOnUdpTaskCallBack != null) {
                mOnUdpTaskCallBack.onDeviceDiscoveryResult(mSocketDataArray.getID(), result, mWiFiDevice);
            }
        }


    }

}
