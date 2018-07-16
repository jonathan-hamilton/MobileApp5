package com.example.mobileapp_working;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static com.example.mobileapp_working.DBOpenHelper.TERM_ID;

public class ClassActivity extends AppCompatActivity {

    public static long MASTER_CLASS_ID;

    public static String class_term_id;

    private static final int EDITOR_REQUEST_CODE = 300;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView class_list = (ListView) findViewById(R.id.class_listview);
        class_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MASTER_CLASS_ID = id;
                openClassDetail(view);

            }
        });

        TextView class_term_number = findViewById(R.id.class_term_number);
        class_term_id = String.valueOf(TermList.MASTER_TERM_ID);
        class_term_number.setText("Term: " + getTermTitle());

        populateClassListView();

        ListView class_listview = findViewById(R.id.class_listview);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String getTermTitle() {
        DBOpenHelper helper = new DBOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT term_title FROM terms WHERE _id = " + TermList.MASTER_TERM_ID, null);
        if (result != null) {
            result.moveToFirst();
        }
//        db.close();
        return result.getString(0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void openClassDetail(View v) {
        Intent intent = new Intent(this, ClassDetail.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    private void populateClassListView() {
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursorFromClassTable = helper.getClassesInTerm("class", TermList.MASTER_TERM_ID);

        String[] fromFieldNames = new String[]{
                DBOpenHelper.CLASS_TITLE,
                DBOpenHelper.CLASS_START,
                DBOpenHelper.CLASS_END,
                DBOpenHelper.CLASS_STATUS,
        };

        int[] toViewIds = new int[]{
                R.id.term_number,
                R.id.class_start,
                R.id.class_end,
                R.id.class_status,

        };

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(
                getBaseContext(),
                R.layout.class_layout,
                cursorFromClassTable,
                fromFieldNames,
                toViewIds,
                0);
        ListView class_list = (ListView) findViewById(R.id.class_listview);
        class_list.setAdapter(myCursorAdapter);
    }

    private void populateClassHeader() {
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursor = helper.getAllData("class");

        String[] fromFieldNames = new String[]{
                TERM_ID
        };

        int[] toViewIds = new int[]{
                R.id.class_term_number,
        };

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(
                getBaseContext(),
                R.layout.class_layout,
                cursor,
                fromFieldNames,
                toViewIds,
                0);
        ListView term_list = (ListView) findViewById(R.id.class_listview);
        term_list.setAdapter(myCursorAdapter);
    }

    public void openClassEditActivity(View view) {
        Intent intent = new Intent(this, ClassAdd.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    public void onBackPressed(){
        TermList.MASTER_TERM_ID = 0;
        Intent intent = new Intent(this, TermList.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
        return;
    }
}
