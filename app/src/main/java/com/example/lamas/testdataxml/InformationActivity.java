package com.example.lamas.testdataxml;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.lamas.testdataxml.data.Data;
import com.example.lamas.testdataxml.data.Monument;
import com.example.lamas.testdataxml.initial_tests.Configuration2;
import com.example.lamas.testdataxml.list_activities.LieuxAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class InformationActivity extends Activity implements
        TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    public Data data;
    private Button bContinue;
    LieuxAdapter lieuxAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        bContinue = (Button) findViewById(R.id.button);

        textToSpeech = new TextToSpeech(this, this);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        expListView = (ExpandableListView) findViewById(R.id.expandableListView3);

        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");
        data = Data.getInstance(getApplicationContext());
        Monument monument = data.getMonuments().get(id);
        listDataHeader.add("Description");
        listDataHeader.add("Accessibilité");
        listDataHeader.add("Horaires");
        //listDataHeader = b.getStringArrayList("headers");
        //listDataChild = (HashMap<String, List<String>>) b.getSerializable("information");
        listDataChild.put("Description", new ArrayList<>(Arrays.asList(monument.getDescription())));
        listDataChild.put("Accessibilité", monument.getAccessibilite());
        listDataChild.put("Horaires", monument.getHoraires());

        TextView txtListChild = (TextView) findViewById(R.id.nom_lieu);
        txtListChild.setText(monument.getName());

        //Personnalisation de l'application
        data = Data.getInstance(getApplicationContext());
        txtListChild.setTextColor(data.getParameters().getCouleurTexte());
        txtListChild.setBackgroundColor(data.getParameters().getCouleurBackground());
        txtListChild.setTypeface(data.getParameters().getTypeface());

        lieuxAdapter = new LieuxAdapter(getApplicationContext(), listDataHeader, listDataChild);

        expListView.setAdapter(lieuxAdapter);

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

        if(data.getParameters().isGooglemaps() == false){
            bContinue.setVisibility(View.INVISIBLE);

        }else if(data.getParameters().isGooglemaps() == true){
            bContinue.setVisibility(View.VISIBLE);
            bContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String toSpeak = bContinue.getText().toString();
                    convertTextToSpeech(toSpeak);
                    Intent secondeActivite = new Intent(InformationActivity.this, MainActivity.class);
                    startActivity(secondeActivite);
                }

            });

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

    /**
     * Releases the resources used by the TextToSpeech engine. It is good
     * practice for instance to call this method in the onDestroy() method of an
     * Activity so the TextToSpeech engine can be cleanly stopped.
     *
     * @see android.app.Activity#onDestroy()
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
    }

    /**
     * Speaks the string using the specified queuing strategy and speech
     * parameters.
     */
    private void convertTextToSpeech(String text) {
        //String text = inputText.getText().toString();
        //if (null == text || "".equals(text)) {
        //    text = "Please give some input.";
        //}
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }



}
