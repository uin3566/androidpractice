package com.xuf.www.experiment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.util.ToastUtil;

/**
 * Created by Xuf on 2016/1/10.
 */
public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.RecyclerHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] mDatas;

    public SimpleRecyclerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = mContext.getResources().getStringArray(R.array.simple_recycler_items);
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = mLayoutInflater.inflate(R.layout.item_recycler_text, viewGroup, false);
        return new RecyclerHolder(mContext, itemView);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.length;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder recyclerHolder, int i) {
        recyclerHolder.textView.setText(mDatas[i]);
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public RecyclerHolder(final Context context, View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.tv_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.showShort(context, "item position:" + getPosition());
                }
            });
        }
    }
}
