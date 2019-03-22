package cn.com.startai.qxsdk.connect.mqtt;

public enum QXMqttConnectState {
    CONNECTED,
    DISCONNECTED,
    CONNECTING,
    DISCONNECTING,
    CONNECTFAIL;

    private QXMqttConnectState() {
    }

}