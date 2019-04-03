package cn.com.startai.qxsdk.utils.area;


import android.icu.text.UnicodeSet;

import cn.com.startai.qxsdk.global.QXSpController;

/**
 * Created by Robin on 2019/4/1.
 * 419109715@qq.com 彬影
 */
public class QXLocationManager {
    private AreaLocation location;


    private static QXLocationManager instance;

    public static QXLocationManager getInstance() {
        if (instance == null) {
            instance = new QXLocationManager();
        }
        return instance;
    }

    private QXLocationManager() {
    }

    public void setLocation(AreaLocation location) {
        this.location = location;
    }

    public synchronized AreaLocation getLocation() {
        if (location == null) {
            location = AreaConfig.getArea();
            if (location == null) {
                location = QXSpController.getLocation();
            }
        }
        return location;
    }


}
