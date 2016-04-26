package com.example.lamas.testdataxml;

/**
 * Created by abbf2501 on 2016-03-01.
 */
public class Constants {

  public static final Boolean ALLOW_MOCK_LOCATION = false;
  public static final Boolean DEBUG_MODE = true;

  /* nom du fichier DATA*/
  public static final String FILE_PATH = "Data";

  public static final int MIN_ACCURACY = 11;
  public static final int WAIT_FOR_GPS_TIMEOUT = 4500;
  public static final int SAFETY_CHECK_TIMEOUT = 5000;
  public static final int REQUEST_LOCATION_MANAGER_TIME = 5000;

  public static final int POI_NOTIFICATION_ID = 0;
  public static final int CHECK_GPS_NOTIFICATION_ID = 1;
  public static final int SAFETYCHECK_NOTIFICATION_ID = 2;

  public static final String ACTION_FILTER = "com.example.lamas.testdataxml.ProximityReceiver";

  public static int MSG_SHOW_GPS_ALERT = 0;
  public static int MSG_DISMISS_GPS_ALERT = 1;
  public static int MSG_SHOW_LOST_ALERT = 2;

  /*les balises des monuments */
  public static final String BALISE_MONUMENTS= "Monuments";
  public static final String ID_MONUMENT = "Id";
  public static final String RADIUS = "Radius";
  public static final String NOM_MONUMENT = "Name";
  public static final String LATITUDE = "Lat";
  public static final String LONGITUDE = "Lng";
  public static final String DESCRIPTION_MONUMENT = "Desc";
  public static final String INFORMATIONS_SUPPLEMENTAIRE = "Informations";
  public static final String LIST_ACCESSIBILITES="Accessibilites";
  public static final String UNE_ACCESSIBILITE="Accessibilite";
  public static final String HORAIRES="Horaires";
  public static final String HORAIRE="Horaire";

    /* les balises des parcours*/
    public static final String BALISE_PARCOURS= "Parcours";
    public static final String DESCRIPTION_PARCOURS = "Description";
    public static final String ID_PARCOURS = "Id";
    public static final String NOM_PARCOURS = "Name";
    public static final String DUREE = "Duree";
    public static final String EVALUATION = "Evalutation";
    public static final String LIST_MONUMENTS = "Monuments";
    public static final String UN_MONUMENT = "Id_monument";
}
