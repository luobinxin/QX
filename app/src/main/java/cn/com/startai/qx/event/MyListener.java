package cn.com.startai.qx.event;

import org.greenrobot.eventbus.EventBus;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.Bind;
import cn.com.startai.qxsdk.busi.entity.BindEmail;
import cn.com.startai.qxsdk.busi.entity.BindMobile;
import cn.com.startai.qxsdk.busi.entity.BindThirdAccount;
import cn.com.startai.qxsdk.busi.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetLatestAppVersion;
import cn.com.startai.qxsdk.busi.entity.GetUserInfo;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.Register;
import cn.com.startai.qxsdk.busi.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.busi.entity.SendEmail;
import cn.com.startai.qxsdk.busi.entity.UnActivate;
import cn.com.startai.qxsdk.busi.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.busi.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.busi.entity.UpdateRemark;
import cn.com.startai.qxsdk.busi.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.event.IQXBusiResultListener;
import cn.com.startai.qxsdk.global.QXError;

/**
 * Created by Robin on 2019/3/26.
 * 419109715@qq.com 彬影
 */
public class MyListener implements IQXBusiResultListener {
    /**
     * 修改备注名结果
     *
     * @param resp 成功内容
     */
    @Override
    public void onUpdateRemarkResult(UpdateRemark.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UPDATE_REMARK_RESULT, resp));
    }

    /**
     * 查询最新软件版本结果
     *
     * @param resp 最新软件版本信息
     */
    @Override
    public void onGetLatestVersionResult(GetLatestAppVersion.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_GET_LATEST_VERSION_RESULT, resp));
    }

    /**
     * 更新用户密码返回
     *
     * @param resp 用户密码信息
     */
    @Override
    public void onUpdateLoginPwdResult(UpdateLoginPwd.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UPDATE_USER_PWD_RESULT, resp));
    }

    /**
     * 更新用户信息结果
     *
     * @param resp
     */
    @Override
    public void onUpdateUserInfoResult(UpdateUserInfo.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UPDATE_USERINFO_RESULT, resp));
    }

    /**
     * 更新设备信息
     *
     * @param resp
     */
    @Override
    public void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UPDATE_DEVICE_INFO_RESULT, resp));
    }

    /**
     * 查询用户信息结果
     *
     * @param resp
     */
    @Override
    public void onGetUserInfoResult(GetUserInfo.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_GET_USERINFO_RESULT, resp));
    }

    /**
     * 发送邮件结果返回
     *
     * @param resp 成功的信息
     */
    @Override
    public void onSendEmailResult(SendEmail.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_SEND_EMAIL_RESULT, resp));
    }

    /**
     * 登录过期 需要重新登录
     */
    @Override
    public void onLoginExpired() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_LOGIN_EXPIRED));
    }

    /**
     * 登录结果
     *
     * @param resp
     */
    @Override
    public void onLoginResult(Login.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_LOGIN_RESULT, resp));
    }

    /**
     * 注册结果回调
     *
     * @param resp
     */
    @Override
    public void onRegisterResult(Register.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_REGISTER_RESULT, resp));
    }

    /**
     * 检验验证码结果
     *
     * @param resp
     */
    @Override
    public void onCheckIdetifyResult(CheckIdentifyCode.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_CHECK_IDENTIFY_CODE_RESULT, resp));
    }

    /**
     * 获取验证码结果
     *
     * @param resp
     */
    @Override
    public void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_GET_IDENTIFY_CODE_RESULT, resp));
    }

    /**
     * 与服务器连接成功
     */
    @Override
    public void onServerConnected() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_SERVER_CONNECTED));

//        final String topic1 = "Q/client/868893046962824/#";
//        final String topic2 = "SERVICE/NMC/qx55e3cf4bf12fd3f7/#";
//        List<String> strs = new ArrayList();
//        strs.add(topic1);
//        strs.add(topic2);
//        QXMqttImpl.getInstance().subscribeSync(strs, new IQXCallListener() {
//            @Override
//            public void onSuccess() {
//                TAndL.L("sub success TTTTT");
//                QXMqttData mqttData = new QXMqttData("abce".getBytes(), "Q/client/868893046962824");
//                QXMqttImpl.getInstance().publish(mqttData, null);
//
//            }
//
//            @Override
//            public void onFailed(QXError error) {
//                TAndL.L("sub Feiled TTTTT");
//
//
//            }
//        });

    }

    /**
     * 与服务器断开连接|连接失败
     *
     * @param qxError
     */
    @Override
    public void onServerDisConnect(QXError qxError) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_SERVER_DISCONNECTED, qxError));
    }

    /**
     * 重连接中
     */
    @Override
    public void onServerReConnecting() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_SERVER_RECONNECTING));
    }

    /**
     * 发现一台设备（局域网）
     *
     * @param deviceBean
     */
    @Override
    public void onDiscoveryResult(DeviceBean deviceBean) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_DISCOVERY_RESULT, deviceBean));
    }

    /**
     * 开始发现（局域网）
     */
    @Override
    public void onDiscoveryStart() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_DISCOVERY_START));
    }

    /**
     * 停止发现（局域网）
     */
    @Override
    public void onDiscoveryStop() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_DISCOVERY_STOP));
    }

    /**
     * 设备激活回调，如果激活成功只会回调一次
     *
     * @param resp
     */
    @Override
    public void onActiviteResult(Activate.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_ACTIVATE_RESULT, resp));
    }

    /**
     * 第三方硬件激活结果
     *
     * @param resp
     */
    @Override
    public void onHardwareActivateResult(Activate.Resp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_HARDWARE_ACTIVATE_RESULT, resp));
    }

    /**
     * 添加好友回调
     *
     * @param resp
     * @param id        自己的id
     * @param bebinding 被绑定者 开发者需要持久化，在向对端发送消息时需要携带此bebinding的id
     */
    @Override
    public void onBindResult(Bind.Resp resp, String id, Bind.Resp.ContentBean.BebindingBean bebinding) {

    }

    /**
     * 注销激活
     *
     * @param resp
     */
    @Override
    public void onUnActiviteResult(UnActivate.Resp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UNACTIVATE_RESULT, resp));
    }

    /**
     * 重置登录密码结果
     *
     * @param resp 成功内容
     */
    @Override
    public void onResetLoginPwdResult(ResetLoginPwd.Resp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_RESET_LOGIN_PWE_RESULT, resp));
    }

    /**
     * 绑定手机号返回
     *
     * @param resp
     */
    @Override
    public void onBindMobileNumResult(BindMobile.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_BIND_MOBILE_RESULT, resp));
    }

    /**
     * 解绑第三方账号返回
     *
     * @param resp
     */
    @Override
    public void onUnBindThirdAccountResult(UnBindThirdAccount.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UNBIND_THIRDACCOUNT_RESULT, resp));
    }

    /**
     * 绑定第三方账号返回
     *
     * @param resp
     */
    @Override
    public void onBindThirdAccountResult(BindThirdAccount.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_BIND_THIRDACCOUNT_RESULT, resp));
    }

    /**
     * 绑定邮箱返回
     *
     * @param resp
     */
    @Override
    public void onBindEmailResult(BindEmail.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_BIND_EMAIL_RESULT, resp));
    }

    /**
     * 登出
     *
     * @param result
     */
    @Override
    public void onLogoutResult(int result) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_LOGOUT_RESULT, result));
    }
}
