package ca.blogspot.johnchenprogramming.whoami

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActions : BroadcastReceiver() {

    companion object {
        var interval = 60 * 60 * 1000L ;
        fun setNotificationInterval(interval: Int) {
            this.interval = interval.toLong();
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(0)
        NotificationScheduler(context, interval).schedule()
    }
}
