package com.example.lamas.testdataxml.initial_tests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lamas.testdataxml.Activite_test;
import com.example.lamas.testdataxml.R;
import com.example.lamas.testdataxml.data.Data;

import java.util.Locale;

public class Configuration1 extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private Button BAssister;
    private Button BLibre;
    private Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_configuration1);
        BAssister = (Button) findViewById(R.id.button1);
        BLibre = (Button) findViewById(R.id.button2);
        data=Data.getInstance(getApplicationContext());
        textToSpeech = new TextToSpeech(this, this);

        BAssister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String toSpeak = BAssister.getText().toString();
                convertTextToSpeech(toSpeak);
                data.getParameters().setGooglemaps(true);
                Intent secondeActivite = new Intent(Configuration1.this, Configuration2.class);
                startActivity(secondeActivite);
            }

        });

        BLibre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String toSpeak = BLibre.getText().toString();
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                data.getParameters().setGooglemaps(false);
                    Intent secondeActivite = new Intent(Configuration1.this, Configuration2.class);
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
                convertTextToSpeech("Voulez-vous une navigation assistée ou libre?");
            }
        } else {
            Log.e("error", "Initilization Failed!");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(textToSpeech != null && !textToSpeech.isSpeaking() ){
            textToSpeech.stop();

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(textToSpeech != null){
            convertTextToSpeech("Voulez-vous une navigation assistée ou libre?");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
    }

    private void convertTextToSpeech(String text) {
        //String text = inputText.getText().toString();
        //if (null == text || "".equals(text)) {
        //    text = "Please give some input.";
        //}
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
