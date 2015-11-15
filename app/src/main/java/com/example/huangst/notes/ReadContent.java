package com.example.huangst.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huangst on 15/11/13.
 */
public class ReadContent extends Activity implements View.OnClickListener {


    NotesDatabaseHelper helper;
    SQLiteDatabase db;

    Button change, back, delete, addimg;
    ImageView readimg;
    EditText readtext;
    VideoView readvideo;
    String id;
    String content, time, path, video;
    Bitmap bitmap;
    File photofile;
    int flag,flag2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readcontent);

        flag = 3;//无更改照片标志

        helper = new NotesDatabaseHelper(this, "notes.db", null, 1);
        db = helper.getWritableDatabase();

        initView();

        id = getIntent().getStringExtra("id");
        content = getIntent().getStringExtra("content");
        time = getIntent().getStringExtra("time");
        path = getIntent().getStringExtra("path");
        video = getIntent().getStringExtra("video");

        Log.d("readcontent", id + "," + content + "," + time);

        readtext.setText(content);


        if (path == null) {
            Log.d("readcontent", "path==null");
            readimg.setVisibility(View.GONE);
        } else {
            readimg.setVisibility(View.VISIBLE);
            addimg.setText("更换图片");

            Log.d("readcontent", path);
            readimg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dialog();
                    return true;
                }
            });

        }
        if (video == null) {
            Log.d("readcontent", "video==null");
            readvideo.setVisibility(View.GONE);
        }


        bitmap = BitmapFactory.decodeFile(path);
        readimg.setImageBitmap(bitmap);


    }

    public String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = simpleDateFormat.format(curDate);
        return str;
    }

    public void initView() {
        change = (Button) findViewById(R.id.btn_changetext);
        back = (Button) findViewById(R.id.btn_back);
        delete = (Button) findViewById(R.id.btn_delete);
        addimg = (Button) findViewById(R.id.btn_addimg);

        readimg = (ImageView) findViewById(R.id.iv_readimg);
        readtext = (EditText) findViewById(R.id.et_readtext);
        readvideo = (VideoView) findViewById(R.id.vv_readvideo);


        change.setOnClickListener(this);
        addimg.setOnClickListener(this);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);


    }

    public void change() {
        ContentValues values = new ContentValues();
        values.put(helper.CONTENT, readtext.getText().toString());
        values.put(helper.TIME, getTime());
        if(photofile!=null && flag==1){
            values.put(helper.PATH,photofile+"");
        }
        if(flag==0){
            String a = null;
            values.put(helper.PATH,a);
        }

        db.update(helper.TABLE_NAME, values, helper.ID + " = ?", new String[]{id});

        Log.d("readcontent", "successful");

    }

    public void delete() {

        db.delete(helper.TABLE_NAME, helper.ID + " = ?", new String[]{id});
        Log.d("readcontent", "successful");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_changetext:
                change();
                finish();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_delete:
                delete();
                finish();
                break;
            case R.id.btn_addimg:


                Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photofile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getTime() + ".jpg");
                photo.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
                startActivityForResult(photo, 1);
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bitmap != null) {
            bitmap.recycle();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                flag = 1;
                readimg.setVisibility(View.VISIBLE);
                bitmap = BitmapFactory.decodeFile(photofile.getAbsolutePath());
                readimg.setImageBitmap(bitmap);
                addimg.setText("更换图片");
                readimg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        dialog();
                        return true;
                    }
                });
            }
        }
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReadContent.this);
        builder.setMessage("确认删除图片吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                readimg.setVisibility(View.GONE);
                addimg.setText("添加图片");
                flag = 0;
                bitmap.recycle();


            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


}
