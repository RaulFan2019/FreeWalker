package cn.yy.freewalker.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yy.freewalker.R;
import cn.yy.freewalker.config.UrlConfig;
import cn.yy.freewalker.entity.model.PhotoSelectBean;
import cn.yy.freewalker.utils.ImageU;
import cn.yy.freewalker.utils.YLog;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/16 22:46
 */
public class PhotoSelectAdapter extends BaseAdapter {

    Activity context;
    LayoutInflater inflator;
    onItemClickChangedListener mListener;

    boolean isSelectMode;

    private List<PhotoSelectBean> listPhoto;


    public interface onItemClickChangedListener {
        void onItemClick(int index);

        void onItemLongClick(int index);
    }

    public void setListener(onItemClickChangedListener listener) {
        this.mListener = listener;
    }

    public PhotoSelectAdapter(Activity context, List<PhotoSelectBean> listPhoto, boolean isSelectMode) {
        this.context = context;
        this.inflator = LayoutInflater.from(context);
        this.listPhoto = listPhoto;
        this.isSelectMode = isSelectMode;
    }

    @Override
    public int getCount() {
        if (isSelectMode) {
            return listPhoto.size();
        } else {
            return listPhoto.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return listPhoto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        PhotoSelectBean data = null;
        if (position != listPhoto.size()) {
            data = listPhoto.get(position);
        }

        ViewHolder holder = null;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.item_grid_photo, null);
            holder = createHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.UpdateUI(parent.getContext(), data, position);
        return view;

    }


    public ViewHolder createHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder {
        public FrameLayout flBase;
        public ImageView photoView;
        public View vSelect;

        public ViewHolder(View view) {
            photoView = view.findViewById(R.id.img_photo);
            vSelect = view.findViewById(R.id.v_select);
            flBase = view.findViewById(R.id.fl_base);
        }

        public void UpdateUI(Context context, PhotoSelectBean data, int position) {
            if (position == listPhoto.size()) {
                photoView.setImageResource(R.drawable.icon_device_add);
                photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                flBase.setBackgroundResource(R.drawable.bg_me_photo_empty);
                vSelect.setVisibility(View.GONE);
            } else {
                flBase.setBackgroundResource(R.color.bg_base_gray);
                photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageU.loadPhoto(UrlConfig.IMAGE_HOST + data.photo.imgUrl, photoView);
                if (isSelectMode && data.isSelected) {
                    vSelect.setVisibility(View.VISIBLE);
                    photoView.setAlpha(0.5f);
                } else {
                    vSelect.setVisibility(View.GONE);
                    photoView.setAlpha(1f);
                }
            }

            flBase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YLog.e("PhotoSelectAdapter", "onClick  position:" + position + ",isSelectMode:" + isSelectMode);
                    if (isSelectMode) {
                        if (mListener != null) {
                            mListener.onItemClick(position);
                        }
                    } else {
                        if (position == listPhoto.size() && mListener != null) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

            flBase.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!isSelectMode) {
                        if (mListener != null) {
                            mListener.onItemLongClick(position);
                        }
                    }
                    return false;
                }
            });
        }

    }
}
