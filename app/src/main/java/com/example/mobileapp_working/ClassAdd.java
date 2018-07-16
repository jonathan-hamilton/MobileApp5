package com.example.mobileapp_working;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ClassAdd extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 400;

    String class_add_term_id;
    String class_add_course_mentor_id;
    String class_add_alert_id;

    EditText classAddName;
    EditText classAddStart;
    EditText classAddEnd;
    EditText classAddStatus;
    EditText classAddCourseMentorName;
    EditText classAddCourseMentorPhone;
    EditText classAddCourseMentorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView class_add_term_number = findViewById(R.id.class_add_term_number);
        class_add_term_id = String.valueOf(TermList.MASTER_TERM_ID);
        class_add_term_number.setText("Term: " + class_add_term_id);

        FloatingActionButton classAddFab = (FloatingActionButton) findViewById(R.id.class_add_fab);
        classAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addClass();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addClass() {

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valuesToClassTable = new ContentValues();
        ContentValues valuesToCourseMentorTable = new ContentValues();

        class_add_term_id = String.valueOf(TermList.MASTER_TERM_ID);

        classAddName = findViewById(R.id.class_add_name);
        classAddStart = findViewById(R.id.class_add_start);
        classAddEnd = findViewById(R.id.class_add_end);
        classAddStatus = findViewById(R.id.class_add_status);
        classAddCourseMentorName = findViewById(R.id.class_add_course_mentor_name);
        classAddCourseMentorPhone = findViewById(R.id.class_add_course_mentor_phone);
        classAddCourseMentorEmail = findViewById(R.id.class_add_course_mentor_email);

        valuesToCourseMentorTable.put("course_mentor_name", classAddCourseMentorName.getText().toString());
        valuesToCourseMentorTable.put("course_mentor_phone", classAddCourseMentorPhone.getText().toString());
        valuesToCourseMentorTable.put("course_mentor_email", classAddCourseMentorEmail.getText().toString());

        long class_add_course_mentor_id = db.insert(DBOpenHelper.TABLE_COURSE_MENTOR,
                null, valuesToCourseMentorTable);

        valuesToClassTable.put("term_id", class_add_term_id);
        valuesToClassTable.put("coursementor_id", class_add_course_mentor_id);
        valuesToClassTable.put("class_name", classAddName.getText().toString());
        valuesToClassTable.put("class_start", classAddStart.getText().toString());
        valuesToClassTable.put("class_end", classAddEnd.getText().toString());
        valuesToClassTable.put("class_status", classAddStatus.getText().toString());

        long new_class_id = db.insert(DBOpenHelper.TABLE_CLASS,null,
                valuesToClassTable);

        Intent intent = new Intent(this, ClassActivity.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);

        Toast.makeText(this, "Class added Successfully",Toast.LENGTH_LONG).show();

        db.close();
    }

}
