package cn.yy.freewalker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/2 22:52
 */
public class DialogSingleSelectListAdapter extends BaseAdapter {

    private List<String> listData;

    public DialogSingleSelectListAdapter(List<String> data) {
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

        String data = listData.get(position);
        Holder holder = null;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.item_list_dialog_single_select, null);
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
        private TextView tvSelect;

        public Holder(View view) {
            tvSelect = (TextView) view.findViewById(R.id.tv_select);
        }

        public void UpdateUI(Context context, String data, int position) {
            tvSelect.setText(data);
        }
    }

}
