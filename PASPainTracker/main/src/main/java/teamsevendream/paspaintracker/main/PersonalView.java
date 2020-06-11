package teamsevendream.paspaintracker.main;

import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class PersonalView extends AppCompatActivity {

    private static String TAG = "PersonalView";

    DatabaseHelper databaseHelper;
    private Button btnChangePersonalData;
    private Button btnGoHome;
    private TextView nameView;
    private TextView surnameView;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_view);
        nameView = findViewById(R.id.nameView);
        surnameView = findViewById(R.id.surnameView);
        dateView = findViewById(R.id.dateView);
        btnChangePersonalData = findViewById(R.id.btnChangePersonalData);
        btnGoHome = findViewById(R.id.btnGoHome);
        databaseHelper = new DatabaseHelper(this);
        viewPersonalData();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);

        btnChangePersonalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalView.this, PersonalEntry.class));
            }
        });

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalView.this, MainActivity.class));
            }
        });
    }

    private void viewPersonalData() {
        List<String> data = databaseHelper.getUserData();
        nameView.setText("NAME: " + data.get(0));
        surnameView.setText("SURNAME: " + data.get(1));
        dateView.setText("DATE OF BIRTH: " + data.get(2));
    }

}
