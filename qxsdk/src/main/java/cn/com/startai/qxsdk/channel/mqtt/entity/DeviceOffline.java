

package cn.com.startai.qxsdk.channel.mqtt.entity;

import android.text.TextUtils;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

public class DeviceOffline {

    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {
        DeviceOffline.Resp resp = QXJsonUtils.fromJson(miof, DeviceOffline.Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "接收的数据格式错误");
        } else {
            String sn = resp.getContent().getSn();
            if (TextUtils.isEmpty(sn)) {
                sn = resp.getContent().getClientid();
            }

            QXLog.e(TAG, "设备离线 " + sn);
                callBack.onDeviceOnlineOffLine(0,    sn);

        }
    }

    public static class Resp extends BaseMessage {
        private DeviceOffline.Resp.ContentBean content;

        public Resp() {
        }

        public DeviceOffline.Resp.ContentBean getContent() {
            return this.content;
        }

        public void setContent(DeviceOffline.Resp.ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {
            private String sn;
            private String reason;
            private String clientid;
            private String username;
            private int ts;

            public ContentBean() {
            }

            public String toString() {
                return "ContentBean{sn='" + this.sn + '\'' + ", reason='" + this.reason + '\'' + ", clientid='" + this.clientid + '\'' + ", username='" + this.username + '\'' + ", ts=" + this.ts + '}';
            }

            public String getSn() {
                return this.sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getReason() {
                return this.reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public String getClientid() {
                return this.clientid;
            }

            public void setClientid(String clientid) {
                this.clientid = clientid;
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
        private String sn;
        private String reason;

        public String toString() {
            return "ContentBean{sn='" + this.sn + '\'' + ", reason='" + this.reason + '\'' + '}';
        }

        public String getSn() {
            return this.sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getReason() {
            return this.reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
