// IBinderPool.aidl
package com.xuf.www.experiment.ipc;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
