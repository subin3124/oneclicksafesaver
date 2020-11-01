package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class msgconfig extends AppCompatActivity {
DB dbh;
Cursor cursor;
SQLiteDatabase db;
Button okay;
EditText firemsg;
EditText watermsg;
EditText kmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msgconfig);
        dbh = new DB(this,null,null,1);
        db = dbh.getWritableDatabase();
        okay = findViewById(R.id.okay);
        firemsg = findViewById(R.id.firemsg);
        watermsg = findViewById(R.id.watermsg);
        kmsg = findViewById(R.id.kmsg);
        cursor = db.rawQuery("SELECT * FROM User",null);
        cursor.moveToFirst();
        firemsg.setText(cursor.getString(cursor.getColumnIndex("화재문자")));
        watermsg.setText(cursor.getString(cursor.getColumnIndex("온수문자")));
        kmsg.setText(cursor.getString(cursor.getColumnIndex("칼문자")));
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("UPDATE User SET 화재문자='"+firemsg.getText()+"',온수문자='"+watermsg.getText()+"',칼문자='"+kmsg.getText()+"';");
                startActivity(new Intent(getApplicationContext(),Config.class));
            }
        });
    }
}