package com.xuf.www.experiment.ipc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.xuf.www.experiment.R;

import java.util.List;

/**
 * Created by lenov0 on 2015/11/15.
 */
public class BinderPoolActivity extends Activity {

    private static final String TAG = "BinderPoolActivity";

    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private MyHandler mMyHandler = new MyHandler();
    private NewBookArrivedListener mListener = new NewBookArrivedListener();
    private IBookManager mBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        new Thread(new Runnable() {
            @Override
            public void run() {
                execute();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBookManager != null && mBookManager.asBinder().isBinderAlive()) {
            try {
                mBookManager.unregisterListener(mListener);
                Log.d(TAG, "unregisterListener" + mListener);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    private void execute(){
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder addBinder = binderPool.queryBinder(BinderPool.BINDER_ADD);
        IComputeAdd computeAdd = ComputeAddImpl.asInterface(addBinder);
        if (computeAdd != null) {
            try {
                Log.d(TAG, "IComputeAdd:");
                int result = computeAdd.add(3, 4);
                Log.d(TAG, "add 3, 4, result:" + result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        IBinder minusBinder = binderPool.queryBinder(BinderPool.BINDER_MINUS);
        IComputeMinus computeMinus = ComputeMinusImpl.asInterface(minusBinder);
        if (computeMinus != null) {
            try {
                Log.d(TAG, "IComputeMinus:");
                int result = computeMinus.minus(3, 4);
                Log.d(TAG, "minus 3, 4, result:" + result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        IBinder bookManagerBinder = binderPool.queryBinder(BinderPool.BINDER_BOOK_MANAGER);
        mBookManager = BookManagerImpl.asInterface(bookManagerBinder);
        if (mBookManager != null) {
            try {
                Log.d(TAG, "IBookManager:");
                Book newBook = new Book(1, "android art");
                mBookManager.addBook(newBook);
                List<Book> list = mBookManager.getBookList();
                Log.d(TAG, "query book list:");
                for (Book book : list) {
                    Log.d(TAG, "book name:" + book.mBookName);
                }
                Log.d(TAG, "registerListener" + mListener);
                mBookManager.registerListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private class NewBookArrivedListener extends IOnNewBookArrivedListener.Stub{
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Message message = new Message();
            message.what = MESSAGE_NEW_BOOK_ARRIVED;
            message.obj = newBook;
            mMyHandler.sendMessage(message);
        }
    }

    private static class MyHandler extends Handler {
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
}
