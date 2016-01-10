package com.xuf.www.experiment.ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenov0 on 2015/11/4.
 */
public class Book implements Parcelable{

    public int mBookId;
    public String mBookName;

    public Book(){

    }

    public Book(int bookId, String bookName) {
        this.mBookId = bookId;
        this.mBookName = bookName;
    }

    private Book(Parcel in){
        mBookId = in.readInt();
        mBookName = in.readString();
    }

    @Override
    public String toString() {
        String book = "[bookId:" + mBookId + "," + "bookName:" + mBookName + "]";
        return book;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mBookId);
        dest.writeString(mBookName);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

}
