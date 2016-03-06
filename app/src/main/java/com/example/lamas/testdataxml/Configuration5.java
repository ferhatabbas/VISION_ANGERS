package com.example.lamas.testdataxml;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
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




        //Police Arial
        bPolice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.getParameters().setTypeface(tfArial);

                String toSpeak = bPolice1.getText().toString();
                convertTextToSpeech(toSpeak);

                Intent secondeActivite = new Intent(Configuration5.this, Activite_test.class);
                startActivity(secondeActivite);
            }
        });

        bPolice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.getParameters().setTypeface(tfTrebuchet);

                String toSpeak = bPolice2.getText().toString();
                convertTextToSpeech(toSpeak);

                Intent secondeActivite = new Intent(Configuration5.this, Activite_test.class);
                startActivity(secondeActivite);
            }
        });

        bPolice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.getParameters().setTypeface(tfverdana);

                String toSpeak = bPolice3.getText().toString();
                convertTextToSpeech(toSpeak);

                Intent secondeActivite = new Intent(Configuration5.this, Activite_test.class);
                startActivity(secondeActivite);
            }
        });

        bPolice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.getParameters().setTypeface(tfHelvetica);

                String toSpeak = bPolice4.getText().toString();
                convertTextToSpeech(toSpeak);

                Intent secondeActivite = new Intent(Configuration5.this, Activite_test.class);
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
    public void onResume() {
        super.onResume();
        if(textToSpeech != null){
            convertTextToSpeech("Quelle police préférez-vous?");
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
