package cn.com.startai.qxsdk.event;

import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.global.QXError;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public interface IQXBusiResultListener {

//    /**
//     * 连接设备结果
//     *
//     * @param resp
//     */
//    void onConnectDeviceResult(ConnectDeviceResp resp);
//
//    /**
//     * 消息透传结果
//     */
//    void onPassthroughResult(PassthroughResp resp);
//
//    /**
//     * 修改备注名结果
//     *
//     * @param resp 成功内容
//     */
//    void onUpdateRemarkResult(C_0x8015.Resp resp);
//
//    /**
//     * 删除好友回调
//     */
//    void onUnBindResult(UnBindDeviceResp resp);
//
//    /**
//     * 获取绑定关系列表回调
//     */
//    void onGetBindDeviceListResult(GetBindListResp resp);
//
//    /**
//     * 查询最新软件版本结果
//     *
//     * @param resp 最新软件版本信息
//     */
//    void onGetLatestVersionResult(C_0x8016.Resp resp);
//
//    /**
//     * 更新用户密码返回
//     *
//     * @param resp 用户密码信息
//     */
//    void onUpdateUserPwdResult(C_0x8025.Resp resp);
//
//    /**
//     * 更新用户信息结果
//     *
//     * @param resp
//     */
//    void onUpdateUserInfoResult(C_0x8020.Resp resp);

    /**
     * 更新设备信息
     *
     * @param resp
     */
    void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp);

//
//    /**
//     * 查询用户信息结果
//     *
//     * @param resp
//     */
//    void onGetUserInfoResult(C_0x8024.Resp resp);
//
//    /**
//     * 发送邮件结果返回
//     *
//     * @param resp 成功的信息
//     */
//    void onSendEmailResult(C_0x8023.Resp resp);
//
//    /**
//     * 登录过期 需要重新登录
//     */
//    void onLoginExpired();

    /**
     * 登录结果
     *
     * @param resp
     */
    void onLoginResult(Login.Resp resp);

//    /**
//     * 注册结果回调
//     */
//    void onRegisterResult(C_0x8017.Resp resp);
//
//
//    /**
//     * 检验验证码结果
//     */
//    void onCheckIdetifyResult(C_0x8022.Resp resp);
//
//    /**
//     * 获取验证码结果
//     */
//    void onGetIdentifyCodeResult(C_0x8021.Resp resp);
//

    /**
     * 与服务器连接成功
     */
    void onServerConnected();

    /**
     * 与服务器断开连接|连接失败
     *
     * @param qxError
     */
    void onServerDisConnect(QXError qxError);

    /**
     * 重连接中
     */
    void onServerReConnecting();
//
//    /**
//     * 绑定结果
//     *
//     * @param resp
//     */
//    void onBindDeviceResult(BindDeviceResp resp);
//
//
////        /**
////         * 连接设备结果
////         *
////         * @param result
////         */
////        void onControlDeviceResult(ConnectDeviceResp result);
////
//

    /**
     * 发现一台设备（局域网）
     *
     * @param deviceBean
     */
    void onDiscoveryResult(DeviceBean deviceBean);

    /**
     * 开始发现（局域网）
     */
    void onDiscoveryStart();

    /**
     * 停止发现（局域网）
     */
    void onDiscoveryStop();
//
//    /**
//     * 设备属性状态 变化  包括  ssid rssi bssid 局域网连接状态 ，广域网连接状态 ，绑定状态 等等
//     *
//     * @param deviceBean
//     */
//    void onDeviceStatusChange(DeviceBean deviceBean);
//
//    /**
//     * 设备激活回调，如果激活成功只会回调一次
//     */
//    void onActiviteResult(C_0x8001.Resp resp);
//

    /**
     * 第三方硬件激活结果
     */
    void onHardwareActivateResult(Activate.Resp resp);
//
//    /**
//     * 添加好友回调
//     *
//     * @param id        自己的id
//     * @param bebinding 被绑定者 开发者需要持久化，在向对端发送消息时需要携带此bebinding的id
//     */
//    void onBindResult(C_0x8002.Resp resp, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding);
//
//    /**
//     * 注销激活
//     */
//    void onUnActiviteResult(C_0x8003.Resp resp);
//
//
//    /**
//     * 重置登录密码结果
//     *
//     * @param resp 成功内容
//     */
//    void onResetLoginPwdResult(C_0x8026.Resp resp);
//
//    /**
//     * 第三方支付 统一下单结果
//     *
//     * @param resp
//     */
//    void onThirdPaymentUnifiedOrderResult(C_0x8028.Resp resp);
//
//    /**
//     * 查询真实订单支付结果
//     *
//     * @param resp
//     */
//    void onGetRealOrderPayStatus(C_0x8031.Resp resp);
//
//    /**
//     * 查询支付宝认证信息
//     *
//     * @param resp
//     */
//    void onGetAlipayAuthInfoResult(C_0x8033.Resp resp);
//
//    /**
//     * 绑定手机号返回
//     *
//     * @param resp
//     */
//    void onBindMobileNumResult(C_0x8034.Resp resp);
//
//    /**
//     * 查询天气信息返回
//     *
//     * @param resp
//     */
//    void onGetWeatherInfoResult(C_0x8035.Resp resp);
//
//    /**
//     * 解绑第三方账号返回
//     *
//     * @param resp
//     */
//    void onUnBindThirdAccountResult(C_0x8036.Resp resp);
//
//    /**
//     * 绑定第三方账号返回
//     *
//     * @param resp
//     */
//    void onBindThirdAccountResult(C_0x8037.Resp resp);
//
//
//    /**
//     * 获取绑定列表 分页
//     *
//     * @param resp
//     */
//    void onGetBindListByPageResult(C_0x8038.Resp resp);
//
//    /**
//     * 绑定邮箱返回
//     *
//     * @param resp
//     */
//    void onBindEmailResult(C_0x8039.Resp resp);
//
//
//    /**
//     * 登出
//     *
//     * @param result
//     * @param errorCode
//     * @param errorMsg
//     */
//    void onLogoutResult(int result, String errorCode, String errorMsg);
//
//    /**
//     * 网络状态变化
//     *
//     * @param action
//     * @param networkType
//     * @param state
//     */
//    void onNetworkChange(String action, String networkType, NetworkInfo.State state);
//

}
