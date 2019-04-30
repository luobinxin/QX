package cn.com.startai.qxsdk.db.bean;

import android.text.TextUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.com.startai.qxsdk.channel.mqtt.busi.IThirdAccountType;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.Login;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.utils.QXJsonUtils;


/**
 * Created by Robin on 2018/10/16.
 * qq: 419109715 彬影
 */

@Table(name = UserBean.TABLE_NAME)
public class UserBean implements Serializable, IThirdAccountType {

    public static final String TABLE_NAME = "UserBean";

    @Column(name = F_ID, isId = true)
    private long _id; //自增 不参与业务
    public static final String F_ID = "_id";

    @Column(name = F_USERID)
    private String userId; //userid
    public static final String F_USERID = "userId";


    @Column(name = F_UPDATETIME)
    private long updateTime; //数据更新时间
    public static final String F_UPDATETIME = "updateTime";

    @Column(name = F_ADDTIME)
    private long addTime; //数据添加时间
    public static final String F_ADDTIME = "addTime";


    @Column(name = F_TOKEN)
    private String token; //token
    public static final String F_TOKEN = "token";

    @Column(name = F_EXPIREIN)
    private long expire_in; //expire_in //时效单位 分钟
    public static final String F_EXPIREIN = "expire_in";

    @Column(name = F_TYPE)
    private long type; //type 登录类型
    public static final String F_TYPE = "type";


    @Column(name = F_USERNAME)
    private String userName;
    public static final String F_USERNAME = "userName";

    @Column(name = F_BIRTHDAY)
    private String birthday;
    public static final String F_BIRTHDAY = "birthday";

    @Column(name = F_PROVINCE)
    private String province;
    public static final String F_PROVINCE = "province";

    @Column(name = F_CITY)
    private String city;
    public static final String F_CITY = "city";

    @Column(name = F_TOWN)
    private String town;
    public static final String F_TOWN = "town";


    @Column(name = F_ADDRESS)
    private String address;
    public static final String F_ADDRESS = "address";

    @Column(name = F_NICKNAME)
    private String nickName;
    public static final String F_NICKNAME = "nickName";

    @Column(name = F_HEADPIC)
    private String headPic;
    public static final String F_HEADPIC = "headPic";

    @Column(name = F_SEX)
    private String sex;
    public static final String F_SEX = "sex";

    @Column(name = F_FIRSTNAME)
    private String firstName;
    public static final String F_FIRSTNAME = "firstName";

    @Column(name = F_LASTNAME)
    private String lastName;
    public static final String F_LASTNAME = "lastName";

    @Column(name = F_EMAIL)
    private String email;
    public static final String F_EMAIL = "email";

    @Column(name = F_MOBILE)
    private String mobile;
    public static final String F_MOBILE = "mobile";

    @Column(name = F_ISHAVEPWD)
    private int isHavePwd;
    public static final String F_ISHAVEPWD = "isHavePwd";

    @Column(name = F_THIRDINFOS)
    private String thirdInfos;
    public static final String F_THIRDINFOS = "thirdInfos";

    @Column(name = "loginStatus")
    private int loginStatus;
    public static final String F_LOGINSTATUS = "loginStatus";

    private List<GetUserInfo.Resp.ContentBean.ThirdInfosBean> thirdInfoList;

    public UserBean() {
    }

    /**
     * 是否只有一个第三方账号绑定，此时不允许解绑
     *
     * @return
     */
    public boolean isOnlyOneThindBind() {
        if (TextUtils.isEmpty(getMobile())
                && TextUtils.isEmpty(getEmail())
                && getThirdInfoList() != null
                && getThirdInfoList().size() == 1) {
            return true;
        }
        return false;
    }

    public UserBean fromLoginResp(Login.Resp loginResp) {
        if (loginResp.getResult() == BaseMessage.RESULT_SUCCESS) {
            Login.Resp.ContentBean content = loginResp.getContent();
            setUserId(content.getUserid());
            setUserName(content.getUname());
            setToken(content.getToken());
            setType(content.getType());
            setExpire_in(content.getExpire_in());
            setLoginStatus(1);
        }
        return this;

    }

    public UserBean fromUpdateUserInfoResp(UpdateUserInfo.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            UpdateUserInfo.Resp.ContentBean content = resp.getContent();
            if (content.getAddress() != null) {
                setAddress(content.getAddress());
            }
            if (content.getBirthday() != null) {
                setBirthday(content.getBirthday());
            }
            if (content.getCity() != null) {
                setCity(content.getCity());
            }
            if (content.getFirstName() != null) {
                setFirstName(content.getFirstName());
            }
            if (content.getLastName() != null) {
                setLastName(content.getLastName());
            }
            if (content.getHeadPic() != null) {
                setHeadPic(content.getHeadPic());
            }
            if (content.getNickName() != null) {
                setNickName(content.getNickName());
            }
            if (content.getProvince() != null) {
                setProvince(content.getProvince());
            }
            if (content.getSex() != null) {
                setSex(content.getSex());
            }
            if (content.getTown() != null) {
                setTown(content.getTown());
            }
            if (content.getUserName() != null) {
                setUserName(content.getUserName());
            }
            if (content.getUserid() != null) {
                setUserId(content.getUserid());
            }
        }
        return this;
    }

    public UserBean fromGetUserInfoResp(GetUserInfo.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            GetUserInfo.Resp.ContentBean content = resp.getContent();
            setAddress(content.getAddress());
            setBirthday(content.getBirthday());
            setCity(content.getCity());
            setEmail(content.getEmail());
            setFirstName(content.getFirstName());
            setLastName(content.getLastName());
            setHeadPic(content.getHeadPic());
            setMobile(content.getMobile());
            setNickName(content.getNickName());
            setProvince(content.getProvince());
            setSex(content.getSex());
            setTown(content.getTown());
            setUserName(content.getUserName());
            setUserId(content.getUserid());
            setIsHavePwd(content.getIsHavePwd());
            setThirdInfoList(content.getThirdInfos());
            setThirdInfos(QXJsonUtils.toJson(getThirdInfoList()));

        }
        return this;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "_id=" + _id +
                ", userId='" + userId + '\'' +
                ", updateTime=" + updateTime +
                ", addTime=" + addTime +
                ", token='" + token + '\'' +
                ", expire_in=" + expire_in +
                ", type=" + type +
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
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", isHavePwd=" + isHavePwd +
                ", thirdInfos='" + thirdInfos + '\'' +
                ", loginStatus=" + loginStatus +
                '}';
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public List<GetUserInfo.Resp.ContentBean.ThirdInfosBean> getThirdInfoList() {
        if (thirdInfoList == null) {
            if (!TextUtils.isEmpty(thirdInfos)) {
                thirdInfoList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(thirdInfos);
                    if (jsonArray.length() > 0) {
                        GetUserInfo.Resp.ContentBean.ThirdInfosBean thirdInfosBean = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            thirdInfosBean = new GetUserInfo.Resp.ContentBean.ThirdInfosBean();
                            thirdInfosBean.setNickName(jsonObject.getString("nickName"));
                            thirdInfosBean.setType(jsonObject.getInt("type"));
                            thirdInfoList.add(thirdInfosBean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return thirdInfoList;
    }

    public void setThirdInfoList(List<GetUserInfo.Resp.ContentBean.ThirdInfosBean> thirdInfoList) {
        this.thirdInfoList = thirdInfoList;
    }

    public String getThirdInfos() {
        return thirdInfos;
    }

    public void setThirdInfos(String thirdInfos) {
        this.thirdInfos = thirdInfos;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(long expire_in) {
        this.expire_in = expire_in;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
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


}
