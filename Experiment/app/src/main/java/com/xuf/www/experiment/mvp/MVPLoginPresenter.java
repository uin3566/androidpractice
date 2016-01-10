package com.xuf.www.experiment.mvp;

/**
 * Created by lenov0 on 2015/12/13.
 */
public class MVPLoginPresenter implements LoginResult{

    private MVPLoginViewCallback mViewCallback;
    private MVPLoginInterator mInterator;

    public MVPLoginPresenter(MVPLoginViewCallback viewCallback) {
        mViewCallback = viewCallback;
        mInterator = new MVPLoginInterator(this);
    }

    public void login(){
        mInterator.login(mViewCallback.getUserName(), mViewCallback.getPassWord());
    }

    @Override
    public void onLoginSuccess() {
        mViewCallback.showToast("登陆成功");
    }

    @Override
    public void onError(String errorInfo) {
        mViewCallback.showToast(errorInfo);
    }
}
