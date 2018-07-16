package com.example.mobileapp_working;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;


public class AssessmentEdit extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 900;
    int month;
    int day;
    int year;
    public static String ASSESSMENT_TYPE = null;
    CheckBox editAssessmentObjectiveCheckBox;
    CheckBox editAssessmentPerformanceCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editAssessmentObjectiveCheckBox = findViewById(R.id.edit_assessment_objective_checkbox);
        editAssessmentPerformanceCheckBox = findViewById(R.id.edit_assessment_performance_checkbox);

        editAssessmentObjectiveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ASSESSMENT_TYPE = "Objective";
                editAssessmentPerformanceCheckBox.setChecked(false);
            }
        });

        editAssessmentPerformanceCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ASSESSMENT_TYPE = "Performance";
                editAssessmentObjectiveCheckBox.setChecked(false);
            }
        });

        if (ClassDetail.ASSESSMENT_ID != 0) {
            populateDatePickerAndAssessmentType();
        }

        FloatingActionButton setAssessemntFab = (FloatingActionButton) findViewById(R.id.assessment_edit_fab);
        setAssessemntFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateAssessment();
//                ASSESSMENT_TYPE = null;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateAssessment() {
        DatePicker edit_assessment_date_picker = findViewById(R.id.edit_assessment_date_picker);

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valuesToAssessmentTable = new ContentValues();

        month = edit_assessment_date_picker.getMonth() + 1;
        day = edit_assessment_date_picker.getDayOfMonth();
        year = edit_assessment_date_picker.getYear();

        String dateToAssessmentTable = month + "-" + day + "-" + year;

        valuesToAssessmentTable.put("assessment_type", ASSESSMENT_TYPE);
        valuesToAssessmentTable.put("assessment_due_date", dateToAssessmentTable);

        db.update("assessments", valuesToAssessmentTable, "_id = " +
                ClassDetail.ASSESSMENT_ID, null);
        db.close();

        Toast.makeText(this, "Assessment Updated", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, ClassDetail.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }


    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_set_assessment, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void populateDatePickerAndAssessmentType() {
        DatePicker editAssessmentDatePicker = findViewById(R.id.edit_assessment_date_picker);
        boolean isChecked = true;

        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursorFromAssessmentTable = helper.getAssmentInfoForSetAssessment("assessments",
                ClassDetail.ASSESSMENT_ID);

        String assessmentDate = cursorFromAssessmentTable.getString(3);
        String[] parts = assessmentDate.split("-");

        int month = Integer.parseInt(parts[0]) - 1;
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        editAssessmentDatePicker.updateDate(year, month, day);

        ASSESSMENT_TYPE = cursorFromAssessmentTable.getString(2);
        if (ASSESSMENT_TYPE.equals("Performance")){
            editAssessmentPerformanceCheckBox.setChecked(isChecked);
        }else if(ASSESSMENT_TYPE.equals("Objective")){
            editAssessmentObjectiveCheckBox.setChecked(isChecked);
        }
    }


    public void deleteAssessment(MenuItem item) {
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Toast.makeText(this, "Assessment Deleted", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, ClassDetail.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);

        db.delete("assessments", "_id = " + String.valueOf(ClassDetail.ASSESSMENT_ID), null);
    }
}

