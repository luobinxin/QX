package cn.com.startai.qxsdk.busi.charger.entity;


import java.io.Serializable;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 查询押金费率
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetDepositFeeRule implements Serializable {

    public static final String MIOF_DESC = "查询押金费率";

    public static final int DEPOSITENEED_TRUE = 1;
    public static final int DEPOSITENEED_FALSE = 2;


    /**
     * 查询押金费率 返回结果
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

        listener.onGetDepositFeeRuleResult(resp);

    }


    public static class Req {


        /**
         * userId :
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


            private Req errcontent;


            /**
             * userId :
             * id : 1元/小时
             * depFee : 100
             * isDeposit : 1
             * remark : 500
             */

            private String userId;
            private String id;
            private int depFee;
            private int isDeposit;
            private String remark;

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
                        ", userId='" + userId + '\'' +
                        ", id='" + id + '\'' +
                        ", depFee=" + depFee +
                        ", isDeposit=" + isDeposit +
                        ", remark=" + remark +
                        '}';
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getDepFee() {
                return depFee;
            }

            public void setDepFee(int depFee) {
                this.depFee = depFee;
            }

            public int getIsDeposit() {
                return isDeposit;
            }

            public void setIsDeposit(int isDeposit) {
                this.isDeposit = isDeposit;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }
    }
}


