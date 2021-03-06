package cn.com.startai.qxsdk;

import android.text.TextUtils;

import cn.com.startai.qxsdk.db.QXDBManager;
import cn.com.startai.qxsdk.db.bean.UserBean;


/**
 * Created by Robin on 2019/3/8.
 * 419109715@qq.com 彬影
 */
public class QXUserManager {

    //将构造函数私有化
    private QXUserManager() {
    }

    public static QXUserManager getInstance() {
        return SingleTonHoulder.singleTonInstance;
    }

    //静态内部类
    public static class SingleTonHoulder {
        private static final QXUserManager singleTonInstance = new QXUserManager();
    }


    private String userId;
    private UserBean currUser;

    public UserBean getUser() {
        if (QX.getInstance().isInit()) {

            if (currUser == null) {
                currUser = QXDBManager.getInstance().getUserByLoginStatus(1);
            }
            return currUser;
        } else {
            return null;
        }

    }



    public String getUserId() {
        if (QX.getInstance().isInit()) {
            if (TextUtils.isEmpty(userId)) {
                UserBean user = getUser();
                if (user != null) {
                    userId = user.getUserId();
                }
            }
            return userId;
        } else {
            return "";
        }

    }

    public void resetUser() {
        this.userId = "";
        this.currUser = null;
        QXDBManager.getInstance().resetUser();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setCurrUser(UserBean currUser) {
        this.currUser = currUser;
    }

    public void addOrUpdateUser(UserBean userBean) {
        QXDBManager.getInstance().addOrUpdateUser(userBean);
        this.currUser = userBean;
        this.userId = userBean.getUserId();
    }



}
