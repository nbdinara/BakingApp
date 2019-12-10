package com.example.bakingapp;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.CursorMatchers.withRowString;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    public static final String RECIPE_NAME = "Brownies";
    public static final String INGREDIENT_NAME = "unsalted butter";
    public static final String STEP_DESCRIPTION = "Melt butter and bittersweet chocolate.";


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Clicks on a GridView item and checks it opens up the OrderActivity with the correct details.
     */
    @Test
    public void clickGridViewItem_OpensOrderActivity() {


        // Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific
        // gridview item and clicks it.
        //onData(anything()).inAdapterView(withId(R.id.rv_recipes)).atPosition(0).perform(click());
        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));


        // Checks that the OrderActivity opens with the correct tea name displayed
        onView(withId(R.id.tv_recipe_name_header)).check(matches(withText(RECIPE_NAME)));

        onData(anything()).inAdapterView(withId(R.id.lv_ingredients)).atPosition(1)
                .onChildView(withId(R.id.tv_ingredient_name)).check(matches(withText(INGREDIENT_NAME)));

        onData(anything()).inAdapterView(withId(R.id.lv_steps)).atPosition(2).check(matches(withText(STEP_DESCRIPTION)));

    }

}
