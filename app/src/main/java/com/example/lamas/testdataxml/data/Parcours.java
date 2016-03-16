package com.example.lamas.testdataxml.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocj2405 on 2016-03-02.
 */
public class Parcours {

    private String name;
    private String description;

    private List<Lieu> listLieux = new ArrayList<>();

    public Parcours (String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void addLieu(Lieu lieu) {
        listLieux.add(lieu);
    }

    public List<Lieu> getLieux(){
        return listLieux;
    }
}
