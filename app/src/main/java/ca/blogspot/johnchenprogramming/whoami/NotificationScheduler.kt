package ca.blogspot.johnchenprogramming.whoami

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
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
        val feelings = FeelingReminder().getFeelingList()
        remoteViews.setTextViewText(R.id.title, TITLE)
        remoteViews.setTextViewText(R.id.sub_title, SUB_TITLE)
        remoteViews.setTextViewText(R.id.feeling_0, feelings[0])
        remoteViews.setTextViewText(R.id.feeling_1, feelings[1])
        remoteViews.setTextViewText(R.id.feeling_2, feelings[2])
        remoteViews.setTextViewText(R.id.feeling_3, feelings[3])
        remoteViews.setTextViewText(R.id.feeling_4, feelings[4])
        remoteViews.setTextViewText(R.id.feeling_5, feelings[5])
        remoteViews.setPendingIntentTemplate(
                R.id.feelings,
                PendingIntent.getBroadcast(
                        context,
                        0,
                        Intent(context, NotificationActions::class.java),
                        0))
        var fillInIntent = Intent(context, NotificationActions::class.java)
        fillInIntent.putExtra("SELECTED_FEELING", feelings[0]);
        remoteViews.setOnClickFillInIntent(R.id.feeling_0, fillInIntent)
        fillInIntent = Intent(context, NotificationActions::class.java)
        fillInIntent.putExtra("SELECTED_FEELING", feelings[1]);
        remoteViews.setOnClickFillInIntent(R.id.feeling_1, fillInIntent)
        fillInIntent.putExtra("SELECTED_FEELING", feelings[2]);
        remoteViews.setOnClickFillInIntent(R.id.feeling_2, fillInIntent)
        fillInIntent.putExtra("SELECTED_FEELING", feelings[3]);
        remoteViews.setOnClickFillInIntent(R.id.feeling_3, fillInIntent)
        fillInIntent.putExtra("SELECTED_FEELING", feelings[4]);
        remoteViews.setOnClickFillInIntent(R.id.feeling_4, fillInIntent)
        fillInIntent.putExtra("SELECTED_FEELING", feelings[5]);
        remoteViews.setOnClickFillInIntent(R.id.feeling_5, fillInIntent)
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
