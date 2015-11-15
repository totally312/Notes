package com.example.huangst.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.Inflater;

/**
 * Created by huangst on 15/11/13.
 */
public class AddContent extends Activity implements View.OnClickListener {

    String val, id;
    EditText et_addtext;
    Button btn_addtext, btn_back, btn_addimg;
    NotesDatabaseHelper helper;
    SQLiteDatabase db;
    ImageView img;
    VideoView video;
    File photofile, mphotofile;
    Bitmap bitmap, mbitmap;
    int flag;

    public String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = simpleDateFormat.format(curDate);
        return str;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);

        flag = 1;


        initView();


        helper = new NotesDatabaseHelper(this, "notes.db", null, 1);
        db = helper.getWritableDatabase();



    }

    public void initView() {
        et_addtext = (EditText) findViewById(R.id.et_addtext);
        img = (ImageView) findViewById(R.id.iv_addimg);
        video = (VideoView) findViewById(R.id.vv_addvideo);
        btn_addtext = (Button) findViewById(R.id.btn_addtext);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_addimg = (Button) findViewById(R.id.btn_addimg);


        btn_addtext.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_addimg.setOnClickListener(this);


    }


    public void add() {
        ContentValues values = new ContentValues();
        values.put(helper.CONTENT, et_addtext.getText().toString());
        values.put(helper.TIME, getTime());
        if (photofile != null && flag == 1) {
            values.put(helper.PATH, photofile.getAbsolutePath() + "");
            Log.d("readcontent", photofile + "");

        }


        db.insert(helper.TABLE_NAME, null, values);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addtext:
                add();
                finish();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_addimg:
                video.setVisibility(View.GONE);
                Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photofile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getTime() + ".jpg");
                photo.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
                startActivityForResult(photo, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {


            if (resultCode == RESULT_CANCELED) {

            }

            if (resultCode == RESULT_OK) {
                flag = 1;
                img.setVisibility(View.VISIBLE);
                btn_addimg.setText("更换图片");

                Log.d("readcontent", "?????");
                if (photofile != null) {
                    Log.d("readcontent", photofile + "");
                } else {
                    Log.d("readcontent", "no photofile");
                }

                bitmap = BitmapFactory.decodeFile(photofile.getAbsolutePath());
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);

                img.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        dialog();
                        return true;
                    }
                });

            }
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
    protected void onDestroy() {
        super.onDestroy();
        Log.d("readcontent", "destory");
        if (bitmap != null) {
            bitmap.recycle();
        }
    }


    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddContent.this);
        builder.setMessage("确认删除图片吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                img.setVisibility(View.GONE);
                btn_addimg.setText("添加图片");
                flag = 2;
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
