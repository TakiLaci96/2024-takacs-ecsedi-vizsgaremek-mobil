package com.example.mobilapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;


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

    /*
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_error);
        init();

        // Kép feltöltés gombra kattintás
        buttonErrorPictureUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
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

    private void takeaPhoto() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 1);
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


    // Kép megjelenítése
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Ha a kamera visszatért
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // A kép adatainak lekérése
            Bundle extras = data.getExtras();
            // A kép megjelenítése
            Bitmap firstBitmap = (Bitmap) extras.get("data");
            String base64Bitmap = convertImageToBase64(firstBitmap);
            imageViewErrorPicture.setImageBitmap(firstBitmap);
            Bitmap secondBitmap = convertBase64ToImage(base64Bitmap);
            imageViewErrorPicture.setImageBitmap(secondBitmap);
        }
    }

    // Kép átalakítása base64-be
    private String convertImageToBase64(Bitmap bitmap) {
        // A kép átalakítása byte tömbbé, majd base64-be
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // A kép tömörítése
        // A tömörítési minőség 100%
        // A tömörítési formátum JPEG
        // A tömörített kép byte tömbbe való átalakítása
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        // A byte tömb átalakítása base64-be
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        // A base64 string visszaadása
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    // Kép átalakítása base64-ből
    private Bitmap convertBase64ToImage(String base64String) {
        // A base64 string átalakítása byte tömbbé
        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
        // A byte tömb átalakítása képpé
        // A kép visszaadása
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }


/*
    // Kép kiválasztásának eredménye
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            imageViewErrorPicture.setImageURI(selectedImage);
        }
    }

 */


    //TODO: Kép feltöltése
    //TODO: Hiba felvétele a szerverre
}