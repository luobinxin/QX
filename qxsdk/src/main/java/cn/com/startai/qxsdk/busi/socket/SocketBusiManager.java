package cn.com.startai.qxsdk.busi.socket;

import android.app.Application;

import cn.com.startai.qxsdk.QXDeviceManager;
import cn.com.startai.qxsdk.busi.common.PassthroughResp;
import cn.com.startai.qxsdk.busi.socket.entity.QueryVersionReq;
import cn.com.startai.qxsdk.busi.socket.entity.RecoveryReq;
import cn.com.startai.qxsdk.busi.socket.entity.RecoveryResp;
import cn.com.startai.qxsdk.busi.socket.entity.RenameDeviceReq;
import cn.com.startai.qxsdk.busi.socket.entity.RenameDeviceResp;
import cn.com.startai.qxsdk.busi.socket.entity.UpdateVersionReq;
import cn.com.startai.qxsdk.busi.socket.entity.UpdateVersionResp;
import cn.com.startai.qxsdk.busi.socket.task.SocketProtocolTaskImpl;
import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.QXBusiManager;
import cn.com.startai.qxsdk.busi.common.PassthroughReq;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.event.IQXCallListener;
import cn.com.startai.qxsdk.global.LooperManager;
import cn.com.startai.qxsdk.global.QXInitParam;
import cn.com.startai.qxsdk.global.QXParamManager;
import cn.com.swain.support.protocolEngine.ProtocolProcessorFactory;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;

/**
 * Created by Robin on 2019/4/17.
 * 419109715@qq.com 彬影
 */
public class SocketBusiManager extends QXBusiManager implements ISocketTaskCallback {

    private AbsProtocolProcessor pm;
    private ISocketBusiListener socketBusiListener;

    public AbsProtocolProcessor getPm() {
        return pm;
    }

    public void setPm(AbsProtocolProcessor pm) {
        this.pm = pm;
    }

    public ISocketBusiListener getSocketBusiListener() {
        return socketBusiListener;
    }

    public void setSocketBusiListener(ISocketBusiListener socketBusiListener) {
        this.socketBusiListener = socketBusiListener;
        setQXBusiListener(socketBusiListener);
    }

    private SocketBusiManager() {
    }

    public static SocketBusiManager getInstance() {
        return SingleTonHoulder.singleTonInstance;
    }


    private static class SingleTonHoulder {
        private static final SocketBusiManager singleTonInstance = new SocketBusiManager();
    }

    @Override
    public void init(Application app, QXInitParam qxInitParam) {

        super.init(app, qxInitParam);

        pm = ProtocolProcessorFactory.newMultiChannelSingleTask(LooperManager.getInstance().getProtocolLooper(),
                new SocketProtocolTaskImpl(this, getApp()),
                QXParamManager.getInstance().getmProtocolVersion(), true);

    }

    @Override
    protected void callbackPassthrough(PassthroughResp resp) {
        super.callbackPassthrough(resp);
        pm.onInputProtocolData(new ReceivesData(resp.getIp(), resp.getDataArr()));
    }

    public void renameDevice(RenameDeviceReq req, IQXCallListener listener) {
        BaseData rename = SocketDataCreater.getInstance().getRenameDeviceName(req);
        passthrough(new PassthroughReq(req.getDeviceBean(), rename.getData()), listener);
    }

    public void queryVersion(QueryVersionReq req, IQXCallListener listener) {
        BaseData queryVersion = SocketDataCreater.getInstance().getQueryVersion(req.getDeviceBean().getMac());
        passthrough(new PassthroughReq(req.getDeviceBean(), queryVersion.getData()), listener);
    }

    public void updateDevice(UpdateVersionReq req, IQXCallListener listener) {
        BaseData queryVersion = SocketDataCreater.getInstance().getUpdate(req.getDeviceBean().getMac());
        passthrough(new PassthroughReq(req.getDeviceBean(), queryVersion.getData()), listener);
    }

    public void recoveryDevice(RecoveryReq req, IQXCallListener listener) {
        BaseData queryVersion = SocketDataCreater.getInstance().getRecovery(req.getDeviceBean().getMac());
        passthrough(new PassthroughReq(req.getDeviceBean(), queryVersion.getData()), listener);
    }

    // --------------------- ISocketTaskCallback start ---------------------

    @Override
    public void onUpdateVersionResult(boolean b, UpdateVersionResp resp) {
        if (b) {
            resp.setResult(1);
            DeviceBean deviceBeanByMac = QXDeviceManager.getInstance().getDeviceBeanByMac(resp.getDeviceBean().getMac());
            resp.setDeviceBean(deviceBeanByMac);
            if (socketBusiListener != null) {
                socketBusiListener.onUpdateVersionResult(resp);
            }
        }
    }

    @Override
    public void onSettingRecoveryResult(String id, boolean result) {
        if (result && socketBusiListener != null) {
            socketBusiListener.onSettingRecoveryResult(new RecoveryResp(result ? 1 : 0, QXDeviceManager.getInstance().getDeviceBeanByMac(id)));
        }
    }

    @Override
    public void onDeviceRenameResult(String id, boolean b, String name) {

        if (b) {
            DeviceBean deviceBeanByMac = QXDeviceManager.getInstance().getDeviceBeanByMac(id);
            if (socketBusiListener != null) {
                socketBusiListener.onDeviceRenameResult(new RenameDeviceResp(1, deviceBeanByMac, name));
            }
        }

    }
    // --------------------- ISocketTaskCallback end ---------------------
}
