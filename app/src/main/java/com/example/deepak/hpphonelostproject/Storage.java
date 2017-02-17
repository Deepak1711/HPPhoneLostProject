package com.example.deepak.hpphonelostproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created on 2/2/17.
 */


public class Storage extends SQLiteOpenHelper {

    private static final String DB_NAME = "Emergency.db";
    private static final String TABLE_NAME = "EmergencyContacts";
    private static final String COL_ONE = "contact1";
    private static final String COL_TWO = "contact2";

    public Storage(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String str = "create table " + TABLE_NAME + "(" + COL_ONE + " varchar," + COL_TWO + " varchar);";
        db.execSQL(str);
    }

    public boolean insert(ArrayList<String> str) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ONE, str.get(0));
        cv.put(COL_TWO, str.get(1));
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        if (result != -1) {
            return true;
        }
        return false;
    }


    public ArrayList<String> fetchContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] clos = {COL_ONE, COL_TWO};
        Cursor cur = db.query(TABLE_NAME, clos, null, null, null, null, null);
        ArrayList<String> str = new ArrayList<>();
        if (cur.moveToLast()) {
            str.add(cur.getString(0));
            str.add(cur.getString(1));
        }
        return str;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
