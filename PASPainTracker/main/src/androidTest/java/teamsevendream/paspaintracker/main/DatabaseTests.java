package teamsevendream.paspaintracker.main;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseTests{

    private DatabaseHelper database;
    private Context context;

    @Before
    public void implementDB() {
        context = InstrumentationRegistry.getTargetContext();
        database = new DatabaseHelper(context);
    }

    @Test
    public void testDatabaseMethods_painAdd() {
        database.createPainData(1, "Head", "Hit it", "Sitting Down", "Standing up",
                "h", "03/12/2019", "02:00", "20847", "-4027");

        Date date = null;
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = curFormater.parse("03/12/2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        List<String> data = database.getPainData(calendarDate.getTime().toString(), "02:00");
        assertEquals("Intensitys don't match", "1", data.get(1));
        assertEquals("Areas don't match", "Head", data.get(2));
        assertEquals("Details don't match", "Hit it", data.get(3));
        assertEquals("Helps don't match", "Sitting Down", data.get(4));
        assertEquals("Not Help don't match", "Standing up", data.get(5));
        assertEquals("Worse don't match", "h", data.get(6));
        assertEquals("Dates don't match", "03/12/2019", data.get(7));
        assertEquals("Times don't match", "02:00", data.get(8));
        assertEquals("Lats don't match", "20847", data.get(9));
        assertEquals("Lons don't match", "-4027", data.get(10));

        database.updatePainData(data.get(0),2, "Back", "Hit it", "Sitting Down", "Standing up",
                "h", "03/12/2019", "02:00", "20847", "-4027");

        List<String> dataUpdated = database.getPainData(calendarDate.getTime().toString(), "02:00");
        assertEquals("Intensitys don't match", "2", dataUpdated.get(1));
        assertEquals("Areas don't match", "Back", dataUpdated.get(2));
        assertEquals("Details don't match", "Hit it", dataUpdated.get(3));
        assertEquals("Helps don't match", "Sitting Down", dataUpdated.get(4));
        assertEquals("Not Help don't match", "Standing up", dataUpdated.get(5));
        assertEquals("Worse don't match", "h", dataUpdated.get(6));
        assertEquals("Dates don't match", "03/12/2019", dataUpdated.get(7));
        assertEquals("Times don't match", "02:00", dataUpdated.get(8));
        assertEquals("Lats don't match", "20847", dataUpdated.get(9));
        assertEquals("Lons don't match", "-4027", dataUpdated.get(10));

        // Clean up database
        boolean result = database.deleteRecord(data.get(0));
        assertTrue("Delete failed", result);
    }

    @Test
    public void testDatabaseMethods_userAdd() {

        database.createUserData("John", "Doe", "02/10/2000");

        List<String> data = database.getUserData();
        assertEquals("Name don't match", "John", data.get(0));
        assertEquals("Surnames don't match", "Doe", data.get(1));
        assertEquals("DOBs don't match", "02/10/2000", data.get(2));
    }

    @Test
    public void testDatabaseMethods_spiderAdd() {

        database.createSpiderData(1,1,1,1,1,1,1,1,1,1,1,1);

        List<Integer> data = database.getSpiderData();
        assertEquals("Spider0 don't match", 1, (int) data.get(0));
        assertEquals("Spider1 don't match", 1, (int) data.get(1));
        assertEquals("Spider2 don't match", 1, (int) data.get(2));
        assertEquals("Spider3 don't match", 1, (int) data.get(3));
        assertEquals("Spider4 don't match", 1, (int) data.get(4));
        assertEquals("Spider5 don't match", 1, (int) data.get(5));
        assertEquals("Spider6 don't match", 1, (int) data.get(6));
        assertEquals("Spider7 don't match", 1, (int) data.get(7));
        assertEquals("Spider8 don't match", 1, (int) data.get(8));
        assertEquals("Spider9 don't match", 1, (int) data.get(9));
        assertEquals("Spider10 don't match", 1, (int) data.get(10));
        assertEquals("Spider11 don't match", 1, (int) data.get(11));

        database.updateSpiderData(2,2,2,2,2,2,2,2,2,2,2,2);

        List<Integer> dataUpdate = database.getSpiderData();
        assertEquals("Spider0 don't match", 2, (int) dataUpdate.get(0));
        assertEquals("Spider1 don't match", 2, (int) dataUpdate.get(1));
        assertEquals("Spider2 don't match", 2, (int) dataUpdate.get(2));
        assertEquals("Spider3 don't match", 2, (int) dataUpdate.get(3));
        assertEquals("Spider4 don't match", 2, (int) dataUpdate.get(4));
        assertEquals("Spider5 don't match", 2, (int) dataUpdate.get(5));
        assertEquals("Spider6 don't match", 2, (int) dataUpdate.get(6));
        assertEquals("Spider7 don't match", 2, (int) dataUpdate.get(7));
        assertEquals("Spider8 don't match", 2, (int) dataUpdate.get(8));
        assertEquals("Spider9 don't match", 2, (int) dataUpdate.get(9));
        assertEquals("Spider10 don't match", 2, (int) dataUpdate.get(10));
        assertEquals("Spider11 don't match", 2, (int) dataUpdate.get(11));
    }
}