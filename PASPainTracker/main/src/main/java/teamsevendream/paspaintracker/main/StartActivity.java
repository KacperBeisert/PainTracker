package teamsevendream.paspaintracker.main;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private static String TAG = "StartActivity";

    DatabaseHelper databaseHelper;
    private Button btnGetStarted;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);

        btnGetStarted = findViewById(R.id.btnGetStarted);
        databaseHelper = new DatabaseHelper(this);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean empty = databaseHelper.checkTableUserDataEmpty();
                if (empty) {
                    startActivity(new Intent(StartActivity.this, PersonalEntry.class));
                }
                else {
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                }
            }
        });
    }

}

