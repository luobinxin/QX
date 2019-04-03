package cn.com.startai.qxsdk.global;

import cn.com.startai.qxsdk.busi.entity.BrokerHost;

/**
 * Created by Robin on 2019/3/29.
 * 419109715@qq.com 彬影
 */
public class AreaNodesManager {


    private static final AreaNodesManager ourInstance = new AreaNodesManager();

    public static AreaNodesManager getInstance() {
        return ourInstance;
    }

    private AreaNodesManager() {
    }


    private BrokerHost.Resp.ContentBean cacheAreaNodes;


    public BrokerHost.Resp.ContentBean getCacheAreaNodes() {

        return cacheAreaNodes;
    }

    public void setCacheAreaNodes(BrokerHost.Resp.ContentBean cacheAreaNodes) {
        this.cacheAreaNodes = cacheAreaNodes;
    }

    public BrokerHost.Resp.ContentBean getSpAreaNodes() {
        return QXSpController.getAllAreaNodeBean();
    }

    public void setSpAreaNodes(BrokerHost.Resp.ContentBean areaNodes) {
        QXSpController.setAreaNodeBeans(areaNodes);
    }



}
