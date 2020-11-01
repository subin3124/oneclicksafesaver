package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Config extends AppCompatActivity {
DB dbh;
SQLiteDatabase db;
Cursor cursor;
    Button insert;
    Button message;
    EditText Name;
    EditText mils;
    EditText Phone;
    CheckBox chk_korean;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        dbh = new DB(this,null,null,1);
        db = dbh.getWritableDatabase();
        insert = findViewById(R.id.insert);
        message = findViewById(R.id.msg);
        mils = findViewById(R.id.mils);
       Name = findViewById(R.id.Name);
        Phone = findViewById(R.id.Phone);
        chk_korean= findViewById(R.id.dknow);
        cursor = db.rawQuery("SELECT * FROM User",null);
        cursor.moveToFirst();
        if (cursor.getInt(cursor.getColumnIndex("최초실행")) == 0){
            Name.setText(cursor.getString(cursor.getColumnIndex("이름")));
            Phone.setText(cursor.getString(cursor.getColumnIndex("비상연락처")));
            mils.setText(cursor.getString(cursor.getColumnIndex("대기밀리초")));
        }
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.execSQL("UPDATE User SET 이름='"+Name.getText()+"', 비상연락처='"+Phone.getText()+"', 최초실행=0, 대기밀리초="+mils.getText()+";");
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),msgconfig.class));
            }
        });
    }
}