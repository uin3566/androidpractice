// IBookManager.aidl
package com.xuf.www.experiment.ipc;

import com.xuf.www.experiment.ipc.Book;
import com.xuf.www.experiment.ipc.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
