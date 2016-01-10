package com.xuf.www.experiment.ipc;

import android.os.RemoteException;

/**
 * Created by lenov0 on 2015/11/15.
 */
public class ComputeAddImpl extends IComputeAdd.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
