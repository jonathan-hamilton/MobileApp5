package com.example.mobileapp_working;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ClassEdit extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 2000;
    String class_edit_term_id;
    private long COURSEMENTOR_ID;

    EditText classEditName;
    EditText classEditStart;
    EditText classEditEnd;
    EditText classEditStatus;
    EditText classEditCourseMentorName;
    EditText classEditCourseMentorPhone;
    EditText classEditCourseMentorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Class");

        TextView class_edit_term_number = findViewById(R.id.class_edit_term_number);
        class_edit_term_id = String.valueOf(TermList.MASTER_TERM_ID);
        class_edit_term_number.setText("Term: " + getTermTitle());

        classEditName = findViewById(R.id.class_edit_name);
        classEditStart = findViewById(R.id.class_edit_start);
        classEditEnd = findViewById(R.id.class_edit_end);
        classEditStatus = findViewById(R.id.class_edit_status);
        classEditCourseMentorName = findViewById(R.id.class_edit_course_mentor_name);
        classEditCourseMentorPhone = findViewById(R.id.class_edit_course_mentor_phone);
        classEditCourseMentorEmail = findViewById(R.id.class_edit_course_mentor_email);

        populateClassEditFields();

        FloatingActionButton classEditFab = (FloatingActionButton) findViewById(R.id.class_edit_fab);
        classEditFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateClass();
            }
        });
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

    private void updateClass(){
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valuesToClassTable = new ContentValues();
        ContentValues valuesToCourseMentorTable = new ContentValues();

        valuesToCourseMentorTable.put("course_mentor_name", classEditCourseMentorName.getText().toString());
        valuesToCourseMentorTable.put("course_mentor_phone", classEditCourseMentorPhone.getText().toString());
        valuesToCourseMentorTable.put("course_mentor_email", classEditCourseMentorEmail.getText().toString());

        long class_add_course_mentor_id = db.update(DBOpenHelper.TABLE_COURSE_MENTOR,
                valuesToCourseMentorTable,
                "_id = " + COURSEMENTOR_ID,
                null);

        valuesToClassTable.put("class_name", classEditName.getText().toString());
        valuesToClassTable.put("class_start", classEditStart.getText().toString());
        valuesToClassTable.put("class_end", classEditEnd.getText().toString());
        valuesToClassTable.put("class_status", classEditStatus.getText().toString());

        long new_class_id = db.update(DBOpenHelper.TABLE_CLASS,
                valuesToClassTable,
                "_id = " + ClassActivity.MASTER_CLASS_ID,
                null);

        Intent intent = new Intent(this, ClassActivity.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);

        Toast.makeText(this, "Class Updated successfully",Toast.LENGTH_LONG).show();

        db.close();

    }

    private void populateClassEditFields() {
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursorFromClassTable = helper.getClassDetailInfo("class",ClassActivity.MASTER_CLASS_ID);
        Cursor cursorFromCourseMentorTable = helper.getCourseMentorDetailInfo("coursementor",
                ClassActivity.MASTER_CLASS_ID);

        COURSEMENTOR_ID = cursorFromClassTable.getLong(2);

        classEditCourseMentorName.setText(cursorFromCourseMentorTable.getString(1));
        classEditCourseMentorPhone.setText(cursorFromCourseMentorTable.getString(2));
        classEditCourseMentorEmail.setText(cursorFromCourseMentorTable.getString(3));

        classEditName.setText(cursorFromClassTable.getString(3));
        classEditStart.setText(cursorFromClassTable.getString(4));
        classEditEnd.setText(cursorFromClassTable.getString(5));
        classEditStatus.setText(cursorFromClassTable.getString(6));

    }

}
