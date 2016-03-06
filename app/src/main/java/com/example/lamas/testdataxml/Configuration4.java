package com.example.lamas.testdataxml;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class Configuration4 extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;

    private Button bCombinaison1;
    private Button bCombinaison2;
    private Button bCombinaison3;
    private Button bCombinaison4;
    private Button bCombinaison5;

    private Data data;

    private int couleurBackground;
    private int couleurLettrage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_configuration4);

        bCombinaison1 = (Button) findViewById(R.id.button1);
        bCombinaison2 = (Button) findViewById(R.id.button2);
        bCombinaison3 = (Button) findViewById(R.id.button3);
        bCombinaison4 = (Button) findViewById(R.id.button4);
        bCombinaison5 = (Button) findViewById(R.id.button5);

        textToSpeech = new TextToSpeech(this, this);

        data = Data.getInstance(getApplicationContext());

        bCombinaison1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = bCombinaison1.getText().toString();
                convertTextToSpeech(toSpeak);

                //Backgroung noir et lettrage blanc
                couleurBackground = Color.BLACK;
                data.getParameters().setCouleurBackground(couleurBackground );
                couleurLettrage = Color.WHITE;
                data.getParameters().setCouleurTexte(couleurLettrage);

                Intent secondeActivite = new Intent(Configuration4.this, Configuration5.class);
                startActivity(secondeActivite);
            }
        });


        bCombinaison2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = bCombinaison2.getText().toString();
                convertTextToSpeech(toSpeak);

                //Backgroung jaune et lettrage noir
                couleurBackground = Color.YELLOW;
                data.getParameters().setCouleurBackground(couleurBackground );
                couleurLettrage = Color.BLACK;
                data.getParameters().setCouleurTexte(couleurLettrage);

                Intent secondeActivite = new Intent(Configuration4.this, Configuration5.class);
                startActivity(secondeActivite);
            }
        });


        bCombinaison3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = bCombinaison3.getText().toString();
                convertTextToSpeech(toSpeak);

                //Backgroung rouge et lettrage blanc
                couleurBackground = Color.RED;
                data.getParameters().setCouleurBackground(couleurBackground );
                couleurLettrage = Color.WHITE;
                data.getParameters().setCouleurTexte(couleurLettrage);

                Intent secondeActivite = new Intent(Configuration4.this, Configuration5.class);
                startActivity(secondeActivite);
            }
        });


        bCombinaison4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = bCombinaison4.getText().toString();
                convertTextToSpeech(toSpeak);

                //Backgroung blanc et lettrage noir
                couleurBackground = Color.WHITE;
                data.getParameters().setCouleurBackground(couleurBackground );
                couleurLettrage = Color.BLACK;
                data.getParameters().setCouleurTexte(couleurLettrage);

                Intent secondeActivite = new Intent(Configuration4.this, Configuration5.class);
                startActivity(secondeActivite);
            }
        });


        bCombinaison5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = bCombinaison5.getText().toString();
                convertTextToSpeech(toSpeak);

                //Backgroung jaune et lettrage rouge
                couleurBackground = Color.YELLOW;
                data.getParameters().setCouleurBackground(couleurBackground );
                couleurLettrage = Color.RED;
                data.getParameters().setCouleurTexte(couleurLettrage);

                Intent secondeActivite = new Intent(Configuration4.this, Configuration5.class);
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
                convertTextToSpeech("Quelle combinaison de couleurs préférez-vous?");
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
            convertTextToSpeech("Quelle combinaison de couleurs préférez-vous?");
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

