package com.xuf.www.experiment.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

/**
 * Created by lenov0 on 2015/11/15.
 */
public class BinderPool {

    private static final String TAG = "BinderPool";

    public static final int BINDER_ADD = 0;
    public static final int BINDER_MINUS = 1;
    public static final int BINDER_BOOK_MANAGER = 2;

    private Context mContext;
    private static volatile BinderPool mInstance = null;

    private IBinderPool mBinderPool;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private static IBinder mBookManagerIBinder;

    private BinderPool(Context context){
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    private void connectBinderPoolService(){
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static BinderPool getInstance(Context context){
        if (null == mInstance){
            synchronized (BinderPool.class){
                if (null == mInstance){
                    mInstance = new BinderPool(context);
                }
            }
        }
        return mInstance;
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();
            if (mBookManagerIBinder != null) {
                ((BookManagerImpl) mBookManagerIBinder).setServiceConnected(false);
            }
        }
    };

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e){
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
            if (mBookManagerIBinder != null){
                ((BookManagerImpl)mBookManagerIBinder).setServiceConnected(true);
                ((BookManagerImpl)mBookManagerIBinder).hasNewBook();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try {
            if (mBinderPool != null){
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e){
            e.printStackTrace();
        }

        return binder;
    }

    public static class BinderPoolImpl extends IBinderPool.Stub{
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode){
                case BINDER_ADD:
                    binder = new ComputeAddImpl();
                    break;
                case BINDER_MINUS:
                    binder = new ComputeMinusImpl();
                    break;
                case BINDER_BOOK_MANAGER:
                    if (mBookManagerIBinder == null) {
                        mBookManagerIBinder = new BookManagerImpl();
                    }
                    return mBookManagerIBinder;
                default:
                    break;
            }
            return binder;
        }
    }

}
