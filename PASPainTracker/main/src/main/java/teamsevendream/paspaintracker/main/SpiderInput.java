package teamsevendream.paspaintracker.main;

import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SpiderInput extends AppCompatActivity {

    private static String TAG = "SpiderInput";

    DatabaseHelper databaseHelper;
    private Button btnSubmitSpiderData;
    private SeekBar spiderAnswer1;
    private SeekBar spiderAnswer2;
    private SeekBar spiderAnswer3;
    private SeekBar spiderAnswer4;
    private SeekBar spiderAnswer5;
    private SeekBar spiderAnswer6;
    private SeekBar spiderAnswer7;
    private SeekBar spiderAnswer8;
    private SeekBar spiderAnswer9;
    private SeekBar spiderAnswer10;
    private SeekBar spiderAnswer11;
    private SeekBar spiderAnswer12;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider_input);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);

        spiderAnswer1 = findViewById(R.id.spiderAnswer1);
        spiderAnswer2 = findViewById(R.id.spiderAnswer2);
        spiderAnswer3 = findViewById(R.id.spiderAnswer3);
        spiderAnswer4 = findViewById(R.id.spiderAnswer4);
        spiderAnswer5 = findViewById(R.id.spiderAnswer5);
        spiderAnswer6 = findViewById(R.id.spiderAnswer6);
        spiderAnswer7 = findViewById(R.id.spiderAnswer7);
        spiderAnswer8 = findViewById(R.id.spiderAnswer8);
        spiderAnswer9 = findViewById(R.id.spiderAnswer9);
        spiderAnswer10 = findViewById(R.id.spiderAnswer10);
        spiderAnswer11 = findViewById(R.id.spiderAnswer11);
        spiderAnswer12 = findViewById(R.id.spiderAnswer12);
        btnSubmitSpiderData = findViewById(R.id.btnSubmitSpiderData);
        databaseHelper = new DatabaseHelper(this);

        btnSubmitSpiderData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int answer1 = spiderAnswer1.getProgress();
                int answer2 = spiderAnswer2.getProgress();
                int answer3 = spiderAnswer3.getProgress();
                int answer4 = spiderAnswer4.getProgress();
                int answer5 = spiderAnswer5.getProgress();
                int answer6 = spiderAnswer6.getProgress();
                int answer7 = spiderAnswer7.getProgress();
                int answer8 = spiderAnswer8.getProgress();
                int answer9 = spiderAnswer9.getProgress();
                int answer10 = spiderAnswer10.getProgress();
                int answer11 = spiderAnswer11.getProgress();
                int answer12 = spiderAnswer12.getProgress();
                AddData(answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8,
                        answer9, answer10, answer11, answer12);
            }
        });
    }

    /**
     * TODO
     * @return
     */
    public void AddData(int answer1, int answer2, int answer3, int answer4, int answer5,
                        int answer6, int answer7, int answer8, int answer9, int answer10,
                                                            int answer11, int answer12) {
        boolean empty = databaseHelper.checkTableSpiderDataEmpty();
        boolean insertData;
        if (empty) {
            Log.d(TAG, "Creating Data");
            insertData = databaseHelper.createSpiderData(answer1, answer2, answer3, answer4,
                    answer5, answer6, answer7, answer8, answer9, answer10, answer11, answer12);
        } else {
            Log.d(TAG, "Updating Data");
            insertData = databaseHelper.updateSpiderData(answer1, answer2, answer3, answer4,
                    answer5, answer6, answer7, answer8, answer9, answer10, answer11, answer12);
        }

        if (insertData) {
            toastMessage("Data added successfully!");
            startActivity(new Intent(SpiderInput.this, MainActivity.class));
        } else {
            toastMessage("Error with insertion!");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
