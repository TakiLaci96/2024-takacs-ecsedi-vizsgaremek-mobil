package com.example.mobilapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class NewErrorActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText editTextErrorTitle;
    private EditText editTextErrorDescription;
    private EditText editTextErrorLocation;
    private Button buttonErrorPicture;
    private Button buttonErrorSubmit;
    private Button buttonErrorCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_error);
        init();

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

    public void init(){
        progressBar = findViewById(R.id.progressBar);
        editTextErrorTitle = findViewById(R.id.editTextErrorTitle);
        editTextErrorDescription = findViewById(R.id.editTextErrorDescription);
        editTextErrorLocation = findViewById(R.id.editTextErrorLocation);
        buttonErrorPicture = findViewById(R.id.buttonErrorPicture);
        buttonErrorSubmit = findViewById(R.id.buttonErrorSubmit);
        buttonErrorCancel = findViewById(R.id.buttonErrorCancel);
    }

    //TODO: Kép feltöltése
    //TODO: Hiba felvétele a szerverre
}