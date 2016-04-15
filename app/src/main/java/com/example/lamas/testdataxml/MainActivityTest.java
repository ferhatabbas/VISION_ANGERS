package com.example.lamas.testdataxml;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MainActivityTest extends Activity {

    public TextView textView;
    public String textToAppend;
    private Handler mHandler = new Handler();

    private Runnable addText = new Runnable() {
        public void run() {
            textView.append(textToAppend);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_test);
        textView = (TextView) findViewById(R.id.editTextTest);

    }

    public void write(String text){
        textToAppend = text;
        mHandler.post(addText);
    }

}
