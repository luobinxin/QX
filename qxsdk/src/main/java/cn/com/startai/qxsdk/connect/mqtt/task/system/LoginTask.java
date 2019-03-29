package cn.com.startai.qxsdk.connect.mqtt.task.system;

import android.os.AsyncTask;

import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.connect.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public class LoginTask extends AsyncTask {
    private OnMqttTaskCallBack callBack;
    private String miof;

    public LoginTask(String msg, OnMqttTaskCallBack callBack) {
        this.miof = msg;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        
        Login.Resp resp = QXJsonUtils.fromJson(miof, Login.Resp.class);
        QXLog.d(TAG, "resp = " + resp);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return null;
        }

        Login.Resp.ContentBean content = resp.getContent();
        if (resp.getResult() == 1) {
            QXLog.e(TAG, "login success");


        } else {
            //登录失败
            Login.Req errcontent = content.getErrcontent();
            content.setType(errcontent.getType());
            content.setUname(errcontent.getUname());

            QXLog.e(TAG, "login failed " + content.getErrmsg());
        }

        callBack.onLoginResult(resp);
        cancel(false);
        return null;
    }
}
