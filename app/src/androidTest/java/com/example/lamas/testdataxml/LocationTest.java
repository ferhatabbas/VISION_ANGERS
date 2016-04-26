package com.example.lamas.testdataxml;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;

import com.example.lamas.testdataxml.data.Data;
import com.example.lamas.testdataxml.list_activities.InformationActivity;

import java.util.ArrayList;


/**
 * Created by Maxence on 16-03-16.
 */
public class LocationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private double step_one_lat = 45.3799629, step_one_long = -71.924;
    private double step_two_lat = 45.3799629, step_two_long = -71.9242;
    private double step_three_lat = 45.3799629, step_three_long = -71.92435;
    private double cafetaria_science_lat = 45.3799629, cafetaria_science_long = -71.9250;
    private double step_five_lat = 45.3796, step_five_long = -71.9250;
    private double step_six_lat = 45.3792, step_six_long = -71.9250;
    private double step_seven_lat = 45.3788, step_seven_long = -71.9250;
    private double step_eight_lat = 45.3785, step_eight_long = -71.9250;
    private double step_nine_lat = 45.3783, step_nine_long = -71.92535;
    private double genie_lat = 45.3783, genie_long = -71.9259;
    private double step_eleven_lat = 45.378, step_eleven_long = -71.9259;
    private double step_twelve_lat = 45.3779, step_twelve_long = -71.9259;
    private double step_thirteen_lat = 45.3777, step_thirteen_long = -71.9259;
    private double step_fourteen_lat = 45.3775, step_fourteen_long = -71.9259;
    private double step_fifteen_lat = 45.3773, step_fifteen_long = -71.9259;
    private double admin_lat = 45.377, admin_long = -71.9255;

    private class Step {
        public double longitude, latitude;
        public boolean is_POI = false;
        public int id = 0;
        public Step(double latitude, double longitude){
            this.longitude = longitude;
            this.latitude = latitude;
        }
        public Step(double latitude, double longitude, boolean is_POI, int id){
            this.is_POI = is_POI;
            this.longitude = longitude;
            this.latitude = latitude;
            this.id = id;
        }
    }

    public ArrayList<Step> long_course = new ArrayList<>();
    public ArrayList<Step> short_course = new ArrayList<>();


    public void setUp(){
        long_course.add(new Step(step_one_lat, step_one_long));
        long_course.add(new Step(step_two_lat, step_two_long));
        long_course.add(new Step(step_three_lat, step_three_long));
        long_course.add(new Step(cafetaria_science_lat, cafetaria_science_long, true, 6));
        long_course.add(new Step(step_five_lat, step_five_long));
        long_course.add(new Step(step_six_lat, step_six_long));
        long_course.add(new Step(step_seven_lat, step_seven_long));
        long_course.add(new Step(step_eight_lat, step_eight_long));
        long_course.add(new Step(step_nine_lat, step_nine_long));
        long_course.add(new Step(genie_lat, genie_long, true, 7));
        long_course.add(new Step(step_eleven_lat, step_eleven_long));
        long_course.add(new Step(step_twelve_lat, step_twelve_long));
        long_course.add(new Step(step_thirteen_lat, step_thirteen_long));
        long_course.add(new Step(step_fourteen_lat, step_fourteen_long));
        long_course.add(new Step(step_fifteen_lat, step_fifteen_long));
        long_course.add(new Step(admin_lat, admin_long, true, 8));

        short_course.add(new Step(step_one_lat, step_one_long));
        short_course.add(new Step(step_two_lat, step_two_long));
        short_course.add(new Step(step_three_lat, step_three_long));
        short_course.add(new Step(cafetaria_science_lat, cafetaria_science_long, true, 6));
        short_course.add(new Step(step_five_lat, step_five_long));
        short_course.add(new Step(step_six_lat, step_six_long));
    }

    public void tearDown(){
        getActivity().finish();
    }


    public LocationTest() {
        super(MainActivity.class);
    }

    public void testActivityExists() {
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }

    public void testMockLocation() throws InterruptedException {
        double latitude = 45.35, longitude = 45.25;
        MainActivity activity = getActivity();
        activity.pushNewLocation(latitude, longitude, 10.0f);
        Thread.sleep(Constants.REQUEST_LOCATION_MANAGER_TIME);
        assertEquals(activity.getInstantMarker().getPosition().getLatitude(), latitude);
        assertEquals(activity.getInstantMarker().getPosition().getLongitude(), longitude);
    }


    public void test_long_course() throws InterruptedException {
        MainActivity activity = getActivity();
        Instrumentation.ActivityMonitor activityMonitor = new Instrumentation.ActivityMonitor(InformationActivity.class.getName(),
                null, false);
        getInstrumentation().addMonitor(activityMonitor);
        int wait_time_between_step = 6000;
        float accuracy = 10.0f;
        for (Step step: long_course){
            activity.pushNewLocation(step.latitude, step.longitude, accuracy);
            Thread.sleep(wait_time_between_step);
        }

        assertEquals(activityMonitor.getHits(), 3);
    }

    public void test_long_course_2() throws InterruptedException {
        MainActivity activity = getActivity();
        Instrumentation.ActivityMonitor activityMonitor = new Instrumentation.ActivityMonitor(InformationActivity.class.getName(),
                null, false);
        getInstrumentation().addMonitor(activityMonitor);
        int i=0;
        int wait_time_between_step = Constants.REQUEST_LOCATION_MANAGER_TIME;
        float accuracy = 10.0f;
        for (Step step: long_course){
            if(step.is_POI){
                i++;
            }
            activity.pushNewLocation(step.latitude, step.longitude, accuracy);
            Thread.sleep(wait_time_between_step);
            assertEquals(i, activityMonitor.getHits());
        }

        assertEquals(activityMonitor.getHits(), 3);
    }

    public void test_display_right_POI_activity() throws InterruptedException {
        MainActivity activity = getActivity();
        Instrumentation.ActivityMonitor activityMonitor = new Instrumentation.ActivityMonitor(InformationActivity.class.getName(),
                null, false);
        getInstrumentation().addMonitor(activityMonitor);
        float accuracy = 10.0f;
        for (Step step : short_course) {
            activity.pushNewLocation(step.latitude, step.longitude, accuracy);
            if (!step.is_POI) {
                Thread.sleep(Constants.REQUEST_LOCATION_MANAGER_TIME);
            }
            else {
                Activity informationsActivity = activityMonitor.waitForActivityWithTimeout(12000);
                assertNotNull("Activity was not started", informationsActivity);
                assertEquals(step.id, informationsActivity.getIntent().getExtras().getInt("id"));
                informationsActivity.finish();
            }

        }
    }

    public void test_long_course_poor_accuracy() throws InterruptedException {
        MainActivity activity = getActivity();
        Instrumentation.ActivityMonitor activityMonitor = new Instrumentation.ActivityMonitor(InformationActivity.class.getName(),
                null, false);
        getInstrumentation().addMonitor(activityMonitor);
        float accuracy = 300.0f;
        for (Step step: long_course){
            activity.pushNewLocation(step.latitude, step.longitude, accuracy);
            Thread.sleep(Constants.REQUEST_LOCATION_MANAGER_TIME);
        }

        assertEquals(0, activityMonitor.getHits());
    }

    public void test_long_course_changing_accuracy() throws InterruptedException {
        MainActivity activity = getActivity();
        Instrumentation.ActivityMonitor activityMonitor = new Instrumentation.ActivityMonitor(InformationActivity.class.getName(),
                null, false);
        getInstrumentation().addMonitor(activityMonitor);
        float accuracy = 100.0f;
        for(int i =0;i<long_course.size();i++){
            if(i == 7){
                accuracy=10.0f;
            }
            activity.pushNewLocation(long_course.get(i).latitude, long_course.get(i).longitude, accuracy);
            Thread.sleep(Constants.REQUEST_LOCATION_MANAGER_TIME);
        }

        //assertEquals(2, activityMonitor.getHits());
    }

    public void test_poor_GPS_reception_alert() throws InterruptedException {
        MainActivity activity = getActivity();
        int wait_time_between_step = 6000;
        float accuracy = 100.0f;
        for(int i =0;i<8;i++){
            activity.pushNewLocation(long_course.get(i).latitude, long_course.get(i).longitude, accuracy);
            Thread.sleep(wait_time_between_step);
            assertTrue(activity.getWaitForGPSDialog().isShowing());
        }
        accuracy = 10.0f;
        for(int i =8;i<long_course.size();i++){
            activity.pushNewLocation(long_course.get(i).latitude, long_course.get(i).longitude, accuracy);
            Thread.sleep(wait_time_between_step);
            assertFalse(activity.getWaitForGPSDialog().isShowing());
        }
    }

    public void test_poor_GPS_reception_alert_reactivity() throws InterruptedException {
        MainActivity activity = getActivity();
        int wait_time_between_step = Constants.REQUEST_LOCATION_MANAGER_TIME;
        float accuracy = 100.0f;
        for(int i =0;i<8;i++){
            activity.pushNewLocation(long_course.get(i).latitude, long_course.get(i).longitude, accuracy);
            Thread.sleep(wait_time_between_step);
            assertTrue(activity.getWaitForGPSDialog().isShowing());
        }
    }

    public void test_poor_GPS_reception_alert_reactivity_2() throws InterruptedException {
        MainActivity activity = getActivity();
        int wait_time_between_step = Constants.REQUEST_LOCATION_MANAGER_TIME;
        float accuracy = 10.0f;
        for(int i =0;i<4;i++){
            activity.pushNewLocation(long_course.get(i).latitude, long_course.get(i).longitude, accuracy);
            Thread.sleep(wait_time_between_step);
            assertFalse(activity.getWaitForGPSDialog().isShowing());
        }
        accuracy = 100.0f;
        for(int i =4;i<12;i++){
            activity.pushNewLocation(long_course.get(i).latitude, long_course.get(i).longitude, accuracy);
            Thread.sleep(wait_time_between_step);
            assertTrue(activity.getWaitForGPSDialog().isShowing());
        }
        accuracy = 10.0f;
        for(int i =12;i<long_course.size();i++){
            activity.pushNewLocation(long_course.get(i).latitude, long_course.get(i).longitude, accuracy);
            Thread.sleep(wait_time_between_step);
            assertFalse(activity.getWaitForGPSDialog().isShowing());
        }
    }

    public void test_late_GPS_reception_alert_reactivity() throws InterruptedException {
        MainActivity activity = getActivity();
        float accuracy = 10.0f;
        activity.pushNewLocation(long_course.get(1).latitude, long_course.get(1).longitude, accuracy);
        Thread.sleep(10000);
        assertTrue(activity.getWaitForGPSDialog().isShowing());

    }

    public void test_safety_check() throws InterruptedException {
        MainActivity activity = getActivity();
        float accuracy = 10.0f;

        for(int i=0; i<7; i++){
            activity.pushNewLocation(step_one_lat, step_one_long, accuracy);
            Thread.sleep(Constants.SAFETY_CHECK_TIMEOUT+1000);
        }
        assertTrue(activity.getSafetyCheckDialog().isShowing());
    }

    public void test_shared_preferences_vs_singleton(){
        MainActivity activity = getActivity();
        Data data = Data.getInstance(activity);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Name", "Angers");
        editor.commit();

        long startTime = System.nanoTime();
        int temp = data.getParameters().getCouleurTexte();
        System.out.println(temp);
        long difference = System.nanoTime() - startTime;
        System.out.println("Singleton: "+difference+" ns");

        startTime = System.nanoTime();
        String temp2 = getActivity().getPreferences(Context.MODE_PRIVATE).getString("Name", "ok");
        System.out.println(temp2);
        difference = System.nanoTime() - startTime;
        System.out.println("SharedPref: " + difference + " ns");
    }

}
