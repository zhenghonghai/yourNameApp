package com.example.gyt.data.login;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.gyt.data.MyDBHelper;
import com.example.gyt.data.Result;
import com.example.gyt.data.model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 所有数据库的操作在此类中进行
 */
public class LoginDataSource {

    private Context context;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;
    private User user;
    private OkHttpClient client = new OkHttpClient();
    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private Gson gson = new Gson();
    private RequestBody requestBody;
    private Request request;
    private Response response = null;

    public LoginDataSource(Context context) {
        this.context = context;
        myDBHelper = new MyDBHelper(context);
        db = myDBHelper.getWritableDatabase();
    }
    // 设置user值，防止内存泄漏
    public void setUser(User user) {
        this.user = user;
    }

    public Result<User> login(String userName, String password) {
        try {
            // 先判断本地是否保存着用户
            select(userName, password);
            if (user != null) {
                User tempUser = user;
                setUser(null);
                return new Result.Success<>(tempUser);
            } else {
                // 否，请求网络验证用户是否存在
                loginRequest(userName, password);
                if (user != null) {
//                    insert(user);
                    User tempUser = user;
                    setUser(null);
                    return new Result.Success<>(tempUser);
                } else {
                    return new Result.Error(new IOException("Error logging in"));
                }
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {

    }

    void insert(User... users) {
        //插入数据SQL语句
        for (User user : users) {
            String sql = "insert into user values(" +
                    user.getId() + "," +
                    user.getAccount() + "," +
                    user.getPassword() + "," +
                    user.getIcon() + "," +
                    user.getUserName() + "," +
                    user.getTelephone() + "," +
                    user.getEmail() + "," +
                    user.getGender() + "," +
                    user.getTime() +
                    ")";
            //执行SQL语句
            db.execSQL(sql);
        }
    }

    /**
     * 查询本地是否存在此用户
     *
     * @param userName
     * @param password
     */
    void select(String userName, String password) {
        String sql = "select * from user where userName = ? and password = ? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{userName, password});
        while (cursor.moveToNext()) {
            setUser((User) cursor);
        }
        cursor.close();
    }

    /**
     * 网络请求用户名是否存在
     *
     * @param userName
     * @param password
     */
    private void loginRequest(String userName, String password) {
        // 构建请求参数
        User paramUser = new User(userName, password);
        // -- 转成JSON数据格式
        String json = gson.toJson(paramUser);
        requestBody = RequestBody.create(json, mediaType);
//        RequestBody formBody = new FormBody.Builder()
//                .add("userName",userName)
//                .add("password", password)
//                .build();
        //发起请求
        request = new Request.Builder()
                .url("http://260h46p490.qicp.vip:33560/login")
                .post(requestBody)
                .build();
        //新建一个线程，用于得到服务器响应的参数
        try {
            //回调
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                try {
                    // 获取返回的数据
                    String responseString = response.body().string();
                    // 解析数据
                    JSONObject object = new JSONObject(responseString);
                    JSONObject resultMessageJson = object.getJSONObject("resultMessage");
                    int code =(int) resultMessageJson.get("code");
                    if ( code == 0) {
                        setUser(gson.fromJson(object.getJSONObject("user").toString(), User.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
