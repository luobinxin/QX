package cn.com.startai.qxsdk.busi.charger.entity;

import java.io.Serializable;
import java.util.List;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 请求 获取附近店铺信息
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetNearbyStores implements Serializable {

    public static final String MIOF_DESC = "获取附近店铺信息";


    /**
     * 请求 获取附近店铺信息 返回结果
     *
     * @param miof
     */
    public static void handlerMsg(String miof, IChargerBusiListener listener) {


        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {

            QXLog.d(TAG, MIOF_DESC + "成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setLat(errcontent.getLat());
            content.setLng(errcontent.getLng());

            QXLog.d(TAG, MIOF_DESC + "失败");
        }
        listener.onGetNearbyStoresResult(resp);

    }


    public static class Req {
        /*lng :113.367631
         *lat :23.130781
         */

        private String lng;
        private String lat;


        public Req(String lng, String lat) {
            this.lng = lng;
            this.lat = lat;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "lng='" + lng + '\'' +
                    ", lat='" + lat + '\'' +
                    '}';
        }


        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
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


            private Req errcontent;


            /**
             * lng : 113.367631
             * lat : 23.130781
             * resultSet : [{"merchant_id":114,"m_lng":"113.367631","m_lat":"23.130781","type":3},{"merchant_id":115,"m_lng":"113.367631","m_lat":"23.130781","type":2},{"merchant_id":112,"m_lng":"113.367631","m_lat":"23.130781","type":1}]
             */

            private String lng;
            private String lat;
            private List<ResultSetBean> resultSet;

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", lng='" + lng + '\'' +
                        ", lat='" + lat + '\'' +
                        ", resultSet=" + resultSet +
                        '}';
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public List<ResultSetBean> getResultSet() {
                return resultSet;
            }

            public void setResultSet(List<ResultSetBean> resultSet) {
                this.resultSet = resultSet;
            }

            public static class ResultSetBean {
                /**
                 * merchant_id : 114
                 * m_lng : 113.367631
                 * m_lat : 23.130781
                 * type : 3
                 */

                private String merchant_id;
                private String m_lng;
                private String m_lat;
                private int type;

                @Override
                public String toString() {
                    return "ResultSetBean{" +
                            "merchant_id=" + merchant_id +
                            ", m_lng='" + m_lng + '\'' +
                            ", m_lat='" + m_lat + '\'' +
                            ", type=" + type +
                            '}';
                }

                public String getMerchant_id() {
                    return merchant_id;
                }

                public void setMerchant_id(String merchant_id) {
                    this.merchant_id = merchant_id;
                }

                public String getM_lng() {
                    return m_lng;
                }

                public void setM_lng(String m_lng) {
                    this.m_lng = m_lng;
                }

                public String getM_lat() {
                    return m_lat;
                }

                public void setM_lat(String m_lat) {
                    this.m_lat = m_lat;
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
}


