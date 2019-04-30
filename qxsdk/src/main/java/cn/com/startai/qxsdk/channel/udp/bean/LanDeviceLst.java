package cn.com.startai.qxsdk.channel.udp.bean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.com.startai.qxsdk.db.bean.DeviceBean;

public class LanDeviceLst {
    private final Map<String, DeviceBean> mLanDeviceArrayIP = Collections.synchronizedMap(new HashMap());
    private final Map<String, DeviceBean> mLanDeviceArrayMac = Collections.synchronizedMap(new HashMap());

    public LanDeviceLst() {
    }

    public synchronized void deviceDiscoveryUpdateDevice(DeviceBean mDevice) {
        this.mLanDeviceArrayIP.put(mDevice.getIp(), mDevice);
        this.mLanDeviceArrayMac.put(mDevice.getMac(), mDevice);
    }

    public void clear() {
        this.mLanDeviceArrayMac.clear();
        this.mLanDeviceArrayIP.clear();
    }

    public synchronized DeviceBean getDeviceBeanByMac(String mac) {
        return this.mLanDeviceArrayMac.get(mac);
    }

    public synchronized DeviceBean getLanDeviceByIP(String ip) {
        return this.mLanDeviceArrayIP.get(ip);
    }

    public synchronized void removeDeviceByMac(String mac) {
        DeviceBean deviceBean = this.mLanDeviceArrayMac.get(mac);
        this.mLanDeviceArrayMac.remove(mac);
        if (deviceBean != null) {
            this.mLanDeviceArrayIP.remove(deviceBean.getIp());
        }

    }
}
