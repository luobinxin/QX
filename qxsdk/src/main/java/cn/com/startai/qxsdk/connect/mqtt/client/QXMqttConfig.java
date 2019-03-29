package cn.com.startai.qxsdk.connect.mqtt.client;

import android.content.Context;
import android.text.TextUtils;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.UUID;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import cn.com.startai.qxsdk.global.DeviceInfoManager;
import cn.com.startai.qxsdk.global.QXSpController;
import cn.com.startai.qxsdk.utils.QXLog;

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
    public static final String DEFAULT_ROOTCRT = "startai/startai.bks";
    public static String mqusername = "jfeo39nvoire90rjf9fer9303ugrg";
    public static String mqpassword = "fjfier3mkdf83rfdk9ncju88329328fjjff";
    public static boolean cleanSession = false;
    //    public static boolean isAutoReconnection = true;
    public static int connectTimeOut = 10;// mqtt连接超时时长
    public static int keepAliveInterval = 60;// mqtt的心跳时长，单位秒
    public static int messageSendRetryTimes = 3; // 消息发送失败重试次数默认为3
    public static int changeHostTimeDelay = 500;// 触发节点切换算法的最低时延 ，设置为0即关闭节点自动切换功能

    public static final String HOST_CN = "ssl://cn.startai.net:8883";
    public static final String HOST_US = "ssl://us.startai.net:8883";
//    private static final String HOST_TEST = "ssl://192.168.1.189:8883";


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
                "Qixing123".toCharArray());
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

    //获取最优连接节点
    public static String getHost() {

        //获取内存缓存的数据

        //如果有 则表示， 此次是断线重连，不需要切换节点，直接返回上一次的连接节点

        //如果没有

        //获取本地数据 如果有，则表示需要切换节点
        //将本地数据覆盖到内存缓存数据
        //
        //如果没有 表示第一次启动，需要定位获取最优节点

        return QXMqttConfig.HOST_CN;

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
            sn = DeviceInfoManager.getInstance().getSn(context);
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
