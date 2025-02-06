package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class welcome_activity extends AppCompatActivity {

    private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getStartedButton = findViewById(R.id.startButton);

        getStartedButton.setOnClickListener(view -> {
            markWelcomeSeen();
            startActivity(new Intent(welcome_activity.this, MainActivity.class));
            finish();
        });

        if (hasSeenWelcome()) {
            startActivity(new Intent(welcome_activity.this, MainActivity.class));
            finish();
        }
    }

    private boolean hasSeenWelcome() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("hasSeenWelcome", false);
    }

    private void markWelcomeSeen() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hasSeenWelcome", true);
        editor.apply();
    }
}
