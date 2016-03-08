package com.example.lamas.testdataxml;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ParcoursActivity extends Activity implements
        TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;

    ParcoursAdapter parcoursAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();
    Data data;

    // TEST
    public List<Parcours> listParcours = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_parcours);

        textToSpeech = new TextToSpeech(this, this);

        // TEST
        //prepareData();


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // TEST : Passage d'un paramètre
        //Bundle b = getIntent().getExtras();
        //int id = b.getInt("id");

        // preparing list data
        prepareListData();


        parcoursAdapter = new ParcoursAdapter(getApplicationContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(parcoursAdapter);

        //Personnalisation de l'application
        data = Data.getInstance(getApplicationContext());

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
        Data data = Data.getInstance(getApplicationContext());
        for(Map.Entry<Integer, ParcoursABC> entry : data.getParcourses().entrySet()){
            listDataHeader.add(entry.getValue().getName());
            String monuments = "";
            for(Monument temp: entry.getValue().getMonuments()){
                monuments = monuments+temp.getName()+"\n";
            }
            List<String> description = new ArrayList<String>();
            description.add(entry.getValue().getDescription()+"\n\n"+
                    "Durée "+entry.getValue().getDuree()+" min\n\n"+
                    "Évaluation "+entry.getValue().getEval().name()+"\n\n"+
                     monuments);
            listDataChild.put(entry.getValue().getName(), description);
        }
        //for (int i = 0; i < listParcours.size(); i++) {
        //    Parcours parcours = listParcours.get(i);
        //    listDataHeader.add(parcours.getName());
        //    List<String> description = new ArrayList<String>();
        //    description.add(parcours.getDescription());
        //    listDataChild.put(parcours.getName(), description );
        //}
    }

    public void prepareData() {
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
        textToSpeech.shutdown();
    }

    private void convertTextToSpeech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}

