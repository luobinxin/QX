package cn.com.startai.qxsdk.connect.udp;

import android.os.Looper;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.connect.BaseData;
import cn.com.startai.qxsdk.connect.IConnectListener;
import cn.com.startai.qxsdk.global.LooperManager;
import cn.com.swain.support.udp.AbsFastUdp;
import cn.com.swain.support.udp.FastUdpFactory;
import cn.com.swain.support.udp.impl.IUDPResult;

import static cn.com.startai.qxsdk.connect.mqtt.IQXMqtt.TAG;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QXUDPImpl implements IQXUDP {
    private AbsFastUdp mUdpCom;
    private IConnectListener listener;

    private QXUDPImpl() {
    }

    private static volatile QXUDPImpl instance;

    public static void registerInstance() {
        if (instance == null) {
            synchronized (QXUDPImpl.class) {
                if (instance == null) {
                    instance = new QXUDPImpl();

                    instance.init();
                }
            }
        }
        QX.setQxUdp(instance);
    }

    @Override
    public void init() {

        Looper workLooper = LooperManager.getInstance().getWorkLooper();
        mUdpCom = FastUdpFactory.newFastUniUdp(workLooper);
        mUdpCom.regUDPSocketResult(mResult);
        mUdpCom.init();
    }

    @Override
    public void release() {
        if (mUdpCom != null && mResult != null) {
            mUdpCom.unregUDPSocketResult(mResult);
            mUdpCom.release();
        }
    }

    @Override
    public void doSend(BaseData req) {
        if (req instanceof UDPData) {
            UDPData udpData = (UDPData) req;
            send(udpData);
        }
    }


    @Override
    public void send(UDPData data) {
        DatagramPacket datagramPacket = getDatagramPacket(data);
        mUdpCom.send(datagramPacket);
    }

    @Override
    public void sendDelay(UDPData req, long delay, long maxDelay) {
        DatagramPacket datagramPacket = getDatagramPacket(req);
        mUdpCom.sendDelay(datagramPacket, delay, maxDelay);
    }

    @Override
    public void broadcast(UDPData req) {
        DatagramPacket datagramPacket = getDatagramPacket(req);
        mUdpCom.broadcast(datagramPacket);
    }

    @Override
    public void broadcastDelay(UDPData req, long delay, long maxDelay) {
        DatagramPacket datagramPacket = getDatagramPacket(req);
        mUdpCom.broadcastDelay(datagramPacket);
    }

    @Override
    public DatagramPacket getDatagramPacket(UDPData data) {

        InetAddress address = null;
        try {
            address = InetAddress.getByName(data.getIp());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        return new DatagramPacket(data.getDataArr(), data.getDataArr().length, address, data.getPort());
    }

    private IUDPResult mResult = new IUDPResult() {
        @Override
        public void onUDPInitResult(boolean result, String ip, int port) {
            Log.d(TAG, "onUDPInitResult = " + result + " ip = " + ip + " port = " + port);

        }

        @Override
        public void onUDPReceiveData(String ip, int port, byte[] data) {
            Log.d(TAG, "onUDPReceiveData ip = " + ip + " port = " + port + " data = " + Arrays.toString(data));

        }

        @Override
        public void onUDPReleaseResult(boolean result) {
            Log.d(TAG, "onUDPReleaseResult = " + result);
        }
    };
}
