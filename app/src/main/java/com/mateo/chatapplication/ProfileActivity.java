package com.mateo.chatapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView imageViewCircleProfileUpdate;
    private TextInputEditText editTextUsernameProfileUpdate;
    private Button buttonProfileUpdate;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    Uri imageUri;
    boolean imageControl = false;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageViewCircleProfileUpdate = findViewById(R.id.imageViewUsers);
        editTextUsernameProfileUpdate = findViewById(R.id.editTextUsernameProfileUpdate);
        buttonProfileUpdate = findViewById(R.id.buttonProfileUpdate);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        getUserInfo();

        imageViewCircleProfileUpdate.setOnClickListener(view -> {

            imageChooser();

        });

        buttonProfileUpdate.setOnClickListener(view -> {
            updateProfile();
        });

    }

    public void getUserInfo(){
        reference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("username").getValue().toString();
                image = snapshot.child("image").getValue().toString();

                editTextUsernameProfileUpdate.setText(name);
                if (image.equals("null")){
                    imageViewCircleProfileUpdate.setImageResource(R.drawable.baseline_account_circle_24);
                }else{
                    Picasso.get().load(image).into(imageViewCircleProfileUpdate);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateProfile(){
        String username = editTextUsernameProfileUpdate.getText().toString();
        reference.child("Users").child(firebaseUser.getUid()).child("username").setValue(username);
        if (imageControl){
            UUID randomID = UUID.randomUUID();
            String imageName = "images/"+randomID+".jpg";
            storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                    myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String filepath = uri.toString();
                            reference.child("Users").child(auth.getUid()).child("image").setValue(filepath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ProfileActivity.this, "Write to database is successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, "Write to database is not successful"+ e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });
        }else {
            reference.child("Users").child(auth.getUid()).child("image").setValue(image);
        }

        Intent i = new Intent(ProfileActivity.this,MainActivity.class);
        i.putExtra("username",username);
        startActivity(i);
        finish();
    }

    public void imageChooser(){
        Intent view = new Intent();
        view.setType("image/*");
        view.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(view,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode ==RESULT_OK && data!=null){

            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewCircleProfileUpdate);
            imageControl = true;
        }else {

            imageControl = false;
        }
    }



}