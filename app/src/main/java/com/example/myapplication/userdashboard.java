package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class userdashboard extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView dataTextView;
    private TextView usernametext , useremailtaxt;
    private Button logoutButton;
    private Button Document , newform , Getdata;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private String uid, imageUri2 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdashboard);
        dataTextView = findViewById(R.id.viewdata);
        logoutButton = findViewById(R.id.logoutButton);
        Document = findViewById(R.id.Document);
        usernametext = findViewById(R.id.usernametext);
        useremailtaxt = findViewById(R.id.useremailtext);

        newform = findViewById(R.id.newform);
        Getdata = findViewById(R.id.getdata);
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        setupprofile();

        newform.setOnClickListener(v -> opendocsactivity());

        Getdata.setOnClickListener(v -> viewdocsactivity());

        logoutButton.setOnClickListener(v -> logoutUser());
        // Save login state in shared preferences
        saveLoginState();



    }
    private void viewdocsactivity () {

        startActivity(new Intent(userdashboard.this, ViewDocumentsActivity.class));
    }

    private void opendocsactivity() {

        startActivity(new Intent(userdashboard.this, uploaddocuments_activity.class));
    }


    private void setupprofile() {
        DocumentReference df = fstore.collection("users").document(uid);

        // extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Uri imageuri;
                Log.d("Tag", "onSuccess:" + documentSnapshot.getData());
                usernametext.setText(documentSnapshot.getString("name"));
                useremailtaxt.setText(documentSnapshot.getString("email"));

                Toast.makeText(getApplicationContext(), "setup successfull", Toast.LENGTH_SHORT).show();

            }

        });

    }





    private void logoutUser() {
        mAuth.signOut();
        clearLoginState();
        startActivity(new Intent(userdashboard.this, loginactivity.class));
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
