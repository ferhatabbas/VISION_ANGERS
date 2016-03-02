package com.example.lamas.testdataxml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class POIDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poidetails);
        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");

        //Access informations about the poi
        Data data = Data.getInstance(getApplicationContext());
        Monument poi = data.getMonuments().get(id);

        //Display informations
        TextView description = (TextView) findViewById(R.id.textView2);
        description.setText(poi.getDescription());

        TextView accessibilite = (TextView) findViewById(R.id.textView4);
        for(String temp: poi.getAccessibilite()){
            accessibilite.append(temp+"\n");
        }

        TextView schedule = (TextView) findViewById(R.id.textView6);
        for(String temp: poi.getHoraires()){
            schedule.append(temp+"\n");
        }

        TextView informations = (TextView) findViewById(R.id.textView8);
        informations.setText(poi.getInformations());
        

    }

}
