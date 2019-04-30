package cn.com.startai.qxsdk.busi.charger.entity;

import java.io.Serializable;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 请求用余额支付订单
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class PayForOrderWithBalance implements Serializable {

    public static final String MIOF_DESC = "请求用余额支付订单";


    /**
     * 请求用余额支付订单 返回结果
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
            content.setNo(errcontent.getNo());

            QXLog.d(TAG, MIOF_DESC + "失败");
        }
        listener.onPayForOrderWithBalanceResult(resp);

    }


    public static class Req {

        private String no;

        public Req(String no) {
            this.no = no;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "no='" + no + '\'' +
                    '}';
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
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
             * no : LENT2018112817163224552973294Y
             * balance_deduction : 0
             * fee : 0
             * need_pay_fee : 0
             * discount_fee : 0
             */

            private String no;
            private int balance_deduction;
            private int fee;
            private int need_pay_fee;
            private int discount_fee;


            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", no='" + no + '\'' +
                        ", balance_deduction=" + balance_deduction +
                        ", fee=" + fee +
                        ", need_pay_fee=" + need_pay_fee +
                        ", discount_fee=" + discount_fee +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public int getBalance_deduction() {
                return balance_deduction;
            }

            public void setBalance_deduction(int balance_deduction) {
                this.balance_deduction = balance_deduction;
            }

            public int getFee() {
                return fee;
            }

            public void setFee(int fee) {
                this.fee = fee;
            }

            public int getNeed_pay_fee() {
                return need_pay_fee;
            }

            public void setNeed_pay_fee(int need_pay_fee) {
                this.need_pay_fee = need_pay_fee;
            }

            public int getDiscount_fee() {
                return discount_fee;
            }

            public void setDiscount_fee(int discount_fee) {
                this.discount_fee = discount_fee;
            }
        }
    }
}


