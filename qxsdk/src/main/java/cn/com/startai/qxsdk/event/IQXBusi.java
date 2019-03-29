package cn.com.startai.qxsdk.event;

import android.support.annotation.NonNull;

import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.Login;

/**
 * 对外
 */
public interface IQXBusi {


//    /**
//     * 消息透传
//     *
//     * @param req
//     * @param callListener
//     */
//    void passthrough(PassthroughReq req, IQXCallListener callListener);
//
//    /**
//     * 修备注名
//     *
//     * @param req      对方的sn 跟新的备注名
//     * @param listener
//     */
//    void updateRemark(C_0x8015.Req.ContentBean req, IOnCallListener listener);
//
//
//    /**
//     * 与设备进行连接设备
//     *
//     * @param deviceBean
//     * @param callListener
//     */
//    void connectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener);
//
//    /**
//     * 断开与设备的连接
//     *
//     * @param deviceBean
//     * @param callListener
//     */
//    void disConnectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener);
//

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
//
//    /**
//     * 绑定设备
//     *
//     * @param callListener
//     */
//    void bindDevice(@NonNull BindDeviceReq req, IQXCallListener callListener);
//
//
//    /**
//     * 解绑设备
//     *
//     * @param callListener
//     */
//    void unBindDevice(@NonNull UnBindDeviceReq req, IQXCallListener callListener);
//
//    /**
//     * 查询绑定关系
//     *
//     * @return
//     */
//    void getBindDeviceList(IQXCallListener callListener);
//
//
//    /**
//     * 获取最新软件版本
//     *
//     * @param callListener
//     */
//    void getLatestVersion(IQXCallListener callListener);
//
//    /**
//     * 修改用户登录密码
//     *
//     * @param oldPwd       老密码
//     * @param newPwd       新密码
//     * @param callListener
//     */
//    void updateUserPwd(String oldPwd, String newPwd, IQXCallListener callListener);
//
//    /**
//     * 更新用户信息
//     *
//     * @param contentBean
//     * @param callListener
//     */
//    void updateUserInfo(C_0x8020.Req.ContentBean contentBean, IQXCallListener callListener);
//
//    /**
//     * 查询用户信息
//     *
//     * @param callListener
//     */
//    void getUserInfo(IQXCallListener callListener);
//
//    /**
//     * 请求发送邮件
//     *
//     * @param req 邮箱
//     */
//    void sendEmail(C_0x8023.Req.ContentBean req, IQXCallListener callListener);
//
//
//    /**
//     * 第三方账号登录
//     *
//     * @param contentBean  第三方登录参数
//     * @param callListener
//     */
//    void loginWithThirdAccount(C_0x8027.Req.ContentBean contentBean, IQXCallListener callListener);
//
//
//    /**
//     * 登出
//     */
//    void logout();
//

    /**
     * 登录
     *
     * @param req
     * @param callListener
     */
    void login(@NonNull Login.Req req, IOnCallListener callListener);

//
//
//    /**
//     * 注册
//     *
//     * @param req
//     * @param callListener
//     */
//    void register(@NonNull C_0x8017.Req.ContentBean req, IQXCallListener callListener);
//
//
//    /**
//     * 获取手机验证码
//     *
//     * @param req
//     * @param callListener
//     */
//    void getIdentifyCode(@NonNull C_0x8021.Req.ContentBean req, IQXCallListener callListener);
//
//    /**
//     * 检验手机验证码
//     *
//     * @param req
//     * @param callListener
//     */
//    void checkIdentifyCode(@NonNull C_0x8022.Req.ContentBean req, IQXCallListener callListener);
//
//
//    /**
//     * sdk 注销
//     */
//    void unInit();
//
//    /**
//     * sdk 初始化
//     *
//     * @param application
//     * @param qxInitParam
//     * @param iqxBusiResultListener
//     */
//    void init(@NonNull Application application, @NonNull QXInitParam qxInitParam, IQXBusiResultListener iqxBusiResultListener);
//
//
//    Application getApp();
//
//
//    /**
//     * 注销激活
//     */
//    void unActivite(IQXCallListener callListener);
//

    /**
     * 帮第三方设备激活
     */
    void hardwareActivate(Activate.Req req, IOnCallListener callListener);
//
//
//    /**
//     * 更新设备信息
//     */
//    void updateDeviceInfo(IQXCallListener callListener);
//
//
//    /**
//     * 重置密码
//     *
//     * @param req          手机号 或邮箱号
//     * @param callListener
//     */
//    void resetLoginPwd(C_0x8026.Req.ContentBean req, IQXCallListener callListener);
//
//
//    /**
//     * 第三方支付 统一下单
//     */
//    void thirdPaymentUnifiedOrder(C_0x8028.Req.ContentBean contentBean, IQXCallListener callListener);
//
//    /**
//     * 查询真实订单支付结果
//     */
//    void getRealOrderPayStatus(String orderNum, IQXCallListener callListener);
//
//    /**
//     * 查询支付宝认证信息
//     *
//     * @param callListener
//     */
//    void getAlipayAuthInfo(String authType, IQXCallListener callListener);
//
//    /**
//     * 关联手机号
//     *
//     * @param callListener
//     */
//    void bindMobileNum(C_0x8034.Req.ContentBean req, IQXCallListener callListener);
//
//
//    /**
//     * 解绑第三方账号
//     *
//     * @param req
//     * @param callListener
//     */
//    void unBindThirdAccount(C_0x8036.Req.ContentBean req, IQXCallListener callListener);
//
//    /**
//     * 绑定第三方账号
//     *
//     * @param req
//     * @param callListener
//     */
//    void bindThirdAccount(C_0x8037.Req.ContentBean req, IQXCallListener callListener);
//
//    /**
//     * 查询天气信息
//     *
//     * @param req
//     * @param callListener
//     */
//    void getWeatherInfo(C_0x8035.Req.ContentBean req, IQXCallListener callListener);
//
//    /**
//     * 绑定邮箱
//     *
//     * @param req
//     * @param callListener
//     */
//    void bindEmail(C_0x8039.Req.ContentBean req, IQXCallListener callListener);
//
//    /**
//     * 查询 绑定列表 分页
//     */
//    void getBindListByPage(C_0x8038.Req.ContentBean req, IQXCallListener callListener);


}
