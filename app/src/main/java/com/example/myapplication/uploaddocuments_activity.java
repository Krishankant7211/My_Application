package com.example.myapplication;

import android.content.Intent;

import java.math.BigInteger;
import java.security.MessageDigest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class uploaddocuments_activity extends AppCompatActivity {


    String hashofdata;
    String pdfFileName;
    private EditText Investigatorid;
    private EditText Investigatorname;
    private EditText candidatecontactinfo;
    private EditText candidatename;
    private EditText dateofinvestigation;
    private EditText typeofplacement;
    private EditText fathername;
    private EditText emailid;
    private EditText contactno;
    private EditText dateofbirth;
    private EditText address ;
    private EditText numberofinterviewround;
    private EditText interviewername;
    private EditText interviewfeedback;

    private EditText educationalbackground;
    private EditText companyname;
    private EditText positionoffered;
    private EditText dateofoffer;
    private EditText locationofplacement;
    private EditText typesofdocuments;
    private EditText descriptionofevidence;
    private Button  submit , makepdf;
    private TextView otherprojectD1;
    private TextView otherprojectD2;
    private TextView otherprojectvideo;
    private TextView otherformatdata;
    private TextView collectedbysign;
    private TextView varifiedbysign ;
    private EditText salaryoffered;
    private EditText banifitsincluded;
    private EditText contractduration;
    private EditText expwithplacementprocess;
    private EditText satisfactionwithoffer;
    private EditText additionalcomments;
    private EditText additionalinfo;

    private static final int opd1code = 700;
    private static final int opd2code = 800;
    private static final int opvideocode = 900;
    private static final int otherformatcode = 1000;
    private static final int collectedbycode = 1100;
    private static final int varifiedbycode = 1200;
    private static final int STORAGE_PERMISSION_CODE = 100;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaddocuments);

//        fields filled by the user
        Investigatorid = findViewById(R.id.investigatoid);
        Investigatorname = findViewById(R.id .investigatorname);
        typeofplacement = findViewById(R.id.typeofplacement);
        salaryoffered = findViewById(R.id.salaryoffered);
        banifitsincluded = findViewById(R.id.benifitsincluded);
        contractduration = findViewById(R.id.contractduration);
        expwithplacementprocess = findViewById(R.id.experiencewithplacementprocess);
        satisfactionwithoffer = findViewById(R.id.satisfactionwiththeoffer);
        additionalcomments = findViewById(R.id.additionalcomments);
        additionalinfo = findViewById(R.id.additonalinfo);
        typesofdocuments = findViewById(R.id.typeofdocument);
        candidatename = findViewById(R.id.candidatename);
        dateofinvestigation = findViewById(R.id .dateofinvestigation);
        candidatecontactinfo = findViewById(R.id.candidatecontactinfo);
        educationalbackground = findViewById(R.id.educationalbackground);
        descriptionofevidence = findViewById(R.id.descriptionofevidence);
        companyname = findViewById(R.id.companyname);
        positionoffered = findViewById(R.id.positionoffered);
        dateofoffer = findViewById(R.id.dateofoffer);
        locationofplacement = findViewById(R.id.locationofplacement);


//        documents to be uploaded
        otherprojectD1 = findViewById(R.id.otherprojectD1);
        otherprojectD2 = findViewById(R.id.otherprojectD2);
        otherprojectvideo = findViewById(R.id.otherprojectvideo);
        otherformatdata = findViewById(R.id.otherformatdata);
        collectedbysign = findViewById(R.id.collectedbysign);
        varifiedbysign = findViewById(R.id.varifiedbysign);

        makepdf = findViewById(R.id.makepdf);
        submit = findViewById(R.id.submit);
        hashofdata = generateHash(String.valueOf(Investigatorname));


        otherprojectD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iotherc = new Intent(Intent.ACTION_GET_CONTENT);
                Iotherc.setType("*/*");
                startActivityForResult(Iotherc, opd1code);
            }
        });
        otherprojectD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iotherc = new Intent(Intent.ACTION_GET_CONTENT);
                Iotherc.setType("*/*");
                startActivityForResult(Iotherc, opd2code);
            }
        });
        otherprojectvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iotherc = new Intent(Intent.ACTION_GET_CONTENT);
                Iotherc.setType("video/*");
                startActivityForResult(Iotherc, opvideocode);
            }
        });

        otherformatdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iotherc = new Intent(Intent.ACTION_GET_CONTENT);
                Iotherc.setType("*/*");
                startActivityForResult(Iotherc, otherformatcode);
            }
        });
        collectedbysign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iotherc = new Intent(Intent.ACTION_GET_CONTENT);
                Iotherc.setType("*/*");
                startActivityForResult(Iotherc, collectedbycode);
            }
        });

        varifiedbysign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iotherc = new Intent(Intent.ACTION_GET_CONTENT);
                Iotherc.setType("*/*");
                startActivityForResult(Iotherc, varifiedbycode);
            }
        });


        makepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Map<String, String> UserFormData = new HashMap<>(); // ✅ Initialize HashMap
//                UserFormData.put("General information ","");
//                UserFormData.put("Investigator Name " , Investigatorname.getText().toString());
//                UserFormData.put("Investigator Id ", Investigatorid.getText().toString());
//                UserFormData.put("Date of Investigation ", dateofinvestigation.getText().toString());
//                UserFormData.put("Type of Placement ", typeofplacement.getText().toString());
//                UserFormData.put("Candidate Details ","");
//                UserFormData.put("Candidate Name ",candidatename.getText().toString());
//                UserFormData.put("Candidate Contact Information ", candidatecontactinfo.getText().toString());
//                UserFormData.put("Educational Backgroung ", educationalbackground.getText().toString());
//                UserFormData.put("Placement Details ","");
//                UserFormData.put("Company Name ",companyname.getText().toString());
//                UserFormData.put("Position Offered ",positionoffered.getText().toString());
//                UserFormData.put("Date of Offer ",dateofoffer.getText().toString());
//                UserFormData.put("Location of Placement ",locationofplacement.getText().toString());
//                UserFormData.put("Document collection","");
////                yaha par documents wali files aayegi
//                UserFormData.put("Types of documents ",typesofdocuments.getText().toString());
//                UserFormData.put("Other Projects Document 1 ",otherprojectD1.getText().toString());
//                UserFormData.put("Other Projects Document 2 ",otherprojectD2.getText().toString());
//                UserFormData.put("Other Projects Video ",otherprojectvideo.getText().toString());
//                UserFormData.put("Other Format Data ",otherformatdata.getText().toString());
//                UserFormData.put("Collected By ",collectedbysign.getText().toString());
//                UserFormData.put("Varified By ",varifiedbysign.getText().toString());
//                UserFormData.put("Interview Process","");
//                UserFormData.put("Number of Interview Rounds",numberofinterviewround.getText().toString());
//                UserFormData.put("Interview's Name ",interviewername.getText().toString());
//                UserFormData.put("Interview Feedback ",interviewfeedback.getText().toString());
//                UserFormData.put("Offer Details ","");
//                UserFormData.put("Salary Offered " , salaryoffered.getText().toString());
//                UserFormData.put("Benefits Included " , banifitsincluded.getText().toString());
//                UserFormData.put("Contract Duration " , contractduration.getText().toString());
//                UserFormData.put(" Experience With the Placement Precess " , expwithplacementprocess.getText().toString());
//                UserFormData.put(" Satisfaction With the Offer " , satisfactionwithoffer.getText().toString());
//                UserFormData.put("Additional Comments " , additionalcomments.getText().toString());
//                UserFormData.put(" Additional Notes " , "");
//                UserFormData.put(" Additional Information " , additionalinfo.getText().toString());


                // Add more strings as needed

                // Create a unique file name with timestamp
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                pdfFileName = "Form_" + timeStamp + ".pdf";

                try {
                    // Define the directory to save the PDF (e.g., Downloads folder)
                    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(directory, pdfFileName);

                    // Initialize PDF writer
                    PdfWriter writer = new PdfWriter(file);
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf);

                    // Add content to the PDF
                    document.add(new Paragraph("Form Data"));
                    document.add(new Paragraph("Investigator name: " + Investigatorname.getText().toString()));
                    document.add(new Paragraph("Investigator id : " + Investigatorid.getText().toString()));
                    // Add more fields as needed

                    // Close the document
                    document.close();

                    Toast.makeText(getApplicationContext(), "Pdf saved to " + file.getAbsolutePath() , Toast.LENGTH_SHORT).show();
                    // Navigate back to dashboard
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error creating PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }



        });


    }
    @Override
    protected void onActivityResult(int requestcode, int resultcode, @Nullable Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (resultcode == RESULT_OK) {

            if (requestcode == opd1code) {
                assert data != null;
                String opd1 = Objects.requireNonNull(data.getData()).getPath();
            }
            else if (requestcode == opd2code) {
                assert data != null;
                String opd2 = Objects.requireNonNull(data.getData()).getPath();
            }
            else if (requestcode == opvideocode) {
                assert data != null;
                String opvideo = Objects.requireNonNull(data.getData()).getPath();
            }
            else if (requestcode == otherformatcode) {
                assert data != null;
                String otherformatdocument = Objects.requireNonNull(data.getData()).getPath();
            }
            else if (requestcode == collectedbycode) {
                assert data != null;
                String collectedby = Objects.requireNonNull(data.getData()).getPath();
            }
            else if (requestcode == varifiedbycode) {
                assert data != null;
                String varifiedby = Objects.requireNonNull(data.getData()).getPath();
            }


        }
    }




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