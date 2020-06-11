package teamsevendream.paspaintracker.main;

import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class PainEntryTests {

    private DatabaseHelper database;
    private Context context;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addPainRecordTest() {
        context = androidx.test.InstrumentationRegistry.getTargetContext();
        database = new DatabaseHelper(context);

        onView(withId(R.id.btnRecordPain)).perform(click());

        onView(withId(R.id.bodySearch)).perform(click());
        onView(withId(R.id.lv1)).perform(click());

        onView(withId(R.id.intensitySeekBar)).perform(clickSeekBar(5));

        //add user data and submit
        onView(withId(R.id.contextInput)).perform(replaceText("Hurt hand"),
                closeSoftKeyboard());

        onView(withId(R.id.dateEdit)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 3, 14));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.timeEdit)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 53));
        onView(withId(android.R.id.button1)).perform(click());


        onView(withId(R.id.helpedInput)).perform(replaceText("Sitting"),
                closeSoftKeyboard());

        onView(withId(R.id.notHelpedInput)).perform(ViewActions.scrollTo(), replaceText("Standing"),
                closeSoftKeyboard());

        onView(withId(R.id.painWorseInput)).perform(ViewActions.scrollTo(), replaceText("Standing"),
                closeSoftKeyboard());

        onView(withId(R.id.btnSubmitPainData)).perform(ViewActions.scrollTo(), click());

        Date date = null;
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = curFormater.parse("14/03/2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        List<String> data = database.getPainData(calendarDate.getTime().toString(), "10:53");

        assertEquals("Not all data retrieved", 11, data.size());

        assertEquals("Intensitys don't match", "3", data.get(1));
        assertEquals("Contexts don't match", "Hurt hand", data.get(3));
        assertEquals("Helped don't match", "Sitting", data.get(4));
        assertEquals("Not Helped don't match", "Standing", data.get(5));
        assertEquals("Made Worse don't match", "Standing", data.get(6));
        assertEquals("Dates don't match", "14/03/2019", data.get(7));
        assertEquals("Times don't match", "10:53", data.get(8));
        assertNotNull("Lat is null", data.get(9));
        assertNotNull("Lon is null", data.get(10));

        // Clean up database
        boolean result = database.deleteRecord(data.get(0));
        assertTrue("Delete failed", result);

    }

    //used to set the values of the seekbars
    public static ViewAction clickSeekBar(final int pos){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        SeekBar seekBar = (SeekBar) view;
                        final int[] screenPos = new int[2];
                        seekBar.getLocationOnScreen(screenPos);

                        // get the width of the actual bar area
                        // by removing padding
                        int trueWidth = seekBar.getWidth()
                                - seekBar.getPaddingLeft() - seekBar.getPaddingRight();

                        // what is the position on a 0-1 scale
                        //  add 0.3f to avoid roundoff to the next smaller position
                        float relativePos = (0.3f + pos)/(float) seekBar.getMax();
                        if ( relativePos > 1.0f )
                            relativePos = 1.0f;

                        // determine where to click
                        final float screenX = trueWidth*relativePos + screenPos[0]
                                + seekBar.getPaddingLeft();
                        final float screenY = seekBar.getHeight()/2f + screenPos[1];
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }
}
