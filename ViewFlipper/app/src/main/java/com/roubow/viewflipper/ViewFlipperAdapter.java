package com.roubow.viewflipper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/24.
 */
public class ViewFlipperAdapter extends BaseAdapter {

    private int[] images = {R.mipmap.first, R.mipmap.second, R.mipmap.third};
    private Context mContext;

    public ViewFlipperAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlipperBanner flipperBanner = null;
        if (null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_view_flipper, parent, false);
        }

        flipperBanner = (FlipperBanner)convertView.findViewById(R.id.banner);
        setBanner(flipperBanner);
        return convertView;
    }

    private void setBanner(FlipperBanner flipperBanner){
        ImageView v1 = new ImageView(mContext);
        v1.setScaleType(ImageView.ScaleType.FIT_XY);
        v1.setImageResource(images[0]);

        ImageView v2 = new ImageView(mContext);
        v2.setScaleType(ImageView.ScaleType.FIT_XY);
        v2.setImageResource(images[1]);

        ImageView v3 = new ImageView(mContext);
        v3.setScaleType(ImageView.ScaleType.FIT_XY);
        v3.setImageResource(images[2]);

        List<View> viewList = new ArrayList<>();
        viewList.add(v1);
        viewList.add(v2);
        viewList.add(v3);

        flipperBanner.setBanners(viewList);
    }
}
