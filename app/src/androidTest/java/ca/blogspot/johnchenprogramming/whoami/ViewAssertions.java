package ca.blogspot.johnchenprogramming.whoami;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.util.HumanReadables;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.StringDescription;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

class ViewAssertions {
    public static ViewAssertion hasTextSize(final int resId) {
        return new ViewDimensionAssertion(resId) {
            @Override
            String getDimensionName() {
                return "text size";
            }

            @Override
            float getActualDimension(View foundView) {
                return ((TextView) foundView).getTextSize();
            }
        };
    }

    private abstract static class ViewDimensionAssertion implements ViewAssertion {
        final int resId;
        ViewDimensionAssertion(int resId) {
            this.resId = resId;
        }

        @Override
        public void check(final View foundView, NoMatchingViewException noViewException) {
            StringDescription description = new StringDescription();
            if (noViewException != null) {
                description.appendText(String.format(
                        "' check could not be performed because view '%s' was not found.\n",
                        noViewException.getViewMatcherDescription()));
                throw noViewException;
            } else {
                float expectedOffset = getTargetContext().getResources().getDimensionPixelSize(resId);
                // TODO(user): describe the foundView matcher instead of the foundView it self.
                description.appendText("View:").appendText(HumanReadables.describe(foundView))
                        .appendText(" does not have the " + getDimensionName() + " of " + expectedOffset);

                assertThat(
                        description.toString(),
                        getActualDimension(foundView),
                        is(expectedOffset)
                );
            }
        }

        abstract String getDimensionName();
        abstract float getActualDimension(View foundView);
    }
}
