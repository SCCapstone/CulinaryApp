package com.github.CulinaryApp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.github.CulinaryApp.views.CategoriesActivity;
import com.github.CulinaryApp.views.RecipeInstructionsActivity;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
//import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;


//@RunWith(AndroidJUnit4.class)
@SmallTest
public class NavigationTests {

    @BeforeClass
    public static void setupClass(){
//        throw new RuntimeException("no tests");
    }


    @Rule
    public ActivityScenarioRule<CategoriesActivity> categoryRule = new ActivityScenarioRule<>(CategoriesActivity.class);


    @Test
    public void recipeClickTest(){
        categoryRule.getScenario().moveToState(Lifecycle.State.CREATED);

        ActivityScenario<CategoriesActivity> scene = ActivityScenario.launch(CategoriesActivity.class);
        scene.moveToState(Lifecycle.State.RESUMED);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        scene.onActivity(activity ->{

            RecyclerView rec = activity.findViewById(R.id.recyclerView);
            rec.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.Recipe1).performClick();


        });

        //CREATED is called right as OnPause is called, meaning the activity was left
        assertEquals(Lifecycle.State.STARTED, scene.getState());

        scene.close();
        categoryRule.getScenario().close();
    }

    @Test
    public void CategoriesToFavoritesNavigationSuccessTest(){
        categoryRule.getScenario().moveToState(Lifecycle.State.RESUMED);

        ActivityScenario<CategoriesActivity> scene = ActivityScenario.launch(CategoriesActivity.class);

        scene.onActivity( activity -> {
            activity.findViewById(R.id.toolbarFavs).performClick();
        });

        //CREATED is called right as OnPause is called, meaning the activity was left
        assertEquals(Lifecycle.State.STARTED, scene.getState());

        scene.close();
        categoryRule.getScenario().close();
    }

    @Test
    public void CategoriesToProfileNavigationSuccessTest(){
        categoryRule.getScenario().moveToState(Lifecycle.State.RESUMED);

        ActivityScenario<CategoriesActivity> scene = ActivityScenario.launch(CategoriesActivity.class);

        scene.onActivity( activity -> {
            activity.findViewById(R.id.toolbarProfile).performClick();
        });

        //CREATED is called right as OnPause is called, meaning the activity was left
        assertEquals(Lifecycle.State.STARTED, scene.getState());

        scene.close();
        categoryRule.getScenario().close();
    }


//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.github.CulinaryApp", appContext.getPackageName());
//    }
}