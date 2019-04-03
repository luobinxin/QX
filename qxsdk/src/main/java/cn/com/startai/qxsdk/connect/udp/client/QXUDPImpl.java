package cn.com.startai.qxsdk.connect.udp.client;

import android.os.Looper;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import cn.com.startai.qxsdk.connect.udp.IQXUDP;
import cn.com.startai.qxsdk.connect.udp.IQXUDPListener;
import cn.com.startai.qxsdk.connect.udp.UDPData;
import cn.com.startai.qxsdk.event.IQXCallListener;
import cn.com.startai.qxsdk.global.LooperManager;
import cn.com.swain.support.udp.AbsFastUdp;
import cn.com.swain.support.udp.FastUdpFactory;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QXUDPImpl implements IQXUDP {
    private AbsFastUdp mUdpCom;

    private IQXUDPListener udpListener;

    private QXUDPImpl() {
    }

    private static volatile QXUDPImpl instance;

    public static IQXUDP getInstance() {
        if (instance == null) {
            synchronized (QXUDPImpl.class) {
                if (instance == null) {
                    instance = new QXUDPImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {

        Looper workLooper = LooperManager.getInstance().getWorkLooper();
        mUdpCom = FastUdpFactory.newFastUniUdp(workLooper);
        mUdpCom.regUDPSocketResult(udpListener);
        mUdpCom.init();
    }

    @Override
    public void release() {
        if (mUdpCom != null && udpListener != null) {
            mUdpCom.unregUDPSocketResult(udpListener);
            mUdpCom.release();
        }
    }


    @Override
    public void send(UDPData data, IQXCallListener listener) {
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
        return new DatagramPacket(data.getData(), data.getData().length, address, data.getPort());
    }

    @Override
    public void setListener(IQXUDPListener listener) {
        this.udpListener = listener;
    }

    @Override
    public IQXUDPListener getListener() {
        return udpListener;
    }


}
