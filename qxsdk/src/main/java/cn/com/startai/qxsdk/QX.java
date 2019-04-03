package cn.com.startai.qxsdk;

import android.app.Application;
import android.support.annotation.NonNull;

import cn.com.startai.qxsdk.busi.BusiEntity.BindDeviceReq;
import cn.com.startai.qxsdk.busi.BusiEntity.PassthroughReq;
import cn.com.startai.qxsdk.busi.BusiEntity.UnBindDeviceReq;
import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.BindEmail;
import cn.com.startai.qxsdk.busi.entity.BindMobile;
import cn.com.startai.qxsdk.busi.entity.BindThirdAccount;
import cn.com.startai.qxsdk.busi.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetBindList;
import cn.com.startai.qxsdk.busi.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetWeatherInfo;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.LoginWithThirdAccount;
import cn.com.startai.qxsdk.busi.entity.Register;
import cn.com.startai.qxsdk.busi.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.busi.entity.SendEmail;
import cn.com.startai.qxsdk.busi.entity.ThirdPaymentUnifiedOrder;
import cn.com.startai.qxsdk.busi.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.busi.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.busi.entity.UpdateRemark;
import cn.com.startai.qxsdk.busi.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.connect.BaseData;
import cn.com.startai.qxsdk.connect.mqtt.QXMqttConnectState;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.event.IQXBusi;
import cn.com.startai.qxsdk.event.IQXCallListener;
import cn.com.startai.qxsdk.event.IQXBusiResultListener;
import cn.com.startai.qxsdk.global.QXInitParam;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QX implements IQXBusi {

    private static QXBusiManager qxBusiManager;
    private static QXUserManager qxUserManager;

    public QXUserManager getQxUserManager() {
        return qxUserManager;
    }

    public void setQxUserManager(QXUserManager qxUserManager) {
        QX.qxUserManager = qxUserManager;
    }

    public static final String TAG = "QX";

    private QX() {
        qxBusiManager = QXBusiManager.getInstance();
        qxUserManager = QXUserManager.getInstance();
    }

    private static QX instance;

    public static QX getInstance() {
        if (instance == null) {
            instance = new QX();
        }
        return instance;
    }

    private boolean isDebug;

    public boolean isInit() {
        return qxBusiManager.isInit();
    }

    public Application getApp() {
        return qxBusiManager.getApp();
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean isDebug() {
        return isDebug;
    }

    @Override
    public void init(@NonNull Application app, QXInitParam qxInitParam) {

        qxBusiManager.init(app, qxInitParam);
    }

    /**
     * 添加监听
     *
     * @param listener
     */
    @Override
    public void addQXBusiListener(IQXBusiResultListener listener) {
        qxBusiManager.addQXBusiListener(listener);
    }

    /**
     * 注销监听
     *
     * @param listener
     */
    @Override
    public void removeQXBusiListener(IQXBusiResultListener listener) {
        qxBusiManager.removeQXBusiListener(listener);
    }

    public void release() {
        qxBusiManager.release();
    }


    public void doSend(BaseData baseData, IQXCallListener listener) {
        qxBusiManager.doSend(baseData, listener);
    }


    public QXBusiManager getQxBusiManager() {
        return qxBusiManager.getInstance();
    }


    public QXMqttConnectState getServerConnectState() {
        if (qxBusiManager.getQxMqtt() != null) {
            return qxBusiManager.getQxMqtt().getQXMqttConnectState();
        }
        return QXMqttConnectState.DISCONNECTED;
    }

    /**
     * 注销激活
     *
     * @param callListener
     */
    @Override
    public void unActivite(IQXCallListener callListener) {
        qxBusiManager.unActivite(callListener);
    }

    /**
     * 消息透传
     *
     * @param req
     * @param callListener
     */
    @Override
    public void passthrough(PassthroughReq req, IQXCallListener callListener) {
        qxBusiManager.passthrough(req, callListener);
    }

    /**
     * 修备注名
     *
     * @param req      对方的sn 跟新的备注名
     * @param listener
     */
    @Override
    public void updateRemark(UpdateRemark.Req req, IQXCallListener listener) {
        qxBusiManager.updateRemark(req, listener);
    }

    /**
     * 与设备进行连接设备
     *
     * @param deviceBean
     * @param callListener
     */
    @Override
    public void connectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener) {
        qxBusiManager.connectDevice(deviceBean, callListener);
    }

    /**
     * 断开与设备的连接
     *
     * @param deviceBean
     * @param callListener
     */
    @Override
    public void disConnectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener) {
        qxBusiManager.disConnectDevice(deviceBean, callListener);
    }

    /**
     * 局域网设备扫描
     *
     * @param timeMillims
     */
    @Override
    public void discovery(long timeMillims) {
        qxBusiManager.discovery(timeMillims);
    }

    /**
     * 停止局域网设备扫描
     */
    @Override
    public void stopDiscovery() {
        qxBusiManager.stopDiscovery();
    }

    /**
     * 绑定设备
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindDevice(@NonNull BindDeviceReq req, IQXCallListener callListener) {
        qxBusiManager.bindDevice(req, callListener);
    }

    /**
     * 解绑设备
     *
     * @param req
     * @param callListener
     */
    @Override
    public void unBindDevice(@NonNull UnBindDeviceReq req, IQXCallListener callListener) {
        qxBusiManager.unBindDevice(req, callListener);
    }

    /**
     * 查询绑定关系
     *
     * @param callListener
     * @return
     */
    @Override
    public void getBindDeviceList(IQXCallListener callListener) {
        qxBusiManager.getBindDeviceList(callListener);
    }

    /**
     * 获取最新软件版本
     *
     * @param callListener
     */
    @Override
    public void getLatestVersion(IQXCallListener callListener) {
        qxBusiManager.getLatestVersion(callListener);
    }

    /**
     * 修改用户登录密码
     *
     * @param req
     * @param callListener
     */
    @Override
    public void updateLoginPwd(UpdateLoginPwd.Req req, IQXCallListener callListener) {
        qxBusiManager.updateLoginPwd(req, callListener);
    }

    /**
     * 更新用户信息
     *
     * @param req
     * @param callListener
     */
    @Override
    public void updateUserInfo(UpdateUserInfo.Req req, IQXCallListener callListener) {
        qxBusiManager.updateUserInfo(req, callListener);
    }

    /**
     * 查询用户信息
     *
     * @param callListener
     */
    @Override
    public void getUserInfo(IQXCallListener callListener) {
        qxBusiManager.getUserInfo(callListener);

    }

    /**
     * 请求发送邮件
     *
     * @param req          邮箱
     * @param callListener
     */
    @Override
    public void sendEmail(SendEmail.Req req, IQXCallListener callListener) {
        qxBusiManager.sendEmail(req, callListener);
    }

    /**
     * 第三方账号登录
     *
     * @param req          第三方登录参数
     * @param callListener
     */
    @Override
    public void loginWithThirdAccount(LoginWithThirdAccount.Req req, IQXCallListener callListener) {
        qxBusiManager.loginWithThirdAccount(req, callListener);
    }

    /**
     * 登出
     */
    @Override
    public void logout() {
        qxBusiManager.logout();
    }

    /**
     * 登录
     *
     * @param req
     * @param callListener
     */
    @Override
    public void login(@NonNull Login.Req req, IQXCallListener callListener) {
        qxBusiManager.login(req, callListener);
    }

    /**
     * 注册
     *
     * @param req
     * @param callListener
     */
    @Override
    public void register(@NonNull Register.Req req, IQXCallListener callListener) {
        qxBusiManager.register(req, callListener);
    }

    /**
     * 获取手机验证码
     *
     * @param req
     * @param callListener
     */
    @Override
    public void getIdentifyCode(@NonNull GetIdentifyCode.Req req, IQXCallListener callListener) {
        qxBusiManager.getIdentifyCode(req, callListener);
    }

    /**
     * 检验手机验证码
     *
     * @param req
     * @param callListener
     */
    @Override
    public void checkIdentifyCode(@NonNull CheckIdentifyCode.Req req, IQXCallListener callListener) {
        qxBusiManager.checkIdentifyCode(req, callListener);
    }

    /**
     * 帮第三方设备激活
     *
     * @param req
     * @param callListener
     */
    @Override
    public void hardwareActivate(Activate.Req req, IQXCallListener callListener) {
        qxBusiManager.hardwareActivate(req, callListener);
    }

    /**
     * 更新设备信息
     *
     * @param req
     * @param callListener
     */
    @Override
    public void updateDeviceInfo(UpdateDeviceInfo.Req req, IQXCallListener callListener) {
        qxBusiManager.updateDeviceInfo(req, callListener);
    }

    /**
     * 重置密码
     *
     * @param req          手机号 或邮箱号
     * @param callListener
     */
    @Override
    public void resetLoginPwd(ResetLoginPwd.Req req, IQXCallListener callListener) {
        qxBusiManager.resetLoginPwd(req, callListener);
    }

    /**
     * 第三方支付 统一下单
     *
     * @param req
     * @param callListener
     */
    @Override
    public void thirdPaymentUnifiedOrder(ThirdPaymentUnifiedOrder.Req req, IQXCallListener callListener) {
        qxBusiManager.thirdPaymentUnifiedOrder(req, callListener);
    }

    /**
     * 查询真实订单支付结果
     *
     * @param orderNum
     * @param callListener
     */
    @Override
    public void getRealOrderPayStatus(String orderNum, IQXCallListener callListener) {
        qxBusiManager.getRealOrderPayStatus(orderNum, callListener);
    }

    /**
     * 查询支付宝认证信息
     *
     * @param authType
     * @param callListener
     */
    @Override
    public void getAlipayAuthInfo(String authType, IQXCallListener callListener) {
        qxBusiManager.getAlipayAuthInfo(authType, callListener);
    }

    /**
     * 关联手机号
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindMobileNum(BindMobile.Req req, IQXCallListener callListener) {
        qxBusiManager.bindMobileNum(req, callListener);
    }

    /**
     * 解绑第三方账号
     *
     * @param req
     * @param callListener
     */
    @Override
    public void unBindThirdAccount(UnBindThirdAccount.Req req, IQXCallListener callListener) {
        qxBusiManager.unBindThirdAccount(req, callListener);
    }

    /**
     * 绑定第三方账号
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindThirdAccount(BindThirdAccount.Req req, IQXCallListener callListener) {
        qxBusiManager.bindThirdAccount(req, callListener);
    }

    /**
     * 查询天气信息
     *
     * @param req
     * @param callListener
     */
    @Override
    public void getWeatherInfo(GetWeatherInfo.Req req, IQXCallListener callListener) {
        qxBusiManager.getWeatherInfo(req, callListener);
    }

    /**
     * 绑定邮箱
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindEmail(BindEmail.Req req, IQXCallListener callListener) {
        qxBusiManager.bindEmail(req, callListener);
    }

    /**
     * 查询 绑定列表 分页
     *
     * @param req
     * @param callListener
     */
    @Override
    public void getBindListByPage(GetBindList.Req req, IQXCallListener callListener) {
        qxBusiManager.getBindListByPage(req, callListener);
    }
}
