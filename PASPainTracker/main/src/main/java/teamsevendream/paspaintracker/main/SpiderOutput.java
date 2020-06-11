package teamsevendream.paspaintracker.main;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mapbox.android.gestures.Utils;
import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.RadarChart;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class SpiderOutput extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static String TAG = "SpiderOutput";

    DatabaseHelper databaseHelper;
    Spinner spinner;
    RadarChart radarChart;
    private Button ToVideoPage;

    private ViewGroup viewRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider_output);
        ToVideoPage = findViewById(R.id.ToVideoPage);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);

        radarChart = (RadarChart) findViewById(R.id.radar_chart);
        ToVideoPage = findViewById(R.id.ToVideoPage);
        viewRoot = findViewById(R.id.viewRoot);

        spinner = findViewById(R.id.spinner);
        databaseHelper = new DatabaseHelper(this);
        ArrayList<String> label = new ArrayList();
        label.add("Thoughts/Feelings");
        label.add("Problem Solving");
        label.add("Stress Management");
        label.add("Goals");
        label.add("Pacing");
        label.add("Flare-ups");
        label.add("Pain Effects");
        label.add("Pain Alteration");
        label.add("Medication");
        label.add("Coping");
        label.add("Relaxation");
        label.add("Others");
        ArrayList<Float> entries = new ArrayList<>();
        List<Integer> data = databaseHelper.getSpiderData();

        entries.add((float) (data.get(0)));
        entries.add(data.get(1).floatValue());
        entries.add((float) (data.get(1)));
        entries.add((float) data.get(2));
        entries.add((float) data.get(3));
        entries.add((float) data.get(4));
        entries.add((float) data.get(5));
        entries.add((float) data.get(6));
        entries.add((float) data.get(7));
        entries.add((float) data.get(8));
        entries.add((float) data.get(9));
        entries.add((float) data.get(10));
        entries.add((float) data.get(11));

        try {
            JSONObject dataSet = new JSONObject();
            dataSet.put("labels", label.toString());
            JSONObject val = new JSONObject();
            val.put("PAIN", entries.toString());
            dataSet.put("values", val.toString());
            ArrayList<ChartData> values = new ArrayList();
            values.add(new ChartData(dataSet));
            radarChart.setData(values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] videoSource = new String[]{"Understanding Pain", "Relaxation Techniques",
                "Flare-ups"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, videoSource);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }





    String videopath;
    String description1 = "In this film you will learn how chronic pain is defined and the " +
            "difference between acute and chronic pain. Understanding the difference is " +
            "important as not all pain is the result of injury or a diagnosed condition such " +
            "as arthritis or fibromyalgia. X-rays and other investigations don’t always " +
            "explain why you have pain. This can be confusing to everyone concerned. Also," +
            " it means you may look well to others even though you don’t feel it inside " +
            "– this can lead to people judging you and treating you differently. The " +
            "sensation of pain you feel is the outcome of an interaction between the body " +
            "and the central nervous system. The important thing to remember is, if you " +
            "feel pain, it is real.";

    String description2 = "Flare-ups are those times when your symptoms are at their worst." +
            " This film explores the causes of flare-ups, how to avoid them and how to " +
            "manage them when they do happen. The key message is to understand what is going" +
            " on and to have a plan so you can get through a flare-up.";

    String description3 = "This film explores the importance of relaxation in managing pain." +
            " Stress is an integral part of the pain experience and relaxation has a key " +
            "role to play in reducing stress and toning down the volume of pain. Although " +
            "relaxation can take many forms, the simplest is controlling your breathing. It" +
            " can be practiced anywhere and is helpful in reducing the effects of stress. " +
            "Other approaches are outlined and advice is given on how to adjust your " +
            "environment to get the best out any quiet moments you can create in your day.";

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        TextView videoDescription = findViewById(R.id.videoDescription);

        switch (position) {
            case 0:
                videopath = "android.resource://" + getPackageName() + "/" +
                        R.raw.understandingpain;
                videoDescription.setText(description1);
                break;
            case 1:
                videopath = "android.resource://" + getPackageName() + "/" +
                        R.raw.relaxationtechniques;
                videoDescription.setText(description3);
                break;
            case 2:
                videopath = "android.resource://" + getPackageName() + "/" + R.raw.flareups;
                videoDescription.setText(description2);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("Select a video!");
    }

    public void doThis(View v){
        Intent i = new Intent(this, VideoPage.class);
        i.putExtra("videoPath", videopath);
        startActivity(i);
    }
    

}

