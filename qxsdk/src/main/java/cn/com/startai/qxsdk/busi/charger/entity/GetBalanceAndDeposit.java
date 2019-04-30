package cn.com.startai.qxsdk.busi.charger.entity;


import java.io.Serializable;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 查询押金 和余额
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetBalanceAndDeposit implements Serializable {

    public static final String MIOF_DESC = "请求查询押金和余额";


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

            QXLog.d(TAG, MIOF_DESC + "成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setUserId(errcontent.getUserId());

            QXLog.d(TAG, MIOF_DESC + "失败");
        }
        listener.onGetBalanceAndDepositResult(resp);


    }


    public static class Req {

        /**
         * userId : userId
         * fee : 100
         * platform : 1
         * type : 1
         */

        private String userId;

        @Override
        public String toString() {
            return "ContentBean{" +
                    "userId='" + userId + '\'' +
                    '}';
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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
             * amount : 2000
             * deposit : 10000
             */

            private String userId;
            private int amount;
            private int deposit;
            private Req errcontent;


            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userId='" + userId + '\'' +
                        ", amount=" + amount +
                        ", deposit=" + deposit +
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

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public int getDeposit() {
                return deposit;
            }

            public void setDeposit(int deposit) {
                this.deposit = deposit;
            }
        }
    }
}


