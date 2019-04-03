package cn.com.startai.qxsdk.connect.ble;

import cn.com.startai.qxsdk.connect.udp.client.QXUDPImpl;
import cn.com.startai.qxsdk.event.IQXCallListener;

/**
 * Created by Robin on 2019/3/21.
 * 419109715@qq.com 彬影
 */
public class QXBleImpl implements IQXBLE {
    private static volatile QXBleImpl instance;

    private QXBleImpl() {
    }

    public static IQXBLE getInstance() {
        if (instance == null) {
            synchronized (QXUDPImpl.class) {
                if (instance == null) {
                    instance = new QXBleImpl();

                    instance.init();
                }
            }
        }
        return instance;
    }


    @Override
    public void init() {

    }

    @Override
    public void release() {

    }



    @Override
    public void send(BLEData data, IQXCallListener listener) {

    }
}
