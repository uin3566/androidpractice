package com.xuf.www.experiment.ipc;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2015/11/16.
 */
public class BookManagerImpl extends IBookManager.Stub {

    private static final String TAG = "BookManagerImpl";

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();

    private boolean mIsServiceConnected = true;

    public void setServiceConnected(boolean isServiceConnected){
        mIsServiceConnected = isServiceConnected;
    }

    public BookManagerImpl(){
        hasNewBook();
    }

    public void hasNewBook(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsServiceConnected) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    int bookId = mBookList.size() + 1;
                    Book book = new Book(bookId, "new book#" + bookId);
                    try {
                        _onNewBookArrived(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    private void _onNewBookArrived(Book book) throws RemoteException{
        mBookList.add(book);
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++){
            IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
            listener.onNewBookArrived(book);
        }
        mListenerList.finishBroadcast();
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        return mBookList;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        mBookList.add(book);
    }

    @Override
    public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
        mListenerList.register(listener);
    }

    @Override
    public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
        mListenerList.unregister(listener);
    }
}
