package com.example.emppunching43;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class StoreWeeks extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "project_details.db";
    public static final String TABLE_NAME = "project_table";
    public static final String COL_DAY = "day";
    public static final String COL_PROJECT_NAME = "project_name";
    public static final String COL_PROJECT_ID = "project_id";
    public static final String COL_CUSTOMER_NAME = "customer_name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_START_HOUR = "start_hour";
    public static final String COL_END_HOUR = "end_hour";
    public static final String COL_SHIFT = "shift";

    public StoreWeeks(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_DAY + " TEXT PRIMARY KEY NOT NULL, " +
                COL_PROJECT_NAME + " TEXT, " +
                COL_PROJECT_ID+ " INT, " +
                COL_CUSTOMER_NAME + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_START_HOUR + " TEXT, " +
                COL_END_HOUR + " TEXT, " +
                COL_SHIFT + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      
    }

    public boolean insertData(String day, String projectName,int projectId, String customerName, String desc, String startHour, String endHour, String shift) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DAY, day);
        contentValues.put(COL_PROJECT_NAME, projectName);
        contentValues.put(COL_PROJECT_ID, projectId);
        contentValues.put(COL_CUSTOMER_NAME, customerName);
        contentValues.put(COL_DESCRIPTION, desc);
        contentValues.put(COL_START_HOUR, startHour);
        contentValues.put(COL_END_HOUR, endHour);
        contentValues.put(COL_SHIFT, shift);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getDataForDate(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_PROJECT_NAME,COL_PROJECT_ID,COL_CUSTOMER_NAME, COL_DESCRIPTION, COL_START_HOUR, COL_END_HOUR, COL_SHIFT};
        String selection = COL_DAY + " = ?";
        String[] selectionArgs = {day};
        return db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }
    public boolean areAllDatesPresent(List<String> dates) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean allDatesPresent = true;
        for (String date : dates) {
            Cursor cursor = db.query(
                    TABLE_NAME,
                    null,
                    "day = ?",
                    new String[] { date },
                    null,
                    null,
                    null
            );

            if (cursor.getCount() == 0) {
                allDatesPresent = false;
                break;
            }
            cursor.close();
        }
        db.close();
        return allDatesPresent;
    }
    public List<String> getDataForDateList(String day) {
        List<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_PROJECT_NAME, COL_PROJECT_ID, COL_CUSTOMER_NAME, COL_DESCRIPTION, COL_START_HOUR, COL_END_HOUR, COL_SHIFT};
        String selection = COL_DAY + " = ?";
        String[] selectionArgs = {day};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    dataList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return dataList;
    }
}


