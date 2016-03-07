package com.example.lamas.testdataxml;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abbf2501 on 2016-02-29.
 */
public class Data {
    private static Data instance;
    private Map<Integer, Monument> monuments = new HashMap<>();
    private Map<Integer, ParcoursABC> parcourses = new HashMap<>();
    private Parameters parameters = new Parameters();


    public static Data getInstance(Context context) {
        if(instance == null) {
            instance = new Data(context);
        }
        return instance;
    }

    private Data(Context context) {
        String temp;
        JSONObject jsonMonuments;
        try {
            InputStream is = context.getAssets().open(Constants.FILE_PATH);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            temp = new String(buffer, "UTF-8");
            jsonMonuments = new JSONObject(temp);
            recupererData(jsonMonuments);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void recupererData(JSONObject obj) throws JSONException {

        int Id;
        double lat;
        double lng;
        String name;
        String desc;
        String infos;
        long duree;
        Evaluation eval;

        ArrayList<Monument> tempsMonum = new ArrayList<>();

        JSONArray Monuments = obj.getJSONArray(Constants.BALISE_MONUMENTS);
        JSONArray Parcours = obj.getJSONArray(Constants.BALISE_PARCOURS);


        for(int i=0 ; i< Monuments.length() ; i++) {
            ArrayList<String> Accessibilite =new ArrayList<>();
            ArrayList<String> Horaires =new ArrayList<>();
            JSONObject objs = Monuments.getJSONObject(i);
            Id =  objs.getInt(Constants.ID_MONUMENT);
            lat = objs.getDouble(Constants.LATITUDE);
            lng = objs.getDouble(Constants.LONGITUDE);
            name = objs.getString(Constants.NOM_MONUMENT);
            desc = objs.getString(Constants.DESCRIPTION_MONUMENT);
            infos = objs.getString(Constants.INFORMATIONS_SUPPLEMENTAIRE);
            if (objs.has(Constants.LIST_ACCESSIBILITES)) {
                JSONArray jsonObjs = objs.getJSONArray(Constants.LIST_ACCESSIBILITES);
                for (int j = 0; j < jsonObjs.length(); j++) {
                    JSONObject itemObj = jsonObjs.getJSONObject(j);
                    Accessibilite.add(itemObj.getString(Constants.UNE_ACCESSIBILITE));
                }
            }
            if (objs.has(Constants.HORAIRES)) {
                JSONArray jsonObjs = objs.getJSONArray(Constants.HORAIRES);
                for (int j = 0; j < jsonObjs.length(); j++) {
                    JSONObject itemObj = jsonObjs.getJSONObject(j);
                    Horaires.add(itemObj.getString(Constants.HORAIRE));
                }
            }
            this.monuments.put(Id, new Monument(Id, lat, lng, name, desc, infos, Accessibilite, Horaires));
        }

        for(int i=0 ; i< Parcours.length() ; i++) {
             JSONObject objs = Parcours.getJSONObject(i);
             Id = objs.getInt(Constants.ID_PARCOURS);
             name = objs.getString(Constants.NOM_PARCOURS);
             duree = objs.getLong(Constants.DUREE);
             desc = objs.getString(Constants.DESCRIPTION_PARCOURS);
            switch ( objs.getInt(Constants.EVALUATION)) {
                case 1:
                    eval = Evaluation.PASBON;
                    break;
                case 2:
                    eval = Evaluation.MOYEN;
                    break;
                case 3:
                    eval = Evaluation.BON;
                    break;
                case 4:
                    eval = Evaluation.TRESBON;
                    break;
                case 5:
                    eval = Evaluation.EXCELLENT;
                    break;
                default:
                    eval = Evaluation.PASBON;
                    break;
            }

            if (objs.has(Constants.LIST_MONUMENTS)) {
                JSONArray jsonObjs = objs.getJSONArray(Constants.LIST_MONUMENTS);
                tempsMonum = new ArrayList<>();
                for (int j = 0; j < jsonObjs.length(); j++) {
                    JSONObject itemObj = jsonObjs.getJSONObject(j);
                    tempsMonum.add(monuments.get(itemObj.getInt(Constants.UN_MONUMENT)));
                }
            }

            parcourses.put(Id, new ParcoursABC(Id, name, duree, eval, desc, tempsMonum));
        }

    }

    public Map<Integer, Monument> getMonuments() {
        return monuments;
    }

    public Map<Integer, ParcoursABC> getParcourses() {
        return parcourses;
    }

    public Parameters getParameters() {
        return parameters;
    }
}

