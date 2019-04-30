package cn.com.startai.qx.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.blankj.utilcode.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.startai.qx.R;
import cn.com.startai.qxsdk.db.bean.DeviceBean;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<DeviceBean> list;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * 设置点击事件
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置长按点击事件
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public MyRecyclerViewAdapter(List<DeviceBean> list) {
        this.list = list;
    }


    public DeviceBean getItem(int position) {
        return list.get(position);
    }


    public void setList(List<DeviceBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceBean contentBean = this.list.get(position);

        holder.tv1.setText(contentBean.getSn());
        holder.tv2.setText("Name:" + contentBean.getName());
        holder.tv3.setText(contentBean.getMac());
        holder.tv4.setText("局域网绑定:" + contentBean.isLanBind());
        holder.tv5.setText("广域网绑定:" + contentBean.isWanBind());
        holder.tv6.setText(contentBean.getIp() + ":" + contentBean.getPort());
        holder.tv7.setText(TimeUtils.date2String(new Date(contentBean.getUpdateTime()), new SimpleDateFormat("MM-dd HH:mm")));
        holder.tv8.setText("广域网在线:" + contentBean.isWanState());
        holder.tv9.setText("局域网在线:" + contentBean.isLanState() );
        holder.tv10.setText("RSSI" + ":" + contentBean.getRssi()+ "  " + contentBean.getSsid());
        holder.tv11.setText("激活状态:" + contentBean.isActivateState());
        holder.tv12.setText("Alias:" + contentBean.getRemark());
        holder.tv13.setText("Ver:" + contentBean.getMainVersion() + "." + contentBean.getSubVersion());

        //局域网绑定 并局域网在线 或 广域网绑定 并局域网在线  则可通信
        int imageRes = 0;
        if (contentBean.isCanCommunicate()) {

            imageRes = R.drawable.ic_personal_video_black_48dp;
        } else {

            imageRes = R.drawable.ic_personal_video_black_gray_48dp;
        }

        holder.imageView.setImageResource(imageRes);


        holder.itemView.setOnLongClickListener(new MyOnLongClickListener(position));
        holder.itemView.setOnClickListener(new MyOnClickListener(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tv5;
        TextView tv6;
        TextView tv7;
        TextView tv8;
        TextView tv9;
        TextView tv10;
        TextView tv11;
        TextView tv12;
        TextView tv13;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item_devicelist);
            tv1 = itemView.findViewById(R.id.tv_sn);
            tv2 = itemView.findViewById(R.id.tv_item_devicelist_2);
            tv3 = itemView.findViewById(R.id.tv_item_devicelist_3);
            tv4 = itemView.findViewById(R.id.tv_item_devicelist_4);
            tv5 = itemView.findViewById(R.id.tv_item_devicelist_5);
            tv6 = itemView.findViewById(R.id.tv_item_devicelist_6);
            tv7 = itemView.findViewById(R.id.tv_item_devicelist_7);
            tv8 = itemView.findViewById(R.id.tv_item_devicelist_8);
            tv9 = itemView.findViewById(R.id.tv_item_devicelist_9);
            tv10 = itemView.findViewById(R.id.tv_item_devicelist_10);
            tv11 = itemView.findViewById(R.id.tv_item_devicelist_11);
            tv12 = itemView.findViewById(R.id.tv_item_devicelist_12);
            tv13 = itemView.findViewById(R.id.tv_item_devicelist_13);

        }
    }


    private class MyOnLongClickListener implements View.OnLongClickListener {
        private int position;

        public MyOnLongClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v) {
            onItemLongClickListener.onItemLongClick(v, position);
            return true;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, position);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}