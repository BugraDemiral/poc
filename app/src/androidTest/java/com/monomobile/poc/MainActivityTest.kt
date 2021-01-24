package com.monomobile.poc

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val viewGroup = onView(
            allOf(
                withId(R.id.toolbar),
                withParent(withParent(withId(R.id.drawer_layout))),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.refresh), withContentDescription("Refresh"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val searchView = onView(
            allOf(
                withId(R.id.searchView),
                withParent(withParent(withId(R.id.strItems))),
                isDisplayed()
            )
        )
        searchView.check(matches(isDisplayed()))

        val recyclerView = onView(
            allOf(
                withId(R.id.rvItems),
                withParent(withParent(withId(R.id.strItems))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.tvFilter), withText("Filter"),
                withParent(withParent(withId(R.id.strItems))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Filter")))

        val textView3 = onView(
            allOf(
                withId(R.id.tvFilter), withText("Filter"),
                withParent(withParent(withId(R.id.strItems))),
                isDisplayed()
            )
        )
        textView3.check(matches(isDisplayed()))
    }
}
