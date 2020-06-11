package teamsevendream.paspaintracker.main;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;

//import static android.app.PendingIntent.getActivity;

public class StartActivityTests {

//    StartActivity startActivity;
    private DatabaseHelper database;
    private Context context;

    @Rule
    public ActivityTestRule<StartActivity> mActivityRule =
            new ActivityTestRule<>(StartActivity.class);
    
    @Test
    public void setupApp_newUser(){
        context = androidx.test.InstrumentationRegistry.getTargetContext();
        database = new DatabaseHelper(context);

        //clicks the getting started button
        onView(withId(R.id.btnGetStarted)).perform(click());

        //add user data and submit
        onView(withId(R.id.nameEntry)).perform(replaceText("John"),
                closeSoftKeyboard());
        onView(withId(R.id.surnameEntry)).perform(replaceText("Doe"),
                closeSoftKeyboard());
        onView(withId(R.id.dateEntry)).perform(replaceText("02/10/2000"),
                closeSoftKeyboard());
        onView(withId(R.id.btnSubmitPersonalData)).perform(click());

        //add spider data and submit
        onView(withId(R.id.spiderAnswer1)).perform(clickSeekBar(5));
        onView(withId(R.id.spiderAnswer2)).perform(clickSeekBar(5));
        onView(withId(R.id.spiderAnswer3)).perform(clickSeekBar(5));
        onView(withId(R.id.spiderAnswer4)).perform(ViewActions.scrollTo(), clickSeekBar(5));
        onView(withId(R.id.spiderAnswer5)).perform(ViewActions.scrollTo(), clickSeekBar(5));
        onView(withId(R.id.spiderAnswer6)).perform(ViewActions.scrollTo(), clickSeekBar(5));
        onView(withId(R.id.spiderAnswer7)).perform(ViewActions.scrollTo(), clickSeekBar(5));
        onView(withId(R.id.spiderAnswer8)).perform(ViewActions.scrollTo(), clickSeekBar(5));
        onView(withId(R.id.spiderAnswer9)).perform(ViewActions.scrollTo(), clickSeekBar(5));
        onView(withId(R.id.spiderAnswer10)).perform(ViewActions.scrollTo(), clickSeekBar(5));
        onView(withId(R.id.spiderAnswer11)).perform(ViewActions.scrollTo(), clickSeekBar(5));
        onView(withId(R.id.spiderAnswer12)).perform(ViewActions.scrollTo(), clickSeekBar(5));
        onView(withId(R.id.btnSubmitSpiderData)).perform(ViewActions.scrollTo(), click());

        //check user data added successfully
        List<String> data = database.getUserData();
        assertEquals("Name don't match", "John", data.get(0));
        assertEquals("Surnames don't match", "Doe", data.get(1));
        assertEquals("DOBs don't match", "02/10/2000", data.get(2));

        //check spider data added successfully
        List<Integer> dataSpider = database.getSpiderData();
        assertEquals("Spider0 don't match", 5, (int) dataSpider.get(0));
        assertEquals("Spider1 don't match", 5, (int) dataSpider.get(1));
        assertEquals("Spider2 don't match", 5, (int) dataSpider.get(2));
        assertEquals("Spider3 don't match", 5, (int) dataSpider.get(3));
        assertEquals("Spider4 don't match", 5, (int) dataSpider.get(4));
        assertEquals("Spider5 don't match", 5, (int) dataSpider.get(5));
        assertEquals("Spider6 don't match", 5, (int) dataSpider.get(6));
        assertEquals("Spider7 don't match", 5, (int) dataSpider.get(7));
        assertEquals("Spider8 don't match", 5, (int) dataSpider.get(8));
        assertEquals("Spider9 don't match", 5, (int) dataSpider.get(9));
        assertEquals("Spider10 don't match", 5, (int) dataSpider.get(10));
        assertEquals("Spider11 don't match", 5, (int) dataSpider.get(11));
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
