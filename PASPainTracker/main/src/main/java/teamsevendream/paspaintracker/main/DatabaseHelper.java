package teamsevendream.paspaintracker.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "PASPainTrackerDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER_DATA = "userData";
    private static final String USER_ID = "userID";
    private static final String USER_NAME = "name";
    private static final String USER_SURNAME = "surname";
    private static final String USER_DOB = "dateOfBirth";

    private static final String TABLE_SPIDER_DATA = "spiderData";
    private static final String SPIDER_ID = "spiderID";
    private static final String SPIDER_QUESTION1 = "question1";
    private static final String SPIDER_QUESTION2 = "question2";
    private static final String SPIDER_QUESTION3 = "question3";
    private static final String SPIDER_QUESTION4 = "question4";
    private static final String SPIDER_QUESTION5 = "question5";
    private static final String SPIDER_QUESTION6 = "question6";
    private static final String SPIDER_QUESTION7 = "question7";
    private static final String SPIDER_QUESTION8 = "question8";
    private static final String SPIDER_QUESTION9 = "question9";
    private static final String SPIDER_QUESTION10 = "question10";
    private static final String SPIDER_QUESTION11 = "question11";
    private static final String SPIDER_QUESTION12 = "question12";

    private static final String TABLE_PAIN_DATA = "painData";
    private static final String PAIN_ID = "painID";
    private static final String PAIN_INTENSITY = "intensity";
    private static final String PAIN_AREA = "area";
    private static final String PAIN_DETAILS = "details";
    private static final String PAIN_HELP = "help";
    private static final String PAIN_NOT_HELP = "notHelp";
    private static final String PAIN_WORSE = "worse";
    private static final String PAIN_DATE = "date";
    private static final String PAIN_TIME = "time";
    private static final String PAIN_LONG = "longitude";
    private static final String PAIN_LAT = "latitude";

    private static final String CREATE_TABLE_USER_DATA = "CREATE TABLE " + TABLE_USER_DATA + " (" +
            USER_ID + " INTEGER PRIMARY KEY, " + USER_NAME + " TEXT, " + USER_SURNAME + " TEXT, " +
            USER_DOB + " TEXT" + ");";

    private static final String CREATE_TABLE_SPIDER_DATA = "CREATE TABLE " + TABLE_SPIDER_DATA +
            " (" + SPIDER_ID + " INTEGER PRIMARY KEY, " + SPIDER_QUESTION1 + " INTEGER, " +
            SPIDER_QUESTION2 + " INTEGER, " + SPIDER_QUESTION3 + " INTEGER, " + SPIDER_QUESTION4 +
            " INTEGER, " + SPIDER_QUESTION5 + " INTEGER, " + SPIDER_QUESTION6 + " INTEGER, " +
            SPIDER_QUESTION7 + " INTEGER, " + SPIDER_QUESTION8 + " INTEGER, " + SPIDER_QUESTION9 +
            " INTEGER, " + SPIDER_QUESTION10 + " INTEGER, " + SPIDER_QUESTION11 + " INTEGER, " +
            SPIDER_QUESTION12 + " INTEGER" + ");";

    private static final String CREATE_TABLE_PAIN_DATA = "CREATE TABLE " + TABLE_PAIN_DATA + " (" +
            PAIN_ID + " INTEGER PRIMARY KEY, " + PAIN_INTENSITY + " INTEGER, " + PAIN_AREA +
            " TEXT, " + PAIN_DETAILS + " TEXT, " + PAIN_HELP + " TEXT, " + PAIN_NOT_HELP + " TEXT, "
            + PAIN_WORSE + " TEXT, " + PAIN_DATE + " TEXT, " + PAIN_TIME + " TEXT, " + PAIN_LAT + " TEXT, "
            + PAIN_LONG + " TEXT" + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER_DATA);
        db.execSQL(CREATE_TABLE_SPIDER_DATA);
        db.execSQL(CREATE_TABLE_PAIN_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPIDER_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIN_DATA);
        onCreate(db);
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean createPainData(int intensity, String area, String details, String help,
                                  String notHelp, String worse, String date, String time, String lat, String lon) {
        Log.d(TAG, date);
        Log.d(TAG, time);
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAIN_INTENSITY, intensity);
        contentValues.put(PAIN_AREA, area);
        contentValues.put(PAIN_DETAILS, details);
        contentValues.put(PAIN_HELP, help);
        contentValues.put(PAIN_NOT_HELP, notHelp);
        contentValues.put(PAIN_WORSE, worse);
        contentValues.put(PAIN_DATE, date);
        contentValues.put(PAIN_TIME, time);
        contentValues.put(PAIN_LAT, lat);
        contentValues.put(PAIN_LONG, lon);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_PAIN_DATA, null, contentValues);
        if (result == -1) {
            Log.e(TAG, "Error with insertion!");
            return false;
        } else {
            Log.d(TAG, "Pain data added successfully!");
            return true;
        }
    }

    /**
     * TODO
     *
     * @return
     */
    public List<String> getPainDates() {
        Log.d(TAG, "Getting pain dates...");
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> painDateList = new ArrayList<String>();
        String query = "SELECT " + PAIN_DATE + " FROM " + TABLE_PAIN_DATA;
        Cursor data = db.rawQuery(query, null);
        boolean checkCursor = data.moveToFirst();
        if (checkCursor) {
            Log.d(TAG, "Adding to painDateList...");
            try {
                painDateList.add(data.getString(0));
                while (data.moveToNext()) {
                    painDateList.add(data.getString(0));
                }
            } catch (CursorIndexOutOfBoundsException e) {
                Log.e(TAG, "Error with retrieval!");
            }
        } else {
            Log.d(TAG, "No entries found!");
        }
        data.close();
        Log.d(TAG, "Returning pain dates...");
        return painDateList;
    }

    /**
     * TODO
     *
     * @return
     */
    public List<String> getPainData(String date, String time) {
        String newDate = null;
        try {
            SimpleDateFormat inputFormat =
                    new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date oldDate = inputFormat.parse(date);
            Log.d(TAG, "Old date: " + oldDate);
            newDate = outputFormat.format(oldDate);
            Log.d(TAG, "New date: " + newDate);
        } catch (ParseException e) {
            Log.e(TAG, "Error with date parsing!");
        }
        Log.d(TAG, "Getting pain data...");
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> painDataList = new ArrayList<String>();
        String query = "SELECT * FROM " + TABLE_PAIN_DATA +
                " WHERE " + PAIN_DATE + " = '" + newDate + "' AND " + PAIN_TIME + " = '" + time + "'";
        Cursor data = db.rawQuery(query, null);
        boolean checkCursor = data.moveToFirst();
        if (checkCursor) {
            Log.d(TAG, "Adding to painDataList...");
            try {
                for (int i = 0; i < 11; i++) {
                    painDataList.add(data.getString(i));
                }
                while (data.moveToNext()) {
                    for (int i = 0; i < 11; i++) {
                        painDataList.add(data.getString(i));
                    }
                }
            } catch (CursorIndexOutOfBoundsException e) {
                Log.e(TAG, "Error with retrieval!");
            }
        } else {
            Log.d(TAG, "No entries found!");
        }
        data.close();
        Log.d(TAG, "Returning pain data...");
        Log.d(TAG, painDataList.toString());
        return painDataList;
    }


    public boolean checkPainDataEmpty(String date, String time) {
        Log.d(TAG, "TIME: " + time);
        Log.d(TAG, "DATE: " + date);
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_PAIN_DATA +
                " WHERE " + PAIN_DATE + " = '" + date + "' AND " + PAIN_TIME + " = '" + time + "'";


        Cursor data = db.rawQuery(query, null);
        boolean empty = data.moveToFirst();
        Log.d(TAG, "empty: " + empty);
        data.close();
        return !empty;
    }




    public boolean updatePainData(String id, int intensity, String area, String details, String help,
                                  String notHelp, String worse, String date, String time, String lat, String lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Log.d(TAG, "UPDATING DATA");

        contentValues.put(PAIN_INTENSITY, intensity);
        contentValues.put(PAIN_AREA, area);
        contentValues.put(PAIN_DETAILS, details);
        contentValues.put(PAIN_HELP, help);
        contentValues.put(PAIN_NOT_HELP, notHelp);
        contentValues.put(PAIN_WORSE, worse);
        contentValues.put(PAIN_TIME, time);
        contentValues.put(PAIN_LAT, lat);
        contentValues.put(PAIN_LONG, lon);



        int result = db.update(TABLE_PAIN_DATA, contentValues, PAIN_ID + " = ?", new String[]{id});
        if (result == 0) {
            Log.e(TAG, "Error with updation!");
            return false;
        } else {
            Log.d(TAG, "User data updated successfully!");
            return true;
        }
    }

    /**
     * TODO
     *
     * @return
     */
    public List<String> getPainDateTime(String date) {
        String newDate = null;
        try {
            SimpleDateFormat inputFormat =
                    new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date oldDate = inputFormat.parse(date);
            Log.d(TAG, "Old date: " + oldDate);
            newDate = outputFormat.format(oldDate);
            Log.d(TAG, "New date: " + newDate);
        } catch (ParseException e) {
            Log.e(TAG, "Error with date parsing!");
        }
        Log.d(TAG, "Getting pain data times...");
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> painTimeList = new ArrayList<String>();
        String query = "SELECT * FROM " + TABLE_PAIN_DATA +
                " WHERE " + PAIN_DATE + " = '" + newDate + "'";
        Cursor data = db.rawQuery(query, null);
        boolean checkCursor = data.moveToFirst();
        if (checkCursor) {
            Log.d(TAG, "Adding to painTimeList...");
            try {
                painTimeList.add(data.getString(8));
                while (data.moveToNext()) {
                    painTimeList.add(data.getString(8));
                }
            } catch (CursorIndexOutOfBoundsException e) {
                Log.e(TAG, "Error with retrieval!");
            }
        } else {
            Log.d(TAG, "No entries found!");
        }
        data.close();
        Log.d(TAG, "Returning pain data times...");
        return painTimeList;
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean deleteRecord(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PAIN_DATA, PAIN_ID + "=" + id, null) > 0;
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean createUserData(String name, String surname, String dateOfBirth) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_SURNAME, surname);
        contentValues.put(USER_DOB, dateOfBirth);
        Log.d(TAG, "createUserData: (NAME: " + name + ", SURNAME: " + surname +
                ", DATE OF BIRTH: " + dateOfBirth + ") to " + TABLE_USER_DATA);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_USER_DATA, null, contentValues);
        if (result == -1) {
            Log.e(TAG, "Error with insertion!");
            return false;
        } else {
            Log.d(TAG, "User data added successfully!");
            return true;
        }
    }

    /**
     * TODO
     *
     * @return
     */
    public List<String> getUserData() {
        Log.d(TAG, "Getting user data...");
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> userDataList = new ArrayList<String>();
        String query = "SELECT * FROM " + TABLE_USER_DATA;
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        Log.d(TAG, "Adding to userDataList...");
        userDataList.add(data.getString(1));
        userDataList.add(data.getString(2));
        userDataList.add(data.getString(3));
        data.close();
        Log.d(TAG, "Returning user data...");
        return userDataList;
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean updateUserData(String name, String surname, String dateOfBirth) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_SURNAME, surname);
        contentValues.put(USER_DOB, dateOfBirth);
        int result = db.update(TABLE_USER_DATA, contentValues, null, null);
        if (result == 0) {
            Log.e(TAG, "Error with updation!");
            return false;
        } else {
            Log.d(TAG, "User data updated successfully!");
            return true;
        }
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean checkTableUserDataEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USER_DATA;
        Cursor data = db.rawQuery(query, null);
        boolean empty = data.moveToFirst();
        data.close();
        return !empty;
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean createSpiderData(int answer1, int answer2, int answer3, int answer4, int answer5,
                                    int answer6, int answer7, int answer8, int answer9,
                                    int answer10, int answer11, int answer12) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(checkTableSpiderDataEmpty()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SPIDER_QUESTION1, answer1);
            contentValues.put(SPIDER_QUESTION2, answer2);
            contentValues.put(SPIDER_QUESTION3, answer3);
            contentValues.put(SPIDER_QUESTION4, answer4);
            contentValues.put(SPIDER_QUESTION5, answer5);
            contentValues.put(SPIDER_QUESTION6, answer6);
            contentValues.put(SPIDER_QUESTION7, answer7);
            contentValues.put(SPIDER_QUESTION8, answer8);
            contentValues.put(SPIDER_QUESTION9, answer9);
            contentValues.put(SPIDER_QUESTION10, answer10);
            contentValues.put(SPIDER_QUESTION11, answer11);
            contentValues.put(SPIDER_QUESTION12, answer12);
            Log.d(TAG, "createSpiderData: (QUESTION1: " + answer1 + ", QUESTION2: " + answer2 +
                    ", QUESTION3: " + answer3 + ", QUESTION4: ...) to " + TABLE_SPIDER_DATA);
            long result = db.insert(TABLE_SPIDER_DATA, null, contentValues);
            if (result == -1) {
                Log.e(TAG, "Error with insertion!");
                return false;
            } else {
                Log.d(TAG, "Spider data added successfully!");
                return true;
            }
        }
        else{
            boolean result = updateSpiderData(answer1,answer2,answer3,answer4,answer5,answer6,answer7,answer8,answer9
            ,answer10,answer11,answer12);
            if (!result) {
                Log.e(TAG, "Error with insertion!");
                return false;
            } else {
                Log.d(TAG, "Spider data added successfully!");
                return true;
            }
        }
    }

    /**
     * TODO
     *
     * @return
     */
    public List<Integer> getSpiderData() {
        Log.d(TAG, "Getting spider data...");
        SQLiteDatabase db = this.getWritableDatabase();
        List<Integer> spiderDataList = new ArrayList<Integer>();
        String query = "SELECT * FROM " + TABLE_SPIDER_DATA;
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        Log.d(TAG, "Adding to spiderDataList...");
        spiderDataList.add(data.getInt(1));
        spiderDataList.add(data.getInt(2));
        spiderDataList.add(data.getInt(3));
        spiderDataList.add(data.getInt(4));
        spiderDataList.add(data.getInt(5));
        spiderDataList.add(data.getInt(6));
        spiderDataList.add(data.getInt(7));
        spiderDataList.add(data.getInt(8));
        spiderDataList.add(data.getInt(9));
        spiderDataList.add(data.getInt(10));
        spiderDataList.add(data.getInt(11));
        spiderDataList.add(data.getInt(12));
        data.close();
        Log.d(TAG, "Returning spider data...");
        Log.d(TAG, spiderDataList.toString());
        return spiderDataList;
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean updateSpiderData(int answer1, int answer2, int answer3, int answer4, int answer5,
                                    int answer6, int answer7, int answer8, int answer9,
                                    int answer10, int answer11, int answer12) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPIDER_QUESTION1, answer1);
        contentValues.put(SPIDER_QUESTION2, answer2);
        contentValues.put(SPIDER_QUESTION3, answer3);
        contentValues.put(SPIDER_QUESTION4, answer4);
        contentValues.put(SPIDER_QUESTION5, answer5);
        contentValues.put(SPIDER_QUESTION6, answer6);
        contentValues.put(SPIDER_QUESTION7, answer7);
        contentValues.put(SPIDER_QUESTION8, answer8);
        contentValues.put(SPIDER_QUESTION9, answer9);
        contentValues.put(SPIDER_QUESTION10, answer10);
        contentValues.put(SPIDER_QUESTION11, answer11);
        contentValues.put(SPIDER_QUESTION12, answer12);
        int result = db.update(TABLE_SPIDER_DATA, contentValues, null, null);
        if (result == 0) {
            Log.e(TAG, "Error with updation!");
            return false;
        } else {
            Log.d(TAG, "Spider data updated successfully!");
            return true;
        }
    }

    /**
     * TODO
     *
     * @return TO BE COMPLETED
     */
    public boolean checkTableSpiderDataEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SPIDER_DATA;
        Cursor data = db.rawQuery(query, null);
        boolean empty = data.moveToFirst();
        data.close();
        return !empty;
    }

    public List<List<String>> getPainDataMonth(String month, String year) {
        Log.d(TAG, "Getting pain data for specified month/year...");
        SQLiteDatabase db = this.getWritableDatabase();
        List<List<String>> painDataMonthList = new ArrayList<List<String>>();
        String query;
        if (month.length() == 1) {
            query = "SELECT * FROM " + TABLE_PAIN_DATA + " WHERE " + PAIN_DATE + " LIKE '%/0" +
                    month + "/" + year + "'" + " ORDER BY " + PAIN_DATE + " ASC ";
        } else {
            query = "SELECT * FROM " + TABLE_PAIN_DATA + " WHERE " + PAIN_DATE + " LIKE '%/" + month
                    + "/" + year + "'" + " ORDER BY " + PAIN_DATE + " ASC ";
        }
        Cursor data = db.rawQuery(query, null);
        boolean checkCursor = data.moveToFirst();
        if (checkCursor) {
            Log.d(TAG, "Adding to painDataMonthList...");
            try {
                List<String> painDataList = new ArrayList<String>();
                painDataList.add(data.getString(1));
                painDataList.add(data.getString(2));
                painDataList.add(data.getString(3));
                painDataList.add(data.getString(4));
                painDataList.add(data.getString(5));
                painDataList.add(data.getString(6));
                painDataList.add(data.getString(7));
                painDataList.add(data.getString(8));
                painDataMonthList.add(painDataList);
                while (data.moveToNext()) {
                    painDataList = new ArrayList<String>();
                    painDataList.add(data.getString(1));
                    painDataList.add(data.getString(2));
                    painDataList.add(data.getString(3));
                    painDataList.add(data.getString(4));
                    painDataList.add(data.getString(5));
                    painDataList.add(data.getString(6));
                    painDataList.add(data.getString(7));
                    painDataList.add(data.getString(8));
                    painDataMonthList.add(painDataList);
                }
            } catch (CursorIndexOutOfBoundsException e) {
                Log.e(TAG, "Error with retrieval!");
            }
        } else {
            Log.d(TAG, "No entries found!");
        }
        data.close();
        Log.d(TAG, "Returning pain data for specified month/year...");
        return painDataMonthList;
    }

}