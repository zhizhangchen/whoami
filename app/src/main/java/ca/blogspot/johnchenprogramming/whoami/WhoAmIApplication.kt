package ca.blogspot.johnchenprogramming.whoami

import android.app.Application

class WhoAmIApplication : Application() {
    companion object {
        val FEELINGS_NOTIFICATION_INTERVAL = 60 * 60 * 1000L ;
    }

    override fun onCreate() {
        super.onCreate()
        init(NotificationScheduler(this, FEELINGS_NOTIFICATION_INTERVAL));
    }

    fun init(scheduler: NotificationScheduler) {
        scheduler.schedule();
    }
}
