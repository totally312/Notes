package com.example.huangst.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by huangst on 15/11/12.
 */
public class NotesDatabaseHelper extends SQLiteOpenHelper {

    public String TABLE_NAME = "notes"; //表的名字
    public String CONTENT = "content";
    public String TIME = "time";
    public String ID = "_id";//记得id前面加下划线
    public String PATH = "path";
    public String VIDEO = "video";


    public NotesDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


    }

    @Override


    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + ID + " integer primary key autoincrement,"
                + CONTENT + " text,"
                + TIME + " text,"
                + PATH + " text,"
                + VIDEO + " text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
