package ca.blogspot.johnchenprogramming.whoami

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import android.preference.PreferenceManager
import android.app.NotificationChannel



open class NotificationScheduler(private val context: Context? = null, private val interval: Long = 0) : BroadcastReceiver() {
    companion object {
        val CHANNEL_ID = "Feelings"
        val TITLE = "How are you feeling?"
        val SUB_TITLE = "(Observe your feelings without judgements...)"
        val PREFERENCE_ALARM_SET: String = "ALARM_SET"
        val SELECTED_FEELING_KEY: String = "SELECTED_FEELING"
        val FEELING_VIEW_IDS = listOf(R.id.feeling_0, R.id.feeling_1, R.id.feeling_2, R.id.feeling_3, R.id.feeling_4, R.id.feeling_5)
    }

    @SuppressLint("NewApi")
    override fun onReceive(context: Context?, intent: Intent?) {
        val nm = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_ID,
                        NotificationManager.IMPORTANCE_LOW)
                // Configure the notification channel.
                channel.description = "Remind you to be conscious of your feelings"
                nm.createNotificationChannel(channel)
            }
        }
        val remoteViews = RemoteViews(context.packageName, R.layout.feelings);
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        val style = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationCompat.DecoratedCustomViewStyle()
        } else {
            NotificationCompat.BigPictureStyle()
        };

        notificationBuilder
                .setSmallIcon(R.drawable.notification_icon_background)
                .setCustomContentView(remoteViews)
                .setStyle(style)
        remoteViews.setTextViewText(R.id.title, TITLE)
        remoteViews.setTextViewText(R.id.sub_title, SUB_TITLE)
        val feelings = FeelingReminder().getFeelingList()
        for (i in feelings.indices) {
            remoteViews.setTextViewText(FEELING_VIEW_IDS[i], feelings[i])
            val feelingIntent = Intent(context, NotificationActions::class.java)
            feelingIntent.putExtra(SELECTED_FEELING_KEY, feelings[i])
            remoteViews.setOnClickPendingIntent(
                    FEELING_VIEW_IDS[i],
                    PendingIntent.getBroadcast(context, 0, feelingIntent, 0))
        }
        nm.notify(0, notificationBuilder.build());
    }

    open fun schedule() {
        val p = PreferenceManager.getDefaultSharedPreferences(context)
        val alarmSet = p.getBoolean(PREFERENCE_ALARM_SET, false)
        if (!alarmSet) {
            p.edit().putBoolean(PREFERENCE_ALARM_SET, true).apply()
            val am = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.set(
                    AlarmManager.RTC,
                    System.currentTimeMillis(),
                    PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent(context, NotificationScheduler::class.java),
                            0)
            )
        }
    }
}
