package ca.blogspot.johnchenprogramming.whoami

import kotlin.concurrent.fixedRateTimer

class FeelingReminder(interval: Long) {
    val INTERVAL = 50L;

    fun getFeelingList() : List<String> {
        return listOf("angry", "happy", "I'm calm", "Depressed", "I'm feeling jealous", "I'm hating my self");
    }

    fun listen(feelingListener: Listener) {
        fixedRateTimer(period = INTERVAL) {
            feelingListener.onRemind();
        }
    }

    interface Listener {
        fun onRemind();
    }

}
