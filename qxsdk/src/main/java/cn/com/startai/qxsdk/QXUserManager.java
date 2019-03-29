package cn.com.startai.qxsdk;

import android.text.TextUtils;

import cn.com.startai.qxsdk.db.QXDBManager;
import cn.com.startai.qxsdk.db.bean.UserBean;


/**
 * Created by Robin on 2019/3/8.
 * 419109715@qq.com 彬影
 */
public class QXUserManager {

    private static QXUserManager instance;

    public static QXUserManager getInstance() {
        if (instance == null) {
            instance = new QXUserManager();
        }
        return instance;
    }

    private QXUserManager() {
    }

    private String userId;
    private UserBean currUser;

    public UserBean getUser() {

        if (currUser == null) {
            currUser = QXDBManager.getInstance().getUserByLoginStatus(1);
        }

        return currUser;
    }

    public String getUserId() {

        if (TextUtils.isEmpty(userId)) {
            UserBean user = getUser();
            if (user != null) {
                userId = user.getUserId();
            }
        }

        return userId;
    }

    public void resetUser() {
        this.userId = "";
        this.currUser = null;
        QXDBManager.getInstance().resetUser();
    }

}
