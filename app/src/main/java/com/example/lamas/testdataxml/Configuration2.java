package com.example.lamas.testdataxml;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import java.util.logging.Handler;


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

