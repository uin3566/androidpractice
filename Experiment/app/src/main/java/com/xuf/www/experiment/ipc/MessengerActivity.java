package com.xuf.www.experiment.ipc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xuf.www.experiment.GlobalConfig;
import com.xuf.www.experiment.R;
import com.xuf.www.experiment.adapter.MessageListAdapter;
import com.xuf.www.experiment.bean.ChatMessage;
import com.xuf.www.experiment.util.DiskLruCacheHelper;
import com.xuf.www.experiment.util.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by lenov0 on 2015/11/3.
 */
public class MessengerActivity extends Activity {

    private static final String TAG = "MessengerActivity";
    private static final String MSG_KEY = "MessageList";

    private Messenger mClient;
    private Messenger mService;

    private ListView mMsgListView;
    private MessageListAdapter mAdapter;
    private ArrayList<ChatMessage> mMessageList;

    private DiskLruCacheHelper mCacheHelper;

    private static class MessengerHandler extends Handler{
        private static WeakReference<MessengerActivity> mReference;

        public MessengerHandler(MessengerActivity reference){
            mReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalConfig.MSG_FROM_SERVER:
                    String message = msg.getData().getString("reply");
                    Log.d(TAG, "receive reply from Server:" + message);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setComeMsg(true);
                    chatMessage.setMessageContent(message);

                    mReference.get().mMessageList.add(chatMessage);
                    mReference.get().mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        mClient = new Messenger(new MessengerHandler(this));

        _initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCacheHelper.put(MSG_KEY, mMessageList);
        mCacheHelper.close();
        mCacheHelper = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCacheHelper = new DiskLruCacheHelper(this, "MESSAGE_LIST");
        mMessageList = (ArrayList<ChatMessage>)mCacheHelper.getObject(MSG_KEY);
        if (mMessageList == null){
            mMessageList = new ArrayList<>();
        }
        mAdapter.setData(mMessageList);
        mMsgListView.setSelection(mAdapter.getCount() - 1);
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    private void _initView(){
        mMsgListView = (ListView)findViewById(R.id.lv_message_list);
        mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        mAdapter = new MessageListAdapter(this);
        mMsgListView.setAdapter(mAdapter);

        final EditText msgEditText = (EditText)findViewById(R.id.et_message);
        TextView sendButton = (TextView)findViewById(R.id.btn_send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgEditText.getText().toString();
                if (msg.equals("")) {
                    ToastUtil.showShort(MessengerActivity.this, "输入内容不能为空");
                    return;
                }
                if (mService == null) {
                    ToastUtil.showShort(MessengerActivity.this, "尚未连接到服务器");
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setComeMsg(false);
                chatMessage.setMessageContent(msg);
                mMessageList.add(chatMessage);
                mAdapter.notifyDataSetChanged();

                Message message = Message.obtain(null, GlobalConfig.MSG_FROM_CLIENT);
                Bundle data = new Bundle();
                data.putString("msg", msg);
                message.setData(data);
                message.replyTo = mClient;
                try {
                    mService.send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                msgEditText.setText("");
            }
        });
    }
}
