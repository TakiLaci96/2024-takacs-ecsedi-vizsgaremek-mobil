package com.example.mobilapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class NewErrorActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText editTextErrorTitle;
    private EditText editTextErrorDescription;
    private EditText editTextErrorLocation;
    private ImageView imageViewErrorPicture;
    private Button buttonErrorPictureUpload;
    private Button buttonErrorPictureCamera;
    private Button buttonErrorSubmit;
    private Button buttonErrorCancel;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_new_error);
        init();

        // Kép feltöltés gombra kattintás
        buttonErrorPictureUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
            }
        });

        // Kép készítés gombra kattintás
        buttonErrorPictureCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeaPhoto();
            }
        });



        // Hiba felvétel gombra kattintás, ellenőrzések
        buttonErrorSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorTitle = editTextErrorTitle.getText().toString();
                String errorDescription = editTextErrorDescription.getText().toString();
                String errorLocation = editTextErrorLocation.getText().toString();

                if (errorTitle.isEmpty() || errorDescription.isEmpty() || errorLocation.isEmpty()) {
                    Toast.makeText(NewErrorActivity.this,
                            "Minden mező kitöltése kötelező", Toast.LENGTH_SHORT).show();
                    return;
                } else if (errorTitle.length() < 5) {
                    Toast.makeText(NewErrorActivity.this,
                            "A hiba címe legalább 5 karakter hosszú kell legyen", Toast.LENGTH_SHORT).show();
                    return;
                } else if (errorDescription.length() < 10) {
                    Toast.makeText(NewErrorActivity.this,
                            "A hiba leírása legalább 15 karakter hosszú kell legyen", Toast.LENGTH_SHORT).show();
                    return;
                } else if (errorLocation.length() < 5) {
                    Toast.makeText(NewErrorActivity.this,
                            "A hiba helyszíne legalább 10 karakter hosszú kell legyen", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO: Hiba felvétele a szerverre
            }
        });

        buttonErrorCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewErrorActivity.this, OptionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    public void init() {
        progressBar = findViewById(R.id.progressBar);
        editTextErrorTitle = findViewById(R.id.editTextErrorTitle);
        editTextErrorDescription = findViewById(R.id.editTextErrorDescription);
        editTextErrorLocation = findViewById(R.id.editTextErrorLocation);
        imageViewErrorPicture = findViewById(R.id.imageViewErrorPicture);
        buttonErrorPictureUpload = findViewById(R.id.buttonErrorPictureUpload);
        buttonErrorPictureCamera = findViewById(R.id.buttonErrorPictureCamera);
        buttonErrorSubmit = findViewById(R.id.buttonErrorSubmit);
        buttonErrorCancel = findViewById(R.id.buttonErrorCancel);
    }

    private void takeaPhoto() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;

            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Hiba történt a kép betöltésekor", Toast.LENGTH_SHORT).show();
                }
            }

            if (imageBitmap != null) {
                imageViewErrorPicture.setImageBitmap(imageBitmap);
            }
        }
    }





}