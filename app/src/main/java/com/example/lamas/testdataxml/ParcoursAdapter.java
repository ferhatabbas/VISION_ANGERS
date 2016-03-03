package com.example.lamas.testdataxml;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

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

    // TEST
    public List<Parcours> listParcours = new ArrayList<>();

    // Liste des lieux du parcours, en ordre
    List<String> lieuxListData = new ArrayList<>();

    public ParcoursAdapter(Context context, List<String> listDataHeader,
                           HashMap<String, List<String>> listChildData) {
        // TEST
        prepareData();

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

        prepareLieuxListData(groupPosition);
        //System.out.println("GP :" + groupPosition);
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

    private void prepareLieuxListData(int groupPosition){
        Parcours parcours = listParcours.get(groupPosition);
        List<Lieu> liste = parcours.getLieux();
        List<String> listTemp = new ArrayList<>();

        for(int i = 0 ; i < liste.size() ; i++){
            Lieu L = liste.get(i);
            listTemp.add(L.getName());
        }
        lieuxListData = listTemp;
    }

    public void prepareData(){
        Parcours chateauEtHistoire = new Parcours("Château et histoire",
                "Parcours pittoresque vous faisant visiter le magnifique château de la ville d'Angers ainsi" +
                        "que ses plus beaux quartiers historiques. Halte santé au McDonald's.");

        Parcours jardinEtSalade = new Parcours("Jardin et Salade",
                "Parcours sauvage dans les somptueux jardins emmenagés de la ville. Arrêt pour une" +
                        "petite salade au McDonald's du coin.");

        Parcours musiqueDuKing = new Parcours("Musique et le King",
                "Parcours dans les salles de concert afin de profiter des spectacles à la mode de l'été. " +
                        "Souper romantique au Burger King comme intermède.");

        Lieu chateau = new Lieu("Château d'Angers");
        Lieu jardinBotanique = new Lieu("Jardin botanique");
        Lieu jardinPoison = new Lieu("Jardin de plantes vénimeuses");
        Lieu mcdo = new Lieu("McDonald's");
        Lieu burgerKing = new Lieu("Burger King");
        Lieu concertMetal = new Lieu("Concert de métal");
        Lieu celineDion = new Lieu("Spectacle de Céline Dion");
        Lieu maisonHistorique = new Lieu("Maisons historiques");
        Lieu vieuxAngers = new Lieu("Vieux Angers");

        chateauEtHistoire.addLieu(chateau);
        chateauEtHistoire.addLieu(maisonHistorique);
        chateauEtHistoire.addLieu(vieuxAngers);
        chateauEtHistoire.addLieu(mcdo);

        jardinEtSalade.addLieu(jardinBotanique);
        jardinEtSalade.addLieu(jardinPoison);
        jardinEtSalade.addLieu(mcdo);

        musiqueDuKing.addLieu(concertMetal);
        musiqueDuKing.addLieu(burgerKing);
        musiqueDuKing.addLieu(vieuxAngers);
        musiqueDuKing.addLieu(celineDion);

        listParcours.add(chateauEtHistoire);
        listParcours.add(jardinEtSalade);
        listParcours.add(musiqueDuKing);
    }
}
