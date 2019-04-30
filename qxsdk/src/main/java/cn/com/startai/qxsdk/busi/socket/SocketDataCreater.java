package cn.com.startai.qxsdk.busi.socket;

import cn.com.startai.qxsdk.busi.socket.entity.RenameDeviceReq;
import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.channel.udp.QXUdpDataCreater;
import cn.com.startai.qxsdk.channel.udp.SocketSecureKey;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;

/**
 * Created by Robin on 2019/4/17.
 * 419109715@qq.com 彬影
 */
public class SocketDataCreater extends QXUdpDataCreater {


    private SocketDataCreater() {
    }

    public static SocketDataCreater getInstance() {
        return SingleTonHoulder.singleTonInstance;
    }


    private static class SingleTonHoulder {
        private static final SocketDataCreater singleTonInstance = new SocketDataCreater();
    }

    public BaseData getRenameDeviceName(RenameDeviceReq req) {
        String newName = req.getNewName();
        byte[] nameBytes = newName.getBytes();
        String mac = req.getDeviceBean().getMac();

        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_RENAME);

        final byte[] params = new byte[32];
        int length = 0;
        if (nameBytes != null) {
            length = nameBytes.length;
        }
        System.arraycopy(EMPTY_BYTES_32, 0, params, 0, 32);

        if (length > 32) {
            length = 32;
        }
        if (length > 0) {
            System.arraycopy(nameBytes, 0, params, 0, length);
        }
        mSecureDataPack.setParams(params);
        BaseData responseData = newResponseDataRecord(mac, mSecureDataPack);
        responseData.initMqttUdpMode();
        return responseData;

    }

    public BaseData getRecovery(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_RECOVERY_SCM);

        return newResponseDataRecord(mac, mSecureDataPack);
    }


    /**
     * 查询版本号
     */
    public BaseData getQueryVersion(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_UPDATE);
        mSecureDataPack.setParams(new byte[]{SocketSecureKey.Model.MODEL_QUERY_VERSION});
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    /**
     * 升级
     */
    public BaseData getUpdate(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_UPDATE);
        mSecureDataPack.setParams(new byte[]{SocketSecureKey.Model.MODEL_UPDATE});
        return newResponseDataRecord(mac, mSecureDataPack);
    }

}
