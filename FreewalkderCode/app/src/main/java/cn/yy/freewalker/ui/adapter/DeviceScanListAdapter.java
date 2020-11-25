package cn.yy.freewalker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yy.freewalker.R;
import cn.yy.freewalker.entity.model.DeviceBleScan;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/7 10:40
 */
public class DeviceScanListAdapter extends BaseAdapter {

    private List<DeviceBleScan> listData;

    public DeviceScanListAdapter(List<DeviceBleScan> data) {
        this.listData = data;
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

        DeviceBleScan data = listData.get(position);
        Holder holder = null;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.item_list_device_scan, null);
            holder = createHolder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.UpdateUI(parent.getContext(), data, position);
        return view;
    }


    public Holder createHolder(View view) {
        return new Holder(view);
    }

    class Holder {
        private TextView tvDeviceName;

        public Holder(View view) {
            tvDeviceName = (TextView) view.findViewById(R.id.tv_device_name);
        }

        public void UpdateUI(Context context, DeviceBleScan data, int position) {
            tvDeviceName.setText(data.device.getName() + "_" + data.device.getAddress().substring(5).replace(":",""));
        }
    }

}
