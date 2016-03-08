package com.example.lamas.testdataxml;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Configuration3 extends Activity implements TextToSpeech.OnInitListener {

    TextToSpeech textToSpeech;

    private Button bTaille1;
    private Button bTaille2;
    private Button bTaille3;
    private Button bTaille4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_configuration3);

        //Identification des boutons
        bTaille1 = (Button) findViewById(R.id.button1);
        bTaille2 = (Button) findViewById(R.id.button2);
        bTaille3 = (Button) findViewById(R.id.button3);
        bTaille4 = (Button) findViewById(R.id.button4);

        textToSpeech = new TextToSpeech(this, this);


        //Gestion des boutons
        bTaille1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = "Taille 1";
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                Intent secondeActivite = new Intent(Configuration3.this, Configuration4.class);
                startActivity(secondeActivite);
            }
        });


        bTaille2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = "Taille 2";
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                Intent secondeActivite = new Intent(Configuration3.this, Configuration4.class);
                startActivity(secondeActivite);
            }
        });


        bTaille3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = "Taille 3";
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                Intent secondeActivite = new Intent(Configuration3.this, Configuration4.class);
                startActivity(secondeActivite);
            }
        });

        bTaille4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = "Taille 4";
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                Intent secondeActivite = new Intent(Configuration3.this, Configuration4.class);
                startActivity(secondeActivite);
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CANADA_FRENCH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {
                convertTextToSpeech("Parmi les quatre lignes ci-dessous, quelle est la plus petite que vous pouvez lire?");
            }
        } else {
            Log.e("error", "Initilization Failed!");
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if(textToSpeech != null ){
            textToSpeech.stop();

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(textToSpeech != null){
            convertTextToSpeech("Parmi les quatre lignes ci-dessous, quelle est la plus petite que vous pouvez lire?");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
    }

    private void convertTextToSpeech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
