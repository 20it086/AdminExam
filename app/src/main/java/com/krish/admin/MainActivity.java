package com.krish.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.krish.admin.faculty.UpdateFaculty;
import com.krish.admin.notice.DeleteNotice;
import com.krish.admin.notice.UploadNotice;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice,addGalleryImg,addEbook,faculty,deleteNotice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadNotice = findViewById(R.id.addNotice);
        addGalleryImg = findViewById(R.id.addGalleryImg);
        addEbook = findViewById(R.id.addEbook);
        faculty = findViewById(R.id.faculty);
        deleteNotice = findViewById(R.id.deleteNotice);
        uploadNotice.setOnClickListener(this);
        addGalleryImg.setOnClickListener(this);
        addEbook.setOnClickListener(this);
        faculty.setOnClickListener(this);
        deleteNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addNotice:
                Intent intent = new Intent(MainActivity.this, UploadNotice.class);
                startActivity(intent);
                break;
            case R.id.addGalleryImg:
                Intent intent1 = new Intent(MainActivity.this,UploadImage.class);
                startActivity(intent1);
                break;
            case R.id.addEbook:
                Intent intent2 = new Intent(MainActivity.this,UploadPdf.class);
                startActivity(intent2);
                break;
            case R.id.faculty:
                Intent intent3 = new Intent(MainActivity.this, UpdateFaculty.class);
                startActivity(intent3);
                break;
            case R.id.deleteNotice:
                Intent intent4 = new Intent(MainActivity.this, DeleteNotice.class);
                startActivity(intent4);
                break;
        }
    }
}