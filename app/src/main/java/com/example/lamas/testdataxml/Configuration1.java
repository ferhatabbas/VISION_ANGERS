package com.example.lamas.testdataxml;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Configuration1 extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private Button bNonVoyant;
    private Button bMalVoyant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_configuration1);
        bNonVoyant = (Button) findViewById(R.id.button1);
        bMalVoyant = (Button) findViewById(R.id.button2);

        textToSpeech = new TextToSpeech(this, this);

        bNonVoyant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String toSpeak = bNonVoyant.getText().toString();
                convertTextToSpeech(toSpeak);

                Intent secondeActivite = new Intent(Configuration1.this, Activite_test.class);
                startActivity(secondeActivite);
            }

        });

        bMalVoyant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String toSpeak = bMalVoyant.getText().toString();
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
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
                convertTextToSpeech("Êtes-vous non-voyant ou malvoyant?");
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
            convertTextToSpeech("Êtes-vous non-voyant ou malvoyant?");
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
