package cn.com.startai.qxsdk.channel.mqtt.entity;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.com.startai.qxsdk.QXUserManager;
import cn.com.startai.qxsdk.channel.mqtt.busi.MiofTag;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.db.bean.UserBean;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 统一下单
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class ThirdPaymentUnifiedOrder implements Serializable {

    public static String MSG_DESC = "统一下单 ";

    public static final int TYPE_DEPOSIT = 1; //押金
    public static final int TYPE_BALANCE = 2; //余额
    public static final int TYPE_ORDER = 3; //订单

    public static final int PLATFORM_WECHAT = 1;
    public static final int PLATFORM_ALIPAY = 2;


    public static class Req {
        private String userid;
        private int type;
        private int platform;
        private String order_num;
        private String goods_description;
        private String fee_type;
        private String total_fee;

        @Override
        public String toString() {
            return "ContentBean{" +
                    "userid='" + userid + '\'' +
                    ", type=" + type +
                    ", platform=" + platform +
                    ", order_num='" + order_num + '\'' +
                    ", goods_description='" + goods_description + '\'' +
                    ", fee_type='" + fee_type + '\'' +
                    ", total_fee='" + total_fee + '\'' +
                    '}';
        }

        public Req(String userid, int type, int platform, String order_num, String goods_description, String fee_type, String total_fee) {
            this.userid = userid;
            this.type = type;
            this.platform = platform;
            this.order_num = order_num;
            this.goods_description = goods_description;
            this.fee_type = fee_type;
            this.total_fee = total_fee;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public Req(int type, int platform, String order_num, String goods_description, String fee_type, String total_fee) {
            this.type = type;
            this.platform = platform;
            this.order_num = order_num;
            this.goods_description = goods_description;
            this.fee_type = fee_type;
            this.total_fee = total_fee;
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

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getGoods_description() {
            return goods_description;
        }

        public void setGoods_description(String goods_description) {
            this.goods_description = goods_description;
        }

        public String getFee_type() {
            return fee_type;
        }

        public void setFee_type(String fee_type) {
            this.fee_type = fee_type;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
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


            private String userid;
            private int type;
            private int platform;
            private String order_num;
            private String goods_description;
            private String fee_type;
            private String total_fee;

            private String appid;
            private String partnerid;
            private String prepayid;
            private String package_;
            private String noncestr;
            private String timestamp;
            private String sign;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", type=" + type +
                        ", platform=" + platform +
                        ", order_num='" + order_num + '\'' +
                        ", goods_description='" + goods_description + '\'' +
                        ", fee_type='" + fee_type + '\'' +
                        ", total_fee='" + total_fee + '\'' +
                        ", appid='" + appid + '\'' +
                        ", partnerid='" + partnerid + '\'' +
                        ", prepayid='" + prepayid + '\'' +
                        ", package_='" + package_ + '\'' +
                        ", noncestr='" + noncestr + '\'' +
                        ", timestamp='" + timestamp + '\'' +
                        ", sign='" + sign + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getPartnerid() {
                return partnerid;
            }

            public void setPartnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getPrepayid() {
                return prepayid;
            }

            public void setPrepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getPackage_() {
                return package_;
            }

            public void setPackage_(String package_) {
                this.package_ = package_;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getOrder_num() {
                return order_num;
            }

            public void setOrder_num(String order_num) {
                this.order_num = order_num;
            }

            public String getGoods_description() {
                return goods_description;
            }

            public void setGoods_description(String goods_description) {
                this.goods_description = goods_description;
            }

            private Req errcontent = null;


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


            public String getFee_type() {
                return fee_type;
            }

            public void setFee_type(String fee_type) {
                this.fee_type = fee_type;
            }

            public String getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(String total_fee) {
                this.total_fee = total_fee;
            }

            public String getWX_Appid() {
                return appid;
            }

            public void setWX_Appid(String appid) {
                this.appid = appid;
            }

            public String getWX_Partnerid() {
                return partnerid;
            }

            public void setWX_Partnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getWX_Prepayid() {
                return prepayid;
            }

            public void setWX_Prepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getWX_Package_() {
                return package_;
            }

            public void setWX_Package_(String package_) {
                this.package_ = package_;
            }

            public String getWX_Noncestr() {
                return noncestr;
            }

            public void setWX_Noncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getWX_Timestamp() {
                return timestamp;
            }

            public void setWX_Timestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getWX_Sign() {
                return sign;
            }

            public String getZFB_Sign() {
                return sign;
            }

            public void setWX_Sign(String sign) {
                this.sign = sign;
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

        }
    }

    /**
     * 请求 统一下单 返回结果
     *
     * @param miof
     */
    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {


        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            QXLog.e(TAG, "统一下单 成功");

            if (resp.getContent().getPlatform() == PLATFORM_WECHAT) {
                //package 是关键字 不能实体映射 需要单独取出
                try {
                    JSONObject jsonObject = new JSONObject(miof);
                    JSONObject content = jsonObject.getJSONObject(MiofTag.TAG_CONTENT);
                    String aPackage = content.getString("package");
                    resp.getContent().setWX_Package_(aPackage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(resp.getContent().getUserid())) {
                UserBean currUser = QXUserManager.getInstance().getUser();
                if (currUser != null && !TextUtils.isEmpty(currUser.getUserId())) {
                    resp.getContent().setUserid(currUser.getUserId());
                }
            }

        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setFee_type(errcontent.getFee_type());
            content.setGoods_description(errcontent.getGoods_description());
            content.setOrder_num(errcontent.getOrder_num());
            content.setType(errcontent.getType());
            content.setTotal_fee(errcontent.getTotal_fee());
            content.setPlatform(errcontent.getPlatform());
            content.setUserid(errcontent.getUserid());

            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        callBack.onThirdPaymentUnifiedOrderResult(resp);
    }
}
