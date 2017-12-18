package ca.blogspot.johnchenprogramming.whoami

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.By
import android.support.test.uiautomator.Until
import java.io.File


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        clearPreferences()
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.openNotification();
        device.wait(Until.hasObject(By.text(NotificationScheduler.TITLE)), 10000)
        val title = device.findObject(By.text(NotificationScheduler.TITLE))
        val subtitle = device.findObject(By.text(NotificationScheduler.SUB_TITLE))
        assertEquals(NotificationScheduler.TITLE, title.getText())
        assertEquals(NotificationScheduler.SUB_TITLE, subtitle.getText())
    }

    private fun clearPreferences() {
        val root = InstrumentationRegistry.getTargetContext().filesDir.parentFile
        val sharedPreferencesFileNames = File(root, "shared_prefs").list()
        for (fileName in sharedPreferencesFileNames) {
            InstrumentationRegistry.getTargetContext().getSharedPreferences(
                    fileName.replace(".xml", ""),
                    Context.MODE_PRIVATE
            ).edit().clear().apply()
        }
    }
}
