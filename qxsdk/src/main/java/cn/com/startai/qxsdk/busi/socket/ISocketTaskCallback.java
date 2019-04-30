package cn.com.startai.qxsdk.busi.socket;


import cn.com.startai.qxsdk.busi.socket.entity.UpdateVersionResp;

/**
 * Created by Robin on 2019/3/27.
 * 419109715@qq.com 彬影
 */
public interface ISocketTaskCallback {


    void onUpdateVersionResult(boolean b, UpdateVersionResp resp);

    void onSettingRecoveryResult(String id, boolean result);

    void onDeviceRenameResult(String id, boolean b, String name);
}
