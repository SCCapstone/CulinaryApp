package com.github.CulinaryApp;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
//import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.SmallTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;


//@RunWith(AndroidJUnit4.class)
@SmallTest
public class ProfileActivityInstrumentedTest {

    @BeforeClass
    public static void setupClass(){
//        throw new RuntimeException("no tests");
    }

    @Rule
    public ActivityScenarioRule<ProfileActivity> rule = new ActivityScenarioRule<>(ProfileActivity.class);

    @Test
    public void editBioPopupTest(){
        rule.getScenario().moveToState(Lifecycle.State.STARTED);

        ActivityScenario<ProfileActivity> scene = ActivityScenario.launch(ProfileActivity.class);
        scene.onActivity( activity -> {
            Button clipboardButton = activity.findViewById(R.id.toolbarFavs);
            FragmentContainerView clipboardHolder = activity.findViewById(R.id.clipboardFragmentHolder);

            clipboardButton.performClick();
            assertEquals(clipboardHolder.getVisibility(), View.VISIBLE);
        });

    }

//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.github.CulinaryApp", appContext.getPackageName());
//    }
}