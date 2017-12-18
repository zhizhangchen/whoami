package ca.blogspot.johnchenprogramming.whoami

import android.support.test.InstrumentationRegistry

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.By
import android.support.test.uiautomator.Until

class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.openNotification();
        device.wait(Until.hasObject(By.text(NotificationScheduler.TITLE)), 10000)
        val title = device.findObject(By.text(NotificationScheduler.TITLE))
        val subtitle = device.findObject(By.text(NotificationScheduler.SUB_TITLE))
        assertEquals(NotificationScheduler.TITLE, title.getText())
        assertEquals(NotificationScheduler.SUB_TITLE, subtitle.getText())
    }

}
