package com.example.myapplication;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ViewDocumentsActivity extends AppCompatActivity {

    private ListView pdfListView;
    private ArrayList<String> pdfFileNames = new ArrayList<>();
    private ArrayList<String> pdfFileHashes = new ArrayList<>();
    private ArrayList<File> pdfFiles = new ArrayList<>();
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_documents);

        pdfListView = findViewById(R.id.pdfListView);

//        if (checkPermission()) {
            loadPdfFiles();
            setupListView();
//        } else {
            requestPermission();
//        }
    }

    private void loadPdfFiles() {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".pdf")) {
                    pdfFileNames.add(file.getName());
                    pdfFiles.add(file);
                    pdfFileHashes.add(generateHash(file.getName()));
                }
            }
        }
    }

    private void setupListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pdfFileNames )  ;
        pdfListView.setAdapter(adapter);

        pdfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPdf(position);
            }
        });
    }

    private void openPdf(int position) {
        File pdfFile = pdfFiles.get(position);
        try {
            // Use FileProvider to get a content URI
            Uri pdfUri = FileProvider.getUriForFile(this,
                    "com.example.myapplication.fileprovider", // Replace with your package name
                    pdfFile);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/ pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission to the PDF viewer
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            loadPdfFiles();
//            setupListView();
//        } else {
//            Toast.makeText(this, "Permission denied. Cannot access PDFs.", Toast.LENGTH_LONG).show();
//        }
//    }


    // This is the hash generator function
    public static String generateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Using SHA-256
            byte[] hashBytes = digest.digest(data.getBytes()); // Convert string to bytes & hash it

            // Convert byte array to a hexadecimal string
            BigInteger no = new BigInteger(1, hashBytes);
            String hash = no.toString(16);

            // Padding with leading zeros if required
            while (hash.length() < 64) {
                hash = "0" + hash;
            }

            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }





}