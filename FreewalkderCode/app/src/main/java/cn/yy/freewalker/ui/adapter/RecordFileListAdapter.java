package cn.yy.freewalker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.widget.listview.SlideViewRecordFile;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/6 11:36
 */
public class RecordFileListAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private List<String> listData;
    private Context mContext;
    private onListItemDeleteListener mListener;


    public interface onListItemDeleteListener {
        void onDelete(int index);
    }

    public RecordFileListAdapter(Context context, List<String> data) {
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

        SlideViewRecordFile slideView = (SlideViewRecordFile) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.item_list_record_file, null);

            slideView = new SlideViewRecordFile(mContext);
            slideView.setContentView(itemView);
            holder = new ViewHolder(slideView);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }

        String fileName = listData.get(position);
        final int pos = position;
        slideView.shrink();

        holder.tvFileName.setText(mContext.getString(R.string.chat_tx_record_file_name) + " " + fileName);


        holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDelete(pos);
                }
            }
        });

        return slideView;
    }

    private static class ViewHolder {
        public TextView tvFileName;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            tvFileName = view.findViewById(R.id.tv_file_name);
            deleteHolder = view.findViewById(R.id.ll_holder);
        }
    }

}
