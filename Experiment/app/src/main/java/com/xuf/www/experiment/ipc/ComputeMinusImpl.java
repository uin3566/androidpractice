package com.xuf.www.experiment.ipc;

import android.os.RemoteException;

/**
 * Created by lenov0 on 2015/11/15.
 */
public class ComputeMinusImpl extends IComputeMinus.Stub {

    @Override
    public int minus(int a, int b) throws RemoteException {
        return a - b;
    }
}
