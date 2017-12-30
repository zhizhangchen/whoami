package ca.blogspot.johnchenprogramming.testutil

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.util.HumanReadables
import android.view.View
import android.widget.TextView

import org.hamcrest.StringDescription
import org.hamcrest.core.Is
import org.junit.Assert

object ViewAssertions {
    fun hasTextSize(resId: Int): ViewAssertion {
        return object : ViewDimensionAssertion(resId) {
            override val dimensionName: String
                get() = "text size"

            override fun getActualDimension(foundView: View): Float {
                return (foundView as TextView).textSize
            }
        }
    }

    private abstract class ViewDimensionAssertion internal constructor(internal val resId: Int) : ViewAssertion {

        internal abstract val dimensionName: String

        override fun check(foundView: View, noViewException: NoMatchingViewException?) {
            val description = StringDescription()
            if (noViewException != null) {
                description.appendText("Check could not be performed because view  ${noViewException.viewMatcherDescription} was not found.\n")
                throw noViewException
            } else {
                val expectedOffset = InstrumentationRegistry.getTargetContext().resources.getDimensionPixelSize(resId).toFloat()
                // TODO(user): describe the foundView matcher instead of the foundView it self.
                description.appendText("View:").appendText(HumanReadables.describe(foundView))
                        .appendText(" does not have the $dimensionName of $expectedOffset")

                Assert.assertThat(
                        description.toString(),
                        getActualDimension(foundView),
                        Is.`is`(expectedOffset)
                )
            }
        }

        internal abstract fun getActualDimension(foundView: View): Float
    }
}
