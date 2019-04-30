
package cn.com.startai.qxsdk.channel.mqtt.entity;

import android.text.TextUtils;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

public class DeviceOnline {

    public DeviceOnline() {
    }

    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {
        DeviceOnline.Resp resp = QXJsonUtils.fromJson(miof, DeviceOnline.Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "接收的数据格式错误");
        } else {
            String sn = resp.getContent().getSn();
            if (TextUtils.isEmpty(sn)) {
                String clientid = resp.getContent().getClientid();
                QXLog.e(TAG, "设备上线 sn = " + sn + " userid = " + clientid);
                sn = clientid;
            }
            callBack.onDeviceOnlineOffLine(1, sn);
        }
    }

    public static class Resp extends BaseMessage {
        private DeviceOnline.Resp.ContentBean content;

        public Resp() {
        }

        public DeviceOnline.Resp.ContentBean getContent() {
            return this.content;
        }

        public void setContent(DeviceOnline.Resp.ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {
            private String sn;
            private String ipaddress;
            private int protocol;
            private String clientid;
            private boolean clean_sess;
            private int connack;
            private String username;
            private int ts;

            public ContentBean() {
            }

            public String toString() {
                return "ContentBean{sn='" + this.sn + '\'' + ", ipaddress='" + this.ipaddress + '\'' + ", protocol=" + this.protocol + ", clientid='" + this.clientid + '\'' + ", clean_sess=" + this.clean_sess + ", connack=" + this.connack + ", username='" + this.username + '\'' + ", ts=" + this.ts + '}';
            }

            public String getSn() {
                return this.sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getIpaddress() {
                return this.ipaddress;
            }

            public void setIpaddress(String ipaddress) {
                this.ipaddress = ipaddress;
            }

            public int getProtocol() {
                return this.protocol;
            }

            public void setProtocol(int protocol) {
                this.protocol = protocol;
            }

            public String getClientid() {
                return this.clientid;
            }

            public void setClientid(String clientid) {
                this.clientid = clientid;
            }

            public boolean isClean_sess() {
                return this.clean_sess;
            }

            public void setClean_sess(boolean clean_sess) {
                this.clean_sess = clean_sess;
            }

            public int getConnack() {
                return this.connack;
            }

            public void setConnack(int connack) {
                this.connack = connack;
            }

            public String getUsername() {
                return this.username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getTs() {
                return this.ts;
            }

            public void setTs(int ts) {
                this.ts = ts;
            }
        }
    }

    public static class Req {
        public Req() {
        }

        private String sn;
        private String ipaddress;

        public String toString() {
            return "ContentBean{sn='" + this.sn + '\'' + ", ipaddress='" + this.ipaddress + '\'' + '}';
        }

        public String getSn() {
            return this.sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getIpaddress() {
            return this.ipaddress;
        }

        public void setIpaddress(String ipaddress) {
            this.ipaddress = ipaddress;
        }
    }
}
