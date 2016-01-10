package com.xuf.www.experiment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xuf.www.experiment.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private List<T> mDataList;
    private Context mContext;
    private int mLayoutId;

    public CommonAdapter(Context context, int layoutId, List<T> dataList){
        mContext = context;
        mLayoutId = layoutId;
        mDataList = dataList;
    }

    public void setDataList(List<T> dataList){
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDataList == null){
            return 0;
        }
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        if (mDataList == null || mDataList.isEmpty()){
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommonViewHolder viewHolder = CommonViewHolder.get(mContext, convertView, mLayoutId, parent);
        fillView(viewHolder, position);
        return viewHolder.getConvertView();
    }

    public abstract void fillView(CommonViewHolder holder, int position);
}
