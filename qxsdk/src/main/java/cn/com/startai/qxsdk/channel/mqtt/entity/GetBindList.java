package cn.com.startai.qxsdk.channel.mqtt.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 分页获取好友列表
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetBindList implements Serializable {

    public static String MSG_DESC = "分页获取好友列表 ";


    /*
    1.查询用户绑定的设备
2.查询用户的用户好友
3.查询设备的设备列表
4.查询设备的用户好友
5.查询用户的手机列表
6.查询手机的用户好友
7.查询所有
     */
    public static final int TYPE_USER_DEVICE = 1;
    public static final int TYPE_USER_USER = 2;
    public static final int TYPE_DEVICE_DEVICE = 3;
    public static final int TYPE_DEVICE_USER = 4;
    public static final int TYPE_USER_MOBILE = 5;
    public static final int TYPE_MOBILE_USER = 6;
    public static final int TYPE_ALL = 7;


    public static class Req {
        /**
         * id :
         * type : 1
         * page : 1
         * rows : 1
         */

        private String id;
        private int type;
        private int page;
        private int rows;

        public Req() {
        }

        public Req(int page, int rows) {
            this.page = page;
            this.rows = rows;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "id='" + id + '\'' +
                    ", type=" + type +
                    ", page=" + page +
                    ", rows=" + rows +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }
    }


    public static class Resp extends BaseMessage implements Serializable {

        private ContentBean content;
        @Override
        public String toString() {
            return "Resp{" +
                    "content=" + content +
                    ", msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    '}';
        }


        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends QXError {


            private Req errcontent = null;


            /**
             * id :
             * type : 1
             * page : 1
             * rows : 1
             * total : 12
             * friends : [{"id":"","bindingtime":111,"alias":"alias","connstatus":1,"featureid":"","mac":"","type":1}]
             */

            private String id;
            private int type;
            private int page;
            private int rows;
            private int total;
            private List<ContentBean.FriendBean> friends;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", id='" + id + '\'' +
                        ", type=" + type +
                        ", page=" + page +
                        ", rows=" + rows +
                        ", total=" + total +
                        ", friends=" + friends +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getRows() {
                return rows;
            }

            public void setRows(int rows) {
                this.rows = rows;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<Resp.ContentBean.FriendBean> getFriends() {
                return friends;
            }

            public void setFriends(List<Resp.ContentBean.FriendBean> friends) {
                this.friends = friends;
            }


            public static class FriendBean implements Serializable {

                /**
                 * apptype : smartCtrl
                 * bindingtime : 1530074475244
                 * connstatus : 1
                 * id : 736F6C863300F4EB1EE79CC8015B503D
                 * type : 1
                 */
                private String apptype;
                private long bindingtime;
                private int connstatus;
                private String id;
                private int type;
                private String alias;
                private String featureid;
                private String topic;
                private String mac;

                @Override
                public String toString() {
                    return "ContentBean{" +
                            "apptype='" + apptype + '\'' +
                            ", bindingtime=" + bindingtime +
                            ", connstatus=" + connstatus +
                            ", id='" + id + '\'' +
                            ", type=" + type +
                            ", alias='" + alias + '\'' +
                            ", featureid='" + featureid + '\'' +
                            ", topic='" + topic + '\'' +
                            ", mac='" + mac + '\'' +
                            '}';
                }

                public String getFeatureid() {
                    return featureid;
                }

                public void setFeatureid(String featureid) {
                    this.featureid = featureid;
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

                public String getAlias() {
                    return alias;
                }

                public void setAlias(String alias) {
                    this.alias = alias;
                }

                public String getApptype() {
                    return apptype;
                }

                public void setApptype(String apptype) {
                    this.apptype = apptype;
                }

                public long getBindingtime() {
                    return bindingtime;
                }

                public void setBindingtime(long bindingtime) {
                    this.bindingtime = bindingtime;
                }

                public int getConnstatus() {
                    return connstatus;
                }

                public void setConnstatus(int connstatus) {
                    this.connstatus = connstatus;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

            }

        }
    }


    /**
     * 请求 分页获取好友列表 返回结果
     *
     * @param miof
     */
    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {


        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, MSG_DESC + " 返回格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            QXLog.e(TAG, MSG_DESC + " 成功");

        } else {
            GetBindList.Resp.ContentBean content = resp.getContent();
            GetBindList.Req errcontent = content.getErrcontent();
            content.setId(errcontent.getId());
            content.setPage(errcontent.getPage());
            content.setRows(errcontent.getRows());
            content.setType(errcontent.getType());
            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }

        callBack.onGetBindListResult(resp);
    }
}
