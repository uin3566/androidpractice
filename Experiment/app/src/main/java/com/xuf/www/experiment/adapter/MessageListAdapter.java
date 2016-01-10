package com.xuf.www.experiment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.bean.ChatMessage;
import com.xuf.www.experiment.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class MessageListAdapter extends BaseAdapter {

    private static final int TYPE_MSG_COME = 0;
    private static final int TYPE_MSG_TO = 1;

    private Context mContext;
    private List<ChatMessage> mMessageList;

    public MessageListAdapter(Context context){
        mContext = context;
    }

    public void setData(List<ChatMessage> messageList){
        mMessageList = messageList;
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        ChatMessage msg = (ChatMessage)getItem(position);
        if (msg.isComeMsg()){
            type = TYPE_MSG_COME;
        } else {
            type = TYPE_MSG_TO;
        }

        return type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public int getCount() {
        if (mMessageList == null){
            return 0;
        }
        return mMessageList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null){
            if (type == TYPE_MSG_COME){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_come_message, parent, false);
            } else{
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_to_message, parent, false);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder = (ViewHolder)convertView.getTag();
        ChatMessage message = (ChatMessage)getItem(position);
        holder.mMessage.setText(message.getMessageContent());

        return convertView;
    }

    private class ViewHolder{
        private ImageView mPortrait;
        private TextView mMessage;

        public ViewHolder(View view)
        {
            mPortrait = (ImageView)view.findViewById(R.id.iv_portrait);
            mMessage = (TextView)view.findViewById(R.id.tv_msg_content);
        }
    }
}
