package cn.com.startai.qx.utils;


import android.content.Context;
import android.content.SharedPreferences;

import cn.com.startai.qxsdk.global.QXInitParam;
import cn.com.startai.qxsdk.utils.QXJsonUtils;


/**
 * sp管理类
 * Created by Robin on 2018/5/11.
 * qq: 419109715 彬影
 */

public class DemoSPUtils {

    private SharedPreferences sp;
    private String SP_NAME = "demo";

    private static DemoSPUtils demoSpUtils;

    public static DemoSPUtils getInstance() {
        if (demoSpUtils == null) {
            demoSpUtils = new DemoSPUtils();
        }
        return demoSpUtils;
    }

    public void init(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private DemoSPUtils() {

    }


    private static final String LAST_INIT_PARAM = "LAST_INIT_PARAM";

    public void putLastInitParam(QXInitParam initParam) {
        if (sp != null) {

            sp.edit().putString(LAST_INIT_PARAM, QXJsonUtils.toJson(initParam)).apply();
        }
    }

    public QXInitParam getLastInitParam() {
        if (sp != null) {
            String string = sp.getString(LAST_INIT_PARAM, "");
            QXInitParam param = QXJsonUtils.fromJson(string, QXInitParam.class);
            return param;
        } else {
            return null;
        }
    }

}
