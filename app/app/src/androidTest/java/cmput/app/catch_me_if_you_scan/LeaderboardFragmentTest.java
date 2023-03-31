package cmput.app.catch_me_if_you_scan;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

import android.app.Activity;
import android.view.View;
import android.widget.SearchView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//Set Query for Search View
//Author: MiguelSlv
//Publish Date: Nov 30, 2017
//License: CC BY-SA
//URL: https://stackoverflow.com/questions/48037060/how-to-type-text-on-a-searchview-using-espresso

@RunWith(JUnit4.class)
public class LeaderboardFragmentTest {

    public ActivityScenario<MainActivity> scenario;
    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        scenario = scenarioRule.getScenario();
    }

    /**
     * Tests if the app can navigate to the leaderboard activity using the navigation bar.
     */
    @Test
    public void switchLeaderboardUsingNavBar() throws InterruptedException {
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.leaderboard_nav)).perform(click());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.fragment_leaderboard)).check(matches(isDisplayed()));
    }

    /**
     * Tests if the leaderboard is not displayed in the activity layout after the activity is launched.
     */
    @Test
    public void leaderBoardNotInViewAfterLaunch(){
        scenario.onActivity(activity -> {
        });

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.fragment_leaderboard)).check(doesNotExist());
    }



    @Test
    public void checkFilter() {
        scenario.onActivity(activity -> {
        });
        onView(withId(R.id.leaderboard_nav)).perform(click());
        onView(withId(R.id.search_bar)).perform(typeSearchViewText("Mr."));
        onData(anything()).inAdapterView(withId(R.id.leaderboard_list_view)).atPosition(0)
                .onChildView(withId(R.id.username_text_view))
                .check(matches(withText("Mr. Snowden")));
        onView(withId(R.id.search_bar)).perform(typeSearchViewText("Krist"));
        onData(anything()).inAdapterView(withId(R.id.leaderboard_list_view)).atPosition(0)
                .onChildView(withId(R.id.username_text_view))
                .check(matches(withText("Kristen")));
    }


    /**
     * This custom function is used to type in the search view
     * @param text
     * @return
     */
    public static ViewAction typeSearchViewText(final String text){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView) view).setQuery(text,true);
            }
        };
    }
}
