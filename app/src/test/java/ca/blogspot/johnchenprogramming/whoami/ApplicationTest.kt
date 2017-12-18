package ca.blogspot.johnchenprogramming.whoami

import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


class ApplicationTest {
    @Test
    fun notification_isScheduled() {
        val scheduler = mock(NotificationScheduler::class.java)
        WhoAmIApplication().init(scheduler)
        verify(scheduler).schedule()
    }
}
