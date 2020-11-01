package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Fire1 extends AppCompatActivity implements TextToSpeech.OnInitListener {
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
        speakdic.add("불이 났나요? 제가 하는말을 잘 듣고 따라하면 안전할 수 있어요.");
        speakdic.add("먼저 불이야! 라고 외치고 집 밖으로 나갑니다. 119에는 자동 신고됩니다.");
        speakdic.add("집밖으로 나갈 때에는 엘리베이터를 이용하지 말고 계단으로 대피합니다.");
        speakdic.add("대피할때에 검정색 연기가 있다면 옷소매로 입을 막고 머리를 숙이고 대피합니다.");
        setContentView(R.layout.activity_fire1);
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
                    if(!tts.isSpeaking()) {
                        try {
                            tts.speak(speakdic.get(i), tts.QUEUE_ADD, null);
                            i++;
                        } catch (Exception e) {
                            System.out.println(e);
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