package com.example.lamas.testdataxml.data;

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
    private int radius;
    public ArrayList<String> accessibilite = new ArrayList<>();
    public ArrayList<String> horaires = new ArrayList<>();

    public Monument(int id, int radius, double latitude, double longitude, String name, String description,String informations,ArrayList<String> accessibilite, ArrayList<String> horaires) {
        this.id = id;
        this.radius = radius;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
        this.informations = informations;
        this.accessibilite = accessibilite;
        this.horaires=horaires;
    }

    public int getRadius() {
        return radius;
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

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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