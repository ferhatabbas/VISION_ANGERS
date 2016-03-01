package com.example.lamas.testdataxml;

import java.util.ArrayList;

/**
 * Created by abbf2501 on 2016-02-10.
 */
public class Parcours {
    private int Id;
    private String name ;
    private ArrayList<Integer> tempsMonum;
    private ArrayList<Monument> monuments;
    private long duree;
    private int evaltemp;
    private Evaluation eval;




//don't expose

    public Parcours(int id, String name, long duree, Evaluation eval, ArrayList<Monument> monuments) {
        Id = id;
        this.name = name;
        this.monuments = monuments;
        this.duree = duree;
        this.eval = eval;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getTempsMonum() {
        return tempsMonum;
    }

    public void setTempsMonum(ArrayList<Integer> tempsMonum) {
        this.tempsMonum = tempsMonum;
    }

    public ArrayList<Monument> getMonuments() {
        return monuments;
    }

    public void setMonuments(ArrayList<Monument> monuments) {
        this.monuments = monuments;
    }

    public long getDuree() {
        return duree;
    }

    public void setDuree(long duree) {
        this.duree = duree;
    }

    public int getEvaltemp() {
        return evaltemp;
    }

    public void setEvaltemp(int evaltemp) {
        this.evaltemp = evaltemp;
    }

    public Evaluation getEval() {
        return eval;
    }

    public void setEval(Evaluation eval) {
        this.eval = eval;
    }
}
