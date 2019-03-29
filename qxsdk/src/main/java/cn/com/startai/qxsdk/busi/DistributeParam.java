package cn.com.startai.qxsdk.busi;


import cn.com.startai.qxsdk.connect.mqtt.MsgType;

/**
 * Created by Robin on 2018/12/27.
 * qq: 419109715 彬影
 */

public class DistributeParam {


    public static boolean ACTIVITE_DISTRIBUTE = false;// 激活
    public static boolean GETBROKERHOST_DISTRIBUTE = false;//获取区域节点
    public static boolean REGISTER_DISTRIBUTE = false;//注册
    public static boolean LOGIN_DISTRIBUTE = false;//登录
    public static boolean GETIDENTIFYCODE_DISTRIBUTE = false;//获取手机验证码
    public static boolean CHECKIDENTIFYCODE_DISTRIBUTE = false;//检验手机验证码
    public static boolean UNACTIVITE_DISTRIBUTE = false;//注销激活
    public static boolean UPDATEDEVICEINFO_DISTRIBUTE = false;//更新设备信息
    public static boolean GETLATESTVERSION_DISTRIBUTE = false;//获取最新软件版本
    public static boolean UPDATEUSERPWD_DISTRIBUTE = false;//修改用户登录密码
    public static boolean SENDEMAIL_DISTRIBUTE = false;//请求发送邮件
    public static boolean RESETMOBILELOGINPWD_DISTRIBUTE = false;//重置手机登录密码
    public static boolean THIRDPAYMENTUNIFIEDORDER_DISTRIBUTE = false;//第三方支付 统一下单
    public static boolean GETALIPAYAUTHINFO_DISTRIBUTE = false;//查询支付宝认证信息
    public static boolean GETWEATHERINFO_DISTRIBUTE = false;//查询天气
    public static boolean LOGINWITHTHIRDACCOUNT_DISTRIBUTE = false;//第三方账号登录
    public static boolean GETREALORDERPAYSTATUS_DISTRIBUTE = false;//查询真实订单支付结果


    public static boolean BIND_DISTRIBUTE = true;//添加设备或好友
    public static boolean UNBIND_DISTRIBUTE = true;//删除设备或好友
    public static boolean GETBINDLIST_DISTRIBUTE = true;//查询绑定关系
    public static boolean PASSTHROUGH_DISTRIBUTE = true;//消息透传
    public static boolean UPDATEUSERINFO_DISTRIBUTE = true;//更新用户信息
    public static boolean GETUSERINFO_DISTRIBUTE = true;//查询用户信息
    public static boolean UPDATEREMARK_DISTRIBUTE = true;//修改好友|设备 备注名
    public static boolean UNBINDTHIRDACCOUNT_DISTRIBUTE = true;//解绑第三方账号
    public static boolean BINDTHIRDACCOUNT_DISTRIBUTE = true;//绑定第三方账号
    public static boolean BINDMOBILENUM_DISTRIBUTE = true;//关联手机号
    public static boolean BINDEMAILNUM_DISTRIBUTE = true;//关联邮箱号
    public static boolean GETBINDLISTBYPAGE_DISTRIBUTE = true;//分页查询好友列表

    public static boolean isDistribute(String msgType) {
        switch (msgType) {
            case MsgType.TYPE_LOGIN:

                return LOGIN_DISTRIBUTE;

            case MsgType.TYPE_ACTIVATE:

                return ACTIVITE_DISTRIBUTE;

            case MsgType.TYPE_GET_BROKER_HOST:

                return GETBROKERHOST_DISTRIBUTE;
            default:
                return true;


        }
    }

}
