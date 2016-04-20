package com.example.lamas.testdataxml.list_activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.lamas.testdataxml.R;
import com.example.lamas.testdataxml.data.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rocj2405 on 2016-03-02.
 */
public class ParcoursAdapter extends BaseExpandableListAdapter {


    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;



    // Liste des lieux du parcours, en ordre
    private List<String> lieuxListData = new ArrayList<>();

    public ParcoursAdapter(Context context, List<String> listDataHeader,
                           HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    // Format : TEXTVIEW - Description
    //          LISTVIEW - Liste des lieux à visiter, en ordre
    //          BUTTON   - Sélectionner
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parcours_layout, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.description);
        txtListChild.setText(childText);

        TextView txtListLieu = (TextView) convertView.findViewById(R.id.liste_ordonnee);
        String listeLieux = new String();


        for (int i = 0 ; i < lieuxListData.size() ; i++){
            listeLieux = listeLieux.concat((i+1) + ". " + lieuxListData.get(i) + "\n");
        }

        txtListLieu.setText(listeLieux);

        Button parcours_btn = (Button) convertView.findViewById(R.id.select_button);
        parcours_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, LieuxActivity.class);
                intent.putExtra("id", groupPosition);
                intent.putExtra("nameParcours", _listDataHeader.get(groupPosition));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(intent);
            }
        });

        //Personnalisation de l'application
        Data data = Data.getInstance(_context);
        txtListChild.setTextColor(data.getParameters().getCouleurTexte());
        txtListChild.setBackgroundColor(data.getParameters().getCouleurBackground());
        txtListChild.setTypeface(data.getParameters().getTypeface());
        txtListLieu.setTextColor(data.getParameters().getCouleurBackground());
        txtListLieu.setBackgroundColor(data.getParameters().getCouleurTexte());
        txtListLieu.setTypeface(data.getParameters().getTypeface());
        parcours_btn.setTextColor(data.getParameters().getCouleurBackground());
        parcours_btn.setBackgroundColor(data.getParameters().getCouleurTexte());
        parcours_btn.setTypeface(data.getParameters().getTypeface());

        convertView.setBackgroundColor(data.getParameters().getCouleurBackground());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        //Personnalisation de l'application
        Data data = Data.getInstance(_context);
        lblListHeader.setTextColor(data.getParameters().getCouleurBackground());
        lblListHeader.setBackgroundColor(data.getParameters().getCouleurTexte());
        lblListHeader.setTypeface(data.getParameters().getTypeface());

        //convertView.setBackgroundColor(data.getParameters().getCouleurBackground());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
