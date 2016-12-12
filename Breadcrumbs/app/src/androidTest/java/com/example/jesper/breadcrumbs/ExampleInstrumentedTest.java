package com.example.jesper.breadcrumbs;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity> mainTestRule = new ActivityTestRule<>(MainActivity.class);

	//Startpage
	@Test
	public void isRootAdded() throws Exception {
		onView(withText("Earth")).check(matches(isDisplayed()));
	}

	@Test
	public void isRootClickable1() throws Exception {
		onView(withText("Earth")).check(matches(not(isClickable())));
	}

	//One opened
	@Test
	public void isRootClickable2() throws Exception{
		onView(withText("North America")).perform(click());

		onView(withText("Earth")).check(matches(isClickable()));
	}

	@Test
	public void isLastClickable1() throws Exception{
		onView(withText("North America")).perform(click());
		onView(withText("Earth")).perform(click());
		onView(withText("Europe")).perform(click());

		onView(withText("Europe")).check(matches(not(isClickable())));
	}

	//Multiple opened0
	@Test
	public void isLastClickable2() throws Exception {
		onView(withText("Europe")).perform(click());
		onView(withText("Sweden")).perform(click());
		onView(withText("Linköping")).perform(click());

		onView(withText("Linköping")).check((matches(not(isClickable()))));
	}

	@Test
	public void isBreadcrumbsRemoved() throws Exception {
		onView(withText("North America")).perform(click());
		onView(withText("USA")).perform(click());

		onView(withText("Earth")).perform(click());
		onView(withText("USA")).check(doesNotExist());
	}
}
