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
import android.view.View;
import android.widget.EditText;

public class TermAdd extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(TermList.MASTER_TERM_ID != 0) {
            populateTermFields(TermList.MASTER_TERM_ID);
        }
        FloatingActionButton termEditFab = (FloatingActionButton) findViewById(R.id.fab1);
        termEditFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addTerm();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateTermFields(long termId) {
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursorFromTermTable = helper.getTermInfoForDetail("terms", TermList.MASTER_TERM_ID);

        EditText termNumber = findViewById(R.id.term_number);
        EditText termStart = findViewById(R.id.term_start);
        EditText termNEnd = findViewById(R.id.term_end);
    }

    private void addTerm() {
        EditText termNumber = findViewById(R.id.term_number);
        EditText termStart = findViewById(R.id.term_start);
        EditText termEnd = findViewById(R.id.term_end);



        if(R.id.class_add_term_number != 0 & R.id.term_start != 0 & R.id.term_end != 0) {
            DBOpenHelper helper = new DBOpenHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("term_title", termNumber.getText().toString());
            values.put("term_start", termStart.getText().toString());
            values.put("term_end", termEnd.getText().toString());

            long newTermId = db.insert(DBOpenHelper.TABLE_TERMS, null, values);
        }

        Intent intent = new Intent(this, TermList.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);

    }
}
