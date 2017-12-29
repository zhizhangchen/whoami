package ca.blogspot.johnchenprogramming.whoami

import android.support.test.InstrumentationRegistry.getTargetContext
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author John
 */
class ApplicationAndroidTest {
    @Test
    fun applicationIcon() {
        val context = getTargetContext()
        assertEquals(context.applicationInfo.icon, R.drawable.who_am_i)
    }
}
