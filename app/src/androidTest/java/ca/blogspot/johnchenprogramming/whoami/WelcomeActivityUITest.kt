package ca.blogspot.johnchenprogramming.whoami

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

/**
 * @author John
 */
class WelcomeActivityUITest {
    @get:Rule
    var activityRule = ActivityTestRule<WelcomeActivity>(WelcomeActivity::class.java)

    @Test
    fun content() {
        onView(withText(R.string.content)).perform(click())
        sleep(1000)
        onView(withText(R.string.button)).perform(click())
        assertTrue(activityRule.activity.isFinishing);
    }
}
