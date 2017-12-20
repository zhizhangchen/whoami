package ca.blogspot.johnchenprogramming.whoami

import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*

import org.junit.Test

import org.junit.Assert.*

class NotificationTest {
    @Test
    fun notification_isFired() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.wakeUp()
        device.openNotification();
        device.wait(Until.hasObject(By.text(NotificationScheduler.TITLE)), 10000)
        val header = device.findObject(By.text("Who Am I"))
        val feelings = FeelingReminder().getFeelingList()
        if (feelings.any { feeling -> device.findObject(By.text(feeling)) == null }) {
            header.click()
            header.click()
        }
        val title = device.findObject(By.text(NotificationScheduler.TITLE))
        val subtitle = device.findObject(By.text(NotificationScheduler.SUB_TITLE))
        assertEquals(NotificationScheduler.TITLE, title.text)
        assertEquals(NotificationScheduler.SUB_TITLE, subtitle.text)
        device.waitForIdle()
        feelings.forEach {feeling ->
            assertThat(
                    feeling,
                    device.findObject(By.text(feeling)),
                    notNullValue())
        }
        val firstFeelingSelector = By.text(feelings[0])
        device.findObject(firstFeelingSelector).click()
        assertThat(
                "Notification should have been dismissed",
                device.findObject(firstFeelingSelector),
                nullValue())
        device.pressHome()
    }
}
