package com.xuf.www.experiment.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.xuf.www.experiment.GlobalConfig;

/**
 * Created by lenov0 on 2015/11/3.
 */
public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private final Messenger mService = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalConfig.MSG_FROM_CLIENT:
                    Log.d(TAG, "receive msg from Client:" + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message message = Message.obtain(null, GlobalConfig.MSG_FROM_SERVER);
                    Bundle data = new Bundle();
                    data.putString("reply", "您好，我现在暂时不在，请待会再联系!");
                    message.setData(data);
                    try {
                        client.send(message);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mService.getBinder();
    }
}
