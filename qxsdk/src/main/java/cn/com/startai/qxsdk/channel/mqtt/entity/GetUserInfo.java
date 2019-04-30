package cn.com.startai.qxsdk.channel.mqtt.entity;


import java.io.Serializable;
import java.util.List;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.busi.IThirdAccountType;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.db.bean.UserBean;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 查询用户信息
 * Created by Robin on 2018/7/11.
 * qq: 419109715 彬影
 */

public class GetUserInfo implements Serializable, IThirdAccountType {

    public static String MSG_DESC = "查询用户信息 ";

    public static class Req {

        private String userid;
        private Integer loginType;

        public Req(String userid, Integer loginType) {
            this.userid = userid;
            this.loginType = loginType;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "userid='" + userid + '\'' +
                    ", loginType=" + loginType +
                    '}';
        }

        public Integer getLoginType() {
            return loginType;
        }

        public void setLoginType(Integer loginType) {
            this.loginType = loginType;
        }


        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }


    /**
     * 查询用户信息返回
     */
    public static class Resp extends BaseMessage implements Serializable {

        private ContentBean content;
        private UserBean userBean;

        @Override
        public String toString() {
            return "Resp{" +
                    "content=" + content +
                    ", userBean=" + userBean +
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

        public UserBean getUserBean() {
            return userBean;
        }

        public void setUserBean(UserBean userBean) {
            this.userBean = userBean;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends QXError implements Serializable {


            /**
             * userid :
             * userName : Robin
             * birthday : 1991-10-15
             * province : 广东省
             * city : 广州市
             * town : 天河区
             * address : 中山大道
             * nickName : 会飞的企鹅
             * headPic : http://ggg.pic
             * sex : 男
             * firstName : 罗
             * lastName : 彬心
             */

            private String userid = null;
            private String userName = null;
            private String birthday = null;
            private String province = null;
            private String city = null;
            private String town = null;
            private String address = null;
            private String nickName = null;
            private String headPic = null;
            private String sex = null;
            private String firstName = null;
            private String email = null;
            private String mobile = null;
            private String lastName = null;
            private int isHavePwd;

            private Req errcontent;
            private List<ThirdInfosBean> thirdInfos;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", userName='" + userName + '\'' +
                        ", birthday='" + birthday + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", town='" + town + '\'' +
                        ", address='" + address + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", headPic='" + headPic + '\'' +
                        ", sex='" + sex + '\'' +
                        ", firstName='" + firstName + '\'' +
                        ", email='" + email + '\'' +
                        ", mobile='" + mobile + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", isHavePwd=" + isHavePwd +
                        ", errcontent=" + errcontent +
                        ", thirdInfos=" + thirdInfos +
                        '}';
            }


            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public int getIsHavePwd() {
                return isHavePwd;
            }

            public void setIsHavePwd(int isHavePwd) {
                this.isHavePwd = isHavePwd;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getTown() {
                return town;
            }

            public void setTown(String town) {
                this.town = town;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public List<ThirdInfosBean> getThirdInfos() {
                return thirdInfos;
            }

            public void setThirdInfos(List<ThirdInfosBean> thirdInfos) {
                this.thirdInfos = thirdInfos;
            }


            public static class ThirdInfosBean {
                /**
                 * nickName : 微信
                 * type : 10
                 */
                private String nickName;
                private int type;

                @Override
                public String toString() {
                    return "ThirdInfosBean{" +
                            "nickName='" + nickName + '\'' +
                            ", type=" + type +
                            '}';
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }
            }
        }
    }

    /**
     * 请求查询 用户信息返回结果
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


            QXLog.e(TAG, "查询用户信息成功");
        } else {
            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setUserid(errcontent.getUserid());

            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        callBack.onGetUserInfoResult(resp);
    }
}
