package cn.com.startai.qxsdk.busi.charger.entity;


import java.io.Serializable;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.entity.ThirdPaymentUnifiedOrder;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 请求充值
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class RequestRecharge implements Serializable {

    private static final String MIOF_DESC = "请求充值";


    public static final int PLATFORM_WECHAT = 1;//微信支付
    public static final int PLATFORM_ALIPAY = 2;//支付宝支付
    public static final int PLATFORM_EGHL = 4;//EGHL 支付

    public static final int TYPE_BALANCE = 1;//余额
    public static final int TYPE_DEPOSIT = 2;//押金


    /**
     * 请求 费率 返回结果
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

            QXLog.e(TAG, MIOF_DESC + "成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setFee(errcontent.getFee());
            content.setUserId(errcontent.getUserId());
            content.setType(errcontent.getType());
            content.setPlatform(errcontent.getPlatform());

            QXLog.e(TAG, MIOF_DESC + "失败");
        }


        listener.onRequestRechargeResult(resp);

    }


    public static class Req {

        /**
         * userId : userId
         * fee : 100
         * platform : 1
         * type : 1
         */

        private String userId;
        private int fee;
        private int platform;
        private int type;

        public Req(int fee, int platform, int type) {
            this.fee = fee;
            this.platform = platform;
            this.type = type;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "userId='" + userId + '\'' +
                    ", fee=" + fee +
                    ", platform=" + platform +
                    ", type=" + type +
                    '}';
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
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


            /**
             * userId : userId
             * fee : 100
             * type : 1
             * platform : 1
             * orderInfo : {"body":"余额充值","out_trade_no":"20150806125346","fee_type":"CNY","total_fee":100,"trade_type":"APP"}
             */

            private String userId;
            private int fee;
            private int type;
            private int platform;
            private OrderInfoBean orderInfo;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userId='" + userId + '\'' +
                        ", fee=" + fee +
                        ", type=" + type +
                        ", platform=" + platform +
                        ", orderInfo=" + orderInfo +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public int getFee() {
                return fee;
            }

            public void setFee(int fee) {
                this.fee = fee;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getPlatform() {
                return platform;
            }

            public void setPlatform(int platform) {
                this.platform = platform;
            }

            public OrderInfoBean getOrderInfo() {
                return orderInfo;
            }

            public void setOrderInfo(OrderInfoBean orderInfo) {
                this.orderInfo = orderInfo;
            }

            public ThirdPaymentUnifiedOrder.Req toThirdPayRequestBean() {
                ThirdPaymentUnifiedOrder.Req contentBean = new ThirdPaymentUnifiedOrder.Req(type, platform, orderInfo.getOut_trade_no(), orderInfo.body, orderInfo.getFee_type(), orderInfo.getTotal_fee() + "");
                return contentBean;
            }

            public static class OrderInfoBean {
                /**
                 * body : 余额充值
                 * out_trade_no : 20150806125346
                 * fee_type : CNY
                 * total_fee : 100
                 * trade_type : APP
                 */

                private String body;
                private String out_trade_no;
                private String fee_type;
                private int total_fee;
                private String trade_type;


                public String getBody() {
                    return body;
                }

                public void setBody(String body) {
                    this.body = body;
                }

                public String getOut_trade_no() {
                    return out_trade_no;
                }

                public void setOut_trade_no(String out_trade_no) {
                    this.out_trade_no = out_trade_no;
                }

                public String getFee_type() {
                    return fee_type;
                }

                public void setFee_type(String fee_type) {
                    this.fee_type = fee_type;
                }

                public int getTotal_fee() {
                    return total_fee;
                }

                public void setTotal_fee(int total_fee) {
                    this.total_fee = total_fee;
                }

                public String getTrade_type() {
                    return trade_type;
                }

                public void setTrade_type(String trade_type) {
                    this.trade_type = trade_type;
                }

                @Override
                public String toString() {
                    return "OrderInfoBean{" +
                            "body='" + body + '\'' +
                            ", out_trade_no='" + out_trade_no + '\'' +
                            ", fee_type='" + fee_type + '\'' +
                            ", total_fee=" + total_fee +
                            ", trade_type='" + trade_type + '\'' +
                            '}';
                }
            }
        }
    }


}
