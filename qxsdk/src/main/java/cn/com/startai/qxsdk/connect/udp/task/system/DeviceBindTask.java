package cn.com.startai.qxsdk.connect.udp.task.system;
import cn.com.startai.qxsdk.connect.udp.SocketSecureKey;
import cn.com.startai.qxsdk.connect.udp.bean.LanBindingDevice;
import cn.com.startai.qxsdk.connect.udp.task.OnUdpTaskCallBack;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.util.Bit;
import cn.com.swain.baselib.util.MacUtil;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.task.SocketResponseTask;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/1 0001
 * desc :
 */
public class DeviceBindTask extends SocketResponseTask {

    private OnUdpTaskCallBack mOnUdpTaskCallBack;

    public DeviceBindTask(OnUdpTaskCallBack mOnUdpTaskCallBack) {
        this.mOnUdpTaskCallBack = mOnUdpTaskCallBack;
        Tlog.e(TAG, " new DeviceBindTask ");
    }

    @Override
    protected void doTask(SocketDataArray mSocketDataArray) {

        byte[] protocolParams = mSocketDataArray.getProtocolParams();

        if (protocolParams.length < 77) {
            Tlog.e(TAG, " param length error : " + protocolParams.length);
            if (mOnUdpTaskCallBack != null) {
                mOnUdpTaskCallBack.onLanBindResult(false, null);
            }
            return;
        }

        boolean result = SocketSecureKey.Util.resultIsOk(protocolParams[0]);


        LanBindingDevice mLanBindingDevice = new LanBindingDevice();

        boolean admin = protocolParams[1] == 0x01; // 管理员
        mLanBindingDevice.setIsAdmin(admin);

        String deviceUserID = new String(protocolParams, 2, 32).trim(); // userID
        mLanBindingDevice.setOid(deviceUserID);

        String userID = new String(protocolParams, 2 + 32, 32).trim(); // userID
        mLanBindingDevice.setMid(userID);

        String mac = MacUtil.byteToMacStr(protocolParams, 2 + 32 + 32);
        mLanBindingDevice.setOmac(mac);

        byte[] CPU_BUF = new byte[4];
        CPU_BUF[3] = protocolParams[2 + 32 + 32 + 6];
        CPU_BUF[2] = protocolParams[2 + 32 + 32 + 6 + 1];
        CPU_BUF[1] = protocolParams[2 + 32 + 32 + 6 + 2];
        CPU_BUF[0] = protocolParams[2 + 32 + 32 + 6 + 3];

        String cpuInfo = String.format("%x%x%x%x", CPU_BUF[0], CPU_BUF[1], CPU_BUF[2], CPU_BUF[3]);
        mLanBindingDevice.setCpuInfo(cpuInfo);

        int isBindResult = protocolParams[2 + 32 + 32 + 6 + 4];

        Tlog.v(TAG, " deviceID:" + deviceUserID
                + " userID:" + userID + " mac:" + mac
                + " cpuInfo:" + cpuInfo
                + " " + Integer.toBinaryString(isBindResult & 0xFF));
        if (mOnUdpTaskCallBack != null) {
            if (Bit.isOne(isBindResult, 0)) {
                mOnUdpTaskCallBack.onLanBindResult(result, mLanBindingDevice);
            } else {
                mOnUdpTaskCallBack.onLanUnBindResult(result, mLanBindingDevice);
            }

        }

    }


}
