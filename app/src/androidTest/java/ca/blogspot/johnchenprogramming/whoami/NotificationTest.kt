package ca.blogspot.johnchenprogramming.whoami

import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.rule.ActivityTestRule
import android.support.test.uiautomator.*
import org.hamcrest.CoreMatchers.*
import org.junit.After

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class NotificationTest {
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val feelings = FeelingReminder().getFeelingList()
    @Rule @JvmField
    val mActivityRule = ActivityTestRule<WelcomeActivity>(WelcomeActivity::class.java)

    @Before
    private fun setUp() {
        device.wakeUp()
        device.openNotification()
        device.wait(Until.hasObject(By.text(NotificationScheduler.TITLE)), 10000)
        val header = device.findObject(By.text("Who Am I"))
        if (feelings.any { feeling -> device.findObject(By.text(feeling)) == null }) {
            header.click()
            header.click()
        }
        device.waitForIdle()
    }

    @Test
    private fun notification_allContentAreShown() {
        val title = device.findObject(By.text(NotificationScheduler.TITLE))
        val subtitle = device.findObject(By.text(NotificationScheduler.SUB_TITLE))
        assertEquals(NotificationScheduler.TITLE, title.text)
        assertEquals(NotificationScheduler.SUB_TITLE, subtitle.text)
        feelings.forEach {feeling ->
            assertThat(
                    feeling,
                    device.findObject(By.text(feeling)),
                    notNullValue())
        }
    }

    @Test
    fun notification_dismissedWhenClickingFeelingText() {
        findFirstFeeling()?.click()
        assertNotificationDismissed()
    }

    @Test
    private fun notification_dismissedWhenClickingFeelingIcon() {
        val radioButton = By.desc(getTargetContext().getString(R.string.radio_button))
        device.findObject(radioButton).click()
        assertNotificationDismissed()
    }

    @Test
    fun notification_shouldRepeat() {
        val interval = 5000
        NotificationActions.setNotificationInterval(interval)
        notification_dismissedWhenClickingFeelingIcon()
        setUp()
        notification_allContentAreShown()
    }

    private fun findFirstFeeling(): UiObject2? {
        return device.findObject(By.text(feelings[0]))
    }

    private fun assertNotificationDismissed() {
        device.waitForIdle()
        assertThat(
                "Notification should have been dismissed",
                findFirstFeeling(),
                nullValue())
    }

    @After
    fun tearDown() {
        device.pressHome()
    }
}
