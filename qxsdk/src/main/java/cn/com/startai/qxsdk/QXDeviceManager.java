package cn.com.startai.qxsdk;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Collections;
import java.util.List;

import cn.com.startai.qxsdk.channel.mqtt.entity.Bind;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetBindList;
import cn.com.startai.qxsdk.channel.udp.bean.LanDeviceLst;
import cn.com.startai.qxsdk.db.QXDBManager;
import cn.com.startai.qxsdk.db.bean.DeviceBean;


/**
 * 管理缓存已经绑定的设备列表
 * Created by Robin on 2019/3/8.
 * 419109715@qq.com 彬影
 */
public class QXDeviceManager {

    //将构造函数私有化
    private QXDeviceManager() {
    }

    public static QXDeviceManager getInstance() {
        return SingleTonHoulder.singleTonInstance;
    }

    //静态内部类
    public static class SingleTonHoulder {
        private static final QXDeviceManager singleTonInstance = new QXDeviceManager();
    }

    public void release() {
        if (deviceBeanList != null) {
            deviceBeanList.clear();
            deviceBeanList = null;
        }
    }

    private List<DeviceBean> deviceBeanList;

    public List<DeviceBean> getDeviceBeanList() {
        if (deviceBeanList == null) {
            deviceBeanList = getBindListFromLocal();
        }
        return deviceBeanList;
    }

    private List<DeviceBean> getBindListFromLocal() {
        deviceBeanList = QXDBManager.getInstance().getAllDeviceBeanByUserid(QXUserManager.getInstance().getUserId());
        Collections.sort(deviceBeanList);
        return deviceBeanList;
    }


    public DeviceBean getDeviceBeanByMac(@NonNull String mac) {
        getDeviceBeanList();

        DeviceBean deviceBean = null;
        if (deviceBeanList != null) {
            for (DeviceBean d : deviceBeanList) {
                if (mac.equals(d.getMac())) {
                    deviceBean = d;
                    break;
                }
            }
        }
        return deviceBean;
    }

    public DeviceBean getDeviceBeanBySn(@NonNull String sn) {
        DeviceBean deviceBean = null;
        if (deviceBeanList != null) {
            for (DeviceBean d : deviceBeanList) {
                if (sn.equals(d.getSn())) {
                    deviceBean = d;
                    break;
                }
            }
        }
        return deviceBean;
    }


    public void deleteLocalDevice(DeviceBean deviceBean) {

        for (DeviceBean bean : deviceBeanList) {
            if (bean.getSn() != null && bean.getSn().equals(deviceBean.getSn())
                    || bean.getMac() != null && bean.getMac().equals(deviceBean.getMac())) {
                deviceBeanList.remove(bean);
                break;
            }
        }

        if (!TextUtils.isEmpty(deviceBean.getSn())) {
            QXDBManager.getInstance().deleteDeviceBeanByUserIdAndSn(QXUserManager.getInstance().getUserId(), deviceBean.getSn());
        }
        if (!TextUtils.isEmpty(deviceBean.getMac())) {
            QXDBManager.getInstance().deleteDeviceBeanByUserIdAndMac(QXUserManager.getInstance().getUserId(), deviceBean.getMac());
        }

    }

    public void onUpdate(DeviceBean deviceBean, boolean isNeedUpdateDB) {

        for (DeviceBean bean : deviceBeanList) {
            if (bean.getSn() != null && bean.getSn().equals(deviceBean.getSn())
                    || bean.getMac() != null && bean.getMac().equals(deviceBean.getMac())) {
                deviceBeanList.remove(bean);
                deviceBeanList.add(deviceBean);
                Collections.sort(deviceBeanList);
                if (isNeedUpdateDB) {
                    QXDBManager.getInstance().addOrUpdateDeviceBean(deviceBean);
                }
                break;
            }
        }
    }

    public void onLanBindReulst(DeviceBean deviceBean) {

        getBindListFromLocal();

        DeviceBean deviceBeanByMac = getDeviceBeanByMac(deviceBean.getMac());
        if (deviceBeanByMac == null) {
            deviceBeanList.add(deviceBean);
            Collections.sort(deviceBeanList);
            QXDBManager.getInstance().addOrUpdateDeviceBean(deviceBean);
        }
    }

    public void onBindResult(Bind.Resp.ContentBean.BebindingBean bebindingBean) {
        getBindListFromLocal();

        DeviceBean deviceBeanBySn = getDeviceBeanBySn(bebindingBean.getId());
        if (deviceBeanBySn == null) {
            DeviceBean deviceBean = new DeviceBean();
            deviceBean.fromWanDeviceInfo_8002(bebindingBean);
            deviceBeanList.add(new DeviceBean());
            Collections.sort(deviceBeanList);
            QXDBManager.getInstance().addOrUpdateDeviceBean(deviceBean);
        }

    }

    public void onUnBindResult(DeviceBean deviceBean) {

        getBindListFromLocal();
        deleteLocalDevice(deviceBean);

    }

    public void onGetBindDeviceResult(List<GetBindList.Resp.ContentBean.FriendBean> listClould) {

        List<DeviceBean> listLocal = getBindListFromLocal();

        //获取绑定列表成功
        for (GetBindList.Resp.ContentBean.FriendBean deviceCloud : listClould) { //遍历 云端设备列表

            DeviceBean localDevice = null;
            for (DeviceBean deviceLocal : listLocal) {

                if (deviceCloud.getId().equals(deviceLocal.getSn())) {
                    localDevice = deviceLocal;
                    break;
                }
            }


            if (localDevice != null) {//云端有 本地有
                localDevice.setWanState(deviceCloud.getConnstatus() == 1);
                localDevice.setRemark(deviceCloud.getAlias());
                localDevice.setWanBind(true);
                localDevice.setWanBindtime(deviceCloud.getBindingtime());

                QXDBManager.getInstance().addOrUpdateDeviceBean(localDevice);

            } else {//云端有 本地没有

                localDevice = new DeviceBean();
                localDevice.fromWanDeviceInfo_8005(deviceCloud);
                localDevice.setWanBind(true);
                localDevice.setWanBindtime(deviceCloud.getBindingtime());
                localDevice.setUserId(QXUserManager.getInstance().getUserId());
                QXDBManager.getInstance().addOrUpdateDeviceBean(localDevice);


            }

        }

        for (DeviceBean deviceLocal : listLocal) { //遍历 终端设备列表

            GetBindList.Resp.ContentBean.FriendBean cloudBean = null;
            for (GetBindList.Resp.ContentBean.FriendBean deviceCloud : listClould) {
                if (deviceCloud.getId().equals(deviceLocal.getSn())) {
                    cloudBean = deviceCloud;
                    break;
                }

            }

            if (cloudBean != null) {//本地有 云端有
                //与上面情况一致， 不需要重复处理

            } else {//本地有 云端没有
                deleteLocalDevice(new DeviceBean(deviceLocal.getSn()));
                QXDBManager.getInstance().addOrUpdateDeviceBean(deviceLocal);

                if (deviceLocal.isLanBind()) {
                    //如果不存在局域网列表中，就删除
                    LanDeviceLst lanDeviceLst = QX.getInstance().getQxBusiManager().getmDiscoveryDeviceLst();
                    if (lanDeviceLst.getDeviceBeanByMac(deviceLocal.getMac()) == null
                            && lanDeviceLst.getLanDeviceByIP(deviceLocal.getIp()) == null) {

                        QXDBManager.getInstance().deleteDeviceBeanByUserIdAndMac(QXUserManager.getInstance().getUserId(), deviceLocal.getMac());
                        QXDBManager.getInstance().deleteDeviceBeanByUserIdAndSn(QXUserManager.getInstance().getUserId(), deviceLocal.getSn());
                    }
                } else { //如果本地没有局域网绑定状态 ，则是无效数据，需要删除设备
                    QXDBManager.getInstance().deleteDeviceBeanByUserIdAndMac(QXUserManager.getInstance().getUserId(), deviceLocal.getMac());
                    QXDBManager.getInstance().deleteDeviceBeanByUserIdAndSn(QXUserManager.getInstance().getUserId(), deviceLocal.getSn());
                }
            }

        }


        getBindListFromLocal();

    }


    public void resetLanState() {
        if (deviceBeanList != null) {
            for (DeviceBean deviceBean : deviceBeanList) {
                if (deviceBean.isLanState()) {
                    deviceBean.setLanState(false);
                }
            }
            QXDBManager.getInstance().resetAllDeviceBeanLanConnectStatus(QXUserManager.getInstance().getUserId());
        }
    }
}
