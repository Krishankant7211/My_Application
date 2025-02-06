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

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.contracts.DataRegistry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;

public class userdashboard extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView dataTextView;
    private TextView usernametext , useremailtaxt;
    private Button logoutButton;
    private Button Document , newform , Getdata;
    private Web3j web3j;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private String uid, imageUri2 ;
    private BlockchainManager blockchainManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));  // Replace with your Ganache RPC URL
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
        logoutButton.setOnClickListener(v -> logoutUser());

        Getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlockchainManager blockchainManager = new BlockchainManager(
                        "https://127.0.0.1:7545", // Replace with your blockchain's RPC URL
                        "0x3E3507BD15f19c40B1ACc67105B24B3cE388668f" // Replace with your deployed contract address
                );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            List<DataRegistry.Document> data = blockchainManager.viewData();
                            String documentName = String.valueOf(data.get(0));
                            String documentHash = String.valueOf(data.get(1));
                            dataTextView.setText("Document Name: " + documentName + "\nDocument Hash: " + documentHash);
                        } catch (Exception e) {
                            e.printStackTrace();
                            dataTextView.setText("Error retrieving data");
                        }
                    }
                });
            }
        });

        // Save login state in shared preferences
        saveLoginState();
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
