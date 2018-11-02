package com.example.deneme.xapiyoutubevideo;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends YouTubeBaseActivity {


    Button b, bd, br;
    boolean aa = true;

    String VIDEO_ID = ""; //youtube video id sini yazıyoruz
    String API_KEY = ""; //Youtube API' sinin keyini yapışıtıyoruz

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private YouTubePlayer yp;
    Chronometer kronometre;
    long baslangic = 0, bitis =0;
    long pauseoffset;

    long fark =0 ;
    long video_uzunluk = 0;

    boolean başarı_durumu = true;
    double oran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youTubePlayerView = findViewById(R.id.youtube_view);
        b = findViewById(R.id.btnOynat);
        bd = findViewById(R.id.btnDurdur);
        kronometre = findViewById(R.id.kronometre);

        br = findViewById(R.id.btnReset);


        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo(VIDEO_ID);
                yp = youTubePlayer;

                kronometre.setBase(SystemClock.elapsedRealtime() - pauseoffset);
                kronometre.start();
                baslangic = kronometre.getBase();

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aa){
                    youTubePlayerView.initialize(API_KEY, onInitializedListener);
                    aa = false;
                }
                else{
                    yp.play();
                    kronometre.setBase(SystemClock.elapsedRealtime() - pauseoffset);
                    kronometre.start();
                    baslangic = SystemClock.elapsedRealtime();

                }

            }
        });
        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yp.pause();
                kronometre.stop();
                Log.d("-----------------------"," "+SystemClock.elapsedRealtime());
                pauseoffset = SystemClock.elapsedRealtime() - kronometre.getBase();
                bitis = SystemClock.elapsedRealtime();
                fark = fark + ( bitis - baslangic );   //mili saniye
                Log.d("-----------------------", "Fark: "+fark);
                video_uzunluk = yp.getDurationMillis();
                Log.d("-----------------------", "Video Uzunluğu: "+video_uzunluk);

                oran = (double)fark/ (double) video_uzunluk;   //Videonun ne kadarının izlendiğini gösteriyor bize.

                VeriGonder g = new VeriGonder();
                g.setOran(oran);

                Log.d("-----------------------", "Gerçek Oran: " + oran);

                g.execute();

            }
        });

        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kronometre.setBase(SystemClock.elapsedRealtime());
                pauseoffset = 0;
                youTubePlayerView.initialize(API_KEY, onInitializedListener);
            }
        });




    }

}


