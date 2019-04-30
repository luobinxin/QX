package cn.com.startai.qxsdk.channel.udp.client;

import android.os.Looper;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.channel.udp.IQXUDP;
import cn.com.startai.qxsdk.channel.udp.event.IQXUDPListener;
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

    public static QXUDPImpl getInstance() {
        return SingleTonHoulder.singleTonInstance;
    }


    private static class SingleTonHoulder {
        private static final QXUDPImpl singleTonInstance = new QXUDPImpl();
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
    public void send(BaseData data) {
        DatagramPacket datagramPacket = getDatagramPacket(data);
        mUdpCom.send(datagramPacket);
    }

    @Override
    public void sendDelay(BaseData req, long delay, long maxDelay) {
        DatagramPacket datagramPacket = getDatagramPacket(req);
        mUdpCom.sendDelay(datagramPacket, delay, maxDelay);
    }

    @Override
    public void broadcast(BaseData req) {
        DatagramPacket datagramPacket = getDatagramPacket(req);
        mUdpCom.broadcast(datagramPacket);
    }

    @Override
    public void broadcastDelay(BaseData req, long delay, long maxDelay) {
        DatagramPacket datagramPacket = getDatagramPacket(req);
        mUdpCom.broadcastDelay(datagramPacket);
    }

    @Override
    public DatagramPacket getDatagramPacket(BaseData data) {

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
