package teamsevendream.paspaintracker.main;

import androidx.appcompat.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class PersonalEntry extends AppCompatActivity {

    private static String TAG = "PersonalEntry";

    DatabaseHelper databaseHelper;
    private Button btnSubmitPersonalData;
    private EditText nameEntry;
    private EditText surnameEntry;
    private EditText dateEntry;

    private String dateInput;
    private String fixedMonth;
    private String fixedDay;
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_entry);
        nameEntry = findViewById(R.id.nameEntry);
        surnameEntry = findViewById(R.id.surnameEntry);
        dateEntry = (EditText) findViewById(R.id.dateEntry);
        btnSubmitPersonalData = findViewById(R.id.btnSubmitPersonalData);
        databaseHelper = new DatabaseHelper(this);
        getUserData();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);

        btnSubmitPersonalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEntry.getText().toString();
                String surname = surnameEntry.getText().toString();
                if (nameEntry.length() != 0 && surnameEntry.length() != 0
                                            && dateEntry.length() != 0) {
                    if(dateEntry.length() != 0){
                        AddData(name, surname, dateEntry.getText().toString());
                    }
                    else{
                        AddData(name, surname, dateInput);
                    }
                    nameEntry.setText("");
                    surnameEntry.setText("");
                    dateEntry.setText("");

                }
                else {
                    toastMessage("You must fill all the fields!");
                }
            }
        });

        dateEntry.setInputType(InputType.TYPE_NULL);
        dateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(PersonalEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                dateEntry.setText(dayOfMonth + "/" +
                                        (monthOfYear + 1) + "/" + year);
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
                picker.show();
            }
        });
    }

    /**
     * TODO
     * @return
     */
    public void AddData(String name, String surname, String dateOfBirth) {
        boolean empty = databaseHelper.checkTableUserDataEmpty();
        boolean insertData;
        boolean checkStart;
        if (empty) {
            insertData = databaseHelper.createUserData(name, surname, dateOfBirth);
            checkStart = true;
        }
        else {
            insertData = databaseHelper.updateUserData(name, surname, dateOfBirth);
            checkStart = false;
        }

        if (insertData) {
            toastMessage("Data added successfully!");
            if (checkStart) {
                startActivity(new Intent(PersonalEntry.this, SpiderInput.class));
            }
            else {
                startActivity(new Intent(PersonalEntry.this, MainActivity.class));
            }
        }
        else {
            toastMessage("Error with insertion!");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getUserData() {
        if (!databaseHelper.checkTableUserDataEmpty()) {
            List<String> data = databaseHelper.getUserData();
            nameEntry.setText(data.get(0));
            surnameEntry.setText(data.get(1));
            dateEntry.setText(data.get(2));
        }
    }

}
