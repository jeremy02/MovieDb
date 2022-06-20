package com.MovieDb.app.ui.main;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.MovieDb.app.ui.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.MovieDb.app.core.utils.EspressoIdlingResource;
import com.MovieDb.app.main.MainActivity;
import com.ariefzuhri.movee.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainActivityTest {

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource());
    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource());
    }

    @Test
    public void loadMovies() {
        onView(withId(R.id.menu_movie)).perform(click());
        onView(allOf(withId(R.id.rv_horiz), isDescendantOfA(withId(R.id.fragment_movie))))
                .check(withItemCount(equalTo(20)));
        onView(allOf(withId(R.id.rv_vert), isDisplayed()))
                .check(withItemCount(equalTo(20)));
    }

    @Test
    public void loadTVs() {
        onView(withId(R.id.menu_tv)).perform(click());
        onView(allOf(withId(R.id.rv_horiz), isDescendantOfA(withId(R.id.fragment_tv))))
                .check(withItemCount(equalTo(20)));
        onView(allOf(withId(R.id.rv_vert), isDisplayed()))
                .check(withItemCount(equalTo(20)));
    }

    @Test
    public void loadDetailMovie() {
        onView(withId(R.id.menu_movie)).perform(click());
        onView(allOf(withId(R.id.rv_vert), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.tv_header), isDisplayed()));
        onView(allOf(withId(R.id.tv_release_year), isDisplayed()));
        onView(withId(R.id.tv_release_date)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_studio)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tv_score_average), isDisplayed()));
        onView(withId(R.id.tv_score_count)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_type)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_runtime)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_genre)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_synopsis)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.rv_recommendation)).check(withItemCount(greaterThanOrEqualTo(0)));
        onView(withId(R.id.btn_trailer)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_trailer)).check(matches(isClickable()));
        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_favorite)).check(matches(isClickable()));
        onView(withId(R.id.fab_back)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_back)).check(matches(isClickable()));
    }

    @Test
    public void loadDetailTV() {
        onView(withId(R.id.menu_tv)).perform(click());
        onView(allOf(withId(R.id.rv_vert), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.tv_header), isDisplayed()));
        onView(allOf(withId(R.id.tv_release_year), isDisplayed()));
        onView(withId(R.id.tv_release_date)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_studio)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tv_score_average), isDisplayed()));
        onView(withId(R.id.tv_score_count)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_type)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_runtime)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_genre)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recommendation)).check(withItemCount(greaterThanOrEqualTo(0)));
        onView(withId(R.id.tv_synopsis)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.btn_trailer)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_trailer)).check(matches(isClickable()));
        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_favorite)).check(matches(isClickable()));
        onView(withId(R.id.fab_back)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_back)).check(matches(isClickable()));
    }

    @Test
    public void loadDiscover() {
        onView(withId(R.id.menu_discover)).perform(click());
        onView(withId(R.id.chip_group)).check(matches(isDisplayed()));

        onView(withId(R.id.chip_popular)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.chip_popular)).perform(click());
        onView(withId(R.id.rv_movie)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.rv_movie)).check(withItemCount(equalTo(20)));
        onView(withId(R.id.rv_tv)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.rv_tv)).check(withItemCount(equalTo(20)));

        onView(withId(R.id.chip_upcoming)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.chip_upcoming)).perform(click());
        onView(withId(R.id.rv_movie)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.rv_movie)).check(withItemCount(equalTo(20)));
        onView(withId(R.id.rv_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        onView(withId(R.id.chip_latest_release)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.chip_latest_release)).perform(click());
        onView(withId(R.id.rv_movie)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.rv_movie)).check(withItemCount(greaterThanOrEqualTo(0)));
        onView(withId(R.id.rv_tv)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.rv_tv)).check(withItemCount(greaterThanOrEqualTo(0)));

        onView(withId(R.id.chip_top_rated)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.chip_top_rated)).perform(click());
        onView(withId(R.id.rv_movie)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.rv_movie)).check(withItemCount(equalTo(20)));
        onView(withId(R.id.rv_tv)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.rv_tv)).check(withItemCount(equalTo(20)));
    }

    @Test
    public void loadSearch() {
        onView(withId(R.id.menu_discover)).perform(click());
        onView(withId(R.id.search_view)).check(matches(isDisplayed()));
        onView(withId(R.id.search_view)).perform(click());
        onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText("the promised neverland"), pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(withItemCount(greaterThanOrEqualTo(2)));

        onView(withId(R.id.search_view)).check(matches(isDisplayed()));
        onView(withId(R.id.search_view)).perform(click());
        onView(isAssignableFrom(AutoCompleteTextView.class)).perform(clearText(), typeText("maquia"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(withItemCount(greaterThanOrEqualTo(1)));
    }

    @Test
    public void loadFavorite() {
        onView(withId(R.id.menu_movie)).perform(click());
        onView(allOf(withId(R.id.rv_vert), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_favorite)).perform(click());
        onView(withId(R.id.fab_back)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_back)).perform(click());

        onView(withId(R.id.menu_tv)).perform(click());
        onView(allOf(withId(R.id.rv_vert), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_favorite)).perform(click());
        onView(withId(R.id.fab_back)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_back)).perform(click());

        onView(withId(R.id.menu_favorite)).perform(click());

        onView(withId(R.id.chip_all)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.chip_all)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(withItemCount(greaterThanOrEqualTo(0)));

        onView(withId(R.id.chip_movie)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.chip_movie)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(withItemCount(greaterThanOrEqualTo(0)));

        onView(withId(R.id.chip_tv)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.chip_tv)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(withItemCount(greaterThanOrEqualTo(0)));

        onView(withId(R.id.fab_option)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_option)).perform(click());
    }
}