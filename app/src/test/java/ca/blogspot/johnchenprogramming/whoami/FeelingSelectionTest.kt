package ca.blogspot.johnchenprogramming.whoami

import android.app.NotificationManager
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FeelingSelectionTest() {
    val INTERVAL = 50L;
    @Test
    fun feelingList_isLongerThan5() {
        assertTrue(FeelingReminder(INTERVAL).getFeelingList().size >= 5);
    }

    @Test
    fun feelingList_contains() {
        with(FeelingReminder(INTERVAL).getFeelingList()) {
            listOf("angry", "happy", "calm", "depressed", "jealous", "hating")
                    .forEach({w -> assertTrue(any({f -> f.contains(w, true)}))})
        }
    }

    var previousTime = 0L;
    @Test
    fun feelingGeneration_isPeriodical() {
        val o = Object();
        object : Thread() {
            override fun run() {
                FeelingReminder(INTERVAL).listen(object : FeelingReminder.Listener {
                    override fun onRemind() {
                        if (previousTime == 0L) {
                            previousTime = Date().time;
                        } else {
                            synchronized(o) {
                                o.notifyAll();
                            }
                        }
                    }
                })
            }
        }.start();

        synchronized(o) {
            o.wait(INTERVAL * 3L);
        }

        val currentTime = Date().time;
        assertTrue(Math.abs(currentTime  - previousTime - INTERVAL) < 20)
    }
}

