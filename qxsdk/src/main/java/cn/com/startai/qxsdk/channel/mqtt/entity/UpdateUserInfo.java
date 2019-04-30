package cn.com.startai.qxsdk.channel.mqtt.entity;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.db.bean.UserBean;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;


/**
 * 更新用户信息
 * Created by Robin on 2018/7/11.
 * qq: 419109715 彬影
 */

public class UpdateUserInfo {

    public static String MSG_DESC = "更新用户信息 ";

    public static class Req {

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
        private String country = null;
        private String town = null;
        private String address = null;
        private String nickName = null;
        private String headPic = null;
        private String sex = null;
        private String firstName = null;
        private String lastName = null;


        @Override
        public String toString() {
            return "ContentBean{" +
                    "userid='" + userid + '\'' +
                    ", userName='" + userName + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", country='" + country + '\'' +
                    ", town='" + town + '\'' +
                    ", address='" + address + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", headPic='" + headPic + '\'' +
                    ", sex='" + sex + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }


        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
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


    }


    /**
     * 更新用户信息返回
     */
    public static class Resp extends BaseMessage {

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

        public static class ContentBean extends QXError {


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
            private String country = null;
            private String province = null;
            private String city = null;
            private String town = null;
            private String address = null;
            private String nickName = null;
            private String headPic = null;
            private String sex = null;
            private String firstName = null;
            private String lastName = null;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", userName='" + userName + '\'' +
                        ", birthday='" + birthday + '\'' +
                        ", country='" + country + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", town='" + town + '\'' +
                        ", address='" + address + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", headPic='" + headPic + '\'' +
                        ", sex='" + sex + '\'' +
                        ", firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }


            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
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


        }
    }

    /**
     * 请求更新用户信息返回结果
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


            QXLog.e(TAG, "用户信息更新成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setAddress(errcontent.getAddress());
            content.setBirthday(errcontent.getBirthday());
            content.setCity(errcontent.getCity());
            content.setCountry(errcontent.getCountry());
            content.setFirstName(errcontent.getFirstName());
            content.setLastName(errcontent.getLastName());
            content.setHeadPic(errcontent.getHeadPic());
            content.setTown(errcontent.getTown());
            content.setSex(errcontent.getSex());
            content.setUserName(errcontent.getUserName());
            content.setUserid(errcontent.getUserid());
            content.setProvince(errcontent.getProvince());
            content.setNickName(errcontent.getNickName());
            content.setAddress(errcontent.getAddress());


            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());

        }

        callBack.onUpdateUserInfoResult(resp);

    }
}
