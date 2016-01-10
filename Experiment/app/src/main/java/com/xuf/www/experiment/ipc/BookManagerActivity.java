package com.xuf.www.experiment.ipc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.xuf.www.experiment.R;

import java.util.List;

/**
 * Created by lenov0 on 2015/11/4.
 */
public class BookManagerActivity extends Activity {

    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private MyHandler mMyHandler = new MyHandler();
    private IBookManager mBookManager;
    private NewBookArrivedListener mListener;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBookManager = IBookManager.Stub.asInterface(service);
            try {
                Book newBook = new Book(3, "android art");
                mBookManager.addBook(newBook);
                List<Book> list = mBookManager.getBookList();
                Log.d(TAG, "query book list:");
                for (Book book : list){
                    Log.d(TAG, "book name:" + book.mBookName);
                }
                mBookManager.registerListener(mListener);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "receive new book:" + ((Book)msg.obj).mBookName);
                    break;
                default:
                    break;
            }
        }
    }

    private class NewBookArrivedListener extends IOnNewBookArrivedListener.Stub {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Message message = mMyHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook);
            message.sendToTarget();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        mListener = new NewBookArrivedListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBookManager != null && mBookManager.asBinder().isBinderAlive()) {
            try {
                Log.d(TAG, "unregister listener:" + mListener);
                mBookManager.unregisterListener(mListener);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
    }
}
