package com.example.mobileapp_working;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ClassDetail extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 500;
    public static String ALERT_TYPE;
    public static long ASSESSMENT_ID;

    String class_detail_term_id;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView class_detail_term_number = findViewById(R.id.class_detail_term_number);
        class_detail_term_id = String.valueOf(TermList.MASTER_TERM_ID);
        class_detail_term_number.setText("Term: " + getTermTitle());

        ListView assessment_dates = findViewById(R.id.class_detail_assessment_dates);
        assessment_dates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ASSESSMENT_ID = id;
                openEditAssessmentDate(view);

            }
        });

        populateClassFields();
        populateAssessmentList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public String getTermTitle() {
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT term_title FROM terms WHERE _id = " + TermList.MASTER_TERM_ID, null);
        if (result != null) {
            result.moveToFirst();
        }
        db.close();
        return result.getString(0);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_class_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }


    private void populateAssessmentList() {
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursorFromAssessmentsTable = helper.getAssessmentInfoForClassDetailList(
                "assessments", ClassActivity.MASTER_CLASS_ID);

        String[] fromFieldNames = new String[]{
                DBOpenHelper.ASSESSMENT_DUE_DATE,
                DBOpenHelper.ASSESSMENT_TYPE
        };

        int[] toViewIds = new int[]{
                R.id.assessment_date,
                R.id.assessment_type
        };

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(
                getBaseContext(),
                R.layout.assessment_layout,
                cursorFromAssessmentsTable,
                fromFieldNames,
                toViewIds,
                0);

        ListView assessment_ist = findViewById(R.id.class_detail_assessment_dates);
        assessment_ist.setAdapter(myCursorAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void populateClassFields() {
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursorFromClassTable = helper.getClassDetailInfo("class",ClassActivity.MASTER_CLASS_ID);
        Cursor cursorFromCourseMentorTable = helper.getCourseMentorDetailInfo("coursementor",
                ClassActivity.MASTER_CLASS_ID);

        TextView class_detail_name = findViewById(R.id.class_detail_name);
        TextView class_detail_start = findViewById(R.id.class_detail_start);
        TextView class_detail_end = findViewById(R.id.class_detail_end);
        TextView class_detail_status = findViewById(R.id.class_detail_status);

        class_detail_name.setText("Course: " + cursorFromClassTable.getString(3));
        class_detail_start.setText("Start: " + cursorFromClassTable.getString(4));
        class_detail_end.setText("End: " + cursorFromClassTable.getString(5));
        class_detail_status.setText("Status: " + cursorFromClassTable.getString(6));

        Cursor coursementorIdFromClassTable = helper.getCourseMentorIdFromClassTable("class",
                ClassActivity.MASTER_CLASS_ID);

        if(coursementorIdFromClassTable != null) {
            coursementorIdFromClassTable.moveToFirst();
        }

        TextView class_detail_course_mentor_name = findViewById(R.id.class_detail_course_mentor_name);
        TextView class_detail_course_mentor_phone = findViewById(R.id.class_detail_course_mentor_phone);
        TextView class_detail_course_mentor_email = findViewById(R.id.class_detail_course_mentor_email);

        class_detail_course_mentor_name.setText("Course Mentor: " + cursorFromCourseMentorTable.getString(1));
        class_detail_course_mentor_phone.setText("Course Mentor Phone:  " + cursorFromCourseMentorTable.getString(2));
        class_detail_course_mentor_email.setText("Course Mentor Email: " + cursorFromCourseMentorTable.getString(3));

    }

    public void openNotesActivity(View view) {
        Intent intent = new Intent(this, NotesListActivity.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    public void openAlertActivity(View view) {
        Intent intent = new Intent(this, AlertList.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    public void openSetAssessemntDate(View view) {
        Intent intent = new Intent(this, AssessmentSet.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    private void openEditAssessmentDate(View view) {
        Intent intent = new Intent(this, AssessmentEdit.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    public void deleteClass(MenuItem item) {
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Intent intent = new Intent(this, ClassActivity.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);

        db.delete("class", "_id = " + String.valueOf(ClassActivity.MASTER_CLASS_ID), null);
        Toast.makeText(this, "Class Deleted", Toast.LENGTH_LONG).show();
        //        db.close();
    }

    public void openEditClass(MenuItem item) {
        Intent intent = new Intent(this, ClassEdit.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }
}
