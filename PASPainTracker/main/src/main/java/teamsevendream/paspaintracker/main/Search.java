package teamsevendream.paspaintracker.main;

import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.graphics.Color;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.Toast;

import android.util.Log;

public class Search extends AppCompatActivity {
    private static String TAG = "Search";
    private SearchView searchView;
    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bodypart_search);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);
        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.lv1);
        String items[] = new String[] {
                "Forehead", "Head (Back)", "Left Temple", "Right Temple",
                "Left Ear", "Right Ear", "Left Eye", "Right Eye", "Left Cheek", "Right Cheek",
                "Nose", "Mouth", "Chin", "Neck (Front)", "Neck (Back)", "Chest", "Abdomen",
                "Left Armpit", "Right Armpit", "Left Shoulder", "Right Shoulder",
                "Left Shoulder Blade", "Right Shoulder Blade", "Back", "Loin",
                "Left Upper Arm (Front)", "Right Upper Arm (Front)", "Left Upper Arm (Back)",
                "Right Upper Arm (Back)", "Left Elbow", "Right Elbow", "Left Forearm (Front)",
                "Right Forearm (Front)", "Left Forearm (Back)", "Right Forearm (Back)",
                "Left Wrist", "Right Wrist", "Left Palm", "Right Palm", "Left Fingers",
                "Right Fingers", "Left Hip", "Right Hip", "Buttocks", "Groin", "Left Thigh (Front)",
                "Right Thigh (Front)", "Left Thigh (Back)", "Right Thigh (Back)", "Left Kneecap",
                "Right Kneecap", "Left Shin", "Right Shin", "Left Calf", "Right Calf", "Left Ankle",
                "Right Ankle", "Left Heel", "Right Heel", "Left Instep", "Right Instep", "Left Sole",
                "Right Sole", "Left Toe", "Right Toe"};
        list = new ArrayList(Arrays.asList(items));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView item = (TextView) super.getView(position,convertView,parent);
                item.setTextColor(Color.parseColor("#FFFFFF"));
                return item;
            }
        };
        listView.setAdapter(adapter);
        listView.setClickable(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (list.contains(query)) {
                    adapter.getFilter().filter(query);
                } else {
                    Toast.makeText(Search.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                Log.d(TAG,parent.getItemAtPosition(position).toString());
                selected = parent.getItemAtPosition(position).toString();
                Intent data = new Intent();
                data.putExtra("Pain",selected);
                setResult(RESULT_OK, data);
                finish();
            }

        });
    }
}