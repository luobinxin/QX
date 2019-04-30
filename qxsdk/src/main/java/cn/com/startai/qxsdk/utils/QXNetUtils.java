package cn.com.startai.qxsdk.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2018/10/19.
 * qq: 419109715 彬影
 */

public class QXNetUtils {

    private QXNetUtils() {

    }


    public static ConnectWIFIInfo getConnectedWiFiInfo(Context context) {
        WifiManager mWiFiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();
        String ssid = connectionInfo != null ? connectionInfo.getSSID() : "unknown";
        String bssid = connectionInfo != null ? connectionInfo.getBSSID() : "unknown";
        int conNetworkID = connectionInfo != null ? connectionInfo.getNetworkId() : -1;
        String pwd = null;

        // 有些手机获取不到连接的ssid
        if (conNetworkID != -1) {
            List<WifiConfiguration> configuredNetworks = mWiFiManager.getConfiguredNetworks();
            if (configuredNetworks != null && configuredNetworks.size() > 0) {
                for (WifiConfiguration mWifiConfiguration : configuredNetworks) {
                    if (mWifiConfiguration.networkId == conNetworkID) {
                        pwd = mWifiConfiguration.preSharedKey;
                        ssid = mWifiConfiguration.SSID;
                        bssid = mWifiConfiguration.BSSID;

                        break;
                    }
                }
            }
        }

        if (TextUtils.isEmpty(bssid) || !bssid.contains(":")) {
            bssid = getGatewayMac(context);
        }
        QXLog.d("StartAINetUtils", "connectionInfo ssid: " + ssid + " NetworkId: " + conNetworkID + " pwd: " + pwd + " bssid : " + bssid);

        ConnectWIFIInfo connectWIFIInfo = new ConnectWIFIInfo();

        if (conNetworkID == -1) {
            return connectWIFIInfo;
        }


        ssid = ssid.replaceAll("\"", "");
        connectWIFIInfo.ssid = ssid;
        connectWIFIInfo.bssid = bssid;
        connectWIFIInfo.pwd = pwd;
        return connectWIFIInfo;
    }


    public static class ConnectWIFIInfo {

        public String ssid = "";
        public String bssid = "";
        public String pwd = "";

    }

    public static String getGateWayIp(Context context) {
        WifiManager mWiFiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = mWiFiManager.getDhcpInfo();

        return intToIp(dhcpInfo.gateway);
    }

    /**
     * int值转换为ip
     *
     * @param addr
     * @return
     */
    private static String intToIp(int addr) {
        return ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }


    /**
     * 获取路由器MAC地址
     *
     * @return
     */
    public static String getGatewayMac(Context context) {
        String str = "";
        try {
            str = getMacFromFile(getGateWayIp(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @param str 根据ip获取到对应mac地址信息
     * @return
     */
    private static String getMacFromFile(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        List a = readFile("/proc/net/arp");
        if (a != null && a.size() > 1) {
            for (int i = 1; i < a.size(); i++) {
                List arrayList = new ArrayList();
                String[] split = ((String) a.get(i)).split(" ");
                int i2 = 0;
                while (i2 < split.length) {
                    if (split[i2] != null && split[i2].length() > 0) {
                        arrayList.add(split[i2]);
                    }
                    i2++;
                }
                if (arrayList.size() > 4 && ((String) arrayList.get(0)).equalsIgnoreCase(str)) {
                    return ((String) arrayList.get(3)).toUpperCase();
                }
            }
        }
        return "";
    }

    private static List<String> readFile(String str) {
        IOException e;
        Throwable th;
        File file = new File(str);
        List<String> arrayList = new ArrayList();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    arrayList.add(readLine);
                } catch (IOException e2) {
                    e = e2;
                }
            }
            bufferedReader.close();
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e3) {
            e = e3;
            bufferedReader = null;
            try {
                e.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e4) {
                    }
                }
                return arrayList;
            } catch (Throwable th2) {
                th = th2;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                    }
                }
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = null;
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return arrayList;
    }


}
