package cn.com.startai.qxsdk.utils;

import android.util.Log;

import cn.com.startai.qxsdk.QX;

/**
 * Created by Robin on 2019/3/21.
 * 419109715@qq.com 彬影
 */
public class QXLog {


    public static void e(String tag, String msg) {
        if (QX.isIsDebug()) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (QX.isIsDebug()) {
            Log.w(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (QX.isIsDebug()) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (QX.isIsDebug()) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (QX.isIsDebug()) {
            Log.v(tag, msg);
        }
    }
}

