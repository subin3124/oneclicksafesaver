package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    Button btn1;
    Button btn2;
    Button configBtn;
    TextToSpeech textToSpeech;
    SmsManager smsManager;
    DB dbh;
    String Phone;
    String firemsg;
    String watermsg;
    String kmsg;
    SQLiteDatabase db;
    Cursor cursor;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.SEND_SMS)){
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.SEND_SMS},0);
        }
        btn1 = findViewById(R.id.Btn1);
        btn2 = findViewById(R.id.Btn2);

        dbh = new DB(this,null,null,1);
        db = dbh.getWritableDatabase();
        configBtn = findViewById(R.id.config);
        textToSpeech = new TextToSpeech(this,this);
        smsManager = SmsManager.getDefault();
        Select();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak("불이 났습니다.",textToSpeech.QUEUE_ADD,null);
                smsManager.sendTextMessage(Phone,"null",firemsg,null,null);
              //  smsManager.sendTextMessage("119",null,"아동이 혼자있는 집에 화재가 났습니다.",null,null);
                startActivity(new Intent(getApplicationContext(),Fire1.class));

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               textToSpeech.speak("뜨거운 물에 닿았습니다.",textToSpeech.QUEUE_FLUSH,null);
               smsManager.sendTextMessage(Phone,null,watermsg,null,null);
               startActivity(new Intent(getApplicationContext(),water.class));
            }
        });

        configBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Config.class));
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == textToSpeech.SUCCESS) {

            int lang = textToSpeech.setLanguage(Locale.KOREAN);
        }

    }

    @Override
    protected void onDestroy() {
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
    private void Select() {
        cursor = db.rawQuery("SELECT * FROM User", null);
        cursor.moveToFirst();
        if (cursor.getInt(cursor.getColumnIndex("최초실행")) == 0){
            Phone = cursor.getString(cursor.getColumnIndex("비상연락처"));
            firemsg = cursor.getString(cursor.getColumnIndex("화재문자"));
            watermsg = cursor.getString(cursor.getColumnIndex("온수문자"));
    }else{

            Toast toast = Toast.makeText(MainActivity.this, "설정이 필요합니다.", Toast.LENGTH_SHORT);
            toast.show();
            startActivity(new Intent(getApplicationContext(), Config.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0){
            if(grantResults[0]!=0){
             finishAffinity();
             System.runFinalization();
             System.exit(0);
            }
        }
    }
}
