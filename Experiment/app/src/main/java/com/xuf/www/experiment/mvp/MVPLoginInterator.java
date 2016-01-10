package com.xuf.www.experiment.mvp;

/**
 * Created by lenov0 on 2015/12/13.
 */
public class MVPLoginInterator {

    private LoginResult mResult;

    public MVPLoginInterator(LoginResult result){
        mResult = result;
    }

    public void login(String userName, String password){
        if (userName.equals("xuf") && password.equals("123456")){
            mResult.onLoginSuccess();
        } else if (userName.equals("")){
            mResult.onError("用户名不能为空");
        } else if (password.equals("")){
            mResult.onError("请输入密码");
        } else {
            mResult.onError("用户名或密码错误");
        }
    }

}
