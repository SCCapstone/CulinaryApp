/*
package com.github.CulinaryApp;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.github.CulinaryApp.views.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import androidx.test.ext.junit.rules.*;
import androidx.test.ext.junit.rules.ActivityScenerioRule; // to simulate activity launch
import static android.support.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.Iterables; // to simulate intent

@RunsWith(AndroidJUnit4.class)
public class BehaviorTests {
    String userInput1;
    String userInput2;
    String userInput3;
    String userInput4; // ..., ...

    /**
     * Simulate UI navigation using ActivityScenerioRule
     */

    /*First test simulates Login interactions, given an existing user

    @Rule
    ActivityScenerioRule rule = new ActivityScenerioRule<>(LoginActivity.class);

    private void enterInfo(String anEmail, String aPassword) {

        onView(R.id.email).perform(typeText(anEmail), closeSoftKeyboard());
        onView(R.id.password).perform(typeText(aPassword), closeSoftKeyboard());
    }

    public boolean nextActivitySuccesfullyLaunched(Class<CategoriesActivity> theNextActivity) {
        return assertTrue(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(theNextActivity);
    }

    @Test
    public void authenticatedUserLogsIn() {

        userInput1 = "email@gmail.com";
        userInput2 = "email@gmail";
        enterInfo(userInput1, userInput2);

        assertTrue(nextActivitySuccesfullyLaunched(CategoriesActivity.class));

    }


}
*/

package com.github.CulinaryApp;

import androidx.annotation.ContentView;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.github.CulinaryApp.views.LoginActivity;
//import static android.support.Espresso.onView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class BehaviorTests{

    @Rule
    public ActivityScenarioRule<LoginActivity> rule =
            new ActivityScenarioRule<>(LoginActivity.class);

    private String email = "default@email.com";
    private String password = "default";

    @Test
    public void launchedActivityDisplaysExpectedInitialState() {
        try(ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.moveToState(Lifecycle.State.CREATED);
            assert scenario.getState().isAtLeast(Lifecycle.State.CREATED);
        }
    }
    @Before
    public void setUp() throws Exception{

    }

    @Test
    public void testUnserInput(){

    }

    @After
    public void tearDown() throws Exception{

    }

}

