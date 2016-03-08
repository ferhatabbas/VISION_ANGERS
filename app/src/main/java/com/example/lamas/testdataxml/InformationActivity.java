package com.example.lamas.testdataxml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InformationActivity extends AppCompatActivity {
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
        listDataHeader = b.getStringArrayList("headers");
        listDataChild = (HashMap<String, List<String>>) b.getSerializable("information");
        String nomMonument = b.getString("nomMonument");

        TextView txtListChild = (TextView) findViewById(R.id.nom_lieu);
        txtListChild.setText(nomMonument);

        lieuxAdapter = new LieuxAdapter(getApplicationContext(), listDataHeader, listDataChild);

        expListView.setAdapter(lieuxAdapter);

    }

}
