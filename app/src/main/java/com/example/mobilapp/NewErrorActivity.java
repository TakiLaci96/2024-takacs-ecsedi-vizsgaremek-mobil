package com.example.mobilapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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

import com.google.gson.Gson;

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
    private String base64Bitmap;
    private String requestUrl = "http://10.0.2.2:8000/api/store";
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

                // Hiba felvétel
                NewError error = new NewError(errorTitle, errorDescription, errorLocation, base64Bitmap);
                RequestTask task = new RequestTask(requestUrl, "POST", new Gson().toJson(error));
                task.execute();

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


    private class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }

        //doInBackground metódus létrehozása a kérés elküldéséhez
        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                if (requestType.equals("POST")) {
                    response = RequestHandler.post(requestUrl, requestParams);
                }
            } catch (IOException e) {
                Toast.makeText(NewErrorActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        //onPostExecute metódus létrehozása a válasz feldolgozásához
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(NewErrorActivity.this,
                        "Hiba történt a kérés feldolgozása során", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            }
            if (requestType.equals("POST")) {
                if (response.getResponseCode() == 201) {
                    Toast.makeText(NewErrorActivity.this,
                            "Sikeres hiba felvétel", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewErrorActivity.this, OptionActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
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
                base64Bitmap = convertImageToBase64(imageBitmap);
            }
        }
    }

    private String convertImageToBase64(Bitmap bitmap) {
        // A kép átalakítása base64-be
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
}