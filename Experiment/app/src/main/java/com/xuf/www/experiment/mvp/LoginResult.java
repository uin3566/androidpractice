package com.xuf.www.experiment.mvp;

/**
 * Created by lenov0 on 2015/12/13.
 */
public interface LoginResult {

    public void onLoginSuccess();

    public void onError(String errorInfo);
}
