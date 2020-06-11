package teamsevendream.paspaintracker.main;


//import android.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class ViewPain extends AppCompatActivity {

    private static String TAG = "ViewPain";
    private static String lat;
    private static String lon;
    DatabaseHelper databaseHelper;
    private TextView bodyPart;
    private TextView context;
    private TextView whatHelped;
    private TextView whatNotHelped;
    private TextView worse;
    private TextView intensityTitle;
    private SeekBar viewIntensity;
    private TextView painDate;
    String recordIDtext;
    private Button btnMap;
    private Button btnDelete;
    private Button btnEdit;
    private ImageView imgBodyArea;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pain);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);

        Intent intent = getIntent();
        String date = intent.getExtras().getString("date");
        String time = intent.getExtras().getString("time");

        Log.d(TAG, "View Pain " + date);


        bodyPart = findViewById(R.id.viewBodyPart);
        context = findViewById(R.id.viewPainContext);
        whatHelped = findViewById(R.id.viewPainWhatHelped);
        whatNotHelped = findViewById(R.id.viewPainWhatNotHelped);
        worse = findViewById(R.id.viewPainWorse);
        viewIntensity = findViewById(R.id.viewPainSeekBar);
        painDate = findViewById(R.id.painDate);
        btnMap = findViewById(R.id.btnMap);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEditPain);
        imgBodyArea = findViewById(R.id.imgBodyArea);
        intensityTitle = findViewById(R.id.textView3);


        databaseHelper = new DatabaseHelper(this);

        viewPainData(date, time);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(ViewPain.this, MapPage.class);
                j.putExtra("lat", lat);
                j.putExtra("lon", lon);
                Log.d(TAG, "showing pain page...");
                startActivity(j);
            }
        });

        viewIntensity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deleteRecord(recordIDtext);

                startActivity(new Intent(ViewPain.this, MainActivity.class));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ViewPain.this, RecordPain.class);

                intent.putExtra("date", date);
                intent.putExtra("time", time);

                startActivity(intent);

            }
        });

    }

    private void viewPainData(String date, String time) {

        List<String> data = databaseHelper.getPainData(date, time);

        String recordId = data.get(0);
        recordIDtext = recordId;

        String intensityVal = data.get(1);
        intensityTitle.setText("3. INTENSITY OF PAIN:  " + intensityVal);
        viewIntensity.setProgress(Integer.parseInt(intensityVal));

        String bodyPartText = data.get(2);
        String area = getBodyArea(bodyPartText);
        Log.i(TAG, "Area: " + area);

        bodyPart.setText(bodyPartText);

        String contextText = data.get(3);
        context.setText(contextText);

        String whatHelpedText = data.get(4);
        whatHelped.setText(whatHelpedText);

        String whatNotHelpedText = data.get(5);
        whatNotHelped.setText(whatNotHelpedText);

        String whatMadeWorseText = data.get(6);
        worse.setText(whatMadeWorseText);

        String painDateText = data.get(7);
        painDate.setText(painDateText);

        lat = data.get(9);
        lon = data.get(10);
        Log.d(TAG, data.toString());
        Log.d(TAG, "Lat: " + lat.toString() + " long: " + lon.toString());

    }

    private String getBodyArea(String bodyPart) {
        Log.i(TAG, "Getting body area for " + bodyPart);
        String[] split = bodyPart.split(" ");

        List<String> head = Arrays.asList("Forehead", "Head", "Temple", "Ear", "Eye", "Cheek",
                "Nose", "Mouth", "Chin");
        String neck = "Neck";
        List<String> chest = Arrays.asList("Chest", "Abdomen");
        List<String> shoulder = Arrays.asList("Armpit", "Shoulder", "Shoulder Blade");
        String back = "Back";
        List<String> hip = Arrays.asList("Loin", "Hip", "Groin");
        List<String> arm = Arrays.asList("Arm", "Forearm");
        List<String> hand = Arrays.asList("Wrist", "Palm", "Fingers");
        String elbow = "Elbow";
        String butt = "Buttocks";
        List<String> leg = Arrays.asList("Thigh", "Kneecap", "Shin", "Calf");
        List<String> foot = Arrays.asList("Ankle", "Heel", "Instep", "Sole", "Toe");

        //for the parts which only have 1 word
        if (split.length == 1) {
            if (head.contains(split[0])) {
                imgBodyArea.setImageResource(R.mipmap.head);
                return "head";
            } else if (chest.contains(split[0])) {
                imgBodyArea.setImageResource(R.mipmap.chest);
                return "chest";
            } else if (back.equals(split[0])) {
                imgBodyArea.setImageResource(R.mipmap.back);
                return "back";
            } else if (hip.contains(split[0])) {
                imgBodyArea.setImageResource(R.mipmap.hip);
                return "hip";
            }
            else if (butt.equals(split[0])) {
                imgBodyArea.setImageResource(R.mipmap.butt);
                return "butt";
            }
            return null;
        }
        //for the parts which only have 3 or more words
        else if (split.length >= 3) {
            if (shoulder.contains(split[1])) {
                if (split[0] == "Right") {
                    imgBodyArea.setImageResource(R.mipmap.right_shoulder);
                    return "right_shoulder";
                }
                imgBodyArea.setImageResource(R.mipmap.left_shoulder);
                return "left_shoulder";
            } else if (arm.contains(split[2])) {
                if (split[0] == "Right") {
                    imgBodyArea.setImageResource(R.mipmap.right_arm);
                    return "right_arm";
                }
                imgBodyArea.setImageResource(R.mipmap.left_arm);
                return "left_arm";
            } else if (hand.contains(split[2])) {
                imgBodyArea.setImageResource(R.mipmap.hand);
                return "hand";
            } else if (leg.contains(split[2])) {
                imgBodyArea.setImageResource(R.mipmap.leg);
                return "leg";
            }
            return null;
        }
        //the rest of the body parts
        else {
            Log.i(TAG, split[0] + " " + split[1]);
            if (neck.equals(split[0])) {
                imgBodyArea.setImageResource(R.mipmap.neck);
                return "neck";
            } else if (butt.equals(split[1])) {
                imgBodyArea.setImageResource(R.mipmap.butt);
                return "butt";
            } else if (head.contains(split[1]) | head.contains(split[0])) {

                imgBodyArea.setImageResource(R.mipmap.head);
                return "head";
            } else if (chest.contains(split[1])) {
                imgBodyArea.setImageResource(R.mipmap.chest);
                return "chest";
            } else if (shoulder.contains(split[1])) {
                if (split[0] == "Right") {
                    imgBodyArea.setImageResource(R.mipmap.right_shoulder);
                    return "right_shoulder";
                }
                imgBodyArea.setImageResource(R.mipmap.left_shoulder);
                return "left_shoulder";
            } else if (hip.contains(split[1])) {
                imgBodyArea.setImageResource(R.mipmap.hip);
                return "hip";
            } else if (arm.contains(split[1])) {
                if (split[0] == "Right") {
                    imgBodyArea.setImageResource(R.mipmap.right_arm);
                    return "right_arm";
                }
                imgBodyArea.setImageResource(R.mipmap.left_arm);
                return "left_arm";
            } else if (hand.contains(split[1])) {
                imgBodyArea.setImageResource(R.mipmap.hand);
                return "hand";
            } else if (leg.contains(split[1])) {
                imgBodyArea.setImageResource(R.mipmap.leg);
                return "leg";
            } else if (foot.contains(split[1])) {
                imgBodyArea.setImageResource(R.mipmap.foot);
                return "foot";
            } else if (elbow.equals(split[1])) {
                if (split[0] == "Right") {
                    imgBodyArea.setImageResource(R.mipmap.right_elbow);
                    return "right_elbow";
                }
                imgBodyArea.setImageResource(R.mipmap.left_elbow);
                return "left_elbow";
            }
            return null;
        }

    }

}
