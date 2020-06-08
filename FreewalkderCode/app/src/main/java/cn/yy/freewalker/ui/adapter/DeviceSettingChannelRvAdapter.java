package cn.yy.freewalker.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.yy.freewalker.R;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/8 21:41
 */
public class DeviceSettingChannelRvAdapter extends RecyclerView.Adapter<DeviceSettingChannelRvAdapter.ViewHolder> {

    private static final String TAG = "DeviceSettingChannelRvAdapter";

    private Context mContext;
    private int selectChannel;
    private onChannelClick mListener;


    public interface onChannelClick {
        void onClick(int channel);
    }

    public DeviceSettingChannelRvAdapter(Context context,int selectChannel, onChannelClick listener) {
        this.mContext = context;
        this.selectChannel = selectChannel;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_device_setting_channel, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.llChannel.setWeightSum(1.0f);
        holder.llChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                selectChannel = position + 1;
                mListener.onClick(position + 1);
                notifyDataSetChanged();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.llChannel.setWeightSum(1.0f);
        holder.tvChannel.setText((position + 1) + "");
        if (selectChannel == (position + 1)) {
            holder.tvChannel.setBackgroundColor(mContext.getResources().getColor(R.color.accent));
            holder.tvChannel.setTextColor(Color.WHITE);
        } else {
            holder.tvChannel.setBackgroundColor(Color.WHITE);
            holder.tvChannel.setTextColor(mContext.getResources().getColor(R.color.tv_secondly));
        }
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llChannel;
        TextView tvChannel;

        public ViewHolder(View view) {
            super(view);
            llChannel = view.findViewById(R.id.ll_channel);
            tvChannel = view.findViewById(R.id.tv_channel);
        }

    }
}
