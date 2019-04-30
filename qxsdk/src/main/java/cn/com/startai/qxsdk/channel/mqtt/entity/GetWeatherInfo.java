package cn.com.startai.qxsdk.channel.mqtt.entity;

import java.io.Serializable;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;


/**
 * 查询天气
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetWeatherInfo implements Serializable {

    public static String MSG_DESC = "查询天气 ";


    public static class Req {


        /**
         * lat :
         * lng :
         */

        private String lat;
        private String lng;

        public Req() {
        }

        public Req(String lat, String lng) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "lat='" + lat + '\'' +
                    ", lng='" + lng + '\'' +
                    '}';
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
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
             * lat :
             * lng :
             * province : 广东省
             * city : 广州市
             * district : 天河区
             * qlty : 优
             * tmp : 14
             * weather :
             * weatherPic :
             */

            private String lat;
            private String lng;
            private String province;
            private String city;
            private String district;
            private String qlty;
            private String tmp;
            private String weather;
            private String weatherPic;
            private String pubtime; // 天气发布时间

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", lat='" + lat + '\'' +
                        ", lng='" + lng + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", district='" + district + '\'' +
                        ", qlty='" + qlty + '\'' +
                        ", tmp='" + tmp + '\'' +
                        ", weather='" + weather + '\'' +
                        ", weatherPic='" + weatherPic + '\'' +
                        ", pubtime='" + pubtime + '\'' +
                        '}';
            }

            public String getPubtime() {
                return pubtime;
            }

            public void setPubtime(String pubtime) {
                this.pubtime = pubtime;
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getWeatherPic() {
                return weatherPic;
            }

            public void setWeatherPic(String weatherPic) {
                this.weatherPic = weatherPic;
            }
        }
    }

    /**
     * 请求 查询天气 返回结果
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
            GetWeatherInfo.Resp.ContentBean content = resp.getContent();
            GetWeatherInfo.Req errcontent = content.getErrcontent();
            content.setLat(errcontent.getLat());
            content.setLng(errcontent.getLng());
            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }

        callBack.onGetWeatherInfoResult(resp);

    }
}
