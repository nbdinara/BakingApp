package com.example.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.bakingapp.model.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class StepsFragmentTest {


    private List<Step> steps = new ArrayList<>();
    private final int id = 2;


    private final String STEP_FULL_DESCRIPTION = "2. Melt the butter and bittersweet chocolate together in a microwave or a double boiler. If microwaving, heat for 30 seconds at a time, removing bowl and stirring ingredients in between.";
    private final String NEXT_STEP_FULL_DESCRIPTION = "3. Mix both sugars into the melted chocolate in a large mixing bowl until mixture is smooth and uniform.";
    private final String PREVIOUS_STEP_FULL_DESCRIPTION = "1. Preheat the oven to 350?F. Butter the bottom and sides of a 9\\\"x13\\\" pan.";

    @Rule
    public ActivityTestRule<StepDetailsActivity> mActivityTestRule =
            new ActivityTestRule<>(StepDetailsActivity.class, false, false);

    private Activity launchedActivity;

    @Before
    public void setup() {

        steps.add(new Step(7, 0, "Recipe Introduction", "Recipe Introduction", "", ""));
        steps.add(new Step(8, 1, "Starting prep", "1. Preheat the oven to 350?F. Butter the bottom and sides of a 9\\\"x13\\\" pan.", "", ""));
        steps.add(new Step(9, 2, "Melt butter and bittersweet chocolate.", "2. Melt the butter and bittersweet chocolate together in a microwave or a double boiler. If microwaving, heat for 30 seconds at a time, removing bowl and stirring ingredients in between.", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc43_1-melt-choclate-chips-and-butter-brownies/1-melt-choclate-chips-and-butter-brownies.mp4", ""));
        steps.add(new Step(10, 3, "Add sugars to wet mixture.", "3. Mix both sugars into the melted chocolate in a large mixing bowl until mixture is smooth and uniform.", "", ""));
        steps.add(new Step(11, 4, "Mix together dry ingredients.", "4. Sift together the flour, cocoa, and salt in a small bowl and whisk until mixture is uniform and no clumps remain. ", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc9e_4-sift-flower-add-coco-powder-salt-brownies/4-sift-flower-add-coco-powder-salt-brownies.mp4", ""));
        steps.add(new Step(12, 5, "Add eggs.", "5. Crack 3 eggs into the chocolate mixture and carefully fold them in. Crack the other 2 eggs in and carefully fold them in. Fold in the vanilla.", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc62_2-mix-egss-with-choclate-butter-brownies/2-mix-egss-with-choclate-butter-brownies.mp4", ""));
        steps.add(new Step(13, 6, "Add dry mixture to wet mixture.", "6. Dump half of flour mixture into chocolate mixture and carefully fold in, just until no streaks remain. Repeat with the rest of the flour mixture. Fold in the chocolate chips.", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdcc8_5-mix-wet-and-cry-batter-together-brownies/5-mix-wet-and-cry-batter-together-brownies.mp4", ""));
        steps.add(new Step(14, 7, "Add batter to pan.", "7. Pour the batter into the prepared pan and bake for 30 minutes.", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdcf4_8-put-brownies-in-oven-to-bake-brownies/8-put-brownies-in-oven-to-bake-brownies.mp4", ""));
        steps.add(new Step(15, 8, "Remove pan from oven.", "8. Remove the pan from the oven and let cool until room temperature. If you want to speed this up, you can feel free to put the pan in a freezer for a bit.", "", ""));
        steps.add(new Step(16, 9, "Cut and serve.", "9. Cut and serve.", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdcf9_9-final-product-brownies/9-final-product-brownies.mp4", ""));

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("steps", (ArrayList<? extends Parcelable>) steps);
        intent.putExtra("id", 2);
        launchedActivity = mActivityTestRule.launchActivity(intent);
    }

    // public IntentsTestRule<RecipeDetailsActivity> mActivityRule = new IntentsTestRule<>(
    //       RecipeDetailsActivity.class);

    /**
     * Clicks on a Steps listview item and checks it opens up the Step details fragment with
     * the correct details.
     */
    @Test
    public void clickStepsListViewItem_OpensStepDetailsActivity() {


        // onView(withId(R.id.recipe_container)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_step_full_description)).check(matches(isDisplayed()));
        //onData(anything()).inAdapterView(withId(R.id.lv_steps)).atPosition(0).perform(click());

        //intended(allOf(hasComponent(StepDetailsFragment.class.getName()), (hasExtra("steps", steps)), hasExtra("id", id)));
        // Checks that the OrderActivity opens with the correct tea name displayed

        onView(withId(R.id.tv_step_full_description)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_step_full_description)).check(matches(withText(STEP_FULL_DESCRIPTION)));
    }

    @Test
    public void clickNextButton_DisplaysNextStepDetails() {


        // onView(withId(R.id.recipe_container)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_next)).check(matches(isDisplayed()));
        //onData(anything()).inAdapterView(withId(R.id.lv_steps)).atPosition(0).perform(click());

        //intended(allOf(hasComponent(StepDetailsFragment.class.getName()), (hasExtra("steps", steps)), hasExtra("id", id)));
        // Checks that the OrderActivity opens with the correct tea name displayed

        onView(withId(R.id.btn_next)).perform(click());
        onView(withId(R.id.tv_step_full_description)).check(matches(withText(NEXT_STEP_FULL_DESCRIPTION)));
    }

    //only for mobile

    @Test
    public void clickPreviousButton_DisplaysNextStepDetails() {


        // onView(withId(R.id.recipe_container)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_previous)).check(matches(isDisplayed()));
        //onData(anything()).inAdapterView(withId(R.id.lv_steps)).atPosition(0).perform(click());

        //intended(allOf(hasComponent(StepDetailsFragment.class.getName()), (hasExtra("steps", steps)), hasExtra("id", id)));
        // Checks that the OrderActivity opens with the correct tea name displayed

        onView(withId(R.id.btn_previous)).perform(click());
        onView(withId(R.id.tv_step_full_description)).check(matches(withText(PREVIOUS_STEP_FULL_DESCRIPTION)));
    }

}
