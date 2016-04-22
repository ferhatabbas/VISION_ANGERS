package com.example.lamas.testdataxml.list_activities;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

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
import java.util.Map;

public class ParcoursActivity extends Activity implements
        TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;

    private ParcoursAdapter parcoursAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader = new ArrayList<>();
    private HashMap<String, List<String>> listDataChild = new HashMap<>();
    private Data data;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_parcours);

        textToSpeech = new TextToSpeech(this, this);

        data = Data.getInstance(getApplicationContext());


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // preparing list data
        prepareListData();


        parcoursAdapter = new ParcoursAdapter(getApplicationContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(parcoursAdapter);


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

    }

    /*
       * Preparing the list data
       */

    private void prepareListData() {
        for(int i=1; i <= data.getParcourses().size(); i++){
            ParcoursABC entry = data.getParcourses().get(i);
            listDataHeader.add(entry.getName());
            String monuments = "";
            for(Monument temp: entry.getMonuments()){
                monuments = monuments+temp.getName()+"\n";
            }
            List<String> description = new ArrayList<String>();
            description.add(entry.getDescription()+"\n\n"+
                    "Durée "+entry.getDuree()+" min\n\n"+
                    "Évaluation "+entry.getEval().name());
            listDataChild.put(entry.getName(), description);
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
                convertTextToSpeech("Parcours");
            }
        } else {
            Log.e("error", "Initilization Failed!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if( textToSpeech != null){
            textToSpeech.shutdown();
        }
    }

    private void convertTextToSpeech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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

}

