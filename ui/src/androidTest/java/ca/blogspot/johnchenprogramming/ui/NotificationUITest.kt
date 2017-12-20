package ca.blogspot.johnchenprogramming.ui

import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.hasTextColor
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import ca.blogspot.johnchenprogramming.ui.ViewAssertions.hasTextSize
import ca.blogspot.johnchenprogramming.ui.test.R
import org.junit.Rule
import org.junit.Test


/**
 * @author John
 */
class NotificationUITest {
    @get:Rule
    var mActivityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

    @Test
    fun feelingsView_checkFontSizes () {
        onView(withId(R.id.title)).check(hasTextSize(R.dimen.tile_size)).check(
                matches(hasTextColor(R.color.abc_primary_text_material_light)));
    }
}

