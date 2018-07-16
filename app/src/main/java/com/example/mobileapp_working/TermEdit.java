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
import android.widget.EditText;
import android.widget.Toast;


public class TermEdit extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1500;
    public static String tNum;
    public static String tStart;
    public static String tEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateTermEditFields();

        FloatingActionButton termEditFab = (FloatingActionButton) findViewById(R.id.term_edit_fab);
        termEditFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                updateTerm(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateTermEditFields() {
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursorFromTermTable = helper.getTermDetailInfo("terms", TermList.MASTER_TERM_ID);
        EditText termNumber = findViewById(R.id.term_edit_number_edittext);
        EditText termStart = findViewById(R.id.term_edit_start_edittext);
        EditText termEnd = findViewById(R.id.term_edit_end_edittext);

        termNumber.setText(cursorFromTermTable.getString(1));
        termStart.setText(cursorFromTermTable.getString(2));
        termEnd.setText(cursorFromTermTable.getString(3));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        TermList.MASTER_TERM_ID = 0;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_term_edit, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void updateTerm(View view) {
        EditText termNumber = findViewById(R.id.term_edit_number_edittext);
        EditText termStart = findViewById(R.id.term_edit_start_edittext);
        EditText termEnd = findViewById(R.id.term_edit_end_edittext);

        tNum = termNumber.getText().toString();
        tStart = termStart.getText().toString();
        tEnd = termEnd.getText().toString();

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("term_title", tNum);
        values.put("term_start", tStart);
        values.put("term_end", tEnd);

        long termId = db.update("terms",values, "_id = " +
        TermList.MASTER_TERM_ID, null);

        Toast.makeText(this, "Term Information Updated", Toast.LENGTH_LONG).show();

        populateTermEditFields();
    }

    public void openClassList(View view){
        Intent intent = new Intent(this, ClassActivity.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    public void deleteTerm(MenuItem item) {
        if(noClasses()) {
            DBOpenHelper helper = new DBOpenHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            Toast.makeText(this, "Term Deleted", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, TermList.class);
            startActivityForResult(intent, EDITOR_REQUEST_CODE);

            db.delete("terms", "_id = " + String.valueOf(TermList.MASTER_TERM_ID), null);
            db.close();
        }
    }

    private boolean noClasses() {
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from class WHERE term_id = " + TermList.MASTER_TERM_ID + ";", null, null);
        if(cursor.moveToFirst()){
            Toast.makeText(this, "Delete all classes within this term first", Toast.LENGTH_LONG).show();
            db.close();
            return false;
        }
        else {
            db.close();
            return true;
        }
    }
}
