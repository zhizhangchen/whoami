package ca.blogspot.johnchenprogramming.whoami

import org.junit.Test

import org.junit.Assert.*

class FeelingSelectionTest {
    @Test
    fun feelingList_isLongerThan5() {
        assertTrue(FeelingReminder().getFeelingList().size >= 5)
    }

    @Test
    fun feelingList_contains() {
        with(FeelingReminder().getFeelingList()) {
            listOf("angry", "happy", "calm", "depressed", "jealous", "hating")
                    .forEach({w -> assertTrue(any({f -> f.contains(w, true)}))})
        }
    }
}

