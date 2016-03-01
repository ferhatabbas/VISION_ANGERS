package com.example.lamas.testdataxml;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    private Data instance;
    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        /*String flat = null;
        StringBuffer sb = new StringBuffer();
        BufferedReader brM = null;
        BufferedReader brP = null;


        try {
            brM = new BufferedReader(new InputStreamReader(getAssets().open(
                    filePathM)));
            brP = new BufferedReader(new InputStreamReader(getAssets().open(
                    filePathP)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            instance =Data.getInstance(brM,brP);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        instance = Data.getInstance(getApplicationContext());

        instance.getMonuments();

        instance.getParcourses();
        System.out.print("");

        //        ((EditText) findViewById(R.id.editText)).setText(flat);

    }


}