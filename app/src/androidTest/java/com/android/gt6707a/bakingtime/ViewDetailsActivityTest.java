package com.android.gt6707a.bakingtime;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.gt6707a.bakingtime.entity.Ingredient;
import com.android.gt6707a.bakingtime.entity.Recipe;
import com.android.gt6707a.bakingtime.entity.Step;
import com.android.gt6707a.bakingtime.viewDetails.ViewDetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewDetailsActivityTest {
  final String TEST_DESCRIPTION = "This is a test description";

  @Rule
  public ActivityTestRule<ViewDetailsActivity> mActivityTestRule =
      new ActivityTestRule<ViewDetailsActivity>(ViewDetailsActivity.class) {
        @Override
        protected Intent getActivityIntent() {
          Recipe recipe = new Recipe();
          recipe.setName("Test Recipe");
          recipe.setIngredients(new ArrayList<Ingredient>());

          List<Step> steps = new ArrayList<>();
          Step step1 = new Step();
          step1.setDescription(TEST_DESCRIPTION);
          steps.add(step1);
          recipe.setSteps(steps);

          Intent i = new Intent();
          i.putExtra("recipe", recipe);
          return i;
        }
      };

  @Test
  public void navigatesToViewStepDetailsActivityWhenClicked() {
    onView(withId(R.id.steps_recycler_view))
        .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.description_text_view)).check(matches(withText(TEST_DESCRIPTION)));
  }
}
