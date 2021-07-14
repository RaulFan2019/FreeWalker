package cn.yy.freewalker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yy.freewalker.R;
import cn.yy.freewalker.config.DeviceConfig;
import cn.yy.freewalker.entity.adapter.BindDeviceAdapterEntity;
import cn.yy.freewalker.ui.widget.listview.SlideViewUnbindDevice;
import cn.yy.sdk.ble.array.ConnectStates;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 11:36
 */
public class DeviceListAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private List<BindDeviceAdapterEntity> listData;
    private Context mContext;
    private onListItemDeleteListener mListener;


    public interface onListItemDeleteListener {
        void onDelete(int index);
    }

    public DeviceListAdapter(Context context, List<BindDeviceAdapterEntity> data) {
        super();
        this.mContext = context;
        this.listData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setListener(onListItemDeleteListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        SlideViewUnbindDevice slideView = (SlideViewUnbindDevice) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.item_list_device, null);

            slideView = new SlideViewUnbindDevice(mContext);
            slideView.setContentView(itemView);
            holder = new ViewHolder(slideView);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }

        BindDeviceAdapterEntity item = listData.get(position);
        final int pos = position;
        slideView.shrink();

        holder.tvDeviceName.setText("FreeWLK_" + item.deviceDbEntity.deviceMac.substring(5).replace(":",""));

        if (item.deviceDbEntity.deviceType == DeviceConfig.Type.BLACK) {
            holder.vDevice.setBackgroundResource(R.drawable.icon_device_black);
        } else {
            holder.vDevice.setBackgroundResource(R.drawable.icon_device_red);
        }

        switch (item.status) {
            case ConnectStates.CONNECTING:
                holder.tvDeviceStatus.setText(mContext.getString(R.string.device_status_connecting));
                holder.tvDeviceStatus.setTextColor(mContext.getResources().getColor(R.color.tv_accent));
                holder.tvSettings.setVisibility(View.GONE);
                break;
            case ConnectStates.DISCONNECT:
            case ConnectStates.CONNECT_FAIL:
                holder.tvDeviceStatus.setText(mContext.getString(R.string.device_status_disconnect));
                holder.tvDeviceStatus.setTextColor(mContext.getResources().getColor(R.color.tv_thirdly));
                holder.tvSettings.setVisibility(View.GONE);
                break;
            case ConnectStates.WORKED:
                holder.tvDeviceStatus.setText(mContext.getString(R.string.device_status_worked));
                holder.tvDeviceStatus.setTextColor(mContext.getResources().getColor(R.color.tv_accent));
                holder.tvSettings.setVisibility(View.VISIBLE);
                break;
        }

        holder.deleteHolder.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onDelete(pos);
            }
        });

        return slideView;
    }

    private static class ViewHolder {
        public TextView tvDeviceName;
        public TextView tvDeviceStatus;
        public TextView tvSettings;
        public View vDevice;
        public View vMore;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            tvDeviceName = view.findViewById(R.id.tv_device_name);
            tvDeviceStatus = view.findViewById(R.id.tv_device_status);
            tvSettings = view.findViewById(R.id.tv_settings);
            vDevice = view.findViewById(R.id.v_device);
            deleteHolder = view.findViewById(R.id.ll_holder);
            vMore = view.findViewById(R.id.v_more);
        }
    }

}
