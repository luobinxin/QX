package cn.com.startai.qxsdk.channel.mqtt;

public enum ServerConnectState {
    CONNECTED,
    DISCONNECTED,
    CONNECTING,
    DISCONNECTING,
    CONNECTFAIL;

    private ServerConnectState() {
    }

}