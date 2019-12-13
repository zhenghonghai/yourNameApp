package com.example.gyt.ui.login;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gyt.R;
import com.example.gyt.data.Result;
import com.example.gyt.data.login.LoginRepository;
import com.example.gyt.data.model.User;


/**
 * 此类管理界面数据，获取setText、getText等之类的
 */
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    // 获得登录结果
    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
    // 获得校验结果
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }


    // 登录
    public void login(String userName, String password) {
        Result<User> result = loginRepository.login(userName, password);

        if (result instanceof Result.Success) {
            User data = ((Result.Success<User>) result).getData();
            loginResult.postValue(new LoginResult(new User(data)));
        } else {
            loginResult.postValue(new LoginResult(R.string.login_failed));
        }
    }

    // 校验用户和密码
    void isValid(String userName, String password) {
        if(!isUserNameValid(userName)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        }else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // 对用户进行校验
    private boolean isUserNameValid(String userName) {
        return userName != null;
    }
    // 对密码进行校验
    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        if (password.trim().length() < 5) {
            return false;
        }
        return true;
    }
}
