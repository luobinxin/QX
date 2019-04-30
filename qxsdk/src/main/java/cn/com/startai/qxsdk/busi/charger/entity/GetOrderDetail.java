package cn.com.startai.qxsdk.busi.charger.entity;

import java.io.Serializable;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 请求查询订单详情
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetOrderDetail implements Serializable {

    public static final String MIOF_DESC = "请求查询订单详情";


    /**
     * 请求查询订单详情 返回结果
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

        listener.onGetOrderDetailResult(resp);


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

        public static class ContentBean extends QXError implements Serializable {


            private Req errcontent;
            /**
             * no : LENT2018012821520607070620574F
             * lent_time : 2018-06-23 18：00：00
             * lent_adress : 广东省广州市天河区建中路3号
             * return_time : 2018-06-23 18：00：00
             * return_adress : 广东省广州市天河区建中路3号
             * duration : 3天2小时12分钟
             * fee : 100
             * pay_state : 1
             * discount_fee : 50
             * balance_deduction : 50
             * need_pay_fee : 0
             * total_page : 10
             */
            private String no;
            private String lent_time;
            private String lent_adress;
            private String return_time;
            private String return_adress;
            private String duration;
            private int fee;
            private int pay_state;
            private int discount_fee;
            private int balance_deduction;
            private int need_pay_fee;


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
                        ", no='" + no + '\'' +
                        ", lent_time='" + lent_time + '\'' +
                        ", lent_adress='" + lent_adress + '\'' +
                        ", return_time='" + return_time + '\'' +
                        ", return_adress='" + return_adress + '\'' +
                        ", duration='" + duration + '\'' +
                        ", fee=" + fee +
                        ", pay_state=" + pay_state +
                        ", discount_fee=" + discount_fee +
                        ", balance_deduction=" + balance_deduction +
                        ", need_pay_fee=" + need_pay_fee +

                        '}';
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getLent_time() {
                return lent_time;
            }

            public void setLent_time(String lent_time) {
                this.lent_time = lent_time;
            }

            public String getLent_adress() {
                return lent_adress;
            }

            public void setLent_adress(String lent_adress) {
                this.lent_adress = lent_adress;
            }

            public String getReturn_time() {
                return return_time;
            }

            public void setReturn_time(String return_time) {
                this.return_time = return_time;
            }

            public String getReturn_adress() {
                return return_adress;
            }

            public void setReturn_adress(String return_adress) {
                this.return_adress = return_adress;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public int getFee() {
                return fee;
            }

            public void setFee(int fee) {
                this.fee = fee;
            }

            public int getPay_state() {
                return pay_state;
            }

            public void setPay_state(int pay_state) {
                this.pay_state = pay_state;
            }

            public int getDiscount_fee() {
                return discount_fee;
            }

            public void setDiscount_fee(int discount_fee) {
                this.discount_fee = discount_fee;
            }

            public int getBalance_deduction() {
                return balance_deduction;
            }

            public void setBalance_deduction(int balance_deduction) {
                this.balance_deduction = balance_deduction;
            }

            public int getNeed_pay_fee() {
                return need_pay_fee;
            }

            public void setNeed_pay_fee(int need_pay_fee) {
                this.need_pay_fee = need_pay_fee;
            }


        }
    }
}


