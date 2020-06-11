package teamsevendream.paspaintracker.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.TimePickerDialog;
import android.widget.SeekBar;
import android.widget.EditText;
import android.text.InputType;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.widget.Toast;

public class RecordPain extends AppCompatActivity implements LocationListener {
    private static String TAG = "RecordPain";
    DatabaseHelper databaseHelper;
    private SeekBar painAnswer;
    private EditText contextEditText;
    private EditText helpedEditText;
    private EditText notHelpedEditText;
    private EditText painWorseEditText;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private EditText dateEditText;
    private EditText timeEditText;
    private Button btnSubmitPainData;
    private String dateInput;
    private String timeInput;
    private String fixedMonth;
    private String fixedDay;
    private String painPart;
    private long painTime;
    private EditText search;
    private String id;

    //values used for location
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private Location location;
    protected Context context;
    String lat;
    String provider;
    protected String latitude;
    protected String longitude;

    Intent intent;
    String date;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pain_input);
        databaseHelper = new DatabaseHelper(this);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String painTime = mdformat.format(calendar.getTime());

        intent = getIntent();

        try{
            date = intent.getExtras().getString("time");
            time = intent.getExtras().getString("date");
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }


        Log.d(TAG, "Pain TIME: " + time);
        Log.d(TAG, "date INPUT: " + date);


        painAnswer = findViewById(R.id.intensitySeekBar);
        contextEditText = findViewById(R.id.contextInput);
        helpedEditText = findViewById(R.id.helpedInput);
        notHelpedEditText = findViewById(R.id.notHelpedInput);
        painWorseEditText = findViewById(R.id.painWorseInput);
        btnSubmitPainData = findViewById(R.id.btnSubmitPainData);
        dateEditText = (EditText) findViewById(R.id.dateEdit);
        timeEditText = (EditText) findViewById(R.id.timeEdit);
        search = (EditText) findViewById(R.id.bodySearch);






        //Log.d(TAG, "time of record: " + painTime);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            Log.e(TAG, "Error"); // lets the user know there is a problem with the gps
        }



        Calendar now = Calendar.getInstance();;
        int hourIn = now.get(Calendar.HOUR_OF_DAY);
        int minuteIn = now.get(Calendar.MINUTE);

        String hour = (hourIn < 10 ? "0" : "") + Integer.toString(hourIn);


        String minute = (minuteIn < 10 ? "0" : "") + Integer.toString(minuteIn);

        timeEditText.setText(hour + ":" + minute);



        Log.d(TAG, "MINUTE AFTER: " + minute);

        timeInput = hour + minute;


        search.setInputType(InputType.TYPE_NULL);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(RecordPain.this, Search.class),1);
            }
        });

        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                datePicker = new DatePickerDialog(RecordPain.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                fixedDay = Integer.toString(dayOfMonth);
                                fixedMonth = Integer.toString(monthOfYear + 1);
                                if (dayOfMonth <= 9) {
                                    fixedDay = "0" + Integer.toString(dayOfMonth);
                                }
                                if ((monthOfYear + 1) <= 9) {
                                    fixedMonth = "0" + Integer.toString(monthOfYear + 1);
                                }
                                dateInput = fixedDay + "/" + fixedMonth + "/"
                                        + Integer.toString(year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        timeEditText.setInputType(InputType.TYPE_NULL);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);
                timePicker = new TimePickerDialog(RecordPain.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String hour = Integer.toString(selectedHour);
                                String minute = Integer.toString(selectedMinute);
                                if (selectedHour < 10){
                                    hour = String.format("%02d", selectedHour);
                                }
                                if (selectedMinute < 10){
                                    minute = String.format("%02d", selectedMinute);
                                }
                                timeEditText.setText(hour + ":" + minute);
                                timeInput = hour + ":" + minute;
                                Log.d(TAG, timeInput);
                            }
                        },hour,minute,true);
                timePicker.show();
            }

        });

        btnSubmitPainData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int seekerAnswer = painAnswer.getProgress();
                Log.d(TAG,"seek: " + seekerAnswer);

                String contextInput = contextEditText.getText().toString();
                Log.d(TAG,"context: " + contextInput);


                String whatHelpedInput = helpedEditText.getText().toString();
                Log.d(TAG, "help: " + whatHelpedInput);

                String didNotHelpInput = notHelpedEditText.getText().toString();
                Log.d(TAG, "NotHelp: " + didNotHelpInput);

                String whatMadeWorseInput = painWorseEditText.getText().toString();
                Log.d(TAG, "madeWorse" + whatMadeWorseInput);




                Log.d(TAG, "search: " + search.getText().toString());


                if (contextInput.length() != 0 && whatHelpedInput.length() != 0 && search.length() != 0
                        && dateEditText.length() != 0 && didNotHelpInput.length() != 0
                        && whatMadeWorseInput.length() != 0) {

                    Log.d(TAG, "LOCATION: " + location);

                    addData(painPart, contextInput, whatHelpedInput, didNotHelpInput,
                            whatMadeWorseInput, dateInput, timeEditText.getText().toString(), seekerAnswer, location);

                    startActivity(new Intent(RecordPain.this, MainActivity.class));

                } else {
                    toastMessage("You must fill all the fields!");
                }
            }
        });

        if (date!= null && time != null){
            getPainData(time, date);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                painPart = data.getStringExtra("Pain");
                search.setText(painPart);
            }
        }
    }


    /**
     * TODO
     * @return
     */

    public void addData(String selectedEntry, String contextInput, String whatHelpedInput,
                        String didNotHelpInput, String whatMadeWorseInput, String dateInput,
                        String timeInput, int seekerAnswer, Location location) {

        boolean recordEmpty = databaseHelper.checkPainDataEmpty(dateEditText.getText().toString(), timeInput);

        boolean insertData;

        if(recordEmpty){
            Log.d(TAG, "adding new data");
            insertData = databaseHelper.createPainData(seekerAnswer, selectedEntry, contextInput,
                    whatHelpedInput, didNotHelpInput, whatMadeWorseInput, dateInput, timeInput,
                    Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));

        }   else {
            Log.d(TAG, "updating data");
            insertData = databaseHelper.updatePainData(id, seekerAnswer, search.getText().toString(), contextInput,
                    whatHelpedInput, didNotHelpInput, whatMadeWorseInput, dateEditText.getText().toString(), timeInput,
                    Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
        }

        if (insertData) {
            toastMessage("Data added successfully!");
            finish();
        } else {
            toastMessage("Error with insertion!");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getPainData(String date, String time) {


        if (databaseHelper.checkPainDataEmpty(time, date)) {


            List<String> data = databaseHelper.getPainData(date, time);

            id = data.get(0);


            String intensityVal = data.get(1);
            painAnswer.setProgress(Integer.parseInt(intensityVal));

            search.setText(data.get(2));




            contextEditText.setText(data.get(3));

            String whatHelpedText = data.get(4);
            helpedEditText.setText(whatHelpedText);

            String whatNotHelpedText = data.get(5);
            notHelpedEditText.setText(whatNotHelpedText);

            String whatMadeWorseText = data.get(6);
            painWorseEditText.setText(whatMadeWorseText);

            String painDateText = data.get(7);
            dateEditText.setText(painDateText);

            String timeText = data.get(8);
            String[] timeVals = timeText.split(":");
            timeInput = timeVals[0] + timeVals[1];
            timeEditText.setText(data.get(8));

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (SecurityException e) {
                Log.e(TAG, "Error"); // lets the user know there is a problem with the gps
            }

//            latitude = data.get(9).toString();
//
//            longitude = data.get(10);
        }
    }
}