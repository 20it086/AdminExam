package com.krish.admin.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krish.admin.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView csDepartment,meDepartment,civilDepartment,computerDepartment;
    private LinearLayout csNoData,meNoData,civilNoData,computerNoData;
    private List<TeacherData> list1,list2,list3,list4;
    private DatabaseReference  reference,dbRef;
    private TeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        fab = findViewById(R.id.fab);
        csDepartment = findViewById(R.id.csDepartment);
        meDepartment = findViewById(R.id.meDepartment);
        civilDepartment = findViewById(R.id.civilDepartment);
        computerDepartment = findViewById(R.id.computerDepartment);

        csNoData = findViewById(R.id.csNoData);
        meNoData = findViewById(R.id.meNoData);
        civilNoData = findViewById(R.id.civilNoData);
        computerNoData = findViewById(R.id.computerNoData);

        reference = FirebaseDatabase.getInstance().getReference().child("teacher");

        csDepartment();
        meDepartment();
        civilDepartment();
        computerDepartment();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateFaculty.this,AddTeacher.class);
                startActivity(intent);
            }
        });
    }

    private void csDepartment() {
        dbRef = reference.child("Computer Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<TeacherData>();
                if (!snapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else {
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list1,UpdateFaculty.this,"Computer Science");
                    csDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); 
    }
    private void meDepartment() {
        dbRef = reference.child("Mechanical");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<TeacherData>();
                if (!snapshot.exists()){
                    meNoData.setVisibility(View.VISIBLE);
                    meDepartment.setVisibility(View.GONE);
                }else {
                    meNoData.setVisibility(View.GONE);
                    meDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    meDepartment.setHasFixedSize(true);
                    meDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list2,UpdateFaculty.this,"Mechanical");
                    meDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void civilDepartment() {
        dbRef = reference.child("Civil");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3 = new ArrayList<TeacherData>();
                if (!snapshot.exists()){
                    civilNoData.setVisibility(View.VISIBLE);
                    civilDepartment.setVisibility(View.GONE);
                }else {
                    civilNoData.setVisibility(View.GONE);
                    civilDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    civilDepartment.setHasFixedSize(true);
                    civilDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list3,UpdateFaculty.this,"Civil");
                    civilDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void computerDepartment() {
        dbRef = reference.child("Computer Engineering");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4 = new ArrayList<TeacherData>();
                if (!snapshot.exists()){
                    computerNoData.setVisibility(View.VISIBLE);
                    computerDepartment.setVisibility(View.GONE);
                }else {
                    computerNoData.setVisibility(View.GONE);
                    computerDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherData data = snapshot1.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    computerDepartment.setHasFixedSize(true);
                    computerDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list4,UpdateFaculty.this,"Computer Engineering");
                    computerDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}