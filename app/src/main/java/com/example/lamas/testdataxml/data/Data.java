package com.example.lamas.testdataxml.data;

import android.content.Context;

import com.example.lamas.testdataxml.Constants;

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

        int id;
        Evaluation eval;

        ArrayList<String> accessibilites;
        ArrayList<String> horaires;
        ArrayList<Monument> monumentsParcours;

        JSONArray monuments = obj.getJSONArray(Constants.BALISE_MONUMENTS);
        JSONArray parcours = obj.getJSONArray(Constants.BALISE_PARCOURS);


        for(int i=0 ; i< monuments.length() ; i++) {
            accessibilites =new ArrayList<>();
            horaires =new ArrayList<>();
            JSONObject monument = monuments.getJSONObject(i);
            id =  monument.getInt(Constants.ID_MONUMENT);

            if (monument.has(Constants.LIST_ACCESSIBILITES)) {
                JSONArray jsonAccessibilites = monument.getJSONArray(Constants.LIST_ACCESSIBILITES);
                for (int j = 0; j < jsonAccessibilites.length(); j++) {
                    accessibilites.add(jsonAccessibilites.getJSONObject(j).getString(Constants.UNE_ACCESSIBILITE));
                }
            }
            if (monument.has(Constants.HORAIRES)) {
                JSONArray jsonObjs = monument.getJSONArray(Constants.HORAIRES);
                for (int j = 0; j < jsonObjs.length(); j++) {
                    horaires.add(jsonObjs.getJSONObject(j).getString(Constants.HORAIRE));
                }
            }
            this.monuments.put(id, new Monument(id, monument.getInt(Constants.RADIUS),
                    monument.getDouble(Constants.LATITUDE),
                    monument.getDouble(Constants.LONGITUDE),
                    monument.getString(Constants.NOM_MONUMENT),
                    monument.getString(Constants.DESCRIPTION_MONUMENT),
                    monument.getString(Constants.INFORMATIONS_SUPPLEMENTAIRE),
                    accessibilites, horaires));
        }

        for(int i=0 ; i< parcours.length() ; i++) {
             JSONObject objs = parcours.getJSONObject(i);
             id = objs.getInt(Constants.ID_PARCOURS);
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

            monumentsParcours = new ArrayList<>();
            if (objs.has(Constants.LIST_MONUMENTS)) {
                JSONArray monumentsArray = objs.getJSONArray(Constants.LIST_MONUMENTS);
                for (int j = 0; j < monumentsArray.length(); j++) {
                    JSONObject itemObj = monumentsArray.getJSONObject(j);
                    monumentsParcours.add(this.monuments.get(itemObj.getInt(Constants.UN_MONUMENT)));
                }
            }

            parcourses.put(id, new ParcoursABC(id, objs.getString(Constants.NOM_PARCOURS),
                    objs.getLong(Constants.DUREE), eval,
                    objs.getString(Constants.DESCRIPTION_PARCOURS), monumentsParcours));
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

