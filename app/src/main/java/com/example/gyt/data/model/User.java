package com.example.gyt.data.model;


import java.util.Date;

public class User {
    private int id;
    private String account;
    private String password;
    private String icon;
    private String userName;
    private String telephone;
    private String email;
    private int gender;
    private Date time;

    public User() {
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public User (User user) {
        this.id = user.getId();
        this.account = user.getAccount();
        this.password = user.getPassword();
        this.icon = user.getIcon();
        this.userName = user.getUserName();
        this.telephone = user.getTelephone();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.time = user.getTime();
    }

    public User(int id, String account, String password, String icon, String userName, String telephone, String email, int gender, Date time) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.icon = icon;
        this.userName = userName;
        this.telephone = telephone;
        this.email = email;
        this.gender = gender;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
