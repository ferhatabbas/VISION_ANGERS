package com.example.lamas.testdataxml.list_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.lamas.testdataxml.MainActivity;
import com.example.lamas.testdataxml.R;
import com.example.lamas.testdataxml.data.Data;
import com.example.lamas.testdataxml.data.Information;
import com.example.lamas.testdataxml.data.Lieu;
import com.example.lamas.testdataxml.data.Monument;
import com.example.lamas.testdataxml.data.Parcours;
import com.example.lamas.testdataxml.data.ParcoursABC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LieuxActivity extends Activity implements
        TextToSpeech.OnInitListener{

    private TextToSpeech textToSpeech;
    private Data data;

    LieuxAdapter lieuxAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();
    Button bNavigation;
    int id;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_lieux);

        textToSpeech = new TextToSpeech(this, this);


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableListView2);

        bNavigation = (Button) findViewById(R.id.buttonNavigation);

        Bundle b = getIntent().getExtras();
        id = b.getInt("id")+1;
        String nameParcours = b.getString("nameParcours");

        TextView txtListChild = (TextView) findViewById(R.id.parcoursTitle);
        txtListChild.setText(nameParcours);

        //Personnalisation de l'application
        data = Data.getInstance(getApplicationContext());
        txtListChild.setTextColor(data.getParameters().getCouleurTexte());
        txtListChild.setBackgroundColor(data.getParameters().getCouleurBackground());
        txtListChild.setTypeface(data.getParameters().getTypeface());

        // preparing list data
        prepareListData(id);

        lieuxAdapter = new LieuxAdapter(getApplicationContext(), listDataHeader, listDataChild);


        // setting list adapter
        expListView.setAdapter(lieuxAdapter);
        //expListView.expandGroup(0);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                String toSpeak = listDataHeader.get(groupPosition).toString();
                convertTextToSpeech(toSpeak);
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                String toSpeak = listDataChild.get(listDataHeader.get(groupPosition)).get(
                        childPosition).toString();
                convertTextToSpeech(toSpeak);
                return false;
            }
        });

        bNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LieuxActivity.this, MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }

        });

    }

    /*
       * Preparing the list data
       */

    private void prepareListData(int id) {
        Data data = Data.getInstance(getApplicationContext());
        ParcoursABC parcours_temp = data.getParcourses().get(id);
        for(Monument monument: parcours_temp.getMonuments()){
            listDataHeader.add(monument.getName());
            List<String> resume = new ArrayList<>();
            resume.add("Description:\n"+monument.getDescription());
            resume.add("Accessibilités:\n"+monument.getAccessibiliteString());
            resume.add("Horaires:\n"+monument.getHorairesString());
            listDataChild.put(monument.getName(), resume);
        }

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CANADA_FRENCH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {
                Bundle b = getIntent().getExtras();
                String nameParcours = b.getString("nameParcours");
                convertTextToSpeech(nameParcours);
            }
        } else {
            Log.e("error", "Initilization Failed!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(textToSpeech != null){
            textToSpeech.shutdown();
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

    private void convertTextToSpeech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
