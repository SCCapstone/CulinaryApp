package com.github.CulinaryApp;

import android.app.Instrumentation;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.CulinaryApp.views.CategoriesActivity;
import com.github.CulinaryApp.views.RecipeInstructionsActivity;

import androidx.activity.result.ActivityResult;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
//import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.util.Objects;

import static org.junit.Assert.*;


//@RunWith(AndroidJUnit4.class)
@SmallTest
public class LikesTests {

    @BeforeClass
    public static void setupClass(){
//        throw new RuntimeException("no tests");
    }

    @Rule
    public ActivityScenarioRule<CategoriesActivity> rule = new ActivityScenarioRule<>(CategoriesActivity.class);

    @Test
    public void likeSuccessTest(){
        rule.getScenario().moveToState(Lifecycle.State.RESUMED);

        ActivityScenario<CategoriesActivity> scene = ActivityScenario.launch(CategoriesActivity.class);

        scene.onActivity( activity -> {
            activity.findViewById(R.id.toolbarFavs).performClick();
        });

        scene.getResult();
        assertEquals(Lifecycle.State.DESTROYED, scene.getState());
    }

    /*@Test
    public void testTest(){
        rule.getScenario().moveToState(Lifecycle.State.RESUMED);

        ActivityScenario<CategoriesActivity> scene = ActivityScenario.launch(CategoriesActivity.class);
        scene.onActivity( activity -> {
            assertEquals(1,1);
        });

    }*/

    /*@Test
    public void editBioPopupTest(){
        rule.getScenario().moveToState(Lifecycle.State.STARTED);

        ActivityScenario<ProfileActivity> scene = ActivityScenario.launch(ProfileActivity.class);
        scene.onActivity( activity -> {
            Button clipboardButton = activity.findViewById(R.id.toolbarFavs);
            FragmentContainerView clipboardHolder = activity.findViewById(R.id.clipboardFragmentHolder);

            clipboardButton.performClick();
            assertEquals(clipboardHolder.getVisibility(), View.VISIBLE);
        });

    }*/

//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.github.CulinaryApp", appContext.getPackageName());
//    }
}