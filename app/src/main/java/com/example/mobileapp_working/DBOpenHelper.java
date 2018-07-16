package com.example.mobileapp_working;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by Jonathan Hamilton on 2/25/2018.
 */

public class DBOpenHelper extends SQLiteOpenHelper{

    private static final String LOG = "DBOpenHelper";

    //Define Database
    private static final String DATABASE_NAME = "mobileApp.db";
    private static final int DATABASE_VERSION = 1;

    //Terms Table declarations //////////////////////////////////////////////////////////////////

    public static final String TABLE_TERMS = "terms";

    public static final String TERM_ID = "_id";
    public static final String TERM_TITLE = "term_title";
    public static final String TERM_START = "term_start";
    public static final String TERM_END = "term_end";
    public static final String TERM_CREATED = "term_created";

    public static final String[] ALL_TERM_COLUMNS = {
            TERM_ID,
            TERM_TITLE,
            TERM_START,
            TERM_END,
            TERM_CREATED};

    //Code to create Terms Table
    private static final String TERMS_TABLE_CREATE = "CREATE TABLE " + TABLE_TERMS + "(" +
            TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TERM_TITLE + " TEXT, " +
            TERM_START + " TEXT, " +
            TERM_END + " TEXT, " +
            TERM_CREATED + " TEXT default CURRENT_TIMESTAMP" +
            ")";

    //Class Table declarations ///////////////////////////////////////////////////////////////

    public static final String TABLE_CLASS = "class";
    public static final String CLASS_ID = "_id"; // PK
    public static final String CLASS_TERM_ID = "term_id"; // FK
    public static final String CLASS_COURSE_MENTOR_ID = "coursementor_id"; // FK
    public static final String CLASS_TITLE = "class_name";
    public static final String CLASS_START = "class_start";
    public static final String CLASS_END = "class_end";
    public static final String CLASS_STATUS = "class_status";
    public static final String CLASS_CREATED = "class_created";

    public static final String[] ALL_CLASS_COLUMNS = {
            CLASS_ID,
            CLASS_TERM_ID,
            CLASS_COURSE_MENTOR_ID,
            CLASS_TITLE,
            CLASS_START,
            CLASS_END,
            CLASS_STATUS,
            CLASS_CREATED};

    private static final String CLASS_TABLE_CREATE = "CREATE TABLE " + TABLE_CLASS + "(" +
            CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CLASS_TERM_ID + " INTEGER, " +
            CLASS_COURSE_MENTOR_ID + " INTEGER, " +
            CLASS_TITLE + " TEXT, " +
            CLASS_START + " TEXT, " +
            CLASS_END + " TEXT, " +
            CLASS_STATUS + " TEXT, " +
            CLASS_CREATED + " TEXT default CURRENT_TIMESTAMP" +
            ")";

    //Course Course Mentor Table declarations ///////////////////////////////////////////////////

    public static final String TABLE_COURSE_MENTOR = "coursementor";

    public static final String COURSE_MENTOR_ID = "_id"; // PK
    public static final String COURSE_MENTOR_NAME = "course_mentor_name"; // FK
    public static final String COURSE_MENTOR_PHONE = "course_mentor_phone"; // FK
    public static final String COURSE_MENTOR_EMAIL = "course_mentor_email"; // FK
    public static final String COURSE_MENTOR_CREATED_ON = "course_mentor_created";


    public static final String[] ALL_COURSE_MENTOR_COLUMNS = {
            COURSE_MENTOR_ID,
            COURSE_MENTOR_NAME,
            COURSE_MENTOR_PHONE,
            COURSE_MENTOR_EMAIL,
            COURSE_MENTOR_CREATED_ON };

    //Code to create Course Mentor Table
    private static final String COURSE_MENTOR_TABLE_CREATE = "CREATE TABLE " + TABLE_COURSE_MENTOR + "(" +
            COURSE_MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COURSE_MENTOR_NAME + " TEXT, " +
            COURSE_MENTOR_PHONE + " TEXT, " +
            COURSE_MENTOR_EMAIL + " TEXT, " +
            COURSE_MENTOR_CREATED_ON + " TEXT default CURRENT_TIMESTAMP" +
            ")";

    //Course Alert Table declarations ///////////////////////////////////////////////////////////////

    public static final String TABLE_ALERTS = "alerts";
    public static final String ALERT_ID = "_id"; // PK
    public static final String ALERT_CLASS_ID = "class_id"; // PK
    public static final String ALERT_DUE = "alert_due";
    public static final String ALERT_TYPE = "alert_type";
    public static final String ALERT_CREATED_ON= "alert_created";


    //Code to create Alert Table
    private static final String ALERT_TABLE_CREATE = "CREATE TABLE " + TABLE_ALERTS + "(" +
            ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ALERT_CLASS_ID + " INTEGER, " +
            ALERT_DUE + " TEXT, " +
            ALERT_TYPE + " TEXT, " +
            ALERT_CREATED_ON + " TEXT default CURRENT_TIMESTAMP" +
            ")";

    //Course Notes Table declarations ///////////////////////////////////////////////////////////////

    public static final String TABLE_NOTES = "notes";
    public static final String NOTE_ID = "_id"; // PK
    public static final String CLASS_ID_FK = "class_id"; // FK
    public static final String NOTE_CONTENT = "note_content";
    public static final String NOTE_CREATED_ON= "note_created";

    //Code to create Note Table
    private static final String NOTES_TABLE_CREATE = "CREATE TABLE " + TABLE_NOTES + "(" +
            NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CLASS_ID_FK + " TEXT, " +
            NOTE_CONTENT + " TEXT, " +
            NOTE_CREATED_ON + " TEXT default CURRENT_TIMESTAMP" +
            ")";

    //AsseSsments Table declarations ///////////////////////////////////////////////////////////////

    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String ASSESSMENT_ID = "_id"; // PK
    public static final String ASSESSMENT_CLASS_ID = "class_id"; // FK
    public static final String ASSESSMENT_TYPE = "assessment_type";
    public static final String ASSESSMENT_DUE_DATE = "assessment_due_date";
    public static final String ASSESSEMNT_CREATED_ON= "assessment_created";


    //Code to create Assessments Table
    private static final String ASSESSMENTS_TABLE_CREATE = "CREATE TABLE " + TABLE_ASSESSMENTS + "(" +
            ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ASSESSMENT_CLASS_ID + " TEXT, " +
            ASSESSMENT_TYPE + " TEXT, " +
            ASSESSMENT_DUE_DATE + " TEXT, " +
            ASSESSEMNT_CREATED_ON + " TEXT default CURRENT_TIMESTAMP" +
            ")";


    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERMS_TABLE_CREATE);
        db.execSQL(CLASS_TABLE_CREATE);
        db.execSQL(COURSE_MENTOR_TABLE_CREATE);
        db.execSQL(ALERT_TABLE_CREATE);
        db.execSQL(NOTES_TABLE_CREATE);
        db.execSQL(ASSESSMENTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_MENTOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);

        onCreate(db);
    }

    public Cursor getAllData(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();

        String where = null;
        Cursor result = db.rawQuery("SELECT * FROM " + tableName, null);
        if(result != null){
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    public Cursor getAlertDataForListView(String tableName, long classId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName + " WHERE class_id = " + classId, null);
        if(result != null){
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Cursor getCourseMentorIdFromClassTable(String tableName, long coursementor_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String where = null;
        Cursor result = db.rawQuery("SELECT * FROM " + tableName + " WHERE _id = " +
                coursementor_id,null);
        if(result != null){
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    public Cursor getClassesInTerm (String tableName, long termId){
        SQLiteDatabase db = this.getReadableDatabase();

        String where = null;
        Cursor result = db.rawQuery("SELECT * FROM " + tableName + " WHERE term_id = " + termId, null);
        if(result != null){
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    public Cursor getClassDetailInfo(String tableName, long classId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName + " WHERE _id = " + classId, null);
        if(result != null){
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    public Cursor getCourseMentorDetailInfo(String tableName, long classId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName + " WHERE _id = " + classId, null);
        if(result != null){
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    public Cursor getAssessmentInfoForClassDetailList(String tableName, long classId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName +
                " WHERE class_id = " + classId, null);
        if(result != null){
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    public Cursor getNoteInfoForList(String tableName, long classId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName +
                " WHERE class_id = " + classId, null);
        if (result != null) {
            result.moveToFirst();
        }
        return result;
    }

    public Cursor getAssmentInfoForSetAssessment(String tableName, long assessmentId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName +
                " WHERE _id = " + assessmentId, null);
        if(result != null){
            result.moveToFirst();
        }
        db.close();
        return result;

    }

    public Cursor getNoteInfoForDetail(String tableName, long noteId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName +
                " WHERE _id = " + noteId, null);
        if (result != null) {
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    public Cursor getTermInfoForDetail(String tableName, long masterTermId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName +
                " WHERE _id = " + masterTermId, null);
        if (result != null) {
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    public Cursor getTermDetailInfo(String tableName, long termId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName +
                " WHERE _id = " + termId, null);
        if (result != null) {
            result.moveToFirst();
        }
        db.close();
        return result;
    }

    public Cursor getAlertInfoForAlertEdit(String tableName, long alertId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + tableName +
                " WHERE _id = " + alertId, null);
        if (result != null) {
            result.moveToFirst();
        }
        db.close();
        return result;
    }


}
