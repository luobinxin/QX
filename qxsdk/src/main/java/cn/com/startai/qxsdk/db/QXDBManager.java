package cn.com.startai.qxsdk.db;


import android.util.Log;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.db.bean.TopicBean;
import cn.com.startai.qxsdk.db.bean.UserBean;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;


/**
 * Created by Robin on 2017/8/17.
 * 419109715@qq.com 彬影
 */
public class QXDBManager {


    private static QXDBManager instance;
    private DbManager.DaoConfig daoConfig;
    private static DbManager db;

    private final String DB_NAME = "sdk.db";
    private static final int VERSION = 2;


    private QXDBManager() {
        daoConfig = new DbManager.DaoConfig()
                .setDbName(DB_NAME)
                .setDbVersion(VERSION)
                .setAllowTransaction(true)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();

                        QXLog.d(TAG, "数据库已经打开 name = " + DB_NAME + " verison = " + VERSION);
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //数据库升级操作
                        QXLog.d(TAG, "数据库升级" + oldVersion + " ==> " + newVersion);


                    }
                });
        db = x.getDb(daoConfig);

    }

    public void release() {
        if (db != null) {
            try {
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized DbManager getDB() {
        return db;
    }

    public static QXDBManager getInstance() {
        if (instance == null) {
            instance = new QXDBManager();
        }
        return instance;
    }

//--------------------------- DeviceBean -------------------------------

    /**
     * 添加 或 更新
     *
     * @param deviceBean
     */
    public void addOrUpdateDeviceBean(DeviceBean deviceBean) {
//        QXLog.d(TAG, "addOrUpdateDeviceBean deviceBean = " + deviceBean);
        try {
            DeviceBean first = db.selector(DeviceBean.class)
                    .where(DeviceBean.F_USERID, "=", deviceBean.getUserId())
                    .and(WhereBuilder.b(DeviceBean.F_SN, "=", deviceBean.getSn()).or(DeviceBean.F_MAC, "=", deviceBean.getMac()))
                    .findFirst();

            deviceBean.setUpdateTime(System.currentTimeMillis());

            if (first == null) {
                deviceBean.setAddTime(System.currentTimeMillis());
                db.saveBindingId(deviceBean);

            } else {
                deviceBean.set_id(first.get_id());
                db.update(deviceBean);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除
     *
     * @param sn
     */
    public void deleteDeviceBeanByUserIdAndSn(String userid, String sn) {
        try {
            db.delete(DeviceBean.class, WhereBuilder.b(DeviceBean.F_SN, "=", sn).and(DeviceBean.F_USERID, "=", userid));

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除
     *
     * @param mac
     */
    public void deleteDeviceBeanByUserIdAndMac(String userid, String mac) {
        try {
            db.delete(DeviceBean.class, WhereBuilder.b(DeviceBean.F_MAC, "=", mac).and(DeviceBean.F_USERID, "=", userid));

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除所有
     */
    public void deleteAllDeviceBean(String userId) {
        try {
            db.delete(DeviceBean.class, WhereBuilder.b(DeviceBean.F_USERID, "=", userId));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public List<DeviceBean> getAllDeviceBeanByUserid(String userId) {

        List<DeviceBean> all = null;
        try {
            all = db.selector(DeviceBean.class).where(DeviceBean.F_USERID, "=", userId).findAll();
        } catch (DbException e) {
            e.printStackTrace();

        }
        if (all == null) {
            all = new ArrayList<>();
        }
        return all;


    }

    public DeviceBean getDeviceBeanByUseridAndIp(String userId, String ip) {

        DeviceBean fir = null;
        try {
            fir = db.selector(DeviceBean.class).where(DeviceBean.F_USERID, "=", userId)
                    .and(DeviceBean.F_IP, "=", ip)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();

        }
        return fir;


    }

    public DeviceBean getDeviceBeanByUseridAndSn(String userId, String sn) {
        long t = System.currentTimeMillis();
        DeviceBean fir = null;
        try {
            fir = db.selector(DeviceBean.class).where(DeviceBean.F_USERID, "=", userId)
                    .and(DeviceBean.F_SN, "=", sn).findFirst();
        } catch (DbException e) {
            e.printStackTrace();

        }
        Log.d(TAG, "getDevice by sn use time = " + (System.currentTimeMillis() - t));
        return fir;


    }

    public DeviceBean getDeviceBeanByUseridAndMac(String userId, String mac) {

        long t = System.currentTimeMillis();
        DeviceBean fir = null;
        try {
            fir = db.selector(DeviceBean.class).where(DeviceBean.F_USERID, "=", userId)
                    .and(DeviceBean.F_MAC, "=", mac).findFirst();
        } catch (DbException e) {
            e.printStackTrace();

        }
        Log.d(TAG, "getDevice by mac use time = " + (System.currentTimeMillis() - t));
        return fir;


    }

    public List<DeviceBean> getAllDeviceBeanByUserIdAndLanBind(String userId) {
        List<DeviceBean> all = null;
        try {
            all = db.selector(DeviceBean.class)
                    .where(DeviceBean.F_USERID, "=", userId)
                    .and(DeviceBean.F_ISLANBIND, "=", true)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();

        }
        if (all == null) {
            all = new ArrayList<>();
        }
        return all;

    }

    public List<DeviceBean> getAllDeviceBeanByUserIdAndWanBind(String userId) {
        List<DeviceBean> all = null;
        try {
            all = db.selector(DeviceBean.class)
                    .where(DeviceBean.F_USERID, "=", userId)
                    .and(DeviceBean.F_ISWANBIND, "=", true)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();

        }
        if (all == null) {
            all = new ArrayList<>();
        }
        return all;
    }


    public void resetAllDeviceBeanLanConnectStatus(String userId) {

        try {
            db.update(DeviceBean.class, WhereBuilder.b(DeviceBean.F_USERID, "=", userId), new KeyValue(DeviceBean.F_LANSTATE, false));

        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public void deleteUnUseDeviceBean(String userId) {
        try {
            db.delete(DeviceBean.class, WhereBuilder.b(DeviceBean.F_ISWANBIND, "=", false).and(DeviceBean.F_ISLANBIND, "=", false));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
//--------------------------- UserBean -------------------------------

    /**
     * 查询所有
     */
    public ArrayList<UserBean> getAllUser() {
        ArrayList<UserBean> all = null;
        try {
            all = (ArrayList<UserBean>) db.findAll(UserBean.class);

        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
        if (all == null) {
            all = new ArrayList<>();
        }
        return all;
    }

    /**
     * 删除所有
     */
    public void deleteAllUser() {

        try {
            db.delete(UserBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除单个
     */
    public void deleteUserByUserid(String userid) {

        try {
            db.delete(UserBean.class, WhereBuilder.b(UserBean.F_USERID, "=", userid));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除单个
     */
    public void deleteUserByUName(String uName) {

        try {
            db.delete(UserBean.class, WhereBuilder.b(UserBean.F_USERNAME, "=", uName));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除单个
     */
    public void deleteUserByLoginStatus(int loginStatus) {

        try {
            db.delete(UserBean.class, WhereBuilder.b(UserBean.F_LOGINSTATUS, "=", loginStatus));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    /**
     * 添加或更新
     *
     * @param userBean
     */
    public void addOrUpdateUser(UserBean userBean) {
        long t = System.currentTimeMillis();
        try {
            UserBean first = db.selector(UserBean.class).where(WhereBuilder.b(UserBean.F_USERID, "=", userBean.getUserId())).findFirst();
            userBean.setUpdateTime(System.currentTimeMillis());
            if (first == null) {
                db.save(userBean);
            } else {
                userBean.set_id(first.get_id());
                db.update(userBean);
            }
            QXLog.d(TAG, "addOrUpdate use time = " + (System.currentTimeMillis() - t));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置
     */
    public void resetUser() {
        try {
            db.update(UserBean.class, null, new KeyValue(UserBean.F_LOGINSTATUS, 0));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public UserBean getUserByUserid(String userid) {

        UserBean first = null;
        try {
            first = db.selector(UserBean.class).where(UserBean.F_USERID, "=", userid).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return first;

    }

    public UserBean getUserByUname(String uName) {
        UserBean first = null;
        try {
            first = db.selector(UserBean.class).where(UserBean.F_USERNAME, "=", uName).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return first;
    }

    public UserBean getUserByLoginStatus(int loginStatus) {
        UserBean first = null;
        try {
            first = db.selector(UserBean.class).where(UserBean.F_LOGINSTATUS, "=", loginStatus).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        QXLog.d(TAG, "db currUser = " + first);
        return first;
    }


    //--------------------------- TopicBean -------------------------------

    /**
     * 查询所有
     *
     * @param id userid/sn
     * @return
     */
    public ArrayList<TopicBean> getAllTopic(String id) {

        try {
            ArrayList<TopicBean> all = (ArrayList<TopicBean>) db.selector(TopicBean.class).where(WhereBuilder.b(TopicBean.F_ID, "=", id)).findAll();
            return all;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 删除所有
     *
     * @param id userid/sn
     */
    public void deleteAllTopic(String id) {

        try {
            db.delete(TopicBean.class, WhereBuilder.b(TopicBean.F_ID, "=", id));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除所有
     */
    public void deleteAllTopic() {

        try {
            db.delete(TopicBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加或更新
     *
     * @param topicBean
     */
    public void addOrUpdateTopic(TopicBean topicBean) {
        long t = System.currentTimeMillis();
        try {
            TopicBean first = db.selector(TopicBean.class).where(WhereBuilder.b(TopicBean.F_ID, "=", topicBean.getId()).and(TopicBean.F_TOPIC, "=", topicBean.getTopic())).findFirst();
            topicBean.setTime(System.currentTimeMillis());
            if (first == null) {
                db.save(topicBean);
            } else {
                topicBean.set_id(first.get_id());
                db.update(topicBean);
            }
            QXLog.d(TAG, "addOrUpdate use time = " + (System.currentTimeMillis() - t));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置
     */
    public void resetTopic(String id) {
        try {
            db.update(TopicBean.class, WhereBuilder.b(TopicBean.F_ID, "=", id), new KeyValue(TopicBean.F_TYPE, "remove"));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除
     *
     * @param id
     * @param topic
     */
    public void deleteTopicByTopic(String id, String topic) {
        try {
            db.delete(TopicBean.class, WhereBuilder.b(TopicBean.F_ID, "=", id).and(TopicBean.F_TOPIC, "=", topic));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}