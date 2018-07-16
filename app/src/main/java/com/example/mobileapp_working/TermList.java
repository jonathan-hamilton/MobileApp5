package com.example.mobileapp_working;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TermList extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1;
    private CursorAdapter cursorAdapter;

    public static long MASTER_TERM_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        ListView termListView = (ListView) findViewById(R.id.term_listview);
        termListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openTermEdit(view);
                MASTER_TERM_ID = id;
            }
        });

        populateTermsListView();

        FloatingActionButton term_list_fab = (FloatingActionButton) findViewById(R.id.term_list_fab);
        term_list_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddTerm(view);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void populateTermsListView(){
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursor = helper.getAllData("terms");

        String[] fromFieldNames = new String[]{
                DBOpenHelper.TERM_TITLE,
                DBOpenHelper.TERM_START,
                DBOpenHelper.TERM_END
        };

        int[] toViewIds = new int[]{
                R.id.class_add_term_number,
                R.id.term_start,
                R.id.term_end
        };

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(
                getBaseContext(),
                R.layout.term_layout,
                cursor,
                fromFieldNames,
                toViewIds,
                0);
        ListView term_list = (ListView) findViewById(R.id.term_listview);
        term_list.setAdapter(myCursorAdapter);
    }

    public void openAddTerm(View v) {
        Intent intent = new Intent(this, TermAdd.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    public void openTermEdit(View v) {
        Intent intent = new Intent(this, TermEdit.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }


}

