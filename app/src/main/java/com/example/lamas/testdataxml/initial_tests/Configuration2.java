package com.example.lamas.testdataxml.initial_tests;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lamas.testdataxml.R;

import java.util.Locale;


public class Configuration2 extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private Button bAccepter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_configuration2);

        //Identification des boutons
        bAccepter = (Button) findViewById(R.id.button1);

        textToSpeech = new TextToSpeech(this, this);

        //Gestion des boutons
        bAccepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = bAccepter.getText().toString();
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                Intent secondeActivite = new Intent(Configuration2.this, Configuration3.class);
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
                convertTextToSpeech("Les prochaines questions permettront de personnaliser l'application en fonction de vos préférences visuelles au niveau de la police et des couleurs");
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
            convertTextToSpeech("Les prochaines questions permettront de personnaliser l'application en fonction de vos préférences visuelles au niveau de la police et des couleurs");

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

