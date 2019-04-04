//package cn.com.startai.qxsdk.event;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.com.startai.qxsdk.busi.entity.Activate;
//import cn.com.startai.qxsdk.busi.entity.Bind;
//import cn.com.startai.qxsdk.busi.entity.BindEmail;
//import cn.com.startai.qxsdk.busi.entity.BindMobile;
//import cn.com.startai.qxsdk.busi.entity.BindThirdAccount;
//import cn.com.startai.qxsdk.busi.entity.CheckIdentifyCode;
//import cn.com.startai.qxsdk.busi.entity.GetIdentifyCode;
//import cn.com.startai.qxsdk.busi.entity.GetLatestAppVersion;
//import cn.com.startai.qxsdk.busi.entity.GetUserInfo;
//import cn.com.startai.qxsdk.busi.entity.Login;
//import cn.com.startai.qxsdk.busi.entity.Register;
//import cn.com.startai.qxsdk.busi.entity.ResetLoginPwd;
//import cn.com.startai.qxsdk.busi.entity.SendEmail;
//import cn.com.startai.qxsdk.busi.entity.UnActivate;
//import cn.com.startai.qxsdk.busi.entity.UnBindThirdAccount;
//import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
//import cn.com.startai.qxsdk.busi.entity.UpdateLoginPwd;
//import cn.com.startai.qxsdk.busi.entity.UpdateRemark;
//import cn.com.startai.qxsdk.busi.entity.UpdateUserInfo;
//import cn.com.startai.qxsdk.db.bean.DeviceBean;
//import cn.com.startai.qxsdk.global.QXError;
//
///**
// * Created by Robin on 2019/3/25.
// * 419109715@qq.com 彬影
// */
//public class QXEventManager implements IQXBusiResultListener {
//
//    private QXEventManager() {
//    }
//
//    private static QXEventManager instance;
//
//    public static QXEventManager getInstance() {
//        if (instance == null) {
//            synchronized (QXEventManager.class) {
//                if (instance == null) {
//                    instance = new QXEventManager();
//                }
//            }
//        }
//        return instance;
//    }
//
//
//    protected List<IQXBusiResultListener> busiListenerList = new ArrayList<>();
//
//    public void addQXBusiResultListener(IQXBusiResultListener listener) {
//        synchronized (this) {
//            if (null != listener) {
//                if (null == this.busiListenerList) {
//                    this.busiListenerList = new ArrayList<>();
//                }
//                this.busiListenerList.add(listener);
//            }
//        }
//    }
//
//    public void removeBusiResultListener(IQXBusiResultListener listener) {
//        synchronized (this) {
//            if (null != listener && null != this.busiListenerList && 0 < this.busiListenerList.size()) {
//                this.busiListenerList.remove(listener);
//            }
//        }
//    }
//
//
//    /**
//     * 修改备注名结果
//     *
//     * @param resp 成功内容
//     */
//    @Override
//    public void onUpdateRemarkResult(UpdateRemark.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onUpdateRemarkResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 查询最新软件版本结果
//     *
//     * @param resp 最新软件版本信息
//     */
//    @Override
//    public void onGetLatestVersionResult(GetLatestAppVersion.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onGetLatestVersionResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 更新用户密码返回
//     *
//     * @param resp 用户密码信息
//     */
//    @Override
//    public void onUpdateLoginPwdResult(UpdateLoginPwd.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onUpdateLoginPwdResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 更新用户信息结果
//     *
//     * @param resp
//     */
//    @Override
//    public void onUpdateUserInfoResult(UpdateUserInfo.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onUpdateUserInfoResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 更新设备信息
//     *
//     * @param resp
//     */
//    @Override
//    public void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onUpdateDeviceInfoResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 查询用户信息结果
//     *
//     * @param resp
//     */
//    @Override
//    public void onGetUserInfoResult(GetUserInfo.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onGetUserInfoResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 发送邮件结果返回
//     *
//     * @param resp 成功的信息
//     */
//    @Override
//    public void onSendEmailResult(SendEmail.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onSendEmailResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 登录过期 需要重新登录
//     */
//    @Override
//    public void onLoginExpired() {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onLoginExpired();
//            }
//        }
//    }
//
//    /**
//     * 登录结果
//     *
//     * @param resp
//     */
//    @Override
//    public void onLoginResult(Login.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onLoginResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 注册结果回调
//     *
//     * @param resp
//     */
//    @Override
//    public void onRegisterResult(Register.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onRegisterResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 检验验证码结果
//     *
//     * @param resp
//     */
//    @Override
//    public void onCheckIdetifyResult(CheckIdentifyCode.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onCheckIdetifyResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 获取验证码结果
//     *
//     * @param resp
//     */
//    @Override
//    public void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onGetIdentifyCodeResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 与服务器连接成功
//     */
//    @Override
//    public void onServerConnected() {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onServerConnected();
//            }
//        }
//    }
//
//    /**
//     * 与服务器断开连接|连接失败
//     *
//     * @param qxError
//     */
//    @Override
//    public void onServerDisConnect(QXError qxError) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onServerDisConnect(qxError);
//            }
//        }
//    }
//
//    /**
//     * 重连接中
//     */
//    @Override
//    public void onServerReConnecting() {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onServerReConnecting();
//            }
//        }
//    }
//
//    /**
//     * 发现一台设备（局域网）
//     *
//     * @param deviceBean
//     */
//    @Override
//    public void onDiscoveryResult(DeviceBean deviceBean) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onDiscoveryResult(deviceBean);
//            }
//        }
//    }
//
//    /**
//     * 开始发现（局域网）
//     */
//    @Override
//    public void onDiscoveryStart() {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onDiscoveryStart();
//            }
//        }
//    }
//
//    /**
//     * 停止发现（局域网）
//     */
//    @Override
//    public void onDiscoveryStop() {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onDiscoveryStop();
//            }
//        }
//    }
//
//    /**
//     * 设备激活回调，如果激活成功只会回调一次
//     *
//     * @param resp
//     */
//    @Override
//    public void onActiviteResult(Activate.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onActiviteResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 第三方硬件激活结果
//     *
//     * @param resp
//     */
//    @Override
//    public void onHardwareActivateResult(Activate.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onHardwareActivateResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 添加好友回调
//     *
//     * @param resp
//     * @param id        自己的id
//     * @param bebindingBean 被绑定者 开发者需要持久化，在向对端发送消息时需要携带此bebinding的id
//     */
//    @Override
//    public void onBindResult(Bind.Resp resp, String id, Bind.Resp.ContentBean.BebindingBean bebindingBean) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onBindResult(resp,id,bebindingBean);
//            }
//        }
//    }
//
//    /**
//     * 注销激活
//     *
//     * @param resp
//     */
//    @Override
//    public void onUnActiviteResult(UnActivate.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onUnActiviteResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 重置登录密码结果
//     *
//     * @param resp 成功内容
//     */
//    @Override
//    public void onResetLoginPwdResult(ResetLoginPwd.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onResetLoginPwdResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 绑定手机号返回
//     *
//     * @param resp
//     */
//    @Override
//    public void onBindMobileNumResult(BindMobile.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onBindMobileNumResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 解绑第三方账号返回
//     *
//     * @param resp
//     */
//    @Override
//    public void onUnBindThirdAccountResult(UnBindThirdAccount.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onUnBindThirdAccountResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 绑定第三方账号返回
//     *
//     * @param resp
//     */
//    @Override
//    public void onBindThirdAccountResult(BindThirdAccount.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onBindThirdAccountResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 绑定邮箱返回
//     *
//     * @param resp
//     */
//    @Override
//    public void onBindEmailResult(BindEmail.Resp resp) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onBindEmailResult(resp);
//            }
//        }
//    }
//
//    /**
//     * 登出
//     *
//     * @param result
//     */
//    @Override
//    public void onLogoutResult(int result) {
//        if (busiListenerList != null) {
//            for (final IQXBusiResultListener listener : busiListenerList) {
//                listener.onLogoutResult(result);
//            }
//        }
//    }
//
//
//}