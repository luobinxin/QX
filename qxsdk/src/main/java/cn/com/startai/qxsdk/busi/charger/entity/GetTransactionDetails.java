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
 * 查询交易明细
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetTransactionDetails implements Serializable {

    public static final String MIOF_DESC = "查询交易明细";

    public static final int TRANSACTIONTYPE_RECHARGER = 1;
    public static final int TRANSACTIONTYPE_CONSUMER = 2;
    public static final int TRANSACTIONTYPE_REFUND = 3;

    public static final int PAYTYPE_WECHAT = 1;
    public static final int PAYTYPE_ALIPAY = 2;
    public static final int PAYTYPE_BALANCE = 3;
    public static final int PAYTYPE_EGHL = 4;

    public static final int CHARGING_TYPE_MOBILE = 1;
    public static final int CHARGING_TYPE_SIT = 2;

    public static final int RECHARGER_TYPE_BALANCE = 1;
    public static final int RECHARGER_TYPE_DEPOSIT = 2;

    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_SHIPPED = "SHIPPED";
    public static final String RESULT_FAIL = "FAIL";


    /**
     * 查询交易明细 返回结果
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
            content.setCount(errcontent.getCount());
            content.setPage(errcontent.getPage());
            content.setTransactionType(errcontent.getTransactionType());

            QXLog.d(TAG, MIOF_DESC + "失败");
        }

        listener.onGetTransactionDetailsResult(resp);

    }


    public static class Req {

        /**
         * transactionType : 1
         * userId :
         * page : 1
         * count : 10
         */

        private int transactionType;
        private String userId;
        private int page;
        private int count;

        @Override
        public String toString() {
            return "ContentBean{" +
                    "transactionType=" + transactionType +
                    ", userId='" + userId + '\'' +
                    ", page=" + page +
                    ", count=" + count +
                    '}';
        }


        public Req(int transactionType, int page, int count) {
            this.transactionType = transactionType;
            this.page = page;
            this.count = count;
        }

        public int getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(int transactionType) {
            this.transactionType = transactionType;
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
             * transactionType : 1
             * userId :
             * page : 1
             * count : 10
             * data : [{"fee":1,"recharge_type":1,"pay_type":2,"time_end":"201812181149","result":"SUCCESS"}]
             */

            private int transactionType;
            private String userId;
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
                        ", transactionType=" + transactionType +
                        ", userId='" + userId + '\'' +
                        ", page=" + page +
                        ", count=" + count +
                        ", total_page=" + total_page +
                        ", data=" + data +
                        '}';
            }

            public int getTotal_page() {
                return total_page;
            }

            public void setTotal_page(int total_page) {
                this.total_page = total_page;
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public int getTransactionType() {
                return transactionType;
            }

            public void setTransactionType(int transactionType) {
                this.transactionType = transactionType;
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

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {


                /**
                 * fee : 1
                 * recharge_type : 1
                 * pay_type : 2
                 * time_end : 201812181149
                 * result : SUCCESS
                 */

                private int fee;//金额
                private int recharge_type;//1：充电宝，2：座充
                private int charging_type;//1：充值余额，2：充值押金
                private int pay_type;//付款方式，1：微信，2：支付宝
                private String time_end;//订单完成时间，格式:yyyyMMddHHmmss
                private String result; //充值结果，SUCCESS：成功，SHIPPED，未完成的支付，FAIL：支付失败  //消费结果，SUCCESS：成功，SHIPPED，未完成的支付，FAIL：失败

                @Override
                public String toString() {
                    return "DataBean{" +
                            "fee=" + fee +
                            ", recharge_type=" + recharge_type +
                            ", charging_type=" + charging_type +
                            ", pay_type=" + pay_type +
                            ", time_end='" + time_end + '\'' +
                            ", result='" + result + '\'' +
                            '}';
                }

                public int getCharging_type() {
                    return charging_type;
                }

                public void setCharging_type(int charging_type) {
                    this.charging_type = charging_type;
                }

                public int getFee() {
                    return fee;
                }

                public void setFee(int fee) {
                    this.fee = fee;
                }

                public int getRecharge_type() {
                    return recharge_type;
                }

                public void setRecharge_type(int recharge_type) {
                    this.recharge_type = recharge_type;
                }

                public int getPay_type() {
                    return pay_type;
                }

                public void setPay_type(int pay_type) {
                    this.pay_type = pay_type;
                }

                public String getTime_end() {
                    return time_end;
                }

                public void setTime_end(String time_end) {
                    this.time_end = time_end;
                }

                public String getResult() {
                    return result;
                }

                public void setResult(String result) {
                    this.result = result;
                }
            }
        }
    }
}


