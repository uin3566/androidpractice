package com.xuf.www.experiment.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.util.ToastUtil;

/**
 * Created by lenov0 on 2015/12/13.
 */
public class MVPLoginActivity extends Activity implements MVPLoginViewCallback{

    private MVPLoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);

        mPresenter = new MVPLoginPresenter(this);

        Button loginButton = (Button)findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login();
            }
        });
    }

    @Override
    public String getUserName() {
        EditText editText = (EditText)findViewById(R.id.et_user_name);
        return editText.getText().toString();
    }

    @Override
    public String getPassWord() {
        EditText editText = (EditText)findViewById(R.id.et_password);
        return editText.getText().toString();
    }

    @Override
    public void showToast(String toast) {
        ToastUtil.showShort(this, toast);
    }
}
