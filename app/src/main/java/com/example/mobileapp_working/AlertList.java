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

public class AlertList extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 800;
    public static long ALERT_ID;
    public static String ALERT_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ALERT_TYPE = null;

        populateAlertListView();

        FloatingActionButton alert_activity_fab = (FloatingActionButton) findViewById(R.id.alert_activity_fab);
        alert_activity_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAlertDetail(view);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateAlertListView() {
        DBOpenHelper helper = new DBOpenHelper(this);
        Cursor cursor = helper.getAlertDataForListView("alerts", ClassActivity.MASTER_CLASS_ID);

        String[] fromFieldNames = new String[]{
                DBOpenHelper.ALERT_DUE,
                DBOpenHelper.ALERT_TYPE
        };

        int[] toViewIds = new int[]{
                R.id.alert_due,
                R.id.alert_type
        };

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(
                getBaseContext(),
                R.layout.alert_layout,
                cursor,
                fromFieldNames,
                toViewIds,
                0);
        ListView term_list = (ListView) findViewById(R.id.alert_listview);
        term_list.setAdapter(myCursorAdapter);
    }

    public void openAlertDetail(View view) {
        Intent intent = new Intent(this, AlertAdd.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

}
