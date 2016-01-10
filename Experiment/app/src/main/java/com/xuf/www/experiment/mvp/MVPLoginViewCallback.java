package com.xuf.www.experiment.mvp;

/**
 * Created by lenov0 on 2015/12/13.
 */
public interface MVPLoginViewCallback {

    public String getUserName();

    public String getPassWord();

    public void showToast(String toast);
}
