package cn.com.startai.qx.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Robin on 2019/3/11.
 * 419109715@qq.com 彬影
 */
public class HashkeyUtil {

    public static String getHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "cn.com.startai.qx",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String msg = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", msg);
                return msg;
            }

        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        return null;
    }


}
