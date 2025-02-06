package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class verifier_activity extends AppCompatActivity {
    private TextView verifierNameText, verifierEmailText;
    private Button logoutButton;
    private ImageView profileimage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private String uid, imageUri2 ;

    private static final int profileimgcode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifier);
        verifierNameText= findViewById(R.id.verifierNameText);
        verifierEmailText = findViewById(R.id.verifierEmailText);
        profileimage = findViewById(R.id.verifierProfileImage);
        logoutButton = findViewById(R.id.logoutButton);

        saveLoginState();
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iotherc = new Intent(Intent.ACTION_GET_CONTENT);
                Iotherc.setType("image/*");
                startActivityForResult(Iotherc, profileimgcode);

            }
        });


        setupprofile();
        logoutButton.setOnClickListener(v -> logoutUser());

    }
    protected void onActivityResult(int requestcode, int resultcode, @Nullable Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (resultcode == RESULT_OK) {
            if (requestcode == profileimgcode) {
                Uri imageUri = data.getData();
                setupprofileimage(imageUri); // Call the method to store up the profile
                imageUri2 = data.getData().toString();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    profileimage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }


private  void setupprofileimage(Uri imageUri){
    DocumentReference df = fstore.collection("users").document(uid);
    //    save profile image to database
    if (imageUri != null) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("profileimage", imageUri.toString());
        fstore.collection("users").document(uid).update(userMap);
        finish();
    }

}


private void setupprofile(){
    DocumentReference df = fstore.collection("users").document(uid);

    // extract the data from the document
    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            Uri imageuri;
            Log.d("Tag" , "onSuccess:" + documentSnapshot.getData());
                verifierNameText.setText(documentSnapshot.getString("name"));
                verifierEmailText.setText(documentSnapshot.getString("email"));
            if (documentSnapshot.getString(" profileimage ") != null ){

            }
                Toast.makeText(getApplicationContext(), "setup successfull", Toast.LENGTH_SHORT).show();


        }
    });


}

    private void logoutUser() {
        mAuth.signOut();
        clearLoginState();
        startActivity(new Intent(verifier_activity.this, loginactivity.class));
        finish();
    }
    private void saveLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void clearLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }


}