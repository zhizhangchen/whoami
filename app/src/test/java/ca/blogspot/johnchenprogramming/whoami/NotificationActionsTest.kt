package ca.blogspot.johnchenprogramming.whoami

import android.content.BroadcastReceiver
import org.junit.Test

import org.junit.Assert.*
import android.app.NotificationManager
import android.content.Context
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class NotificationActionsTest {
    @Mock
    private lateinit var context : Context;

    @Mock
    private lateinit var nm : NotificationManager;

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    fun notificationActions_isBroadcastReceiver() {
        assertTrue(BroadcastReceiver::class.java.isAssignableFrom(NotificationActions::class.java));
    }

    @Test
    fun notificationActions_cancelNotification() {
        doReturn(nm).`when`(context).getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationActions().onReceive(context, null)
        verify(nm).cancel(0)
    }
}

