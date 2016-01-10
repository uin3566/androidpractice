// IOnNewBookArrivedListener.aidl
package com.xuf.www.experiment.ipc;

// Declare any non-default types here with import statements

import com.xuf.www.experiment.ipc.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
