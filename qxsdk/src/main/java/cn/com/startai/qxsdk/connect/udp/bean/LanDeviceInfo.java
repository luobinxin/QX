package cn.com.startai.qxsdk.connect.udp.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/6 0006
 * desc :
 */

public class LanDeviceInfo implements Cloneable, Serializable {

    public int model; // ble wifi
    public int mainVersion; // 主版本号
    public int subVersion; // 次版本号
    public boolean hasAdmin;// 有管理员
    public boolean isAdmin; //是否是管理员
    public boolean hasRemote;//是否已经远程连接
    public boolean bindNeedPwd;// 绑定需要密码
    public boolean hasActivate;// 已经激活

    //    public boolean isBind; // 是否已经绑定
    public boolean isLanBind;//是否已经局域网绑定
    public boolean isWanBind;//是否已经广域网绑定

//    public boolean state = true;// 连接的状态;是否在线

    public String sn; // 服务器唯一标示符
    public String mac;// mac 和js通信的唯一标示
    public String name;

    //
    public String ip; // 局域网内ip
    public int port; // 局域网内port

    public String ssid;
    public int rssi; // rssi;

    public boolean relayState; // 继电器的状态
    public String cpuInfo; // 设备cpuInfo
    public int product;
    public int customer;

    public boolean lanConnectStatus; //局域网的连接状态




    @Override
    public String toString() {
        return "LanDeviceInfo{" +
                "model=" + model +
                ", mainVersion=" + mainVersion +
                ", subVersion=" + subVersion +
                ", hasAdmin=" + hasAdmin +
                ", isAdmin=" + isAdmin +
                ", hasRemote=" + hasRemote +
                ", bindNeedPwd=" + bindNeedPwd +
                ", hasActivate=" + hasActivate +
                ", isLanBind=" + isLanBind +
                ", isWanBind=" + isWanBind +
                ", sn='" + sn + '\'' +
                ", mac='" + mac + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", ssid='" + ssid + '\'' +
                ", rssi=" + rssi +
                ", relayState=" + relayState +
                ", cpuInfo='" + cpuInfo + '\'' +
                ", product=" + product +
                ", customer=" + customer +
                ", lanConnectStatus=" + lanConnectStatus +
                '}';
    }

    public boolean isLanConnectStatus() {
        return lanConnectStatus;
    }

    public void setLanConnectStatus(boolean lanConnectStatus) {
        this.lanConnectStatus = lanConnectStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LanDeviceInfo that = (LanDeviceInfo) o;

        return mac != null ? mac.equals(that.mac) : that.mac == null;
    }

    @Override
    public int hashCode() {
        return mac != null ? mac.hashCode() : 0;
    }

    public LanDeviceInfo copy(LanDeviceInfo mWiFiDevice) {

        this.model = mWiFiDevice.model;
        this.mainVersion = mWiFiDevice.mainVersion;
        this.subVersion = mWiFiDevice.subVersion;
        this.hasAdmin = mWiFiDevice.hasAdmin;
        this.isAdmin = mWiFiDevice.isAdmin; //是否是管理员
        this.hasRemote = mWiFiDevice.hasRemote;
        this.bindNeedPwd = mWiFiDevice.bindNeedPwd;
        this.hasActivate = mWiFiDevice.hasActivate;

//        this.isBind = mWiFiDevice.isBind;
        this.isLanBind = mWiFiDevice.isLanBind;
        this.isWanBind = mWiFiDevice.isWanBind;


        this.sn = mWiFiDevice.sn;
        this.mac = mWiFiDevice.mac;
        this.name = mWiFiDevice.name;

        this.ip = mWiFiDevice.ip;
        this.port = mWiFiDevice.port;
        this.relayState = mWiFiDevice.relayState;

        this.ssid = mWiFiDevice.ssid;
        this.rssi = mWiFiDevice.rssi;
        return this;
    }


    @Override
    public LanDeviceInfo clone() {
        LanDeviceInfo clone = null;
        try {
            clone = (LanDeviceInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    public JSONObject toJsonObj() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("ip", ip);
            obj.put("name", name);
            obj.put("switch", relayState);
            obj.put("mac", mac);
            obj.put("encrypted", bindNeedPwd);
            obj.put("isBinding", isWanBind);

            JSONObject mWiFiInfo = new JSONObject();
            mWiFiInfo.put("ssid", ssid);
            mWiFiInfo.put("strength", rssi);

            obj.put("wifiInfomation", mWiFiInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }


    public static LanDeviceInfo fromJson(String deviceJson) {
        LanDeviceInfo mWiFiDevice = new LanDeviceInfo();

        try {
            JSONObject obj = new JSONObject(deviceJson);
            mWiFiDevice.name = obj.optString("name");
            mWiFiDevice.ip = obj.optString("ip");
            mWiFiDevice.mac = obj.optString("mac");
            mWiFiDevice.relayState = obj.optBoolean("state");
            mWiFiDevice.bindNeedPwd = obj.getBoolean("encrypted");
            mWiFiDevice.isLanBind = obj.getBoolean("isBinding");

            JSONObject wifiInfomation = (JSONObject) obj.opt("wifiInfomation");
            mWiFiDevice.ssid = wifiInfomation.optString("ssid");
            mWiFiDevice.rssi = wifiInfomation.optInt("strength");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mWiFiDevice;
    }

    public boolean isHasAdmin() {
        return hasAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isHasRemote() {
        return hasRemote;
    }

    public boolean isBindNeedPwd() {
        return bindNeedPwd;
    }

    public boolean isHasActivate() {
        return hasActivate;
    }

    public boolean isLanBind() {
        return isLanBind;
    }

    public void setLanBind(boolean lanBind) {
        isLanBind = lanBind;
    }

    public boolean isWanBind() {
        return isWanBind;
    }

    public void setWanBind(boolean wanBind) {
        isWanBind = wanBind;
    }


    public boolean isRelayState() {
        return relayState;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getModel() {
        return this.model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getMainVersion() {
        return this.mainVersion;
    }

    public void setMainVersion(int mainVersion) {
        this.mainVersion = mainVersion;
    }

    public int getSubVersion() {
        return this.subVersion;
    }

    public void setSubVersion(int subVersion) {
        this.subVersion = subVersion;
    }

    public boolean getHasAdmin() {
        return this.hasAdmin;
    }

    public void setHasAdmin(boolean hasAdmin) {
        this.hasAdmin = hasAdmin;
    }

    public boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean getHasRemote() {
        return this.hasRemote;
    }

    public void setHasRemote(boolean hasRemote) {
        this.hasRemote = hasRemote;
    }

    public boolean getBindNeedPwd() {
        return this.bindNeedPwd;
    }

    public void setBindNeedPwd(boolean bindNeedPwd) {
        this.bindNeedPwd = bindNeedPwd;
    }

    public boolean getHasActivate() {
        return this.hasActivate;
    }

    public void setHasActivate(boolean hasActivate) {
        this.hasActivate = hasActivate;
    }


    public boolean getIsLanBind() {
        return this.isLanBind;
    }

    public void setIsLanBind(boolean isLanBind) {
        this.isLanBind = isLanBind;
    }

    public boolean getIsWanBind() {
        return this.isWanBind;
    }

    public void setIsWanBind(boolean isWanBind) {
        this.isWanBind = isWanBind;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void checkName() {
        if (this.name == null) {
            String tMac = this.mac;
            if (tMac != null) {
                tMac = tMac.replace(":", "");
                if (tMac.length() > 6) {
                    tMac = "socket" + tMac.substring(6);
                }
            }
            setName(tMac);
        }
    }


    public boolean getRelayState() {
        return this.relayState;
    }

    public void setRelayState(boolean relayState) {
        this.relayState = relayState;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getRssi() {
        return this.rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public String getCpuInfo() {
        return this.cpuInfo;
    }


    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

}
