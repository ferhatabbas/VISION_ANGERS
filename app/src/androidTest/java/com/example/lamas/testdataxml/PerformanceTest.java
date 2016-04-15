package com.example.lamas.testdataxml;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Created by Maxence on 16-04-15.
 */
public class PerformanceTest extends ActivityInstrumentationTestCase2<MainActivityTest> {

    public long var = 1000;
    public static long var2 = 1000;
    public static final long var3 = 1000;

    public PerformanceTest() {
        super(MainActivityTest.class);
    }

    public void test_write() throws InterruptedException {
        MainActivityTest activity = getActivity();
        assertNotNull(activity);
        activity.write("Démarrage des tests de performance: \n");
        Thread.sleep(3000);
    }

    public void test_array_list() throws InterruptedException {
        MainActivityTest activity = getActivity();
        ArrayList<Integer> temp = new ArrayList<>();
        for(int i=0; i<100000; i++){
            temp.add(i);
        }

        int sum = 0;
        int size = temp.size();
        //Test 1

        long before, delay = 0;
        for (int j = 0; j < 1000; j++){
            before = System.currentTimeMillis();
            for(int i=0; i < temp.size(); i++){
                sum += temp.get(i);
            }
            delay += System.currentTimeMillis() - before;
        }
        double averagetime = delay / 1000;
        activity.write("Test 1 :" + averagetime + "\n");
        System.out.print("Test 1 :" + averagetime + " "+sum+"\n");
        //Test 2
        sum = 0;
        delay = 0;
        for (int j = 0; j < 1000; j++){
            before = System.currentTimeMillis();
            for(int i=0; i < size; i++){
                sum += temp.get(i);
            }
            delay += System.currentTimeMillis() - before;
        }
        averagetime = delay / 1000;
        activity.write("Test 2 :" + averagetime + "\n");
        System.out.print("Test 2 :" + averagetime + " "+sum+"\n");
        //Test 3
        sum = 0;
        delay = 0;
        for (int j = 0; j < 1000; j++){
            before = System.currentTimeMillis();
            for(int i : temp){
                sum += i;
            }
            delay += System.currentTimeMillis() - before;
        }
        averagetime = delay / 1000;
        activity.write("Test 3 :" + averagetime + "\n");
        System.out.print("Test 3 :" + averagetime + " "+sum+"\n");
        Thread.sleep(10000);
        activity.finish();
    }

    public void test_access_time() throws InterruptedException {
        MainActivityTest activity = getActivity();
        long temp=0, before, delay=0;
        for(int j=0; j<1000; j++){
            before = System.nanoTime();
            temp = var;
            //temp += 1;
            delay += System.nanoTime() - before;
        }
        double averagetime = delay / 1000;
        activity.write("Test 1 :" +averagetime+"\n");
        System.out.print("Test 1 :" + averagetime + " " + temp + "\n");

        delay =0;
        for(int j=0; j<1000; j++){
            before = System.nanoTime();
            temp = getVar();
            //temp += 1;
            delay += System.nanoTime() - before;
        }
        averagetime = delay / 1000;
        activity.write("Test 2 :" + averagetime + "\n");
        System.out.print("Test 2 :" + averagetime +" "+temp+ "\n");

        delay =0;
        for(int j=0; j<1000; j++){
            before = System.nanoTime();
            temp = var2;
            //temp += 1;
            delay += System.nanoTime() - before;
        }
        averagetime = delay / 1000;
        activity.write("Test 3 :" + averagetime + "\n");
        System.out.print("Test 3 :" + averagetime +" "+temp+ "\n");

        delay =0;
        for(int j=0; j<1000; j++){
            before = System.nanoTime();
            temp = var3;
            //temp += 1;
            delay += System.nanoTime() - before;
        }
        averagetime = delay / 1000;
        activity.write("Test 4 :" + averagetime + "\n");
        System.out.print("Test 4 :" + averagetime +" "+temp+ "\n");

        delay =0;
        for(int j=0; j<1000; j++){
            before = System.nanoTime();
            temp = getVar4();
            //temp += 1;
            delay += System.nanoTime() - before;
        }
        averagetime = delay / 1000;
        activity.write("Test 5 :" + averagetime + "\n");
        System.out.print("Test 5 :" + averagetime +" "+temp+ "\n");

        Thread.sleep(10000);
        activity.finish();
    }

    public long getVar(){
        return var;
    }

    public static long getVar4(){
        return 1000;
    }
}
