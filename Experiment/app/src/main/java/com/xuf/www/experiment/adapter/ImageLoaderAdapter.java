package com.xuf.www.experiment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.util.ImageLoader;
import com.xuf.www.experiment.widget.SquareImageView;

import java.util.List;

/**
 * Created by Administrator on 2015/9/24.
 */
public class ImageLoaderAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mImagePaths;
    private ImageLoader mImageLoader;

    public ImageLoaderAdapter(Context context, List<String> imagePaths){
        mContext = context;
        mImagePaths = imagePaths;
        mImageLoader = ImageLoader.getInstance(1, ImageLoader.Type.TYPE_LIFO);
    }

    @Override
    public int getCount() {
        return mImagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImagePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder)convertView.getTag();
        String imgPath = (String)getItem(position);
        holder.mImageView.setImageResource(R.mipmap.img_default);
        mImageLoader.loadImage(imgPath, holder.mImageView);

        return convertView;
    }

    private final class ViewHolder{
        public ViewHolder(View root){
            mImageView = (SquareImageView)root.findViewById(R.id.iv_image);
        }
        private SquareImageView mImageView;
    }
}
