package cn.com.startai.qxsdk.db.bean;

import android.text.TextUtils;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import cn.com.startai.qxsdk.connect.udp.bean.LanDeviceInfo;


/**
 * Created by Robin on 2018/10/16.
 * qq: 419109715 彬影
 */

@Table(name = DeviceBean.TABLE_NAME)
public class DeviceBean implements Serializable {

    public static final String TABLE_NAME = "DeviceBean";

    @Column(name = F_ID, isId = true)
    private long _id; //自增 不参与业务
    public static final String F_ID = "_id";

    @Column(name = F_USERID)
    private String userId; //userid
    public static final String F_USERID = "userId";

    @Column(name = F_SN)
    private String sn; //sn
    public static final String F_SN = "sn";

    @Column(name = F_MAC)
    private String mac; //mac
    public static final String F_MAC = "mac";

    @Column(name = F_MODEL)
    private int model; // ble wifi
    public static final String F_MODEL = "model";

    @Column(name = F_MAINVERSION)
    private int mainVersion; // 主版本号
    public static final String F_MAINVERSION = "mainVersion";

    @Column(name = F_SUBVERSION)
    private int subVersion; // 次版本号
    public static final String F_SUBVERSION = "subVersion";

    @Column(name = F_HASADMIN)
    private boolean hasAdmin;// 有管理员
    public static final String F_HASADMIN = "hasAdmin";

    @Column(name = F_ISADMIN)
    private boolean isAdmin; //是否是管理员
    public static final String F_ISADMIN = "isAdmin";

    @Column(name = F_REMOTESTATE)
    private boolean remoteState;//是否已经远程连接
    public static final String F_REMOTESTATE = "remoteState";

    @Column(name = F_BINDNEEDPWD)
    private boolean bindNeedPwd;// 绑定需要密码
    public static final String F_BINDNEEDPWD = "bindNeedPwd";

    @Column(name = F_HASACTIVATE)
    private boolean hasActivate;// 已经激活
    public static final String F_HASACTIVATE = "hasActivate";

    @Column(name = F_ISLANBIND)
    private boolean isLanBind;//是否已经局域网绑定
    public static final String F_ISLANBIND = "isLanBind";

    @Column(name = F_ISWANBIND)
    private boolean isWanBind;//是否已经广域网绑定
    public static final String F_ISWANBIND = "isWanBind";

    @Column(name = FILED_PRODUCT)
    private int product;
    public static final String FILED_PRODUCT = "product";

    @Column(name = FILED_CUSTOMER)
    private int customer;
    public static final String FILED_CUSTOMER = "customer";

    @Column(name = F_LANSTATE)
    private boolean lanState;//局域网连接状态
    public static final String F_LANSTATE = "lanState";

    @Column(name = F_NAME)
    private String name; //设备名称 （局域网内）在任何手机看都一样的名称
    public static final String F_NAME = "name";

    @Column(name = F_ALIAS)
    private String alias; //设备别名（同步到云端） 只给自己看的
    public static final String F_ALIAS = "alias";

    @Column(name = F_IP)
    private String ip; // 局域网内ip
    public static final String F_IP = "ip";


    @Column(name = F_PORT)
    private int port; // 局域网内port
    public static final String F_PORT = "port";

    @Column(name = F_SSID)
    private String ssid;
    public static final String F_SSID = "ssid";

    @Column(name = F_BSSID)
    private String bssid;
    public static final String F_BSSID = "bssid";


    @Column(name = F_RSSI)
    private int rssi; // rssi;
    public static final String F_RSSI = "rssi";


    @Column(name = F_CPUINFO)
    private String cpuInfo; // 设备cpuInfo
    public static final String F_CPUINFO = "cpuInfo";

    @Column(name = F_LANBINDINGTIME)
    private long lanBindingtime; //局域网绑定时间
    public static final String F_LANBINDINGTIME = "lanBindingtime";

    @Column(name = F_APPTYPE)
    private String apptype;//对方的类型
    public static final String F_APPTYPE = "apptype";

    @Column(name = F_FEATUREID)
    private String featureid;//功能id
    public static final String F_FEATUREID = "featureid";

    @Column(name = F_WANBINDTIME)
    private long wanBindtime; //广域网绑定时间
    public static final String F_WANBINDTIME = "wanBindtime";


    @Column(name = F_UPDATETIME)
    private long updateTime; //数据更新时间
    public static final String F_UPDATETIME = "updateTime";

    @Column(name = F_ADDTIME)
    private long addTime; //数据添加时间
    public static final String F_ADDTIME = "addTime";

    @Column(name = F_TOKEN)
    private int token; //数据添加时间
    public static final String F_TOKEN = "token";

    @Override
    public String toString() {
        return "DeviceBean{" +
                "_id=" + _id +
                ", userId='" + userId + '\'' +
                ", sn='" + sn + '\'' +
                ", mac='" + mac + '\'' +
                ", model=" + model +
                ", mainVersion=" + mainVersion +
                ", subVersion=" + subVersion +
                ", hasAdmin=" + hasAdmin +
                ", isAdmin=" + isAdmin +
                ", remoteState=" + remoteState +
                ", bindNeedPwd=" + bindNeedPwd +
                ", hasActivate=" + hasActivate +
                ", isLanBind=" + isLanBind +
                ", isWanBind=" + isWanBind +
                ", product=" + product +
                ", customer=" + customer +
                ", lanState=" + lanState +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", rssi=" + rssi +
                ", cpuInfo='" + cpuInfo + '\'' +
                ", lanBindingtime=" + lanBindingtime +
                ", apptype='" + apptype + '\'' +
                ", featureid='" + featureid + '\'' +
                ", wanBindtime=" + wanBindtime +
                ", updateTime=" + updateTime +
                ", addTime=" + addTime +
                ", token=" + token +
                '}';
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public DeviceBean() {
    }


    public DeviceBean(String sn, String mac) {
        this.sn = sn;
        this.mac = mac;
    }

    public DeviceBean(String sn) {
        this.sn = sn;
    }

//    public void fromWanDeviceInfo_8002(C_0x8002.Resp.ContentBean.BebindingBean bebindingBean) {
//
//        this.apptype = bebindingBean.getApptype();
//        this.featureid = bebindingBean.getFeatureid();
//        this.remoteState = bebindingBean.getConnstatus() == 1;
//    }
//
//    public void fromWanDeviceInfo_8005(C_0x8005.Resp.ContentBean contentBean) {
//
//
//        this.sn = contentBean.getId();
//        this.mac = contentBean.getMac();
//        this.alias = contentBean.getAlias();
//        this.apptype = contentBean.getApptype();
//        this.featureid = contentBean.getFeatureid();
//        this.remoteState = contentBean.getConnstatus() == 1;
//
//    }

    /**
     * 合并 局域网参数
     *
     * @param lanDeviceInfo
     */
    public void fromLanDeviceInfo(LanDeviceInfo lanDeviceInfo) {
        this.cpuInfo = lanDeviceInfo.getCpuInfo();
        this.mac = lanDeviceInfo.getMac();
        this.port = lanDeviceInfo.getPort();
        this.bindNeedPwd = lanDeviceInfo.getBindNeedPwd();
        this.name = lanDeviceInfo.getName();
        this.hasActivate = lanDeviceInfo.getHasActivate();
        this.subVersion = lanDeviceInfo.getSubVersion();
        this.mainVersion = lanDeviceInfo.getMainVersion();
        this.ssid = lanDeviceInfo.getSsid();
        this.rssi = lanDeviceInfo.getRssi();
        this.sn = lanDeviceInfo.getSn();
        this.model = lanDeviceInfo.getModel();
        this.isWanBind = lanDeviceInfo.getIsWanBind();
        this.isLanBind = lanDeviceInfo.getIsLanBind();
        this.isAdmin = lanDeviceInfo.getIsAdmin();
        this.remoteState = lanDeviceInfo.getHasRemote();
        this.hasAdmin = lanDeviceInfo.getHasAdmin();
        this.ip = lanDeviceInfo.getIp();
        this.product = lanDeviceInfo.getProduct();
        this.customer = lanDeviceInfo.getCustomer();

    }

    public LanDeviceInfo toLanDeviceInfo() {
        LanDeviceInfo info = new LanDeviceInfo();
        info.setCpuInfo(cpuInfo);
        info.setMac(mac);
        info.setPort(port);
        info.setBindNeedPwd(bindNeedPwd);
        info.setName(name);
        info.setHasActivate(hasActivate);
        info.setSubVersion(subVersion);
        info.setMainVersion(mainVersion);
        info.setSsid(ssid);
        info.setRssi(rssi);
        info.setSn(sn);
        info.setModel(model);
        info.setIsWanBind(isWanBind);
        info.setIsLanBind(isLanBind);
        info.setIsAdmin(isAdmin);
        info.setHasRemote(remoteState);
        info.setHasAdmin(hasAdmin);
        info.setIp(ip);
        info.setProduct(product);
        info.setCustomer(customer);

        return info;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceBean that = (DeviceBean) o;

        //sn 相同 或 mac 相同 就认为是同一台设备

        if (!TextUtils.isEmpty(sn) && sn.equals(that.sn)) {
            return true;
        }
        if (!TextUtils.isEmpty(mac) && mac.equals(that.mac)) {
            return true;
        }

        return false;

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

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getWanBindtime() {
        return wanBindtime;
    }

    public void setWanBindtime(long wanBindtime) {
        this.wanBindtime = wanBindtime;
    }

//    public String getTopic() {
//        return topic;
//    }

//    public void setTopic(String topic) {
//        this.topic = topic;
//    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getMainVersion() {
        return mainVersion;
    }

    public void setMainVersion(int mainVersion) {
        this.mainVersion = mainVersion;
    }

    public int getSubVersion() {
        return subVersion;
    }

    public void setSubVersion(int subVersion) {
        this.subVersion = subVersion;
    }

    public boolean isHasAdmin() {
        return hasAdmin;
    }

    public void setHasAdmin(boolean hasAdmin) {
        this.hasAdmin = hasAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isRemoteState() {
        return remoteState;
    }

    public void setRemoteState(boolean remoteState) {
        this.remoteState = remoteState;
    }

    public String getFeatureid() {
        return featureid;
    }

    public void setFeatureid(String featureid) {
        this.featureid = featureid;
    }

    public boolean isHasActivate() {
        return hasActivate;
    }

    public void setHasActivate(boolean hasActivate) {
        this.hasActivate = hasActivate;
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


    public boolean isLanState() {
        return lanState;
    }

    public void setLanState(boolean lanState) {
        this.lanState = lanState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }


    public long getLanBindingtime() {
        return lanBindingtime;
    }

    public void setLanBindingtime(long lanBindingtime) {
        this.lanBindingtime = lanBindingtime;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public boolean isBindNeedPwd() {
        return bindNeedPwd;
    }

    public void setBindNeedPwd(boolean bindNeedPwd) {
        this.bindNeedPwd = bindNeedPwd;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public boolean isCanCommunicateByWan() {

        return isRemoteState() && isWanBind();
    }

    public boolean isCanCommunicateByLan() {
        return isLanBind() && isLanState();
    }

    public boolean isCanCommunicate() {
        return isCanCommunicateByLan() || isCanCommunicateByWan();
    }
}
