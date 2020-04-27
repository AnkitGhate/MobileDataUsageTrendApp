package com.ankitgh.mobiledatatrend

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.ankitgh.mobiledatatrend.R.id.recyclerview
import com.ankitgh.mobiledatatrend.R.id.rootView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get : Rule
    val mainActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityViewTest() {
        onView(withId(rootView)).check(matches(isDisplayed()))
        onView(withId(recyclerview)).check(matches(isDisplayed()))
    }
}