package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DB extends SQLiteOpenHelper {
    static final String Name = "user.db";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Name, null, version);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User (이름 TEXT, 비상연락처 TEXT, 최초실행 INT, 화재문자 TEXT, 온수문자 TEXT, 칼문자 TEXT, 대기밀리초 INT);");
        db.execSQL("INSERT INTO User VALUES ('dummy','0000000',1,'불이 났습니다.','뜨거운 물에 아이가 다쳤습니다.','아이가 뾰족한 것에 다쳤습니다.',10000);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
