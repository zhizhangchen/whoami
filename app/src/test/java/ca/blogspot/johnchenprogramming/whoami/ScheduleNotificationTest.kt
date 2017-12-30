package ca.blogspot.johnchenprogramming.whoami

import android.app.*
import android.content.*
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import ca.blogspot.johnchenprogramming.whoami.NotificationScheduler.Companion.FEELING_LAYOUT_IDS
import ca.blogspot.johnchenprogramming.whoami.NotificationScheduler.Companion.FEELING_TEXT_VIEW_IDS
import ca.blogspot.johnchenprogramming.whoami.NotificationScheduler.Companion.PREFERENCE_ALARM_SET
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.whenNew
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox


@RunWith(PowerMockRunner::class)
@PrepareForTest(
        PendingIntent::class,
        NotificationScheduler::class,
        PreferenceManager::class,
        Build.VERSION::class)

class ScheduleNotificationTest {
    companion object {
        private val INTERVAL = 500L
    }

    @Mock
    private lateinit var context : Context

    @Mock
    private lateinit var am : AlarmManager

    @Mock
    private lateinit var nm : NotificationManager

    @Mock
    private lateinit var schedulingPendingIntent: PendingIntent

    @Mock
    private lateinit var notificationActionsPendingIntent: PendingIntent

    @Mock
    private lateinit var intent : Intent

    @Mock
    private lateinit var actionsIntent : Intent

    @Mock
    private lateinit var notificationBuilder : NotificationCompat.Builder

    private var alarmSet  = false

    @Before
    fun setUp() {
        doReturn(am).`when`(context).getSystemService(Context.ALARM_SERVICE)
        doReturn(nm).`when`(context).getSystemService(Context.NOTIFICATION_SERVICE)
        PowerMockito.mockStatic(PendingIntent::class.java)
        `when`(PendingIntent.getBroadcast(
                any(Context::class.java),
                ArgumentMatchers.anyInt(),
                any(Intent::class.java),
                ArgumentMatchers.anyInt())
        ).thenAnswer {
            invocation ->
                if ((invocation.arguments[2] as Intent).component.className.equals(NotificationActions::class.java.name))
                    notificationActionsPendingIntent
                else
                    schedulingPendingIntent
        }

        whenNew(Intent::class.java).withArguments(context, NotificationScheduler::class.java).thenReturn(intent)
        val component = mock(ComponentName::class.java)
        `when`(component.className).thenReturn(NotificationScheduler::class.java.name)
        `when`(intent.component).thenReturn(component)
        whenNew(Intent::class.java).withArguments(context, NotificationActions::class.java).thenReturn(actionsIntent)
        val actionsComponent = mock(ComponentName::class.java)
        `when`(actionsComponent.className).thenReturn(NotificationActions::class.java.name)
        `when`(actionsIntent.component).thenReturn(actionsComponent)

        val sharedPreference = mock(SharedPreferences::class.java)
        val editor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPreference.edit()).thenReturn(editor)
        doAnswer{alarmSet = true; editor }.`when`(editor).putBoolean(PREFERENCE_ALARM_SET, true)
        doAnswer{alarmSet}.`when`(sharedPreference).getBoolean(eq(PREFERENCE_ALARM_SET), ArgumentMatchers.anyBoolean())
        PowerMockito.mockStatic(PreferenceManager::class.java)
        `when`(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPreference)
    }

    @Test
    fun notification_scheduled() {
        NotificationScheduler(context, INTERVAL).schedule()
        verify(am).set(eq(AlarmManager.RTC), ArgumentMatchers.anyLong(), eq(schedulingPendingIntent))
    }

    @Test
    fun alarm_receive() {
        assertTrue(BroadcastReceiver::class.java.isAssignableFrom(NotificationScheduler::class.java))
    }

    @Test
    fun notification_create() {
        whenNew(NotificationCompat.Builder::class.java).withArguments(context, NotificationScheduler.CHANNEL_ID).thenReturn(notificationBuilder)
        val remoteViews = mock(RemoteViews::class.java)
        whenNew(RemoteViews::class.java).withArguments(eq(context.packageName), ArgumentMatchers.anyInt()).thenReturn(remoteViews)
        val notification = mock(Notification::class.java)
        `when`(notificationBuilder.build()).thenReturn(notification)
        `when`(notificationBuilder.setSmallIcon(ArgumentMatchers.anyInt())).thenReturn(notificationBuilder)
        `when`(notificationBuilder.setCustomContentView(any(RemoteViews::class.java))).thenReturn(notificationBuilder)
        NotificationScheduler(context, INTERVAL).onReceive(context, intent)
        verify(nm).notify(ArgumentMatchers.anyInt(), eq(notification))
        verify(notificationBuilder).setSmallIcon(R.drawable.notification_icon)
        verify(notificationBuilder).setCustomContentView(any(RemoteViews::class.java))
        verify(remoteViews).setTextViewText(R.id.title, NotificationScheduler.TITLE)
        verify(remoteViews).setTextViewText(R.id.sub_title, NotificationScheduler.SUB_TITLE)
        val feelings = FeelingReminder().getFeelingList()
        for(i in feelings.indices) {
            verify(remoteViews).setTextViewText(FEELING_TEXT_VIEW_IDS[i], feelings[i])
            verify(remoteViews).setOnClickPendingIntent(eq(FEELING_LAYOUT_IDS[i]), eq(notificationActionsPendingIntent))
            verify(actionsIntent).putExtra("SELECTED_FEELING", feelings[i])
        }
    }

    @Test
    fun notification_createApi26() {
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 26)
        val chanel = mock(NotificationChannel::class.java)
        whenNew(NotificationChannel::class.java).withAnyArguments().thenReturn(chanel)
        val remoteViews = mock(RemoteViews::class.java)
        whenNew(RemoteViews::class.java).withArguments(eq(context.packageName), ArgumentMatchers.anyInt()).thenReturn(remoteViews)
        notification_create()
    }
}
