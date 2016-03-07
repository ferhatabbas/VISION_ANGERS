package com.example.lamas.testdataxml;

/**
 * Created by rocj2405 on 2016-03-03.
 */
public class Information {
    private String typeInfo;
    private String information;

    public Information(String typeInfo, String information) {
        this.typeInfo = typeInfo;
        this.information = information;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public String getInformation() {
        return information;
    }
}
