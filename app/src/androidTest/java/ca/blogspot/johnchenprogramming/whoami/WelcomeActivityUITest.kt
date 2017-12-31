package ca.blogspot.johnchenprogramming.whoami

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import ca.blogspot.johnchenprogramming.testutil.ViewAssertions.hasTextSize
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

/**
 * @author John
 */
class WelcomeActivityUITest {
    @Rule @JvmField
    val activityRule = ActivityTestRule<WelcomeActivity>(WelcomeActivity::class.java)

    @Test
    fun content() {
        onView(withText(R.string.content)).perform(click())
        sleep(1000)
        onView(withText(R.string.button)).perform(click())
        assertTrue(activityRule.activity.isFinishing)
    }

    @Test
    fun isButtonTextSize20sp() {
        onView(withText(R.string.button)).check(hasTextSize(R.dimen.button_size))
    }
}
