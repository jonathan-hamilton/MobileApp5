package com.example.mobileapp_working;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class NotesListActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 600;
    public static long NOTE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateNoteListView();

        ListView notesListView = findViewById(R.id.notes_listview);
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NOTE_ID = id;
                openExistingNote(view);
            }
        });

        FloatingActionButton add_note_fab = (FloatingActionButton) findViewById(R.id.add_note_fab);
        add_note_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openNoteAdd(view);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateNoteListView() {
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursor = helper.getNoteInfoForList("notes", ClassActivity.MASTER_CLASS_ID);


        String[] fromFieldNames = new String[]{
                DBOpenHelper.NOTE_CREATED_ON,
                DBOpenHelper.NOTE_CONTENT
        };

        int[] toViewIds = new int[]{
                R.id.note_created,
                R.id.note_content
        };

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(
                getBaseContext(),
                R.layout.note_list_layout,
                cursor,
                fromFieldNames,
                toViewIds,
                0);

        ListView notesListView = findViewById(R.id.notes_listview);
        notesListView.setAdapter(myCursorAdapter);

    }

    private void openExistingNote(View v) {
        Intent intent = new Intent(this, NoteDetail.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }


    public void openNoteAdd(View view) {
        Intent intent = new Intent(this, NoteAdd.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

}
