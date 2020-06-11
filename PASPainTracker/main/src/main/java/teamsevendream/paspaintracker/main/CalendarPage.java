package teamsevendream.paspaintracker.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarPage extends AppCompatActivity {

    private static String TAG = "CalendarPage";

    private Button btnRecordPain;
    private Button btnExportMonth;
    DatabaseHelper databaseHelper;
    private CalendarView calendarView;
    List<EventDay> events = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);
        databaseHelper = new DatabaseHelper(this);
        btnExportMonth = findViewById(R.id.btnExportMonth);
        btnRecordPain = findViewById(R.id.btnRecordPain);
        calendarView = findViewById(R.id.calendarView);
        context = this;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);
        btnRecordPain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalendarPage.this, RecordPain.class));
            }
        });
        btnExportMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = calendarView.getCurrentPageDate();
                exportMonth(calendar);
            }
        });
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                if (events.contains(eventDay.getCalendar().getTime())) {
                    List<String> list = databaseHelper.getPainDateTime(eventDay.getCalendar().getTime().toString());
                    if (list.size() == 1){
                        Intent intent = new Intent(CalendarPage.this, ViewPain.class);
                        intent.putExtra("date", eventDay.getCalendar().getTime().toString());
                        intent.putExtra("time", list.get(0));
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                        builderSingle.setTitle("Choose a time: ");
                        String[] stringList = list.toArray(new String[0]);
                        builderSingle.setItems(stringList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which < list.size()) {
                                    Intent intent = new Intent(CalendarPage.this, ViewPain.class);
                                    intent.putExtra("date", eventDay.getCalendar().getTime().toString());
                                    intent.putExtra("time", stringList[which]);
                                    startActivity(intent);
                                }
                            }
                        });
                        AlertDialog dialog = builderSingle.create();
                        dialog.show();
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
        getEntries(calendarView);
    }

    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.activity_calendar_page);
        databaseHelper = new DatabaseHelper(this);
        btnExportMonth = findViewById(R.id.btnExportMonth);
        btnRecordPain = findViewById(R.id.btnRecordPain);
        calendarView = findViewById(R.id.calendarView);
        context = this;
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.app_name);
        btnRecordPain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalendarPage.this, RecordPain.class));
            }
        });
        btnExportMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = calendarView.getCurrentPageDate();
                exportMonth(calendar);
            }
        });
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                if (events.contains(eventDay)) {
                    List<String> list = databaseHelper.getPainDateTime(eventDay.getCalendar().getTime().toString());
                    if (list.size() == 1) {
                        Intent intent = new Intent(CalendarPage.this, ViewPain.class);
                        intent.putExtra("date", eventDay.getCalendar().getTime().toString());
                        intent.putExtra("time", list.get(0));
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                        builderSingle.setTitle("Choose a time: ");
                        String[] stringList = list.toArray(new String[0]);
                        builderSingle.setItems(stringList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which < list.size()) {
                                    Intent intent = new Intent(CalendarPage.this, ViewPain.class);
                                    intent.putExtra("date", eventDay.getCalendar().getTime().toString());
                                    intent.putExtra("time", stringList[which]);
                                    startActivity(intent);
                                }
                            }
                        });
                        AlertDialog dialog = builderSingle.create();
                        dialog.show();
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
        getEntries(calendarView);
    }

    private void getEntries(CalendarView calendar) {
        List<String> dates = databaseHelper.getPainDates();
        for (int i = 0; i < dates.size(); i++) {
            Date date = null;
            SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Log.d(TAG, "Date: " + dates.get(i));
                date = curFormater.parse(dates.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (date != null) {
                Calendar entryCalendar = Calendar.getInstance();
                entryCalendar.setTime(date);
                events.add(new EventDay(entryCalendar, R.drawable.ic_baseline_add_24px));
            }
        }
        calendar.setEvents(events);
    }

    private void exportMonth(Calendar calendar) {
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        List<String> userData = databaseHelper.getUserData();
        List<List<String>> painDataMonth = databaseHelper.getPainDataMonth(month, year);
        if (painDataMonth.size() == 0) {
            toastMessage("No records to export!");
            return;
        }
        File documentsDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!documentsDir.exists()) {
            boolean checkDir = documentsDir.mkdirs();
            Log.d("CheckDocumentsDir", checkDir + "");
        }
        File painDataDir = new File(documentsDir, "PainDataPDF");
        if (!painDataDir.exists()) {
            boolean checkDir = painDataDir.mkdirs();
            Log.d("CheckPainDataDir", checkDir + "");
        }
        try {
            File painDataFile = new File(painDataDir, month + "-" + year + ".pdf");
            FileOutputStream outStream = new FileOutputStream(painDataFile);
            PdfDocument painDoc = new PdfDocument();
            int recordCounter = 0;
            int pageNumber = 1;
            int entryLine = 0;
            while (recordCounter < painDataMonth.size()) {
                int xCoord = 10;
                int yCoord = 20;
                PdfDocument.PageInfo painDocInfo = new PdfDocument
                        .PageInfo.Builder(595, 842, pageNumber).create();
                PdfDocument.Page painDocPage = painDoc.startPage(painDocInfo);
                Canvas pageCanvas = painDocPage.getCanvas();
                Paint pagePaint = new Paint();
                pageCanvas.drawText("PAIN RECORDS: " + month + "/" + year, xCoord, yCoord, pagePaint);
                yCoord += 20;
                pageCanvas.drawText("USER: " + userData.get(0) + " " + userData.get(1) + " " +
                        userData.get(2), xCoord, yCoord, pagePaint);
                yCoord += 20;
                while (recordCounter < painDataMonth.size() && yCoord <= 805) {
                    if (entryLine == 0) {
                        yCoord += 5;
                        pageCanvas.drawText("DATE: " + painDataMonth.get(recordCounter).get(6) + " | "
                                        + "TIME: " + painDataMonth.get(recordCounter).get(7) + " | "
                                        + "AREA: " + painDataMonth.get(recordCounter).get(1) + " | "
                                        + "INTENSITY: " + painDataMonth.get(recordCounter).get(0) + " | "
                                        + "DETAILS: " + painDataMonth.get(recordCounter).get(2), xCoord,
                                yCoord, pagePaint);
                        yCoord += 20;
                        entryLine = 1;
                    }
                    else {
                        pageCanvas.drawText("HELPED: " + painDataMonth.get(recordCounter).get(3) + " | "
                                        + "DIDN'T HELP: " + painDataMonth.get(recordCounter).get(4) + " | "
                                        + "WORSE: " + painDataMonth.get(recordCounter).get(5), xCoord,
                                yCoord, pagePaint);
                        yCoord += 20;
                        recordCounter++;
                        entryLine = 0;
                    }
                }
                painDoc.finishPage(painDocPage);
                pageNumber++;
            }
            painDoc.writeTo(outStream);
            painDoc.close();
            toastMessage("PDF file exported successfully! Check 'Documents' folder!");
        } catch (IOException ioe) {
            toastMessage("Error with file creation!");
            Log.e("IOException", ioe.getMessage());
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
