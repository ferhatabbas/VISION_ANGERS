package com.example.lamas.testdataxml;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Activite_test extends AppCompatActivity {

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

        getWindow().getDecorView().setBackgroundColor(data.getParameters().getCouleurBackground());

    }

}
