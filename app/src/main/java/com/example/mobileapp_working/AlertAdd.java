package com.example.mobileapp_working;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlertAdd extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1000;
    int month;
    int day;
    int year;

    private int interval;

    static long alertId = 0;

    CheckBox setAlertStartCheckBox;
    CheckBox setAlertEndCheckBox;
    CheckBox setAlertObjectiveCheckBox;
    CheckBox setAlertPerformanceCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setAlertStartCheckBox = findViewById(R.id.alert_add_start_checkBox);
        setAlertEndCheckBox = findViewById(R.id.alert_add_end_checkBox);
        setAlertObjectiveCheckBox = findViewById(R.id.alert_add_objective_checkBox);
        setAlertPerformanceCheckBox = findViewById(R.id.alert_add_performance_checkBox);

        setAlertStartCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertList.ALERT_TYPE = "Course Start";
                setAlertEndCheckBox.setChecked(false);
                setAlertObjectiveCheckBox.setChecked(false);
                setAlertPerformanceCheckBox.setChecked(false);
            }
        });

        setAlertEndCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertList.ALERT_TYPE = "Course End";
                setAlertStartCheckBox.setChecked(false);
                setAlertObjectiveCheckBox.setChecked(false);
                setAlertPerformanceCheckBox.setChecked(false);
            }
        });

        setAlertObjectiveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertList.ALERT_TYPE = "Objective";
                setAlertStartCheckBox.setChecked(false);
                setAlertEndCheckBox.setChecked(false);
                setAlertPerformanceCheckBox.setChecked(false);
            }
        });

        setAlertPerformanceCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertList.ALERT_TYPE = "Performance";
                setAlertStartCheckBox.setChecked(false);
                setAlertEndCheckBox.setChecked(false);
                setAlertObjectiveCheckBox.setChecked(false);
            }
        });


        FloatingActionButton alertActivityFab = (FloatingActionButton) findViewById(R.id.alert_add_fab);
        alertActivityFab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                addAlert(view);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addAlert(View view) {
        DatePicker addAlertDatePicker = findViewById(R.id.alert_add_date_picker);

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valuesToAlertsTable = new ContentValues();

        month = addAlertDatePicker.getMonth();
        day = addAlertDatePicker.getDayOfMonth();
        year = addAlertDatePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String formattedAlertDate = sdf.format(calendar.getTime());

        Date currentDate = new Date();
        long alertDateInMillis = calendar.getTime().getTime();
        interval = (int)(alertDateInMillis - currentDate.getTime());

        if(AlertList.ALERT_TYPE != null) {
            valuesToAlertsTable.put("class_id", ClassActivity.MASTER_CLASS_ID);
            valuesToAlertsTable.put("alert_due", formattedAlertDate);
            valuesToAlertsTable.put("alert_type", AlertList.ALERT_TYPE);
        }

        alertId = db.insert(DBOpenHelper.TABLE_ALERTS, null,
                valuesToAlertsTable);

        if(AlertList.ALERT_TYPE == null) {
            Toast.makeText(this, "Indicate Type of Alert", Toast.LENGTH_LONG).show();
            return;
        }else{
            createAlert(view);
        }

        db.close();
        Intent intent = new Intent(this, AlertList.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    private void createAlert(View view) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, interval);
        Date futureAlertDate = calendar.getTime();

        Toast.makeText(this, "The Alert will be triggered on " +
        futureAlertDate, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), Alert.class);
        intent.putExtra("com.example.mobileapp_working.alertId", alertId);
        startActivity(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureAlertDate.getTime(), pendingIntent);

    }
}
