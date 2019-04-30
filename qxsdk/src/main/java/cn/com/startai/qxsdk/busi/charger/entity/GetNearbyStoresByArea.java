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
 * 请求 按区域查询门店信息
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetNearbyStoresByArea implements Serializable {

    public static final String MIOF_DESC = "按区域查询门店信息";

    public static final int CHARGINGTYPE_MOBILE = 1;
    public static final int CHARGINGTYPE_SIT = 2;
    public static final int CHARGINGTYPE_MOBILE_AND_SIT = 3;


    /**
     * 请求 按区域查询门店信息 返回结果
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
            content.setCount(errcontent.getCount());
            content.setDevice_type(errcontent.getDevice_type());
            content.setPage(errcontent.getPage());
            content.setRegion(errcontent.getRegion());

            QXLog.d(TAG, MIOF_DESC + "失败");
        }

        listener.onGetNearbyStoreByAreaResult(resp);

    }


    public static class Req {

        /**
         * device_type : 1
         * region : 广州市天河区
         * lng : 113.367631
         * lat : 23.130781
         * page : 1
         * count : 10
         */

        private int device_type = CHARGINGTYPE_MOBILE_AND_SIT;

        private String region;
        private String lng;
        private String lat;
        private int page;
        private int count;

        public Req(int device_type, String region, String lng, String lat, int page, int count) {
            this.device_type = device_type;
            this.region = region;
            this.lng = lng;
            this.lat = lat;
            this.page = page;
            this.count = count;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "device_type=" + device_type +
                    ", region='" + region + '\'' +
                    ", lng='" + lng + '\'' +
                    ", lat='" + lat + '\'' +
                    ", page=" + page +
                    ", count=" + count +
                    '}';
        }

        public int getDevice_type() {
            return device_type;
        }

        public void setDevice_type(int device_type) {
            this.device_type = device_type;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
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

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
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
             * device_type : 1
             * region : 天河区
             * page : 1
             * count : 10
             * total_page : 10
             * data : [{"merchantId":110,"name":"尊宝比萨","address":"广东省深圳市福田区水围七街4号","openTime":"9:00-22:00","consumption":100,"fullyCount":0,"vacancyCount":3,"logo":"img/002.jpg","type":1,"distance":1000,"lng":"113.367631","lat":"23.130781"}]
             */

            private int device_type;
            private String region;
            private int page;
            private int count;
            private int total_page;
            private List<DataBean> data;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", device_type=" + device_type +
                        ", region='" + region + '\'' +
                        ", page=" + page +
                        ", count=" + count +
                        ", total_page=" + total_page +
                        ", data=" + data +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public int getDevice_type() {
                return device_type;
            }

            public void setDevice_type(int device_type) {
                this.device_type = device_type;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getTotal_page() {
                return total_page;
            }

            public void setTotal_page(int total_page) {
                this.total_page = total_page;
            }

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * merchantId : 110
                 * name : 尊宝比萨
                 * address : 广东省深圳市福田区水围七街4号
                 * openTime : 9:00-22:00
                 * consumption : 100
                 * fullyCount : 0
                 * vacancyCount : 3
                 * logo : img/002.jpg
                 * type : 1
                 * distance : 1000
                 * lng : 113.367631
                 * lat : 23.130781
                 */

                private String merchantId;
                private String name;
                private String address;
                private String openTime;
                private int consumption;
                private int fullyCount;
                private int vacancyCount;
                private String logo;
                private int type;
                private int distance;
                private String lng;
                private String lat;


                @Override
                public String toString() {
                    return "DataBean{" +
                            "merchantId=" + merchantId +
                            ", name='" + name + '\'' +
                            ", address='" + address + '\'' +
                            ", openTime='" + openTime + '\'' +
                            ", consumption=" + consumption +
                            ", fullyCount=" + fullyCount +
                            ", vacancyCount=" + vacancyCount +
                            ", logo='" + logo + '\'' +
                            ", type=" + type +
                            ", distance=" + distance +
                            ", lng='" + lng + '\'' +
                            ", lat='" + lat + '\'' +
                            '}';
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

                public int getDistance() {
                    return distance;
                }

                public void setDistance(int distance) {
                    this.distance = distance;
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
        }
    }
}


