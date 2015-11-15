package com.example.huangst.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button textbtn;
    ListView lv;
    Intent intent;
    MyAdapter adapter;
    SQLiteDatabase sqLiteDatabase;
    private NotesDatabaseHelper notesDatabaseHelper;

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = sqLiteDatabase.query(notesDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        adapter = new MyAdapter(this, cursor);
        lv.setAdapter(adapter);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        notesDatabaseHelper = new NotesDatabaseHelper(this, "notes.db", null, 1);

        sqLiteDatabase = notesDatabaseHelper.getWritableDatabase();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = sqLiteDatabase.query(notesDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
                cursor.moveToPosition(position);
                String idid = cursor.getString(cursor.getColumnIndex("_id"));
                Log.d("id", idid + "");
                String content = cursor.getString(cursor.getColumnIndex(notesDatabaseHelper.CONTENT));
                String time = cursor.getString(cursor.getColumnIndex(notesDatabaseHelper.TIME));
                String path = cursor.getString(cursor.getColumnIndex(notesDatabaseHelper.PATH));
                String video = cursor.getString(cursor.getColumnIndex(notesDatabaseHelper.VIDEO));
                Intent intent = new Intent(MainActivity.this, ReadContent.class);
                intent.putExtra("id", idid);
                intent.putExtra("content", content);
                intent.putExtra("time", time);
                intent.putExtra("path", path);
                intent.putExtra("video", video);
                startActivity(intent);


            }
        });

    }


    public void initView() {
        textbtn = (Button) findViewById(R.id.text);
        lv = (ListView) findViewById(R.id.list);


        textbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        intent = new Intent(this, AddContent.class);
        switch (v.getId()) {
            case R.id.text:
                startActivity(intent);
                break;
            default:
                break;

        }


    }
}
