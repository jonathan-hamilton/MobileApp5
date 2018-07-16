package com.example.mobileapp_working;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlertEdit extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1900;
    int month;
    int day;
    int year;
    private String alertType = null;

    CheckBox editAlertStartCheckBox;
    CheckBox editAlertEndCheckBox;
    CheckBox editAlertObjectiveCheckBox;
    CheckBox editAlertPerformanceCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editAlertStartCheckBox = findViewById(R.id.alert_edit_start_checkBox);
        editAlertEndCheckBox = findViewById(R.id.alert_edit_end_checkBox);
        editAlertObjectiveCheckBox = findViewById(R.id.alert_edit_objective_checkBox);
        editAlertPerformanceCheckBox = findViewById(R.id.alert_edit_performance_checkBox);

        editAlertStartCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertType = "Course Start";
                editAlertEndCheckBox.setChecked(false);
                editAlertObjectiveCheckBox.setChecked(false);
                editAlertPerformanceCheckBox.setChecked(false);
            }
        });

        editAlertEndCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertType = "Course End";
                editAlertStartCheckBox.setChecked(false);
                editAlertObjectiveCheckBox.setChecked(false);
                editAlertPerformanceCheckBox.setChecked(false);
            }
        });

        editAlertObjectiveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertType = "Objective";
                editAlertStartCheckBox.setChecked(false);
                editAlertEndCheckBox.setChecked(false);
                editAlertPerformanceCheckBox.setChecked(false);
            }
        });

        editAlertPerformanceCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertType = "Performance";
                editAlertStartCheckBox.setChecked(false);
                editAlertEndCheckBox.setChecked(false);
                editAlertObjectiveCheckBox.setChecked(false);
            }
        });

        populateAlertEdit();

        FloatingActionButton editAlertActivityFab = (FloatingActionButton) findViewById(R.id.edit_alert_fab);
        editAlertActivityFab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                updateAlert(view);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateAlertEdit() {
        DatePicker editAlertDatePicker = findViewById(R.id.alert_edit_date_picker);
        boolean isChecked = true;

        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursorFromAlertTable = helper.getAlertInfoForAlertEdit("alerts",
                AlertList.ALERT_ID);

        String alertDate = cursorFromAlertTable.getString(2);
        String[] parts = alertDate.split("-");

        int month = Integer.parseInt(parts[0]) - 1;
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        editAlertDatePicker.updateDate(year,month,day);

        alertType = cursorFromAlertTable.getString(3);
        if(alertType.equals("Course Start")){
            AlertList.ALERT_TYPE = "Course Start";
            editAlertStartCheckBox.setChecked(isChecked);
        }else if(alertType.equals("Course End")){
            AlertList.ALERT_TYPE = "Course End";
            editAlertEndCheckBox.setChecked(isChecked);
        }else if(alertType.equals("Objective")){
            AlertList.ALERT_TYPE = "Objective";
            editAlertObjectiveCheckBox.setChecked(isChecked);
        }else if(alertType.equals("Performance")){
            AlertList.ALERT_TYPE = "Performance";
            editAlertPerformanceCheckBox.setChecked(isChecked);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_alert_edit, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateAlert(View view) {
        DatePicker setAlertDatePicker = findViewById(R.id.alert_edit_date_picker);

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valuesToAlertsTable = new ContentValues();

        month = setAlertDatePicker.getMonth();
        day = setAlertDatePicker.getDayOfMonth();
        year = setAlertDatePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String formattedAlertDate = sdf.format(calendar.getTime());

        Date currentDate = new Date();
        long alertDateInMillis = calendar.getTime().getTime();
        long interval = alertDateInMillis - currentDate.getTime();

        if(alertType != null) {
            valuesToAlertsTable.put("class_id", ClassActivity.MASTER_CLASS_ID);
            valuesToAlertsTable.put("alert_due", formattedAlertDate);
            valuesToAlertsTable.put("alert_type", alertType);
        }

        long updated_alert_id = db.update("alerts", valuesToAlertsTable,
                "_id = " + AlertList.ALERT_ID, null);

        db.close();

        if(alertType == null){
            Toast.makeText(this, "Indicate Type of Alert", Toast.LENGTH_LONG).show();
            return;
        }
        else if(alertType.equals("Course Start")){
            Intent broadcastIntent = new Intent(this, Alert.class);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(
                            this.getApplicationContext(),
                            1,
                            broadcastIntent,
                            0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    interval,
                    pendingIntent);
            Toast.makeText(this,"Set START Alert",Toast.LENGTH_LONG).show();
        }
        else if(alertType.equals("Course End")){
            Intent broadcastIntent = new Intent(this, Alert.class);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(
                            this.getApplicationContext(),
                            1,
                            broadcastIntent,
                            0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    interval,
                    pendingIntent);
            Toast.makeText(this,"Set END Alert",Toast.LENGTH_LONG).show();
        }
        else if(alertType.equals("Objective")){
            Intent broadcastIntent = new Intent(this, Alert.class);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(
                            this.getApplicationContext(),
                            1,
                            broadcastIntent,
                            0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    interval,
                    pendingIntent);
            Toast.makeText(this,"Set OBJECTIVE Alert",Toast.LENGTH_LONG).show();
        }
        else if(alertType.equals("Performance")){
            Intent broadcastIntent = new Intent(this, Alert.class);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(
                            this.getApplicationContext(),
                            1,
                            broadcastIntent,
                            0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    interval,
                    pendingIntent);
            Toast.makeText(this,"Set Performance Alert",Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(this, AlertList.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);

    }

    public void deleteAlert(MenuItem menuitem){
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Toast.makeText(this, "Alert Deleted", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, AlertList.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);

        db.delete("alerts", "_id = " + AlertList.ALERT_ID, null);
        db.close();
    }

}
