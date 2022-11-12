package com.krish.admin.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krish.admin.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateTeacher extends AppCompatActivity {

    private ImageView updateTeacherImage;
    private EditText updateTeacherName,updateTeacherEmail,updateTeacherPost;
    private Button updateTeacherbtn,deleteTeacherbtn;

    private String name,email,image,post;
    private ProgressDialog pd;

    private DatabaseReference reference,dbRef;
    private StorageReference storageReference;
    String DownloadUrl,category,uniqueKey;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        image = getIntent().getStringExtra("image");

        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");

        pd = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

        updateTeacherImage = findViewById(R.id.updateTeacherImage);
        updateTeacherName = findViewById(R.id.updateTeacherName);
        updateTeacherEmail = findViewById(R.id.updateTeacherEmail);
        updateTeacherPost = findViewById(R.id.updateTeacherPost);
        updateTeacherbtn = findViewById(R.id.updateTeacherbtn);
        deleteTeacherbtn = findViewById(R.id.deleteTeacherbtn);

        try {
            Picasso.get().load(image).into(updateTeacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateTeacherEmail.setText(email);
        updateTeacherName.setText(name);
        updateTeacherPost.setText(post);

        updateTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        updateTeacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = updateTeacherName.getText().toString();
                email = updateTeacherEmail.getText().toString();
                post = updateTeacherPost.getText().toString();
                checkValidation();
            }
        });
        deleteTeacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    private void deleteData() {
        reference.child(category).child(uniqueKey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateTeacher.this, "Teacher Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateTeacher.this,UpdateFaculty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacher.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation() {
        if(name.isEmpty()){
            updateTeacherName.setError("Empty");
            updateTeacherName.requestFocus();
        }else if(email.isEmpty()){
            updateTeacherEmail.setError("Empty");
            updateTeacherEmail.requestFocus();
        }else if(post.isEmpty()){
            updateTeacherPost.setError("Empty");
            updateTeacherPost.requestFocus();
        }else if (bitmap == null){
            pd.setMessage("Uploading...");
            pd.show();
            updateData(image);
        }else {
            pd.setMessage("Uploading...");
            pd.show();
            uploadImage();
        }
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Teachers ").child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UpdateTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DownloadUrl = String.valueOf(uri);
                                    updateData(DownloadUrl);
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(UpdateTeacher.this,"Somthing went Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateData(String s) {
        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("image",s);

        reference.child(category).child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                pd.dismiss();
                Toast.makeText(UpdateTeacher.this, "Teacher Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateTeacher.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UpdateTeacher.this, "Something Want Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent pickimage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimage,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ &&  resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateTeacherImage.setImageBitmap(bitmap);
        }
    }
}