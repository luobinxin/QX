package cn.com.startai.qx.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.com.startai.qx.R;
import cn.com.startai.qxsdk.db.bean.DeviceBean;


public class UpdateAdapter extends BaseQuickAdapter<DeviceBean, BaseViewHolder> {


    public UpdateAdapter(int layoutResId, @Nullable List<DeviceBean> data) {
        super(layoutResId, data);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, DeviceBean item) {

        String productStr = "";
        int product = item.getProduct();
        int customer = item.getCustomer();
        if (customer == 0 && product == 2) {
            //mimiwifi
            productStr = "Mini Wifi";
        } else if (customer == 8 && product == 8) {
            //MUSIK
            productStr = "MUSIK";
        } else if (customer == 0 && product == 6) {
            //英国插座
            productStr = "En Plug";
        } else {
            //其他
            productStr = "other";
        }

        helper.setText(R.id.tv_ver, item.getMainVersion() + "." + item.getSubVersion())
                .setText(R.id.tv_sn, item.getName())
                .setText(R.id.tv_ip, item.getIp())
                .setText(R.id.tv_product, productStr)
                .setText(R.id.tv_mac, item.getMac());

    }


}
