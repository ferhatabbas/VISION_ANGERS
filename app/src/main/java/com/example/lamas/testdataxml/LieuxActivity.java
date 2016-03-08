package com.example.lamas.testdataxml;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LieuxActivity extends Activity {
    LieuxAdapter lieuxAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();

    // TEST
    public List<Parcours> listParcours = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_lieux);

        // TEST
        prepareData();

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableListView2);

        // TEST : Passage d'un paramètre
        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");
        String nameParcours = b.getString("nameParcours");

        System.out.println("ID parcours: " + id);

        TextView txtListChild = (TextView) findViewById(R.id.parcoursTitle);
        txtListChild.setText(nameParcours);

        // preparing list data
        prepareListData(id);

        lieuxAdapter = new LieuxAdapter(getApplicationContext(), listDataHeader, listDataChild);

//        Button parcours_btn = (Button) findViewById(R.id.buttonNavigation);
//        parcours_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<String> listHeaders = prepareInformationHeader();
//                HashMap<String, List<String>> listInformationChild = prepareInformationChild(0, 0);
//                Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
//                intent.putExtra("nomMonument", "Faculté des lettres et sciences humaines" );
//                intent.putStringArrayListExtra("headers", (ArrayList<String>) listHeaders);
//                intent.putExtra("information", listInformationChild);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });

        // setting list adapter
        expListView.setAdapter(lieuxAdapter);
        //expListView.expandGroup(0);
    }

    /*
       * Preparing the list data
       */

    private void prepareListData(int id) {
        //Parcours parcours = listParcours.get(id);
        //listDataHeader.add(parcours.getName());
        //List<Lieu> listLieux = parcours.getLieux();
        //List<String> lieux = new ArrayList<>();
        Data data = Data.getInstance(getApplicationContext());
        ParcoursABC parcours_temp = data.getParcourses().get(id+1);
        for(Monument monument: parcours_temp.getMonuments()){
            listDataHeader.add(monument.getName());
            List<String> resume = new ArrayList<>();
            resume.add("Description\n\n"+monument.getDescription());
            resume.add("Accessibilités\n\n"+monument.getAccessibiliteString());
            resume.add("Horaires\n\n"+monument.getHorairesString());
            listDataChild.put(monument.getName(), resume);
        }
        /*for (int i = 0; i < listLieux.size(); i++) {
            List<String> resume = new ArrayList<>();
            listDataHeader.add(listLieux.get(i).getName());
            resume.add()
            listDataChild.put(listLieux.get(i).getName(), prepareResume(listLieux.get(i)));
        }*/
    }

    private List<String> prepareInformationHeader(){
        List<String> headers = new ArrayList<>();
        headers.add("Description");
        headers.add("Accessibilités");
        headers.add("Horaires");

        return headers;
    }

    private HashMap<String, List<String>> prepareInformationChild(int groupID, int monumentID){
        Data data = Data.getInstance(getApplicationContext());
        ParcoursABC parcours_temp = data.getParcourses().get(groupID + 1);
        ArrayList<Monument> monuments = parcours_temp.getMonuments();
        Monument monument = monuments.get(monumentID);

        HashMap<String, List<String>> informations = new HashMap<>();
        List<String> descriptions = new ArrayList<>();
        descriptions.add(monument.getDescription());
        List<String> accessibilités = new ArrayList<>();
        accessibilités.add(monument.getAccessibiliteString());
        List<String> horaires = new ArrayList<>();
        horaires.add(monument.getHorairesString());

        informations.put("Description", descriptions);
        informations.put("Accessibilités", accessibilités);
        informations.put("Horaires", horaires);

        return informations;
    }

    private List<String> prepareResume(Lieu lieu){
        List<Information> informations = lieu.getInformations();
        List<String> resume = new ArrayList<>();

        for (int i = 0 ; i < informations.size() ; i++) {
            Information info = informations.get(i);
            String desc = new String();

            desc = info.getTypeInfo().concat("\n\n" + info.getInformation());
            resume.add(desc);
        }

        System.out.println(resume);
        return resume;
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

}
