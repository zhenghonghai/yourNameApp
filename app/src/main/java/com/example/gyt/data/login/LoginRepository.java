package com.example.gyt.data.login;

import com.example.gyt.data.Result;
import com.example.gyt.data.model.User;

/**
 * 所有数据的获取在此类中获取（本地或网络）
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private LoginDataSource dataSource;
    private User user = null;


    public LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    // 是否登录成功
    public boolean isLoggedIn() {
        return user != null;
    }
    // 退出登录
    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setUser(User user){
        this.user = user;
    }

    public Result<User> login(String userName, String password) {
        Result<User> result = dataSource.login(userName, password);
        if (result instanceof Result.Success) {
            setUser(((Result.Success<User>) result).getData());
        }
        return result;
    }

}
