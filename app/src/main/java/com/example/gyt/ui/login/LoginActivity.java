package com.example.gyt.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gyt.MainActivity;
import com.example.gyt.R;
import com.example.gyt.data.login.LoginDataSource;
import com.example.gyt.data.model.User;


/**
 * 此类写页面UI
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mUserName,mPassword;
    private Button mLogin;
    private ProgressBar mLoading;
    private LoginViewModel loginViewModel;
    private MyHandler myHandler;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(this)).get(LoginViewModel.class);
        findViewById();
        constraintLayout.getBackground().setAlpha(150);
        setListeneries();
        myHandler = new MyHandler();
        // 对登录校验进行观察
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                // 设置登录按钮是否可以点击
                mLogin.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUserNameError() != null) {
                    mUserName.setError(getString(loginFormState.getUserNameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    mPassword.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginViewModel = null;
//        Log.d("LoginActivity","什么时候执行销毁");
    }

    // 登录失败的操作
    private void showLoginFailed(@StringRes Integer errorString) {
        mLoading.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),errorString,Toast.LENGTH_SHORT).show();
    }
    // 登录成功的操作
    private void updateUiWithUser(User user) {
        mLoading.setVisibility(View.GONE);
        String welcome = "Welcome ! " + user.getAccount();
        Toast.makeText(getApplicationContext(),welcome,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        setResult(Activity.RESULT_OK);
        startActivity(intent);
//        finish();
    }

    // 控件初始化
    void findViewById() {
        constraintLayout = findViewById(R.id.container);
        mUserName = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        mLoading = findViewById(R.id.loading);

    }
    // 监听事件
    void setListeneries () {
        // 对输入框进行监听
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 判断用户和密码输入是否为空
                loginViewModel.isValid(mUserName.getText().toString(), mPassword.getText().toString());
            }
        };
        //      监听是否为空
        mUserName.addTextChangedListener(afterTextChangedListener);
        mPassword.addTextChangedListener(afterTextChangedListener);
        //      监听密码长度需大于6位
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //  显示进度条
                    mLoading.setVisibility(View.VISIBLE);
                    mUserName.clearFocus();
                    mPassword.clearFocus();
                    // 校验登录账号
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loginViewModel.login(mUserName.getText().toString(), mPassword.getText().toString());
                            myHandler.sendEmptyMessage(0);
                        }
                    }).start();
                }
                return false;
            }
        });
        // 对登录按钮进行监听
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  显示进度条
                mLoading.setVisibility(View.VISIBLE);
                mUserName.clearFocus();
                mPassword.clearFocus();
                // 校验登录账号
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loginViewModel.login(mUserName.getText().toString(), mPassword.getText().toString());
                        myHandler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                //执行业务逻辑
            if (msg.what == 0) {
                // 跟新UI
                // 对登录结果进行观察
                loginViewModel.getLoginResult().observe(LoginActivity.this, new Observer<LoginResult>() {
                    @Override
                    public void onChanged(LoginResult loginResult) {
                        if (loginResult == null) {
                            return;
                        }
                        mLoading.setVisibility(View.GONE);
                        if (loginResult.getError() != null) {
                            // 登录失败的操作
                            showLoginFailed(loginResult.getError());
                        }
                        if (loginResult.getSuccess() != null) {
                            // 登录成功的操作
                            updateUiWithUser(loginResult.getSuccess());
                        }
                    }
                });
            }
        }
    }

}

