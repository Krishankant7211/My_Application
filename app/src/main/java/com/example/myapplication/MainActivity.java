package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    ProgressBar ProgressBar;
    private Web3j web3j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));  // Replace with your Ganache RPC URL
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = findViewById(R.id.progressb);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            DocumentReference df = fstore.collection("users").document(currentUser.getUid());
            // extract the data from the document
            df.get().addOnSuccessListener(documentSnapshot -> {
                Log.d("Tag" , "onSuccess:" + documentSnapshot.getData());
                if (documentSnapshot.getString(" is_user ") != null ){
                    startActivity(new Intent(getApplicationContext(), userdashboard.class));
//                    yaha par progressbar ki visibility thi jaisi elseif wale main hai
                    finish();
                }else if (documentSnapshot.getString("is_admin") != null){
                    startActivity(new Intent(getApplicationContext(), userdashboard.class));
//                    ProgressBar.setVisibility(View.GONE);
                    finish();
                }
                else if (documentSnapshot.getString("is_verifier") != null){
                    startActivity(new Intent(getApplicationContext(), verifier_activity.class));
//                    ProgressBar.setVisibility(View.GONE);
                    finish();
                }

            });


        }else {
            startActivity(new Intent(MainActivity.this, loginactivity.class));
            ProgressBar.setVisibility(View.GONE);
            finish();
        }


    }
}

