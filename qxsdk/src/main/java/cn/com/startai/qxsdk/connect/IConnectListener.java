package cn.com.startai.qxsdk.connect;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IConnectListener {

    void onInitResult(boolean result);

    void onReceiveData(BaseData baseData);

    void onRelease();

}
