package com.example.myapplication;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTesting {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test public void testingAddTask(){
        onView(withId(R.id.button)).perform(click());
        onView((withId(R.id.editTextTextPersonName))).perform(typeText("Coding"),closeSoftKeyboard());
        onView(withId(R.id.editTextTextPersonName2)).perform(typeText("Still Coding"),closeSoftKeyboard());
        onView(withId(R.id.editTextTextPersonName3)).perform(typeText("In Progress"),closeSoftKeyboard());
//        onView(withId(R.id.button3)).perform(click());
//        onView(withText("Coding")).check(matches(isDisplayed()));
    }

    @Test public void testingRecyclerView(){
//        onView(withId(R.id.taskRecycler)).perform(click());
//        onView(withId(R.id.titleFinish)).check(matches(isDisplayed()));
//        onView(withId(R.id.bodyFinish)).check(matches(isDisplayed()));
//        onView(withId(R.id.stateFinish)).check(matches(isDisplayed()));
        onView(withId(R.id.taskRecycler)).check(matches(isDisplayed()));

    }

    @Test public void testingSettingsPage(){
        onView(withId(R.id.settings)).perform(click());
        onView(withId(R.id.userNameLabel)).perform(typeText("Qusai Tashtosh"),closeSoftKeyboard());
        onView(withId(R.id.button4)).perform(click());
        onView(withId(R.id.welcomeMsg)).check(matches(withText("Qusai Tashtosh: Task")));
    }
}