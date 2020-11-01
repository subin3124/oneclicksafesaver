package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class water extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextToSpeech tts;
    int i=2;
    DB dbh;
    int mils=0;
    SQLiteDatabase db;
    List<String> speakdic = new ArrayList<String>();
    Cursor cursor;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speakdic.add("뜨거운 물에 데였나요? 제가 하는말을 잘 듣고 따라하면 안전할 수 있어요.");
        speakdic.add("먼저 화장실로 가서 찬물을 약하게 틀어 놓으세요");
        speakdic.add("찬물이 약하게 틀어졌나요? 이제 찬물을 데인 부분에 뿌려주세요");
        speakdic.add("아프지 않을때까지 흐르는 찬물에 데인곳을 씼어야 합니다.");
        setContentView(R.layout.water);
        tts = new TextToSpeech(this,this);


    }

    @Override
    public void onInit(int status) {
        if (status == tts.SUCCESS) {
            dbh = new DB(this,null,null,1);
            db = dbh.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM User",null);
            cursor.moveToFirst();
            mils = cursor.getInt(cursor.getColumnIndex("대기밀리초"));
            int lang = tts.setLanguage(Locale.KOREAN);
            tts.speak(speakdic.get(0),tts.QUEUE_FLUSH,null);
            tts.speak(speakdic.get(1),tts.QUEUE_ADD,null);
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(!tts.isSpeaking()){
                    try {
                        tts.speak(speakdic.get(i), tts.QUEUE_ADD, null);
                        i++;
                    }catch (Exception e) {
                        timer.cancel();
                    }

                    }
                }
            };
            timer.schedule(timerTask,mils,mils);


        }
    }

    @Override
    protected void onDestroy() {
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


}