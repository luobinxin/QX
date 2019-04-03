package cn.com.startai.qxsdk.busi.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.w3c.dom.Node;

import java.util.List;
import java.util.TimerTask;

import cn.com.startai.qxsdk.connect.mqtt.BaseMessage;
import cn.com.startai.qxsdk.connect.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.AreaNodesManager;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.global.QXSpController;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;
import cn.com.startai.qxsdk.utils.QXTimerUtil;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/26.
 * 419109715@qq.com 彬影
 */
public class BrokerHost {
    public static class Req {

        public String ip;

        public Req(String ip) {
            this.ip = ip;
        }

        @Override
        public String toString() {
            return "Req{" +
                    "ip='" + ip + '\'' +
                    '}';
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

    public static class Resp extends BaseMessage {

        /**
         * toid : clientidclient1
         * msgcw : 0x08
         * lng :
         * apptype : Cloud/BXTM
         * msgid :
         * fromid : SERVICE/NMC/Smart/Controll/H5/0x07
         * contentBean : {"node":[{"ipport":"47.106.45.110:8883","server_domain":"cn.startai.net:8883","weight":2},{"ipport":"47.252.50.56:8883","server_domain":"us.startai.net:8883","weight":1}],"cycle":86400}
         * result : 1
         * domain : startai
         * m_ver : Json_1.1.4_4.2
         * msgtype : 0x8000
         * lat :
         * ts : 1526012849543
         */


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
             * node : [{"server_domain":"ssl:// cn.startai.net:8883","ip":"47.106.45.110","port":8883,"protocol":"ssl","weight":1}]
             * cycle : 86400
             */

            private int cycle;
            private List<NodeBean> node;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", cycle=" + cycle +
                        ", node=" + node +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public int getCycle() {
                return cycle;
            }

            public void setCycle(int cycle) {
                this.cycle = cycle;
            }

            public List<NodeBean> getNode() {
                return node;
            }

            public void setNode(List<NodeBean> node) {
                this.node = node;
            }

            public static class NodeBean {
                /**
                 * server_domain : ssl:// cn.startai.net:8883
                 * ip : 47.106.45.110
                 * port : 8883
                 * protocol : ssl
                 * weight : 1
                 */

                private String server_domain;
                private String ip;
                private int port;
                private String protocol;
                private double weight;

                public String getServer_domain() {
                    return server_domain;
                }

                public void setServer_domain(String server_domain) {
                    this.server_domain = server_domain;
                }

                public String getIp() {
                    return ip;
                }

                public void setIp(String ip) {
                    this.ip = ip;
                }

                public int getPort() {
                    return port;
                }

                public void setPort(int port) {
                    this.port = port;
                }

                public String getProtocol() {
                    return protocol;
                }

                public void setProtocol(String protocol) {
                    this.protocol = protocol;
                }

                public double getWeight() {
                    return weight;
                }

                public void setWeight(double weight) {
                    this.weight = weight;
                }
            }

            public NodeBean getOptimalNode() {

                NodeBean optimalNodeBean = null;
                for (NodeBean nodeBean : node) {
                    if (optimalNodeBean == null) {
                        optimalNodeBean = nodeBean;
                    } else {
                        if (optimalNodeBean.getWeight() < nodeBean.getWeight()) {
                            optimalNodeBean = nodeBean;
                        }
                    }
                }
                return optimalNodeBean;
            }

            public NodeBean matchByCountryCode(@NonNull String countryCode) {

                for (NodeBean nodeBean : node) {

                    if (nodeBean.getServer_domain().toUpperCase().contains(countryCode.toUpperCase())) {
                        return nodeBean;
                    }

                }
                return null;
            }

            public NodeBean getNodeByHost(@NonNull String host) {
                for (NodeBean nodeBean : node) {
                    if (host.contains(nodeBean.getServer_domain()) ) {
                        return nodeBean;
                    }
                }
                return null;
            }
        }
    }


    /**
     * 处理 节点信息
     *
     * @param miof
     */
    public static void handerMsg(String miof, OnMqttTaskCallBack callback) {


        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return;
        }

        if (resp.getResult() == 1) {
            QXLog.e(TAG, "节点获取成功");

        }

        callback.onGetBrokerHostesult(resp);

    }


}
