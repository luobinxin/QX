package cn.com.startai.qxsdk.channel.mqtt.entity;


import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.client.QXMqttConfig;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 查询最新版本
 * Created by Robin on 2018/7/19.
 * qq: 419109715 彬影
 */

public class GetLatestAppVersion {

    public static String MSG_DESC = "查询最新版本 ";


    public static class Req {

        private String appid = QXMqttConfig.getAppid();

        @Override
        public String toString() {
            return "ContentBean{" +
                    ", appid='" + appid + '\'' +
                    '}';
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

    }

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
             * os : android
             * versionName : 1.0
             * versionCode : 1
             * packageName :
             * updateUrl : http:// /fid
             * hash :
             * updateLog :
             * forcedUpdate : 1
             * fileName : 文件名
             */
            private String appid;
            private String os;
            private String versionName;
            private int versionCode;
            private String packageName;
            private String updateUrl;
            private String hash;
            private String updateLog;
            private int forcedUpdate;
            private String fileName;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", appid='" + appid + '\'' +
                        ", os='" + os + '\'' +
                        ", versionName='" + versionName + '\'' +
                        ", versionCode=" + versionCode +
                        ", packageName='" + packageName + '\'' +
                        ", updateUrl='" + updateUrl + '\'' +
                        ", hash='" + hash + '\'' +
                        ", updateLog='" + updateLog + '\'' +
                        ", forcedUpdate=" + forcedUpdate +
                        ", fileName='" + fileName + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public String getAppid() {

                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getOs() {
                return os;
            }

            public void setOs(String os) {
                this.os = os;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public String getUpdateUrl() {
                return updateUrl;
            }

            public void setUpdateUrl(String updateUrl) {
                this.updateUrl = updateUrl;
            }

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public String getUpdateLog() {
                return updateLog;
            }

            public void setUpdateLog(String updateLog) {
                this.updateLog = updateLog;
            }

            public int getForcedUpdate() {
                return forcedUpdate;
            }

            public void setForcedUpdate(int forcedUpdate) {
                this.forcedUpdate = forcedUpdate;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

        }


    }

    /**
     * 查询最新版本
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
            QXLog.e(TAG, "查询最新版本成功");
        } else {
            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setAppid(errcontent.getAppid());
            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }

        callBack.onGetLatestAppVersionResult(resp);

    }

}
