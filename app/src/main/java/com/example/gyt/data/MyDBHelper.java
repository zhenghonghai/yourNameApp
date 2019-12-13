package com.example.gyt.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 此功能是创建sqlite数据库
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "gyt.db";
    private static int DATABASE_VERSION = 1;

    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDBHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 将需要创建多张表的语句放这里
        // 创建用户表
        String sql = "create table user" +
                "(" +
                "    id int(32) not null primary key," +
                "    account varchar(255)," +
                "    password varchar(255)," +
                "    icon varchar(255)," +
                "    userName varchar(255)," +
                "    telephone varchar(13)," +
                "    email varchar(255)," +
                "    gender int(2)," +
                "    time timestamp" +
                ")";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
