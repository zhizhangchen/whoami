package ca.blogspot.johnchenprogramming.whoami

import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


class WelcomeActivityTest {
    @Test
    fun isScheduled() {
        val scheduler = mock(NotificationScheduler::class.java)
        WelcomeActivity().init(scheduler)
        verify(scheduler).schedule()
    }
}
