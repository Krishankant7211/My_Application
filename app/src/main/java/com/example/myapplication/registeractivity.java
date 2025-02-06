// RegisterActivity.java
package com.example.myapplication;

import android.content.Intent;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.web3j.crypto.WalletUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class registeractivity extends AppCompatActivity {
private final int  WALLET_FILE_REQUEST_CODE = 100;

    private EditText etEmail, etPassword , etName , etRepassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etName = findViewById(R.id.editTextName);
        etRepassword = findViewById(R.id.editTextRepassword);
        btnRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        etName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, WALLET_FILE_REQUEST_CODE);
            }
        });
    }


    public String createWallet(String password) {
        try {
            String walletDirectory = getFilesDir().getPath(); // Save the wallet file in your app's internal storage
            return WalletUtils.generateNewWalletFile(password, new File(walletDirectory));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private void registerUser() {
        final String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String repassword = etRepassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            sendEmailVerification(user);
                            saveUserToFirestore(user);
                            Credentials credentials = null;
                            try {
                                credentials = WalletUtils.loadCredentials(password, "path_to_wallet_file");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (CipherException e) {
                                throw new RuntimeException(e);
                            }
                            String userAddress = credentials.getAddress();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setEnabled(true);
                            Toast.makeText(registeractivity.this, "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WALLET_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            }
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(registeractivity.this,
                                    "Verification email sent. Please check your email.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());
        userMap.put("userId", user.getUid());
        userMap.put(" is_user ", "1");
        userMap.put("name", etName.getText().toString().trim());
        userMap.put("password", etPassword.getText().toString().trim());

        db.collection("users").document(user.getUid())
                .set(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);
                        if (task.isSuccessful()) {
                            startActivity(new Intent(registeractivity.this, userdashboard.class));
                            finish();
                        } else {
                            Toast.makeText(registeractivity.this, "Failed to save user data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}

