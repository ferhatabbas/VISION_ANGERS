package com.example.lamas.testdataxml.list_activities;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.lamas.testdataxml.R;
import com.example.lamas.testdataxml.data.Data;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rocj2405 on 2016-03-02.
 */
public class LieuxAdapter extends BaseExpandableListAdapter {

    private Data data;
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public LieuxAdapter(Context context, List<String> listDataHeader,
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

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = infalInflater.inflate(R.layout.list_item, null);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        //Personnalisation de l'application
        data = Data.getInstance(_context);
        txtListChild.setTextColor(data.getParameters().getCouleurTexte());
        txtListChild.setBackgroundColor(data.getParameters().getCouleurBackground());
        txtListChild.setTypeface(data.getParameters().getTypeface());

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
        data = Data.getInstance(_context);
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
