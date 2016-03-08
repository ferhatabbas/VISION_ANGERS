package com.example.lamas.testdataxml;

/**
 * Created by abbf2501 on 2016-02-08.
 */

import java.util.ArrayList;



public class Monument  {
    private int id;
    private double latitude;
    private double longitude;
    private String name;
    private String description;
    private String informations;
    public ArrayList<String> accessibilite = new ArrayList<>();
    public ArrayList<String> horaires = new ArrayList<>();

    public Monument(int id, double latitude, double longitude, String name, String description,String informations,ArrayList<String> accessibilite, ArrayList<String> horaires) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
        this.informations = informations;
        this.accessibilite = accessibilite;
        this.horaires=horaires;
    }

    public String getInformations() {
        return informations;
    }

    public void setInformations(String informations) {
        this.informations = informations;
    }

    public ArrayList<String> getHoraires() {
        return horaires;
    }

    public String getHorairesString(){
        String result = "";
        for(String horaire: this.horaires){
            result = result+horaire+"\n";
        }
        return result;
    }

    public void setHoraires(ArrayList<String> horaires) {
        this.horaires = horaires;
    }


    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getAccessibilite() {
        return accessibilite;
    }

    public String getAccessibiliteString(){
        String result = "";
        for(String acces: this.accessibilite){
            result = result+acces+"\n";
        }
        return result;
    }

    public void setAccessibilite(ArrayList<String> accessibilite) {
        this.accessibilite = accessibilite;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        String str ="id= " + id + "Name= " + name + "Lat= " + latitude + "Lng= " + longitude +"Desc= "+ description + "Access = "+ " [ " ;
        for(int i= 0 ; i< accessibilite.size();i++){
            str +=( accessibilite.get(i) + "  ");
        }
        str += "]";
        return str;
    }


}