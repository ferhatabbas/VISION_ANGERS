package com.example.lamas.testdataxml.initial_tests;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lamas.testdataxml.data.Data;
import com.example.lamas.testdataxml.R;
import com.example.lamas.testdataxml.list_activities.ParcoursActivity;

import java.util.Locale;

public class Configuration5 extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;

    private Button bPolice1;
    private Button bPolice2;
    private Button bPolice3;
    private Button bPolice4;

    private Typeface tfArial;
    private Typeface tfTrebuchet;
    private Typeface tfverdana;
    private Typeface tfHelvetica;
    private Intent nextActivity;

    private Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_configuration5);

        data = Data.getInstance(getApplicationContext());

        //Identification des boutons
        bPolice1 = (Button) findViewById(R.id.button1);
        bPolice2 = (Button) findViewById(R.id.button2);
        bPolice3 = (Button) findViewById(R.id.button3);
        bPolice4 = (Button) findViewById(R.id.button4);

        //Création des types d'écriture
        tfArial = Typeface.createFromAsset(getAssets(), "arial.ttf");
        tfTrebuchet = Typeface.createFromAsset(getAssets(), "Trebuchet MS.ttf");
        tfverdana = Typeface.createFromAsset(getAssets(), "verdana.ttf");
        tfHelvetica = Typeface.createFromAsset(getAssets(), "Helvetica.ttf");

        //Assignation des types d'écriture
        bPolice1.setTypeface(tfArial);
        bPolice2.setTypeface(tfTrebuchet);
        bPolice3.setTypeface(tfverdana);
        bPolice4.setTypeface(tfHelvetica);

        //Couleur background boutons
        bPolice1.setBackgroundColor(data.getParameters().getCouleurBackground());
        bPolice2.setBackgroundColor(data.getParameters().getCouleurBackground());
        bPolice3.setBackgroundColor(data.getParameters().getCouleurBackground());
        bPolice4.setBackgroundColor(data.getParameters().getCouleurBackground());

        //Couleur lettrage boutons
        bPolice1.setTextColor(data.getParameters().getCouleurTexte());
        bPolice2.setTextColor(data.getParameters().getCouleurTexte());
        bPolice3.setTextColor(data.getParameters().getCouleurTexte());
        bPolice4.setTextColor(data.getParameters().getCouleurTexte());

        textToSpeech = new TextToSpeech(this, this);
        nextActivity = new Intent(this, ParcoursActivity.class);


        //Police Arial
        bPolice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.getParameters().setTypeface(tfArial);

                String toSpeak = bPolice1.getText().toString();
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                startActivity(nextActivity);
            }
        });

        bPolice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.getParameters().setTypeface(tfTrebuchet);

                String toSpeak = bPolice2.getText().toString();
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                startActivity(nextActivity);
            }
        });

        bPolice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.getParameters().setTypeface(tfverdana);

                String toSpeak = bPolice3.getText().toString();
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                startActivity(nextActivity);
            }
        });

        bPolice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.getParameters().setTypeface(tfHelvetica);

                String toSpeak = bPolice4.getText().toString();
                convertTextToSpeech(toSpeak);
                if(textToSpeech.isSpeaking()) SystemClock.sleep(1000);
                startActivity(nextActivity);
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
                convertTextToSpeech("Quelle police préférez-vous?");
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
    public void onStop(){
        super.onStop();
        if(textToSpeech != null){
            textToSpeech.stop();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(textToSpeech != null){
            convertTextToSpeech("Quelle police préférez-vous?");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(textToSpeech != null){
            textToSpeech.shutdown();
        }
    }

    private void convertTextToSpeech(String text) {
        //String text = inputText.getText().toString();
        //if (null == text || "".equals(text)) {
        //    text = "Please give some input.";
        //}
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
