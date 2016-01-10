package com.xuf.www.experiment.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/9/21.
 */
public class CommonViewHolder {

    private final SparseArray<View> mViewSparseArray;
    private View mConvertView;

    private CommonViewHolder(Context context, int layoutId, ViewGroup root){
        mViewSparseArray = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, root, false);
        mConvertView.setTag(this);
    }

    public static CommonViewHolder get(Context context, View convertView, int layoutId, ViewGroup root){
        if (convertView == null){
            return new CommonViewHolder(context, layoutId, root);
        }
        return (CommonViewHolder)convertView.getTag();
    }

    public <T extends View> T getView(int viewId){
        View view = mViewSparseArray.get(viewId);
        if (view == null){
            view = mConvertView.findViewById(viewId);
            mViewSparseArray.put(viewId, view);
        }
        return (T)view;
    }

    public View getConvertView(){
        return mConvertView;
    }

    public void setText(int viewId, String text){
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public void setImageResource(int viewId, int imgId){
        ImageView imageView = getView(viewId);
        imageView.setImageResource(imgId);
    }
}
