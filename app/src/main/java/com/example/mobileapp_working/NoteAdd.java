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

public class NoteAdd extends AppCompatActivity {


    private static final int EDITOR_REQUEST_CODE = 700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton noteDetailFab = (FloatingActionButton) findViewById(R.id.note_detail_fab);
        noteDetailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addNote(view);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addNote(View view) {
        EditText noteToInput = findViewById(R.id.note_add_edittext);

        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("class_id", ClassActivity.MASTER_CLASS_ID);
        values.put("note_content", noteToInput.getText().toString());

        long newNoteId = db.insert(DBOpenHelper.TABLE_NOTES, null, values);

        Intent intent = new Intent(this, NotesListActivity.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);

    }
}
