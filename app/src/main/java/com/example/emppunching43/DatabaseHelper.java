package com.example.emppunching43;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_CUSTOMER_ID = "ID";
    public static final String TABLE_NAME = "LOGINDETAILS";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String Array_Intent_Values = "APP_VALUES";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "logindetail.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement= "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT," + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public boolean adOne(Login l){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME,l.getUserName());
        cv.put(COLUMN_PASSWORD,l.getPassword());

        long insert = db.insert(TABLE_NAME, null, cv);

        return insert != -1;
    }

    public List<Login> getEveryone(){

        List<Login> returnList = new ArrayList<>();

        String query="SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int userId=cursor.getInt(0);
                String userName=cursor.getString(1);
                String userPassword = cursor.getString(2);
                Login c = new Login(userId,userName,userPassword);
                returnList.add(c);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }
    public  boolean checkUserName(Login l){
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT * FROM " + TABLE_NAME + " WHERE "+COLUMN_USER_NAME+"=?";
        Cursor c = db.rawQuery(query,new String[]{l.getUserName()});
        return c.getCount() > 0;
    }
    public boolean checkUserNamePassword(Login l){
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT * FROM " + TABLE_NAME + " WHERE "+COLUMN_USER_NAME+"=? and "+COLUMN_PASSWORD+"=?";
        Cursor c = db.rawQuery(query,new String[]{l.getUserName(),l.getPassword()});
        return c.getCount() > 0;
    }
}