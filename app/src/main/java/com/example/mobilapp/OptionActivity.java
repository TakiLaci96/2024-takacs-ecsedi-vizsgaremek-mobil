package com.example.mobilapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OptionActivity extends AppCompatActivity {

    private Button buttonNewError;
    private Button buttonListMyErrors;
    private Button buttonListFixedErrors;
    private Button buttonContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_option);
        init();

        // Új hiba bejelentése gombra kattintás
        buttonNewError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, NewErrorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Saját hibák listázása gombra kattintás
        buttonListMyErrors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, MyErrorsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        buttonNewError = findViewById(R.id.buttonNewError);
        buttonListMyErrors = findViewById(R.id.buttonListMyErrors);
        buttonListFixedErrors = findViewById(R.id.buttonListFixedErrors);
        buttonContacts = findViewById(R.id.buttonContacts);
    }
}