package cn.com.startai.qxsdk.busi.socket;

import cn.com.startai.qxsdk.busi.socket.entity.RecoveryResp;
import cn.com.startai.qxsdk.busi.socket.entity.RenameDeviceResp;
import cn.com.startai.qxsdk.busi.socket.entity.UpdateVersionResp;
import cn.com.startai.qxsdk.event.IQXBusiResultListener;

/**
 * Created by Robin on 2019/4/18.
 * 419109715@qq.com 彬影
 */
public interface ISocketBusiListener extends IQXBusiResultListener {

    /**
     * 固件更新
     *
     * @param resp
     */
    void onUpdateVersionResult(UpdateVersionResp resp);

    /**
     * 恢复出厂设置
     *
     * @param resp
     */
    void onSettingRecoveryResult(RecoveryResp resp);

    /**
     * 重命名
     *
     * @param resp
     */
    void onDeviceRenameResult(RenameDeviceResp resp);

}
