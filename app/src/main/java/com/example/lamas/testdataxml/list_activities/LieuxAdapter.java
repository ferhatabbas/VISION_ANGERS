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
import com.example.lamas.testdataxml.data.Information;
import com.example.lamas.testdataxml.data.Lieu;
import com.example.lamas.testdataxml.data.Parcours;

import java.util.ArrayList;
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

    // TEST
    public List<Parcours> listParcours = new ArrayList<>();

    // Liste des lieux du parcours, en ordre
    List<String> lieuxListData = new ArrayList<>();

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

    // TODO
    private void prepareLieuInformation(int groupPosition){
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

        Information desc1 = new Information("Description", "Ceci est une information pertinente sur le lieu sélectionné.");
        Information desc2 = new Information("Description", "Voilà de l'information vraiment fascinante sur quelque chose.");
        Information desc3 = new Information("Description", "Description renversante sur un lieu à quelque part dans la ville.");

        Information lang1 = new Information("Langues parlées", "Français");
        Information lang2 = new Information("Langues parlées", "Français, Anglais");
        Information lang3 = new Information("Langues parlées", "Français, Anglais, Italien");

        Information heur1 = new Information("Heures d'ouvertures", "Samedi : Ouvert\nDimanche : Ouvert\n"
                + "Lundi : Fermé\nMardi : Fermé\nMercredi : Fermé\nJeudi : Fermé\nVendredi : Ouvert");
        Information heur2 = new Information("Heures d'ouvertures", "Samedi : Ouvert\nDimanche : Ouvert\n"
                + "Lundi : Ouvert\nMardi : Ouvert\nMercredi : Ouvert\nJeudi : Ouvert\nVendredi : Ouvert");
        Information heur3 = new Information("Heures d'ouvertures", "Samedi : Fermé\nDimanche : Ouvert\n"
                + "Lundi : Fermé\nMardi : Ouvert\nMercredi : Fermé\nJeudi : Ouvert\nVendredi : Ouvert");

        Information prix1 = new Information("Prix d'entrée", "13$");
        Information prix2 = new Information("Prix d'entrée", "Gratuit");
        Information prix3 = new Information("Prix d'entrée", "5$ pour les enfants de moins de 12 ans\n12$ pour les adultes et les "
                + "enfants de 12 ans et plus");

        chateau.addInformations(desc1, lang2, heur1, prix2);
        jardinBotanique.addInformations(desc2, lang1, heur3, prix3);
        jardinPoison.addInformations(desc3, lang3, heur2, prix1);
        mcdo.addInformations(desc1, lang3, heur1, prix1);
        burgerKing.addInformations(desc3, lang1, heur2, prix2);
        concertMetal.addInformations(desc2, lang2, heur3, prix3);
        celineDion.addInformations(desc1, lang1, heur1, prix2);
        maisonHistorique.addInformations(desc2, lang3, heur2, prix1);
        vieuxAngers.addInformations(desc3, lang2, heur3, prix3);

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
