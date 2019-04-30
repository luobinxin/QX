package cn.com.startai.qxsdk.channel.mqtt.entity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.com.startai.qxsdk.channel.mqtt.busi.IThirdAccountType;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;

/**
 * 第三方登录
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class LoginWithThirdAccount implements Serializable, IThirdAccountType {

    public static String MSG_DESC = "第三方登录 ";


    public static class Req {

        /**
         * code :
         * type : 1
         * userinfo : {"openid":"OPENID","nickname":"NICKNAME","email":"email","sex":1,"province":"PROVINCE","city":"CITY","country":"COUNTRY","headimgurl":"http://url","username":"username","firstName":"firstName","lastName":"lastName","address":"address","unionid":"unionid"}
         */

        private int type;
        private String code;
        private UserinfoBean userinfo;

        public Req() {
        }

        public Req(int type, UserinfoBean userinfo) {
            this.type = type;
            this.userinfo = userinfo;
        }

        public Req(int type, String code) {
            this.type = type;
            this.code = code;
        }

        public void fromFacebookJSONObject(JSONObject facebookJSONObject) {


//                {
//                    "id":"109110993572667",
//                        "name":"罗彬彬",
//                        "picture":{
//                    "data":{
//                        "height":50,
//                                "is_silhouette":true,
//                                "url":"https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=109110993572667&height=50&width=50&ext=1553743839&hash=AeQ6aK5Id-8t4Ky3",
//                                "width":50
//                    }
//                },
//                    "first_name":"彬彬",
//                        "last_name":"罗"
//                }


            this.type = THIRD_FACEBOOK;
            this.userinfo = new UserinfoBean();

            String id = facebookJSONObject.optString("id");   //比如:1565455221565
            String name = facebookJSONObject.optString("name");  //比如：Zhang San
            String first_name = facebookJSONObject.optString("first_name");
            String last_name = facebookJSONObject.optString("last_name");

            //获取用户头像
            JSONObject object_pic = facebookJSONObject.optJSONObject("picture");
            JSONObject object_data = object_pic.optJSONObject("data");
            String url = object_data.optString("url");

            this.userinfo.setUnionid(id);
            this.userinfo.setOpenid(id);
            this.userinfo.setFirstName(first_name);
            this.userinfo.setLastName(last_name);
            this.userinfo.setNickname(name);
            this.userinfo.setHeadimgurl(url);
        }

        public void fromFacebookJson(String facebookJson) throws JSONException {
            fromFacebookJSONObject(new JSONObject(facebookJson));
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "code='" + code + '\'' +
                    ", type=" + type +
                    ", userinfo=" + userinfo +
                    '}';
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public UserinfoBean getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserinfoBean userinfo) {
            this.userinfo = userinfo;
        }

        public static class UserinfoBean {
            /**
             * openid : OPENID
             * nickname : NICKNAME
             * email : email
             * sex : 1
             * province : PROVINCE
             * city : CITY
             * country : COUNTRY
             * headimgurl : http://url
             * username : username
             * firstName : firstName
             * lastName : lastName
             * address : address
             * unionid : unionid
             */

            private String openid;
            private String nickname;
            private String email;
            private int sex;
            private String province;
            private String city;
            private String country;
            private String headimgurl;
            private String username;
            private String firstName;
            private String lastName;
            private String address;
            private String unionid;


            @Override
            public String toString() {
                return "UserinfoBean{" +
                        "openid='" + openid + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", email='" + email + '\'' +
                        ", sex=" + sex +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", country='" + country + '\'' +
                        ", headimgurl='" + headimgurl + '\'' +
                        ", username='" + username + '\'' +
                        ", firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", address='" + address + '\'' +
                        ", unionid='" + unionid + '\'' +
                        '}';
            }

            public String getOpenid() {
                return openid;
            }

            public void setOpenid(String openid) {
                this.openid = openid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
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

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getHeadimgurl() {
                return headimgurl;
            }

            public void setHeadimgurl(String headimgurl) {
                this.headimgurl = headimgurl;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
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

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getUnionid() {
                return unionid;
            }

            public void setUnionid(String unionid) {
                this.unionid = unionid;
            }
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
             * userid :
             * token :
             * expire_in : 7200
             */

            private String userid;
            private String token;
            private int expire_in;
            private int type;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", token='" + token + '\'' +
                        ", expire_in=" + expire_in +
                        ", type=" + type +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public ContentBean() {
            }

            public ContentBean(String userid, String token, int expire_in, int type) {
                this.userid = userid;
                this.token = token;
                this.expire_in = expire_in;
                this.type = type;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public int getExpire_in() {
                return expire_in;
            }

            public void setExpire_in(int expire_in) {
                this.expire_in = expire_in;
            }
        }
    }


    /**
     * 请求 第三方登录 返回结果
     *
     * @param miof
     */
    public static void handlerMsg(String miof, OnMqttTaskCallBack callback) {

        Login.handlerMsg(miof, callback);

    }
}
