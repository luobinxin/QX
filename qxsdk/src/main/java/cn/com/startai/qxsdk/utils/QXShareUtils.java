package cn.com.startai.qxsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.com.startai.qxsdk.QX;


/**
 * Created by Robin on 2018/5/11.
 * qq: 419109715 彬影
 */

public class QXShareUtils {

    private static String SP_NAME = "qx";
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;


    private static void init() {
        if (sp == null) {
            Context context = QX.getInstance().getApp();
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            editor = sp.edit();
        }
    }

    public static void clear() {
        sp.edit().clear().apply();
    }

    public static void putInt(String key, int value) {
        init();
        editor.putInt(key, value).apply();
    }

    public static void putBoolean(String key, boolean value) {
        init();
        editor.putBoolean(key, value).apply();
    }

    public static void putFloat(String key, float value) {
        init();
        editor.putFloat(key, value).apply();
    }

    public static void putLong(String key, long value) {
        init();
        editor.putLong(key, value).apply();
    }

    public static void putString(String key, String value) {
        init();
        editor.putString(key, value).apply();
    }


    public static String getString(String key, String defaultValue) {
        init();
        return sp.getString(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        init();
        return sp.getBoolean(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        init();
        return sp.getInt(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        init();
        return sp.getLong(key, defaultValue);

    }

    public static float getFloat(String key, float defaultValue) {
        init();
        return sp.getFloat(key, defaultValue);
    }


}
