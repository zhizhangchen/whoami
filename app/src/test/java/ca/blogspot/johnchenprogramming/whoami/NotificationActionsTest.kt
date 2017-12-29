package ca.blogspot.johnchenprogramming.whoami

import android.content.BroadcastReceiver
import org.junit.Test

import org.junit.Assert.*
import android.app.NotificationManager
import android.content.Context
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.verifyNew
import org.powermock.api.mockito.PowerMockito.whenNew
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner


@RunWith(PowerMockRunner::class)
@PrepareForTest(NotificationActions::class)

class NotificationActionsTest {
    @Mock
    private lateinit var context : Context

    @Mock
    private lateinit var nm : NotificationManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun notificationActions_isBroadcastReceiver() {
        assertTrue(BroadcastReceiver::class.java.isAssignableFrom(NotificationActions::class.java))
    }

    @Test
    fun notificationActions_cancelAndRescheduleNotification() {
        PowerMockito.mockStatic(NotificationActions::class.java)
        val scheduler = mock(NotificationScheduler::class.java)
        whenNew(NotificationScheduler::class.java).withAnyArguments()
                .thenReturn(scheduler)
        doReturn(nm).`when`(context).getSystemService(Context.NOTIFICATION_SERVICE)
        NotificationActions().onReceive(context, null)
        verify(nm).cancel(0)
        verifyNew(NotificationScheduler::class.java).withArguments(context, NotificationActions.interval)
        verify(scheduler).schedule()
    }
}

