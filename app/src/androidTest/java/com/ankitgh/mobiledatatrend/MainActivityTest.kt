package com.ankitgh.mobiledatatrend

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.ankitgh.mobiledatatrend.R.id.*
import com.ankitgh.mobiledatatrend.ui.RecordAdapter
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
open class MainActivityTest {

    @get : Rule
    val mainActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get : Rule
    val mActivityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule<MainActivity>(MainActivity::class.java)


    @Test
    fun recyclerViewIsVisibleTest() {
        onView(withId(rootView)).check(matches(isDisplayed()))
        onView(withId(recyclerview)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnLowUsageImage(){
        onView(withId(recyclerview)).check(matches(isDisplayed()))
        onView(withId(recyclerview))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecordAdapter.RecordViewHolder>(
                        1, clickItemWithId(
                            low_data_consmption_image
                        )
                    )
            )
    }

    fun clickItemWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id) as View
                if (v.visibility == View.VISIBLE)
                    v.performClick()
            }
        }
    }
}