package teamsevendream.paspaintracker.main;

import android.Manifest;
import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    DatabaseHelper databaseHelper;
    private TextView welcomeMessage;
    private Button btnRecordSpider;
    private Button btnViewSpider;
    private Button btnViewPersonalDetails;
    private Button btnRecordPain;
    private Button btnCalendarPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcomeMessage = findViewById(R.id.WelcomeMessage);
        btnRecordSpider = findViewById(R.id.btnRecordSpider);
        btnViewSpider = findViewById(R.id.btnViewSpider);
        btnViewPersonalDetails = findViewById(R.id.btnViewPersonalDetails);
        btnRecordPain = findViewById(R.id.btnRecordPain);
        databaseHelper = new DatabaseHelper(this);
        btnCalendarPage = findViewById(R.id.btnViewHistory);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);



        setWelcomeMessage();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        btnRecordSpider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SpiderInput.class));
            }
        });
        btnViewSpider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SpiderOutput.class));
            }
        });
        btnCalendarPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CalendarPage.class));
            }
        });
        btnViewPersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PersonalView.class));
            }
        });
        btnRecordPain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecordPain.class));
            }
        });
    }

    private void setWelcomeMessage() {
        List<String> data = databaseHelper.getUserData();
        welcomeMessage.setText("WELCOME, " + data.get(0));
    }

}
