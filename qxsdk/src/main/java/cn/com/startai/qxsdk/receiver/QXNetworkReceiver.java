package cn.com.startai.qxsdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import cn.com.startai.qxsdk.event.IQXNetworkListener;
import cn.com.startai.qxsdk.utils.QXLog;

/**
 * Created by Robin on 2019/3/22.
 * 419109715@qq.com 彬影
 */
public class QXNetworkReceiver extends BroadcastReceiver {

    private List<IQXNetworkListener> list = new ArrayList<>();
    private String TAG = "QXNetworkReceiver";

    private String NETWORKTYPE_WIFI = "WIFI";
    private String NETWORKTYPE_MOBILE = "MOBILE";
    private String NETWORKTYPE_ENTHERNET = "ENTHERNET";
    private String NETWORKTYPE_UNKOWN = "UNKOWN";

    private NetworkInfo.State state;
    private String networkType;


    public boolean isUnkownNetwork() {
        return NETWORKTYPE_UNKOWN.equals(networkType) || state == NetworkInfo.State.UNKNOWN;
    }

    public boolean isAvaliableNetwork(){
        return !isUnkownNetwork();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        String action = intent.getAction();
        QXLog.d(TAG, "CONNECTIVITY_ACTION = " + action);
        if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            callbackNetworkStateChange();
        }

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
            callbackUnkownNetworkState();
            state = NetworkInfo.State.UNKNOWN;
            networkType = NETWORKTYPE_UNKOWN;
        }

        QXLog.e(TAG, "current networkType  " + networkType + " state = " + state);

    }


    public void addNetworkListener(IQXNetworkListener listener) {
        if (list.contains(listener)) {
            list.add(listener);
        }
    }

    public void removeNetworkListener(IQXNetworkListener listener) {
        if (list.contains(listener)) {
            list.remove(listener);
        }
    }

    void callbackNetworkStateChange() {
        for (IQXNetworkListener IQXNetworkListener : list) {
            IQXNetworkListener.onNetworkStateChange();
        }
    }

    void callbackMobileConnected() {
        for (IQXNetworkListener IQXNetworkListener : list) {
            IQXNetworkListener.onMobileConnected();
        }
    }

    void callbackWifiConnected() {
        for (IQXNetworkListener IQXNetworkListener : list) {
            IQXNetworkListener.onWifiConnected();
        }
    }

    void callbackEthernetConnected() {
        for (IQXNetworkListener IQXNetworkListener : list) {
            IQXNetworkListener.onEthernetConnected();
        }
    }

    void callbackUnkownNetworkState() {
        for (IQXNetworkListener IQXNetworkListener : list) {
            IQXNetworkListener.onUnkownNetwork();
        }
    }
}
