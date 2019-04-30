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
 * 查询网点信息
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetStoreInfo implements Serializable {

    public static final String MIOF_DESC = "查询网点信息";

    public static final int TYPE_MOBILE = 1;
    public static final int TYPE_SIT = 2;
    public static final int TYPE_MOBILE_AND_SIT = 3;




    public static class Req {
        /**
         * lng : 113.367631
         * lat : 23.130691
         * merchantId:123
         */

        private String lng;
        private String lat;

        private String merchantId;

        @Override
        public String toString() {
            return "ContentBean{" +
                    "lng='" + lng + '\'' +
                    ", lat='" + lat + '\'' +
                    ", merchantId=" + merchantId +
                    '}';
        }

        public Req(String lng, String lat, String merchantId) {
            this.lng = lng;
            this.lat = lat;
            this.merchantId = merchantId;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
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
             * merchantId : 114
             * name : 亓行
             * address : 广东省广州市天河区建中路3号
             * telephone : 13332965499
             * openTime : 09:00-18:00
             * consumption : 888
             * fullyCount : 0
             * vacancyCount : 0
             * logo : img/002.jpg
             * banners : ["img/001.jpg","img/002.jpg","img/003.jpg"]
             * type : 1
             * lng : 113.367631
             * lat : 23.130691
             * distance : 1000
             */

            private String merchantId;
            private String name;
            private String address;
            private String telephone;
            private String openTime;
            private int consumption;
            private int fullyCount;
            private int vacancyCount;
            private String logo;
            private int type;
            private String lng;
            private String lat;
            private int distance;
            private List<String> banners;


            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", merchantId=" + merchantId +
                        ", name='" + name + '\'' +
                        ", address='" + address + '\'' +
                        ", telephone='" + telephone + '\'' +
                        ", openTime='" + openTime + '\'' +
                        ", consumption=" + consumption +
                        ", fullyCount=" + fullyCount +
                        ", vacancyCount=" + vacancyCount +
                        ", logo='" + logo + '\'' +
                        ", type=" + type +
                        ", lng='" + lng + '\'' +
                        ", lat='" + lat + '\'' +
                        ", distance=" + distance +
                        ", banners=" + banners +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getMerchantId() {
                return merchantId;
            }

            public void setMerchantId(String merchantId) {
                this.merchantId = merchantId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public int getConsumption() {
                return consumption;
            }

            public void setConsumption(int consumption) {
                this.consumption = consumption;
            }

            public int getFullyCount() {
                return fullyCount;
            }

            public void setFullyCount(int fullyCount) {
                this.fullyCount = fullyCount;
            }

            public int getVacancyCount() {
                return vacancyCount;
            }

            public void setVacancyCount(int vacancyCount) {
                this.vacancyCount = vacancyCount;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
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

            public int getDistance() {
                return distance;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public List<String> getBanners() {
                return banners;
            }

            public void setBanners(List<String> banners) {
                this.banners = banners;
            }
        }
    }
    /**
     * 请求 App用户信息换取系统统一userId 返回结果
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
            content.setMerchantId(errcontent.getMerchantId());
            QXLog.d(TAG, MIOF_DESC + "失败");
        }
        listener.onGetStoreInfoResult(resp);

    }
}
