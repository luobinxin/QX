package cn.com.startai.qxsdk.connect;

import android.app.Application;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IConnectBusi {

    void init( );

    void release();

    void doSend(BaseData data);




}
