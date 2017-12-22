package ca.blogspot.johnchenprogramming.whoami

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActions : BroadcastReceiver() {

    companion object {
        val FEELINGS_NOTIFICATION_INTERVAL = 60 * 60 * 1000L ;
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        (context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(0)
        NotificationScheduler(context, FEELINGS_NOTIFICATION_INTERVAL).schedule()
    }
}
