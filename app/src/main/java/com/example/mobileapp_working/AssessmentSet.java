package com.example.mobileapp_working;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

public class AssessmentSet extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 900;
    int month;
    int day;
    int year;
    public static String ASSESSMENT_TYPE = null;
    CheckBox setAssessmentObjectiveCheckBox;
    CheckBox setAssessmentPerformanceCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setAssessmentObjectiveCheckBox = findViewById(R.id.set_assessment_objective_checkbox);
        setAssessmentPerformanceCheckBox = findViewById(R.id.set_assessment_performance_checkbox);

        setAssessmentObjectiveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ASSESSMENT_TYPE = "Objective";
                setAssessmentPerformanceCheckBox.setChecked(false);
            }
        });

        setAssessmentPerformanceCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ASSESSMENT_TYPE = "Performance";
                setAssessmentObjectiveCheckBox.setChecked(false);
            }
        });

        FloatingActionButton setAssessemntFab = (FloatingActionButton) findViewById(R.id.assessment_set_fab);
        setAssessemntFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAssessment();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addAssessment() {
        DatePicker set_assessment_date_picker = findViewById(R.id.set_assessment_date_picker);

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valuesToAssessmentTable = new ContentValues();

        month = set_assessment_date_picker.getMonth() + 1;
        day = set_assessment_date_picker.getDayOfMonth();
        year = set_assessment_date_picker.getYear();

        String dateToAssessmentTable = month + "-" + day + "-" + year;

        valuesToAssessmentTable.put("class_id", ClassActivity.MASTER_CLASS_ID);
        valuesToAssessmentTable.put("assessment_type", ASSESSMENT_TYPE);
        valuesToAssessmentTable.put("assessment_due_date", dateToAssessmentTable);

        long new_assessment_id = db.insert(DBOpenHelper.TABLE_ASSESSMENTS, null,
                valuesToAssessmentTable);
        db.close();

        Intent intent = new Intent(this, ClassDetail.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);

        ASSESSMENT_TYPE = null;

    }

}
