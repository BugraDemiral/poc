package com.monomobile.poc

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private var mIdlingResource: IdlingResource? = null

    @Before
    fun registerIdlingResource() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity {
            mIdlingResource = it.getIdlingResource()
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }

    @Test
    fun detailActivityTest() {
        val recyclerView = onView(
            allOf(
                withId(R.id.rvItems),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val viewGroup = onView(
            allOf(
                withId(R.id.toolbar),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val imageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                withParent(
                    allOf(
                        withId(R.id.toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.imageView2),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.textView), withText("Name: "),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Name: ")))

        val textView2 = onView(
            allOf(
                withId(R.id.textViewName),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView2.check(matches(not(withText(""))))

        val textView3 = onView(
            allOf(
                withId(R.id.textView3), withText("Occupation: "),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Occupation: ")))

        val textView4 = onView(
            allOf(
                withId(R.id.textViewOccupation),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView4.check(matches(not(withText(""))))

        val textView5 = onView(
            allOf(
                withId(R.id.textView5), withText("Status: "),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Status: ")))

        val textView6 = onView(
            allOf(
                withId(R.id.textViewStatus),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView6.check(matches(not(withText(""))))

        val textView7 = onView(
            allOf(
                withId(R.id.textView7), withText("Nickname: "),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("Nickname: ")))

        val textView8 = onView(
            allOf(
                withId(R.id.textViewNickname),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView8.check(matches(not(withText(""))))

        val textView9 = onView(
            allOf(
                withId(R.id.textView9), withText("Season appearance: "),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView9.check(matches(withText("Season appearance: ")))

        val textView10 = onView(
            allOf(
                withId(R.id.textViewSeasonAppearance),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView10.check(matches(not(withText(""))))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

}
