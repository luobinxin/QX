package cn.com.startai.qxsdk.channel.mqtt.entity;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 绑定
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class Bind {

    public static String MSG_DESC = "绑定 ";


    /**
     * 绑定请求json 对应实体类
     */
    public static class Req {

        private String bindingid;
        private String bebindingid;

        public Req(String bebindingid) {
            this.bebindingid = bebindingid;
        }

        public Req() {
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "bindingid='" + bindingid + '\'' +
                    ", bebindingid='" + bebindingid + '\'' +
                    '}';
        }

        public Req(String bindingid, String bebindingid) {
            this.bindingid = bindingid;
            this.bebindingid = bebindingid;
        }

        public String getBindingid() {
            return bindingid;
        }

        public void setBindingid(String bindingid) {
            this.bindingid = bindingid;
        }

        public String getBebindingid() {
            return bebindingid;
        }

        public void setBebindingid(String bebindingid) {
            this.bebindingid = bebindingid;
        }


    }

    /**
     * 绑定请求返回 json 对应实体类
     */
    public static class Resp extends BaseMessage {


        private ContentBean content;

        @Override
        public String toString() {
            return "Resp{" +
                    "msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    ", content=" + content +
                    '}';
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends QXError {

            /**
             * binding : {"id":"","apptype":"","featureid":"","connstatus":""}
             * bebinding : {"id":"","apptype":"","featureid":"","connstatus":""}
             */

            private BindingBean binding;
            private BebindingBean bebinding;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", binding=" + binding +
                        ", bebinding=" + bebinding +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public BindingBean getBinding() {
                return binding;
            }

            public void setBinding(BindingBean binding) {
                this.binding = binding;
            }

            public BebindingBean getBebinding() {
                return bebinding;
            }

            public void setBebinding(BebindingBean bebinding) {
                this.bebinding = bebinding;
            }

            public static class BindingBean {


                private String id;
                private String apptype;
                private String featureid;
                private String topic;
                private String mac;
                private int connstatus;

                @Override
                public String toString() {
                    return "BindingBean{" +
                            "id='" + id + '\'' +
                            ", apptype='" + apptype + '\'' +
                            ", featureid='" + featureid + '\'' +
                            ", topic='" + topic + '\'' +
                            ", mac='" + mac + '\'' +
                            ", connstatus=" + connstatus +
                            '}';
                }

                public String getMac() {
                    return mac;
                }

                public void setMac(String mac) {
                    this.mac = mac;
                }


                public String getTopic() {
                    return topic;
                }

                public void setTopic(String topic) {
                    this.topic = topic;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getApptype() {
                    return apptype;
                }

                public void setApptype(String apptype) {
                    this.apptype = apptype;
                }

                public String getFeatureid() {
                    return featureid;
                }

                public void setFeatureid(String featureid) {
                    this.featureid = featureid;
                }

                public int getConnstatus() {
                    return connstatus;
                }

                public void setConnstatus(int connstatus) {
                    this.connstatus = connstatus;
                }
            }

            public static class BebindingBean {
                public BebindingBean() {
                }

                private String id;
                private String apptype;
                private String featureid;
                private String topic;
                private int connstatus;
                private String mac;

                @Override
                public String toString() {
                    return "BebindingBean{" +
                            "id='" + id + '\'' +
                            ", apptype='" + apptype + '\'' +
                            ", featureid='" + featureid + '\'' +
                            ", topic='" + topic + '\'' +
                            ", connstatus=" + connstatus +
                            ", mac='" + mac + '\'' +
                            '}';
                }

                public BebindingBean(String id, String apptype, String featureid, String topic, int connstatus, String mac) {
                    this.id = id;
                    this.apptype = apptype;
                    this.featureid = featureid;
                    this.topic = topic;
                    this.connstatus = connstatus;
                    this.mac = mac;
                }

                public String getMac() {
                    return mac;
                }

                public void setMac(String mac) {
                    this.mac = mac;
                }

                public String getTopic() {
                    return topic;
                }

                public void setTopic(String topic) {
                    this.topic = topic;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getApptype() {
                    return apptype;
                }

                public void setApptype(String apptype) {
                    this.apptype = apptype;
                }

                public String getFeatureid() {
                    return featureid;
                }

                public void setFeatureid(String featureid) {
                    this.featureid = featureid;
                }

                public int getConnstatus() {
                    return connstatus;
                }

                public void setConnstatus(int connstatus) {
                    this.connstatus = connstatus;
                }
            }

        }
    }

    /**
     * 绑定消息
     *
     * @param miof
     */
    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {


        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            QXLog.e(TAG, "绑定成功");
        } else {

            QXLog.e(TAG, "绑定失败 " + resp.getContent().getErrmsg());
        }

        callBack.onBindResult(resp);

    }


}
