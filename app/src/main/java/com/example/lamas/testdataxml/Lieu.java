package com.example.lamas.testdataxml;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocj2405 on 2016-03-02.
 */
public class Lieu {
    private String name;
    private List<Information> informations = new ArrayList<>();

    public Lieu(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public List<Information> getInformations() {
        return informations;
    }

    public void addInformations(Information desc, Information lang, Information heur, Information prix){
        informations.add(desc);
        informations.add(lang);
        informations.add(heur);
        informations.add(prix);
    }
}
