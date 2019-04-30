package cn.com.startai.qxsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.com.startai.qxsdk.QX;


/**
 * Created by Robin on 2018/5/11.
 * qq: 419109715 彬影
 */

public class QXShareUtils {


    private QXShareUtils() {
    }

    public static QXShareUtils getInstance() {
        return SingleTonHoulder.singleTonInstance;
    }


    private static class SingleTonHoulder {
        private static final QXShareUtils singleTonInstance = new QXShareUtils();
    }


    private String SP_NAME = "qx";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public void init(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            editor = sp.edit();
        }
    }

    public void clear() {
        editor.clear().apply();
    }

    public void remove(String key) {
        editor.remove(key).apply();
    }

    public void putInt(String key, int value) {
         
        editor.putInt(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
         
        editor.putBoolean(key, value).apply();
    }

    public void putFloat(String key, float value) {
         
        editor.putFloat(key, value).apply();
    }

    public void putLong(String key, long value) {
         
        editor.putLong(key, value).apply();
    }

    public void putString(String key, String value) {
         
        editor.putString(key, value).apply();

    }


    public String getString(String key, String defaultValue) {
         
        return sp.getString(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
         
        return sp.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
         
        return sp.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
         
        return sp.getLong(key, defaultValue);

    }

    public float getFloat(String key, float defaultValue) {
         
        return sp.getFloat(key, defaultValue);
    }


}
