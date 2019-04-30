package cn.com.startai.qxsdk.event;

import android.app.Application;
import android.support.annotation.NonNull;

import cn.com.startai.qxsdk.busi.common.BindDeviceReq;
import cn.com.startai.qxsdk.busi.common.GetBindListReq;
import cn.com.startai.qxsdk.busi.common.PassthroughReq;
import cn.com.startai.qxsdk.busi.common.UnBindDeviceReq;
import cn.com.startai.qxsdk.channel.mqtt.entity.Activate;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindMobile;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetRealPayResult;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetWeatherInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.Login;
import cn.com.startai.qxsdk.channel.mqtt.entity.LoginWithThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.Register;
import cn.com.startai.qxsdk.channel.mqtt.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.SendEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.ThirdPaymentUnifiedOrder;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateRemark;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.ServerConnectState;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.global.QXInitParam;

/**
 * 对外
 */
public interface IQXBusi {


    /**
     * 消息透传
     *
     * @param req
     * @param callListener
     */
    void passthrough(PassthroughReq req, IQXCallListener callListener);


    /**
     * 修备注名
     *
     * @param req      对方的sn 跟新的备注名
     * @param listener
     */
    void updateRemark(UpdateRemark.Req req, IQXCallListener listener);


    /**
     * 与设备进行连接设备
     *
     * @param deviceBean
     * @param callListener
     */
    void connectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener);

    /**
     * 断开与设备的连接
     *
     * @param deviceBean
     * @param callListener
     */
    void disConnectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener);


    /**
     * 局域网设备扫描
     *
     * @param timeMillims
     */
    void discovery(long timeMillims);

    /**
     * 停止局域网设备扫描
     */
    void stopDiscovery();

    /**
     * 绑定设备
     *
     * @param callListener
     */
    void bindDevice(@NonNull BindDeviceReq req, IQXCallListener callListener);


    /**
     * 解绑设备
     *
     * @param callListener
     */
    void unBindDevice(@NonNull UnBindDeviceReq req, IQXCallListener callListener);

    /**
     * 获取最新软件版本
     *
     * @param callListener
     */
    void getLatestVersion(IQXCallListener callListener);

    /**
     * 修改用户登录密码
     *
     * @param req
     * @param callListener
     */
    void updateLoginPwd(UpdateLoginPwd.Req req, IQXCallListener callListener);


    /**
     * 更新用户信息
     *
     * @param req
     * @param callListener
     */
    void updateUserInfo(UpdateUserInfo.Req req, IQXCallListener callListener);

    /**
     * 查询用户信息
     *
     * @param callListener
     */
    void getUserInfo(IQXCallListener callListener);


    /**
     * 请求发送邮件
     *
     * @param req 邮箱
     */
    void sendEmail(SendEmail.Req req, IQXCallListener callListener);


    /**
     * 第三方账号登录
     *
     * @param req          第三方登录参数
     * @param callListener
     */
    void loginWithThirdAccount(LoginWithThirdAccount.Req req, IQXCallListener callListener);


    /**
     * 登出
     */
    void logout();


    /**
     * 登录
     *
     * @param req
     * @param callListener
     */
    void login(@NonNull Login.Req req, IQXCallListener callListener);


    /**
     * 注册
     *
     * @param req
     * @param callListener
     */
    void register(@NonNull Register.Req req, IQXCallListener callListener);

    /**
     * 获取手机验证码
     *
     * @param req
     * @param callListener
     */
    void getIdentifyCode(@NonNull GetIdentifyCode.Req req, IQXCallListener callListener);

    /**
     * 检验手机验证码
     *
     * @param req
     * @param callListener
     */
    void checkIdentifyCode(@NonNull CheckIdentifyCode.Req req, IQXCallListener callListener);


    /**
     * 释放资源
     */
    void release();

    /**
     * sdk 初始化
     *
     * @param application
     * @param qxInitParam
     */
    void init(@NonNull Application application, @NonNull QXInitParam qxInitParam);

    /**
     * 设置监听
     *
     * @param listener
     */
    void setQXBusiListener(IQXBusiResultListener listener);


    /**
     * 获取 app
     *
     * @return
     */
    Application getApp();

    /**
     * 是否已经初始化
     *
     * @return
     */
    boolean isInit();

    /**
     * 获取与服务器的连接状态
     *
     * @return
     */
    ServerConnectState getServerConnectState();

    /**
     * 注销激活
     */
    void unActivite(IQXCallListener callListener);


    /**
     * 帮第三方设备激活
     */
    void hardwareActivate(Activate.Req req, IQXCallListener callListener);


    /**
     * 更新设备信息
     */
    void updateDeviceInfo(UpdateDeviceInfo.Req req, IQXCallListener callListener);


    /**
     * 重置密码
     *
     * @param req          手机号 或邮箱号
     * @param callListener
     */
    void resetLoginPwd(ResetLoginPwd.Req req, IQXCallListener callListener);


    /**
     * 第三方支付 统一下单
     */
    void thirdPaymentUnifiedOrder(ThirdPaymentUnifiedOrder.Req contentBean, IQXCallListener callListener);

    /**
     * 查询真实订单支付结果
     */
    void getRealOrderPayStatus(GetRealPayResult.Req req, IQXCallListener callListener);

    /**
     * 查询支付宝认证信息
     *
     * @param callListener
     */
    void getAlipayAuthInfo(String authType, IQXCallListener callListener);

    /**
     * 关联手机号
     *
     * @param callListener
     */
    void bindMobileNum(BindMobile.Req req, IQXCallListener callListener);


    /**
     * 解绑第三方账号
     *
     * @param req
     * @param callListener
     */
    void unBindThirdAccount(UnBindThirdAccount.Req req, IQXCallListener callListener);

    /**
     * 绑定第三方账号
     *
     * @param req
     * @param callListener
     */
    void bindThirdAccount(BindThirdAccount.Req req, IQXCallListener callListener);

    /**
     * 查询天气信息
     *
     * @param req
     * @param callListener
     */
    void getWeatherInfo(GetWeatherInfo.Req req, IQXCallListener callListener);

    /**
     * 绑定邮箱
     *
     * @param req
     * @param callListener
     */
    void bindEmail(BindEmail.Req req, IQXCallListener callListener);

    /**
     * 查询 绑定列表
     */
    void getBindList(GetBindListReq req, IQXCallListener callListener);


}
