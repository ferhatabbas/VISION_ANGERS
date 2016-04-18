package com.example.lamas.testdataxml.data;

import android.graphics.Color;
import android.graphics.Typeface;

import org.osmdroid.ResourceProxy;

/**
 * Created by lang2511 on 2016-03-02.
 */
public class Parameters {
    private int taillePolice = 10;
    private int couleurBackground = -1;
    private int couleurTexte = -16777216;
    private Typeface typeface = null;
    private boolean googlemaps= true;


    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public int getCouleurBackground() {
        return couleurBackground;
    }

    public void setCouleurBackground(int couleurBackground) {
        this.couleurBackground = couleurBackground;
    }


    public int getTaillePolice() {
        return taillePolice;
    }

    public void setTaillePolice(int taillePolice) {
        this.taillePolice = taillePolice;
    }


    public int getCouleurTexte() {
        return couleurTexte;
    }

    public void setCouleurTexte(int couleurTexte) {
        this.couleurTexte = couleurTexte;
    }
    public boolean isGooglemaps() {
        return googlemaps;
    }

    public void setGooglemaps(boolean googlemaps) {
        this.googlemaps = googlemaps;
    }



}


