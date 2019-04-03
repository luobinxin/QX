package cn.com.startai.qxsdk.network;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/26.
 * 419109715@qq.com 彬影
 */
public class QXNetworkManager {

    private static final QXNetworkManager networkManager = new QXNetworkManager();
    private QXNetworkReceiver networkReceiver;
    private Application app;

    private QXNetworkManager() {
    }

    public static QXNetworkManager getInstance() {
        return networkManager;
    }

    private IQXNetworkListener networkListener;

    public IQXNetworkListener getNetworkListener() {
        return networkListener;
    }

    public void setNetworkListener(IQXNetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    public void init(Application application) {
        this.app = application;
        networkReceiver = new QXNetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        if (app != null && networkReceiver != null) {
            app.registerReceiver(networkReceiver, filter);
        }
    }

    public void release() {
        if (networkReceiver != null && app != null) {
            app.unregisterReceiver(networkReceiver);
        }
    }

    private List<IQXNetworkListener> list = new ArrayList<>();
    private String NETWORKTYPE_WIFI = "WIFI";
    private String NETWORKTYPE_MOBILE = "MOBILE";
    private String NETWORKTYPE_ENTHERNET = "ENTHERNET";
    private String NETWORKTYPE_UNKOWN = "UNKOWN";

    private NetworkInfo.State state;
    private String networkType;

    private boolean isUnkownNetwork() {
        return NETWORKTYPE_UNKOWN.equals(networkType) || state == NetworkInfo.State.UNKNOWN;
    }

    public boolean isAvaliableNetwork(boolean outNetCheck) {
        if (isUnkownNetwork()) {
            return false;
        } else {
            if (outNetCheck) {
                //网已经连接 判断是否可上互联网
                return isRealConnectToIntnet();
            } else {
                return true;
            }
        }
    }

    private boolean isRealConnectToIntnet() {

        boolean isAvailable;
        try {
            String host = "www.google.com";
            InetAddress byName = InetAddress.getByName("www.google.com");
            QXLog.e(TAG, host + " byName = " + byName.getHostAddress());
            isAvailable = true;
        } catch (UnknownHostException e) {
            QXLog.e(TAG, e.getMessage());
            isAvailable = false;
        }

        if (!isAvailable) {

            try {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String host = "www.baidu.com";
                InetAddress byName = InetAddress.getByName(host);
                QXLog.e(TAG, host + " byName = " + byName.getHostAddress());
                isAvailable = true;
            } catch (UnknownHostException e) {

                QXLog.e(TAG, e.getMessage());
                isAvailable = false;
            }
        }

        return isAvailable;
    }


    public void addNetworkListener(IQXNetworkListener listener) {
        if (!list.contains(listener)) {
            list.add(listener);
        }
    }

    public void removeNetworkListener(IQXNetworkListener listener) {
        if (!list.contains(listener)) {
            list.remove(listener);
        }
    }

    void callbackNetworkStateChange() {
        for (IQXNetworkListener listener : list) {
            listener.onNetworkStateChange();
        }
    }

    void callbackMobileConnected() {
        for (IQXNetworkListener listener : list) {
            listener.onMobileConnected();
        }
    }

    void callbackWifiConnected() {
        for (IQXNetworkListener listener : list) {
            listener.onWifiConnected();
        }
    }

    void callbackEthernetConnected() {
        for (IQXNetworkListener listener : list) {
            listener.onEthernetConnected();
        }
    }

    void callbackUnkownNetworkState() {
        for (IQXNetworkListener listener : list) {
            listener.onUnkownNetwork();
        }
    }

    class QXNetworkReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            String action = intent.getAction();
            QXLog.d(TAG, "CONNECTIVITY_ACTION = " + action);


            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

            if (activeNetwork != null) { // connected to the internet
                state = activeNetwork.getState();
                networkType = activeNetwork.getTypeName();

                if (activeNetwork.isConnected()) {

                    state = activeNetwork.getState();
                    networkType = activeNetwork.getTypeName();

                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        callbackWifiConnected();
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        callbackMobileConnected();
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        callbackEthernetConnected();
                    }
                } else {
                    callbackUnkownNetworkState();
                }
            } else {
                state = NetworkInfo.State.UNKNOWN;
                networkType = NETWORKTYPE_UNKOWN;
                callbackUnkownNetworkState();
            }

            QXLog.e(TAG, "current networkType  " + networkType + " state = " + state);

            if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                callbackNetworkStateChange();
            }
        }


    }

}
