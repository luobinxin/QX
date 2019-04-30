package cn.com.startai.qxsdk.busi.charger.entity;


import java.io.Serializable;
import java.util.ArrayList;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 请求查询订单列表
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetOrderList implements Serializable {

    public static final String MIOF_DESC = "请求查询订单列表";

    public static int TYPE_MOBILE = 1;
    public static int TYPE_FIXED = 2;
    public static int TYPE_FIXED_AND_MOBILE = 3;


    /**
     * 请求查询订单列表 返回结果
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
            content.setUserId(errcontent.getUserId());
            content.setPage(errcontent.getPage());
            content.setCount(errcontent.getCount());
            content.setType(errcontent.getType());

            QXLog.d(TAG, MIOF_DESC + "失败");
        }
        listener.onGetOrderListResult(resp);

    }


    public static class Req {
        /**
         * userId : userId
         * fee : 100
         * platform : 1
         * type : 1
         */

        private String userId;

        private int page; //第几页
        private int count; // 每页显示条目数
        private int type; //1充电宝 2座充 3充电宝+座充


        public Req(int page, int count, int type) {
            this.page = page;
            this.count = count;
            this.type = type;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "userId='" + userId + '\'' +
                    ", page=" + page +
                    ", count=" + count +
                    ", type=" + type +
                    '}';
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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


            private String userId;

            private int page; //第几页
            private int count; // 每页显示条目数
            private int type; //1充电宝 2座充 3充电宝+座充
            private int total_page; //总页数
            private ArrayList<OrderBean> orders;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userId='" + userId + '\'' +
                        ", page=" + page +
                        ", count=" + count +
                        ", type=" + type +
                        ", orders=" + orders +
                        ", errcontent=" + errcontent +
                        ", total_page=" + total_page +
                        '}';
            }

            public int getTotal_page() {
                return total_page;
            }

            public void setTotal_page(int total_page) {
                this.total_page = total_page;
            }

            public ContentBean() {
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public ArrayList<OrderBean> getOrders() {
                return orders;
            }

            public void setOrders(ArrayList<OrderBean> orders) {
                this.orders = orders;
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public static class OrderBean {


                /**
                 * no : LENT2018012821520607070620574F
                 * fee : 100
                 * lent_time : 2018-06-23 18：00：00
                 * duration  : 3天2小时12分钟
                 * lent_adress : 广东省广州市天河区建中路3号
                 * pay_state : 1
                 */

                private String no;
                private int fee;
                private String lent_time;
                private String duration;
                private String lent_adress;
                private int pay_state;

                public OrderBean() {
                }

                public OrderBean(String no, int fee, String lent_time, String duration, String lent_adress, int pay_state) {
                    this.no = no;
                    this.fee = fee;
                    this.lent_time = lent_time;
                    this.duration = duration;
                    this.lent_adress = lent_adress;
                    this.pay_state = pay_state;
                }

                @Override
                public String toString() {
                    return "OrderBean{" +
                            "no='" + no + '\'' +
                            ", fee=" + fee +
                            ", lent_time='" + lent_time + '\'' +
                            ", duration='" + duration + '\'' +
                            ", lent_adress='" + lent_adress + '\'' +
                            ", pay_state=" + pay_state +
                            '}';
                }

                public String getNo() {
                    return no;
                }

                public void setNo(String no) {
                    this.no = no;
                }

                public int getFee() {
                    return fee;
                }

                public void setFee(int fee) {
                    this.fee = fee;
                }

                public String getLent_time() {
                    return lent_time;
                }

                public void setLent_time(String lent_time) {
                    this.lent_time = lent_time;
                }

                public String getDuration() {
                    return duration;
                }

                public void setDuration(String duration) {
                    this.duration = duration;
                }

                public String getLent_adress() {
                    return lent_adress;
                }

                public void setLent_adress(String lent_adress) {
                    this.lent_adress = lent_adress;
                }

                public int getPay_state() {
                    return pay_state;
                }

                public void setPay_state(int pay_state) {
                    this.pay_state = pay_state;
                }
            }

        }
    }
}


