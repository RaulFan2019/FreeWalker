package cn.yy.freewalker.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
//        holder.llChannel.setWeightSum(1.0f);

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

//        holder.llChannel.setWeightSum(1.0f);
        holder.tvChannel.setText((position + 1) + "");
        if (selectChannel == (position + 1)) {
            holder.llChannel.setBackgroundColor(mContext.getResources().getColor(R.color.accent));
            holder.tvChannel.setTextColor(Color.WHITE);
            holder.vSlot.setBackgroundResource(R.drawable.bg_channel_encryption_selected);
        } else {
            holder.llChannel.setBackgroundColor(Color.WHITE);
            holder.tvChannel.setTextColor(mContext.getResources().getColor(R.color.tv_secondly));
            holder.vSlot.setBackgroundResource(R.drawable.bg_channel_encryption_normal);
        }

        switch (position){
            case 0:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_1));
                break;
            case 1:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_2));
                break;
            case 2:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_3));
                break;
            case 3:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_4));
                break;
            case 4:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_5));
                break;
            case 5:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_6));
                break;
            case 6:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_7));
                break;
            case 7:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_8));
                break;
            case 8:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_9));
                break;
            case 9:
                holder.tvChannelInfo.setVisibility(View.VISIBLE);
                holder.tvChannelInfo.setText(mContext.getString(R.string.device_channel_10));
                break;
            default:
                holder.tvChannelInfo.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flChannel;
        TextView tvChannel;
        TextView tvChannelInfo;
        LinearLayout llChannel;
        View vSlot;

        public ViewHolder(View view) {
            super(view);
            flChannel = view.findViewById(R.id.fl_channel);
            tvChannel = view.findViewById(R.id.tv_channel);
            tvChannelInfo = view.findViewById(R.id.tv_channel_info);
            vSlot = view.findViewById(R.id.v_slot);
            llChannel = view.findViewById(R.id.ll_channel);
        }

    }
}
