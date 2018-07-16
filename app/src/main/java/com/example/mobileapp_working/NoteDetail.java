package com.example.mobileapp_working;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.mobileapp_working.DBOpenHelper.NOTE_ID;
import static com.example.mobileapp_working.DBOpenHelper.TABLE_NOTES;

public class NoteDetail extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateNoteInfo();

        FloatingActionButton noteDetailFab = (FloatingActionButton) findViewById(R.id.note_detail_fab);
        noteDetailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateNote(view);

            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private long updateNote(View view) {
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues newNoteText = new ContentValues();
        EditText updateNoteEditText = findViewById(R.id.note_detail_edittext);
        newNoteText.put("note_content", updateNoteEditText.getText().toString());

        long noteId = db.update(DBOpenHelper.TABLE_NOTES, newNoteText,
                "_id="+ NotesListActivity.NOTE_ID, null);

        db.close();
        populateNoteInfo();
        Intent intent = new Intent(this, NotesListActivity.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
//        NotesListActivity.NOTE_ID = 0;
        return noteId;
    }

    private void populateNoteInfo() {
        EditText noteInfo = findViewById(R.id.note_detail_edittext);
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursorFromNotesTable = helper.getNoteInfoForDetail("notes",
                NotesListActivity.NOTE_ID);

        cursorFromNotesTable.moveToFirst();
        noteInfo.setText(cursorFromNotesTable.getString(2));
    }


    public void deleteNote(MenuItem item) {
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Intent intent = new Intent(this, NotesListActivity.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);

        db.delete("notes", "_id = " + String.valueOf(NotesListActivity.NOTE_ID), null);
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_LONG).show();
    }

    public void emailNote(MenuItem item) {
            Log.i("Send email", "");

            String[] TO = {"someone@gmail.com"};
            String[] CC = {"xyz@gmail.com"};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");


            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_CC, CC);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
                Log.i("Finished sending email...", "");
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this,
                        "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
    }
}
