package com.vivek.tripanalyzer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.vivek.tripanalyzer.models.Transactions;

import java.util.HashMap;

public class TripActivity extends AppCompatActivity {

    EditText amount;
    EditText description;

    Button makeTransaction;
    Button chat;

    TextView upload;
    ImageView tripImage;

    StorageReference storageReference;
    DatabaseReference referenceToTrip;
    public static final int IMAGE_REQUEST = 1;


    String tripKey;
    String memName;
    String imageUrl;
    int memId = 0;
    String tripName;

    int amountToPay;
    String descriptionOfAmount;
    int noOfTransactios;
    ConstraintLayout backImage;
    ProgressDialog pd;
    private StorageTask uploadTask;
    private Uri imageUri;
    ProgressDialog pdUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        final Intent intent = getIntent();

        pdUpload = new ProgressDialog(this);

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tripKey = intent.getStringExtra("tripKey");
        memName = intent.getStringExtra("memberName");
        memId = intent.getIntExtra("memberId", 0);
        backImage = findViewById(R.id.back_image);
        Drawable backGround = backImage.getBackground();

        DatabaseReference referenceToTrip = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey);

        referenceToTrip.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("Name").getValue();
                tripName = title;
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        upload = findViewById(R.id.uploadText);
        tripImage = findViewById(R.id.tripImage);

        storageReference = FirebaseStorage.getInstance().getReference().child("trip uploads");
        referenceToTrip = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey);

        referenceToTrip.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("tripImageUrl").getValue().equals("default")) {
                    Glide.with(TripActivity.this).load(dataSnapshot.child("tripImageUrl").getValue()).into(tripImage);
                    pdUpload.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final String memberWithId = "member" + memId;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("Transactions");
        final DatabaseReference referenceToMember = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("Members").child(memberWithId);
        final DatabaseReference referenceToTotalExpend = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey).child("Total Expenditure");
        final Intent intent1 = new Intent(this, TransactionsActivity.class);
        final Intent intent3 = new Intent(this, MemberList.class);


        amount = findViewById(R.id.amount);
        description = findViewById(R.id.description);
        makeTransaction = findViewById(R.id.submitTransaction);
//        overview = findViewById(R.id.overview);
//        allTransactions = findViewById(R.id.allTransactions);
//        memberList = findViewById(R.id.memberList);
        chat = findViewById(R.id.chat);

        referenceToMember.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    noOfTransactios = (int) dataSnapshot.getChildrenCount() + 1;

                    Log.d("snapppp", "yayyayya snapshot exist");
                    Log.d("snappppCount", String.valueOf(noOfTransactios));
                } else {
                    noOfTransactios = 1;
                    Log.d("snapppp", "yayyayya snapshot dunno exist");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        makeTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountToPay = Integer.parseInt(amount.getText().toString());
                descriptionOfAmount = description.getText().toString();



                referenceToMember.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int memberExpenditure = ((Long) dataSnapshot.child("expenditure by this member").getValue()).intValue() + amountToPay;
                        referenceToMember.child("expenditure by this member").setValue(memberExpenditure);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                referenceToTotalExpend.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int TotalExpend = ((Long) dataSnapshot.getValue()).intValue() + amountToPay;
                        referenceToTotalExpend.setValue(TotalExpend);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Log.d("checkc", String.valueOf(amountToPay));
                Log.d("checkc", descriptionOfAmount);
                Log.d("checkc", String.valueOf(memId));
                Log.d("checkc", memName);
                Log.d("checkc", imageUrl);

                reference.child("Transaction" + noOfTransactios).child("amount").setValue(amountToPay);
                reference.child("Transaction" + noOfTransactios).child("description").setValue(descriptionOfAmount);
                reference.child("Transaction" + noOfTransactios).child("memberName").setValue(memName);
                reference.child("Transaction" + noOfTransactios).child("memberId").setValue(memId);
                reference.child("Transaction" + noOfTransactios).child("imageUrl").setValue(imageUrl);
                intent1.putExtra("tripKey", tripKey);
                startActivity(intent1);

                amount.setText(null);
                description.setText(null);

            }
        });
        final Intent intent2 = new Intent(this, OverviewActivity.class);
        final Intent intent4 = new Intent(this, ChatActivity.class);


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent4.putExtra("tripKey", tripKey);
                Log.d("hello", String.valueOf(memId));
                intent4.putExtra("memberId", memId);
                intent4.putExtra("memberName", memName);
                intent4.putExtra("imageUrl", imageUrl);
                startActivity(intent4);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


    }

    private void openImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = TripActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {

        pdUpload.setMessage("Uploading");
        pdUpload.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        String memberWithId = "member" + memId;

                        referenceToTrip = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripKey);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("tripImageUrl", mUri);
                        referenceToTrip.updateChildren(map);

                    } else {
                        Toast.makeText(TripActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pdUpload.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TripActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pdUpload.dismiss();
                }
            });

        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(this, "Upload is in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(TripActivity.this, MemberList.class);
        Intent intent1 = new Intent(TripActivity.this, OverviewActivity.class);
        Intent intent2 = new Intent(TripActivity.this, TransactionsActivity.class);
        Intent intent3 = new Intent(TripActivity.this, ProfileActivity.class);
        switch (item.getItemId()) {
            case R.id.my_profile:
                intent3.putExtra("tripKey", tripKey);
                intent3.putExtra("memId", memId);
                startActivity(intent3);
                break;


            case R.id.members:
                intent.putExtra("tripKey", tripKey);
                startActivity(intent);
                break;

            case R.id.overview:
                intent1.putExtra("tripKey", tripKey);
                startActivity(intent1);
                break;


            case R.id.share_key:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT,"To join trip please use the following credentials" + "\n" + "\n"
                                + "Trip Name: " + tripName + "\n"
                                + "KEY: "  + "\"" + tripKey + "\"");

                share.setType("text/plain");

                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(share, "Share Info Via ..."));
                break;

            case R.id.transactions:
                intent2.putExtra("tripKey", tripKey);
                startActivity(intent2);
                break;

        }
        return true;
    }
}
