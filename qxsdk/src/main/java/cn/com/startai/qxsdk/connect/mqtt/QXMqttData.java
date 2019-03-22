package cn.com.startai.qxsdk.connect.mqtt;

import cn.com.startai.qxsdk.connect.BaseData;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QXMqttData extends BaseData {

    private String topic;

    public QXMqttData(byte[] dataArr) {
        super(dataArr);
    }

    public QXMqttData(byte[] dataArr, String topic) {
        super(dataArr);
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "QXMqttData{" +
                "topic='" + topic + '\'' +
                '}';
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
