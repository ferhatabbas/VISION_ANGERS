package com.example.lamas.testdataxml.list_activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.lamas.testdataxml.R;
import com.example.lamas.testdataxml.data.Data;
import com.example.lamas.testdataxml.data.Monument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InformationActivity extends Activity {
    LieuxAdapter lieuxAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        expListView = (ExpandableListView) findViewById(R.id.expandableListView3);

        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");
        Data data = Data.getInstance(getApplicationContext());
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

        lieuxAdapter = new LieuxAdapter(getApplicationContext(), listDataHeader, listDataChild);

        expListView.setAdapter(lieuxAdapter);

    }

}
