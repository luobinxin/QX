package cn.com.startai.qxsdk.channel.mqtt.client;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import cn.com.startai.qxsdk.channel.mqtt.entity.BrokerHost;
import cn.com.startai.qxsdk.global.AreaNodesManager;
import cn.com.startai.qxsdk.global.DeviceInfoManager;
import cn.com.startai.qxsdk.global.QXSpController;
import cn.com.startai.qxsdk.utils.QXLog;
import cn.com.startai.qxsdk.utils.area.AreaLocation;
import cn.com.startai.qxsdk.utils.area.QXLocationManager;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/22.
 * 419109715@qq.com 彬影
 */
public class QXMqttConfig {


    private static String sn;
    private static String appid;
    private static String clientid;
    private static String host;
    public static String m_ver = "Json_1.6.0_9.2.19";
    public static ArrayList<String> mqttHosts = new ArrayList<>();
    public static boolean isCheckRootCrt = true;
    public static String DEFAULT_ROOTCRT = "startai/startai.bks";
    public static String mqusername = "jfeo39nvoire90rjf9fer9303ugrg";
    public static String mqpassword = "fjfier3mkdf83rfdk9ncju88329328fjjff";

    public static String CRT_PASS = "Qixing123";

    public static boolean cleanSession = false;
    //    public static boolean isAutoReconnection = true;
    public static int connectTimeOut = 10;// mqtt连接超时时长
    public static int keepAliveInterval = 60;// mqtt的心跳时长，单位秒
    public static int messageSendRetryTimes = 3; // 消息发送失败重试次数默认为3
    public static int changeHostTimeDelay = 500;// 触发节点切换算法的最低时延 ，设置为0即关闭节点自动切换功能

    public static final String HOST_CN = "ssl://cn.startai.net:8883";
    public static final String HOST_US = "ssl://us.startai.net:8883";
    public static final String HOST_CHEN = "ssl://epri.startai.net:8883";
    public static String HOST_DEBUG = "";

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        QXMqttConfig.host = host;
    }

    /**
     * 连接参数
     *
     * @return
     */
    public static MqttConnectOptions getConnectOptions(Context context) {


        MqttConnectOptions options = new MqttConnectOptions();

        options.setCleanSession(cleanSession);
        options.setUserName(mqusername);
        options.setPassword(mqpassword.toCharArray());
        options.setKeepAliveInterval(keepAliveInterval);
        options.setConnectionTimeout(connectTimeOut);
        options.setMaxInflight(60);
//        options.setAutomaticReconnect(isAutoReconnection);//

        QXMqttWill will = getWill();
        if (will != null) {
            options.setWill(will.getTopic(), will.getPlayload(), will.getQos(), will.getRetains());
        }

        boolean checkCrt = isCheckRootCrt;
        if (checkCrt) {
            try {
                options.setSocketFactory(getSocketFactory(context, DEFAULT_ROOTCRT));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return options;
    }

    private static QXMqttWill getWill() {
        return null;
    }


    private static SocketFactory getSocketFactory(Context ctx, String assestPath) throws Exception {
        SSLContext context;
        KeyStore ts = KeyStore.getInstance("BKS");
        ts.load(ctx.getResources().getAssets().open(assestPath),
                CRT_PASS.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance("X509");
        tmf.init(ts);
        TrustManager[] tm = tmf.getTrustManagers();
        context = SSLContext.getInstance("SSL");
        context.init(null, tm, null);
        // SocketFactory factory= SSLSocketFactory.getDefault();
        // Socket socket =factory.createSocket("localhost", 10000);
        SocketFactory factory = context.getSocketFactory();
        return factory;
    }


    public static List<String> getDefaulthosts() {
        List list = new ArrayList();
        list.add(HOST_CN);
        list.add(HOST_US);
        return list;
    }

    private static String getHostFromDefaultByCountryCode(@NonNull String countryCode) {
        String host = null;
        List<String> defaulthosts = getDefaulthosts();
        for (String defaulthost : defaulthosts) {
            if (defaulthost.toUpperCase().contains("ssl://" + countryCode.toUpperCase())) {
                host = defaulthost;
            }
        }
        if (TextUtils.isEmpty(host)) {
            host = HOST_CN;
        }
        return host;
    }

    //获取最优连接节点
    public static String getOptimalHost() {

        String optiimalHost = null;

        if (changeHostTimeDelay == 0) {
            QXLog.d(TAG, "不需要获取最优节点，直接匹配当地节点");
            AreaLocation location = QXLocationManager.getInstance().getLocation();
            if (location != null) {
                optiimalHost = getHostFromDefaultByCountryCode(location.getCountryCode());
            } else {
                optiimalHost = HOST_CN;
            }
            return optiimalHost;
        }
        if (!TextUtils.isEmpty(HOST_DEBUG)) {
            return HOST_DEBUG;
        }

        //获取内存缓存的数据
        BrokerHost.Resp.ContentBean cacheAreaNodes = AreaNodesManager.getInstance().getCacheAreaNodes();
        BrokerHost.Resp.ContentBean spAreaNodes = AreaNodesManager.getInstance().getSpAreaNodes();

        //首次启动应用
        if (cacheAreaNodes == null && spAreaNodes == null) {
            QXLog.d(TAG, "首次启动应用");
            //定位匹配节点
            AreaLocation location = QXLocationManager.getInstance().getLocation();
            if (location != null) {
                optiimalHost = getHostFromDefaultByCountryCode(location.getCountryCode());
            } else {
                optiimalHost = HOST_CN;
            }

        } else {//再次启动应用
            if (cacheAreaNodes == null) {
                QXLog.d(TAG, "再次启动应用");
                AreaNodesManager.getInstance().setCacheAreaNodes(spAreaNodes);
                cacheAreaNodes = AreaNodesManager.getInstance().getCacheAreaNodes();

            } else {
                QXLog.d(TAG, "断线重连");
                BrokerHost.Resp.ContentBean.NodeBean nodeByHost = cacheAreaNodes.getNodeByHost(host);

                if (nodeByHost == null || nodeByHost.getWeight() <= 0) {
                    QXLog.d(TAG, "权值已经全部小于0，重置所有节点权值重新计算时延");
                    AreaNodesManager.getInstance().setCacheAreaNodes(spAreaNodes);
                    cacheAreaNodes = AreaNodesManager.getInstance().getCacheAreaNodes();
                }

            }

            BrokerHost.Resp.ContentBean.NodeBean optimalNode = cacheAreaNodes.getOptimalNode();
            if (optimalNode != null && optimalNode.getWeight() > 0) {
                optiimalHost = optimalNode.getServer_domain();
            } else {
                optiimalHost = HOST_CN;
            }

        }

        return optiimalHost;

    }

    /**
     * @param context
     * @return
     */
    public static String getSn(Context context) {
        if (TextUtils.isEmpty(appid)) {
            QXLog.e(TAG, "appid is empty");
            return null;
        }
        if (TextUtils.isEmpty(sn)) {
            DeviceInfoManager instance = DeviceInfoManager.getInstance();
            sn = instance.getSn_16(instance.getSn(context) + appid);
            QXLog.d(TAG, "sn = " + sn);
        }
        return sn;
    }

    public static String getAppid() {
        if (TextUtils.isEmpty(appid)) {
            appid = QXSpController.getAppid();
        }
        return appid;
    }

    public static void setAppid(String id) {
        appid = id;
        QXSpController.setAppid(id);
    }

    public static String getClientId() {
        if (TextUtils.isEmpty(clientid)) {
            clientid = QXSpController.getClientid();
            if (TextUtils.isEmpty(clientid)) {
                clientid = UUID.randomUUID().toString().replace("-", "");
            }
            setClientId(clientid);
        }

        return clientid;
    }

    public static void setClientId(String cid) {
        clientid = cid;
        QXSpController.setClientid(cid);
    }
}
