 package com.krish.admin.notice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

 public class UploadNotice extends AppCompatActivity {

    private CardView addImage;
    private Bitmap bitmap;
    private ImageView noticeImageview;
    private Button uploadNoticebtn;
    private final int REQ = 1;
    private EditText noticeTitle;
    private DatabaseReference reference,dbRef;
    private StorageReference storageReference;
    private ProgressDialog pd;
    String DownloadUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addImage = findViewById(R.id.addImage);
        noticeTitle = findViewById(R.id.noticeTitle);
        noticeImageview = findViewById(R.id.noticeImageview);
        uploadNoticebtn = findViewById(R.id.uploadNoticebtn);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }

        });
        uploadNoticebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noticeTitle.getText().toString().isEmpty()){
                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                }else if(bitmap == null){
                    uploadData();
                }else{
                    uploadImage();
                }
            }
        });
    }

     private void uploadImage() {
         pd.setMessage("Uploading...");
         pd.show();
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
         byte[] finalimg = baos.toByteArray();
         final StorageReference filePath;
         filePath = storageReference.child("Notice").child(finalimg+"jpg");
         final UploadTask uploadTask = filePath.putBytes(finalimg);
         uploadTask.addOnCompleteListener(UploadNotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                     uploadData();
                                 }
                             });
                         }
                     });
                 }else {
                     pd.dismiss();
                     Toast.makeText(UploadNotice.this,"Somthing went Wrong",Toast.LENGTH_SHORT).show();
                 }
             }
         });
     }
     private void uploadData() {
         dbRef = reference.child("Notice");
        final String uniqueKey = dbRef.push().getKey();

        String title = noticeTitle.getText().toString();

         Calendar callforDate = Calendar.getInstance();
         SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
         String date = currentDate.format(callforDate.getTime());

         Calendar callforTime = Calendar.getInstance();
         SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
         String time = currentTime.format(callforTime.getTime());

         NoticeData noticeData = new NoticeData(title,DownloadUrl,date,time,uniqueKey);

         dbRef.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void unused) {
                 pd.dismiss();
                 Toast.makeText(UploadNotice.this,"Notice Uploaded",Toast.LENGTH_SHORT).show();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 pd.dismiss();
                 Toast.makeText(UploadNotice.this,"Something Went Worng",Toast.LENGTH_SHORT).show();
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
             noticeImageview.setImageBitmap(bitmap);
         }
     }
 }