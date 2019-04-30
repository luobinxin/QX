package cn.com.startai.qx.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.startai.qx.R;
import cn.com.startai.qxsdk.db.bean.DeviceBean;


public class DeviceAdapter extends BaseQuickAdapter<DeviceBean, BaseViewHolder> {


    public DeviceAdapter(int layoutResId, @Nullable List<DeviceBean> data) {
        super(layoutResId, data);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, DeviceBean item) {


        //局域网绑定 并局域网在线 或 广域网绑定 并局域网在线  则可通信
        int imageRes = 0;
        if (item.isCanCommunicate()) {

            imageRes = R.drawable.ic_personal_video_black_48dp;
        } else {

            imageRes = R.drawable.ic_personal_video_black_gray_48dp;
        }

        helper.setImageResource(R.id.iv_item_devicelist, imageRes)
                .setText(R.id.tv_sn, item.getSn())
                .setText(R.id.tv_item_devicelist_2, "Name:" + item.getName())
                .setText(R.id.tv_item_devicelist_3, item.getMac())
                .setText(R.id.tv_item_devicelist_4, "局域网绑定:" + item.isLanBind())
                .setText(R.id.tv_item_devicelist_5, "广域网绑定:" + item.isWanBind())
                .setText(R.id.tv_item_devicelist_6, item.getIp() + ":" + item.getPort())
                .setText(R.id.tv_item_devicelist_7, TimeUtils.date2String(new Date(item.getUpdateTime()), new SimpleDateFormat("MM-dd HH:mm")))
                .setText(R.id.tv_item_devicelist_8, "广域网在线:" + item.isWanState())
                .setText(R.id.tv_item_devicelist_9, "局域网在线:" + item.isLanState())
                .setText(R.id.tv_item_devicelist_10, "RSSI" + ":" + item.getRssi() + "  " + item.getSsid())
                .setText(R.id.tv_item_devicelist_11, "激活状态:" + item.isActivateState())
                .setText(R.id.tv_item_devicelist_12, "Remark:" + item.getRemark())
                .setText(R.id.tv_item_devicelist_13, "Ver:" + item.getMainVersion() + "." + item.getSubVersion())
        ;
    }

}
