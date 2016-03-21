package com.example.lamas.testdataxml;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;
import android.test.ApplicationTestCase;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.lamas.testdataxml.data.Data;
import com.example.lamas.testdataxml.data.Monument;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.*;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTestDATA {
    private Data data;
    private Monument obj;
    private Context context;
    private Monument monum;

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public void setUp() throws Exception {
        context=mActivityRule.getActivity().getApplicationContext();
        data = Data.getInstance(context);
        assertNotNull(data);
        monum= new Monument(2,45.3795022, -71.931661, "Faculté des lettres et sciences humaines", "La Faculté des lettres et sciences humaines offre 14 programmes de formations initiales, dont quatre suivant le régime coopératif, pouvant mener à des carrières en politique, en géomatique, en musique, en psychologie, en communication et en marketing, en littérature, en travail social, en traduction, en histoire, et bien d’autres.","super naze leur site",null, new ArrayList<String>() {{
            add("lundi 08:00-12:00 13:30-16:00");
            add("mardi 08:00-12:00 13:30-16:00");
            add("mercredi 08:00-12:00 13:30-16:00");
            add("jeudi 08:00-12:00 13:30-16:00");
            add("vendredi 08:00-12:00 13:30-16:00");
        }}) ;
    }
    @Test
    public void nombreMonuments(){
        assertEquals(data.getMonuments().size(), 10);
    }
    @Test
    public void nombreParcours(){
        assertEquals(data.getParcourses().size(), 4);
    }
    @Test
    public void monumentExiste(){
        assertNotNull(monum);
        assertFalse(data.getMonuments().containsValue(monum));
    }

    @Test
    public void monumentExisteDansParcours(){
        //  assertNotNull(monum);
        //  assertFalse(data.getParcourses().get(1).getMonuments().equals(monum));
        //  assertFalse(data.getParcourses().get(2).getMonuments().equals(monum));
        //  assertFalse(data.getParcourses().get(3).getMonuments().equals(monum));
        assertFalse(data.getParcourses().get(4).getMonuments().equals(monum));
    }
}