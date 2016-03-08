package com.example.lamas.testdataxml;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activite_test extends Activity {

    private Button bouton;

    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activite_test);

        data = Data.getInstance(getApplicationContext());

        bouton = (Button) findViewById(R.id.button1);
        bouton.setTypeface(data.getParameters().getTypeface());
        bouton.setBackgroundColor(data.getParameters().getCouleurBackground());
        bouton.setTextColor(data.getParameters().getCouleurTexte());

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //String toSpeak = bouton.getText().toString();
                //convertTextToSpeech(toSpeak);

                Intent secondeActivite = new Intent(Activite_test.this, ParcoursActivity.class);
                startActivity(secondeActivite);
            }

        });

    }
}