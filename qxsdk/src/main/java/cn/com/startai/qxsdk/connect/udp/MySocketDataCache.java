package cn.com.startai.qxsdk.connect.udp;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.com.startai.qxsdk.QXUserManager;
import cn.com.startai.qxsdk.db.QXDBManager;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.swain.baselib.app.IApp.IService;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.support.protocolEngine.ProtocolBuild;
import cn.com.swain.support.protocolEngine.Repeat.RepeatMsgModel;
import cn.com.swain.support.protocolEngine.datagram.SocketDataArray;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.ISocketDataProducer;
import cn.com.swain.support.protocolEngine.datagram.dataproducer.SyncSocketDataQueueProducer;
import cn.com.swain.support.protocolEngine.pack.ResponseData;
import cn.com.swain.support.protocolEngine.utils.SEQ;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * author: Guoqiang_Sun
 * date : 2018/9/13 0013
 * desc :
 */
public class MySocketDataCache implements IService {

    private static final class MClassHolder {
        private static final MySocketDataCache CACHE = new MySocketDataCache();
    }

    public static MySocketDataCache getInstance() {
        return MClassHolder.CACHE;
    }

    public static class BuildParams {
        int mProtocolVersion;
        byte mCustom;
        byte mProduct;

        void release() {

        }

        public void setCustom(byte custom) {
            this.mCustom = custom;
        }

        public void setProduct(byte product) {
            this.mProduct = product;
        }

        public void setProtocolVersion(byte protocolVersion) {
            this.mProtocolVersion = protocolVersion;
        }

    }


    private static int protocolVersion = ProtocolBuild.VERSION.VERSION_0;

    private static byte custom;
    private static byte product;

    public byte getProduct() {
        return product;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }


    public void init(BuildParams mParams) {
        if (mParams != null) {
            custom = mParams.mCustom;
            product = mParams.mProduct;
            protocolVersion = mParams.mProtocolVersion;
        }
    }


    @Override
    public void onSCreate() {

        Tlog.v(TAG, " custom:" + custom + " product:" + product + " protocolVersion:" + protocolVersion);

        checkISocketDataProducer();

    }

    @Override
    public void onSResume() {

    }

    @Override
    public void onSPause() {

    }

    @Override
    public void onSDestroy() {

        if (mDeviceMap != null) {
            mDeviceMap.clear();
        }

        if (mTokenMap != null) {
            mTokenMap.clear();
        }

        if (mSocketDataProducer != null) {
            mSocketDataProducer.clear();
        }

    }

    @Override
    public void onSFinish() {

    }

    private static final Object synObj = new byte[1];

    private static final Map<String, SEQ> mDeviceMap = Collections.synchronizedMap(new HashMap<String, SEQ>());

    protected static SEQ getDevice(String mac) {
        SEQ device = mDeviceMap.get(mac);
        if (device == null) {
            synchronized (synObj) {
                device = mDeviceMap.get(mac);
                if (device == null) {
                    device = new SEQ(mac);
                    mDeviceMap.put(mac, device);
                }
            }
        }
        return device;
    }

    private static final Map<String, Integer> mTokenMap = Collections.synchronizedMap(new HashMap<String, Integer>());

    protected static int getToken(String mac) {
        int token2 = getToken2(mac);
        if (token2 == -1) { // token =-1 单片机有问题
            token2 = 0;
        }
        return token2;
    }

    protected static int getToken2(String mac) {
        Integer integer = mTokenMap.get(mac);
        if (integer == null) {
            DeviceBean deviceBeanByUseridAndMac = QXDBManager.getInstance().getDeviceBeanByUseridAndMac(QXUserManager.getInstance().getUserId(), mac);
            if (deviceBeanByUseridAndMac != null) {
                return deviceBeanByUseridAndMac.getToken();
            }
//            return LSpManager.getInstance().getToken(LanParamManager.getInstance().getUserId(), mac);
        }
        return integer;
    }

    public static void putToken(String mac, int token) {
        mTokenMap.put(mac, token);
    }


    private ISocketDataProducer mSocketDataProducer;

    protected void checkISocketDataProducer() {
        if (mSocketDataProducer == null) {
            synchronized (synObj) {
                if (mSocketDataProducer == null) {
                    mSocketDataProducer = new SyncSocketDataQueueProducer(protocolVersion);
                }
            }
        }
    }

    protected ISocketDataProducer getSocketDataProducer() {
        checkISocketDataProducer();
        return mSocketDataProducer;
    }

    protected synchronized SocketDataArray produceSocketDataArray(String mac) {
        final SocketDataArray mSecureDataPack = produceSocketDataArrayNoSeq(mac, -1);
        mSecureDataPack.setSeq((byte) (getDevice(mac).getSelfAddSeq() & 0xFF));
        return mSecureDataPack;
    }

    protected synchronized SocketDataArray produceSocketDataArrayNoSeq(String mac, int token) {
        final SocketDataArray mSecureDataPack = getSocketDataProducer().produceSocketDataArray();
        mSecureDataPack.setISUsed();
        mSecureDataPack.reset();
        mSecureDataPack.changeStateToEscape();
        if (token == -1 || token == 0) {
            token = getToken(mac);
        }
        mSecureDataPack.setToken(token);
        mSecureDataPack.setCustom(custom);
        mSecureDataPack.setProduct(product);
        return mSecureDataPack;
    }

    protected static ResponseData newResponseDataNoRecord(String mac, SocketDataArray mPack) {
        return newResponseData(mac, mPack, false);
    }

    protected static ResponseData newResponseDataRecord(String mac, SocketDataArray mPack) {
        return newResponseData(mac, mPack, true);
    }

    protected static ResponseData newResponseData(String mac, SocketDataArray mPack, boolean record) {
        final ResponseData responseData = new ResponseData(mac, mPack.organizeProtocolData());
        RepeatMsgModel repeatMsgModel = responseData.getRepeatMsgModel();
        repeatMsgModel.setMsgSeq(mPack.getSeq() & 0xFF);
        repeatMsgModel.setCustom(mPack.getCustom());
        repeatMsgModel.setProduct(mPack.getProduct());
        repeatMsgModel.setMsgWhat((mPack.getType() & 0xFF) << 8 | (mPack.getCmd() & 0xFF));
        repeatMsgModel.setNeedRepeatSend(record);
        responseData.getSendModel().setModelIsLan().setModelIsWan();
        mPack.setISUnUsed();
        return responseData;
    }


    protected static ResponseData newResponseDataReport(String mac, SocketDataArray mPack) {
        return newResponseDataNoRecord(mac, mPack);
    }


    /********控制*********/


    /**
     * 查询继电器开关的数据包
     *
     * @return ResponseData
     */
    public static ResponseData getQuickQueryRelayStatus(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUICK_QUERY_SWITCH);

//        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
//        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_RELAY_STATUS);

        mSecureDataPack.setParams(new byte[]{SocketSecureKey.Model.MODEL_RELAY});
        ResponseData responseData = newResponseDataNoRecord(mac, mSecureDataPack);
        responseData.getSendModel().fillEmpty();
        responseData.getSendModel().setModelCasual();
        return responseData;
    }

    /**
     * 继电器开关的数据包
     *
     * @return ResponseData ResponseData
     */
    public static ResponseData getQuickSetRelaySwitch(String mac, boolean on) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUICK_CONTROL_SWITCH);

//        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
//        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_RELAY_SWITCH);

        final byte[] params = new byte[2];
        params[0] = SocketSecureKey.Model.MODEL_RELAY;
        params[1] = SocketSecureKey.Util.on(on);
        mSecureDataPack.setParams(params);
        ResponseData responseData = newResponseDataRecord(mac, mSecureDataPack);
        responseData.getSendModel().fillEmpty();
        responseData.getSendModel().setModelCasual();
        return responseData;
    }


    /**
     * 继电器开关的数据包
     *
     * @return ResponseData ResponseData
     */
    public static ResponseData getSetRelaySwitch(String mac, boolean on) {

        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_RELAY_SWITCH);
        final byte[] params = new byte[2];
        params[0] = SocketSecureKey.Model.MODEL_RELAY;
        params[1] = SocketSecureKey.Util.on(on);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    /**
     * 倒计时
     *
     * @return ResponseData
     */
    public static ResponseData getSetCountdown(String mac, boolean status, boolean switchGear, int hour, int minute) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_COUNTDOWN);

        final byte[] params = new byte[4];
        params[0] = SocketSecureKey.Util.startup(status); // 1 启动 2 结束
        params[1] = SocketSecureKey.Util.on(switchGear); // 1 开 0 关
        params[2] = (byte) (hour & 0xFF);
        params[3] = (byte) (minute & 0xFF);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    /**
     * 温度 湿度 (上限)
     *
     * @return ResponseData
     */
    public static ResponseData getSetTempHumidityAlarm(String mac, boolean startup, int model,
                                                       int valueInt, int valueDeci, boolean limitUp) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_ALARM);

        final byte[] params = new byte[5];
        params[0] = SocketSecureKey.Util.startup(startup);
        params[1] = (byte) model;
        params[2] = SocketSecureKey.Util.limitUp(limitUp);
        params[3] = (byte) (valueInt & 0xFF);
        params[4] = (byte) (valueDeci & 0xFF);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getSetCommonTiming(String mac, byte id, byte state, boolean on, byte week,
                                                  byte hour, byte minute, boolean startup) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_TIMING);

        final byte[] params = new byte[8];
        params[0] = SocketSecureKey.Model.TIMING_COMMON;//model 普通模式
        params[1] = id;
        params[2] = state;
        params[3] = SocketSecureKey.Util.on(on);
        params[4] = week;
        params[5] = hour;
        params[6] = minute;
        params[7] = SocketSecureKey.Util.startup(startup);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getSetAdvanceTiming(String mac, byte id, byte state, byte startHour, byte startMinute,
                                                   byte stopHour, byte stopMinute, boolean on, byte onIntervalHour,
                                                   byte onIntervalMinute, byte offIntervalHour,
                                                   byte offIntervalMinute, boolean startup, byte week) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_TIMING);

        final byte[] params = new byte[14];
        params[0] = SocketSecureKey.Model.TIMING_ADVANCE;//model 高级模式
        params[1] = id;
        params[2] = state;
        params[3] = startHour;
        params[4] = startMinute;
        params[5] = stopHour;
        params[6] = stopMinute;
        params[7] = SocketSecureKey.Util.on(on);
        params[8] = onIntervalHour;
        params[9] = onIntervalMinute;
        params[10] = offIntervalHour;
        params[11] = offIntervalMinute;
        params[12] = SocketSecureKey.Util.startup(startup);
        params[13] = week;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getSetTimingTemp(String mac, byte id, byte state, byte startHour, byte startMinute,
                                                byte stopHour, byte stopMinute, boolean on, byte onIntervalHour,
                                                byte onIntervalMinute, byte offIntervalHour,
                                                byte offIntervalMinute, boolean startup, byte week, byte value, byte hotCode) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_TEMP_HUMI_ALARM);

        final byte[] params = new byte[16];
        params[0] = SocketSecureKey.Model.ALARM_MODEL_TEMPERATURE;//model 定温度模式
        params[1] = id;
        params[2] = state;
        params[3] = startHour;
        params[4] = startMinute;
        params[5] = stopHour;
        params[6] = stopMinute;
        params[7] = SocketSecureKey.Util.on(on);
        params[8] = onIntervalHour;
        params[9] = onIntervalMinute;
        params[10] = offIntervalHour;
        params[11] = offIntervalMinute;
        params[12] = SocketSecureKey.Util.startup(startup);
        params[13] = week;
        params[14] = value;
        params[15] = hotCode;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryTimingTemp(String mac, int model) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TEMP_HUMI_ALARM);

        final byte[] params = new byte[2];
        params[0] = SocketSecureKey.Model.ALARM_MODEL_TEMPERATURE;
        params[1] = (byte) model;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getSetWisdomNightLight(String mac, boolean startup) {
        return getSetWisdomNightLight(mac,
                startup,
                (byte) 0x0, (byte) 0x0,
                (byte) 0x18, (byte) 0x18);
    }

    public static ResponseData getSetWisdomNightLight(String mac,
                                                      boolean startup,
                                                      byte startHour, byte startMinute,
                                                      byte stopHour, byte stopMinute) {
        return getSetNightLight(mac,
                SocketSecureKey.Model.MODEL_NIGHT_LIGHT_WISDOM,
                startup, startHour, startMinute,
                stopHour, stopMinute);
    }

    public static ResponseData getSetTimingNightLight(String mac,
                                                      boolean startup,
                                                      byte startHour, byte startMinute,
                                                      byte stopHour, byte stopMinute) {
        return getSetNightLight(mac,
                SocketSecureKey.Model.MODEL_NIGHT_LIGHT_TIMING,
                startup, startHour, startMinute,
                stopHour, stopMinute);
    }

    public static ResponseData getSetNightLight(String mac, byte id,
                                                boolean startup,
                                                byte startHour, byte startMinute,
                                                byte stopHour, byte stopMinute) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_NIGHT_LIGHT);

        final byte[] params = new byte[6];
        params[0] = id;
        params[1] = SocketSecureKey.Util.startup(startup);
        params[2] = startHour;
        params[3] = startMinute;
        params[4] = stopHour;
        params[5] = stopMinute;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryWisdomNightLight(String mac) {
        return getQueryNightLight(mac, SocketSecureKey.Model.MODEL_NIGHT_LIGHT_WISDOM);
    }

    public static ResponseData getQueryTimingNightLight(String mac) {
        return getQueryNightLight(mac, SocketSecureKey.Model.MODEL_NIGHT_LIGHT_TIMING);
    }

    public static ResponseData getQueryRunningNightLight(String mac) {
        return getQueryNightLight(mac, SocketSecureKey.Model.MODEL_NIGHT_LIGHT_RUNNING);
    }

    public static ResponseData getQueryNightLight(String mac, int id) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_NIGHT_LIGHT);
        final byte[] params = new byte[1];
        params[0] = (byte) id;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryTempSensorStatus(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TEMP_SENSOR_STATUS);
        final byte[] params = new byte[1];
        params[0] = SocketSecureKey.Model.MODEL_TEMP_SENSOR;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryAllIndicatorState(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_ANYNET_FLASH);
        final byte[] params = new byte[1];
        params[0] = (byte) 0xFF;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getTrunAllIndicatorState(String mac, boolean state) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_CONTROL_ANYNET_FLASH);
        final byte[] params = new byte[2];
        params[0] = (byte) 0xFF;
        params[1] = SocketSecureKey.Util.on(state);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getSetTime(String mac, byte year, byte month, byte day, byte hour, byte minute, byte second, byte week) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_TIME);

        final byte[] params = new byte[7];
        params[0] = year;
        params[1] = week;
        params[2] = month;
        params[3] = day;
        params[4] = hour;
        params[5] = minute;
        params[6] = second;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    /********查询*********/

    /**
     * 查询继电器开关的数据包
     *
     * @return ResponseData
     */
    public static ResponseData getQueryRelayStatus(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_RELAY_STATUS);
        mSecureDataPack.setParams(new byte[]{SocketSecureKey.Model.MODEL_RELAY});
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryCountdown(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_COUNTDOWN_DATA);

        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryTemperatureLimitUp(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TEMPERATURE_HUMIDITY_DATA);

        final byte[] params = new byte[]{SocketSecureKey.Model.ALARM_MODEL_TEMPERATURE,
                SocketSecureKey.Model.ALARM_LIMIT_UP};
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryTemperatureLimitDown(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TEMPERATURE_HUMIDITY_DATA);

        final byte[] params = new byte[]{SocketSecureKey.Model.ALARM_MODEL_TEMPERATURE,
                SocketSecureKey.Model.ALARM_LIMIT_DOWN};
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryHumidityLimitUp(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TEMPERATURE_HUMIDITY_DATA);

        final byte[] params = new byte[]{SocketSecureKey.Model.ALARM_MODEL_HUMIDITY,
                SocketSecureKey.Model.ALARM_LIMIT_UP};
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryHumidityLimitDown(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TEMPERATURE_HUMIDITY_DATA);

        final byte[] params = new byte[]{SocketSecureKey.Model.ALARM_MODEL_HUMIDITY,
                SocketSecureKey.Model.ALARM_LIMIT_DOWN};
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryCommonTimingList(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TIMING_LIST_DATA);

        final byte[] params = new byte[]{SocketSecureKey.Model.TIMING_COMMON};
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryAdvanceTimingList(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TIMING_LIST_DATA);

        final byte[] params = new byte[]{SocketSecureKey.Model.TIMING_ADVANCE};
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryTime(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TIME);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getSetTimezone(String mac, byte zone) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_TIMEZONE);
        mSecureDataPack.setParams(new byte[]{zone});
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getQueryTimezone(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_TIMEZONE);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    /**
     * 查询定电量
     *
     * @param mac id
     * @return ResponseData
     */
    public static ResponseData getQuerySpendingElectricityE(String mac) {
        return getQuerySpendingElectricity(mac, SocketSecureKey.Model.SPENDING_ELECTRICITY_E);
    }

    /**
     * 查询定花费
     *
     * @param mac id
     * @return ResponseData
     */
    public static ResponseData getQuerySpendingElectricityS(String mac) {
        return getQuerySpendingElectricity(mac, SocketSecureKey.Model.SPENDING_ELECTRICITY_S);
    }

    private static ResponseData getQuerySpendingElectricity(String mac, byte model) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_SPENDING_ELECTRICITY_DATA);

        mSecureDataPack.setParams(new byte[]{model});
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getSetSpendingCountdown(String mac, boolean startup, byte model, byte y, byte m, byte d, int value) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_SPENDING_ELECTRICITY_DATA);

        byte[] params = new byte[7];
        params[0] = SocketSecureKey.Util.startup(startup);
        params[1] = model;
        params[2] = y;
        params[3] = m;
        params[4] = d;
        params[5] = (byte) ((value >> 8) & 0xFF);
        params[6] = (byte) (value & 0xFF);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    /********上报的回复**********/

    /**
     * 温度湿度response
     *
     * @param mac     id
     * @param success 是否成功
     * @return ResponseData
     */
    public static ResponseData getTempHumiValueReport(String mac, boolean success, byte seq) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArrayNoSeq(mac, -1);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_REPORT);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_TEMP_HUMI_REPORT_RESPONSE);
        mSecureDataPack.setSeq(seq);
        final byte[] params = new byte[]{SocketSecureKey.Util.resultSuccess(success)};
        mSecureDataPack.setParams(params);
        return newResponseDataReport(mac, mSecureDataPack);
    }

    /**
     * 温度湿度设置结束后的回复
     *
     * @param mac     id
     * @param success 是否成功
     * @return ResponseData
     */
    public static ResponseData getTempHumidityExecuteReport(String mac, boolean success, byte seq) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArrayNoSeq(mac, -1);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_REPORT);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_TEMPERATURE_HUMIDITY_REPORT_RESPONSE);
        mSecureDataPack.setSeq(seq);
        final byte[] params = new byte[]{SocketSecureKey.Util.resultSuccess(success)};
        mSecureDataPack.setParams(params);
        return newResponseDataReport(mac, mSecureDataPack);
    }

    /**
     * 电源的response
     *
     * @param mac     id
     * @param success 是否成功
     * @return ResponseData
     */
    public static ResponseData getElectricValueReport(String mac, boolean success, byte seq) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArrayNoSeq(mac, -1);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_REPORT);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_POWER_FREQ_REPORT_RESPONSE);
        mSecureDataPack.setSeq(seq);
        final byte[] params = new byte[]{SocketSecureKey.Util.resultSuccess(success)};
        mSecureDataPack.setParams(params);
        return newResponseDataReport(mac, mSecureDataPack);
    }

    public static ResponseData getTimingExecuteReport(String mac, boolean success, byte seq) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArrayNoSeq(mac, -1);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_REPORT);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_TIMING_REPORT_RESPONSE);
        mSecureDataPack.setSeq(seq);
        final byte[] params = new byte[]{SocketSecureKey.Util.resultSuccess(success)};
        mSecureDataPack.setParams(params);
        return newResponseDataReport(mac, mSecureDataPack);
    }

    public static ResponseData getCountdownExecuteReport(String mac, boolean success, byte seq) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArrayNoSeq(mac, -1);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_REPORT);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_COUNTDOWN_REPORT_RESPONSE);
        mSecureDataPack.setSeq(seq);
        final byte[] params = new byte[]{SocketSecureKey.Util.resultSuccess(success)};
        mSecureDataPack.setParams(params);
        return newResponseDataReport(mac, mSecureDataPack);
    }


    /********System**********/

    /**
     * 心跳报
     *
     * @return ResponseData
     */
    public static ResponseData getHeartbeat(String mac, int token, int seq) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArrayNoSeq(mac, token);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_HEARTBEAT);
        mSecureDataPack.setSeq((byte) (seq & 0xFF));
        final byte[] params = new byte[4];
        params[0] = (byte) ((token >> 24) & 0xFF);
        params[1] = (byte) ((token >> 16) & 0xFF);
        params[2] = (byte) ((token >> 8) & 0xFF);
        params[3] = (byte) ((token) & 0xFF);
        mSecureDataPack.setParams(params);
        ResponseData responseData = newResponseDataNoRecord(mac, mSecureDataPack);
        responseData.getSendModel().setModelOnlyLan();
        responseData.getRepeatMsgModel().setMaxRepeatTimes(1);
        return responseData;
    }

    public static ResponseData getRecovery(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_RECOVERY_SCM);

        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public synchronized static ResponseData getBindDevice(String mac, byte[] userIDByte, byte info, byte[] pwdBytes) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_BIND_DEVICE);

        final byte[] params = new byte[65];
        int length = 0;
        if (userIDByte != null) {
            length = userIDByte.length;
        }
        System.arraycopy(EMPTY_BYTES_32, 0, params, 0, 32);
        if (length > 32) {
            length = 32;
        }
        if (length > 0) {
            System.arraycopy(userIDByte, 0, params, 0, length);
        }
//
        int pointInfo = 32;
        params[pointInfo] = info;
//
        length = 0;
        if (pwdBytes != null) {
            length = pwdBytes.length;
        }
        System.arraycopy(EMPTY_BYTES_32, 0, params, pointInfo + 1, 32);
        if (length > 32) {
            length = 32;
        }
        if (length > 0) {
            System.arraycopy(pwdBytes, 0, params, pointInfo + 1, length);
        }

        mSecureDataPack.setParams(params);
        ResponseData responseData = newResponseDataRecord(mac, mSecureDataPack);
        responseData.getSendModel().setModelOnlyLan();
        return responseData;
    }

    public synchronized static ResponseData getDiscoveryDevice(String mac, byte[] userIDByte) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_DISCOVERY_DEVICE);

        final byte[] params = new byte[32];

        int length = 0;
        if (userIDByte != null) {
            length = userIDByte.length;
        }
        System.arraycopy(EMPTY_BYTES_32, 0, params, 0, 32);
        if (length > 32) {
            length = 32;
        }
        if (length > 0) {
            System.arraycopy(userIDByte, 0, params, 0, length);
        }
        mSecureDataPack.setParams(params);
        ResponseData responseData = newResponseDataNoRecord(mac, mSecureDataPack);
        responseData.getSendModel().setModelOnlyLan();
        return responseData;
    }

    private static final byte[] EMPTY_BYTES_32 = new byte[]{
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    public static ResponseData getQueryDeviceName(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_NAME);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public synchronized static ResponseData getRename(String mac, byte[] nameBytes) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_RENAME);

        final byte[] params = new byte[32];
        int length = 0;
        if (nameBytes != null) {
            length = nameBytes.length;
        }
        System.arraycopy(EMPTY_BYTES_32, 0, params, 0, 32);

        if (length > 32) {
            length = 32;
        }
        if (length > 0) {
            System.arraycopy(nameBytes, 0, params, 0, length);
        }
        mSecureDataPack.setParams(params);
        ResponseData responseData = newResponseDataRecord(mac, mSecureDataPack);
        responseData.getSendModel().fillEmpty();
        responseData.getSendModel().setModelCasual();
        return responseData;
    }

    public static ResponseData getRequestToken(String mac, byte[] userID, int random) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_REQUEST_TOKEN);

        final byte[] params = new byte[32 + 4];
        int length = 0;
        if (userID != null) {
            length = userID.length;
        }
        System.arraycopy(EMPTY_BYTES_32, 0, params, 0, 32);

        if (length > 32) {
            length = 32;
        }
        if (length > 0) {
            System.arraycopy(userID, 0, params, 0, length);
        }

        params[32] = (byte) ((random >> 24) & 0xFF);
        params[32 + 1] = (byte) ((random >> 16) & 0xFF);
        params[32 + 2] = (byte) ((random >> 8) & 0xFF);
        params[32 + 3] = (byte) ((random) & 0xFF);

        mSecureDataPack.setParams(params);
        ResponseData responseData = newResponseDataRecord(mac, mSecureDataPack);
        responseData.getSendModel().setModelOnlyLan();
        return responseData;
    }

    public static ResponseData getControlDevice(String mac, byte[] userID, int token) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_CONTROL_TOKEN);


        final byte[] params = new byte[32 + 4];
        int length = 0;
        if (userID != null) {
            length = userID.length;
        }
        System.arraycopy(EMPTY_BYTES_32, 0, params, 0, 32);

        if (length > 32) {
            length = 32;
        }
        if (length > 0) {
            System.arraycopy(userID, 0, params, 0, length);
        }

        params[32] = (byte) ((token >> 24) & 0xFF);
        params[32 + 1] = (byte) ((token >> 16) & 0xFF);
        params[32 + 2] = (byte) ((token >> 8) & 0xFF);
        params[32 + 3] = (byte) ((token) & 0xFF);

        mSecureDataPack.setParams(params);

        ResponseData responseData = newResponseDataRecord(mac, mSecureDataPack);
        responseData.getSendModel().setModelOnlyLan();
        return responseData;
    }

    public static ResponseData getAppSleep(String mac, byte[] userID, int token) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SLEEP_TOKEN);


        final byte[] params = new byte[32 + 4];
        int length = 0;
        if (userID != null) {
            length = userID.length;
        }
        System.arraycopy(EMPTY_BYTES_32, 0, params, 0, 32);

        if (length > 32) {
            length = 32;
        }
        if (length > 0) {
            System.arraycopy(userID, 0, params, 0, length);
        }

        params[32] = (byte) ((token >> 24) & 0xFF);
        params[32 + 1] = (byte) ((token >> 16) & 0xFF);
        params[32 + 2] = (byte) ((token >> 8) & 0xFF);
        params[32 + 3] = (byte) ((token) & 0xFF);

        mSecureDataPack.setParams(params);
        ResponseData responseData = newResponseDataRecord(mac, mSecureDataPack);
        responseData.getSendModel().setModelOnlyLan();
        return responseData;
    }


    public static ResponseData getDisconnectDevice(String mac, byte[] userID, int token) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_DISCONTROL_TOKEN);


        final byte[] params = new byte[32 + 4];
        int length = 0;
        if (userID != null) {
            length = userID.length;
        }
        System.arraycopy(EMPTY_BYTES_32, 0, params, 0, 32);

        if (length > 32) {
            length = 32;
        }
        if (length > 0) {
            System.arraycopy(userID, 0, params, 0, length);
        }

        params[32] = (byte) ((token >> 24) & 0xFF);
        params[32 + 1] = (byte) ((token >> 16) & 0xFF);
        params[32 + 2] = (byte) ((token >> 8) & 0xFF);
        params[32 + 3] = (byte) ((token) & 0xFF);

        mSecureDataPack.setParams(params);
        ResponseData responseData = newResponseDataRecord(mac, mSecureDataPack);
        responseData.getSendModel().setModelOnlyLan();
        return responseData;
    }

    /**
     * 查询ssid
     */
    public static ResponseData getQuerySSID(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_SSID);
        ResponseData responseData = newResponseDataRecord(mac, mSecureDataPack);
        responseData.getSendModel().fillEmpty();
        responseData.getSendModel().setModelCasual();
        return responseData;
    }


    /**
     * 查询版本号
     */
    public static ResponseData getQueryVersion(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_UPDATE);
        mSecureDataPack.setParams(new byte[]{SocketSecureKey.Model.MODEL_QUERY_VERSION});
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    /**
     * 升级
     */
    public static ResponseData getUpdate(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_UPDATE);
        mSecureDataPack.setParams(new byte[]{SocketSecureKey.Model.MODEL_UPDATE});
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    //


    public static ResponseData getVoltageAlarmValue(String mac, int value) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_VOLTAGE_ALARM_VALUE);

        final byte[] params = new byte[2];
        params[0] = (byte) ((value >> 8) & 0xFF);
        params[1] = (byte) (value & 0xFF);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryVoltageAlarmValue(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_VOLTAGE_ALARM_VALUE);

        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getCurrentAlarmValue(String mac, int value) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_CURRENT_ALARM_VALUE);

        final byte[] params = new byte[2];
        params[0] = (byte) ((value >> 8) & 0xFF);
        params[1] = (byte) (value & 0xFF);

        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getQueryCurrentAlarmValue(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_CURRENT_ALARM_VALUE);

        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getPowerAlarmValue(String mac, int value) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_POWER_ALARM_VALUE);

        final byte[] params = new byte[2];
        params[0] = (byte) ((value >> 8) & 0xFF);
        params[1] = (byte) (value & 0xFF);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getQueryPowerAlarmValue(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_POWER_ALARM_VALUE);

        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getTempUnitBle(String mac, byte unit) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_UNIT_TEMPERATURE_BLE);

        final byte[] params = new byte[]{(byte) (unit & 0xFF)};
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getTempUnit(String mac, byte unit) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_UNIT_TEMPERATURE);

        final byte[] params = new byte[]{(byte) (unit & 0xFF)};
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getMonetaryUnit(String mac, byte unit) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_UNIT_MONETARY);

        final byte[] params = new byte[]{(byte) (unit & 0xFF)};
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getElectricityPrice(String mac, int prices) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_PRICES_ELECTRICITY);

        final byte[] params = new byte[2];
        params[0] = (byte) ((prices >> 8) & 0xFF);
        params[1] = (byte) (prices & 0xFF);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryTemperatureUnitBle(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SYSTEM);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_UNIT_TEMPERATURE_BLE);

        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryTemperatureUnit(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_UNIT_TEMPERATURE);

        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryMonetaryUnit(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_UNIT_MONETARY);

        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryElectricityPrices(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_SETTING);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_PRICES_ELECTRICITY);

        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getQueryHistoryCount(String mac, Date mStart, Date mEnd) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_HISTORY_COUNT);

        byte[] params = new byte[7];
        params[0] = (byte) (mStart.getYear() + 1900 - 2000);
        params[1] = (byte) (mStart.getMonth() + 1);
        params[2] = (byte) mStart.getDate();

        params[3] = (byte) (mEnd.getYear() + 1900 - 2000);
        params[4] = (byte) (mEnd.getMonth() + 1);
        params[5] = (byte) mEnd.getDate();

//                params[6] = (byte) mQueryCount.interval;
        params[6] = (byte) 0x01; // 始终查询五分钟间隔

        mSecureDataPack.setParams(params);
        return newResponseDataNoRecord(mac, mSecureDataPack);
    }

    /**
     * 设置费率
     */
    public static ResponseData getSetConstRate(String mac, byte model, byte hour, byte minute, short price) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_COST_RATE);

        final byte[] params = new byte[5];
        params[0] = model;
        params[1] = hour;
        params[2] = minute;
        params[3] = (byte) ((price >> 8) & 0xFF);
        params[4] = (byte) (model & 0xFF);

        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    /**
     * 查询费率
     */
    public static ResponseData getQueryConstRate(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_COST_RATE);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    /**
     * 查询积累参数
     */
    public static ResponseData getQueryCumuParam(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_CUMU_PARAM);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    /**
     * 查询最大输出
     */
    public static ResponseData getQueryMaxOutput(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_MAX_OUTPUT);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    // 彩灯
    public static ResponseData getQueryColorLamp(String mac, int seq) {
        return getQueryLightColor(mac, seq, SocketSecureKey.Model.MODEL_COLOR_LAMP);
    }

    // 黄灯
    public static ResponseData getQueryYellowLight(String mac, int seq) {
        return getQueryLightColor(mac, seq, SocketSecureKey.Model.MODEL_YELLOW_LIGHT);
    }

    public static ResponseData getQueryLightColor(String mac, int seq, int model) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_LIGHT_COLOR);
        byte[] params = new byte[2];
        params[0] = (byte) seq;
        params[1] = (byte) model;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    // 彩灯
    public static ResponseData getSetColorLamp(String mac, int seq, int r, int g, int b) {
        return getSetLightColor(mac, seq, r, g, b, SocketSecureKey.Model.MODEL_COLOR_LAMP);
    }

    // 彩灯
    public static ResponseData getSeYellowLight(String mac, int seq, int r, int g, int b) {
        return getSetLightColor(mac, seq, r, g, b, SocketSecureKey.Model.MODEL_YELLOW_LIGHT);
    }

    public static ResponseData getSetLightColor(String mac, int seq, int r, int g, int b, int model) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_LIGHT_COLOR);
        byte[] params = new byte[5];
        params[0] = (byte) seq;
        params[1] = (byte) r;
        params[2] = (byte) g;
        params[3] = (byte) b;
        params[4] = (byte) model;
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getSwitchNightLight(String mac, boolean status) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_RELAY_SWITCH);
        final byte[] params = new byte[2];
        params[0] = SocketSecureKey.Model.MODEL_NIGHT_LIGHT;
        params[1] = SocketSecureKey.Util.on(status);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getSwitchFlash(String mac, boolean status) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_RELAY_SWITCH);
        final byte[] params = new byte[2];
        params[0] = SocketSecureKey.Model.MODEL_FLASHLIGHT;
        params[1] = SocketSecureKey.Util.on(status);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getSwitchBackLight(String mac, boolean status) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_RELAY_SWITCH);
        final byte[] params = new byte[2];
        params[0] = SocketSecureKey.Model.MODEL_BACKLIGHT;
        params[1] = SocketSecureKey.Util.on(status);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getSwitchUSB(String mac, boolean status) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_SET_RELAY_SWITCH);
        final byte[] params = new byte[2];
        params[0] = SocketSecureKey.Model.MODEL_USB;
        params[1] = SocketSecureKey.Util.on(status);
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryNightLight(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_RELAY_STATUS);
        mSecureDataPack.setParams(new byte[]{SocketSecureKey.Model.MODEL_NIGHT_LIGHT});
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getQueryFlashState(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_RELAY_STATUS);
        mSecureDataPack.setParams(new byte[]{SocketSecureKey.Model.MODEL_FLASHLIGHT});
        return newResponseDataRecord(mac, mSecureDataPack);
    }


    public static ResponseData getQueryUSBState(String mac) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_CONTROLLER);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_QUERY_RELAY_STATUS);
        mSecureDataPack.setParams(new byte[]{SocketSecureKey.Model.MODEL_USB});
        return newResponseDataRecord(mac, mSecureDataPack);
    }

    public static ResponseData getResponseTestData(String mac, String content) {
        SocketDataArray mSecureDataPack = getInstance().produceSocketDataArray(mac);
        mSecureDataPack.setType(SocketSecureKey.Type.TYPE_ERROR);
        mSecureDataPack.setCmd(SocketSecureKey.Cmd.CMD_TEST);
        byte[] params;
        if (content != null) {
            params = content.getBytes();
        } else {
            params = new byte[1];
            params[0] = 1;
        }
        mSecureDataPack.setParams(params);
        return newResponseDataRecord(mac, mSecureDataPack);
    }


}
