// LoginActivity.java
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class loginactivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        Button btnRegister = findViewById(R.id.buttonGoToRegister);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginactivity.this, registeractivity.class));
            }
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    checkuserrole(Objects.requireNonNull(authResult.getUser()).getUid());
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Login failed ", Toast.LENGTH_SHORT).show();
                    btnLogin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
    }

    private void checkuserrole(String uid) {
        DocumentReference df = fstore.collection("users").document(uid);
        // extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("Tag" , "onSuccess:" + documentSnapshot.getData());
                if (documentSnapshot.getString(" is_user ") != null ){
                    startActivity(new Intent(getApplicationContext(), userdashboard.class));
                    Toast.makeText(getApplicationContext(), "logged in successfully", Toast.LENGTH_SHORT).show();
                    btnLogin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    finish();
                }else if (documentSnapshot.getString("is_admin") != null){
                    startActivity(new Intent(getApplicationContext(), AdminDashboard_activity.class));
                    Toast.makeText(getApplicationContext(), "logged in successfully", Toast.LENGTH_SHORT).show();
                    btnLogin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
                else if (documentSnapshot.getString("is_verifier") != null){
                    startActivity(new Intent(getApplicationContext(), verifier_activity.class));
                    Toast.makeText(getApplicationContext(), "logged in successfully", Toast.LENGTH_SHORT).show();
                    btnLogin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    finish();
                }

            }
        });

    }
}